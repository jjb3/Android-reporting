package edu.gatech.reporter.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import java.util.List;
import java.util.Timer;

import edu.gatech.reporter.beacons.BeaconDatabaseManager;
import edu.gatech.reporter.beacons.BeaconZone;
import edu.gatech.reporter.beacons.ProximityBeaconImplementation;
import edu.gatech.reporter.beacons.ProximityBeaconInterface;
import edu.gatech.reporter.utils.ParameterManager.DataManager;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;

public class ReporterService extends Service implements ProximityBeaconInterface
{
    private static Context mContext;
    private static final String TAG = "Tests";
    private static Timer timer;
    private static DataManager myDataManager;
    private static BeaconDatabaseManager beaconDatabaseManager;
    public static ProximityBeaconImplementation beaconObserver;

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
        beaconDatabaseManager = BeaconDatabaseManager.getInstance(mContext);
        List<BeaconZone> tempList = beaconDatabaseManager.getBeaconDatabase().myBeaconZones().getBeaconZones();
        beaconObserver = ProximityBeaconImplementation.getInstance(this, tempList );
        beaconObserver.startBeaconObserver();

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


    @Override
    public void onEnterBeaconRegion(ProximityAttachment attachments) {

    }

    @Override
    public void onExitBeaconRegion(ProximityAttachment attachments) {

    }

    @Override
    public void onChangeActionInRegion(ProximityAttachment attachments) {

    }
}