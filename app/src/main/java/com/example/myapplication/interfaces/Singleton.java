package com.example.myapplication.interfaces;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Singleton {

    private RequestQueue requestQueue;
    private static Singleton sInstance;

    private Singleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized Singleton getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Singleton(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
