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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferencesFragment extends Fragment {

    private static String JSON_URL;
    private IPModel ipModel;
    private RequestQueue requestQueuepf, requestQueuetags;
    private List<String> tags, chips, preferences;
    ChipGroup chipGroup, cg_temperature, cg_mealtime, cg_noodles, cg_beverages, cg_cuisine, cg_meat, cg_miscellaneous, cg_taste;
    int userId;

    private FragmentPreferencesBinding binding;

    private String userName, weather;
    private Float wallet;

    private List<String> chp_temp_list, chp_mealtime_list, chp_noodle_list, chp_beverages_list, chp_cuisine_list, chp_meat_list, chp_misc_list, chp_taste_list;

    Boolean isPresentTemp, isPresentMealtime, isPresentNoodles, isPresentBev, isPresentCuisine, isPresentMeat, isPresentMisc;


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

        //chipGroup = findViewById(R.id.cg_preferences);
        cg_temperature = root.findViewById(R.id.cg_temperature);
        cg_mealtime = root.findViewById(R.id.cg_mealtime);
        cg_noodles = root.findViewById(R.id.cg_noodles);
        cg_beverages = root.findViewById(R.id.cg_beverages);
        cg_cuisine = root.findViewById(R.id.cg_cuisine);
        cg_meat = root.findViewById(R.id.cg_meat);
        cg_miscellaneous = root.findViewById(R.id.cg_miscellaneous);
        cg_taste = root.findViewById(R.id.cg_taste);

        chp_temp_list = new ArrayList<>();
        chp_mealtime_list = new ArrayList<>();
        chp_noodle_list = new ArrayList<>();
        chp_beverages_list = new ArrayList<>();
        chp_cuisine_list = new ArrayList<>();
        chp_meat_list = new ArrayList<>();
        chp_misc_list = new ArrayList<>();

        chp_temp_list = Arrays.asList("Hot", "Cold");
        chp_mealtime_list = Arrays.asList("Breakfast", "Lunch", "Dessert");
        chp_noodle_list = Arrays.asList("Noodles", "Pasta", "Ramen");
        chp_beverages_list = Arrays.asList("Beverages", "Juice", "Soda", "Milktea");
        chp_cuisine_list = Arrays.asList("American", "Chinese", "Filipino", "Japanese", "Thai");
        chp_meat_list = Arrays.asList("Pork", "Chicken", "Beef", "Fish", "Seafood");
        chp_misc_list = Arrays.asList("Pizza", "Salad", "Fast Food", "Bread", "Quick bites", "Easy to eat", "Heavy meal", "Baked");
        chp_taste_list = Arrays.asList("Crispy", "Spicy", "Sweet");

        // Initialize the ChipGroup object

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

                // FOR CHIPGROUP TEMPERATURE
                for(int a = 0; a < chp_temp_list.size(); a++) {
                        Chip chiptemp = new Chip(getActivity());
                        chiptemp.setText(chp_temp_list.get(a));

                        if(preferences.contains(chp_temp_list.get(a))){
                            chiptemp.setSelected(true);
                            chiptemp.setChipBackgroundColorResource(R.color.mosibusPrimary);
                            chiptemp.setChipStrokeColorResource(R.color.teal_700);
                            chiptemp.setTextColor(getResources().getColor(R.color.white));
                            chips.add(chiptemp.getText().toString());

                            chiptemp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String value = chiptemp.getText().toString();
                                    if (chiptemp.isSelected()) {
                                        chiptemp.setSelected(false);
                                        chiptemp.setTextColor(Color.BLACK);
                                        chiptemp.setChipBackgroundColorResource(R.color.gray);
                                        chips.remove(value);
                                    } else {
                                        Log.d("TAG SIZE clicked chip", value);
                                        chiptemp.setSelected(true);
                                        chiptemp.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                        chiptemp.setChipStrokeColorResource(R.color.teal_700);
                                        chiptemp.setTextColor(getResources().getColor(R.color.white));
                                        chips.add(value);
                                        Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                    }
                                }
                            });

                            cg_temperature.addView(chiptemp);
                            cg_temperature.setVisibility(View.VISIBLE);
                            cg_temperature.getLayoutParams();

                        } else {
                            chiptemp.setSelected(false);
                            chiptemp.setTextColor(Color.BLACK);
                            chiptemp.setChipBackgroundColorResource(R.color.gray);
                            chips.remove(chiptemp.getText().toString());

                            chiptemp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String value = chiptemp.getText().toString();
                                    if (chiptemp.isSelected()) {
                                        chiptemp.setSelected(false);
                                        chiptemp.setTextColor(Color.BLACK);
                                        chiptemp.setChipBackgroundColorResource(R.color.gray);
                                        chips.remove(value);
                                    } else {
                                        Log.d("TAG SIZE clicked chip", value);
                                        chiptemp.setSelected(true);
                                        chiptemp.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                        chiptemp.setChipStrokeColorResource(R.color.teal_700);
                                        chiptemp.setTextColor(getResources().getColor(R.color.white));
                                        chips.add(value);
                                        Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                    }
                                }
                            });

                            cg_temperature.addView(chiptemp);
                            cg_temperature.setVisibility(View.VISIBLE);
                            cg_temperature.getLayoutParams();
                        }

                    }

                // FOR CHIPGROUP MEALTIME
                for(int b = 0; b < chp_mealtime_list.size(); b++) {
                    Chip chipmeal = new Chip(getActivity());
                    chipmeal.setText(chp_mealtime_list.get(b));

                    if(preferences.contains(chp_mealtime_list.get(b))){
                        chipmeal.setSelected(true);
                        chipmeal.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chipmeal.setChipStrokeColorResource(R.color.teal_700);
                        chipmeal.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chipmeal.getText().toString());

                        chipmeal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipmeal.getText().toString();
                                if (chipmeal.isSelected()) {
                                    chipmeal.setSelected(false);
                                    chipmeal.setTextColor(Color.BLACK);
                                    chipmeal.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipmeal.setSelected(true);
                                    chipmeal.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipmeal.setChipStrokeColorResource(R.color.teal_700);
                                    chipmeal.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_mealtime.addView(chipmeal);
                        cg_mealtime.setVisibility(View.VISIBLE);
                        cg_mealtime.getLayoutParams();

                    } else {
                        chipmeal.setSelected(false);
                        chipmeal.setTextColor(Color.BLACK);
                        chipmeal.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chipmeal.getText().toString());

                        chipmeal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipmeal.getText().toString();
                                if (chipmeal.isSelected()) {
                                    chipmeal.setSelected(false);
                                    chipmeal.setTextColor(Color.BLACK);
                                    chipmeal.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipmeal.setSelected(true);
                                    chipmeal.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipmeal.setChipStrokeColorResource(R.color.teal_700);
                                    chipmeal.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_mealtime.addView(chipmeal);
                        cg_mealtime.setVisibility(View.VISIBLE);
                        cg_mealtime.getLayoutParams();
                    }

                }

                // FOR CHIPGROUP NOODLES
                for(int c = 0; c < chp_noodle_list.size(); c++) {
                    Chip chipnood = new Chip(getActivity());
                    chipnood.setText(chp_noodle_list.get(c));

                    if(preferences.contains(chp_noodle_list.get(c))){
                        chipnood.setSelected(true);
                        chipnood.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chipnood.setChipStrokeColorResource(R.color.teal_700);
                        chipnood.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chipnood.getText().toString());

                        chipnood.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipnood.getText().toString();
                                if (chipnood.isSelected()) {
                                    chipnood.setSelected(false);
                                    chipnood.setTextColor(Color.BLACK);
                                    chipnood.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipnood.setSelected(true);
                                    chipnood.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipnood.setChipStrokeColorResource(R.color.teal_700);
                                    chipnood.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_noodles.addView(chipnood);
                        cg_noodles.setVisibility(View.VISIBLE);
                        cg_noodles.getLayoutParams();

                    } else {
                        chipnood.setSelected(false);
                        chipnood.setTextColor(Color.BLACK);
                        chipnood.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chipnood.getText().toString());

                        chipnood.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipnood.getText().toString();
                                if (chipnood.isSelected()) {
                                    chipnood.setSelected(false);
                                    chipnood.setTextColor(Color.BLACK);
                                    chipnood.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipnood.setSelected(true);
                                    chipnood.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipnood.setChipStrokeColorResource(R.color.teal_700);
                                    chipnood.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_noodles.addView(chipnood);
                        cg_noodles.setVisibility(View.VISIBLE);
                        cg_noodles.getLayoutParams();
                    }

                }

                // FOR CHIPGROUP BEVERAGES
                for(int d = 0; d < chp_beverages_list.size(); d++) {
                    Chip chipbev = new Chip(getActivity());
                    chipbev.setText(chp_beverages_list.get(d));

                    if(preferences.contains(chp_beverages_list.get(d))){
                        chipbev.setSelected(true);
                        chipbev.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chipbev.setChipStrokeColorResource(R.color.teal_700);
                        chipbev.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chipbev.getText().toString());

                        chipbev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipbev.getText().toString();
                                if (chipbev.isSelected()) {
                                    chipbev.setSelected(false);
                                    chipbev.setTextColor(Color.BLACK);
                                    chipbev.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipbev.setSelected(true);
                                    chipbev.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipbev.setChipStrokeColorResource(R.color.teal_700);
                                    chipbev.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_beverages.addView(chipbev);
                        cg_beverages.setVisibility(View.VISIBLE);
                        cg_beverages.getLayoutParams();

                    } else {
                        chipbev.setSelected(false);
                        chipbev.setTextColor(Color.BLACK);
                        chipbev.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chipbev.getText().toString());

                        chipbev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipbev.getText().toString();
                                if (chipbev.isSelected()) {
                                    chipbev.setSelected(false);
                                    chipbev.setTextColor(Color.BLACK);
                                    chipbev.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipbev.setSelected(true);
                                    chipbev.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipbev.setChipStrokeColorResource(R.color.teal_700);
                                    chipbev.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_beverages.addView(chipbev);
                        cg_beverages.setVisibility(View.VISIBLE);
                        cg_beverages.getLayoutParams();
                    }

                }

                // FOR CHIPGROUP CUISINE
                for(int e = 0; e < chp_cuisine_list.size(); e++) {
                    Chip chipc = new Chip(getActivity());
                    chipc.setText(chp_cuisine_list.get(e));

                    if(preferences.contains(chp_cuisine_list.get(e))){
                        chipc.setSelected(true);
                        chipc.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chipc.setChipStrokeColorResource(R.color.teal_700);
                        chipc.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chipc.getText().toString());

                        chipc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipc.getText().toString();
                                if (chipc.isSelected()) {
                                    chipc.setSelected(false);
                                    chipc.setTextColor(Color.BLACK);
                                    chipc.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipc.setSelected(true);
                                    chipc.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipc.setChipStrokeColorResource(R.color.teal_700);
                                    chipc.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_cuisine.addView(chipc);
                        cg_cuisine.setVisibility(View.VISIBLE);
                        cg_cuisine.getLayoutParams();

                    } else {
                        chipc.setSelected(false);
                        chipc.setTextColor(Color.BLACK);
                        chipc.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chipc.getText().toString());

                        chipc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipc.getText().toString();
                                if (chipc.isSelected()) {
                                    chipc.setSelected(false);
                                    chipc.setTextColor(Color.BLACK);
                                    chipc.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipc.setSelected(true);
                                    chipc.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipc.setChipStrokeColorResource(R.color.teal_700);
                                    chipc.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_cuisine.addView(chipc);
                        cg_cuisine.setVisibility(View.VISIBLE);
                        cg_cuisine.getLayoutParams();
                    }

                }

                // FOR CHIPGROUP TASTE
                for(int f = 0; f < chp_taste_list.size(); f++) {
                    Chip chiptaste = new Chip(getActivity());
                    chiptaste.setText(chp_taste_list.get(f));

                    if(preferences.contains(chp_taste_list.get(f))){
                        chiptaste.setSelected(true);
                        chiptaste.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chiptaste.setChipStrokeColorResource(R.color.teal_700);
                        chiptaste.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chiptaste.getText().toString());

                        chiptaste.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chiptaste.getText().toString();
                                if (chiptaste.isSelected()) {
                                    chiptaste.setSelected(false);
                                    chiptaste.setTextColor(Color.BLACK);
                                    chiptaste.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chiptaste.setSelected(true);
                                    chiptaste.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chiptaste.setChipStrokeColorResource(R.color.teal_700);
                                    chiptaste.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_taste.addView(chiptaste);
                        cg_taste.setVisibility(View.VISIBLE);
                        cg_taste.getLayoutParams();

                    } else {
                        chiptaste.setSelected(false);
                        chiptaste.setTextColor(Color.BLACK);
                        chiptaste.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chiptaste.getText().toString());

                        chiptaste.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chiptaste.getText().toString();
                                if (chiptaste.isSelected()) {
                                    chiptaste.setSelected(false);
                                    chiptaste.setTextColor(Color.BLACK);
                                    chiptaste.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chiptaste.setSelected(true);
                                    chiptaste.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chiptaste.setChipStrokeColorResource(R.color.teal_700);
                                    chiptaste.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_taste.addView(chiptaste);
                        cg_taste.setVisibility(View.VISIBLE);
                        cg_taste.getLayoutParams();
                    }

                }

                    // FOR CHIPGROUP MISCELLANEOUS
                for(int g = 0; g < chp_misc_list.size(); g++) {
                    Chip chipmisc = new Chip(getActivity());
                    chipmisc.setText(chp_misc_list.get(g));

                    if(preferences.contains(chp_misc_list.get(g))){
                        chipmisc.setSelected(true);
                        chipmisc.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chipmisc.setChipStrokeColorResource(R.color.teal_700);
                        chipmisc.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chipmisc.getText().toString());

                        chipmisc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipmisc.getText().toString();
                                if (chipmisc.isSelected()) {
                                    chipmisc.setSelected(false);
                                    chipmisc.setTextColor(Color.BLACK);
                                    chipmisc.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipmisc.setSelected(true);
                                    chipmisc.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipmisc.setChipStrokeColorResource(R.color.teal_700);
                                    chipmisc.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_miscellaneous.addView(chipmisc);
                        cg_miscellaneous.setVisibility(View.VISIBLE);
                        cg_miscellaneous.getLayoutParams();

                    } else {
                        chipmisc.setSelected(false);
                        chipmisc.setTextColor(Color.BLACK);
                        chipmisc.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chipmisc.getText().toString());

                        chipmisc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value = chipmisc.getText().toString();
                                if (chipmisc.isSelected()) {
                                    chipmisc.setSelected(false);
                                    chipmisc.setTextColor(Color.BLACK);
                                    chipmisc.setChipBackgroundColorResource(R.color.gray);
                                    chips.remove(value);
                                } else {
                                    Log.d("TAG SIZE clicked chip", value);
                                    chipmisc.setSelected(true);
                                    chipmisc.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                    chipmisc.setChipStrokeColorResource(R.color.teal_700);
                                    chipmisc.setTextColor(getResources().getColor(R.color.white));
                                    chips.add(value);
                                    Log.d("TAG SIZE clicked chip", String.valueOf(chips.size()));

                                }
                            }
                        });

                        cg_miscellaneous.addView(chipmisc);
                        cg_miscellaneous.setVisibility(View.VISIBLE);
                        cg_miscellaneous.getLayoutParams();
                    }

                }

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