package edu.gatech.reporter.app;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.gatech.reporter.beacons.Database.BeaconDatabaseManager;
import edu.gatech.reporter.beacons.Database.BeaconZone;
import edu.gatech.reporter.beacons.Database.BeaconZonesEvent;
import edu.gatech.reporter.beacons.ProximityBeaconImplementation;
import edu.gatech.reporter.beacons.ProximityBeaconInterface;
import edu.gatech.reporter.utils.ParameterManager.DataManager;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;
import edu.gatech.reporter.utils.ParameterManager.Parameters;

public class ReporterService extends Service implements ProximityBeaconInterface
{
    private static Context mContext;
    private static final String TAG = "Tests";
    private static Timer timer;
    private static DataManager myDataManager;
    private static BeaconDatabaseManager beaconDatabaseManager;
    public static ProximityBeaconImplementation beaconObserver;

    private List<BeaconZone> initialTrackedBeacons;
    private static List<? extends ProximityAttachment> nearbyBeaconList;

    private Executor executor = Executors.newSingleThreadExecutor();

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
        super.onCreate();
        Log.e(TAG, "onCreate");
        mContext = getApplicationContext();
        myDataManager = new DataManager();
        initBeaconDetection();

        timer = new Timer();
        timer.schedule(new DataUpdateTask(myDataManager), 0, ParameterOptions.getInstance().dataUpdateInterval);
        timer.schedule(new SendDataTask(myDataManager), 0, ParameterOptions.getInstance().reportInterval);
        timer.schedule(new UpdateNearBeaconsTask(nearbyBeaconList), 0, ParameterOptions.getInstance().beaconUpdateViewInterval);

        EventBus.getDefault().register(this);
    }


    private void initBeaconDetection(){

        // Required Initialization to star beacon scan.
        beaconDatabaseManager = BeaconDatabaseManager.getInstance(mContext);
        beaconObserver = ProximityBeaconImplementation.getInstance(this);
        beaconObserver.initProximityObserver();
        beaconObserver.setServiceListener(this);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                initialTrackedBeacons = beaconDatabaseManager.getBeaconDatabase().myBeaconZones().getBeaconZones();
                EventBus.getDefault().post(new BeaconZonesEvent(initialTrackedBeacons));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void beaconZonesEventHandler(BeaconZonesEvent event) {

        initialTrackedBeacons = event.getBeaconZonesList();
        beaconObserver.addProximityZone(initialTrackedBeacons);
        beaconObserver.startBeaconObserver();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        EventBus.getDefault().unregister(this);
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
        timer.schedule(new UpdateNearBeaconsTask(nearbyBeaconList), 0, ParameterOptions.getInstance().beaconUpdateViewInterval);
    }


    @Override
    public void onEnterBeaconRegion(ProximityAttachment attachments) {
        Log.e(TAG, "onEnterBeaconRegion: Inside ReporterService onEnterBeaconRegion");
    }

    @Override
    public void onExitBeaconRegion(ProximityAttachment attachments) {
        Log.e(TAG, "onEnterBeaconRegion: Inside ReporterService EXIT REGION");
    }

    @Override
    public void onChangeActionInRegion(List<? extends ProximityAttachment> attachments) {
        Log.e(TAG, "onEnterBeaconRegion: Inside ReporterService ON CHANGE REGION");
        nearbyBeaconList = attachments;
    }

}