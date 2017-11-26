package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteso.ruben.proyectoversion1.beans.Constants;

/**
 * Created by Elizabeth on 31/10/17.
 */

public class MyAchieveAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final String[] values;
        private final long steps_dummy=1;
        private int mood;
        private boolean sleepCompleteTen,stepsCompleteTen,stepsTodayComplete,sleepTodayComplete,waterCompleteToday;

    public MyAchieveAdapter(Context context, String[] values) {
        super(context, R.layout.achieves_list_item,values);
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View myView, ViewGroup parentGroup){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View theView = inflater.inflate(R.layout.achieves_list_item,parentGroup,false);
        TextView textView = (TextView) theView.findViewById(R.id.throphy_descrip);
        ImageView imageView = (ImageView) theView.findViewById(R.id.throphy_image);

        textView.setText(values[position]);

        String throphy_done = values[position];
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);
        sleepCompleteTen = (sharedPreferences.getBoolean("hoursSleepComplete", false));
        stepsCompleteTen = sharedPreferences.getBoolean("stepsComplete", false);

        stepsTodayComplete = (sharedPreferences.getLong("todaySteps",steps_dummy )>=7500)?true:false;
        sleepTodayComplete = (sharedPreferences.getLong("todayHours", steps_dummy )>7)?true:false;
        waterCompleteToday = sharedPreferences.getBoolean("isCompletedToday", false);


        if(sleepCompleteTen & throphy_done.startsWith("10 day sleep streak")){
            imageView.setImageResource(R.drawable.achieve_yes);
        }
        else if(stepsCompleteTen & throphy_done.startsWith("Complete steps for this 10 days")){
            imageView.setImageResource(R.drawable.achieve_yes);}
        else if(stepsTodayComplete & throphy_done.startsWith("Complete steps for a day")){}
        else if(sleepTodayComplete & throphy_done.startsWith("Nice sleep for today")){}
        else if(waterCompleteToday & throphy_done.startsWith("Complete water intake for a day")){}
        else{
            imageView.setImageResource(R.drawable.noachieve);
        }

        return theView;
    }
}
