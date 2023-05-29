package com.example.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeDealsAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter2;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.DealsModel;
import com.example.myapplication.models.HomeCategoryModel;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.categories.CategoryFragment;
import com.example.myapplication.ui.filter.FilterFragment;
import com.example.myapplication.ui.moods.MixMoodFragment;
import com.example.myapplication.ui.moods.NewMoodFragment;
import com.example.myapplication.ui.moods.OldMoodFragment;
import com.example.myapplication.ui.moods.TrendMoodFragment;
import com.example.myapplication.ui.search.SearchFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.example.myapplication.ui.weather.WeatherFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private FragmentHomeBinding binding;
    private RequestQueue requestQueueRec1, requestQueueRec2, requestQueueCateg, requestQueuePopu, requestQueueFood, requestQueueFood2, requestQueueDeals;

    private static String JSON_URL;
    private IPModel ipModel;


    List<OrderItemModel> order_item_temp_list;
    List<OrderModel> order_temp_list;

    //Category Recycler View
    RecyclerView rv_category;
    List<HomeCategoryModel> home_categ_list;
    HomeCategoryAdapter homeCategoryAdapter;

    //Store Reco Recycler View
    RecyclerView rv_home_store_rec;
    List<StoreModel> home_store_rec_list;
    HomeStoreRecAdapter homeStoreRecAdapter;

    RecyclerView rv_home_store_rec2;
    List<StoreModel> home_store_rec_list2;
    HomeStoreRecAdapter2 homeStoreRecAdapter2;

    //Store Deals Recycler View
    RecyclerView rv_deals;
    List<DealsModel> home_deals_list;
    HomeDealsAdapter homeDealsAdapter;


    // Store Popular Recycler View
    RecyclerView rv_home_pop_store;
    List<StoreModel> home_pop_store_list;
    HomeStorePopularAdapter homeStorePopularAdapter;

    //Food For You Recycler View
    RecyclerView rv_food_for_you;
    List<ProductModel> food_for_you_list;
    HomeFoodForYouAdapter homeFoodForYouAdapter;

    RecyclerView rv_weather;
    List<ProductModel> weather_list;
    WeatherAdapter weatherAdapter;

    //Search
    SearchView searchView;
    List<SearchModel> searchModelList;

    //For Product Bottomsheet
    LinearLayout linearLayout;
    TextView product_name, product_resto, product_price, product_description, tv_counter, tv_weather;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;

    //Category
    List<StoreModel> tempStoreList;
    String category;

    //Getting Bundle
    int userId = 0;
    int tempCount = 0;
    String userName = "", weather;
    HomeFragment homeFragment = this;

    NotificationManager manager;

    Dialog moodDialog, weatherDialog;
    List<Integer> modalInt;

    Dialog filterDialog;

    ImageView iv_hot, iv_cold, iv_old, iv_new, iv_mix, iv_trend;

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("name") != null) {
            userName = intent.getStringExtra("name");
            userId = intent.getIntExtra("id", 0);
            weather = intent.getStringExtra("weather");
            Log.d("HOME FRAG name", userName + userId + " Weather: " + weather);
            Log.d("HOMEuserID", String.valueOf(userId));
            Log.d("weatherHomeFrag", weather);
        } else {
            Log.d("HOME FRAG name", "FAIL");
        }

