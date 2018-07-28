package edu.gatech.reporter.beacons;



import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.List;
import java.util.Set;

public interface ProximityBeaconInterface {

    void onEnterBeaconRegion(ProximityZoneContext attachments);

    void onExitBeaconRegion(ProximityZoneContext attachments);

    void onChangeActionInRegion(Set<? extends ProximityZoneContext> attachments);

}
