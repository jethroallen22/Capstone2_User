package com.example.myapplication.activities;

import static android.app.PendingIntent.getActivity;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.activities.models.UserModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preferences extends AppCompatActivity {

    public static String name = "";
    public static int id;
    String image, weather;
    float wallet;
    private RequestQueue requestQueue1, requestQueuepref, requestQueuetags;
    List<UserModel> userList;

    ChipGroup chipGroup;

    private static String JSON_URL;
    private IPModel ipModel;

    private ImageView imageView3;
    private TextView textViewPref;
    private Button done_btn;

    private View v;

    private List<String> chips, tags;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Preferences", "Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        getSupportActionBar().hide();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getIntent();
        if (intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name");
            id = intent.getIntExtra("id", 0);
            image = intent.getStringExtra("image");
            weather = intent.getStringExtra("weather");
            wallet = intent.getFloatExtra("wallet", 0.0F);
            Log.d("Preferences name", name + id + image);
        } else {
            Log.d("Preferences name", "FAIL");
        }
        userList = new ArrayList();
        requestQueue1 = Singleton.getsInstance(this).getRequestQueue();

        chipGroup = findViewById(R.id.cg_preferences);

        imageView3 = findViewById(R.id.imageView3);
        textViewPref = findViewById(R.id.textViewPref);
        done_btn = findViewById(R.id.done_btn);
        chips = new ArrayList<>();
        requestQueuepref = Singleton.getsInstance(this).getRequestQueue();
        tags = new ArrayList<>();

        requestQueuetags = Singleton.getsInstance(this).getRequestQueue();

        JsonArrayRequest jsonArrayRequestpref = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apitags.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Chip chipAll = new Chip(context);
                chipAll.setText("Select All");
                chipAll.setChipBackgroundColorResource(R.color.gray);

                chipAll.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String value = chipAll.getText().toString();
                        if (chipAll.isSelected()) {
                            chipAll.setSelected(false);
                            chipAll.setTextColor(Color.BLACK);
                            chipAll.setChipBackgroundColorResource(R.color.gray);

                            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                                Chip chip = (Chip) chipGroup.getChildAt(i);
                                chip.setSelected(false);
                                chip.setTextColor(Color.BLACK);
                                chip.setChipBackgroundColorResource(R.color.gray);
                                chips.remove(chip.getText().toString());
                            }

                        } else {
                            chipAll.setSelected(true);
                            chipAll.setChipBackgroundColorResource(R.color.mosibusPrimary);
                            chipAll.setChipStrokeColorResource(R.color.teal_700);
                            chipAll.setTextColor(getResources().getColor(R.color.white));

                            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                                Chip chip = (Chip) chipGroup.getChildAt(i);
                                chip.setSelected(true);
                                chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                chip.setChipStrokeColorResource(R.color.teal_700);
                                chip.setTextColor(getResources().getColor(R.color.white));
                                chips.add(chip.getText().toString());
                            }

                        }//else
                    } //onClick
                }); //chipAll.setOnClickListener
                chipGroup.addView(chipAll);
                chipGroup.setVisibility(View.VISIBLE);
                chipGroup.getLayoutParams();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idtag = jsonObject.getInt("idtag");
                        String tagname = jsonObject.getString("tagname");
                        Log.d("tag=", tagname);
                        tags.add(tagname);
                        Log.d("tags", tags.get(i));

                        Log.d("chip", "inside for");
                        Chip chip = new Chip(context);
                        chip.setText(tags.get(i));
                        chip.setChipBackgroundColorResource(R.color.gray);

                        chip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chip.getText().toString();
                                if (chip.isSelected()) {
                                    chip.setSelected(false);
                                    chip.setTextColor(Color.BLACK);
                                    chip.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    chip.setSelected(true);
                                    chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chip.setChipStrokeColorResource(R.color.teal_700);
                                    chip.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                }
                            }
                        });
                        chipGroup.addView(chip);
                        chipGroup.setVisibility(View.VISIBLE);
                        chipGroup.getLayoutParams();
                    } //list.add(productName);

                    catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }




            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueuepref.add(jsonArrayRequestpref);




            // Set the ViewGroup as the content view of the Preferences activity.
            //setContentView(chipGroup);

            done_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    for (int i = 0; i < chips.size(); i++) {
                        int finalI = i;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "preferences.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("respo", response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("onError", String.valueOf(error));
                            }
                        }) {
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();


                                params.put("idUser", String.valueOf(id));
                                params.put("tag", chips.get(finalI));

                                return params;
                            }
                        };

                        requestQueue.add(stringRequest);
                    }


                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    Log.d("Login", "intent");
                    intent.putExtra("name",name);
                    intent.putExtra("id",id);
                    intent.putExtra("image",image);
                    intent.putExtra("wallet", wallet);
                    intent.putExtra("weather", weather);
                    Log.d("NAME LOGIN: " , name);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

        }
    }