//        modalInt = new ArrayList<>();
//        modalInt.add(1);
//        modalInt.add(2);
//        Collections.shuffle(modalInt);
//        for(int i = 0 ; i < modalInt.size() ; i++)
//            Log.d("modelInt", String.valueOf(modalInt.get(i)));
//        if(modalInt.get(0) == 1)
//            moodModal();
//        else if(modalInt.get(0) == 2)
//            weatherModal();
        moodModal();

        order_item_temp_list = new ArrayList<>();
        order_temp_list = new ArrayList<>();
        searchModelList = new ArrayList<>();
        tempStoreList = new ArrayList<>();
        //HOME CATEGORY
        home_categ_list = new ArrayList<>();
        home_categ_list.add(new HomeCategoryModel(R.drawable.desert, "Western"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.burger, "Fast Food"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.breakfast, "Breakfast"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.lunch, "Lunch"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.dinner, "Dinner"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.lamp, "Chinese"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.temple, "Japanese"));


        rv_category = root.findViewById(R.id.rv_category);
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity().getApplicationContext(), home_categ_list, HomeFragment.this);
        rv_category.setAdapter(homeCategoryAdapter);
        rv_category.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_category.setHasFixedSize(true);
        rv_category.setNestedScrollingEnabled(false);
        requestQueueCateg = Singleton.getsInstance(getActivity()).getRequestQueue();

        rv_weather = root.findViewById(R.id.rv_weather);
        rv_weather.setHasFixedSize(true);
        rv_weather.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        rv_weather.setNestedScrollingEnabled(false);
        weather_list = new ArrayList<>();
        requestQueueFood2 = Singleton.getsInstance(getActivity()).getRequestQueue();
        tv_weather = root.findViewById(R.id.tv_weather);
        tv_weather.setText("Feeling " + weather + "?");
        extractWeather();

        rv_deals = root.findViewById(R.id.rv_deals);
        home_deals_list = new ArrayList<>();
        requestQueueDeals = Singleton.getsInstance(getActivity()).getRequestQueue();
        rv_deals.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_deals.setHasFixedSize(true);
        rv_deals.setNestedScrollingEnabled(false);
        extractDeals();

        //STORE REC 1
        rv_home_store_rec = root.findViewById(R.id.home_store_rec);
        rv_home_store_rec.setHasFixedSize(true);
        rv_home_store_rec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_home_store_rec.setNestedScrollingEnabled(false);
        home_store_rec_list = new ArrayList<>();
        requestQueueRec1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractDataRec1();


        //STORE REC 2
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        rv_home_store_rec2.setHasFixedSize(true);
        rv_home_store_rec2.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_home_store_rec2.setNestedScrollingEnabled(false);
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        requestQueueRec2 = Singleton.getsInstance(getActivity()).getRequestQueue();
        home_store_rec_list2 = new ArrayList<>();
        extractDataRec2();


        rv_home_pop_store = root.findViewById(R.id.rv_home_store_popular);
        home_pop_store_list = new ArrayList<>();
        rv_home_pop_store.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_home_pop_store.setHasFixedSize(true);
        rv_home_pop_store.setNestedScrollingEnabled(false);
        requestQueuePopu = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractPopular();

        rv_food_for_you = root.findViewById(R.id.rv_home_food_for_you);
        food_for_you_list = new ArrayList<>();
        rv_food_for_you.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_food_for_you.setHasFixedSize(true);
        rv_food_for_you.setNestedScrollingEnabled(false);
        requestQueueFood = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractFoodforyou();

        //Search List
        searchView = root.findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("search", query);
                bundle.putSerializable("SearchList", (Serializable) searchModelList);
                bundle.putSerializable("ProductList", (Serializable) food_for_you_list);
                bundle.putSerializable("StoreList", (Serializable) home_store_rec_list);
                bundle.putInt("userId", userId);
                SearchFragment fragment = new SearchFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                CartFragment fragment = new CartFragment();
                bundle.putSerializable("storeList", (Serializable) home_store_rec_list);
                bundle.putInt("userID", userId);
                fragment.setArguments(bundle);
                Log.d("Bundling tempOrderItemList", String.valueOf(bundle.size()));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });

        /*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "My Notification");
        builder.setContentTitle("Mosibus");
        builder.setContentText("Order Now!");
        builder.setSmallIcon(R.drawable.logo);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity().getApplicationContext());
        managerCompat.notify(1, builder.build());

        NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
        manager = (NotificationManager) getSystemService(getActivity().getApplicationContext(), NotificationManager.class);
        manager.createNotificationChannel(channel);
        */

        final TextView textView = binding.textHome;
        return root;
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("Start", "Start");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("Resume", "Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Pause", "Pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Stop", "Stop");
    }


    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/

    public void extractWeather(){
        HomeFragment homeFragment = this;
        JsonArrayRequest jsonArrayRequest7= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifood.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject7 = response.getJSONObject(i);
                        int idProduct = jsonObject7.getInt("idProduct");
                        int idStore = jsonObject7.getInt("idStore");
                        String productName = jsonObject7.getString("productName");
                        String productDescription = jsonObject7.getString("productDescription");
                        float productPrice = (float) jsonObject7.getDouble("productPrice");
                        String productImage = jsonObject7.getString("productImage");
                        String productServingSize = jsonObject7.getString("productServingSize");
                        String productTag = jsonObject7.getString("productTag");
                        int productPrepTime = jsonObject7.getInt("productPrepTime");
                        String storeName = jsonObject7.getString("storeName");
                        String storeImage = jsonObject7.getString("storeImage");
                        String weather2 = jsonObject7.getString("weather");

                        if(weather.toLowerCase().compareTo(weather2.toLowerCase()) == 0) {
                            Log.d("Weather", weather);
                            Log.d("WeatherRead", weather2);
                            ProductModel weatherModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                    productServingSize, productTag, productPrepTime, storeName, storeImage, weather2);
                            weather_list.add(weatherModel);
                        }//list.add(productName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("WeatherListSize", String.valueOf(weather_list.size()));
                for (int i = 0 ; i < weather_list.size() ; i++){
                    Log.d("WeatherModel", weather_list.get(i).getProductName());
                }
                Collections.shuffle(weather_list);
                weatherAdapter = new WeatherAdapter(getActivity(),weather_list,homeFragment);
                rv_weather.setAdapter(weatherAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueFood2.add(jsonArrayRequest7);
    }

    public void extractDeals(){
        JsonArrayRequest jsonArrayRequestDeals = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apideals.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("DealsResponse", String.valueOf(response));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int dealId = jsonObject.getInt("dealsId");
                        int storeId = jsonObject.getInt("storeId");
                        String type = jsonObject.getString("type");
                        int percentage = jsonObject.getInt("percentage");
                        String convFee = jsonObject.getString("convFee");
                        String storeImage = jsonObject.getString("storeImage");
                        String storeName = jsonObject.getString("storeName");

                        Log.d("Dealings", String.valueOf(dealId));

                        DealsModel deal = new DealsModel(dealId,storeId,type,percentage,convFee,
                                storeImage, storeName);
                        home_deals_list.add(deal);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("DealList", String.valueOf(home_deals_list.size()));

                homeDealsAdapter = new HomeDealsAdapter(getActivity(),home_deals_list,homeFragment);
                rv_deals.setAdapter(homeDealsAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueueDeals.add(jsonArrayRequestDeals);
    }

    //Store Recommendation for RecView 1 and 2 Function
    public void extractDataRec1(){
        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"api.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("StoreResponse", String.valueOf(response));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
                        int r_id = jsonObjectRec1.getInt("idStore");
                        String r_image = jsonObjectRec1.getString("storeImage");
                        String r_name = jsonObjectRec1.getString("storeName");
                        String r_description = jsonObjectRec1.getString("storeDescription");
                        String r_location = jsonObjectRec1.getString("storeLocation");
                        String r_category = jsonObjectRec1.getString("storeCategory");
                        float r_rating = (float) jsonObjectRec1.getDouble("storeRating");
                        String r_open = jsonObjectRec1.getString("storeStartTime");
                        String r_close = jsonObjectRec1.getString("storeEndTime");

                        StoreModel rec = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_open, r_close);
                        SearchModel searchModel = new SearchModel(r_image, r_name, r_category);
                        searchModelList.add(searchModel);
                        home_store_rec_list.add(rec);
                        //list.add(r_name);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("StoreSize", String.valueOf(home_store_rec_list.size()));
                }
                JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apistorepopu.php", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseJson", String.valueOf(response));
                        for (int i=0; i < response.length(); i++){
                            Log.d("PopuLength", String.valueOf(response.length()));
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int store_idStore = jsonObject.getInt("store_idStore");
                                for (int j = 0 ; j < home_store_rec_list.size() ; j++){
                                    if(home_store_rec_list.get(j).getStore_id() == store_idStore) {
                                        Log.d("StorePopuMatch", "Match");
                                        home_pop_store_list.add(home_store_rec_list.get(j));
                                    }
                                }
                                Log.d("Popu", String.valueOf(home_pop_store_list.size()));
                                Log.d("StorePopuSize", String.valueOf(home_pop_store_list.size()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            homeStorePopularAdapter = new HomeStorePopularAdapter( home_pop_store_list,getActivity(), homeFragment);
                            rv_home_pop_store.setAdapter(homeStorePopularAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueuePopu.add(jsonArrayRequest3);
                Collections.shuffle(home_store_rec_list);
                homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),home_store_rec_list, homeFragment);
                rv_home_store_rec.setAdapter(homeStoreRecAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueRec1.add(jsonArrayRequestRec1);
    }

    public void extractDataRec2(){
        JsonArrayRequest jsonArrayRequestRec2 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"api.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int r_id = jsonObject.getInt("idStore");
                        String r_image = jsonObject.getString("storeImage");
                        String r_name = jsonObject.getString("storeName");
                        String r_description = jsonObject.getString("storeDescription");
                        String r_location = jsonObject.getString("storeLocation");
                        String r_category = jsonObject.getString("storeCategory");
                        float r_rating = (float) jsonObject.getDouble("storeRating");
                        String r_open = jsonObject.getString("storeStartTime");
                        String r_close = jsonObject.getString("storeEndTime");

                        StoreModel store2 = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_open, r_close);
                        home_store_rec_list2.add(store2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("list2Size", String.valueOf(home_store_rec_list2.size()));
                Collections.shuffle(home_store_rec_list2);
                for(int j = 0 ; j < 3 ; j++){
                    for(int k = 0 ; k < home_store_rec_list2.size() ; k++){
                        if(home_store_rec_list.get(j).getStore_id() == home_store_rec_list2.get(k).getStore_id())
                            home_store_rec_list2.remove(k);
                    }
                }
                for (int l = 0 ; l < home_store_rec_list2.size() ; l++)
                    Log.d("StoreTest",  "Pos: " + l + " " + home_store_rec_list2.get(l).getStore_name());
                Log.d("list2Size", String.valueOf(home_store_rec_list2.size()));

                homeStoreRecAdapter2 = new HomeStoreRecAdapter2(getActivity(),home_store_rec_list2, homeFragment);
                rv_home_store_rec2.setAdapter(homeStoreRecAdapter2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueueRec2.add(jsonArrayRequestRec2);
    }

    //Popular Recommendation Function
    public void extractPopular(){
//        JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apistorepopu.php", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d("ResponseJson", String.valueOf(response));
//                for (int i=0; i < response.length(); i++){
//                    Log.d("PopuLength", String.valueOf(response.length()));
//                    try {
//                        JSONObject jsonObject = response.getJSONObject(i);
//                        int store_idStore = jsonObject.getInt("store_idStore");
//                        for (int j = 0 ; j < home_store_rec_list.size() ; j++){
//                            if(home_store_rec_list.get(j).getStore_id() == store_idStore) {
//                                Log.d("StorePopuMatch", "Match");
//                                home_pop_store_list.add(home_store_rec_list.get(j));
//                            }
//                        }
//                        Log.d("Popu", String.valueOf(home_pop_store_list.size()));
//                        Log.d("StorePopuSize", String.valueOf(home_pop_store_list.size()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    homeStorePopularAdapter = new HomeStorePopularAdapter( home_pop_store_list,getActivity(), homeFragment);
//                    rv_home_pop_store.setAdapter(homeStorePopularAdapter);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueuePopu.add(jsonArrayRequest3);

    }
    //Category Function
    /*public void extractCateg(){

        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apicateg.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject1 = response.getJSONObject(i);
                        String categ_image = jsonObject1.getString("categ_image");
                        String categ_name = jsonObject1.getString("categ_name");

                        Log.d("Category", categ_image + categ_name);

                        HomeCategoryModel categModel = new HomeCategoryModel(categ_image,categ_name);
                        home_categ_list.add(categModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    homeCategoryAdapter = new HomeCategoryAdapter(getActivity(),home_categ_list);
                    rv_category.setAdapter(homeCategoryAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueCateg.add(jsonArrayRequest1);
    }*/

    //Food For You
    public void extractFoodforyou(){
        HomeFragment homeFragment = this;
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifood.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("FoodResponseLength", String.valueOf(response.length()));
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
                        String weather = jsonObjectFoodforyou.getString("weather");

                        ProductModel foodfyModel = new ProductModel(idProduct,idStore,productName,productDescription,productPrice,productImage,
                                productServingSize,productTag,productPrepTime,storeName,storeImage, weather);
                        Log.d("FoodModel", String.valueOf(foodfyModel));
                        food_for_you_list.add(foodfyModel);
                        Log.d("FoodSize", String.valueOf(food_for_you_list.size()));
                        SearchModel searchModel = new SearchModel(productImage, productName, productTag);
                        searchModelList.add(searchModel);
                        //list.add(productName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("FoodSize", String.valueOf(food_for_you_list.size()));
                    Collections.shuffle(food_for_you_list);
                    homeFoodForYouAdapter = new HomeFoodForYouAdapter(getActivity(),food_for_you_list,homeFragment);
                    rv_food_for_you.setAdapter(homeFoodForYouAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueFood.add(jsonArrayRequestFoodforyou);
    }


    @Override
    public void onItemClickForYou(int position) {
        showBottomSheet(position);
    }



    @Override
    public void onItemClickStorePopular(int position) {
        Log.d("CLICKPOPU", "Success");
        Bundle bundle = new Bundle();
//        bundle.putLong("StoreId", home_pop_store_list.get(position).getStore_id());
//        bundle.putString("Image", home_pop_store_list.get(position).getStore_image());
//        bundle.putString("StoreName", home_pop_store_list.get(position).getStore_name());
//        bundle.putString("StoreAddress", home_pop_store_list.get(position).getStore_location());
//        bundle.putString("StoreCategory", home_pop_store_list.get(position).getStore_category());
//        bundle.putString("StoreDescription", home_pop_store_list.get(position).getStore_category());
        StoreModel storeModel = home_pop_store_list.get(position);
        bundle.putParcelable("StoreClass", storeModel);
        bundle.putInt("user", userId);
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClickStoreRec(int position) {
        Log.d("CLICKSTOREREC", "Success");
        Bundle bundle = new Bundle();
//        bundle.putLong("StoreId", home_store_rec_list.get(position).getStore_id());
//        bundle.putString("Image", home_store_rec_list.get(position).getStore_image());
//        bundle.putString("StoreName", home_store_rec_list.get(position).getStore_name());
//        bundle.putString("StoreAddress", home_store_rec_list.get(position).getStore_location());
//        bundle.putString("StoreCategory", home_store_rec_list.get(position).getStore_category());
//        bundle.putString("StoreDescription", home_store_rec_list.get(position).getStore_category());
        StoreModel storeModel = home_store_rec_list.get(position);
        bundle.putParcelable("StoreClass", storeModel);
        bundle.putInt("user", userId);
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }

    @Override
    public void onItemClickDeals(int position) {
        Log.d("CLICKDEALS", "Success");
        Bundle bundle = new Bundle();
//        bundle.putLong("StoreId", home_store_rec_list.get(position).getStore_id());
//        bundle.putString("Image", home_store_rec_list.get(position).getStore_image());
//        bundle.putString("StoreName", home_store_rec_list.get(position).getStore_name());
//        bundle.putString("StoreAddress", home_store_rec_list.get(position).getStore_location());
//        bundle.putString("StoreCategory", home_store_rec_list.get(position).getStore_category());
//        bundle.putString("StoreDescription", home_store_rec_list.get(position).getStore_category());
        DealsModel dealsModel = home_deals_list.get(position);
        bundle.putParcelable("DealsClass", dealsModel);

        for(int i = 0 ; i < home_store_rec_list.size() ; i++){
            if(home_store_rec_list.get(i).getStore_id() == home_deals_list.get(position).getStoreId()){
                StoreModel storeModel = home_store_rec_list.get(i);
                bundle.putParcelable("StoreClass", storeModel);
                bundle.putParcelable("deals", home_deals_list.get(position));
                bundle.putInt("user", userId);
                StoreFragment fragment = new StoreFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        }
    }

    @Override
    public void onItemClickStoreRec2(int position) {
        Bundle bundle = new Bundle();
        StoreModel storeModel = home_store_rec_list2.get(position);
        bundle.putParcelable("StoreClass", storeModel);
        bundle.putInt("user", userId);
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }

    @Override
    public void onItemClickSearch(int pos) {

    }

    @Override
    public void onItemClickWeather(int position) {
        showWeatherBottomSheet(position);
    }

    @Override
    public void onItemClickCategory(int pos) {
        category = home_categ_list.get(pos).getCateg_name();

        Log.d("Result: ", "Success");
        Bundle bundle = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        bundle.putString("categoryString",category);
        bundle.putInt("user", userId);
        bundle.putSerializable("StoreList", (Serializable) home_store_rec_list);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

    }

    //Function
    //Display Product BottomSheet

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

        //Glide.with(getActivity()).load(food_for_you_list.get(position).getProductImage()).into(product_image);
        product_image.setImageBitmap(food_for_you_list.get(position).getBitmapImage());
        product_name.setText(food_for_you_list.get(position).getProductName());
        product_resto.setText(food_for_you_list.get(position).getProductRestoName());
        product_description.setText(food_for_you_list.get(position).getProductDescription());
        product_price.setText("P"+food_for_you_list.get(position).getProductPrice());
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
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+"tempCart.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("1 ", result );
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Log.d("TEMP CART INSERT", "success");
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Not Inserted",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("TEMP CART", "catch" );
                            Toast.makeText(getActivity().getApplicationContext(), "Catch ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("temp_productId", String.valueOf(food_for_you_list.get(position).getIdProduct()));
                        params.put("temp_storeId", String.valueOf(food_for_you_list.get(position).getStore_idStore()));
                        params.put("temp_usersId", String.valueOf(userId));
                        params.put("temp_productName", food_for_you_list.get(position).getProductName());
                        params.put("temp_productPrice", String.valueOf(food_for_you_list.get(position).getProductPrice()));
                        params.put("temp_productQuantity", String.valueOf(product_count));
                        params.put("temp_totalProductPrice", String.valueOf(product_count * food_for_you_list.get(position).getProductPrice()));
                        product_count = 0;
                        return params;
                    }

                };


                RequestQueue requestQueueTempCart = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueueTempCart.add(stringRequest);
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void showWeatherBottomSheet(int position){
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

        product_image.setImageBitmap(weather_list.get(position).getBitmapImage());
        product_name.setText(weather_list.get(position).getProductName());
        product_resto.setText(weather_list.get(position).getProductRestoName());
        product_description.setText(weather_list.get(position).getProductDescription());
        product_price.setText("P"+weather_list.get(position).getProductPrice());
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
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+"tempCart.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("1 ", result );
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Log.d("TEMP CART INSERT", "success");
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Not Inserted",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("TEMP CART", "catch" );
                            Toast.makeText(getActivity().getApplicationContext(), "Catch ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("temp_productId", String.valueOf(weather_list.get(position).getIdProduct()));
                        params.put("temp_storeId", String.valueOf(weather_list.get(position).getStore_idStore()));
                        params.put("temp_usersId", String.valueOf(userId));
                        params.put("temp_productName", weather_list.get(position).getProductName());
                        params.put("temp_productPrice", String.valueOf(weather_list.get(position).getProductPrice()));
                        params.put("temp_productQuantity", String.valueOf(product_count));
                        params.put("temp_totalProductPrice", String.valueOf(product_count * weather_list.get(position).getProductPrice()));
                        product_count = 0;
                        return params;
                    }

                };


                RequestQueue requestQueueTempCart = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueueTempCart.add(stringRequest);
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Handle settings item click
            //filterModal();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterModal(){
        filterDialog = new Dialog(this.getContext());
        filterDialog.setContentView(R.layout.filter_modal);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SeekBar sb_budget;
        Button btn_confirm_filter;
        ImageView close_modal;

        sb_budget = filterDialog.findViewById(R.id.sb_budget);
        btn_confirm_filter = filterDialog.findViewById(R.id.btn_confirm_filter);
        close_modal = filterDialog.findViewById(R.id.close_modal);
        int filterValue;


        close_modal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        sb_budget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int filterValue;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filterValue = progress;

                btn_confirm_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("FilterValue", String.valueOf(filterValue));
                        Bundle bundle = new Bundle();
                        bundle.putInt("budget", filterValue);
                        FilterFragment fragment = new FilterFragment();
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                        filterDialog.dismiss();
                    }
                });

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        filterDialog.show();
    }

    public void moodModal(){
        moodDialog = new Dialog(this.getContext());
        moodDialog.setContentView(R.layout.mood_modal);
        moodDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView moodOld, moodNew, moodMix, moodTrend;
        ImageView btnClose;

        moodOld  = moodDialog.findViewById(R.id.cv_mood_old);
        moodNew = moodDialog.findViewById(R.id.cv_mood_new);
        moodMix = moodDialog.findViewById(R.id.cv_mood_mix);
        moodTrend = moodDialog.findViewById(R.id.cv_mood_trend);
        btnClose = moodDialog.findViewById(R.id.close_modal);

        iv_old = moodDialog.findViewById(R.id.iv_old);
        iv_new = moodDialog.findViewById(R.id.iv_new);
        iv_mix = moodDialog.findViewById(R.id.iv_mix);
        iv_trend = moodDialog.findViewById(R.id.iv_trend);

        iv_old.setImageResource(R.drawable.return_to_the_past);
        iv_new.setImageResource(R.drawable.new_icon);
        iv_mix.setImageResource(R.drawable.bowl);
        iv_trend.setImageResource(R.drawable.trending);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodDialog.dismiss();
            }
        });

        moodOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) food_for_you_list);
                bundle.putInt("userId", userId);
                OldMoodFragment fragment = new OldMoodFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                moodDialog.dismiss();
            }
        });

        moodNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) food_for_you_list);
                bundle.putInt("userId", userId);
                NewMoodFragment fragment = new NewMoodFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                moodDialog.dismiss();
            }
        });

        moodMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) food_for_you_list);
                bundle.putInt("userId", userId);
                MixMoodFragment fragment = new MixMoodFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                moodDialog.dismiss();
            }
        });

        moodTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) food_for_you_list);
                bundle.putInt("userId", userId);
                TrendMoodFragment fragment = new TrendMoodFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                moodDialog.dismiss();
            }
        });

        moodDialog.show();
    }

    public void weatherModal(){
        weatherDialog = new Dialog(this.getContext());
        weatherDialog.setContentView(R.layout.weather_modal);
        weatherDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView weatherHot, weatherCold;
        ImageView btnClose;

        weatherHot  = weatherDialog.findViewById(R.id.cv_weather_hot);
        weatherCold = weatherDialog.findViewById(R.id.cv_weather_cold);
        iv_hot = weatherDialog.findViewById(R.id.iv_hot);
        iv_cold = weatherDialog.findViewById(R.id.iv_cold);
        btnClose = weatherDialog.findViewById(R.id.close_modal);

        iv_hot.setImageResource(R.drawable.hot);
        iv_cold.setImageResource(R.drawable.cold);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDialog.dismiss();
            }
        });

        weatherHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) food_for_you_list);
                bundle.putInt("userId", userId);
                bundle.putString("weather", weather);
                bundle.putInt("userId", userId);
                WeatherFragment fragment = new WeatherFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                weatherDialog.dismiss();
            }
        });

        weatherCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) food_for_you_list);
                bundle.putInt("userId", userId);
                bundle.putString("weather", weather);
                WeatherFragment fragment = new WeatherFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                weatherDialog.dismiss();
            }
        });
        weatherDialog.show();
    }


}