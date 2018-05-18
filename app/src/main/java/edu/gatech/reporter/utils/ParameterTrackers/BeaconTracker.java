package edu.gatech.reporter.utils.ParameterTrackers;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;

/**
 * Created by Wendi on 2016/10/10.
 */

public class BeaconTracker implements BeaconManager.RangingListener{
    private BeaconManager beaconManager;
    private String nearestBeaconID = Const.NO_BEACON_DETECTED;

    public BeaconTracker(){
        beaconManager = new BeaconManager(ReporterService.getContext());
        final BeaconManager.RangingListener self = this;
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                //Region to listen to
                beaconManager.startRanging(new Region("monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        null, null));
                beaconManager.setRangingListener(self);
            }
        });
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        if (!list.isEmpty()) {
            Beacon nearestBeacon = list.get(0);
            if(nearestBeacon == null){
                nearestBeaconID = Const.NO_BEACON_DETECTED;
            }
            nearestBeaconID = nearestBeacon.getProximityUUID().toString() +":"+ nearestBeacon.getMajor() +":"+ nearestBeacon.getMinor();
        }else{
            nearestBeaconID = Const.NO_BEACON_DETECTED;
        }
    }

    public String getNearestBeaconID(){
        return nearestBeaconID;
    }
}
