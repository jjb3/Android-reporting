package edu.gatech.reporter.utils.ParameterManager;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.estimote.proximity_sdk.proximity.ProximityContext;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.reporter.BuildConfig;
import edu.gatech.reporter.ServiceRequests.BeaconServiceRequests;
import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.beacons.Database.UpdateBeaconZonesEvent;
import edu.gatech.reporter.beacons.NearbyBeaconManager;
import edu.gatech.reporter.utils.Connection;
import edu.gatech.reporter.utils.Const;
import edu.gatech.reporter.utils.ParameterTrackers.BatteryTracker;
import edu.gatech.reporter.utils.ParameterTrackers.DeviceIDManager;
import edu.gatech.reporter.utils.ParameterTrackers.GPSTracker;
import edu.gatech.reporter.utils.ParameterTrackers.LightSensor;
import edu.gatech.reporter.utils.ParameterTrackers.MotionSensor;
import edu.gatech.reporter.utils.ParameterTrackers.NetworkManager;
import edu.gatech.reporter.utils.ParameterTrackers.PressureSensor;
import edu.gatech.reporter.utils.ParameterTrackers.TemperatureSensor;

/**
 * Created by Wendi on 2016/10/1.
 */
public class DataManager {
    private BatteryTracker myBatteryTracker;
    private Connection myConnection;
    private NearbyBeaconManager myNearbyBeaconManager;
    private HashMap<String, String> data = new HashMap();
    private NetworkManager myNetWorkManager;
    private GPSTracker myGPSTracker;
    private MotionSensor myMotionSensor;
    private DeviceIDManager myIDManager;
    private LightSensor myLightSensor;
    private PressureSensor myPressureSensor;
    private TemperatureSensor myTemperatureSensor;
    private HashMap<String, ProximityContext> beaconsInRange = new HashMap<>();

    private static final String TAG = "DataManager";
    String versionName = BuildConfig.VERSION_NAME;

    public DataManager(Context context){
        myGPSTracker = new GPSTracker();
        myMotionSensor = new MotionSensor();
        myIDManager = new DeviceIDManager();
        myBatteryTracker = new BatteryTracker();
        myConnection = new Connection();
        myNearbyBeaconManager = new NearbyBeaconManager(context);
        myNetWorkManager = new NetworkManager();
        myTemperatureSensor = new TemperatureSensor();
        myLightSensor = new LightSensor();
        myPressureSensor = new PressureSensor();

        Parameters.getInstance().imei = myIDManager.getIMEI();
        Parameters.getInstance().secureID = myIDManager.getSecureID();
        Parameters.getInstance().macAddress = myIDManager.getMACAddress();
        data.put("data","1");
    }

    public void sendData(){
        myConnection.issueJSONPostRequest(ParameterOptions.getInstance().serverURL,data);
    }

    private void updateData(){
        Parameters.getInstance().batteryPct = myBatteryTracker.getPowerState();
        Parameters.getInstance().sensorData = myMotionSensor.getMotionSensorData();
        Parameters.getInstance().location = myGPSTracker.getLocation();
        Parameters.getInstance().illuminance = myLightSensor.getIlluminance();
        Parameters.getInstance().temperature = myTemperatureSensor.getTemperature();
        Parameters.getInstance().pressure = myPressureSensor.getPressure();
        Parameters.getInstance().internetConnectionState = myNetWorkManager.getNetworkState();;
        Parameters.getInstance().externalPower = myBatteryTracker.getChargeStatus();
        Parameters.getInstance().gpsSpeed = myGPSTracker.getGPSSpeed();
        Parameters.getInstance().gpsAccuracy = myGPSTracker.getGPSAccuracy();
        Parameters.getInstance().gpsTime = myGPSTracker.getGpsTime();
        Parameters.getInstance().heading = myGPSTracker.getHeading();
    }

    private void updateBeaconData() {

        List<ProximityContext> beacons = new ArrayList<>();
        HashMap<String, List<ProximityContext>> nearbyBeacons  = myNearbyBeaconManager.getNearbyBeacons();
        for (Map.Entry<String, List<ProximityContext>> entry : nearbyBeacons.entrySet()) {
            beacons.addAll(entry.getValue());
        }
        List<String> beaconIds  = new ArrayList<>();

        for(ProximityContext beacon : beacons)
            beaconIds.add(beacon.getInfo().getDeviceId());

        Gson gson = new Gson();
        String beaconsList = gson.toJson(beaconIds);
        data.put("beacons_id", beaconsList);
    }

