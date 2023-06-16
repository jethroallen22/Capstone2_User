package com.example.myapplication.ui.preferences;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activities.Home;
import com.example.myapplication.activities.Preferences;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.databinding.FragmentPreferencesBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.ui.home.HomeFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferencesFragment extends Fragment {

    private static String JSON_URL;
    private IPModel ipModel;
    private RequestQueue requestQueuepf, requestQueuetags;
    private List<String> tags, chips, preferences;
    ChipGroup chipGroup;
    int userId;

    private FragmentPreferencesBinding binding;

    private String userName, weather;
    private Float wallet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreferencesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("name") != null) {
            userName = intent.getStringExtra("name");
            userId = intent.getIntExtra("id", 0);
            weather = intent.getStringExtra("weather");
            wallet = intent.getFloatExtra("wallet", 0);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("id");
            Log.d("pref_id", String.valueOf(userId));
        }

        requestQueuepf = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueuetags = Singleton.getsInstance(getActivity()).getRequestQueue();
        chips = new ArrayList<>();
        preferences = new ArrayList<>();
        tags = new ArrayList<>();

        // Initialize the ChipGroup object
        chipGroup = root.findViewById(R.id.cg_preferences2);

        JsonArrayRequest jsonArrayRequestpf = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apipreferences.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idUser = jsonObject.getInt("idUser");
                        String tag = jsonObject.getString("tag");
                        if(userId == idUser){
                            preferences.add(tag);
                            Log.d("TAG SIZE during pref", String.valueOf(preferences.size()));
                        }

                    } //list.add(productName);

                    catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                Log.d("TAG SIZE after pref kinda", String.valueOf(preferences.size()));
                JsonArrayRequest jsonArrayRequesttags = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apitags.php", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Chip chipAll = new Chip(getActivity());
                        chipAll.setText("Select All");
                        chipAll.setChipBackgroundColorResource(R.color.gray);

                        if(tags.size() == preferences.size())
                        {
                            chipAll.setSelected(true);
                            chipAll.setChipBackgroundColorResource(R.color.mosibusPrimary);
                            chipAll.setChipStrokeColorResource(R.color.teal_700);
                            chipAll.setTextColor(getResources().getColor(R.color.white));
                        }

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
                                JSONObject jsonObject2 = response.getJSONObject(i);
                                String tagname = jsonObject2.getString("tagname");
                                tags.add(tagname);
                                Log.d("tags", tags.get(i));

                                Log.d("chip", "inside for");
                                Chip chip = new Chip(getActivity());
                                chip.setText(tags.get(i));
                                String value = chip.getText().toString();

                                boolean isPresent = preferences.contains(tags.get(i));
                                if(isPresent == true){
                                    chip.setSelected(true);
                                    chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chip.setChipStrokeColorResource(R.color.teal_700);
                                    chip.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE called present", String.valueOf(chips.size()));
                                }
                                else{
                                    chip.setSelected(false);
                                    chip.setTextColor(Color.BLACK);
                                    chip.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                }

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
                                            Log.d("TAG SIZE clicked chip", value);
                                            chip.setSelected(true);
                                            chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                            chip.setChipStrokeColorResource(R.color.teal_700);
                                            chip.setTextColor(getResources().getColor(R.color.white));
                                            chips.add(value);
                                            Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

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


                        Log.d("TAG SIZE after chip kinda", String.valueOf(tags.size()));
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                requestQueuetags.add(jsonArrayRequesttags);

            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueuepf.add(jsonArrayRequestpf);


        Button confirm = root.findViewById(R.id.btn_confirmpref);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                Log.d("TAG SIZE before confirm", String.valueOf(chips.size()));
                for (int i = 0; i < chips.size(); i++) {
                    int finalI = i;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "updatepreferences.php", new Response.Listener<String>() {
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


                            params.put("idUser", String.valueOf(userId));
                            params.put("tag", chips.get(finalI));
                            params.put("i", String.valueOf(finalI));

                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }


                Intent intent = new Intent(getActivity().getApplicationContext(), Home.class);
                Log.d("Login", "intent");
                intent.putExtra("name",userName);
                intent.putExtra("id",userId);
                intent.putExtra("wallet", wallet);
                intent.putExtra("weather", weather);
                Log.d("NAME LOGIN: " , userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return root;
    }
}