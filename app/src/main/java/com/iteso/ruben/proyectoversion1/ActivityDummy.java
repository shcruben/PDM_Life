package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.iteso.ruben.proyectoversion1.beans.Constants;
import com.iteso.ruben.proyectoversion1.beans.UserData;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.datamodel.Meta;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityDummy extends AppCompatActivity {

    private static final String TAG = ActivityDummy.class.getSimpleName();


    private String mAccessToken;
    private String mClientSecret;
    protected String id;
    protected String first_name;
    protected String last_name;
    protected String image;
    protected String weight;
    protected String height;
    protected String gender;
    protected TextView textView;
    protected ImageButton back_button;
    protected Button see_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        Intent intent = getIntent();

        if (intent != null) {
            mClientSecret = intent.getStringExtra(UpPlatformSdkConstants.CLIENT_SECRET);
        }
        see_user = (Button) findViewById(R.id.see_user_id);
        textView = (TextView) findViewById(R.id.dummy_textview);
        back_button = (ImageButton) findViewById(R.id.activity_dummy_back_button);
       // UserData myUser = loadPreferences();
       // textView.setText(myUser.toString());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent select_intent = new Intent(ActivityDummy.this, ActivityHome.class);
                startActivity(select_intent);
                finish();
            }
        });

        see_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiManager.getRestApiInterface().getUser(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        genericCallbackListener);
            }
        });

    }
    public UserData loadPreferences(){
        return new UserData().getUserData(ActivityDummy.this);
    }





    //TODO the callbacks are not yet backed by data model, but will get json response,
    //TODO which for now is logged to console
    private Callback genericCallbackListener = new Callback<Object>()  {
        @Override
            public void success(Object o, Response response) {

            LinkedTreeMap<String, LinkedTreeMap> mapa = (LinkedTreeMap) o;
            LinkedTreeMap<String, String> data = mapa.get("data");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("First name: " + data.get("first") + "\n");
            stringBuilder.append("Last name: " + data.get("last") + "\n" + "");

            stringBuilder.append( String.valueOf(data.get("weight")) + "\n");
            stringBuilder.append( String.valueOf(data.get("height")) + "\n");
            long epoch = (System.currentTimeMillis());
            Date today = new Date(epoch);
            stringBuilder.append( "TIME: " + today.toString() );
            Log.e(TAG,  "api call successful, json output: " + o.toString().substring(91,117));
            Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();

            textView.setText(stringBuilder.toString());

       }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e(TAG,  "api call failed, error message: " + retrofitError.getMessage());
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    //TODO the callbacks are not yet backed by data model, but will get json response,
    //TODO which for now is logged to console
    private Callback stepsCallbackListener = new Callback<Object>()  {
        @Override
        public void success(Object o, Response response) {
            Log.e(TAG,  "api call successful, json output: " + o.toString());
            Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
            textView.setText("");

        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e(TAG,  "api call failed, error message: " + retrofitError.getMessage());
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
