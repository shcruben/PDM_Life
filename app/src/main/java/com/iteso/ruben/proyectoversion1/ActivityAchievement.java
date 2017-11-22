package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.iteso.ruben.proyectoversion1.beans.Constants;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityAchievement extends AppCompatActivity {

    private String mAccessToken;
    private double week_hours = 0;
    private double weekSteps = 0;
    private ListView listView;
    private ArrayList<Double> sleepsTime;
    private ArrayList<DataPoint> steps;
    protected ImageButton back_button;
    protected boolean trophySleep;
    protected boolean trophySteps;
    protected double todaySteps;
    protected double todayHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        listView = (ListView) findViewById(R.id.list);
        back_button = (ImageButton) findViewById(R.id.activity_a_back_button);

        Resources res = getResources();
        String[] goal = res.getStringArray(R.array.myGoals);

        MyAchieveAdapter adapter = new MyAchieveAdapter(this, goal);
        listView.setAdapter(adapter);



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


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent select_intent = new Intent(ActivitySleep.this, ActivityHome.class);
                //startActivity(select_intent);
                finish();
            }
        });
       // ArrayAdapter adapter = new ArrayAdapter<String>(this,
       // R.layout.achieves_list_item, R.id.textView1,goal);

      //  ListView listView = (ListView) findViewById(R.id.achieve_cards);
       // listView.setAdapter(adapter);
    }
/* @Override
    protected void onListItemClick(ListView l, View v,int pos, long id){
        String item = (String) getListAdapter().getItem(pos);
        Toast.makeText(this,item+"selected",Toast.LENGTH_LONG).show();
    }*/
private Callback sleepEventListCallbackListener = new Callback<Object>() {
    @Override
    public void success(Object o, Response response) {
        // Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
        LinkedTreeMap<String, LinkedTreeMap> received = (LinkedTreeMap) o;
        LinkedTreeMap<String, ArrayList> data = received.get("data");
        ArrayList<LinkedTreeMap<String, Object>> items = data.get("items");

        sleepsTime = new ArrayList<>();
        double i = 0;


        for (LinkedTreeMap<String, Object> l : items) {
            Log.e("MoveEventList ", (String) l.get("title"));

            Double timeCreated = (Double) l.get("time_created");
            Double timeCompleted = (Double) l.get("time_completed");
            Double totalSleep = (timeCompleted - timeCreated) / 3600;
            Date date = new Date((long) (timeCompleted * 1000));
            week_hours = (Double) totalSleep + week_hours ;
            if(i == 0){todayHours =  totalSleep; }
            sleepsTime.add(totalSleep);
            i++;
        }

       // Toast.makeText(getApplicationContext(), (AchieveSleep(week_hours))?"yees " +week_hours:"NOOO "+ week_hours , Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        trophySleep = AchieveSleep(week_hours);
        editor.putBoolean("hoursSleepComplete",trophySleep);
        editor.putLong("todayHours",(long) todayHours);
        editor.apply();
        Toast.makeText(getApplicationContext(), (trophySleep)?"yees trphy sleep" +(week_hours*100/70) :"NOOO sleep" +(week_hours*100/70) , Toast.LENGTH_LONG).show();
    }
    @Override
    public void failure(RetrofitError retrofitError) {
        Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
    }
};


    private Callback MoveEventListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            //Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
            LinkedTreeMap<String, LinkedTreeMap> received = (LinkedTreeMap) o;
            LinkedTreeMap<String, ArrayList> data = received.get("data");
            ArrayList< LinkedTreeMap <String, Object> > items = data.get("items");

            steps = new ArrayList<>();

            double i = 0;

            for (LinkedTreeMap<String, Object> l : items) {
                Log.e("MoveEventList ",  (String) l.get("title"));
                LinkedTreeMap<String, Double> detail =  (LinkedTreeMap<String, Double>) l.get("details");
                steps.add(new DataPoint(i , detail.get("steps")) );
                weekSteps = (Double) detail.get("steps") + weekSteps ;
                if(i == 0){todaySteps = detail.get("steps"); }
                i++;
            }

            steps.add(steps.get(steps.size() - 1));

            DataPoint[] stepsDP = new DataPoint[steps.size()];

            steps.toArray(stepsDP);

            SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            trophySteps = AchieveSteps(weekSteps);
            editor.putLong("todaySteps", (long) todaySteps);
            editor.putBoolean("stepsComplete",trophySteps);
            editor.apply();
            Toast.makeText(getApplicationContext(), (trophySteps)?"yees trphy steps " +(weekSteps*100/75000):"NOOO steps "+(weekSteps*100/75000) , Toast.LENGTH_LONG).show();

        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public boolean AchieveSleep(double hours){
        boolean nice;

        nice = (hours >= 70)? true : false;

        return nice;
    }

    public boolean AchieveSteps(double steps){
        boolean nice;

        nice = (steps >= 75000)? true : false;

        return nice;
    }
}




