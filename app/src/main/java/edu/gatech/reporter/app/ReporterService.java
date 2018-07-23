package edu.gatech.reporter.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.estimote.proximity_sdk.proximity.ProximityContext;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.gatech.reporter.beacons.BeaconEvents.RestartReportTaskEvent;
import edu.gatech.reporter.beacons.BeaconEvents.ChangeTagsEvent;
import edu.gatech.reporter.beacons.ProximityBeaconImplementation;
import edu.gatech.reporter.beacons.ProximityBeaconInterface;
import edu.gatech.reporter.utils.Const;
import edu.gatech.reporter.utils.ParameterManager.DataManager;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;

public class ReporterService extends Service implements ProximityBeaconInterface
{
    private static Context mContext;
    private static final String TAG = "Tests";
    private static Timer timer;
    private static DataManager myDataManager;
    public static ProximityBeaconImplementation beaconObserver;

    private List<String> initialTrackedBeacons;

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
        myDataManager = new DataManager(mContext);
        initBeaconDetection();

        timer = new Timer();
        timer.schedule(new DataUpdateTask(myDataManager), 0, ParameterOptions.getInstance().dataUpdateInterval);
        timer.schedule(new SendDataTask(myDataManager), 0, ParameterOptions.getInstance().reportInterval);
        timer.schedule(new UpdateNearBeaconsTask(myDataManager.getNearbyBeaconManager()), 0, ParameterOptions.getInstance().beaconUpdateViewInterval);

        EventBus.getDefault().register(this);
    }


    private void initBeaconDetection(){

        // Required Initialization to star beacon scan.
        beaconObserver = ProximityBeaconImplementation.getInstance(this);
        beaconObserver.initProximityObserver();
        beaconObserver.setServiceListener(this);

        initialTrackedBeacons = Arrays.asList(String.valueOf(ParameterOptions.getInstance().beaconTags)
                .split(","));
        beaconObserver.addProximityZone(initialTrackedBeacons);
        beaconObserver.startBeaconObserver();

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void newlySelectecBeaconZonessHandler(ChangeTagsEvent event) {
        beaconObserver.stopBeaconObserver();
        myDataManager.getNearbyBeaconManager().getNearbyBeaconZones().clear();
        myDataManager.getNearbyBeaconManager().getNearbyBeacons().clear();
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void restartReportTask(RestartReportTaskEvent event){
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.schedule(new DataUpdateTask(myDataManager), 0, ParameterOptions.getInstance().dataUpdateInterval);
        timer.schedule(new SendDataTask(myDataManager), 0, ParameterOptions.getInstance().reportInterval);
        timer.schedule(new UpdateNearBeaconsTask(myDataManager.getNearbyBeaconManager()), 0, ParameterOptions.getInstance().beaconUpdateViewInterval);
        initBeaconDetection();
    }


    @Override
    public void onEnterBeaconRegion(ProximityContext attachments) {
        Log.e(TAG, "onEnterBeaconRegion: " + "Beacon Institution Region Entered is:" + attachments.getAttachments().get(Const.BEACON_INSTITUTION_KEY));
        String zone = attachments.getTag();
        myDataManager.getNearbyBeaconManager().getNearbyBeaconZones().put(zone, zone);
        List<ProximityContext> tempList = new ArrayList<>();
        tempList.add(attachments);
        myDataManager.getNearbyBeaconManager().getNearbyBeacons().put(zone, tempList);
    }

    @Override
    public void onExitBeaconRegion(ProximityContext attachments) {
        Log.e(TAG, "onExitBeaconRegion: " + "Beacon Institution Region Entered is:" + attachments.getAttachments().get(Const.BEACON_INSTITUTION_KEY));
        String zone = attachments.getTag();
        myDataManager.getNearbyBeaconManager().getNearbyBeacons().remove(zone);
        myDataManager.getNearbyBeaconManager().getNearbyBeaconZones().remove(zone);

    }

    @Override
    public void onChangeActionInRegion(List<? extends ProximityContext> attachments) {
        List<String> busStops = new ArrayList<>();
        for (ProximityContext attachment : attachments) {
            busStops.add(attachment.getAttachments().get(Const.BEACON_BUS_STOP_KEY));
        }
        Log.e(TAG, "onChangeBeaconRegion: " + "Beacons Nearby: " +  busStops);
        myDataManager.getNearbyBeaconManager().updateNearbyBeaconList(attachments);
    }

}