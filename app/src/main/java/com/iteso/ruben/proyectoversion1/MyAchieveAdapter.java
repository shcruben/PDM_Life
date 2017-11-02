package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Elizabeth on 31/10/17.
 */

public class MyAchieveAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final String[] values;

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

        if(throphy_done.startsWith("Complete")){
            imageView.setImageResource(R.drawable.achieve_yes);
        }
        else{
            imageView.setImageResource(R.drawable.noachieve);
        }

        return theView;
    }
}
