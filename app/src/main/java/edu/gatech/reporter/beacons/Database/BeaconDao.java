package edu.gatech.reporter.beacons.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BeaconDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addZone(BeaconZone beaconZone);

    @Delete
    public void removeZone(BeaconZone beaconZone);

    //"UPDATE beacon_zones SET is_checked= :isChecked WHERE id = :id"
    @Update()
    public void updateZone(BeaconZone beaconZone);

    @Query("select * from beacon_zones")
    public List<BeaconZone> getBeaconZones();

    @Query("DELETE FROM beacon_zones")
    public void nukeZoneTable();

}
