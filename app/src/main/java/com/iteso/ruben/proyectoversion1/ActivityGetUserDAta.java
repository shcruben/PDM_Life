package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iteso.ruben.proyectoversion1.beans.Constants;
import com.iteso.ruben.proyectoversion1.beans.UserData;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import java.sql.Time;

public class ActivityGetUserDAta extends AppCompatActivity {

    private UserData myUser;
    protected NumberPicker weight, height, age, levelExercise;
    protected TimePicker wakeUp;
    protected Button save;
    protected ImageButton back_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_data);

        myUser = new UserData();
        levelExercise = (NumberPicker) findViewById(R.id.exercise_level_data);
        weight = (NumberPicker) findViewById(R.id.weight_data);
        height = (NumberPicker) findViewById(R.id.height_data);
        age = (NumberPicker) findViewById(R.id.age_data);
        wakeUp = (TimePicker) findViewById(R.id.waketime_data);
        save = (Button) findViewById(R.id.save_user_data);
        back_button = (ImageButton) findViewById(R.id.activity_back_button2);

        weight.setMinValue(30);
        weight.setMaxValue(150);
        weight.setValue(60);
        height.setMinValue(50);
        height.setMaxValue(220);
        height.setValue(160);
        age.setMinValue(8);
        age.setMaxValue(130);
        age.setValue(18);

        Resources res = getResources();
        String[] Level = res.getStringArray(R.array.Exercise_level);
        levelExercise.setMinValue(0);
        levelExercise.setMaxValue(Level.length-1);
        levelExercise.setValue(1);
        levelExercise.setDisplayedValues(Level);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myUser.setAge(age.getValue());
                myUser.setHeight(height.getValue());
                myUser.setWeight(weight.getValue());
                myUser.setWakeuptime_hrs(wakeUp.getHour());
                myUser.setWakeuptime_min(wakeUp.getMinute());
                myUser.setLevelOfExcercise(levelExercise.getValue());
                myUser.setLogged(true);
                myUser.setWaterDrank(0);
                myUser.setMood(0);
                myUser.setlastConnection( System.currentTimeMillis());
                Intent intent = new Intent(ActivityGetUserDAta.this, ActivityHome.class);
                intent.putExtra("USER", myUser);
                myUser.savePreferences(ActivityGetUserDAta.this);
                startActivity(intent);
                finish();

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent select_intent = new Intent(ActivityGetUserDAta.this, HelloUpActivity.class);
               // startActivity(select_intent);
               // finish();
            }
        });

    }

    public void onClickGender(View v){
        switch (v.getId()){
            case R.id.activity_get_user_data_gender_f:
                myUser.setGender(UserData.FEMALE);
                break;
            case R.id.activity_get_user_data_gender_m:
                myUser.setGender(UserData.MALE);
                break;
        }
    }

    public UserData loadPreferences(){
        return new UserData().getUserData(ActivityGetUserDAta.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout_button:
                clearPreferences();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //  @RequiresApi(api = Build.VERSION_CODES.N)
    protected void clearPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();

        try {
            Context context = createPackageContext("com.jawbone.helloup",CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences myGod = PreferenceManager.getDefaultSharedPreferences(context);
            myGod.edit().clear().commit();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (SecurityException e){
            e.printStackTrace();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ActivityGetUserDAta.this);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();
        editor2.remove(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN);
        editor2.remove(UpPlatformSdkConstants.UP_PLATFORM_REFRESH_TOKEN);
        editor2.clear().commit();

        Intent intent = new Intent(this, HelloUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

}
