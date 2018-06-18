package edu.gatech.reporter.beacons.Database;

import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import java.util.HashMap;
import java.util.List;

public class UpdateBeaconZonesEvent {

    HashMap<String, ProximityAttachment> nearbyBeaconsList;

    public UpdateBeaconZonesEvent(HashMap<String, ProximityAttachment> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }

    public HashMap<String, ProximityAttachment> getNearbyBeaconsMap() {
        return nearbyBeaconsList;
    }

    public void setNearbyBeaconsList(HashMap<String, ProximityAttachment> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }
}
