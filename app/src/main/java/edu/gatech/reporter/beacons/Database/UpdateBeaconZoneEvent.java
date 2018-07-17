package edu.gatech.reporter.beacons.Database;

import com.estimote.proximity_sdk.proximity.ProximityContext;

public class UpdateBeaconZoneEvent {

    ProximityContext ProximityContext;

    public UpdateBeaconZoneEvent(ProximityContext ProximityContext) {
        this.ProximityContext = ProximityContext;
    }

    public ProximityContext getProximityContext() {
        return ProximityContext;
    }

    public void setProximityContext(ProximityContext ProximityContext) {
        this.ProximityContext = ProximityContext;
    }
}
