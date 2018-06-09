package edu.gatech.reporter.beacons;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.reporter.app.ReporterHome;
import edu.gatech.reporter.utils.Const;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProximityBeaconImplementation {

    private static ProximityObserver beaconObserver;
    private static ProximityObserver.Handler beaconObserverHandler;
    private static ProximityBeaconInterface proximityBeaconImplementor;

    private static ProximityBeaconImplementation instance;

    private ProximityBeaconImplementation(ProximityBeaconInterface activity,
                                         List<BeaconZone> institutionsTracked) {
        proximityBeaconImplementor = activity;
        initProximityObserver(proximityBeaconImplementor);

        for(int i = 0 ; i < institutionsTracked.size() ; i++){
            addProximityZone(institutionsTracked.get(i));
        }
    }

    private ProximityBeaconImplementation(ProximityBeaconInterface activity) {
        proximityBeaconImplementor = activity;
        initProximityObserver(proximityBeaconImplementor);

    }

    public static ProximityBeaconImplementation getInstance(ProximityBeaconInterface proximityBeaconInterface){

        if (instance == null){
            instance = new ProximityBeaconImplementation(proximityBeaconInterface);
        }
        return instance;
    }

    public static ProximityBeaconImplementation getInstance(ProximityBeaconInterface proximityBeaconInterface,
                                                     List<BeaconZone> institutionsTracked){
        if (instance == null){
            instance = new ProximityBeaconImplementation(proximityBeaconInterface, institutionsTracked);
        }
        return instance;
    }


    public void initProximityObserver(ProximityBeaconInterface applicationContext) {

        beaconObserver = new ProximityObserverBuilder(((Context) applicationContext).getApplicationContext(),
                new EstimoteCloudCredentials("androidreporter-lk7", "41674213f80c533d22ce5aed52865253"))
                .withOnErrorAction(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Log.e("app", "proximity observer error: " + throwable);
                        return null;
                    }
                })
                .withBalancedPowerMode()
                .build();
    }

    public void addProximityZone(ProximityZone zone) {
        beaconObserver.addProximityZone(zone);
    }

    public void addProximityZone(List<BeaconZone> zones) {

        for(int i = 0 ; i < zones.size() ; i++){
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
                        Log.d("app", "Institution " + attachment.getPayload().get("Institution") + " - Bus Stop:  "+attachment.getPayload().get("Bus Stop"));
                        proximityBeaconImplementor.onEnterBeaconRegion(attachment);
                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.d("app", "Bye bye, come visit us again on from the first try");
                        proximityBeaconImplementor.onExitBeaconRegion(attachment);
                        return null;
                    }
                })
                .withOnChangeAction(new Function1<List<? extends ProximityAttachment>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends ProximityAttachment> attachments) {
                        List<String> busStops = new ArrayList<>();
                        for (ProximityAttachment attachment : attachments) {
                            busStops.add(attachment.getPayload().get("Bus Stop"));
                            proximityBeaconImplementor.onChangeActionInRegion(attachment);
                        }
                        Log.d("app", "Nearby desks: " + busStops);
                        return null;
                    }
                }).create();
        beaconObserver.addProximityZones(tempProxZone);

    }

    public void startBeaconObserver(){
        beaconObserverHandler = beaconObserver.start();
    }

    public void stopBeaconObserver(){
        beaconObserverHandler.stop();
        beaconObserver = null;
    }

}
