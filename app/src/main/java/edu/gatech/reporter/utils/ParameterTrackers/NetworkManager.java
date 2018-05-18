package edu.gatech.reporter.utils.ParameterTrackers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import edu.gatech.reporter.app.ReporterService;
import edu.gatech.reporter.utils.Const;

/**
 * Created by Wendi on 2016/9/26.
 */
public class NetworkManager {

    private ConnectivityManager myConnectionManager;
    private NetworkInfo activeNetwork;

    public NetworkManager() {
        myConnectionManager = (ConnectivityManager) ReporterService.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = myConnectionManager.getActiveNetworkInfo();
    }

    public int getNetworkState(){
      try{
              if (activeNetwork != null) { // connected to the internet
                  if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                     // Parameters.getInstance().internetConnectionState = Const.WIFI_CONNECTION;
                      return  Const.WIFI_CONNECTION;
                  } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                      return Const.MOBILE_CONNECTION;
                  }
              } else {
                  return Const.NO_INTERNET_CONNECTION;
              }
          return Const.NO_INTERNET_CONNECTION;
          } catch(SecurityException s){
           // Parameters.getInstance().internetConnectionState = Const.NO_INTERNET_CONNECTION;
            Log.d("Permission Denied", "Permission Denied");
            return Const.NO_INTERNET_CONNECTION;
      }
    }
}
