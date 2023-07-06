package com.example.myapplication.activities;

import static android.app.PendingIntent.getActivity;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.UserModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

    ChipGroup chipGroup, cg_temperature, cg_mealtime, cg_noodles, cg_beverages, cg_cuisine, cg_meat, cg_miscellaneous, cg_taste;

    private static String JSON_URL;
    private IPModel ipModel;

    private ImageView imageView3;
    private TextView textViewPref;
    private Button done_btn;

    private View v;

    private List<String> chips, tags, chp_temp_list, chp_mealtime_list, chp_noodle_list, chp_beverages_list, chp_cuisine_list, chp_meat_list, chp_misc_list, chp_taste_list;
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

        //chipGroup = findViewById(R.id.cg_preferences);
        cg_temperature = findViewById(R.id.cg_temperature);
        cg_mealtime = findViewById(R.id.cg_mealtime);
        cg_noodles = findViewById(R.id.cg_noodles);
        cg_beverages = findViewById(R.id.cg_beverages);
        cg_cuisine = findViewById(R.id.cg_cuisine);
        cg_meat = findViewById(R.id.cg_meat);
        cg_miscellaneous = findViewById(R.id.cg_miscellaneous);
        cg_taste = findViewById(R.id.cg_taste);

        imageView3 = findViewById(R.id.imageView3);
        textViewPref = findViewById(R.id.textViewPref);
        done_btn = findViewById(R.id.done_btn);
        chips = new ArrayList<>();
        requestQueuepref = Singleton.getsInstance(this).getRequestQueue();
        tags = new ArrayList<>();

        requestQueuetags = Singleton.getsInstance(this).getRequestQueue();

        chp_temp_list = new ArrayList<>();
        chp_mealtime_list = new ArrayList<>();
        chp_noodle_list = new ArrayList<>();
        chp_beverages_list = new ArrayList<>();
        chp_cuisine_list = new ArrayList<>();
        chp_meat_list = new ArrayList<>();
        chp_misc_list = new ArrayList<>();
        chp_taste_list = new ArrayList<>();

        chp_temp_list = Arrays.asList("Hot", "Cold");
        chp_mealtime_list = Arrays.asList("Breakfast", "Lunch", "Dessert");
        chp_noodle_list = Arrays.asList("Noodles", "Pasta", "Ramen");
        chp_beverages_list = Arrays.asList("Beverages", "Juice", "Soda", "Milktea");
        chp_cuisine_list = Arrays.asList("American", "Chinese", "Filipino", "Japanese", "Thai");
        chp_meat_list = Arrays.asList("Pork", "Chicken", "Beef", "Fish", "Seafood");
        chp_misc_list = Arrays.asList("Pizza", "Salad", "Fast Food", "Bread", "Quick bites", "Easy to eat", "Heavy meal", "Baked");
        chp_taste_list = Arrays.asList("Crispy", "Spicy", "Sweet");

        //ChipGroup Function
        //starts here

        //FOR TEMPERATURE
        Chip chipAll_temp = new Chip(context);
        chipAll_temp.setText("Select All");
        chipAll_temp.setChipBackgroundColorResource(R.color.gray);

        chipAll_temp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_temp.getText().toString();
                if (chipAll_temp.isSelected()) {
                    chipAll_temp.setSelected(false);
                    chipAll_temp.setTextColor(Color.BLACK);
                    chipAll_temp.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_temperature.getChildCount(); i++) {
                        Chip chip = (Chip) cg_temperature.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_temp.setSelected(true);
                    chipAll_temp.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_temp.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_temp.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_temperature.getChildCount(); i++) {
                        Chip chip = (Chip) cg_temperature.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_temperature.addView(chipAll_temp);
        cg_temperature.setVisibility(View.VISIBLE);
        cg_temperature.getLayoutParams();

        for (int i = 0; i < chp_temp_list.size(); i++) {
            try {
                String tagname = chp_temp_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_temp_list.get(i));
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
                cg_temperature.addView(chip);
                cg_temperature.setVisibility(View.VISIBLE);
                cg_temperature.getLayoutParams();
                } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //FOR MEALTIME
        Chip chipAll_mealtime = new Chip(context);
        chipAll_mealtime.setText("Select All");
        chipAll_mealtime.setChipBackgroundColorResource(R.color.gray);

        chipAll_mealtime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_mealtime.getText().toString();
                if (chipAll_mealtime.isSelected()) {
                    chipAll_mealtime.setSelected(false);
                    chipAll_mealtime.setTextColor(Color.BLACK);
                    chipAll_mealtime.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_mealtime.getChildCount(); i++) {
                        Chip chip = (Chip) cg_mealtime.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_mealtime.setSelected(true);
                    chipAll_mealtime.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_mealtime.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_mealtime.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_mealtime.getChildCount(); i++) {
                        Chip chip = (Chip) cg_mealtime.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_mealtime.addView(chipAll_mealtime);
        cg_mealtime.setVisibility(View.VISIBLE);
        cg_mealtime.getLayoutParams();

        for (int i = 0; i < chp_mealtime_list.size(); i++) {
            try {
                String tagname = chp_mealtime_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_mealtime_list.get(i));
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
                cg_mealtime.addView(chip);
                cg_mealtime.setVisibility(View.VISIBLE);
                cg_mealtime.getLayoutParams();
            } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //FOR NOODLES
        Chip chipAll_noodles = new Chip(context);
        chipAll_noodles.setText("Select All");
        chipAll_noodles.setChipBackgroundColorResource(R.color.gray);

        chipAll_noodles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_noodles.getText().toString();
                if (chipAll_noodles.isSelected()) {
                    chipAll_noodles.setSelected(false);
                    chipAll_noodles.setTextColor(Color.BLACK);
                    chipAll_noodles.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_noodles.getChildCount(); i++) {
                        Chip chip = (Chip) cg_noodles.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_noodles.setSelected(true);
                    chipAll_noodles.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_noodles.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_noodles.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_noodles.getChildCount(); i++) {
                        Chip chip = (Chip) cg_noodles.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_noodles.addView(chipAll_noodles);
        cg_noodles.setVisibility(View.VISIBLE);
        cg_noodles.getLayoutParams();

        for (int i = 0; i < chp_noodle_list.size(); i++) {
            try {
                String tagname = chp_noodle_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_noodle_list.get(i));
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
                cg_noodles.addView(chip);
                cg_noodles.setVisibility(View.VISIBLE);
                cg_noodles.getLayoutParams();
            } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //FOR BEVERAGES
        Chip chipAll_bev = new Chip(context);
        chipAll_bev.setText("Select All");
        chipAll_bev.setChipBackgroundColorResource(R.color.gray);

        chipAll_bev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_bev.getText().toString();
                if (chipAll_bev.isSelected()) {
                    chipAll_bev.setSelected(false);
                    chipAll_bev.setTextColor(Color.BLACK);
                    chipAll_bev.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_beverages.getChildCount(); i++) {
                        Chip chip = (Chip) cg_beverages.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_bev.setSelected(true);
                    chipAll_bev.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_bev.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_bev.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_beverages.getChildCount(); i++) {
                        Chip chip = (Chip) cg_beverages.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_beverages.addView(chipAll_bev);
        cg_beverages.setVisibility(View.VISIBLE);
        cg_beverages.getLayoutParams();

        for (int i = 0; i < chp_beverages_list.size(); i++) {
            try {
                String tagname = chp_beverages_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_beverages_list.get(i));
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
                cg_beverages.addView(chip);
                cg_beverages.setVisibility(View.VISIBLE);
                cg_beverages.getLayoutParams();
            } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //FOR CUISINE
        Chip chipAll_cuisine = new Chip(context);
        chipAll_cuisine.setText("Select All");
        chipAll_cuisine.setChipBackgroundColorResource(R.color.gray);

        chipAll_cuisine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_cuisine.getText().toString();
                if (chipAll_cuisine.isSelected()) {
                    chipAll_cuisine.setSelected(false);
                    chipAll_cuisine.setTextColor(Color.BLACK);
                    chipAll_cuisine.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_cuisine.getChildCount(); i++) {
                        Chip chip = (Chip) cg_cuisine.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_cuisine.setSelected(true);
                    chipAll_cuisine.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_cuisine.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_cuisine.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_cuisine.getChildCount(); i++) {
                        Chip chip = (Chip) cg_cuisine.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_cuisine.addView(chipAll_cuisine);
        cg_cuisine.setVisibility(View.VISIBLE);
        cg_cuisine.getLayoutParams();

        for (int i = 0; i < chp_cuisine_list.size(); i++) {
            try {
                String tagname = chp_cuisine_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_cuisine_list.get(i));
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
                cg_cuisine.addView(chip);
                cg_cuisine.setVisibility(View.VISIBLE);
                cg_cuisine.getLayoutParams();
            } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //FOR MEAT
        Chip chipAll_meat = new Chip(context);
        chipAll_meat.setText("Select All");
        chipAll_meat.setChipBackgroundColorResource(R.color.gray);

        chipAll_meat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_meat.getText().toString();
                if (chipAll_meat.isSelected()) {
                    chipAll_meat.setSelected(false);
                    chipAll_meat.setTextColor(Color.BLACK);
                    chipAll_meat.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_meat.getChildCount(); i++) {
                        Chip chip = (Chip) cg_meat.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_meat.setSelected(true);
                    chipAll_meat.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_meat.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_meat.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_meat.getChildCount(); i++) {
                        Chip chip = (Chip) cg_meat.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_meat.addView(chipAll_meat);
        cg_meat.setVisibility(View.VISIBLE);
        cg_meat.getLayoutParams();

        for (int i = 0; i < chp_meat_list.size(); i++) {
            try {
                String tagname = chp_meat_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_meat_list.get(i));
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
                cg_meat.addView(chip);
                cg_meat.setVisibility(View.VISIBLE);
                cg_meat.getLayoutParams();
            } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //FOR MISCELLANEOUS
        Chip chipAll_taste = new Chip(context);
        chipAll_taste.setText("Select All");
        chipAll_taste.setChipBackgroundColorResource(R.color.gray);

        chipAll_taste.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_taste.getText().toString();
                if (chipAll_taste.isSelected()) {
                    chipAll_taste.setSelected(false);
                    chipAll_taste.setTextColor(Color.BLACK);
                    chipAll_taste.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_taste.getChildCount(); i++) {
                        Chip chip = (Chip) cg_taste.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_taste.setSelected(true);
                    chipAll_taste.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_taste.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_taste.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_taste.getChildCount(); i++) {
                        Chip chip = (Chip) cg_taste.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_taste.addView(chipAll_taste);
        cg_taste.setVisibility(View.VISIBLE);
        cg_taste.getLayoutParams();

        for (int i = 0; i < chp_taste_list.size(); i++) {
            try {
                String tagname = chp_taste_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_taste_list.get(i));
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
                cg_taste.addView(chip);
                cg_taste.setVisibility(View.VISIBLE);
                cg_taste.getLayoutParams();
            } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //FOR MISCELLANEOUS
        Chip chipAll_misc = new Chip(context);
        chipAll_misc.setText("Select All");
        chipAll_misc.setChipBackgroundColorResource(R.color.gray);

        chipAll_misc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = chipAll_misc.getText().toString();
                if (chipAll_misc.isSelected()) {
                    chipAll_misc.setSelected(false);
                    chipAll_misc.setTextColor(Color.BLACK);
                    chipAll_misc.setChipBackgroundColorResource(R.color.gray);

                    for (int i = 0; i < cg_miscellaneous.getChildCount(); i++) {
                        Chip chip = (Chip) cg_miscellaneous.getChildAt(i);
                        chip.setSelected(false);
                        chip.setTextColor(Color.BLACK);
                        chip.setChipBackgroundColorResource(R.color.gray);
                        chips.remove(chip.getText().toString());
                    }

                } else {
                    chipAll_misc.setSelected(true);
                    chipAll_misc.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chipAll_misc.setChipStrokeColorResource(R.color.teal_700);
                    chipAll_misc.setTextColor(getResources().getColor(R.color.white));

                    for (int i = 0; i < cg_miscellaneous.getChildCount(); i++) {
                        Chip chip = (Chip) cg_miscellaneous.getChildAt(i);
                        chip.setSelected(true);
                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                        chip.setChipStrokeColorResource(R.color.teal_700);
                        chip.setTextColor(getResources().getColor(R.color.white));
                        chips.add(chip.getText().toString());
                    }

                }//else
            } //onClick
        }); //chipAll.setOnClickListener
        cg_miscellaneous.addView(chipAll_misc);
        cg_miscellaneous.setVisibility(View.VISIBLE);
        cg_miscellaneous.getLayoutParams();

        for (int i = 0; i < chp_misc_list.size(); i++) {
            try {
                String tagname = chp_misc_list.get(i);
                Chip chip = new Chip(context);
                chip.setText(chp_misc_list.get(i));
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
                cg_miscellaneous.addView(chip);
                cg_miscellaneous.setVisibility(View.VISIBLE);
                cg_miscellaneous.getLayoutParams();
            } //list.add(productName);

            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

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
