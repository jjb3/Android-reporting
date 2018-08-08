package edu.gatech.reporter.app;

import java.util.TimerTask;

import edu.gatech.reporter.beacons.NearbyBeaconManager;
import edu.gatech.reporter.utils.ParameterManager.DataManager;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;
import edu.gatech.reporter.utils.ParameterManager.Parameters;

/**
 * Created by Wendi on 2016/10/24.
 */

public class SendDataTask extends TimerTask {
    private DataManager myDataManager;
    public SendDataTask(DataManager dataManager){
        myDataManager = dataManager;
    }
    public void run() {
        if(ParameterOptions.getInstance().isAppRecording) {
            myDataManager.sendData();
        }
    }
}
