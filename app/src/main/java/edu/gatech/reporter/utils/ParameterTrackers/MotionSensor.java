package edu.gatech.reporter.utils.ParameterTrackers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;
import edu.gatech.reporter.utils.ParameterManager.Parameters;
import edu.gatech.reporter.utils.PastAccelerometerDataList;

/**
 * Created by Wendi on 2016/9/4.
 */
public class MotionSensor implements SensorEventListener {

    private final SensorManager mSensorManager;
    private final Sensor mSensor;
    private double[] sensorData;
    private PastAccelerometerDataList pastAccData;

    public MotionSensor() {
        mSensorManager = (SensorManager) ReporterService.getContext().getSystemService(ReporterService.getContext().SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorData = new double [3];
        pastAccData = new PastAccelerometerDataList();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

            sensorData[0] = (double)event.values[0];
            sensorData[1] = (double)event.values[1];
            sensorData[2] = (double)event.values[2];

    }
    public double[] getMotionSensorData(){
        pastAccData.add(sensorData.clone());
        return sensorData.clone();
    }
    public double getJostleIndex(){
        return pastAccData.getJostleIndex();
    }
    public double getDistanceSummation(){ return pastAccData.getDistanceSumFromOrigin();}

    private boolean testThreshold(double firstD, double secondD){
        if(Math.abs(firstD - secondD) > Const.THRESHOLD){
            return true;
        }
        return false;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
