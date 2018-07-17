package edu.gatech.reporter.utils.ParameterTrackers;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;

/**
 * Created by Wendi on 2016/9/22.
 */
public class DeviceIDManager {
    private TelephonyManager teleManager;
    WifiManager wifiManager;
    public DeviceIDManager(){
        teleManager = (TelephonyManager) ReporterService.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager  = (WifiManager)ReporterService.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    public String getIMEI(){
//        try{
//            Parameters.getInstance().imei = teleManager.getDeviceId();
//            if(Parameters.getInstance().imei == null) {
//                Parameters.getInstance().imei = "No sim card";
//            }
//        }catch(SecurityException e){
//            Parameters.getInstance().imei = "Permission error. No right to access IMEI.";
//            Log.d("Permission Denied", "Permission Denied");
//        }
//
//        return Parameters.getInstance().imei;
        try{
            if(teleManager.getDeviceId() == null) {
                return Settings.Secure.getString(ReporterService.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                return teleManager.getDeviceId();
            }
        }catch(SecurityException e){
            Log.d("Permission Denied", "Permission Denied");
            return Const.NO_ID;
        }
    }

    public String getSecureID(){
        return Settings.Secure.getString(ReporterService.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Original posted by robin on http://robinhenniges.com/en/android6-get-mac-address-programmatically
     * @return
     */
    public String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}
