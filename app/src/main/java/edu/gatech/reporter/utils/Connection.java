package edu.gatech.reporter.utils;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
            for (Map.Entry<String, String> entry : data.entrySet())
            {
                obj.put(entry.getKey(), entry.getValue());
            }
        }catch(JSONException e){
            System.out.println("JSON passing error!: " + e.toString());
        }

        Debug.print(obj.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,obj,
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
                });
        queue.add(jsObjRequest);
    }
}
