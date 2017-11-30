package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.iteso.ruben.proyectoversion1.beans.Constants;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ActivityWater extends AppCompatActivity {


    public TextView tv, tv_left, tv_completions;
    public ProgressBar pBar;
    protected ImageButton back_button;
    protected Button myDrink_button;
    private int myProgressMl = 0;
    private double weight = 0;
    private int age = 0;
    private int topMl = 2000;
    final static int glasspMl = 250;
    private long lastConnection;
    private static int happiness = 0;
    private static int completions = 0;
    private static boolean isCompletedToday = false;

    public static int getHappiness() {
        return happiness;
    }

    public static void setHappiness(int happiness) {
        ActivityWater.happiness = happiness;
    }

    public static void increaseHappiness(int inc){
        ActivityWater.happiness += inc;
    }

    public static int getCompletions() {
        return completions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES, Context.MODE_PRIVATE);
        myProgressMl =   sharedPreferences.getInt("waterDrank",myProgressMl );
        lastConnection = sharedPreferences.getLong("lastConnection",lastConnection );
        completions = sharedPreferences.getInt("completions", completions);
        isCompletedToday = sharedPreferences.getBoolean("isCompletedToday", isCompletedToday);



        weight = (double) sharedPreferences.getFloat("weight", 10 );
        age = sharedPreferences.getInt("age", 23);
        tv = (TextView) findViewById(R.id.textView_water);
        pBar = (ProgressBar) findViewById(R.id.progressBar_water);
        back_button = (ImageButton) findViewById(R.id.activity_water_back_button);
        myDrink_button = (Button) findViewById(R.id.myDrink_button);
        tv_left = (TextView) findViewById(R.id.text_waterprogr);
        tv_completions = (TextView)findViewById(R.id.activity_water_textview_completions);

        topMl = (int) (weight * (age < 30 ? 0.03 :
                                age < 55 ? 0.025:
                                0.02 ) * 1000.0);

        if(isDiffDay(lastConnection, System.currentTimeMillis())){
            sharedPreferences.edit().putInt("water_mood",
                    (int) ((float) myProgressMl/ (float) topMl * 5) );
            myProgressMl = 0;
            isCompletedToday = false;
            sharedPreferences.edit().putBoolean("isCompletedToday", false);

        }


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

        String completions_string = getResources()
                .getString(R.string.activity_water_completions_default_text) +
                Integer.toString(completions);
        tv_completions.setText(completions_string);

        UpdateProgress pBarProgress = new UpdateProgress();
        pBarProgress.setEnteringActivity(true);
        pBarProgress.execute(myProgressMl);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("waterDrank", myProgressMl);
        editor.putLong("lastConnection",System.currentTimeMillis());
        editor.putInt("completions", completions);
        editor.putBoolean("isCompletedToday", isCompletedToday);
        editor.commit();


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
        boolean isEnteringActivity;
        @Override
        protected void onPostExecute(Void result) {
            myDrink_button.setClickable(true);
            myDrink_button.setEnabled(true);
            back_button.setClickable(true);
            back_button.setEnabled(true);
            pBar.setClickable(true);
            pBar.setEnabled(true);

            if(!isEnteringActivity) {
                myProgressMl += glasspMl;
                increaseHappiness(+2);
            }
            setEnteringActivity(false);

            if(myProgressMl >= topMl){
                if(!isCompletedToday) {
                    tv.setText(R.string.water_progress_complete_message);
                    tv.setTextColor(getColor(R.color.colorAccent));
                    completions++;
                    String completions_string = getResources()
                            .getString(R.string.activity_water_completions_default_text) +
                            Integer.toString(completions);
                    tv_completions.setText(completions_string);
                }
                isCompletedToday = true;
                Toast.makeText(ActivityWater.this, R.string.too_much_water_toast , Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onPreExecute() {

            progress = (isEnteringActivity) ? 0 : myProgressMl*100/topMl;
            pBar.setClickable(false);
            pBar.setEnabled(false);
            pBar.setProgress(progress);
            tv.setText(R.string.water_progress_incomplete_message);
            tv.setTextColor(getColor(R.color.white));
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
