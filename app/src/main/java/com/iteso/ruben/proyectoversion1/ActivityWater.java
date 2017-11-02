package com.iteso.ruben.proyectoversion1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityWater extends AppCompatActivity {

    public TextView tv, tv_left;
    public ProgressBar pBar;
    protected ImageButton back_button;
    protected Button myDrink_button;
    int pStatus = 0;
    static int myProgressMl = 0;
    final static int topMl = 2000;
    final static int glasspMl = 250;

    boolean flag = false;
    protected Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        tv = (TextView) findViewById(R.id.textView_water);
        pBar = (ProgressBar) findViewById(R.id.progressBar_water);
        back_button = (ImageButton) findViewById(R.id.activity_water_back_button);
        myDrink_button = (Button) findViewById(R.id.myDrink_button);
        tv_left = (TextView) findViewById(R.id.text_waterprogr);



        pBar.setProgress(0);
        myDrink_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                myDrink_button.setClickable(false);
                myDrink_button.setEnabled(false);
                back_button.setEnabled(false);
                back_button.setClickable(false);

                new ActivityWater.UpdateProgress().execute(myProgressMl + glasspMl);
            }
        });

        myDrink_button.setText("+1 Glass (" + glasspMl + " ml)");



        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent select_intent = new Intent(ActivityWater.this, ActivityHome.class);
                //startActivity(select_intent);
                finish();
            }
        });

        UpdateProgress pBarProgress = new UpdateProgress();
        pBarProgress.setEnteringActivity(true);
        pBarProgress.execute(myProgressMl);

    }

    class UpdateProgress extends AsyncTask<Integer, Integer, Void> {
        int progress;
        boolean isEnteringActivity;
        @Override
        protected void onPostExecute(Void result) {
            myDrink_button.setClickable(true);
            myDrink_button.setEnabled(true);
            back_button.setClickable(true);
            back_button.setEnabled(true);
            pBar.setClickable(true);
            pBar.setEnabled(true);

            if(!isEnteringActivity)
                myProgressMl += glasspMl;
            setEnteringActivity(false);

            if(myProgressMl >= topMl){
                myProgressMl = 0;
                tv.setText(R.string.water_progress_complete_message);
                tv.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
        @Override
        protected void onPreExecute() {

            progress = (isEnteringActivity) ? 0 : myProgressMl*100/topMl;
            pBar.setClickable(false);
            pBar.setEnabled(false);
            pBar.setProgress(progress);
            tv.setText(R.string.water_progress_incomplete_message);
            tv.setTextColor(getResources().getColor(R.color.white));
        }
        protected void onProgressUpdate(Integer... values) {
            pBar.setProgress(values[0]);
            tv_left.setText(values[0]*topMl/100 + " ml /" + topMl + " ml");

        }
        @Override
        protected Void doInBackground(Integer... arg0) {

            while (progress < arg0[0]*100/topMl) {
                progress++;
                publishProgress(progress);
                SystemClock.sleep(10);
            }
            return null;
        }

        public boolean isEnteringActivity() {
            return isEnteringActivity;
        }

        public void setEnteringActivity(boolean enteringActivity) {
            isEnteringActivity = enteringActivity;
        }
    }


}
