package edu.gatech.reporter.beacons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gatech.reporter.R;

public class SelectBeaconCatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.beacon_recycler_view) RecyclerView recyclerview;

    private BeaconAdapter beaconAdapter;
    private ArrayList<String> institutionList;
    private SparseBooleanArray chkStatus;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_beacon_activity);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initRecyclerview();
    }

    private void initData(){

        // in the future this will load a list of instituition after a service call has been made
        institutionList = new ArrayList<>();
        institutionList.add("Georgia Tech");
        institutionList.add("University of Georgia");
        institutionList.add("Georgia State University");

        // this in the future will handle screen rotation, wll have to be refined to also understand
        // when a new list is downloaded but keep the ones checked checked.
        chkStatus = new SparseBooleanArray();
        for(int i = 0 ; i < institutionList.size() ; i++){
            chkStatus.append(i, false);
        }

    }

    private void initRecyclerview(){
        beaconAdapter = new BeaconAdapter(institutionList, chkStatus);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(beaconAdapter);
    }

}
