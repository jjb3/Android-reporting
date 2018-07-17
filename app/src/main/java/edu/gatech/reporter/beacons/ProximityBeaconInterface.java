package edu.gatech.reporter.beacons;


import com.estimote.proximity_sdk.proximity.ProximityContext;

import java.util.List;

public interface ProximityBeaconInterface {

    void onEnterBeaconRegion(ProximityContext attachments);

    void onExitBeaconRegion(ProximityContext attachments);

    void onChangeActionInRegion(List<? extends ProximityContext> attachments);

}
