package edu.gatech.reporter.beacons.Database;

import com.estimote.proximity_sdk.proximity.ProximityContext;

import java.util.HashMap;
import java.util.List;

public class UpdateBeaconZonesEvent {

    HashMap<String, List<ProximityContext>> nearbyBeaconsList;

    public UpdateBeaconZonesEvent(HashMap<String, List<ProximityContext>> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }

    public HashMap<String, List<ProximityContext>> getNearbyBeaconsMap() {
        return nearbyBeaconsList;
    }

    public void setNearbyBeaconsList(HashMap<String, List<ProximityContext>> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }
}
