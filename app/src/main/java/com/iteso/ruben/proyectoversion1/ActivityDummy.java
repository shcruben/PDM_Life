package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.iteso.ruben.proyectoversion1.beans.Constants;
import com.iteso.ruben.proyectoversion1.beans.UserData;

import java.util.zip.Inflater;

public class ActivityDummy extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        textView = (TextView)findViewById(R.id.dummy_textview);

        UserData myUser = loadPreferences();

        textView.setText(myUser.toString());

    }
    public UserData loadPreferences(){
        return new UserData().getUserData(ActivityDummy.this);
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

    protected void clearPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        Intent intent = new Intent(this, ActivitySplash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
