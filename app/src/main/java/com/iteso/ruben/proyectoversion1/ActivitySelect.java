package com.iteso.ruben.proyectoversion1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivitySelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);


    }

    public void onClickControl(View view){
        switch (view.getId()){
            case R.id.activity_select_water_control:
                Intent water_control_intent = new Intent(ActivitySelect.this, ActivityMain.class);
                startActivity(water_control_intent);
                finish();
                break;
        }
    }
}
