package edu.gatech.reporter.beacons;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import java.util.List;

import edu.gatech.reporter.app.ReporterHome;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProximityBeaconImplementation {

    ProximityObserver beaconObserver;
    ProximityObserver.Handler beaconObserverHandler;
    ProximityBeaconInterface proximityBeaconImplementor;

    public ProximityBeaconImplementation(ProximityBeaconInterface activity) {
        proximityBeaconImplementor = activity;
        initProximityObserver(activity);

        ProximityZone zone1 = beaconObserver.zoneBuilder()
                .forAttachmentKeyAndValue("Location", "Home Location - Panama")
                .inFarRange()
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.d("app", "Welcome to my first try");
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
                .create();
        beaconObserver.addProximityZones(zone1);
    }

    private void initProximityObserver(ProximityBeaconInterface activity) {

        beaconObserver = new ProximityObserverBuilder((Activity) activity,
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

    public void addProximityZone(List<ProximityZone> zones) {
        beaconObserver.addProximityZones(zones);
    }

    public void addProximityZone(String attachmentKey, String attachmentValue) {
        ProximityZone tempProxZone = beaconObserver.zoneBuilder()
                .forAttachmentKeyAndValue(attachmentKey, attachmentValue)
                .inFarRange()
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.d("app", "Welcome to my first try");
                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.d("app", "Bye bye, come visit us again on from the first try");
                        return null;
                    }
                })
                .create();
        beaconObserver.addProximityZones(tempProxZone);
    }

    public void startBeaconObserver(){
        beaconObserverHandler = beaconObserver.start();
    }

    public void stopBeaconObserver(){
        beaconObserverHandler.stop();
        proximityBeaconImplementor = null;
    }

}
