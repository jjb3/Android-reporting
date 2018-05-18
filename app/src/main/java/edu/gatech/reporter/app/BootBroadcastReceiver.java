package edu.gatech.reporter.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jeffreywongo on 2/10/2018.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    /*
    This should start the ReporterService as the device boots
    Don't know if the information will be sent to server if record button isn't pressed though
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, ReporterService.class);
        context.startService(startServiceIntent);
    }
}
