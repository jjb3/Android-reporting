package edu.gatech.reporter.utils.ParameterTrackers;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;


/**
 * Created on 2016/8/31.
 */
public class BatteryTracker implements StartStopSensorInterface, BatteryTrackerBroadcastReceiver.BatteryChangeInterface {

    private IntentFilter ifilter;
    private BatteryTrackerBroadcastReceiver mReceiver;
    private  Intent batteryStatus;
    private int level;
    private int scale;
    private float batteryPct;

    public BatteryTracker(){
        mReceiver = new BatteryTrackerBroadcastReceiver();
        mReceiver.setBatteryChangeStatusListener(this);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    }

    public Float getPowerState(){
        if(ParameterOptions.getInstance().powerLevelChk) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryPct = level / (float) scale;
            //Parameters.getInstance().batteryPct = batteryPct;
            isUsingExternalPower();
            return batteryPct;
        } else
            return (float) -1.0;
    }

    @Override
    public Float onBatteryEventChanged(Intent intent) {
        batteryStatus = intent;
        level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        batteryPct = level / (float)scale;
        //Parameters.getInstance().batteryPct = batteryPct;
        isUsingExternalPower();
        return batteryPct;
    }

    public Float getBatteryTemp(){
        return (float)(batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)/10);
    }

    public boolean isUsingExternalPower(){
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;

        // How are we charging?
//        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
//        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
//        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
//        if(usbCharge){
//            Parameters.getInstance().externalPower = Const.USB_CHARGE;
//        }else if(acCharge){
//            Parameters.getInstance().externalPower = Const.AC_CHARGE;
//        }else{
//            Parameters.getInstance().externalPower = Const.NOT_CHARGE;
//        }
    }

    public int getChargeStatus(){
        if(ParameterOptions.getInstance().powerLevelChk) {
            if (!isUsingExternalPower()) {
                return Const.NOT_CHARGE;
            } else if (batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_USB) {
                return Const.AC_CHARGE;
            } else {
                return Const.USB_CHARGE;
            }
        } else return Const.CHARGE_NOT_CHARGE;
    }

    @Override
    public void disableOrEnableSensor(boolean isEnabled) {
        if(isEnabled) {
            batteryStatus = ReporterService.getContext().registerReceiver(mReceiver, ifilter);
            mReceiver.isRegistered = isEnabled;
        }
        else {
            if(mReceiver.isRegistered) {
                ReporterService.getContext().unregisterReceiver(mReceiver);
                level = -1;
            }
        }
    }


}
