package edu.gatech.reporter.app;

import com.estimote.proximity_sdk.proximity.ProximityContext;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;


import edu.gatech.reporter.beacons.BeaconEvents.UpdateBeaconZonesEvent;
import edu.gatech.reporter.beacons.NearbyBeaconManager;
import edu.gatech.reporter.utils.Const;

public class UpdateNearBeaconsTask extends TimerTask {

    HashMap<String, List<ProximityContext>> nearbyBeacons;
    HashMap<String, String> nearbyBeaconZones;

    private NearbyBeaconManager myNearbyBeaconManager;

    public UpdateNearBeaconsTask(NearbyBeaconManager nearbyBeaconsManager) {
        myNearbyBeaconManager = nearbyBeaconsManager;
    }

    public UpdateNearBeaconsTask(HashMap<String, List<ProximityContext>> nearbyBeacons) {
        this.nearbyBeacons = nearbyBeacons;
    }

    public void run() {
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(myNearbyBeaconManager.getNearbyBeacons()));
    }

    public void updateNearbyBeaconList(HashMap<String, List<ProximityContext>> nearbyBeaconsList, HashMap<String, String> zones, List<? extends  ProximityContext> attachments) {
        nearbyBeaconZones = zones;

        // remove to start fresh the zone.
        String zoneToUpdate = attachments.get(0).getAttachments().get(Const.BEACON_INSTITUTION_KEY);
        myNearbyBeaconManager.getNearbyBeacons().put(zoneToUpdate, (List<ProximityContext>) attachments);
    }
}
