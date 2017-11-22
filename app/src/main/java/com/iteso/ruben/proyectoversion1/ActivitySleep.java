package com.iteso.ruben.proyectoversion1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iteso.ruben.proyectoversion1.beans.Constants;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivitySleep extends AppCompatActivity {

    private String mAccessToken;
    public TextView tv;
    public ProgressBar pBar;
    protected ImageButton back_button;
    protected ImageButton graphButton;
    protected Button load_button;
    private  boolean isSleepRegistered;
    private long wakeUpTime;
    private SharedPreferences sharedPreferences;
    private long sleepTime;
    int pStatus = 0;
    int MaxSleep = 8;
    private int age = 23;
    boolean flag = false;
    private long lastConnection;
    protected Handler handler = new Handler();
    public NumberPicker realSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES,
                MODE_PRIVATE);

        isSleepRegistered = sharedPreferences.getBoolean("sleepRegistered", false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }


        if(isDiffDay(lastConnection, System.currentTimeMillis())){
            //myProgressMl = 0;
        }



        tv = (TextView) findViewById(R.id.text_sleeprogr);
        pBar = (ProgressBar) findViewById(R.id.progressBar_sleep);
        back_button = (ImageButton) findViewById(R.id.activity_sleep_back_button);
        graphButton = (ImageButton) findViewById(R.id.activity_sleep_graph);
        load_button = (Button) findViewById(R.id.load_button);
        age = sharedPreferences.getInt("age", 23);
        load_button.setText(isSleepRegistered ? "Stop Sleeping" : "Start Sleeping");

        pBar.setProgress(0);

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySleep.this, ActivitySleepGraph.class);

                startActivity(intent);

            }
        });

        load_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(isSleepRegistered) {
                    load_button.setText("Start sleeping");
                    editor.putBoolean("sleepRegistered", false);

                    wakeUpTime = System.currentTimeMillis()/1000;
                    sleepTime = sharedPreferences.getLong("timeOfSleep",
                            sleepTime)-3600*8;

                    editor.putLong("timeOfWakeUp", wakeUpTime);

                    ApiManager.getRestApiInterface().createSleepEvent(
                            UpPlatformSdkConstants.API_VERSION_STRING,
                            getCreateSleepEventRequestParams(),
                            genericCallbackListener
                    );

                    isSleepRegistered = false;

                    editor.commit();
                    new UpdateProgress().execute((int) ( (wakeUpTime - sleepTime) / 3600 ) );
                }else {
                    load_button.setText("Stop sleeping");
                    sleepTime = System.currentTimeMillis()/1000;
                    editor.putLong("timeOfSleep", sleepTime);
                    editor.putBoolean("sleepRegistered", true);
                    editor.commit();
                    isSleepRegistered = true;
                }
            }
        });



        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        MaxSleep = (int) ( (age < 13 ? 10 :
                            age < 17 ? 9:
                                    8
        ));

    }

    private HashMap<String,Object> getCreateSleepEventRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();
        queryHashMap.put("time_created",  sleepTime);
        queryHashMap.put("time_completed", wakeUpTime);
        TimeZone timeZone = TimeZone.getDefault();
        queryHashMap.put("tz",  timeZone.getID());
        queryHashMap.put("share" , false);
        return queryHashMap;
    }


    static int dayOfMonth( long epoch ){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(epoch);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return day;
    }

    static boolean isDiffDay(long epoch1, long epoch2){

        int day1 = dayOfMonth(epoch1);
        int day2 = dayOfMonth(epoch2);

        return (day1 != day2);

    }

    class UpdateProgress extends AsyncTask<Integer, Integer, Void> {
        int progress;
        @Override
        protected void onPostExecute(Void result) {
            load_button.setClickable(true);
            back_button.setClickable(true);
            pBar.setClickable(true);
            pBar.setEnabled(true);
        }
        @Override
        protected void onPreExecute() {
            progress = 0;
            pBar.setClickable(false);
            pBar.setEnabled(false);
            pBar.setProgress(0);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            pBar.setProgress(values[0]);
            tv.setText(values[0] + "% /" + pBar.getMax() +"%");
        }
        @Override
        protected Void doInBackground(Integer... arg0) {
            while (progress < arg0[0] * 100 / MaxSleep) {
                progress++;
                publishProgress(progress);
                SystemClock.sleep(5);
            }
            return null;
        }
    }

    //TODO the callbacks are not yet backed by data model, but will get json response,
    //TODO which for now is logged to console
    private Callback genericCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}

