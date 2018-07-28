package edu.gatech.reporter.beacons.BeaconEvents;



import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.HashMap;
import java.util.List;

public class UpdateBeaconZonesEvent {

    HashMap<String, List<ProximityZoneContext>> nearbyBeaconsList;

    public UpdateBeaconZonesEvent(HashMap<String, List<ProximityZoneContext>> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }

    public HashMap<String, List<ProximityZoneContext>> getNearbyBeaconsMap() {
        return nearbyBeaconsList;
    }

    public void setNearbyBeaconsList(HashMap<String, List<ProximityZoneContext>> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }
}
