package com.iteso.ruben.proyectoversion1.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.iteso.ruben.proyectoversion1.ActivityHeartRateMonitor;
import com.iteso.ruben.proyectoversion1.ActivityHeartRateMonitor.*;
import com.iteso.ruben.proyectoversion1.R;

import static android.support.v7.appcompat.R.styleable.View;

/**
 * Created by Usuario on 26/10/2017.
 */

public class HeartBeatView extends View {

    protected Paint paint;
    protected Bitmap greenBitmap, redBitmap;
    protected Matrix matrix;
    protected int parentWidth, parentHeight;

    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.green_icon);
        redBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.red_icon);
        matrix = new Matrix();
    }

    public HeartBeatView(Context context){
        super((context));

        init();
    }

    public HeartBeatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap;
        COLOR_TYPE colorType = ActivityHeartRateMonitor.getCurrentType();

        if(canvas == null) throw new NullPointerException();

        switch (colorType){
            case GREEN:
                bitmap = greenBitmap;
                break;
            case RED:
                bitmap = redBitmap;
                break;
            default:
                bitmap = greenBitmap;
                break;
        }

        int bitmapX = bitmap.getWidth() / 2;
        int bitmapY = bitmap.getHeight() / 2;

        int parentX = parentWidth / 2;
        int parentY = parentHeight / 2;

        int centerX = parentX - bitmapX;
        int centerY = parentY - bitmapY;

        matrix.reset();
        matrix.postTranslate(centerX, centerY);
        canvas.drawBitmap(bitmap, matrix, paint);

    }
}
