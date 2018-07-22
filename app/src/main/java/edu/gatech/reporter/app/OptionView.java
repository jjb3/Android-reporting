package edu.gatech.reporter.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.gatech.reporter.R;
import edu.gatech.reporter.beacons.BeaconEvents.RestartReportTaskEvent;
import edu.gatech.reporter.beacons.BeaconEvents.ChangeTagsEvent;
import edu.gatech.reporter.beacons.ProximityBeaconImplementation;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;

import static edu.gatech.reporter.R.id.updateInterval;

public class OptionView extends AppCompatActivity {
    private static CheckBox powerLevelChk;
    private static CheckBox locationDataChk;
    private static CheckBox accDataChk;
    private static CheckBox beaconChk;
    private static CheckBox enviChk;
    private static CheckBox imeiChk;
    private static CheckBox androidIDChk;
    private static CheckBox macChk;
    private static CheckBox netStatusCheck;
    private Button changeDataUpdateIntervalButton;
    private Button changeReportIntervalButton;
    private Button changeServerURLButton;
    private Button changeBeaconTagsButton;
    private TextView updateIntervalView;
    private TextView reportIntervalView;
    private TextView serverURLView;
    private TextView beaconTagsView;


    private Executor executor = Executors.newSingleThreadExecutor();
    private ProximityBeaconImplementation beaconObserver;
    List<String> beaconTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_view);
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setUpChkListener();
        beaconObserver = ProximityBeaconImplementation.getInstance(this.getApplicationContext());
    }

    private void setUpChkListener(){
        powerLevelChk = (CheckBox)this.findViewById(R.id.powerLevelChk);
        locationDataChk = (CheckBox)this.findViewById(R.id.locationDataChk);
        accDataChk = (CheckBox)this.findViewById(R.id.accDataChk);
        beaconChk = (CheckBox)this.findViewById(R.id.beaconChk);
        enviChk = (CheckBox)this.findViewById(R.id.enviChk);
        imeiChk = (CheckBox)this.findViewById(R.id.imeiChk);
        androidIDChk = (CheckBox)this.findViewById(R.id.androidIDChk);
        macChk = (CheckBox)this.findViewById(R.id.macChk);
        netStatusCheck = (CheckBox)this.findViewById(R.id.netStatusCheck);
        updateIntervalView = (TextView)this.findViewById(R.id.updateIntervalView);
        reportIntervalView = (TextView)this.findViewById(R.id.reportIntervalView);
        serverURLView = (TextView)this.findViewById(R.id.serverURLView);
        beaconTagsView = (TextView)this.findViewById(R.id.beacon_tags_view);


        powerLevelChk.setChecked(ParameterOptions.getInstance().powerLevelChk);
        locationDataChk.setChecked(ParameterOptions.getInstance().locationDataChk);
        accDataChk.setChecked(ParameterOptions.getInstance().accDataChk);
        beaconChk.setChecked(ParameterOptions.getInstance().beaconChk);
        enviChk.setChecked(ParameterOptions.getInstance().enviChk);
        imeiChk.setChecked(ParameterOptions.getInstance().imeiChk);
        androidIDChk.setChecked(ParameterOptions.getInstance().androidIDChk);
        macChk.setChecked(ParameterOptions.getInstance().macChk);
        netStatusCheck.setChecked(ParameterOptions.getInstance().netStatusCheck);
        updateIntervalView.setText("Data update interval: "+String.valueOf(ParameterOptions.getInstance().dataUpdateInterval) + " ms");
        reportIntervalView.setText(String.valueOf("Report interval: "+ParameterOptions.getInstance().reportInterval) + "ms");
        serverURLView.setText(String.valueOf("Server URL: \n"+ParameterOptions.getInstance().serverURL));
        beaconTagsView.setText("Beacon Tags: \n"+ParameterOptions.getInstance().beaconTags);


        powerLevelChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().powerLevelChk = isChecked;
                writePreferenceToFile();
            }
        });
        locationDataChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().locationDataChk = isChecked;
                writePreferenceToFile();
            }
        });
        accDataChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().accDataChk = isChecked;
                writePreferenceToFile();
            }
        });
        beaconChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().beaconChk = isChecked;
                writePreferenceToFile();
            }
        });
        enviChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().enviChk = isChecked;
            }
        });
        imeiChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().imeiChk = isChecked;
                writePreferenceToFile();
            }
        });
        androidIDChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().androidIDChk = isChecked;
                writePreferenceToFile();
            }
        });

        macChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().macChk = isChecked;
                writePreferenceToFile();
            }
        });

        netStatusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParameterOptions.getInstance().netStatusCheck = isChecked;
                writePreferenceToFile();
            }
        });

        changeDataUpdateIntervalButton = (Button) findViewById(updateInterval);
        changeDataUpdateIntervalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText input = new EditText(OptionView.this);
                input.setText(String.valueOf(ParameterOptions.getInstance().dataUpdateInterval));
                new AlertDialog.Builder(OptionView.this)
                        .setTitle("Change data update interval")
                        .setView(input)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int updateInterval;
                                try {
                                    updateInterval = Integer.parseInt(input.getText().toString());
                                    ParameterOptions.getInstance().dataUpdateInterval = updateInterval;
                                    writePreferenceToFile();
                                    EventBus.getDefault().post(new RestartReportTaskEvent());
                                    updateIntervalView.setText("Data update interval: "+String.valueOf(ParameterOptions.getInstance().dataUpdateInterval) + " ms");
                                } catch (NumberFormatException nf) {
                                    new AlertDialog.Builder(OptionView.this)
                                            .setTitle("Error")
                                            .setMessage("Bad input passed in")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                                //Log.d(LOG, "sample rate :" + sample_rate);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        changeReportIntervalButton = (Button) findViewById(R.id.reportInterval);
        changeReportIntervalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText input = new EditText(OptionView.this);
                input.setText(String.valueOf(ParameterOptions.getInstance().reportInterval));
                new AlertDialog.Builder(OptionView.this)
                        .setTitle("Change report interval")
                        .setView(input)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int reportInterval;
                                try {
                                    reportInterval = Integer.parseInt(input.getText().toString());
                                    ParameterOptions.getInstance().reportInterval = reportInterval;
                                    writePreferenceToFile();
                                    reportIntervalView.setText("Report interval: "+String.valueOf(ParameterOptions.getInstance().reportInterval) + " ms");
                                    EventBus.getDefault().post(new RestartReportTaskEvent());
                                } catch (NumberFormatException nf) {
                                    new AlertDialog.Builder(OptionView.this)
                                            .setTitle("Error")
                                            .setMessage("Bad input passed in")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                                //Log.d(LOG, "sample rate :" + sample_rate);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        changeServerURLButton = (Button) findViewById(R.id.serverURL);
        changeServerURLButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText input = new EditText(OptionView.this);
                input.setText(ParameterOptions.getInstance().serverURL);
                new AlertDialog.Builder(OptionView.this)
                        .setTitle("Change server url")
                        .setView(input)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ParameterOptions.getInstance().serverURL = input.getText().toString();
                                writePreferenceToFile();
                                EventBus.getDefault().post(new RestartReportTaskEvent());
                                serverURLView.setText("Server URL: \n"+String.valueOf(ParameterOptions.getInstance().serverURL));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        changeBeaconTagsButton = (Button) findViewById(R.id.beacon_tags);
        changeBeaconTagsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText input = new EditText(OptionView.this);
                input.setText(ParameterOptions.getInstance().beaconTags);
                EventBus.getDefault().post(new ChangeTagsEvent(null));
                new AlertDialog.Builder(OptionView.this)
                        .setTitle("Set tags to recognize")
                        .setView(input)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String tags = input.getText().toString();
                                ParameterOptions.getInstance().beaconTags = tags;
                                beaconTagsView.setText("Beacon Tags: \n"+ tags);
                                writePreferenceToFile();
                                EventBus.getDefault().post(new RestartReportTaskEvent());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

    }

    private void parseBeaconTags() {
        String allTags = String.valueOf(ParameterOptions.getInstance().beaconTags);
        beaconTags = Arrays.asList(allTags.split(","));
    }
    private void startNewBeaconZoneScan(){

        beaconObserver.addProximityZone(beaconTags);
        beaconObserver.startBeaconObserver();
    }


    private void writePreferenceToFile(){
        ParameterOptions.getInstance().writePreference();
    }
}
