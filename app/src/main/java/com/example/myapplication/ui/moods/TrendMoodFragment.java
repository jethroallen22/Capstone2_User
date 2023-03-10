package com.example.myapplication.ui.moods;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentOldMoodBinding;
import com.example.myapplication.databinding.FragmentTrendMoodBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TrendMoodFragment extends Fragment implements RecyclerViewInterface {

    private FragmentTrendMoodBinding binding;

    List<ProductModel> productModelList;

    RecyclerView rvTrend;

    ProductAdapter productAdapter;

    RequestQueue requestQueue;

    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTrendMoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        productModelList = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        TrendMoodFragment trendMoodFragment = this;
        rvTrend = root.findViewById(R.id.rv_trend_mood);
        rvTrend.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvTrend.setHasFixedSize(true);
        rvTrend.setNestedScrollingEnabled(false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderhistorygetpopu.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idProduct = jsonObject.getInt("idProduct");


                        //list.add(r_name);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    productAdapter = new ProductAdapter(getActivity(),productModelList, trendMoodFragment);
                    rvTrend.setAdapter(productAdapter);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClickForYou(int position) {

    }

    @Override
    public void onItemClickStorePopular(int position) {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClickStoreRec(int position) {

    }

    @Override
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickSearch(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {

    }
}