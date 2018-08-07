package edu.gatech.reporter.beacons;

import android.app.Notification;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.gatech.reporter.R;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProximityBeaconImplementation {

    private static ProximityObserver beaconObserver;
    private static ProximityObserver.Handler beaconObserverHandler;
    private static Context mContext;

    private static ProximityBeaconImplementation instance;
    private static ProximityBeaconInterface proximityBeaconInterface;
    private boolean atLeastOneZoneSelected;

    private List<ProximityZone> mBeaconZones;

    private ProximityBeaconImplementation(Context context) {
        mContext = context;
    }

    private Notification createNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "1")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Beacon Scan")
                .setContentText("Scan is running...")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return mBuilder.build();
    }


    public static ProximityBeaconImplementation getInstance(Context context){
        if (instance == null){
            instance = new ProximityBeaconImplementation(context);
        }
        return instance;
    }

    public void initProximityObserver() {
        if(isNetworkAvailable()) {

            EstimoteCloudCredentials estimoteCloudCredentials = new EstimoteCloudCredentials(mContext.getString(R.string.appId), mContext.getString(R.string.appToken));

                beaconObserver = new ProximityObserverBuilder(mContext.getApplicationContext(), estimoteCloudCredentials)
                .onError(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Log.e("app", "proximity observer error: " + throwable);
                        return null;
                    }
                })
                .withLowLatencyPowerMode()
                .withScannerInForegroundService(createNotification())
                .build();
        } else {
            Toast.makeText(mContext, "NO Internet Connection, Restart app once internet connection has been established", Toast.LENGTH_LONG).show();
        }
    }

    public void addProximityZone(List<String> zones) {
        mBeaconZones = new ArrayList<>(zones.size());
        if(!zones.isEmpty()) {
            if (zones.size() >= 1) {
                if (zones.contains("")) {
                    atLeastOneZoneSelected = false;
                } else {
                    atLeastOneZoneSelected = true;
                    for (String zoneTag : zones){
                        mBeaconZones.add(addProximityZone(zoneTag));
                    }
                }
            }
        }
    }

    public ProximityZone addProximityZone(String beaconZone) {
        ProximityZone tempProxZone = new ProximityZoneBuilder()
                .forTag(beaconZone)
                .inFarRange()
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext attachment) {
                        Log.e("DataManager", "Institution " + attachment.getAttachments().get("Institution") + " - Bus Stop:  "+attachment.getAttachments().get("Bus Stop") + "A rrived");
                        if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                            proximityBeaconInterface.onEnterBeaconRegion(attachment);
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext attachment) {
                        Log.e("DataManager", "Bye bye," + attachment.getAttachments().get("Institution") + " - Bus Stop:  "+attachment.getAttachments().get("Bus Stop") + " left");
                        if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                            proximityBeaconInterface.onExitBeaconRegion(attachment);
                        return null;
                    }
                })
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    public Unit invoke(Set<? extends ProximityZoneContext> proximityZoneContexts) {
                        List<String> busStops = new ArrayList<>();
                        for (ProximityZoneContext attachment : proximityZoneContexts) {
                            busStops.add(attachment.getAttachments().get("Bus Stop"));
                            if (proximityBeaconInterface instanceof ProximityBeaconInterface)
                                proximityBeaconInterface.onChangeActionInRegion(proximityZoneContexts);
                        }
                        Log.e("DataManager", "On Change Called: " + busStops);
                        return null;
                    }}).build();
        return  tempProxZone;
    }

    public ProximityObserver getBeaconObserver() {
        return beaconObserver;
    }

    public void setServiceListener(ProximityBeaconInterface beaconListener){
        proximityBeaconInterface = beaconListener;
    }

    public void startBeaconObserver(){
        if(beaconObserverHandler == null && atLeastOneZoneSelected) {
            beaconObserverHandler = beaconObserver.startObserving(mBeaconZones);
            Log.d("datamanager","beacon scan started");
        }

    }

    public void stopBeaconObserver(){
        if (beaconObserverHandler != null ) {
            beaconObserverHandler.stop();
            beaconObserverHandler = null;
            beaconObserver = null;
            atLeastOneZoneSelected = false;
        }
    }

    public boolean isNetworkAvailable() {

        boolean connection;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        connection = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return connection;
    }
}
