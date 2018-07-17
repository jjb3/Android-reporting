package edu.gatech.reporter.beacons.Database;

import com.estimote.coresdk.recognition.packets.Beacon;

import java.util.List;

public class BeaconZonesEvent {

    private List<BeaconZone> beaconZonesList;

    public BeaconZonesEvent(List<BeaconZone> beaconZonesList) {
        this.beaconZonesList = beaconZonesList;
    }

    public List<BeaconZone> getBeaconZonesList() {
        return beaconZonesList;
    }

    public void setBeaconZonesList(List<BeaconZone> beaconZonesList) {
        this.beaconZonesList = beaconZonesList;
    }
}
