package com.example.myapplication.ui.filter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentFilterBinding;
import com.example.myapplication.databinding.FragmentGalleryBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.moods.NewMoodFragment;
import com.example.myapplication.ui.weather.WeatherFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FilterFragment extends Fragment implements RecyclerViewInterface {

    private FragmentFilterBinding binding;
    //School IP
    private static String JSON_URL;
    private IPModel ipModel;
    RecyclerView rv_filter;
    List<ProductModel> productModelList;
    int budget;
    ProductAdapter productAdapter;
    RequestQueue requestQueue, requestQueueInner, requestQueueOuter;
    TextView tv_budget, tv_current;
    //For Product Bottomsheet
    LinearLayout linearLayout;
    TextView product_name, product_resto, product_price, product_description, tv_counter, tv_weather;
    ImageView iv_icon_mix;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;
    int userId = 0;
    List<String> categ_list, mood_list, weather_list;
    List<StoreModel> storeModelList;
    String mood;
    String TAG = "filter";
    LinearLayout ll_no_result;
    float current = 0;
    Dialog bugetDialog, filterDialog;
    boolean continueShop = false;
    ImageView btn_edit_budget;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.getSerializable("categlist") != null)
                categ_list = (List<String>) bundle.getSerializable("categlist");
            if(bundle.getString("mood") != null) {
                mood = bundle.getString("mood");
                Log.d(TAG, "bundleMood: " + mood);
            }
            if(bundle.getSerializable("weatherlist") != null)
                weather_list = (List<String>) bundle.getSerializable("weatherlist");
            if(bundle.getInt("budget") != 0)
                budget = bundle.getInt("budget");
            if (bundle.getInt("userId") != 0)
                userId = bundle.getInt("userId");
            if(bundle.getSerializable("productList") != null)
                productModelList = (List<ProductModel>) bundle.getSerializable("productList");
            if (bundle.getSerializable("storeList") != null)
                storeModelList = (List<StoreModel>) bundle.getSerializable("storeList");

            Log.d(TAG, "categlist: " + categ_list);
            Log.d(TAG, "weatherlist: " + weather_list);
            Log.d(TAG, "budget" + budget);

        }
        ll_no_result = root.findViewById(R.id.ll_no_result);
        tv_budget = root.findViewById(R.id.tv_budget);
        tv_current = root.findViewById(R.id.tv_current);
        rv_filter = root.findViewById(R.id.rv_filter);
        btn_edit_budget = root.findViewById(R.id.btn_edit_budget);
        if(budget == 0 ){
            budget = 999999;
            tv_budget.setText("Budget: ∞");
        } else {
            tv_budget.setText("Budget: P" + budget);
        }
        productModelList = new ArrayList<>();
        rv_filter.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_filter.setHasFixedSize(true);
        rv_filter.setNestedScrollingEnabled(false);
        requestQueue = Singleton.getsInstance(getContext()).getRequestQueue();
        requestQueueInner = Singleton.getsInstance(getContext()).getRequestQueue();
        requestQueueOuter = Singleton.getsInstance(getContext()).getRequestQueue();
        for(ProductModel product: productModelList){
            for (StoreModel store: storeModelList){
                if(product.getStore_idStore() == store.getStore_id()){
                    product.setProductRestoCategory(store.getStore_category());
                }
            }
        }
        if(mood != null){
            Log.d(TAG, "if(mood != null){");
            if(mood == "Mix"){
                Log.d(TAG, "if(mood == \"Mix\"){");
                extractMix();
            }else if( mood == "New"){
                Log.d(TAG, "}else if( mood == \"New\"){");
                extractNew();
            }else if(mood == "Old"){
                Log.d(TAG, "}else if(mood.toLowerCase() == \"Old\"){");
                extractOld();
            }else if(mood == "Trend"){
                Log.d(TAG, "}else if(mood.toLowerCase() == \"Trend\"){");
                extractTrend();
            }
        } else {
            extractFood();
        }

        binding.fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                CartFragment fragment = new CartFragment();
