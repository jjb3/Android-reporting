package edu.gatech.reporter.app;

import com.estimote.analytics_plugin.dagger.ProximityAnalyticsReporterModule_ProvideTimerFactory;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import org.greenrobot.eventbus.EventBus;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;


import edu.gatech.reporter.beacons.Database.UpdateBeaconZonesEvent;
import edu.gatech.reporter.utils.Const;

public class UpdateNearBeaconsTask extends TimerTask {

    HashMap<String, List<ProximityAttachment>> nearbyBeacons;
    HashMap<String, String> nearbyBeaconZones;

    public UpdateNearBeaconsTask(HashMap<String, List<ProximityAttachment>> nearbyBeacons) {
        this.nearbyBeacons = nearbyBeacons;
    }

    public void run() {
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(nearbyBeacons));
    }

    public void updateNearbyBeaconList(HashMap<String, List<ProximityAttachment>> nearbyBeaconsList, HashMap<String, String> zones, List<? extends  ProximityAttachment> attachments) {
        nearbyBeaconZones = zones;

        // remove to start fresh the zone.
        String zoneToUpdate = attachments.get(0).getPayload().get(Const.BEACON_INSTITUTION_KEY);
        nearbyBeaconsList.put(zoneToUpdate, (List<ProximityAttachment>) attachments);
    }
}
