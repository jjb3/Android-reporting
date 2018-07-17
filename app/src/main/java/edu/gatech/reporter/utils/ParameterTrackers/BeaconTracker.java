package edu.gatech.reporter.utils.ParameterTrackers;

import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;

/**
 * Created by Wendi on 2016/10/10.
 */

public class BeaconTracker{

    private BeaconManager beaconManager;
    private String nearestBeaconsID = Const.NO_BEACON_DETECTED;
    private BeaconRegion region;
    private long pastTimeBeaconDiscovered;

    public BeaconTracker(){
        beaconManager = new BeaconManager(ReporterService.getContext());
        pastTimeBeaconDiscovered = System.currentTimeMillis();

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                pastTimeBeaconDiscovered = System.currentTimeMillis();
                nearestBeaconsID = "";
                Log.d("app", "onBeaconsDiscovered: beacon discovered");

                if (!beacons.isEmpty()) {
                    for(Beacon beacon : beacons){
                        nearestBeaconsID = nearestBeaconsID + beacon.getProximityUUID().toString() + ":"+ beacon.getMajor() +":"+ beacon.getMinor() + "\n";
                    }

                } else {
                    nearestBeaconsID = Const.NO_BEACON_DETECTED;
                }
            }
        });

        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                null, null);


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                //Region to listen to
                //beaconManager.startRanging(region);

            }
        });
    }
    public String getNearestBeaconsID(){
        long interval = System.currentTimeMillis() - pastTimeBeaconDiscovered;
        if( interval > 5000)    // this interval depends on how frequent the beacon advertise itself.
            nearestBeaconsID = Const.NO_BEACON_DETECTED;

//        Log.d("app", "interval passed" + String.valueOf(interval));
        return nearestBeaconsID;
    }
}