//                bundle.putSerializable("storeList", (Serializable) home_store_rec_list);
                bundle.putInt("userID", userId);
                fragment.setArguments(bundle);
                Log.d("Bundling tempOrderItemList", String.valueOf(bundle.size()));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });

        btn_edit_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterModal();
            }
        });

        return root;
    }

    //Food For You
    public void extractFood(){
        Log.d(TAG, "extractFood: ");
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifoodfilter.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
                        int idProduct = jsonObjectFoodforyou.getInt("idProduct");
                        int idStore = jsonObjectFoodforyou.getInt("idStore");
                        String productName = jsonObjectFoodforyou.getString("productName");
                        String productDescription = jsonObjectFoodforyou.getString("productDescription");
                        float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                        String productImage = jsonObjectFoodforyou.getString("productImage");
                        String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
                        String productTag = jsonObjectFoodforyou.getString("productTag");
                        int productPrepTime = jsonObjectFoodforyou.getInt("productPrepTime");
                        String storeName = jsonObjectFoodforyou.getString("storeName");
                        String storeImage = jsonObjectFoodforyou.getString("storeImage");
                        String storeCategory = jsonObjectFoodforyou.getString("storeCategory");
                        String weather = jsonObjectFoodforyou.getString("weather");

                        ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
                        foodfyModel.setProductRestoCategory(storeCategory);
                        Log.d(TAG, foodfyModel.toString());

                        if (categ_list.size() != 0 && weather_list.size() != 0 && budget != 0) {
                            Log.d(TAG, "if (categ_list != null && weather_list != null && budget != 0) {");
                            // If all three lists have values
                            boolean isMatch = false, tmp = false;
                            for(String categ : categ_list){
                                if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ))
                                    isMatch = true;
//                                else
//                                    isMatch = false;
                            }
                            Log.d(TAG, "categ: " + isMatch);
                            for (String weather2 : weather_list){
                                if(foodfyModel.getWeather().equalsIgnoreCase(weather2) && isMatch == true)
                                    tmp = true;
//                                else
//                                    isMatch = false;
                            }
                            isMatch = tmp;
                            Log.d(TAG, "weather: " + isMatch);
                            if(isMatch && foodfyModel.getProductPrice() <= budget) {
                                Log.d(TAG, "ADD");
                                productModelList.add(foodfyModel);
                            }
                        } else {
                            boolean isMatch = false;
                            if (categ_list.size() != 0) {
                                for (String categ : categ_list) {
                                    if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ)) {
                                        isMatch = true;
//                                        break;
                                    }
                                }
                            }
                            if (weather_list.size() != 0) {
                                for (String weather2 : weather_list) {
                                    if (foodfyModel.getWeather().equalsIgnoreCase(weather2)) {
                                        isMatch = true;
//                                        break;
                                    }
                                }
                            }
                            if (isMatch && foodfyModel.getProductPrice() <= budget) {
                                productModelList.add(foodfyModel);
                            }
                        }

                        if (productModelList.isEmpty()) {
                            ll_no_result.setVisibility(View.VISIBLE);
                        } else {
                            ll_no_result.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("ListSize", String.valueOf(productModelList.size()));
                    productAdapter = new ProductAdapter(getActivity(),productModelList,FilterFragment.this);
                    rv_filter.setAdapter(productAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequestFoodforyou);

    }

    public void extractMix(){
        Log.d(TAG, "extractMix: ");
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifoodfilter.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
                        int idProduct = jsonObjectFoodforyou.getInt("idProduct");
                        int idStore = jsonObjectFoodforyou.getInt("idStore");
                        String productName = jsonObjectFoodforyou.getString("productName");
                        String productDescription = jsonObjectFoodforyou.getString("productDescription");
                        float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                        String productImage = jsonObjectFoodforyou.getString("productImage");
                        String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
                        String productTag = jsonObjectFoodforyou.getString("productTag");
                        int productPrepTime = jsonObjectFoodforyou.getInt("productPrepTime");
                        String storeName = jsonObjectFoodforyou.getString("storeName");
                        String storeImage = jsonObjectFoodforyou.getString("storeImage");
                        String storeCategory = jsonObjectFoodforyou.getString("storeCategory");
                        String weather = jsonObjectFoodforyou.getString("weather");

                        ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
                        foodfyModel.setProductRestoCategory(storeCategory);
                        Log.d(TAG, foodfyModel.getProductName());
                        Log.d(TAG, foodfyModel.getProductRestoCategory());

                        if (categ_list.size() != 0 && weather_list.size() != 0 && budget != 0) {
                            Log.d(TAG, "if (categ_list != null && weather_list != null && budget != 0) {");
                            // If all three lists have values
                            boolean isMatch = false, tmp = false;
                            for(String categ : categ_list){
                                if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ))
                                    isMatch = true;
//                                else
//                                    isMatch = false;
                            }
                            Log.d(TAG, "categ: " + isMatch);
                            for (String weather2 : weather_list){
                                if(foodfyModel.getWeather().equalsIgnoreCase(weather2) && isMatch == true)
                                    tmp = true;
//                                else
//                                    isMatch = false;
                            }
                            isMatch = tmp;
                            Log.d(TAG, "weather: " + isMatch);
                            if(isMatch && foodfyModel.getProductPrice() <= budget) {
                                Log.d(TAG, "ADD");
                                productModelList.add(foodfyModel);
                            }
                        } else {
                            boolean isMatch = false;
                            if (categ_list.size() != 0) {
                                for (String categ : categ_list) {
                                    if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ)) {
                                        isMatch = true;
//                                        break;
                                    }
                                }
                            }
                            if (weather_list.size() != 0) {
                                for (String weather2 : weather_list) {
                                    if (foodfyModel.getWeather().equalsIgnoreCase(weather2)) {
                                        isMatch = true;
//                                        break;
                                    }
                                }
                            }
                            if (isMatch && foodfyModel.getProductPrice() <= budget) {
                                productModelList.add(foodfyModel);
                            }
                        }

                        if (productModelList.isEmpty()) {
                            ll_no_result.setVisibility(View.VISIBLE);
                        } else {
                            ll_no_result.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("ListSize", String.valueOf(productModelList.size()));
                    productAdapter = new ProductAdapter(getActivity(),productModelList,FilterFragment.this);
                    rv_filter.setAdapter(productAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequestFoodforyou);
    }

    public void extractNew(){
        Log.d(TAG, "extractNew: ");
        Log.d(TAG, "categList" + categ_list.size());
        Log.d(TAG, "weatherList" + weather_list.size());
        JsonArrayRequest jsonArrayRequestOuter= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifoodfilter.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
                        int idProduct = jsonObjectFoodforyou.getInt("idProduct");
                        int idStore = jsonObjectFoodforyou.getInt("idStore");
                        String productName = jsonObjectFoodforyou.getString("productName");
                        String productDescription = jsonObjectFoodforyou.getString("productDescription");
                        float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                        String productImage = jsonObjectFoodforyou.getString("productImage");
                        String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
                        String productTag = jsonObjectFoodforyou.getString("productTag");
                        int productPrepTime = jsonObjectFoodforyou.getInt("productPrepTime");
                        String storeName = jsonObjectFoodforyou.getString("storeName");
                        String storeImage = jsonObjectFoodforyou.getString("storeImage");
                        String storeCategory = jsonObjectFoodforyou.getString("storeCategory");
                        String weather = jsonObjectFoodforyou.getString("weather");

                        ProductModel foodfyModel = new ProductModel(idProduct,idStore,productName,productDescription,productPrice,productImage,
                                productServingSize,productTag,productPrepTime,storeName,storeImage, weather);
                        foodfyModel.setProductRestoCategory(storeCategory);

                        JsonArrayRequest jsonArrayRequestInner = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderhistoryget.php", null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("ResponseJson", String.valueOf(response));
                                boolean productExistsInOrderItems = false;

                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        int idProduct = jsonObject.getInt("idProduct");
                                        int idUser = jsonObject.getInt("idUser");

                                        if (idUser == userId && foodfyModel.getIdProduct() == idProduct) {
                                            productExistsInOrderItems = true;
                                            break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (!productExistsInOrderItems) {
                                    if (categ_list.size() != 0 && weather_list.size() != 0 && budget != 0) {
                                        Log.d(TAG, "if (categ_list != null && weather_list != null && budget != 0) {");
                                        // If all three lists have values
                                        boolean isMatch = false, tmp = false;
                                        for(String categ : categ_list){
                                            if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ))
                                                isMatch = true;
//                                else
//                                    isMatch = false;
                                        }
                                        Log.d(TAG, "categ: " + isMatch);
                                        for (String weather2 : weather_list){
                                            if(foodfyModel.getWeather().equalsIgnoreCase(weather2) && isMatch == true)
                                                tmp = true;
//                                else
//                                    isMatch = false;
                                        }
                                        isMatch = tmp;
                                        Log.d(TAG, "weather: " + isMatch);
                                        if(isMatch && foodfyModel.getProductPrice() <= budget) {
                                            Log.d(TAG, "ADD");
                                            productModelList.add(foodfyModel);
                                        }
                                    } else {
                                        boolean isMatch = false;
                                        if (categ_list.size() != 0) {
                                            for (String categ : categ_list) {
                                                if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ)) {
                                                    isMatch = true;
//                                        break;
                                                }
                                            }
                                        }
                                        if (weather_list.size() != 0) {
                                            for (String weather2 : weather_list) {
                                                if (foodfyModel.getWeather().equalsIgnoreCase(weather2)) {
                                                    isMatch = true;
//                                        break;
                                                }
                                            }
                                        }
                                        if (isMatch && foodfyModel.getProductPrice() <= budget) {
                                            productModelList.add(foodfyModel);
                                        }
                                    }
                                }

                                if (productModelList.isEmpty()) {
                                    ll_no_result.setVisibility(View.VISIBLE);
                                } else {
                                    ll_no_result.setVisibility(View.GONE);
                                }

                                productAdapter = new ProductAdapter(getActivity(), productModelList, FilterFragment.this);
                                rv_filter.setAdapter(productAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                            }
                        });
                        requestQueueInner.add(jsonArrayRequestInner);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("ListSize", String.valueOf(productModelList.size()));

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueOuter.add(jsonArrayRequestOuter);

    }

    public void extractOld(){
        Log.d(TAG, "extractOld: ");
        Log.d(TAG, "categList" + categ_list.size());
        Log.d(TAG, "weatherList" + weather_list.size());
        JsonArrayRequest jsonArrayRequestOuter= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifoodfilter.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
                        int idProduct = jsonObjectFoodforyou.getInt("idProduct");
                        int idStore = jsonObjectFoodforyou.getInt("idStore");
                        String productName = jsonObjectFoodforyou.getString("productName");
                        String productDescription = jsonObjectFoodforyou.getString("productDescription");
                        float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                        String productImage = jsonObjectFoodforyou.getString("productImage");
                        String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
                        String productTag = jsonObjectFoodforyou.getString("productTag");
                        int productPrepTime = jsonObjectFoodforyou.getInt("productPrepTime");
                        String storeName = jsonObjectFoodforyou.getString("storeName");
                        String storeImage = jsonObjectFoodforyou.getString("storeImage");
                        String storeCategory = jsonObjectFoodforyou.getString("storeCategory");
                        String weather = jsonObjectFoodforyou.getString("weather");

                        ProductModel foodfyModel = new ProductModel(idProduct,idStore,productName,productDescription,productPrice,productImage,
                                productServingSize,productTag,productPrepTime,storeName,storeImage, weather);
                        foodfyModel.setProductRestoCategory(storeCategory);

                        JsonArrayRequest jsonArrayRequestInner = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderhistoryget.php", null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("ResponseJson", String.valueOf(response));
                                boolean productExistsInOrderItems = false;

                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        int idProduct = jsonObject.getInt("idProduct");
                                        int idUser = jsonObject.getInt("idUser");

                                        if (idUser == userId && foodfyModel.getIdProduct() == idProduct) {
                                            productExistsInOrderItems = true;
                                            break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (productExistsInOrderItems) {
                                    if (categ_list.size() != 0 && weather_list.size() != 0 && budget != 0) {
                                        Log.d(TAG, "if (categ_list != null && weather_list != null && budget != 0) {");
                                        // If all three lists have values
                                        boolean isMatch = false, tmp = false;
                                        for(String categ : categ_list){
                                            if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ))
                                                isMatch = true;
//                                else
//                                    isMatch = false;
                                        }
                                        Log.d(TAG, "categ: " + isMatch);
                                        for (String weather2 : weather_list){
                                            if(foodfyModel.getWeather().equalsIgnoreCase(weather2) && isMatch == true)
                                                tmp = true;
//                                else
//                                    isMatch = false;
                                        }
                                        isMatch = tmp;
                                        Log.d(TAG, "weather: " + isMatch);
                                        if(isMatch && foodfyModel.getProductPrice() <= budget) {
                                            Log.d(TAG, "ADD");
                                            productModelList.add(foodfyModel);
                                        }
                                    } else {
                                        boolean isMatch = false;
                                        if (categ_list.size() != 0) {
                                            for (String categ : categ_list) {
                                                if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ)) {
                                                    isMatch = true;
//                                        break;
                                                }
                                            }
                                        }
                                        if (weather_list.size() != 0) {
                                            for (String weather2 : weather_list) {
                                                if (foodfyModel.getWeather().equalsIgnoreCase(weather2)) {
                                                    isMatch = true;
//                                        break;
                                                }
                                            }
                                        }
                                        if (isMatch && foodfyModel.getProductPrice() <= budget) {
                                            productModelList.add(foodfyModel);
                                        }
                                    }
                                }

                                if (productModelList.isEmpty()) {
                                    ll_no_result.setVisibility(View.VISIBLE);
                                } else {
                                    ll_no_result.setVisibility(View.GONE);
                                }

                                productAdapter = new ProductAdapter(getActivity(), productModelList, FilterFragment.this);
                                rv_filter.setAdapter(productAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                            }
                        });
                        requestQueueInner.add(jsonArrayRequestInner);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("ListSize", String.valueOf(productModelList.size()));

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueOuter.add(jsonArrayRequestOuter);
    }

    public void extractTrend(){
        Log.d(TAG, "extractTrend: ");
        Log.d(TAG, "categList" + categ_list.size());
        Log.d(TAG, "weatherList" + weather_list.size());
        JsonArrayRequest jsonArrayRequestOuter= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifoodfilter.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
                        int idProduct = jsonObjectFoodforyou.getInt("idProduct");
                        int idStore = jsonObjectFoodforyou.getInt("idStore");
                        String productName = jsonObjectFoodforyou.getString("productName");
                        String productDescription = jsonObjectFoodforyou.getString("productDescription");
                        float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                        String productImage = jsonObjectFoodforyou.getString("productImage");
                        String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
                        String productTag = jsonObjectFoodforyou.getString("productTag");
                        int productPrepTime = jsonObjectFoodforyou.getInt("productPrepTime");
                        String storeName = jsonObjectFoodforyou.getString("storeName");
                        String storeImage = jsonObjectFoodforyou.getString("storeImage");
                        String storeCategory = jsonObjectFoodforyou.getString("storeCategory");
                        String weather = jsonObjectFoodforyou.getString("weather");

                        ProductModel foodfyModel = new ProductModel(idProduct,idStore,productName,productDescription,productPrice,productImage,
                                productServingSize,productTag,productPrepTime,storeName,storeImage, weather);
                        foodfyModel.setProductRestoCategory(storeCategory);

                        JsonArrayRequest jsonArrayRequestInner = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderhistorygetpopu.php", null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("Trend Resp", String.valueOf(response));
                                boolean productExistsInOrderItems = false;

                                for (int i=0; i < response.length(); i++){
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        int idProduct = jsonObject.getInt("idProduct");
                                        for (int j = 0 ; j < productModelList.size() ; j++){
                                            if(productModelList.get(j).getIdProduct() == idProduct) {
                                                productExistsInOrderItems = true;
                                                break;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (productExistsInOrderItems) {
                                    if (categ_list.size() != 0 && weather_list.size() != 0 && budget != 0) {
                                        Log.d(TAG, "if (categ_list != null && weather_list != null && budget != 0) {");
                                        // If all three lists have values
                                        boolean isMatch = false, tmp = false;
                                        for(String categ : categ_list){
                                            if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ))
                                                isMatch = true;
//                                else
//                                    isMatch = false;
                                        }
                                        Log.d(TAG, "categ: " + isMatch);
                                        for (String weather2 : weather_list){
                                            if(foodfyModel.getWeather().equalsIgnoreCase(weather2) && isMatch == true)
                                                tmp = true;
//                                else
//                                    isMatch = false;
                                        }
                                        isMatch = tmp;
                                        Log.d(TAG, "weather: " + isMatch);
                                        if(isMatch && foodfyModel.getProductPrice() <= budget) {
                                            Log.d(TAG, "ADD");
                                            productModelList.add(foodfyModel);
                                        }
                                    } else {
                                        boolean isMatch = false;
                                        if (categ_list.size() != 0) {
                                            for (String categ : categ_list) {
                                                if (foodfyModel.getProductRestoCategory().equalsIgnoreCase(categ)) {
                                                    isMatch = true;
//                                        break;
                                                }
                                            }
                                        }
                                        if (weather_list.size() != 0) {
                                            for (String weather2 : weather_list) {
                                                if (foodfyModel.getWeather().equalsIgnoreCase(weather2)) {
                                                    isMatch = true;
//                                        break;
                                                }
                                            }
                                        }
                                        if (isMatch && foodfyModel.getProductPrice() <= budget) {
                                            productModelList.add(foodfyModel);
                                        }
                                    }
                                }

                                if (productModelList.isEmpty()) {
                                    ll_no_result.setVisibility(View.VISIBLE);
                                } else {
                                    ll_no_result.setVisibility(View.GONE);
                                }

                                productAdapter = new ProductAdapter(getActivity(), productModelList, FilterFragment.this);
                                rv_filter.setAdapter(productAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                            }
                        });
                        requestQueueInner.add(jsonArrayRequestInner);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("ListSize", String.valueOf(productModelList.size()));

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueOuter.add(jsonArrayRequestOuter);

    }

    public void showBottomSheet(int position){
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.product_bottom_sheet_layout,
                        getActivity().findViewById(R.id.product_bottomSheet_container)
                );
        Log.d(TAG,"bottomSheetView = LayoutInflater.from");
        product_image = bottomSheetView.findViewById(R.id.iv_product_imagee2);
        product_name = bottomSheetView.findViewById(R.id.tv_product_namee2);
        product_resto = bottomSheetView.findViewById(R.id.tv_product_restos);
        product_description = bottomSheetView.findViewById(R.id.tv_product_description2);
        product_price = bottomSheetView.findViewById(R.id.tv_product_pricee2);
        btn_add_to_cart = bottomSheetView.findViewById(R.id.btn_add_to_cart);
        cl_product_add = bottomSheetView.findViewById(R.id.cl_product_add);
        cl_product_minus = bottomSheetView.findViewById(R.id.cl_product_minus);
        tv_counter = bottomSheetView.findViewById(R.id.tv_counter);

        product_image.setImageBitmap(productModelList.get(position).getBitmapImage());
        product_name.setText(productModelList.get(position).getProductName());
        product_resto.setText(productModelList.get(position).getProductRestoName());
        product_description.setText(productModelList.get(position).getProductDescription());
        product_price.setText("P"+productModelList.get(position).getProductPrice());
        tv_counter.setText(Integer.toString(product_count));
        btn_add_to_cart.setEnabled(false);

        //Add count to order
        cl_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_count >= 0 ){
                    cl_product_minus.setClickable(true);
                    btn_add_to_cart.setEnabled(true);
                    product_count +=1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        //Subtract count to order
        cl_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product_count == 0){
                    cl_product_minus.setClickable(false);
                    btn_add_to_cart.setEnabled(false);
                }else{
                    product_count -=1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            int temp_count = 0;
            float tempPrice = 0;
            @Override
            public void onClick(View v) {
                current = current + (productModelList.get(position).getProductPrice() * product_count);
                if(current <= budget || continueShop == true) {
                    tv_current.setText("Current: P" + current);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "tempCart.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            Log.d("1 ", result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Log.d("TEMP CART INSERT", "success");
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Log.d("TEMP CART", "catch");
                                Toast.makeText(getActivity().getApplicationContext(), "Catch ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("temp_productId", String.valueOf(productModelList.get(position).getIdProduct()));
                            params.put("temp_storeId", String.valueOf(productModelList.get(position).getStore_idStore()));
                            params.put("temp_usersId", String.valueOf(userId));
                            params.put("temp_productName", productModelList.get(position).getProductName());
                            params.put("temp_productPrice", String.valueOf(productModelList.get(position).getProductPrice()));
                            params.put("temp_productQuantity", String.valueOf(product_count));
                            params.put("temp_totalProductPrice", String.valueOf(product_count * productModelList.get(position).getProductPrice()));
                            product_count = 0;
                            return params;
                        }

                    };


                    RequestQueue requestQueueTempCart = Volley.newRequestQueue(getActivity().getApplicationContext());
                    requestQueueTempCart.add(stringRequest);
                    bottomSheetDialog.dismiss();

                } else {
                    budgetModal();
                }

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void budgetModal(){
        bugetDialog = new Dialog(this.getContext());
        bugetDialog.setContentView(R.layout.budget_modal);
        bugetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_cont_shop, btn_proceed_cart;

        btn_cont_shop  = bugetDialog.findViewById(R.id.btn_cont_shopping);
        btn_proceed_cart = bugetDialog.findViewById(R.id.btn_proceed_cart);

        btn_cont_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueShop = true;
                bugetDialog.dismiss();
            }
        });

        btn_proceed_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment fragment = new CartFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                bugetDialog.dismiss();
            }
        });


        bugetDialog.show();
    }

    public void filterModal(){
        filterDialog = new Dialog(this.getContext());
        filterDialog.setContentView(R.layout.filter_modal);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText et_budget;
        Button btn_confirm_filter;
        ImageView close_modal;

        et_budget = filterDialog.findViewById(R.id.et_budget);
        btn_confirm_filter = filterDialog.findViewById(R.id.btn_confirm_filter3);
        close_modal = filterDialog.findViewById(R.id.close_modal);
        int filterValue;


        close_modal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        btn_confirm_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget = Integer.parseInt(String.valueOf(et_budget.getText()));
                tv_budget.setText("Budget: P" + budget);
                filterDialog.dismiss();
            }
        });

        filterDialog.show();
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
    public void onItemClickWeather(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {
        showBottomSheet(pos);
    }

    @Override
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickDeals(int pos) {

    }
}