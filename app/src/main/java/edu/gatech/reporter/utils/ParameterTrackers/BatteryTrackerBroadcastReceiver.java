package edu.gatech.reporter.utils.ParameterTrackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryTrackerBroadcastReceiver extends BroadcastReceiver {

    BatteryChangeInterface batteryChangeInterface;
    boolean isRegistered;

    @Override
    public void onReceive(Context context, Intent intent) {
        batteryChangeInterface.onBatteryEventChanged(intent);
    }


    public void setBatteryChangeStatusListener(BatteryChangeInterface batteryChangeInterface){
        this.batteryChangeInterface = batteryChangeInterface;
    }

    public interface BatteryChangeInterface{
        Float onBatteryEventChanged(Intent intent);
    }
}
