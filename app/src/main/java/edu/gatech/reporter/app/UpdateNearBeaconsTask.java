package edu.gatech.reporter.app;

import com.estimote.analytics_plugin.dagger.ProximityAnalyticsReporterModule_ProvideTimerFactory;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.TimerTask;

import edu.gatech.reporter.beacons.Database.BeaconZonesEvent;
import edu.gatech.reporter.beacons.Database.UpdateBeaconZonesEvent;

public class UpdateNearBeaconsTask extends TimerTask {

    List<? extends ProximityAttachment> nearbyBeacons;

    public UpdateNearBeaconsTask(List<? extends ProximityAttachment> nearbyBeacons) {
        this.nearbyBeacons = nearbyBeacons;
    }

    public void run() {
        EventBus.getDefault().post(new UpdateBeaconZonesEvent(nearbyBeacons));
    }
}