    private void updateMapData(){
        data.put("version_name", versionName);
//        Log.d(TAG, versionName);
        data.put("power_level", (ParameterOptions.getInstance().powerLevelChk) ? String.valueOf(Parameters.getInstance().batteryPct):Const.DISABLE_UPLOAD);
        data.put("external_power",(ParameterOptions.getInstance().powerLevelChk)? String.valueOf(Parameters.getInstance().externalPower):Const.DISABLE_UPLOAD);
        data.put("lat",(ParameterOptions.getInstance().locationDataChk)?
                ((Parameters.getInstance().location[0] != null)? String.valueOf(Parameters.getInstance().location[0])
                        :Const.DISABLE_UPLOAD):Const.DISABLE_UPLOAD);
        data.put("lng",(ParameterOptions.getInstance().locationDataChk)?
                ((Parameters.getInstance().location[1] != null)? String.valueOf(Parameters.getInstance().location[1])
                        :Const.DISABLE_UPLOAD):Const.DISABLE_UPLOAD);
        data.put("heading",(ParameterOptions.getInstance().locationDataChk)?
                ((Parameters.getInstance().heading != null)? String.valueOf(Parameters.getInstance().heading)
                        :Const.DISABLE_UPLOAD):Const.DISABLE_UPLOAD);
        data.put("speed",(ParameterOptions.getInstance().locationDataChk)?
                ((Parameters.getInstance().gpsSpeed != null)? String.valueOf(Parameters.getInstance().gpsSpeed)
                        :Const.DISABLE_UPLOAD):Const.DISABLE_UPLOAD);
        data.put("accuracy",(ParameterOptions.getInstance().locationDataChk)?
                ((Parameters.getInstance().gpsSpeed != null)? String.valueOf(Parameters.getInstance().gpsAccuracy)
                        :Const.DISABLE_UPLOAD):Const.DISABLE_UPLOAD);
        data.put("gps_time",(ParameterOptions.getInstance().locationDataChk)?
                ((Parameters.getInstance().gpsTime > 0 )? String.valueOf(getCurrentTimestamp(Parameters.getInstance().gpsTime))
                        :Const.DISABLE_UPLOAD):Const.DISABLE_UPLOAD);
        data.put("acceleration_x",(ParameterOptions.getInstance().accDataChk)? String.valueOf(Parameters.getInstance().sensorData[0]):Const.DISABLE_UPLOAD);
//        Log.e(TAG, "acceleration_x: " + String.valueOf(Parameters.getInstance().sensorData[0]));
        data.put("acceleration_y",(ParameterOptions.getInstance().accDataChk)? String.valueOf(Parameters.getInstance().sensorData[1]):Const.DISABLE_UPLOAD);
        data.put("acceleration_z",(ParameterOptions.getInstance().accDataChk)? String.valueOf(Parameters.getInstance().sensorData[2]):Const.DISABLE_UPLOAD);
        data.put("temperature",(ParameterOptions.getInstance().enviChk)? String.valueOf(Parameters.getInstance().temperature):Const.DISABLE_UPLOAD);
        data.put("pressure",(ParameterOptions.getInstance().enviChk)? String.valueOf(Parameters.getInstance().pressure):Const.DISABLE_UPLOAD);
        data.put("illuminance",(ParameterOptions.getInstance().enviChk)? String.valueOf(Parameters.getInstance().illuminance):Const.DISABLE_UPLOAD);
        data.put("imei",(ParameterOptions.getInstance().imeiChk)? String.valueOf(Parameters.getInstance().imei):Const.DISABLE_UPLOAD);
        data.put("device_id",(ParameterOptions.getInstance().androidIDChk)? String.valueOf(Parameters.getInstance().secureID):Const.DISABLE_UPLOAD);
        data.put("mac_address",(ParameterOptions.getInstance().macChk)? String.valueOf(Parameters.getInstance().macAddress):Const.DISABLE_UPLOAD);
        data.put("networking_state",(ParameterOptions.getInstance().netStatusCheck)? String.valueOf(Parameters.getInstance().internetConnectionState):Const.DISABLE_UPLOAD);
        data.put("timestamp", getCurrentTimestamp());
    }

    public void update(){
       // Log.e(TAG, "sent updated data");
        updateData();
        updateMapData();
        updateBeaconData();
    }

    public NearbyBeaconManager getNearbyBeaconManager(){
        return myNearbyBeaconManager;
    }

    private String getCurrentTimestamp(){
        String date = (DateFormat.format("yyyy-MM-dd'T'HH:mm:ssZ", new java.util.Date()).toString());
        return date;
    }

    private String getCurrentTimestamp(long gpsTime){
        String date = (DateFormat.format("yyyy-MM-dd'T'HH:mm:ssZ", gpsTime)).toString();
        return date;
    }

}
