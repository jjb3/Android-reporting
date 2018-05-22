package edu.gatech.reporter.utils;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.gatech.reporter.R;
import edu.gatech.reporter.utils.ParameterManager.Parameters;

/**
 * Created by Wendi on 2016/9/4.
 */
public class ViewUpdater {
    private static TextView powerLevelText;
    private static TextView gpsLocationText;
    private static TextView motionSensorText;
    private static TextView beaconText;
    private static TextView illuminanceText;
    private static TextView pressureText;
    private static TextView temperatureText;
    private static TextView imeiText;
    private static TextView androidIDText;
    private static TextView networkStateText;
    private static TextView externalPowerText;
    private static Context myContext;

    public static void init(Activity context){
        myContext = context;
        powerLevelText = (TextView) context.findViewById(R.id.powerLevelText);
        gpsLocationText = (TextView) context.findViewById(R.id.gpsLocationText);
        motionSensorText = (TextView) context.findViewById(R.id.motionSensorText);
        illuminanceText = (TextView) context.findViewById(R.id.illuminanceText);
        pressureText = (TextView) context.findViewById(R.id.pressureText);
        temperatureText = (TextView) context.findViewById(R.id.temperatureText);
        imeiText = (TextView) context.findViewById(R.id.imeiText);
        androidIDText = (TextView) context.findViewById(R.id.androidIDText);
        networkStateText = (TextView) context.findViewById(R.id.networkStatusText);
        externalPowerText = (TextView) context.findViewById(R.id.externalPower);
        beaconText = (TextView)context.findViewById(R.id.beaconText);

    }

    public static void update(){
        if(myContext != null){
            powerLevelText.setText(getPowerStateString());
            gpsLocationText.setText(getGPSLocationString());
            motionSensorText.setText(getAccelerometerDataString());
            illuminanceText.setText(getilluminanceString());
            pressureText.setText(getPressureString());
            temperatureText.setText(getTemperatureString());
            networkStateText.setText(getConnectivityString());
            androidIDText.setText(getAndroidIDString());
            imeiText.setText(getIMEIString());
            externalPowerText.setText(getExternalPowerString());
            beaconText.setText(getNearestBeaconIDString());
        }

    }

    private static String getAccelerometerDataString(){
        return "X: "+ String.valueOf(Parameters.getInstance().sensorData[0])
                + " \nY: " + String.valueOf(Parameters.getInstance().sensorData[1])
                + " \nZ: " + String.valueOf(Parameters.getInstance().sensorData[2]);
    }

    private static String getGPSLocationString(){
        return "Latitude: " + String.valueOf(Parameters.getInstance().location[0])
                + "\nLongitude: "+ String.valueOf(Parameters.getInstance().location[1])
                +"\nHeading: "+ String.valueOf(Parameters.getInstance().heading)
                +"\nGPSSpeed: " + String.valueOf(Parameters.getInstance().gpsSpeed)
                +"\nGPSTime: " + DateFormat.format("dd-MM-yyyy hh:mm:ss", Parameters.getInstance().gpsTime).toString();
    }

    private static String getPowerStateString(){
        return "Power Level: "+ String.valueOf((int)(Parameters.getInstance().batteryPct*Const.PERCENTAGE_FACTOR))+"%";
    }

    private static String getExternalPowerString(){
            switch(Parameters.getInstance().externalPower){
                case Const.NOT_CHARGE:
                    return "External Power: Not connected";
                case Const.AC_CHARGE:
                    return "External Power: AC";
                case Const.USB_CHARGE:
                    return "External Power: USB";
            }
            return "Error";
        }

    private static String getMacAddressString(){
        return "MAC Address:\n" + Parameters.getInstance().macAddress;
    }

    private static String getIMEIString(){
        return "IMEI: " + Parameters.getInstance().imei;
    }

    private static String getAndroidIDString(){
        return "Device ID:\n" + Parameters.getInstance().secureID;
    }

    private static String getConnectivityString(){
        switch(Parameters.getInstance().internetConnectionState){
            case Const.WIFI_CONNECTION:
                return "Networking Status:\nCurrently connected to WIFI";
            case Const.MOBILE_CONNECTION:
                return "Networking Status:\nCurrently connected to cellular data";
            case Const.NO_INTERNET_CONNECTION:
                return "Networking Status:\nNo internet connection\nData will not be send to the server.";
        }
        return "Error";
    }

    private static String getTemperatureString(){
        if(Parameters.getInstance().temperature == Const.NO_TEMPERATURE_SENSOR)
            return "Temperature:\nNo temperature sensor";
        return "Temperature: " + Parameters.getInstance().temperature;
    }

    private static String getilluminanceString(){
        if(Parameters.getInstance().illuminance == Const.NO_LIGHT_SENSOR)
            return "Illuminance:\nNo light sensor";
        return "Illuminance:\n" + Parameters.getInstance().illuminance;
    }

    private static String getPressureString(){
        if(Parameters.getInstance().pressure == Const.NO_PRESSURE_SENSOR)
            return "Pressure:\nNo pressure sensor";
        return "Pressure:\n" + Parameters.getInstance().pressure;
    }

    private static String getNearestBeaconIDString(){
        return "Nearest beacon ID:\n" + Parameters.getInstance().nearestBeaconID;
    }

}
