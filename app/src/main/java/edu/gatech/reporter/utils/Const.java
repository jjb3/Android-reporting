package edu.gatech.reporter.utils;

import android.graphics.Color;

import static android.R.attr.factor;
//import static com.google.android.gms.common.api.Status.st;

/**
 * Created by Wendi on 2016/8/31.
 */
public class Const {
    public static final int PERCENTAGE_FACTOR = 100;
    public static final double THRESHOLD = 0.001;
    public static final int NO_INTERNET_CONNECTION = 0;
    public static final int WIFI_CONNECTION = 1;
    public static final int MOBILE_CONNECTION = 2;
    public static final int NOT_CHARGE = 0;
    public static final int USB_CHARGE = 1;
    public static final int AC_CHARGE = 2;
    public static final double NO_LIGHT_SENSOR = -1;
    public static final double NO_PRESSURE_SENSOR = -1;
    public static final double NO_TEMPERATURE_SENSOR = -1;
    public static final String NO_BEACON_DETECTED = "";
    public static final String NO_ID = "";
    public static final String DISABLE_UPLOAD = "";
    public static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 0;
    public static final int MY_PERMISSIONS_ACCESS_NETWORK_STATE = 1;
    public static final int MY_PERMISSIONS_ACCESS_READ_PHONE_STATE = 2;

    public static final int RED_BUTTON_COLOR = Color.parseColor("#cc3300");
    public static final int GREEN_BUTTON_COLOR = Color.parseColor("#00cc66");
    public static final int BLUE_BUTTON_COLOR = Color.parseColor("#42a5f5");
    public static final int GRAY_TEXT_COLOR = Color.parseColor("#798ca7");
    public static final int DARK_BLUE = Color.parseColor("#0033cc");

    //   Database for Beacons Constants
    public static final String BEACON_DB_NAME = "beaconzone_db";
    public static final String KEY_INSTITUTION = "Institution";

    //    <!--Beacon Constants-->
    public static final String BEACON_INSTITUTION_KEY = "Institution";
    public static final String BEACON_BUS_STOP_KEY = "Bus Stop";
}
