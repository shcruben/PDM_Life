package com.iteso.ruben.proyectoversion1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iteso.ruben.proyectoversion1.beans.UserData;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserData myUser = loadPreferences();
        Intent mainIntent;

        if(myUser.isLogged()){

            mainIntent = new Intent().setClass(ActivitySplash.this,
                    ActivityHome.class);
            //mainIntent.putExtra("USER", myUser);
        }else{
            mainIntent = new Intent().setClass(ActivitySplash.this,
                    ActivityWelcome.class);
        }

        startActivity(mainIntent);
        finish();

    }

    public UserData loadPreferences(){
        return new UserData().getUserData(ActivitySplash.this);
    }
}


