package edu.gatech.reporter.app;

import com.estimote.analytics_plugin.dagger.ProximityAnalyticsReporterModule_ProvideTimerFactory;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;


import edu.gatech.reporter.beacons.Database.UpdateBeaconZonesEvent;
import edu.gatech.reporter.utils.Const;

public class UpdateNearBeaconsTask extends TimerTask {

    HashMap<String, ProximityAttachment> nearbyBeacons;
    HashMap<String, String> nearbyBeaconZones;

    public UpdateNearBeaconsTask(HashMap<String, ProximityAttachment> nearbyBeacons) {
        this.nearbyBeacons = nearbyBeacons;
    }

    public void run() {
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(nearbyBeacons));
    }

    public void updateNearbyBeaconList(HashMap<String, ProximityAttachment> nearbyBeaconsList, HashMap<String, String> zones, List<? extends  ProximityAttachment> attachments){
        nearbyBeaconZones = zones;

        for(ProximityAttachment beaconAttch : attachments) {
            if (nearbyBeaconZones.containsKey(beaconAttch.getPayload().get(Const.BEACON_INSTITUTION_KEY))){
                nearbyBeacons.put(beaconAttch.getDeviceId(), beaconAttch);
            } else
                nearbyBeacons.remove(beaconAttch.getDeviceId());
        }

        this.nearbyBeacons = nearbyBeaconsList;
    }
}
