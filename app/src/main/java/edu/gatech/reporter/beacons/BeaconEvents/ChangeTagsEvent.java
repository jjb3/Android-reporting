package edu.gatech.reporter.beacons.BeaconEvents;

import java.util.List;

public class ChangeTagsEvent {

    private List<String> beaconZonesList;

    public ChangeTagsEvent(List<String> beaconZonesList) {
        this.beaconZonesList = beaconZonesList;
    }

    public List<String> getBeaconZonesList() {
        return beaconZonesList;
    }

    public void setBeaconZonesList(List<String> beaconZonesList) {
        this.beaconZonesList = beaconZonesList;
    }
}
