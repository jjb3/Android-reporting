package edu.gatech.reporter.utils.ParameterManager;

import android.content.Context;
import android.content.SharedPreferences;

import edu.gatech.reporter.R;
import edu.gatech.reporter.app.ReporterHome;

/**
 * Created by Wendi on 2016/10/15.
 */

public class ParameterOptions {
    public Context mApplicationContext;
    public boolean powerLevelChk = true;
    public boolean locationDataChk = true;
    public boolean accDataChk = true;
    public boolean beaconChk = true;
    public boolean enviChk = true;
    public boolean imeiChk = true;
    public boolean androidIDChk = true;
    public boolean macChk = true;
    public boolean netStatusCheck = true;
    public boolean isAppRecording = false;
    public int dataUpdateInterval = 500;
    public int reportInterval = 5000;
    public int minUpdateDistance = 0;
    public String serverURL = "https://www.busgenius.com/api/v1/geolocations";
    public String beaconTags = "";

    private static ParameterOptions instance = null;

    private ParameterOptions(){

    }
    public static ParameterOptions getInstance(){
        if(instance == null) {
            instance = new ParameterOptions();
        }
        return instance;
    }

    public void setContext(Context applicationContext){
        this.mApplicationContext = applicationContext;
    }
    public void loadPreference(){
        SharedPreferences sharedPref = mApplicationContext.getSharedPreferences(ReporterHome.class.getSimpleName(), Context.MODE_PRIVATE);
        ParameterOptions.getInstance().powerLevelChk = sharedPref.getInt(mApplicationContext.getString(R.string.powerLevelChk), 1) == 1;
        ParameterOptions.getInstance().locationDataChk = sharedPref.getInt(mApplicationContext.getString(R.string.locationDataChk), 1) == 1;
        ParameterOptions.getInstance().accDataChk = sharedPref.getInt(mApplicationContext.getString(R.string.accDataChk), 1) == 1;
        ParameterOptions.getInstance().beaconChk = sharedPref.getInt(mApplicationContext.getString(R.string.beaconChk), 1) == 1;
        ParameterOptions.getInstance().enviChk = sharedPref.getInt(mApplicationContext.getString(R.string.enviChk), 1) == 1;
        ParameterOptions.getInstance().imeiChk = sharedPref.getInt(mApplicationContext.getString(R.string.imeiChk), 1) == 1;
        ParameterOptions.getInstance().androidIDChk = sharedPref.getInt(mApplicationContext.getString(R.string.androidIDChk), 1) == 1;
        ParameterOptions.getInstance().macChk = sharedPref.getInt(mApplicationContext.getString(R.string.macChk), 1) == 1;
        ParameterOptions.getInstance().netStatusCheck = sharedPref.getInt(mApplicationContext.getString(R.string.netStatusCheck), 1) == 1;
        ParameterOptions.getInstance().dataUpdateInterval = sharedPref.getInt(mApplicationContext.getString(R.string.changeDataUpdateInterval), 500);
        ParameterOptions.getInstance().reportInterval = sharedPref.getInt(mApplicationContext.getString(R.string.changeReportInterval), 5000);
        ParameterOptions.getInstance().serverURL = sharedPref.getString(mApplicationContext.getString(R.string.changeServerURL), serverURL);
        ParameterOptions.getInstance().beaconTags = sharedPref.getString(mApplicationContext.getString(R.string.beacon_tags),"");
        ParameterOptions.getInstance().isAppRecording = sharedPref.getBoolean(mApplicationContext.getString(R.string.isRecordingButtonPressed),false);
    }

    public void writePreference(){
        SharedPreferences sharedPref = mApplicationContext.getSharedPreferences(ReporterHome.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mApplicationContext.getString(R.string.powerLevelChk), (ParameterOptions.getInstance().powerLevelChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.locationDataChk), (ParameterOptions.getInstance().locationDataChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.accDataChk), (ParameterOptions.getInstance().accDataChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.beaconChk), (ParameterOptions.getInstance().beaconChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.enviChk), (ParameterOptions.getInstance().enviChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.imeiChk), (ParameterOptions.getInstance().imeiChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.androidIDChk), (ParameterOptions.getInstance().androidIDChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.macChk), (ParameterOptions.getInstance().macChk) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.netStatusCheck), (ParameterOptions.getInstance().netStatusCheck) ? 1 : 0);
        editor.putInt(mApplicationContext.getString(R.string.changeDataUpdateInterval), ParameterOptions.getInstance().dataUpdateInterval);
        editor.putInt(mApplicationContext.getString(R.string.changeReportInterval), ParameterOptions.getInstance().reportInterval);
        editor.putString(mApplicationContext.getString(R.string.changeServerURL), ParameterOptions.getInstance().serverURL);
        editor.putString(mApplicationContext.getString(R.string.beacon_tags), ParameterOptions.getInstance().beaconTags);
        editor.commit();
    }

    public void writeIsRecordingPreference(){
        SharedPreferences sharedPref = mApplicationContext.getSharedPreferences(ReporterHome.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(mApplicationContext.getString(R.string.isRecordingButtonPressed), ParameterOptions.getInstance().isAppRecording);
        editor.commit();
    }
}
