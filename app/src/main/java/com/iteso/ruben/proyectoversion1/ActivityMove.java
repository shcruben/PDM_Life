package com.iteso.ruben.proyectoversion1;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityMove extends AppCompatActivity {

    private String mAccessToken;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }

        ApiManager.getRestApiInterface().getMoveEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                null,
                MoveEventListCallbackListener);

        back_button = (ImageButton) findViewById(R.id.activity_sleep_graph_back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private Callback MoveEventListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
            LinkedTreeMap<String, LinkedTreeMap> received = (LinkedTreeMap) o;
            LinkedTreeMap<String, ArrayList> data = received.get("data");
            ArrayList< LinkedTreeMap <String, Object> > items = data.get("items");
            for (LinkedTreeMap<String, Object> l : items) {
                Log.e("MoveEventList ",  (String) l.get("title"));
            }
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}

