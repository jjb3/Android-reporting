package edu.gatech.reporter.beacons;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gatech.reporter.R;
import edu.gatech.reporter.app.ReporterHome;

public class SelectBeaconCatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.beacon_recycler_view) RecyclerView recyclerview;

    private BeaconAdapter beaconAdapter;
    private ArrayList<BeaconZone> institutionList;

    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_beacon_activity);
        ButterKnife.bind(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initRecyclerview();
        initToolbar();
    }

    private void initData(){

        // in the future this will load a list of instituition after a service call has been made
        institutionList = new ArrayList<>();
        institutionList.add(new BeaconZone(0,"Georgia Tech", false));
        institutionList.add(new BeaconZone(1,"University of Georgia", false));
        institutionList.add(new BeaconZone(2,"Georgia State University",false));

        // this in the future will handle screen rotation, wll have to be refined to also understand
        // when a new list is downloaded but keep the ones checked checked.

        List<BeaconZone> tempBeaconZone;

        for(int i = 0 ; i < institutionList.size() ; i++){
            ReporterHome.beaconDatabase.myBeaconZones().addZone(institutionList.get(i));
        }
        institutionList = new ArrayList<>();        //delete later this is just for testing purposes.
        tempBeaconZone = ReporterHome.beaconDatabase.myBeaconZones().getBeaconZones();
        for (BeaconZone beaconZone : tempBeaconZone) {
            institutionList.add(beaconZone);
        }

    }

    private void initRecyclerview(){
        beaconAdapter = new BeaconAdapter(institutionList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(beaconAdapter);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Beacons");
    }

    @Override
    public void onBackPressed() {
        for(int i = 0 ; i < institutionList.size() ; i++){
            BeaconZone tempBeaconZone = institutionList.get(i);
            ReporterHome.beaconDatabase.myBeaconZones().updateZone(tempBeaconZone);
        }
        super.onBackPressed();
        finish();
    }
}
