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

public class PressureSensor implements SensorEventListener {
        private SensorManager mSensorManager;
        private Sensor myPressureSensor;
        double pressure;

        public PressureSensor(){
            mSensorManager = (SensorManager) ReporterService.getContext().getSystemService(Context.SENSOR_SERVICE);
            myPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            if(myPressureSensor == null){
                System.out.println("No pressure sensor");
            }
            mSensorManager.registerListener(this, myPressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            pressure = Const.NO_PRESSURE_SENSOR;
        }

        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do something here if sensor accuracy changes.
        }

        @Override
        public final void onSensorChanged(SensorEvent event) {
            pressure = event.values[0];
            // Do something with this sensor data.
        }
        public double getPressure(){
            return pressure;
        }

    }
