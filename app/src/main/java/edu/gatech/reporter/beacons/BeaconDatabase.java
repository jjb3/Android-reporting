package edu.gatech.reporter.beacons;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {BeaconZone.class}, version = 1)
public abstract class BeaconDatabase extends RoomDatabase{

    public abstract BeaconDao myBeaconZones();


}
