package edu.gatech.reporter.app;

import java.util.TimerTask;

import edu.gatech.reporter.beacons.NearbyBeaconManager;
import edu.gatech.reporter.utils.ParameterManager.DataManager;
import edu.gatech.reporter.utils.ParameterManager.Parameters;

/**
 * Created by Wendi on 2016/10/24.
 */

public class SendDataTask extends TimerTask {
    private DataManager myDataManager;
    private NearbyBeaconManager myNearbyBeaconManager;
    public SendDataTask(DataManager dataManager, NearbyBeaconManager nearbyBeaconManager){
        myDataManager = dataManager;
        myNearbyBeaconManager = nearbyBeaconManager;
    }
    public void run() {
        if(Parameters.getInstance().isRecording) {
            myDataManager.sendData();
            myNearbyBeaconManager.sendData();
        }
    }
}
