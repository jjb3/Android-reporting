package edu.gatech.reporter.beacons.Database;

import android.arch.persistence.room.Room;
import android.content.Context;

import edu.gatech.reporter.utils.Const;

public class BeaconDatabaseManager {

    private static BeaconDatabaseManager instance;
    private static BeaconDatabase beaconDatabase;


    private BeaconDatabaseManager(Context context) {
        //TODO change this to not allow main thread queries.
        beaconDatabase = Room.databaseBuilder(context.getApplicationContext(), BeaconDatabase.class, Const.BEACON_DB_NAME).build();
    }

    public static BeaconDatabaseManager getInstance(Context context){

        if (instance == null){
            instance = new BeaconDatabaseManager(context);
        }
        return instance;
    }

    public BeaconDatabase getBeaconDatabase(){
        return beaconDatabase;
    }

}
