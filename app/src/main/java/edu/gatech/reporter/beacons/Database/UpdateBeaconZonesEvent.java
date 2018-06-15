package edu.gatech.reporter.beacons.Database;

import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import java.util.List;

public class UpdateBeaconZonesEvent {

    List<? extends ProximityAttachment> nearbyBeaconsList;

    public UpdateBeaconZonesEvent(List<? extends ProximityAttachment> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }

    public List<? extends ProximityAttachment> getNearbyBeaconsList() {
        return nearbyBeaconsList;
    }

    public void setNearbyBeaconsList(List<? extends ProximityAttachment> nearbyBeaconsList) {
        this.nearbyBeaconsList = nearbyBeaconsList;
    }
}
