package edu.gatech.reporter.ServiceRequests;

public class BeaconPostPayloadTO {

    private String imei;
    private String lat;
    private String lng;
    private String speed;
    private String heading;
    private String accuracy;
    private String timestamp;

    public BeaconPostPayloadTO(String imei, String lat, String lng, String speed, String heading, String accuracy, String timestamp) {
        this.imei = imei;
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
        this.heading = heading;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
    }
}
