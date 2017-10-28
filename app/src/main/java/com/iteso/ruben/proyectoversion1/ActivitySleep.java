package com.iteso.ruben.proyectoversion1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.iteso.ruben.proyectoversion1.R.id.load_button;

public class ActivitySleep extends AppCompatActivity {

    public TextView tv;
    public ProgressBar pBar;
    protected Button back_button;
    protected Button load_button;
    int pStatus = 0;
    int MaxSleep = 12;
    boolean flag = false;
    protected Handler handler = new Handler();
    public NumberPicker realSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        tv = (TextView) findViewById(R.id.text_sleeprogr);
        pBar = (ProgressBar) findViewById(R.id.progressBar_sleep);
        realSleep = (NumberPicker) findViewById(R.id.sleep_input);
        back_button = (Button) findViewById(R.id.activity_main_back_button);
        load_button = (Button) findViewById(R.id.load_button);

        realSleep.setMinValue(0);
        realSleep.setMaxValue(12);
        realSleep.setValue(8);

        pBar.setProgress(0);
        load_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                load_button.setClickable(false);
                new UpdateProgress().execute(realSleep.getValue());
            }
        });



        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent select_intent = new Intent(ActivitySleep.this, ActivityHome.class);
                //startActivity(select_intent);
                finish();
            }
        });

    }

    class UpdateProgress extends AsyncTask<Integer, Integer, Void> {
        int progress;
        @Override
        protected void onPostExecute(Void result) {
            load_button.setClickable(true);
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
        protected void onProgressUpdate(Integer... values) {
            pBar.setProgress(values[0]);
            tv.setText(values[0] + "% /" + pBar.getMax()+"%");
        }
        @Override
        protected Void doInBackground(Integer... arg0) {
            while (progress < arg0[0]*10) {
                progress++;
                publishProgress(progress);
                SystemClock.sleep(5);
            }
            return null;
        }
    }



}

