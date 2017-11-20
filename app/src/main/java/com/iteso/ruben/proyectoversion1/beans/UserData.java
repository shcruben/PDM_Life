package com.iteso.ruben.proyectoversion1.beans;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruben on 23/10/17.
 */

public class UserData implements Parcelable {

    public static final boolean FEMALE = false;
    public static final boolean MALE = true;

    private float weight;
    private float height;
    private int waterDrank;
    private int mood;
    private long lastConnection;
    private int age;
    private int wakeuptime_hrs, wakeuptime_min;
    private long timeOfSleep;
    private long timeOfWakeUp;
    private int levelOfExcercise;
    private boolean isSleepRegistered;
    private boolean mGender;
    private boolean isLogged;

    public long getTimeOfSleep() {
        return timeOfSleep;
    }

    public void setTimeOfSleep(long timeOfSleep) {
        this.timeOfSleep = timeOfSleep;
    }

    public long getTimeOfWakeUp() {
        return timeOfWakeUp;
    }

    public void setTimeOfWakeUp(long timeOfWakeUp) {
        this.timeOfWakeUp = timeOfWakeUp;
    }

    public boolean isSleepRegistered() {
        return isSleepRegistered;
    }

    public void setSleepRegistered(boolean sleepRegistered) {
        isSleepRegistered = sleepRegistered;
    }

    public int getWaterDrank() {
        return waterDrank;
    }

    public void setWaterDrank(int waterDrank) {
        this.waterDrank = waterDrank;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public long getlastConnection() {
        return lastConnection;
    }

    public void setlastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    public boolean isGender() {
        return mGender;
    }

    public void setGender(boolean gender) {
        mGender = gender;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public UserData() {
        isLogged = false;
    }

    protected UserData(Parcel in) {
        weight = in.readFloat();
        height = in.readFloat();
        age = in.readInt();
        levelOfExcercise = in.readInt();
        wakeuptime_hrs = in.readInt();
        wakeuptime_min = in.readInt();
        waterDrank = in.readInt();
        timeOfSleep = in.readLong();
        timeOfWakeUp = in.readLong();
        lastConnection = in.readLong();
        mood = in.readInt();
        isSleepRegistered = in.readByte() != 0;
        isLogged = in.readByte() != 0;;
        mGender = in.readByte() != 0;
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWakeuptime_hrs() {
        return wakeuptime_hrs;
    }

    public void setWakeuptime_hrs(int wakeuptime_hrs) {
        this.wakeuptime_hrs = wakeuptime_hrs;
    }

    public int getWakeuptime_min() {
        return wakeuptime_min;
    }

    public void setWakeuptime_min(int wakeuptime_min) {
        this.wakeuptime_min = wakeuptime_min;
    }

    public int getLevelOfExcercise() {
        return levelOfExcercise;
    }

    public void setLevelOfExcercise(int levelOfExcercise) {
        this.levelOfExcercise = levelOfExcercise;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(weight);
        parcel.writeFloat(height);
        parcel.writeInt(age);
        parcel.writeInt(levelOfExcercise);
        parcel.writeInt(wakeuptime_hrs);
        parcel.writeInt(wakeuptime_min);
        parcel.writeInt(waterDrank);
        parcel.writeLong(timeOfSleep);
        parcel.writeLong(timeOfWakeUp);
        parcel.writeLong(lastConnection);
        parcel.writeInt(mood);
        parcel.writeByte((byte) (isSleepRegistered?1:0));
        parcel.writeByte((byte)(isLogged?1:0));
        parcel.writeByte((byte)(mGender?1:0));
    }

    @Override
    public String toString() {
        final String[] exerciseLevelString = {"High", "Medium", "Low"};
        return "Weight = " + weight + "\n" +
                "Height = " + height + "\n" +
                "Age = " + age + "\n" +
                "Gender = " + (mGender ? "Male\n" : "Female\n") +
                "Wake up time = " + wakeuptime_hrs + ":" + wakeuptime_min + "\n" +
                "Level of Exercise = " + exerciseLevelString[levelOfExcercise] +"\n" +
                "LastConnection = " + lastConnection;
    }


    public UserData getUserData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);

        UserData myUser = new UserData();
        myUser.setAge(sharedPreferences.getInt("age", 18));
        myUser.setWeight(sharedPreferences.getFloat("weight", (float)70.0));
        myUser.setHeight(sharedPreferences.getFloat("height", 180));
        myUser.setWakeuptime_hrs(sharedPreferences.getInt("wakeuptime_hrs", 18));
        myUser.setWakeuptime_min(sharedPreferences.getInt("wakeuptime_min", 18));
        myUser.setLevelOfExcercise(sharedPreferences.getInt("levelOfExcercise", 1));
        myUser.setLogged(sharedPreferences.getBoolean("isLogged", false));
        myUser.setGender(sharedPreferences.getBoolean("mGender", FEMALE));
        myUser.setTimeOfSleep(sharedPreferences.getLong("timeOfSleep", timeOfSleep));
        myUser.setTimeOfWakeUp(sharedPreferences.getLong("timeOfWakeUp", timeOfWakeUp));
        myUser.setlastConnection(sharedPreferences.getLong("lastConnection",lastConnection ));
        myUser.setMood(sharedPreferences.getInt("mood",mood ));
        myUser.setSleepRegistered(sharedPreferences.getBoolean("sleepRegistered", false));
        myUser.setWaterDrank(sharedPreferences.getInt("waterDrank",waterDrank ));
        return myUser;
    }

    public void savePreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_PREFRENCES,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("weight",weight);
        editor.putFloat("height",height);
        editor.putInt("age",age);
        editor.putInt("wakeuptime_hrs", wakeuptime_hrs);
        editor.putInt("wakeuptime_min", wakeuptime_min);
        editor.putInt("levelOfExcercise", levelOfExcercise);
        editor.putBoolean("isLogged",isLogged);
        editor.putBoolean("mGender",mGender);
        editor.putLong("timeOfSleep", timeOfSleep);
        editor.putLong("timeOfWakeUp", timeOfWakeUp);
        editor.putLong("lastConnection",lastConnection);
        editor.putInt("mood",mood);
        editor.putBoolean("sleepRegistered", false);
        editor.putInt("waterDrank", waterDrank);
        editor.apply();

    }
}
