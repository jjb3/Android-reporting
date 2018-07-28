package edu.gatech.reporter.beacons;

import android.app.Service;
import android.content.Context;
import android.text.format.DateFormat;


import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.gatech.reporter.ServiceRequests.BeaconServiceRequests;

public class NearbyBeaconManager {

    // Classes to make service request.
    BeaconServiceRequests myBeaconServiceRequest;
    Context mContext;

    private HashMap<String, List<ProximityZoneContext>> nearbyBeacons;
    private HashMap<String, String> nearbyBeaconZones;


    public NearbyBeaconManager(Context applicationContext) {
        this.mContext = applicationContext;
        nearbyBeacons = new HashMap<>();
        nearbyBeaconZones = new HashMap<>();
        myBeaconServiceRequest = BeaconServiceRequests.getInstance(mContext);
    }


    public HashMap<String, String> getNearbyBeaconZones() {
        return nearbyBeaconZones;
    }

    public HashMap<String, List<ProximityZoneContext>> getNearbyBeacons() {
        return nearbyBeacons;
    }


    public void updateNearbyBeaconList(Set<? extends  ProximityZoneContext> attachments) {
        // remove to start fresh the zone.
        ProximityZoneContext[] zonesToUpdateArray = attachments.toArray(new ProximityZoneContext[attachments.size()]);
        ProximityZoneContext zoneContext = zonesToUpdateArray[0];
        String zoneToUpdate = zoneContext.getTag();
        nearbyBeacons.put(zoneToUpdate, Arrays.asList(zonesToUpdateArray));
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
