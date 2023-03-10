package com.example.myapplication.ui.moods;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Collections;
import java.util.List;



public class OldMoodFragment extends Fragment implements RecyclerViewInterface {

    private FragmentOldMoodBinding binding;

    List<ProductModel> productModelList , tempProductModelList;
    RecyclerView rvOld;
    ProductAdapter productAdapter;
    RequestQueue requestQueue;
    int userId = 0;

    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOldMoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        productModelList = new ArrayList<>();
        tempProductModelList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null){
            productModelList = new ArrayList<>();
            productModelList = (List<ProductModel>) getArguments().getSerializable("productList");
            userId = bundle.getInt("userId");
            Collections.shuffle(productModelList);
            Log.d("Size", String.valueOf(productModelList.size()));
            for (int i = 0 ; i < productModelList.size() ; i++){
                Log.d("ProdName", String.valueOf(productModelList.get(i).getIdProduct()));
            }
        }
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        rvOld = root.findViewById(R.id.rv_old_mood);
        rvOld.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvOld.setHasFixedSize(true);
        rvOld.setNestedScrollingEnabled(false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderhistoryget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ResponseJson", String.valueOf(response));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idProduct = jsonObject.getInt("idProduct");
                        int idUser = jsonObject.getInt("idUser");
                        Log.d("UserIDBundle", String.valueOf(userId));
                        Log.d("UserIDDB", String.valueOf(idUser));

                        if(idUser == userId){
                            Log.d("UserID", "MATCH");
                            Log.d("ListSize", String.valueOf(productModelList.size()));
                            for(int j = 0 ; j < productModelList.size() ; j++){
                                Log.d("IdProductBundle", String.valueOf(idProduct));
                                Log.d("IdProductDB", String.valueOf(productModelList.get(j).getIdProduct()));
                                if (productModelList.get(j).getIdProduct() == idProduct)
                                    tempProductModelList.add(productModelList.get(j));
                            }
                            Log.d("tempList", String.valueOf(tempProductModelList.size()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    productAdapter = new ProductAdapter(getActivity(),tempProductModelList, OldMoodFragment.this);
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