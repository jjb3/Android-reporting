package edu.gatech.reporter.beacons.Database;

import com.estimote.proximity_sdk.proximity.ProximityAttachment;

public class UpdateBeaconZoneEvent {

    ProximityAttachment proximityAttachment;

    public UpdateBeaconZoneEvent(ProximityAttachment proximityAttachment) {
        this.proximityAttachment = proximityAttachment;
    }

    public ProximityAttachment getProximityAttachment() {
        return proximityAttachment;
    }

    public void setProximityAttachment(ProximityAttachment proximityAttachment) {
        this.proximityAttachment = proximityAttachment;
    }
}
