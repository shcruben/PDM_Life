package com.iteso.ruben.proyectoversion1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivitySleepGraph extends AppCompatActivity {

    private String mAccessToken;
    private ImageButton back_button;
    private ArrayList<DataPoint> sleepsTime;
    private GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_graph);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

        if (mAccessToken != null) {
            ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
        }

        ApiManager.getRestApiInterface().getSleepEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                null,
                sleepEventListCallbackListener);

        graphView = (GraphView) findViewById(R.id.activity_sleep_graph_graph);
        graphView.setTitleColor(getColor(R.color.white));

        back_button = (ImageButton) findViewById(R.id.activity_sleep_graph_back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private Callback sleepEventListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
          //  Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
            LinkedTreeMap<String, LinkedTreeMap> received = (LinkedTreeMap) o;
            LinkedTreeMap<String, ArrayList> data = received.get("data");
            ArrayList< LinkedTreeMap <String, Object> > items = data.get("items");

            sleepsTime = new ArrayList<>();
            double i = 0;

            for (LinkedTreeMap<String, Object> l : items) {
                Log.e("MoveEventList ",  (String) l.get("title"));

                Double timeCreated = (Double) l.get("time_created");
                Double timeCompleted = (Double) l.get("time_completed");
                Double totalSleep = (timeCompleted - timeCreated) / 3600;
                Date date = new Date( (long) (timeCompleted *1000) );

                sleepsTime.add( new DataPoint(i, totalSleep) );
                i++;
            }

            Collections.sort(sleepsTime, new Comparator<DataPoint>() {
                @Override
                public int compare(DataPoint o1, DataPoint o2) {
                    return o1.getX() == o2.getX() ? 0 : o1.getX() > o2.getX() ? 1 : -1;
                }
            });

            if(sleepsTime.size() > 0) {
                sleepsTime.add(sleepsTime.get(sleepsTime.size() - 1));
                DataPoint[] sleepTimeDP = new DataPoint[sleepsTime.size()];
                sleepsTime.toArray(sleepTimeDP);

                MyAnimPlot myAnimPlot = new MyAnimPlot(graphView, sleepTimeDP);
                myAnimPlot.show();

                graphView.setTitle("Sleep Time Graph");
                graphView.setTitleTextSize(40);
                graphView.setTitleColor(R.color.text_c);
                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
                staticLabelsFormatter.setHorizontalLabels(new String[] {"newest", "2", "3", "4", "5", "6", "7", "8", "oldest", "oldest"});
                graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            }else{
                graphView.setTitle(getString(R.string.sleep_graph_no_data_message));
            }

//            List<Series> list = graphView.getSeries();
//
//            for(Series<DataPoint> series : list){
//                Iterator<DataPoint> iterator = series.getValues(series.getHighestValueX(), series.getHighestValueX());
//                while(iterator.hasNext()){
//                    DataPoint dataPoint = iterator.next();
//                    Log.e("ASDFG", dataPoint.toString());
//                }
//            }
//
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}

