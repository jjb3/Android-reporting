package edu.gatech.reporter.beacons;

import com.estimote.coresdk.common.internal.utils.L;
import com.estimote.coresdk.observation.utils.Proximity;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import java.util.List;

public interface ProximityBeaconInterface {

    void onEnterBeaconRegion(ProximityAttachment attachments);

    void onExitBeaconRegion(ProximityAttachment attachments);

}
