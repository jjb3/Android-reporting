package edu.gatech.reporter.utils;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.reporter.R;
import edu.gatech.reporter.app.ReporterHome;
import edu.gatech.reporter.app.ReporterService;

/**
 * Created by Wendi on 2016/9/24.
 */
public class Connection {
    RequestQueue queue = Volley.newRequestQueue(ReporterService.getContext());
    public Connection(){
    }

    public void issueGetRequest(String url){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                System.out.println("This does not work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void issueJSONPostRequest(String url, Map<String, String> data) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("imei", "1001");  //TODO HARDCODED IMEI
            obj.put("lat", data.get("lat"));
            obj.put("lng", data.get("lng"));
            obj.put("speed", data.get("speed"));
            obj.put("heading", data.get("heading"));
            obj.put("accuracy", data.get("accuracy") );
            obj.put("timestamp", data.get("timestamp"));
        } catch (JSONException e){
            System.out.println("JSON passing error!: " + e.toString());
        }

//        beaconServiceRequests = BeaconServiceRequests.getInstance(this);
//        beaconServiceRequests.sendPostRequest("1001", "41.0389", "111.6808",
//                "10", "87", "2", "2018-05-20T01:47:26Z");

        Debug.print(obj.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response != null)
                    System.out.println(response);
                    else
                        System.out.println("Empty response");
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Detect error: Print string: " + error.toString());
//                        Toast.makeText(ReporterHome.getActivity(), "Error Sending data! "+ error.toString(),
//                                    Toast.LENGTH_SHORT).show();
                }
            })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("auth_token","Private");
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Accept-Encoding", "utf-8");
                return params;
            }
        };
        queue.add(jsObjRequest);
    }
}
