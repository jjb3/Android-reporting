package edu.gatech.reporter.beacons.Database;

public class BeaconZoneEvent {

    private BeaconZone beaconZone;

    public BeaconZoneEvent(BeaconZone beaconZone) {
        this.beaconZone = beaconZone;
    }

    public BeaconZone getBeaconZone() {
        return beaconZone;
    }

    public void setBeaconZone(BeaconZone beaconZone) {
        this.beaconZone = beaconZone;
    }
}
