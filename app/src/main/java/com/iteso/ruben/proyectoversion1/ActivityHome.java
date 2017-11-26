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

import com.iteso.ruben.proyectoversion1.beans.Constants;
import com.iteso.ruben.proyectoversion1.beans.UserData;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;


public class ActivityHome extends AppCompatActivity {

  protected CoordinatorLayout coordinatorLayout;
  protected FloatingActionButton floatingActionButton;
  protected FloatingActionButton fab1,fab2,fab3,fab4,fab5 ;
  protected Animation fabOpen;
  protected Animation fabClose;
  protected int mood;
  protected boolean isSubmenuShown;
  private ImageView img;
  private static int daysConnected = 0;
  private long lastTimeHome = 0;
  public static int getDaysConnected() {
    return daysConnected;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES,
            Context.MODE_PRIVATE);
    daysConnected = sharedPreferences.getInt("daysConnected", daysConnected);
    lastTimeHome = sharedPreferences.getLong("lastTimeHome", lastTimeHome);
    mood = Math.round(sharedPreferences.getInt("mood",mood )/2);


    if(ActivityWater.isDiffDay(lastTimeHome, System.currentTimeMillis())){

    }

    img = (ImageView) findViewById(R.id.happy_hamtaro);
    img.post(new Runnable() {

      @Override
      public void run() {
        ((AnimationDrawable) img.getBackground()).start();
      }
    });

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
        Intent intent = new Intent(ActivityHome.this, ActivityAchievement.class);
        //intent.putExtra("USER", myUser);
        startActivity(intent);

      }
    });

    fab5.bringToFront();
    fab5.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ActivityHome.this, ActivityMove.class);
        //intent.putExtra("USER", myUser);
        startActivity(intent);

      }
    });

      /*  floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout,"ITEM DELETED!!!!",Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Message saved ...",Toast.LENGTH_LONG).show();
                    }
                });
                snackbar.show();
            }
        }); */

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


}