package edu.gatech.reporter.beacons.Database;

public class SelectBeaconZoneEvent {

    private BeaconZone beaconZone;

    public SelectBeaconZoneEvent(BeaconZone beaconZone) {
        this.beaconZone = beaconZone;
    }

    public BeaconZone getBeaconZone() {
        return beaconZone;
    }

    public void setBeaconZone(BeaconZone beaconZone) {
        this.beaconZone = beaconZone;
    }

}
