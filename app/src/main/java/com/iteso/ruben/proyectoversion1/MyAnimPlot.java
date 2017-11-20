package com.iteso.ruben.proyectoversion1;

import android.os.AsyncTask;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static android.os.SystemClock.sleep;

/**
 * Created by Usuario on 19/11/2017.
 */

public class MyAnimPlot {
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    private int maxPoints = 12;
    private double minX = 0.0;
    private double maxX = 10.0;
    private DataPoint[] points = {};
    private long sleepTime = 50;

    MyAnimPlot(GraphView gv){
        graphView = gv;
        series = new LineGraphSeries<>();
        graphView.addSeries(series);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(minX);
        graphView.getViewport().setMaxX(maxX);
        graphView.getViewport().setMaxY(12);
    }

    MyAnimPlot(GraphView gv, DataPoint[] values){
        graphView = gv;
        series = new LineGraphSeries<>();
        graphView.addSeries(series);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(minX);
        graphView.getViewport().setMaxX(maxX);
        points = values;
    }

    void show(){
        new MyAnimPlotAsyncTask().execute(points);
    }

    class MyAnimPlotAsyncTask extends AsyncTask<DataPoint, Void, Void>{
        @Override
        protected Void doInBackground(DataPoint... dataPoints) {
            for(DataPoint x : dataPoints){
                series.appendData(x, true, maxPoints);
                sleep(sleepTime);
            }
            return null;
        }
    }

    void addValues(DataPoint[] values){
        values = points;
    }

    DataPoint[] getValues(){
        return points;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public void setGraphView(GraphView graphView) {
        graphView.addSeries(series);
        this.graphView = graphView;
    }

    public LineGraphSeries<DataPoint> getSeries() {
        return series;
    }

    public void setSeries(LineGraphSeries<DataPoint> series) {
        graphView.addSeries(series);
        this.series = series;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }
}
