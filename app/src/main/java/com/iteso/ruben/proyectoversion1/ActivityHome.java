package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.iteso.ruben.proyectoversion1.beans.Constants;
import com.iteso.ruben.proyectoversion1.beans.UserData;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.jjoe64.graphview.series.DataPoint;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ActivityHome extends AppCompatActivity {

    protected CoordinatorLayout coordinatorLayout;
    protected FloatingActionButton floatingActionButton;
    protected FloatingActionButton fab1,fab2,fab3,fab4,fab5 ;
    protected Animation fabOpen;
    protected Animation fabClose;
    protected int mood = 0;
    protected int moodS = 0;
    protected int moodM = 0;
    protected int wmood= 5;
    protected boolean isSubmenuShown;
    private ImageView img;
    private static int daysConnected = 0;
    private long lastTimeHome = 0;
    private double week_hours = 0;
    private double weekSteps = 0;
    private boolean isCallbackDoneS;
    private boolean isCallbackDoneM;
    private String mAccessToken;
    protected Runnable animating;


    public static int getDaysConnected() {
        return daysConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);
        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }

        wmood = sharedPreferences.getInt("water_mood",wmood);

        ApiManager.getRestApiInterface().getSleepEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                null,
                sleepEventListCallbackListener);
        ApiManager.getRestApiInterface().getMoveEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                null,
                MoveEventListCallbackListener);

        while(isCallbackDoneM && isCallbackDoneS != true);
        mood = (moodM+moodS + wmood)/3;


        daysConnected = sharedPreferences.getInt("daysConnected", daysConnected);
        lastTimeHome = sharedPreferences.getLong("lastTimeHome", lastTimeHome);

        img = (ImageView) findViewById(R.id.happy_hamtaro);

        animating = new Runnable(){
            @Override
            public void run() {
                ChangeAnimation(mood);
                (
                        (AnimationDrawable) img.getBackground()).start();
            }
        };


        setAnimating();


        isSubmenuShown = false;
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main_coordinator);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_coordinator_floating);
        fab1 = (FloatingActionButton) findViewById(R.id.activity_floating1);
        fab2 = (FloatingActionButton) findViewById(R.id.activity_floating2);
        fab3 = (FloatingActionButton) findViewById(R.id.activity_floating3);
        fab4 = (FloatingActionButton) findViewById(R.id.activity_floating4);
        fab5 = (FloatingActionButton) findViewById(R.id.activity_achievement);


        fabOpen = AnimationUtils.loadAnimation(this,R.anim.file_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        if(floatingActionButton == null)
            Log.e("Null pointer", "floatingActionButton is null");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSubmenuShown){
                    Interpolator interpol = AnimationUtils.loadInterpolator(getBaseContext(),android.R.interpolator.fast_out_slow_in);
                    view.animate().rotation(45f).setInterpolator(interpol).start();
                    fab1.startAnimation(fabOpen);
                    fab2.startAnimation(fabOpen);
                    fab3.startAnimation(fabOpen);
                    fab4.startAnimation(fabOpen);
                    fab5.startAnimation(fabOpen);
                }
                else {
                    Interpolator interpol = AnimationUtils.loadInterpolator(getBaseContext(),android.R.interpolator.fast_out_slow_in);
                    view.animate().rotation(0).setInterpolator(interpol).start();
                    fab1.startAnimation(fabClose);
                    fab2.startAnimation(fabClose);
                    fab3.startAnimation(fabClose);
                    fab4.startAnimation(fabClose);
                    fab5.startAnimation(fabClose);
                }
                fab1.setClickable(!isSubmenuShown);
                fab2.setClickable(!isSubmenuShown);
                fab3.setClickable(!isSubmenuShown);
                fab4.setClickable(!isSubmenuShown);
                fab5.setClickable(!isSubmenuShown);
                isSubmenuShown = !isSubmenuShown;
            }
        });

        fab1.bringToFront();
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityHome.this, ActivityHeartRateMonitor.class);
                //intent.putExtra("USER", myUser);
                startActivity(intent);
            }
        });
        fab2.bringToFront();
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityHome.this, ActivityWater.class);
                //intent.putExtra("USER", myUser);
                startActivity(intent);
            }
        });
        fab3.bringToFront();
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityHome.this, ActivitySleep.class);
                //intent.putExtra("USER", myUser);
                startActivity(intent);

            }
        });
        fab4.bringToFront();
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityHome.this, ActivityMove.class);
                //intent.putExtra("USER", myUser);
                startActivity(intent);

            }
        });

        fab5.bringToFront();
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityHome.this, ActivityAchievement.class);
                //intent.putExtra("USER", myUser);
                startActivity(intent);

            }
        });



    }
    public UserData loadPreferences(){
        return new UserData().getUserData(ActivityHome.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mood = 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);
        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }


        ApiManager.getRestApiInterface().getSleepEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                null,
                sleepEventListCallbackListener);
        ApiManager.getRestApiInterface().getMoveEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                null,
                MoveEventListCallbackListener);

        while(isCallbackDoneM && isCallbackDoneS != true);
        mood = (moodM+moodS + wmood)/3;

        setAnimating();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastTimeHome", System.currentTimeMillis());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.activity_main_menu_settings:
                Intent intent = new Intent(ActivityHome.this, ActivityGetUserDAta.class);
                //intent.putExtra("USER", myUser);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private Callback sleepEventListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            LinkedTreeMap<String, LinkedTreeMap> received = (LinkedTreeMap) o;
            LinkedTreeMap<String, ArrayList> data = received.get("data");
            ArrayList<LinkedTreeMap<String, Object>> items = data.get("items");

            for (LinkedTreeMap<String, Object> l : items) {
                Log.e("MoveEventList ", (String) l.get("title"));
                Double timeCreated = (Double) l.get("time_created");
                Double timeCompleted = (Double) l.get("time_completed");
                Double totalSleep = (timeCompleted - timeCreated) / 3600;
                week_hours = (Double) totalSleep + week_hours ;
            }
            moodS = SleepMood(week_hours);
            isCallbackDoneS = true;


        }
        @Override
        public void failure(RetrofitError retrofitError) {
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    private Callback MoveEventListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            LinkedTreeMap<String, LinkedTreeMap> received = (LinkedTreeMap) o;
            LinkedTreeMap<String, ArrayList> data = received.get("data");
            ArrayList< LinkedTreeMap <String, Object> > items = data.get("items");

            for (LinkedTreeMap<String, Object> l : items) {
                Log.e("MoveEventList ",  (String) l.get("title"));
                LinkedTreeMap<String, Double> detail =  (LinkedTreeMap<String, Double>) l.get("details");
                weekSteps = (Double) detail.get("steps") + weekSteps ;
            }
            moodM = StepsMood(weekSteps);
            isCallbackDoneM = true;

        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };



    public int StepsMood(double steps){
        int step_mood;

        step_mood = (steps >= 75000)?5: (steps >= 60000)?4:
                (steps >= 45000)?3: (steps >= 30000)?2:1;

        return step_mood;
    }

    public int SleepMood(double hours){
        int sleep_mood;

        sleep_mood = (hours >= 80)?5: (hours >= 64)?4:
                (hours >= 48)?3: (hours >= 32)?2:1;

        return sleep_mood;
    }

    public void ChangeAnimation(int mood) {
        switch (mood) {
            case 5:
                img.setBackground(getDrawable(R.drawable.excited));
                break;
            case 4:
                img.setBackground(getDrawable(R.drawable.bumped_up));
                break;
            case 3:
                img.setBackground(getDrawable(R.drawable.eat_happy));
                break;
            case 2:
                img.setBackground(getDrawable(R.drawable.worried));
                break;
            case 1:
                img.setBackground(getDrawable(R.drawable.sad));
                break;
            case 0:
                img.setBackground(getDrawable(R.drawable.excited));
                break;
            default:
                img.setBackground(getDrawable(R.drawable.happy));

                break;
        }

    }

    public synchronized void setAnimating(){
        img.post(animating);
    }


}