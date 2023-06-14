package com.example.myapplication.ui.preferences;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentPreferencesBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.activities.models.IPModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreferencesFragment extends Fragment {

    private static String JSON_URL;
    private IPModel ipModel;
    private RequestQueue requestQueuepf;
    private List<String> tags, chips;
    ChipGroup chipGroup;
    int userId;

    private FragmentPreferencesBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreferencesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if(bundle != null){
            userId = bundle.getInt("id");
            Log.d("pref_id", String.valueOf(userId));
        }

        requestQueuepf = Singleton.getsInstance(getActivity()).getRequestQueue();
        chips = new ArrayList<>();
        tags = new ArrayList<>();

        // Initialize the ChipGroup object
        chipGroup = root.findViewById(R.id.cg_preferences2);

        JsonArrayRequest jsonArrayRequestpf = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apipreferences.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idUser = jsonObject.getInt("idUser");
                        String tag = jsonObject.getString("tag");
                        Log.d("tag=", tag);
                        tags.add(tag);
                        Log.d("tags", tags.get(i));

                        Log.d("chip", "inside for");
                        Chip chip = new Chip(getActivity());
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
        requestQueuepf.add(jsonArrayRequestpf);


        Log.d("pfpfpf", String.valueOf(tags.size()));


        return root;
    }



}