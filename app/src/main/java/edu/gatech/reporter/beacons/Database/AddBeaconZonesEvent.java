package edu.gatech.reporter.beacons.Database;

import java.util.List;

public class AddBeaconZonesEvent {

    private List<BeaconZone> beaconZonesList;

    public AddBeaconZonesEvent(List<BeaconZone> beaconZonesList) {
        this.beaconZonesList = beaconZonesList;
    }

    public List<BeaconZone> getBeaconZonesList() {
        return beaconZonesList;
    }

    public void setBeaconZonesList(List<BeaconZone> beaconZonesList) {
        this.beaconZonesList = beaconZonesList;
    }
}
