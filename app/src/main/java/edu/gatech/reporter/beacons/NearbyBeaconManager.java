package edu.gatech.reporter.beacons;

import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import java.util.HashMap;
import java.util.List;

import edu.gatech.reporter.utils.Const;

public class NearbyBeaconManager {

    private HashMap<String, List<ProximityAttachment>> nearbyBeacons;
    private HashMap<String, String> nearbyBeaconZones;


    public NearbyBeaconManager() {
        nearbyBeacons = new HashMap<>();
        nearbyBeaconZones = new HashMap<>();
    }


    public HashMap<String, String> getNearbyBeaconZones() {
        return nearbyBeaconZones;
    }

    public HashMap<String, List<ProximityAttachment>> getNearbyBeacons() {
        return nearbyBeacons;
    }


    public void updateNearbyBeaconList(List<? extends  ProximityAttachment> attachments) {
        // remove to start fresh the zone.
        String zoneToUpdate = attachments.get(0).getPayload().get(Const.BEACON_INSTITUTION_KEY);
        nearbyBeacons.put(zoneToUpdate, (List<ProximityAttachment>) attachments);
    }

    public void sendData() {

    }


    //add the post request to send.
}
