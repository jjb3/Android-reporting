package edu.gatech.reporter.app;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.estimote.proximity_sdk.api.ProximityZoneContext;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.gatech.reporter.R;
import edu.gatech.reporter.beacons.BeaconEvents.RestartReportTaskEvent;
import edu.gatech.reporter.beacons.BeaconEvents.ChangeTagsEvent;
import edu.gatech.reporter.beacons.BeaconEvents.StartBeaconScanEvent;
import edu.gatech.reporter.beacons.BeaconEvents.UpdateBeaconZonesEvent;
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
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(myDataManager.getNearbyBeaconManager().getNearbyBeacons()));
        initBeaconDetection();
        startForeground(1,createNotification());
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e(TAG, "onCreate");
        mContext = getApplicationContext();
        ParameterOptions.getInstance().setContext(mContext);
        ParameterOptions.getInstance().loadPreference();
        myDataManager =DataManager.getInstance(mContext);
        timer = new Timer();
        timer.schedule(new DataUpdateTask(myDataManager), 0, ParameterOptions.getInstance().dataUpdateInterval);
        timer.schedule(new SendDataTask(myDataManager), 0, ParameterOptions.getInstance().reportInterval);
        EventBus.getDefault().register(this);
    }


    private void initBeaconDetection(){

        // Required Initialization to star beacon scan.
        if(ParameterOptions.getInstance().beaconChk) {
            beaconObserver = ProximityBeaconImplementation.getInstance(this);
            beaconObserver.initProximityObserver();
            beaconObserver.setServiceListener(this);

            initialTrackedBeacons = Arrays.asList(String.valueOf(ParameterOptions.getInstance().beaconTags)
                    .split(","));
            beaconObserver.addProximityZone(initialTrackedBeacons);
            beaconObserver.startBeaconObserver();
        }

    }
    
    private Notification createNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "1")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Background Service Running")
                .setContentText("GPS and beacon data being colledted...")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return mBuilder.build();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void newlySelectecBeaconZonessHandler(ChangeTagsEvent event) {
        beaconObserver.stopBeaconObserver();
        myDataManager.getNearbyBeaconManager().getNearbyBeaconZones().clear();
        myDataManager.getNearbyBeaconManager().getNearbyBeacons().clear();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void startBeaconScanEvent(StartBeaconScanEvent startBeaconScanEvent){
        initBeaconDetection();
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        EventBus.getDefault().unregister(this);
        stopSelf();
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
    }


    @Override
    public void onEnterBeaconRegion(ProximityZoneContext attachments) {
        Log.e(TAG, "onEnterBeaconRegion: " + "Beacon Institution Region Entered is:" + attachments.getAttachments().get(Const.BEACON_INSTITUTION_KEY));
        String zone = attachments.getTag();
        myDataManager.getNearbyBeaconManager().getNearbyBeaconZones().put(zone, zone);
        List<ProximityZoneContext> tempList = new ArrayList<>();
        tempList.add(attachments);
        myDataManager.getNearbyBeaconManager().getNearbyBeacons().put(zone, tempList);
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(myDataManager.getNearbyBeaconManager().getNearbyBeacons()));
    }

    @Override
    public void onExitBeaconRegion(ProximityZoneContext attachments) {
        Log.e(TAG, "onExitBeaconRegion: " + "Beacon Institution Region Entered is:" + attachments.getAttachments().get(Const.BEACON_INSTITUTION_KEY));
        String zone = attachments.getTag();
        myDataManager.getNearbyBeaconManager().getNearbyBeacons().remove(zone);
        myDataManager.getNearbyBeaconManager().getNearbyBeaconZones().remove(zone);
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(myDataManager.getNearbyBeaconManager().getNearbyBeacons()));

    }

    @Override
    public void onChangeActionInRegion(Set<? extends ProximityZoneContext> attachments) {
        List<String> busStops = new ArrayList<>();
        for (ProximityZoneContext attachment : attachments) {
            busStops.add(attachment.getAttachments().get(Const.BEACON_BUS_STOP_KEY));
        }
        Log.e(TAG, "onChangeBeaconRegion: " + "Beacons Nearby: " +  busStops);
        myDataManager.getNearbyBeaconManager().updateNearbyBeaconList(attachments);
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(myDataManager.getNearbyBeaconManager().getNearbyBeacons()));
    }

}