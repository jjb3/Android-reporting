package edu.gatech.reporter.app;

import java.util.TimerTask;

import edu.gatech.reporter.utils.ParameterManager.DataManager;

/**
 * Created by Wendi on 2016/9/10.
 */
class DataUpdateTask extends TimerTask {
    private DataManager myDataManager;
    public DataUpdateTask(DataManager dataManager){
        myDataManager = dataManager;
    }
    public void run() {
        myDataManager.update();
    }

    private void updateData(){

    }
}
