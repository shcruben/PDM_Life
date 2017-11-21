package com.iteso.ruben.proyectoversion1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.iteso.ruben.proyectoversion1.Utils.ImageProcessing;
import com.iteso.ruben.proyectoversion1.beans.Constants;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.iteso.ruben.proyectoversion1.ActivityHeartRateMonitor.COLOR_TYPE.GREEN;

public class ActivityHeartRateMonitor extends AppCompatActivity {

    private static final String APP_TAG = "HeartRateMonitor";
    private AtomicBoolean isProcessing = new AtomicBoolean(false);

    protected SurfaceView preview;
    protected SurfaceHolder previewHolder;
    protected Camera camera;
    protected View image;
    protected TextView text;

    protected PowerManager.WakeLock wakeLock = null;


    public enum COLOR_TYPE{
        GREEN, RED
    };

    protected static COLOR_TYPE currentType = COLOR_TYPE.GREEN;
    private double beats = 0;
    private static final int beatsArraySize = 3;
    private int[] beatsArray = new int[beatsArraySize];
    int beatsIndex = 0;

    private long startTime = 0;


    private int averageIndex = 0;
    private static final int averageArraySize = 4;
    private int[] averageArray = new int[averageArraySize];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_monitor);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WAKE_LOCK
            }, Constants.USE_PERMISSION_TAG);
        }else{
            setWakeLock();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, Constants.USE_PERMISSION_TAG);
        }

        onResume();

        preview = (SurfaceView) findViewById(R.id.activity_heart_rate_preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);

        image = findViewById(R.id.activity_heart_rate_heartBeatView);
        text = (TextView) findViewById(R.id.activity_heart_rate_rate);


    }

    private void setWakeLock(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }
    @Override
    protected void onResume() {
        super.onResume();

        wakeLock.acquire();

        camera = getCameraInstance();

        startTime = System.currentTimeMillis();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.USE_PERMISSION_TAG:
                int isPermissionGranted = PackageManager.PERMISSION_GRANTED;
                for(int permission : grantResults){
                    isPermissionGranted = (isPermissionGranted == PackageManager.PERMISSION_GRANTED &&
                            permission == PackageManager.PERMISSION_GRANTED) ?
                            PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
                }
                if(isPermissionGranted != PackageManager.PERMISSION_GRANTED){
                    finish();
                }else{
                    setWakeLock();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public static COLOR_TYPE getCurrentType(){ return currentType; }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback(){

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try{
                camera.setPreviewDisplay(surfaceHolder);
                camera.setPreviewCallback(previewCallback);
            }catch(Exception e){
                Log.e(APP_TAG, "Exception in setPreviewDisplay()");
            }
        }
        
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(APP_TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    private Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters){
        Camera.Size result = null;

        for(Camera.Size size : parameters.getSupportedPreviewSizes()){
            if(size.width <= width && size.height <= height){
                if(result == null){
                    result = size;
                }else{
                    int resultArea = result.width*result.height;
                    int newArea = size.width*size.height;

                    result = (newArea < resultArea) ? size : result;
                }
            }
        }

        return result;
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!isProcessing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            if (imgAvg == 0 || imgAvg == 255) {
                isProcessing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            COLOR_TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = COLOR_TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = COLOR_TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    isProcessing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                text.setText(String.valueOf(beatsAvg));
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            isProcessing.set(false);
        }
    };

    public static Camera getCameraInstance(){
        Camera camera = null;

        try{
            camera = Camera.open();
        }catch(Exception e){
            Log.e(APP_TAG, "Cannot open camera");
        }

        return camera;
    }

}
