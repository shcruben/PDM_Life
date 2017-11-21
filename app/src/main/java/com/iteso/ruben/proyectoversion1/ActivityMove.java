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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityMove extends AppCompatActivity {

    private String mAccessToken;
    private ImageButton back_button;
    private GraphView stepsGraph;
    private GraphView caloriesGraph;
    private GraphView kmsGraph;
    private ArrayList<DataPoint> steps;
    private ArrayList<DataPoint> calories;
    private ArrayList<DataPoint> kms;

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

        stepsGraph = (GraphView) findViewById(R.id.activity_move_steps);
        caloriesGraph = (GraphView) findViewById(R.id.activity_move_calories);
        kmsGraph = (GraphView) findViewById(R.id.activity_move_kms);


    }

    private Callback MoveEventListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            //Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
            LinkedTreeMap<String, LinkedTreeMap> received = (LinkedTreeMap) o;
            LinkedTreeMap<String, ArrayList> data = received.get("data");
            ArrayList< LinkedTreeMap <String, Object> > items = data.get("items");

            steps = new ArrayList<>();
            kms = new ArrayList<>();
            calories = new ArrayList<>();

            double i = 0;

            for (LinkedTreeMap<String, Object> l : items) {
                Log.e("MoveEventList ",  (String) l.get("title"));
                LinkedTreeMap<String, Double> detail =  (LinkedTreeMap<String, Double>) l.get("details");
                steps.add(new DataPoint(i , detail.get("steps")) );
                calories.add(new DataPoint(i , detail.get("bg_calories")/10 ) );
                kms.add(new DataPoint(i , detail.get("km")) );
                i++;
            }

            if (steps.size() > 0 && calories.size() > 0 && kms.size() > 0) {

                steps.add(steps.get(steps.size() - 1));
                calories.add(calories.get(calories.size() - 1));
                kms.add(kms.get(kms.size() - 1));

                DataPoint[] stepsDP = new DataPoint[steps.size()];
                DataPoint[] kmsDP = new DataPoint[steps.size()];
                DataPoint[] caloriesDP = new DataPoint[steps.size()];

                steps.toArray(stepsDP);
                kms.toArray(kmsDP);
                calories.toArray(caloriesDP);

                MyAnimPlot animPlotSteps = new MyAnimPlot(stepsGraph, stepsDP);
                MyAnimPlot animPlotCalories = new MyAnimPlot(caloriesGraph, caloriesDP);
                MyAnimPlot animPlotKms = new MyAnimPlot(kmsGraph, kmsDP);

                animPlotSteps.show();
                animPlotCalories.show();
                animPlotKms.show();

                stepsGraph.setTitle("Steps Graph");
                stepsGraph.setTitleTextSize(40);
                stepsGraph.setTitleColor(R.color.text_c);

                caloriesGraph.setTitle("Calories Graph");
                caloriesGraph.setTitleTextSize(40);
                caloriesGraph.setTitleColor(R.color.text_c);

                kmsGraph.setTitle("kms Graph");
                kmsGraph.setTitleTextSize(40);
                kmsGraph.setTitleColor(R.color.text_c);
            }



        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}

