package edu.gatech.reporter.beacons.Database;

public class AddBeaconZoneEvent {

    private BeaconZone beaconZone;

    public AddBeaconZoneEvent(BeaconZone beaconZone) {
        this.beaconZone = beaconZone;
    }

    public BeaconZone getBeaconZone() {
        return beaconZone;
    }

    public void setBeaconZone(BeaconZone beaconZone) {
        this.beaconZone = beaconZone;
    }
}

