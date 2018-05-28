package edu.gatech.reporter.ServiceRequests;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class BeaconServiceRequests {

    private static Context context;
    RequestQueue mRequestQueue;

    private static BeaconServiceRequests beaconServiceRequests = null;

    private BeaconServiceRequests(Context mContext){

        context = mContext;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized BeaconServiceRequests getInstance(Context context)
    {

        if(beaconServiceRequests == null)
            beaconServiceRequests = new BeaconServiceRequests(context);

        return beaconServiceRequests;
    }

    private RequestQueue getRequestQueue(){

        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void sendPostRequest(String imei, String lat, String lng, String speed, String heading, String accuracy,
                                String timestamp){

        JSONObject requestObject = new JSONObject();

        try {
            requestObject.put("imei", imei);
            requestObject.put("lat", lat);
            requestObject.put("lng", lng);
            requestObject.put("speed", speed);
            requestObject.put("heading", heading);
            requestObject.put("accuracy", accuracy);
            requestObject.put("timestamp", timestamp);
        } catch (Exception e){

        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, "https://www.busgenius.com/api/v1/geolocations", requestObject ,
            new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    String string = "hello";

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error

                }
            })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("auth_token","NYj1iSTYowyoy18Vv1cFpdJC");
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");
                    params.put("Accept-Encoding", "utf-8");
                    return params;
            }
        };
        mRequestQueue.add(jsonRequest);
    }
}
