package com.example.myapplication.ui.home;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.HomeCategoryModel;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.categories.CategoryFragment;
import com.example.myapplication.ui.moods.MixMoodFragment;
import com.example.myapplication.ui.moods.NewMoodFragment;
import com.example.myapplication.ui.moods.OldMoodFragment;
import com.example.myapplication.ui.moods.TrendMoodFragment;
import com.example.myapplication.ui.search.SearchFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.example.myapplication.ui.weather.WeatherFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
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
    private RequestQueue requestQueueRec1, requestQueueRec2, requestQueueCateg, requestQueuePopu, requestQueueFood, requestQueueFood2;

    //School IP
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
    HomeStoreRecAdapter homeStoreRecAdapter2;

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
    HomeFoodForYouAdapter weatherAdapter;

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

        modalInt = new ArrayList<>();
        modalInt.add(1);
        modalInt.add(2);
        Collections.shuffle(modalInt);
        for(int i = 0 ; i < modalInt.size() ; i++)
            Log.d("modelInt", String.valueOf(modalInt.get(i)));
        if(modalInt.get(0) == 1)
            moodModal();
        else if(modalInt.get(0) == 2)
            weatherModal();


        order_item_temp_list = new ArrayList<>();
        order_temp_list = new ArrayList<>();
        searchModelList = new ArrayList<>();
        tempStoreList = new ArrayList<>();
        //HOME CATEGORY
        home_categ_list = new ArrayList<>();
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Western"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Fast Food"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Burgers"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Breakfast"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Lunch"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Dinner"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Chinese"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Japanese"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Chiken"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.logo, "Asian"));

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

        //STORE REC 1
        rv_home_store_rec = root.findViewById(R.id.home_store_rec);
        rv_home_store_rec.setHasFixedSize(true);
        rv_home_store_rec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_home_store_rec.setNestedScrollingEnabled(false);
        home_store_rec_list = new ArrayList<>();
        requestQueueRec1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractDataRec1();


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

        //STORE REC 2
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        rv_home_store_rec2.setHasFixedSize(true);
        rv_home_store_rec2.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_home_store_rec2.setNestedScrollingEnabled(false);
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        requestQueueRec2 = Singleton.getsInstance(getActivity()).getRequestQueue();
        home_store_rec_list2 = new ArrayList<>();
        extractDataRec2();
        Collections.shuffle(home_store_rec_list2);

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
                bundle.putSerializable("tempOrderList", (Serializable) order_temp_list);
                bundle.putInt("userID", userId);
                fragment.setArguments(bundle);
                Log.d("Bundling tempOrderItemList", String.valueOf(bundle.size()));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });

        /*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "My Notification");
        builder.setContentTitle("My Title");
        builder.setContentText("Hello mga putotoy");
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
                            ProductModel weatherModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                    productServingSize, productTag, productPrepTime, storeName, storeImage, weather2);
                            weather_list.add(weatherModel);
                        }//list.add(productName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    weatherAdapter = new HomeFoodForYouAdapter(getActivity(),weather_list,homeFragment);
                    rv_weather.setAdapter(weatherAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueFood2.add(jsonArrayRequest7);
    }

    //Store Recommendation for RecView 1 and 2 Function
    public void extractDataRec1(){
        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"api.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
                        int r_popularity = jsonObjectRec1.getInt("storePopularity");
                        String r_open = jsonObjectRec1.getString("storeStartTime");
                        String r_close = jsonObjectRec1.getString("storeEndTime");

                        StoreModel rec = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                                        (float) r_rating, r_popularity, r_open, r_close);
                        SearchModel searchModel = new SearchModel(r_image, r_name, r_category);
                        searchModelList.add(searchModel);
                        home_store_rec_list.add(rec);
                        //list.add(r_name);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),home_store_rec_list, homeFragment);
                    rv_home_store_rec.setAdapter(homeStoreRecAdapter);


                }
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
                        int r_popularity = jsonObject.getInt("storePopularity");
                        String r_open = jsonObject.getString("storeStartTime");
                        String r_close = jsonObject.getString("storeEndTime");

                        StoreModel store2 = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_popularity, r_open, r_close);
                        home_store_rec_list2.add(store2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    homeStoreRecAdapter2 = new HomeStoreRecAdapter(getActivity(),home_store_rec_list2, homeFragment);
                    rv_home_store_rec2.setAdapter(homeStoreRecAdapter2);
                    Log.d("SEARCH", String.valueOf(searchModelList.size()));


                }
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

        JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apipopu.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectPop = response.getJSONObject(i);
                        int r_id = jsonObjectPop.getInt("idStore");
                        String r_image = jsonObjectPop.getString("storeImage");
                        String r_name = jsonObjectPop.getString("storeName");
                        String r_description = jsonObjectPop.getString("storeDescription");
                        String r_location = jsonObjectPop.getString("storeLocation");
                        String r_category = jsonObjectPop.getString("storeCategory");
                        float r_rating = (float) jsonObjectPop.getDouble("storeRating");
                        int r_popularity = jsonObjectPop.getInt("storePopularity");
                        String r_open = jsonObjectPop.getString("storeStartTime");
                        String r_close = jsonObjectPop.getString("storeEndTime");

                        StoreModel store3 = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_popularity, r_open, r_close);
                        home_pop_store_list.add(store3);

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
                        food_for_you_list.add(foodfyModel);

                        SearchModel searchModel = new SearchModel(productImage, productName, productTag);
                        searchModelList.add(searchModel);
                        //list.add(productName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        Log.d("CLICKPOPU", "Success");
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
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickSearch(int pos) {

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

        Glide.with(getActivity()).load(food_for_you_list.get(position).getProductImage()).into(product_image);
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


//                Log.d("ADD TO CART: ", "BEFORE ORDER_ITEM");
//                //Check if CartList is empty
//                if(order_temp_list.isEmpty()){
//                    temp_count = product_count;
//                    order_item_temp_list.add(new OrderItemModel(food_for_you_list.get(position).getIdProduct(), food_for_you_list.get(position).getStore_idStore(),
//                            food_for_you_list.get(position).getProductPrice()*temp_count, temp_count,
//                            food_for_you_list.get(position).getProductName()));
//                    product_count = 0;
//                    for (int j = 0 ; j < order_item_temp_list.size() ; j++){
//                        tempPrice += order_item_temp_list.get(j).getItemPrice();
//                    }
//                    order_temp_list.add(new OrderModel(6,tempPrice,"preparing",food_for_you_list.get(position).getStore_idStore(),
//                            food_for_you_list.get(position).getProductRestoImage(),food_for_you_list.get(position).getProductRestoName(),
//                            userId, order_item_temp_list));
//                }else {
//                    for (int i = 0; i < order_temp_list.size(); i++) {
//                        //Check if Order already exist in CartList
//                        if (order_temp_list.get(i).getStore_name().compareTo(food_for_you_list.get(position).getProductRestoName()) == 0) {
//                            // Check if order item already exist
//                            for (int k = 0 ; k < order_temp_list.get(i).getOrderItem_list().size() ; k++){
//                                if(food_for_you_list.get(position).getProductName().compareTo(order_temp_list.get(i).getOrderItem_list().get(k).getProductName()) == 0){
//                                    temp_count = product_count;
//                                    int tempItemQuantity = 0;
//                                    tempItemQuantity = order_temp_list.get(i).getOrderItem_list().get(k).getItemQuantity();
//                                    tempItemQuantity += temp_count;
//                                    product_count = 0;
//                                    order_temp_list.get(i).getOrderItem_list().get(k).setItemQuantity(tempItemQuantity);
//                                    tempItemQuantity = 0;
//                                } else{
//                                    temp_count = product_count;
//                                    order_temp_list.get(i).getOrderItem_list().add(new OrderItemModel(food_for_you_list.get(position).getIdProduct(), food_for_you_list.get(position).getStore_idStore(),
//                                            food_for_you_list.get(position).getProductPrice()*temp_count, temp_count,
//                                            food_for_you_list.get(position).getProductName()));
//                                    product_count = 0;
//                                }
//                            }
//                        } else {
//                            order_item_temp_list = new ArrayList<>();
//                            temp_count = product_count;
//                            order_item_temp_list.add(new OrderItemModel(food_for_you_list.get(position).getIdProduct(), food_for_you_list.get(position).getStore_idStore(),
//                                    food_for_you_list.get(position).getProductPrice()*temp_count, temp_count,
//                                    food_for_you_list.get(position).getProductName()));
//                                    product_count = 1;
//                            for (int j = 0; j < order_item_temp_list.size(); j++) {
//                                tempPrice += order_item_temp_list.get(j).getItemPrice();
//                            }
//                            order_temp_list.add(new OrderModel(6, tempPrice, "preparing", food_for_you_list.get(position).getStore_idStore(),
//                                    food_for_you_list.get(position).getProductRestoImage(), food_for_you_list.get(position).getProductRestoName(),
//                                    userId, order_item_temp_list));
//                        }
//                    }
//                }
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
        btnClose = weatherDialog.findViewById(R.id.close_modal);

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