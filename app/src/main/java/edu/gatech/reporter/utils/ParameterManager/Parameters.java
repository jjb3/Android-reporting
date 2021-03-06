package edu.gatech.reporter.utils.ParameterManager;

import edu.gatech.reporter.utils.Const;

/**
 * Created on 2016/9/4.
 */
public class Parameters {
    public  double  batteryPct;
    public double[] sensorData = new double[4];
    public  Double[] location = new Double[2];
    public Double temperature = Const.NO_TEMPERATURE_SENSOR;
    public Double illuminance = Const.NO_TEMPERATURE_SENSOR;
    public Double pressure = Const.NO_PRESSURE_SENSOR;
    public int internetConnectionState = Const.NO_INTERNET_CONNECTION;
    public int externalPower = Const.NOT_CHARGE;
    public Double gpsSpeed;
    public float gpsAccuracy;
    public long gpsTime;
    public Double heading;
    public String macAddress = "";
    public String imei = "";
    public String secureID = "";
    public String nearestBeaconID = "";
    public boolean isRecording = false;
    public String version = "1.0.2";
    public String lastUpdatedDate = "07/23/2018";


        private static Parameters instance = null;

        protected Parameters(){

        }
        public static Parameters getInstance(){
            if(instance == null) {
                instance = new Parameters();
            }
            return instance;
        }

}
