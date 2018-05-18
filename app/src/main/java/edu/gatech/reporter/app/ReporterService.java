package edu.gatech.reporter.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;

import edu.gatech.reporter.utils.ParameterManager.DataManager;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;

public class ReporterService extends Service
{
    private static Context mContext;
    private static final String TAG = "Tests";
    private static Timer timer;
    private static DataManager myDataManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        mContext = getApplicationContext();
        myDataManager = new DataManager();

        timer = new Timer();
        timer.schedule(new DataUpdateTask(myDataManager), 0, ParameterOptions.getInstance().dataUpdateInterval);
        timer.schedule(new SendDataTask(myDataManager), 0, ParameterOptions.getInstance().reportInterval);
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void restartReportTask(){
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.schedule(new DataUpdateTask(myDataManager), 0, ParameterOptions.getInstance().dataUpdateInterval);
        timer.schedule(new SendDataTask(myDataManager), 0, ParameterOptions.getInstance().reportInterval);
    }
}