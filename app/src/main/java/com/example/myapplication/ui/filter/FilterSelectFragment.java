package com.example.myapplication.ui.filter;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.databinding.FragmentFilterSelectBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.ui.home.HomeFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterSelectFragment extends Fragment {
    private FragmentFilterSelectBinding binding;
    int userId;
    Chip chip_categ, chip_weather, chip_mood;
    ImageView close_btn;
    SeekBar sb_budget;
    TextView tv_set_budget;
    Button btn_confirm_filter;
    List<String> chp_category_list, chp_mood_list, chp_weather_list, chp_budget;
    ImageView btn_filter;
    private RequestQueue requestQueuepf, requestQueuetags;
    String TAG = "filterselect";
    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFilterSelectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if(bundle != null)
            userId = bundle.getInt("userId");

        CheckBox cb_apply_pref;
        ChipGroup cg_category, cg_mood, cg_weather, cg_budget;
        int budget;
        Log.d(TAG, "bottomSheetView = LayoutInflater.from");
        cb_apply_pref = root.findViewById(R.id.cb_apply_pref);
        cg_category = root.findViewById(R.id.cg_category);
        cg_weather = root.findViewById(R.id.cg_weather);
        cg_mood = root.findViewById(R.id.cg_mood);
        close_btn = root.findViewById(R.id.close_btn);
        cg_budget = root.findViewById(R.id.cg_budget);
        btn_confirm_filter = root.findViewById(R.id.btn_confirm_filter);
        requestQueuepf = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueuetags = Singleton.getsInstance(getActivity()).getRequestQueue();

        chp_category_list = new ArrayList<>();
        chp_mood_list = new ArrayList<>();
        chp_mood_list = new ArrayList<>();

//        for (int i = 0; i < home_categ_list.size(); i++) {
//            chp_category_list.add(home_categ_list.get(i).getCateg_name());
//        }
        chp_mood_list = Arrays.asList("Old", "New", "Mix", "Trend");
        chp_weather_list = Arrays.asList("Hot", "Cold");
        chp_budget = Arrays.asList("₱99", "₱999", "₱9999", "Custom");

        List<String> category_list, mood_list, weather_list, budget_list;
        category_list = new ArrayList<>();
        mood_list = new ArrayList<>();
        budget_list = new ArrayList<>();
        weather_list = new ArrayList<>();
        String mood;
        List<String> preferences = new ArrayList<>();

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

                    }
                    catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.d("TAG SIZE after pref kinda", String.valueOf(preferences.size()));
                JsonArrayRequest jsonArrayRequesttags = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apitags.php", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject2 = response.getJSONObject(i);
                                String tagname = jsonObject2.getString("tagname");
                                if (!tagname.equals("Hot") && !tagname.equals("Cold")) {
                                    chp_category_list.add(tagname);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        for (int i = 0; i < chp_category_list.size(); i++) {
                            Chip chip = new Chip(getContext());
                            chip.setText(chp_category_list.get(i));
                            chip.setChipBackgroundColorResource(R.color.gray);
                            chip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String value = chip.getText().toString();
                                    if (chip.isSelected()) {
                                        chip.setSelected(false);
                                        chip.setTextColor(Color.BLACK);
                                        chip.setChipBackgroundColorResource(R.color.gray);
                                        category_list.remove(value);
                                    } else {
                                        chip.setSelected(true);
                                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                        chip.setChipStrokeColorResource(R.color.teal_700);
                                        chip.setTextColor(getResources().getColor(R.color.white));
                                        category_list.add(value);
                                    }
                                }
                            });
                            cg_category.addView(chip);
                        }

                        for (int i = 0; i < chp_mood_list.size(); i++) {
                            Chip chip = new Chip(getContext());
                            chip.setText(chp_mood_list.get(i));
                            chip.setChipBackgroundColorResource(R.color.gray);
                            chip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String value = chip.getText().toString();
                                    mood_list.add("");
                                    if (chip.isSelected()) {
                                        for (int i = 0; i < cg_mood.getChildCount(); i++) {
                                            Chip chip = (Chip) cg_mood.getChildAt(i);
                                            if (chip.getText() != value) {
                                                chip.setEnabled(true);
                                            }
                                        }
                                        chip.setSelected(false);
                                        chip.setTextColor(Color.BLACK);
                                        chip.setChipBackgroundColorResource(R.color.gray);
                                        mood_list.remove(value);
                                    } else {
                                        for (int i = 0; i < cg_mood.getChildCount(); i++) {
                                            Chip chip = (Chip) cg_mood.getChildAt(i);
                                            if (chip.getText() != value) {
                                                chip.setEnabled(false);
                                                chip.setChipBackgroundColorResource(R.color.darkGray);
                                            }
                                        }
                                        chip.setSelected(true);
                                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                        chip.setChipStrokeColorResource(R.color.teal_700);
                                        chip.setTextColor(getResources().getColor(R.color.white));
                                        mood_list.set(0, value);
                                    }
                                }
                            });
                            cg_mood.addView(chip);
                        }

                        cg_mood.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(ChipGroup group, int checkedId) {
                                for (int i = 0; i < group.getChildCount(); i++) {
                                    Chip chip = (Chip) group.getChildAt(i);
                                    if (chip.getId() != checkedId) {
                                        chip.setEnabled(false);
                                    }
                                }
                            }
                        });

                        for (int i = 0; i < chp_weather_list.size(); i++) {
                            Chip chip = new Chip(getContext());
                            chip.setText(chp_weather_list.get(i));
                            chip.setChipBackgroundColorResource(R.color.gray);
                            chip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String value = chip.getText().toString();
                                    if (chip.isSelected()) {
                                        chip.setSelected(false);
                                        chip.setTextColor(Color.BLACK);
                                        chip.setChipBackgroundColorResource(R.color.gray);
                                        weather_list.remove(value);

                                    } else {
                                        chip.setSelected(true);
                                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                        chip.setChipStrokeColorResource(R.color.teal_700);
                                        chip.setTextColor(getResources().getColor(R.color.white));
                                        weather_list.add(value);
                                    }
                                }
                            });
                            cg_weather.addView(chip);
                        }

                        for (int i = 0; i < chp_budget.size(); i++) {
                            Chip chip = new Chip(getContext());
                            chip.setText(chp_budget.get(i));
                            chip.setChipBackgroundColorResource(R.color.gray);
                            chip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String value = chip.getText().toString();
                                    budget_list.add("");
                                    if (chip.isSelected()) {
                                        for (int i = 0; i < cg_budget.getChildCount(); i++) {
                                            Chip chip = (Chip) cg_budget.getChildAt(i);
                                            if (chip.getText() != value) {
                                                chip.setEnabled(true);
                                            }
                                        }
                                        chip.setSelected(false);
                                        chip.setTextColor(Color.BLACK);
                                        chip.setChipBackgroundColorResource(R.color.gray);
                                        budget_list.remove(value);
                                    } else {
                                        for (int i = 0; i < cg_budget.getChildCount(); i++) {
                                            Chip chip = (Chip) cg_budget.getChildAt(i);
                                            if (chip.getText() != value) {
                                                chip.setEnabled(false);
                                                chip.setChipBackgroundColorResource(R.color.darkGray);
                                            }
                                        }
                                        chip.setSelected(true);
                                        chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                        chip.setChipStrokeColorResource(R.color.teal_700);
                                        chip.setTextColor(getResources().getColor(R.color.white));
                                        budget_list.set(0, value);
                                    }
                                }
                            });
                            cg_budget.addView(chip);
                        }

                        cg_budget.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(ChipGroup group, int checkedId) {
                                for (int i = 0; i < group.getChildCount(); i++) {
                                    Chip chip = (Chip) group.getChildAt(i);
                                    if (chip.getId() != checkedId) {
                                        chip.setEnabled(false);
                                    }
                                }
                            }
                        });

                        cb_apply_pref.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    // Select chips based on preferences
                                    for (int i = 0; i < cg_category.getChildCount(); i++) {
                                        Chip chip = (Chip) cg_category.getChildAt(i);
                                        String value = chip.getText().toString();
                                        if (preferences.contains(value)) {
                                            chip.setSelected(true);
                                            chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                            chip.setChipStrokeColorResource(R.color.teal_700);
                                            chip.setTextColor(getResources().getColor(R.color.white));
                                            category_list.add(value);
                                        } else {
                                            chip.setSelected(false);
                                            chip.setTextColor(Color.BLACK);
                                            chip.setChipBackgroundColorResource(R.color.gray);
                                            category_list.remove(value);
                                        }
                                    }
                                    for (int i = 0; i < cg_weather.getChildCount(); i++) {
                                        Chip chip = (Chip) cg_weather.getChildAt(i);
                                        String value = chip.getText().toString();
                                        if (preferences.contains(value)) {
                                            chip.setSelected(true);
                                            chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                                            chip.setChipStrokeColorResource(R.color.teal_700);
                                            chip.setTextColor(getResources().getColor(R.color.white));
                                            weather_list.add(value);
                                        } else {
                                            chip.setSelected(false);
                                            chip.setTextColor(Color.BLACK);
                                            chip.setChipBackgroundColorResource(R.color.gray);
                                            weather_list.remove(value);
                                        }
                                    }
                                } else {
                                    // Reset chips to default
                                    for (int i = 0; i < cg_category.getChildCount(); i++) {
                                        Chip chip = (Chip) cg_category.getChildAt(i);
                                        chip.setSelected(false);
                                        chip.setTextColor(Color.BLACK);
                                        chip.setChipBackgroundColorResource(R.color.gray);
                                        category_list.remove(chip.getText());
                                    }
                                    for (int i = 0; i < cg_weather.getChildCount(); i++) {
                                        Chip chip = (Chip) cg_weather.getChildAt(i);
                                        chip.setSelected(false);
                                        chip.setTextColor(Color.BLACK);
                                        chip.setChipBackgroundColorResource(R.color.gray);
                                        weather_list.remove(chip.getText());
                                    }
                                }
                            }
                        });
                        close_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HomeFragment fragment = new HomeFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                            }
                        });

                        btn_confirm_filter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("filterCateg", String.valueOf(category_list.size()));
                                Log.d("filterWeather", String.valueOf(weather_list.size()));
                                String budget = "";
                                Bundle bundle = new Bundle();
                                FilterFragment fragment = new FilterFragment();
                                bundle.putInt("userId", userId);
                                bundle.putSerializable("categlist", (Serializable) category_list);
                                if (mood_list.size() != 0) {
                                    bundle.putString("mood", mood_list.get(0));
                                }
                                if (budget_list.size() != 0) {
                                    if (budget_list.get(0).equals("Custom"))
                                        budget = "Custom";
                                    else
                                        budget = budget_list.get(0);

                                    bundle.putString("budget", budget);
                                }
                                bundle.putSerializable("weatherlist", (Serializable) weather_list);
                                fragment.setArguments(bundle);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                            }
                        });
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}