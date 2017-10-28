package com.iteso.ruben.proyectoversion1.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ruben on 19/10/17.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Hamtaro.db";
    private static final int DATABASE_VERSION = 1;

    //App tables
    public static final String TABLE_WATER = "water";
    public static final String TABLE_WEIGHT = "weight";
    public static final String TABLE_HEART_RATE = "hrate";
    public static final String TABLR_SLEEP = "sleep";

    //Water Table items
    public static final String KEY_WATER_TDY = "watertdy";
    public static final String KEY_WATER_TRGT = "watertrgt";

    //Weight Table items
    public static final String KEY_WEIGHT_TDY = "weighttdy";
    public static final String KEY_WEIGHT_TRGT = "weighttrgt";

    //Sleep Table items
    public static final String KEY_SLEEP_TDY = "sleeptdy";
    public static final String KEY_SLEEP_TRGT = "sleeptrgt";



    private static DataBaseHandler dataBaseHandler;

    private DataBaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DataBaseHandler getInstance(Context context){
        if(dataBaseHandler == null){
            dataBaseHandler = new DataBaseHandler(context); }
        return dataBaseHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
