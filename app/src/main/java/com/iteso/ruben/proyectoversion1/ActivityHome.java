package com.iteso.ruben.proyectoversion1;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;


public class ActivityHome extends AppCompatActivity {

    protected CoordinatorLayout coordinatorLayout;
    protected FloatingActionButton floatingActionButton;
    protected FloatingActionButton fab1,fab2,fab3,fab4,fab5 ;
    protected Animation fabOpen;
    protected Animation fabClose;
    protected boolean isSubmenuShown;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                Intent intent = new Intent(ActivityHome.this, ActivityDummy.class);
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

}
