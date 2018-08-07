package edu.gatech.reporter.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gatech.reporter.R;
import edu.gatech.reporter.beacons.BeaconEvents.ChangeTagsEvent;
import edu.gatech.reporter.beacons.BeaconEvents.UpdateBeaconZonesEvent;
import edu.gatech.reporter.utils.Const;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;
import edu.gatech.reporter.utils.ParameterManager.Parameters;
import edu.gatech.reporter.utils.ViewUpdater;

public class ReporterHome extends AppCompatActivity {

    private static Button recordButton;
    @BindView(R.id.beacon_recyclerview) RecyclerView beaconRecyclerview;
    @BindView(R.id.beacon_count) TextView beaconCount;

    private static AppCompatActivity self;
    private BeaconHomeAdapter beaconHomeAdapter;

    public static AppCompatActivity getActivity(){
        return self;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
      
        ParameterOptions.getInstance().setContext(getApplicationContext());
        ParameterOptions.getInstance().loadPreference();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        checkPermission();

        recordButton = (Button)findViewById(R.id.recordBtn);
        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Parameters.getInstance().isRecording){

                    Parameters.getInstance().isRecording = false;
                    recordButton.setBackgroundColor(Const.GREEN_BUTTON_COLOR);
                    recordButton.setText("Press to start recording");

                }else{
                    new AlertDialog.Builder(self)
                            .setTitle("Start Recording")
                            .setMessage("Do you really want to start recording?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Parameters.getInstance().isRecording = true;
                                    recordButton.setBackgroundColor(Const.RED_BUTTON_COLOR);
                                    recordButton.setText("Stop recording");

                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

        //Fix orientation change problem
        if(Parameters.getInstance().isRecording == true){
            Parameters.getInstance().isRecording = true;
            recordButton.setBackgroundColor(Const.RED_BUTTON_COLOR);
            recordButton.setText("Stop recording");
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewUpdater.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_msin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, OptionView.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_about){
            Intent intent = new Intent(this, AboutView.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPermission(){
        MultiplePermissionsListener multiplePermissionsListenerDialogBuilder =
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                .withContext(this)
                .withTitle(getString(R.string.permissions_rationale_title))
                .withMessage(getString(R.string.permissions_rationale))
                .withButtonText(android.R.string.ok)
                .build();

        MultiplePermissionsListener multiplePermissionsListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted())
                    startService();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };

        MultiplePermissionsListener compositePermissionListener = new CompositeMultiplePermissionsListener(multiplePermissionsListener, multiplePermissionsListenerDialogBuilder);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_PHONE_STATE
                ).withListener(compositePermissionListener).check();
    }

    private void startService(){
        startService(new Intent(this, ReporterService.class));
        Thread updateView = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(ParameterOptions.getInstance().dataUpdateInterval);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ViewUpdater.update();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        updateView.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectBeaconTagsClickedEvent(ChangeTagsEvent changeTagsEvent) {
        beaconRecyclerview.setAdapter(null);
        beaconCount.setText("(0)");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleUpdateRecyclerviewEvent(UpdateBeaconZonesEvent beaconZonesEvent){
        // put recyclerview that will update the view of the beacons
        if(beaconZonesEvent.getNearbyBeaconsMap() != null)
            initRecyclerview(beaconZonesEvent.getNearbyBeaconsMap());

    }

    public void initRecyclerview(HashMap<String, List<ProximityZoneContext>> nearbyBeacons){

        List<ProximityZoneContext> listOfAllBeaconZones = new ArrayList<>();

        for (Map.Entry<String, List<ProximityZoneContext>> entry : nearbyBeacons.entrySet()) {
            for (ProximityZoneContext attachment : entry.getValue()){
                listOfAllBeaconZones.add(attachment);
            }
        }
        beaconHomeAdapter = new BeaconHomeAdapter(listOfAllBeaconZones);
        beaconRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        beaconRecyclerview.setAdapter(beaconHomeAdapter);
        beaconCount.setText("(" + String.valueOf(beaconHomeAdapter.getItemCount()) + ")");
        beaconHomeAdapter.notifyDataSetChanged();
    }

}
