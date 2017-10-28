package com.iteso.ruben.proyectoversion1;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iteso.ruben.proyectoversion1.beans.UserData;

import java.sql.Time;

public class ActivityGetUserDAta extends AppCompatActivity {

    private UserData myUser;
    protected NumberPicker weight, height, age, levelExercise;
    protected TimePicker wakeUp;
    protected Button save;


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
                Intent intent = new Intent(ActivityGetUserDAta.this, ActivityHome.class);
                intent.putExtra("USER", myUser);
                myUser.savePreferences(ActivityGetUserDAta.this);
                startActivity(intent);
                finish();

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
}
