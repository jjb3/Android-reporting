package edu.gatech.reporter.beacons;

import android.content.Context;
import android.text.format.DateFormat;

import com.estimote.proximity_sdk.proximity.ProximityAttachment;

import java.util.HashMap;
import java.util.List;

import edu.gatech.reporter.ServiceRequests.BeaconServiceRequests;
import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;

public class NearbyBeaconManager {

    // Classes to make service request.
    BeaconServiceRequests myBeaconServiceRequest;
    Context mContext;

    private HashMap<String, List<ProximityAttachment>> nearbyBeacons;
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

    public HashMap<String, List<ProximityAttachment>> getNearbyBeacons() {
        return nearbyBeacons;
    }


    public void updateNearbyBeaconList(List<? extends  ProximityAttachment> attachments) {
        // remove to start fresh the zone.
        String zoneToUpdate = attachments.get(0).getPayload().get(Const.BEACON_INSTITUTION_KEY);
        nearbyBeacons.put(zoneToUpdate, (List<ProximityAttachment>) attachments);
    }

    public void sendData() {

        // harcoded for testing purposes currently.
//        myBeaconServiceRequest.sendPostRequest("1001","8.974842", "-79.506358","3.0", "220", "1", getCurrentTimestamp());
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
