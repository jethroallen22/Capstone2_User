package com.example.myapplication.ui.moods;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentNewMoodBinding;
import com.example.myapplication.databinding.FragmentOldMoodBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.interfaces.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;



public class OldMoodFragment extends Fragment implements RecyclerViewInterface {

    private FragmentOldMoodBinding binding;

    List<ProductModel> productModelList;

    RecyclerView rvOld;

    ProductAdapter productAdapter;

    RequestQueue requestQueue;

    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOldMoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        productModelList = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        OldMoodFragment oldMoodFragment = this;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderhistoryget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int r_id = jsonObjectRec1.getInt("idStore");
                        String r_image = jsonObjectRec1.getString("storeImage");
                        String r_name = jsonObjectRec1.getString("storeName");
                        String r_description = jsonObjectRec1.getString("storeDescription");
                        String r_location = jsonObjectRec1.getString("storeLocation");
                        String r_category = jsonObjectRec1.getString("storeCategory");
                        float r_rating = (float) jsonObjectRec1.getDouble("storeRating");
                        int r_popularity = jsonObjectRec1.getInt("storePopularity");
                        String r_open = jsonObjectRec1.getString("storeStartTime");
                        String r_close = jsonObjectRec1.getString("storeEndTime");

                        StoreModel rec = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_popularity, r_open, r_close);
                        ProductModel productModel = new SearchModel(r_image, r_name, r_category);
                        productModelList.add(productModel);
                        //list.add(r_name);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    productAdapter = new ProductAdapter(getActivity(),productModelList, oldMoodFragment);
                    rvOld.setAdapter(productAdapter);


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

}