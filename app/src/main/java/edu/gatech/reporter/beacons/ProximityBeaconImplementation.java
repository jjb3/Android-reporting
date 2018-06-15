package edu.gatech.reporter.beacons;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.reporter.R;
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



    private ProximityBeaconImplementation(Context context) {
        mContext = context;
        initProximityObserver();

    }

    private Notification createNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "1")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("beacon Scanning")
                .setContentText("LEt you know that bluetooth scanning is running.")
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

        beaconObserver = new ProximityObserverBuilder(mContext.getApplicationContext(),
                new EstimoteCloudCredentials("androidreporter-lk7", "41674213f80c533d22ce5aed52865253"))
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
    }

    public void addProximityZone(ProximityZone zone) {
        beaconObserver.addProximityZone(zone);
    }

    public void addProximityZone(List<BeaconZone> zones) {

        for(int i = 0 ; i < zones.size() ; i++){
            if(zones.get(i).isSelected())
                addProximityZone(zones.get(i));
        }
    }

    public void addProximityZone(BeaconZone beaconZone) {
        ProximityZone tempProxZone = beaconObserver.zoneBuilder()
                .forAttachmentKeyAndValue(Const.KEY_INSTITUTION, beaconZone.getZoneName())
                .inFarRange()
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.e("DataManager", "Institution " + attachment.getPayload().get("Institution") + " - Bus Stop:  "+attachment.getPayload().get("Bus Stop") + "A rrived");
                        if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                            proximityBeaconInterface.onEnterBeaconRegion(attachment);
                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.e("DataManager", "Bye bye," + attachment.getPayload().get("Institution") + " - Bus Stop:  "+attachment.getPayload().get("Bus Stop") + " left");
                        if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                            proximityBeaconInterface.onExitBeaconRegion(attachment);
                        return null;
                    }
                })
                .withOnChangeAction(new Function1<List<? extends ProximityAttachment>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends ProximityAttachment> attachments) {
                        List<String> busStops = new ArrayList<>();
                        for (ProximityAttachment attachment : attachments) {
                            busStops.add(attachment.getPayload().get("Bus Stop"));
                            if(proximityBeaconInterface instanceof ProximityBeaconInterface)
                                proximityBeaconInterface.onChangeActionInRegion(attachments);
                        }
                        Log.e("DataManager", "On Change Called: " + busStops);
                        return null;
                    }
                }).create();
        beaconObserver.addProximityZones(tempProxZone);

    }

    public void setServiceListener(ProximityBeaconInterface beaconListener){
        proximityBeaconInterface = beaconListener;
    }

    public void startBeaconObserver(){
        if(beaconObserverHandler == null)
            beaconObserverHandler = beaconObserver.start();
    }

    public void stopBeaconObserver(){
        beaconObserverHandler.stop();
        beaconObserverHandler = null;
        beaconObserver = null;
    }

}
