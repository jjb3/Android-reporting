package edu.gatech.reporter.utils.ParameterTrackers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;

/**
 * Created by Wendi on 2016/10/4.
 */

public class LightSensor implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor myLightSensor;
    double illuminance;

    public LightSensor(){
        mSensorManager = (SensorManager) ReporterService.getContext().getSystemService(Context.SENSOR_SERVICE);
        myLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, myLightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        illuminance = Const.NO_LIGHT_SENSOR;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        illuminance = event.values[0];
    }

    public double getIlluminance(){
        return illuminance;
    }

}