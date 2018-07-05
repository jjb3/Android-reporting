package edu.gatech.reporter.beacons;

import android.app.Notification;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.proximity.ProximityContext;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.reporter.R;
import edu.gatech.reporter.app.ConnectionReceiver;
import edu.gatech.reporter.app.ReporterHome;
import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.beacons.Database.BeaconZone;
import edu.gatech.reporter.utils.Const;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProximityBeaconImplementation {

    private static ProximityObserver beaconObserver;
    private static ProximityObserver.Handler beaconObserverHandler;
    private static Context mContext;

    private static ProximityBeaconImplementation instance;
    private static ProximityBeaconInterface proximityBeaconInterface;
    //private static ConnectionReceiver connectionReceiver;

    private boolean isNetworkAvailable;

    private List<BeaconZone> mBeaconZones;

    private ProximityBeaconImplementation(Context context) {
        mContext = context;
//        connectionReceiver = new ConnectionReceiver();
//        connectionReceiver.setmContext(mContext);
//        connectionReceiver.setConnectionReceiverListener(this);
        //initProximityObserver();

    }

    private Notification createNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "1")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("beacon Scanning")
                .setContentText("Bluetooth Beacon scan is running, if no internet connection, scanning is not possible.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

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
                .withOnErrorAction(new Function1<Throwable, Unit>() {
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

    public void initProximityObserver(List<BeaconZone> beaconZones) {
        if(isNetworkAvailable && beaconObserver == null) {
            beaconObserver = new ProximityObserverBuilder(mContext.getApplicationContext(),
                    new EstimoteCloudCredentials(mContext.getString(R.string.appId), mContext.getString(R.string.appToken)))
                    .withOnErrorAction(new Function1<Throwable, Unit>() {
                        @Override
                        public Unit invoke(Throwable throwable) {
                            Log.e("app", "proximity observer error: " + throwable);
                            return null;
                        }
                    })
                    .withLowLatencyPowerMode()
                    .withScannerInForegroundService(createNotification())
                    .build();

            for(int i = 0 ; i < beaconZones.size() ; i++){
                if(beaconZones.get(i).isSelected())
                    addProximityZone(beaconZones.get(i));
            }
            beaconObserver.start();
        } else {
            Toast.makeText(mContext, "NO Internet Connection, Restart app once internet connection has been established", Toast.LENGTH_LONG).show();
        }
    }

    public void addProximityZone(List<BeaconZone> zones) {
        mBeaconZones = zones;
        for(int i = 0 ; i < zones.size() ; i++){
            if(zones.get(i).isSelected())
                addProximityZone(zones.get(i));
        }
    }

    public void addProximityZone(BeaconZone beaconZone) {
        ProximityZone tempProxZone = beaconObserver.zoneBuilder()
                .forTag(beaconZone.getZoneName())
                .inFarRange()
                .withOnEnterAction(new Function1<ProximityContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityContext attachment) {
                        Log.e("DataManager", "Institution " + attachment.getAttachments().get("Institution") + " - Bus Stop:  "+attachment.getAttachments().get("Bus Stop") + "A rrived");
                        if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                            proximityBeaconInterface.onEnterBeaconRegion(attachment);
                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityContext attachment) {
                        Log.e("DataManager", "Bye bye," + attachment.getAttachments().get("Institution") + " - Bus Stop:  "+attachment.getAttachments().get("Bus Stop") + " left");
                        if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                            proximityBeaconInterface.onExitBeaconRegion(attachment);
                        return null;
                    }
                })
                .withOnChangeAction(new Function1<List<? extends ProximityContext>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends ProximityContext> attachments) {
                        List<String> busStops = new ArrayList<>();
                        for (ProximityContext attachment : attachments) {
                            busStops.add(attachment.getAttachments().get("Bus Stop"));
                            if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                                proximityBeaconInterface.onChangeActionInRegion(attachments);
                        }
                        Log.e("DataManager", "On Change Called: " + busStops);
                        return null;
                    }
                }).create();
        beaconObserver.addProximityZones(tempProxZone);

    }

    public ProximityObserver getBeaconObserver() {
        return beaconObserver;
    }

    public void setServiceListener(ProximityBeaconInterface beaconListener){
        proximityBeaconInterface = beaconListener;
    }

    public void startBeaconObserver(){
        if(beaconObserverHandler == null) {
            beaconObserverHandler = beaconObserver.start();
            Log.d("datamanager","beacon scan started");
        }

    }

    public void stopBeaconObserver(){
        if (beaconObserverHandler != null ) {
            beaconObserverHandler.stop();
            beaconObserverHandler = null;
            beaconObserver = null;
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

//    @Override
//    public void onNetworkConnectionChanged(boolean isConnected) {
//
//        if(!isNetworkAvailable()) {
//            isNetworkAvailable = isConnected;
//            initProximityObserver(mBeaconZones);
//        }
//    }
}
