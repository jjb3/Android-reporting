package edu.gatech.reporter.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.estimote.proximity_sdk.proximity.ProximityAttachment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import edu.gatech.reporter.R;
import edu.gatech.reporter.ServiceRequests.BeaconServiceRequests;
import edu.gatech.reporter.beacons.BeaconDatabase;
import edu.gatech.reporter.beacons.BeaconDatabaseManager;
import edu.gatech.reporter.beacons.BeaconZone;
import edu.gatech.reporter.beacons.ProximityBeaconImplementation;
import edu.gatech.reporter.beacons.ProximityBeaconInterface;
import edu.gatech.reporter.beacons.SelectBeaconCatActivity;
import edu.gatech.reporter.utils.Const;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;
import edu.gatech.reporter.utils.ParameterManager.Parameters;
import edu.gatech.reporter.utils.ViewUpdater;

public class ReporterHome extends AppCompatActivity implements ProximityBeaconInterface{

    private static Button recordButton;
    public TextView beaconTextView;

    private static AppCompatActivity self;
    int waitForPermissionCount = 0;

    private ProximityBeaconImplementation beaconObserver;
    private HashMap<String, ProximityAttachment> beaconsInRange;
    BeaconServiceRequests beaconServiceRequests;
    public BeaconDatabase beaconDatabase;

    private ArrayList<BeaconZone> trackedBeacons;

    public static AppCompatActivity getActivity(){
        return self;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        beaconDatabase = BeaconDatabaseManager.getInstance(this.getApplicationContext()).getBeaconDatabase();
        beaconObserver = ProximityBeaconImplementation.getInstance(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        beaconTextView = (TextView) findViewById(R.id.beaconText);

        checkPermission();
        if(waitForPermissionCount == 0){
            startService();
        }

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

        ParameterOptions.getInstance().setActivity(this);
        ParameterOptions.getInstance().loadPreference();

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
        if(id == R.id.action_beacons){
            Intent intent = new Intent(this, SelectBeaconCatActivity.class);
            intent.putExtra("selectedbeacons",2);
            beaconObserver.stopBeaconObserver();
            beaconTextView.setText("No beacons nearby");
            beaconsInRange = new HashMap<>();
            this.startActivityForResult(intent,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            waitForPermissionCount++;
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            waitForPermissionCount++;
            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        }

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            waitForPermissionCount++;
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Const.MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
            case Const.MY_PERMISSIONS_ACCESS_NETWORK_STATE:
            case Const.MY_PERMISSIONS_ACCESS_READ_PHONE_STATE:
                waitForPermissionCount--;
        }
        if(waitForPermissionCount == 0)
            startService();
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

    private void updateBeaconsInRange(){

        String beaconsText = "";

        if (!beaconsInRange.isEmpty()) {
            for (Map.Entry<String, ProximityAttachment> entry : beaconsInRange.entrySet()) {
                String key = entry.getKey();
                ProximityAttachment attachment = entry.getValue();
                beaconsText = beaconsText + "Beacon Id: " + key;

                for (Map.Entry<String, String> attch : attachment.getPayload().entrySet()) {
                    String key1 = attch.getKey();
                    String key1Value = attch.getValue();

                    beaconsText = beaconsText + "\n\t" + key1 + ": " + key1Value;
                }
            }
            beaconsText = beaconsText + "\n";
        } else {
            beaconsText = "No beacons nearby";
        }
        beaconTextView.setText(beaconsText);
    }

    private void initBeaconProximityObserver(){
        updateBeaconZonesToTrack();
        beaconsInRange = new HashMap<>();
        beaconObserver.initProximityObserver();
        beaconObserver.addProximityZone(trackedBeacons);
        beaconObserver.startBeaconObserver();
    }

    private void updateBeaconZonesToTrack(){
        List<BeaconZone> tempBeaconZones = beaconDatabase.myBeaconZones().getBeaconZones();
        trackedBeacons = new ArrayList<>();
        for(BeaconZone beacon : tempBeaconZones){
            if(beacon.isSelected())
                trackedBeacons.add(beacon);
        }
    }

    @Override
    public void onEnterBeaconRegion(ProximityAttachment attachments) {
        // intentionally left in blank
    }

    @Override
    public void onExitBeaconRegion(ProximityAttachment attachments) {
        String tempDeviceId = attachments.getDeviceId();

        beaconsInRange.remove(tempDeviceId);
        updateBeaconsInRange();

        if(attachments.getPayload().get("Institution").equals("Georgia Tech"))
            Toast.makeText(ReporterHome.this, "Bye Bye, see you soon.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangeActionInRegion(ProximityAttachment attachments) {
        String tempDeviceId = attachments.getDeviceId();
        if(!beaconsInRange.containsKey(tempDeviceId)) {
            beaconsInRange.put(tempDeviceId, attachments);
            updateBeaconsInRange();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            //intentionally left in blank
            initBeaconProximityObserver();
        }
    }
}
