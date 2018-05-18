package edu.gatech.reporter.utils.ParameterTrackers;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;


/**
 * Created on 2016/8/31.
 */
public class BatteryTracker {

    private IntentFilter ifilter;
    private  Intent batteryStatus;
    private int level;
    private int scale;
    private float batteryPct;

    public BatteryTracker(){
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = ReporterService.getContext().registerReceiver(null, ifilter);
    }

    public Float getPowerState(){
        batteryStatus = ReporterService.getContext().registerReceiver(null, ifilter);
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
        if(!isUsingExternalPower()){
            return Const.NOT_CHARGE;
        }else if(batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_USB){
            return Const.AC_CHARGE;
        }else{
            return Const.USB_CHARGE;
        }
    }

}
