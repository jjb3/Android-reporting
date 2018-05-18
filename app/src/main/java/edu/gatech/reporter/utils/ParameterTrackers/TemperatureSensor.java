package edu.gatech.reporter.utils.ParameterTrackers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import edu.gatech.reporter.app.ReporterService;

import static edu.gatech.reporter.utils.Const.NO_TEMPERATURE_SENSOR;

/**
 * Created by Wendi on 2016/10/4.
 */

public class TemperatureSensor implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor myTemperatureSensor;
    double temperature;

    public TemperatureSensor(){
        mSensorManager = (SensorManager) ReporterService.getContext().getSystemService(Context.SENSOR_SERVICE);
        myTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mSensorManager.registerListener(this, myTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        temperature = NO_TEMPERATURE_SENSOR;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        temperature = event.values[0];
    }
    public double getTemperature(){
        return temperature;
    }

}