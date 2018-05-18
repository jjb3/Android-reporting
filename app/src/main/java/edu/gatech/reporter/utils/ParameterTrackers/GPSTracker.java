package edu.gatech.reporter.utils.ParameterTrackers;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;

/**
 * Code adopted from http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/. Modified on 2016/9/1.
 */
public class GPSTracker extends Service implements LocationListener {

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private int min_update_distance = 0; // 0 meters

    // The minimum time between updates in milliseconds
    private int min_update_interval = 1000 ; // 1s

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker() {
        min_update_interval = ParameterOptions.getInstance().dataUpdateInterval;
        min_update_distance = ParameterOptions.getInstance().minUpdateDistance;
        registerListener();
    }

    public boolean registerListener() {
        try {
            locationManager = (LocationManager) ReporterService.getContext()
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                return false;
            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled){
                    // Get location from Network Provider
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            min_update_interval,
                            min_update_distance, this);
                }

                // Get location from GPS provider
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            min_update_interval,
                            min_update_distance, this);
                }
            }
        } catch(SecurityException s){
            Log.d("Permission Denied", "Permission Denied");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    public Double[] getLocation(){
        Double[] location = new Double[2];
        if(isGPSValid()){
            location[0] = getLatitude();
            location[1] = getLongitude();
        }else{
            location[0] = null;
            location[1] = null;
        }
        return location;
    }

    /**
     * Function to check if best network provider
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public Double  getHeading(){
        if(isGPSValid() && getGPSSpeed()!= 0 && location.getBearing() != 0) {
            return (double)location.getBearing();
        }else
            return null;
    }

    public Double getGPSSpeed(){
        if(isGPSValid()) {
            return (double)location.getSpeed();
        }
        return null;
    }

    private boolean isGPSValid(){
        return location != null && (getLatitude() != 0 && getLongitude() != 0) && locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}