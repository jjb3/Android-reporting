package edu.gatech.reporter.beacons;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "beacon_zones")
public class BeaconZone {

    public BeaconZone(int id, String zoneName, boolean isSelected) {
        this.id = id;
        this.zoneName = zoneName;
        this.isSelected = isSelected;
    }

    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "zone_name")
    private String zoneName;

    @ColumnInfo(name = "is_checked")
    private boolean isSelected;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
