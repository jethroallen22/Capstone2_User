package com.example.myapplication.ui.foryou;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.models.DealsModel;
import com.example.myapplication.activities.models.HomeCategoryModel;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.activities.models.OrderItemModel;
import com.example.myapplication.activities.models.OrderModel;
import com.example.myapplication.activities.models.ProductModel;
import com.example.myapplication.activities.models.SearchModel;
import com.example.myapplication.activities.models.StoreModel;
import com.example.myapplication.activities.models.TagModel;
import com.example.myapplication.adapters.FilterAdapter;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeDealsAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter2;
import com.example.myapplication.adapters.TabFragmentAdapter;
import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.databinding.FragmentForYouBinding;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForYouFragment extends Fragment implements RecyclerViewInterface {

    private FragmentForYouBinding binding;
    private RequestQueue requestQueuepf, requestQueue, requestQueueTag, requestQueueRec1, requestQueueRec2, requestQueueCateg, requestQueuePopu, requestQueueFood, requestQueueFood2, requestQueueDeals, requestTag;

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

    //Food For You Recycler View
    RecyclerView rv_food_for_you;
    List<ProductModel> food_for_you_list;
    HomeFoodForYouAdapter homeFoodForYouAdapter;

    RecyclerView rv_weather, rv_filter;
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

    //Filter Bottomsheet

    Chip chip_categ, chip_weather, chip_mood;
    ImageView close_btn;
    SeekBar sb_budget;
    TextView tv_set_budget;
    Button btn_confirm_filter;
    List<String> chp_category_list, chp_mood_list, chp_weather_list, chp_budget;
    ImageView btn_filter;

    //Category
    List<StoreModel> tempStoreList;
    String category;

    //Getting Bundle
    int userId = 0;
    int tempCount = 0;
    String userName = "", weather;
    ForYouFragment forYouFragment = this;

    NotificationManager manager;

    Dialog moodDialog, weatherDialog;
    List<Integer> modalInt;

    Dialog filterDialog;

    ImageView iv_hot, iv_cold, iv_old, iv_new, iv_mix, iv_trend;

    ChipGroup chip_group;

    float wallet;

    private static final int EARTH_RADIUS = 6371; // Radius of the Earth in kilometers

    double curLat, curLong;
    private static int moodCtr;


    LinearLayoutManager layoutManager;

    List<TagModel> tagModelList;

    List<String> preferences;

    List<ProductModel> productModelList;

    Boolean match;

    FilterAdapter filterAdapter;


    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentForYouBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("name") != null) {
            userName = intent.getStringExtra("name");
            userId = intent.getIntExtra("id", 0);
            weather = intent.getStringExtra("weather");
            wallet = intent.getFloatExtra("wallet", 0);
            curLat = intent.getDoubleExtra("lat",0);
            curLong = intent.getDoubleExtra("long",0);

            Log.d("HOME FRAG name", userName + userId + " Weather: " + weather);
            Log.d("HOMEuserID", String.valueOf(userId));
            Log.d("weatherHomeFrag", weather);
            Log.d("curLatHome", String.valueOf(curLat));

        } else {
            Log.d("HOME FRAG name", "FAIL");
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            wallet = bundle.getFloat("wallet");
        }

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

        rv_food_for_you = root.findViewById(R.id.rv_home_food_for_you2);
        food_for_you_list = new ArrayList<>();
        rv_food_for_you.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        //rv_food_for_you.setHasFixedSize(true);
        rv_food_for_you.setNestedScrollingEnabled(false);
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueTag = Singleton.getsInstance(getActivity()).getRequestQueue();
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

        btn_filter = root.findViewById(R.id.btn_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterBottomSheet();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Start", "Start");
        //moodDialog.dismiss();
    }

    @Override
    public void onResume() {
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


    public void extractWeather() {
        weather_list.clear();
        ForYouFragment forYouFragment = this;
        JsonArrayRequest jsonArrayRequest7 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apifood.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
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

                        if (weather.equalsIgnoreCase(weather2)) {
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
                for (int i = 0; i < weather_list.size(); i++) {
                    Log.d("WeatherModel", weather_list.get(i).getProductName());
                }
                Collections.shuffle(weather_list);
                weatherAdapter = new WeatherAdapter(getActivity(), weather_list, forYouFragment);
                rv_weather.setAdapter(weatherAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueFood2.add(jsonArrayRequest7);
    }


    //Food For You
    public void extractFoodforyou(){
        productModelList = new ArrayList<>();
        preferences = new ArrayList<>();
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

                                match = false;

                                for (int t = 0; t < preferences.size(); t++) {
                                    if(preferences.get(t) == productTag){
                                        match = true;
                                    }
                                    else{
                                        match = false;
                                    }
                                }

                                if(match = true){
                                    foodfyModel.setIdProduct(idProduct);
                                    foodfyModel.setStore_idStore(idStore);
                                    foodfyModel.setProductName(productName);
                                    foodfyModel.setProductImage(productDescription);
                                    foodfyModel.setProductPrice(productPrice);
                                    foodfyModel.setProductImage(productImage);
                                    foodfyModel.setProductServingSize(productServingSize);
                                    foodfyModel.setProductTag(productTag);
                                    foodfyModel.setProductPrepTime(productPrepTime);
                                    foodfyModel.setProductRestoName(storeName);
                                    foodfyModel.setProductRestoImage(storeImage);
                                    foodfyModel.setProductRestoCategory(storeCategory);
                                    foodfyModel.setWeather(weather);
                                    productModelList.add(foodfyModel);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("ListSize", String.valueOf(productModelList.size()));
                            Collections.shuffle(productModelList);
                            filterAdapter = new FilterAdapter(getActivity(),productModelList,ForYouFragment.this);
                            rv_food_for_you.setAdapter(filterAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonArrayRequestFoodforyou);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueTag.add(jsonArrayRequestpf);

    }


    @Override
    public void onItemClickForYou(int position) {
        showBottomSheet(position);
    }

    @Override
    public void onItemClickStorePopular(int position) {

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
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
    }

    @Override
    public void onItemClickStoreRec2(int position) {

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
        bundle.putString("categoryString", category);
        bundle.putInt("user", userId);
        bundle.putSerializable("StoreList", (Serializable) home_store_rec_list);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();

    }

    @Override
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickVoucher(int pos) {

    }

    //Function
    //Display Product BottomSheet

    public void showBottomSheet(int position) {
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.product_bottom_sheet_layout,
                        getActivity().findViewById(R.id.product_bottomSheet_container)
                );
        Log.d(TAG, "bottomSheetView = LayoutInflater.from");
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
        product_price.setText("P" + food_for_you_list.get(position).getProductPrice());
        tv_counter.setText(Integer.toString(product_count));
        btn_add_to_cart.setEnabled(false);

        //Add count to order
        cl_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_count >= 0) {
                    cl_product_minus.setClickable(true);
                    btn_add_to_cart.setEnabled(true);
                    product_count += 1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        //Subtract count to order
        cl_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_count == 0) {
                    cl_product_minus.setClickable(false);
                    btn_add_to_cart.setEnabled(false);
                } else {
                    product_count -= 1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            int temp_count = 0;
            float tempPrice = 0;

            @Override
            public void onClick(View v) {
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

    public void showWeatherBottomSheet(int position) {
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.product_bottom_sheet_layout,
                        getActivity().findViewById(R.id.product_bottomSheet_container)
                );
        Log.d(TAG, "bottomSheetView = LayoutInflater.from");
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
        product_price.setText("P" + weather_list.get(position).getProductPrice());
        tv_counter.setText(Integer.toString(product_count));
        btn_add_to_cart.setEnabled(false);

        //Add count to order
        cl_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_count >= 0) {
                    cl_product_minus.setClickable(true);
                    btn_add_to_cart.setEnabled(true);
                    product_count += 1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        //Subtract count to order
        cl_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_count == 0) {
                    cl_product_minus.setClickable(false);
                    btn_add_to_cart.setEnabled(false);
                } else {
                    product_count -= 1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            int temp_count = 0;
            float tempPrice = 0;

            @Override
            public void onClick(View v) {
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

    public void showFilterBottomSheet() {

        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = getLayoutInflater().inflate(R.layout.filter_bottom_sheet_layout,
                getActivity().findViewById(R.id.filter_bottomSheet_container)
        );
        CheckBox cb_apply_pref;
        ChipGroup cg_category, cg_mood, cg_weather, cg_budget;
        int budget;
        Log.d(TAG, "bottomSheetView = LayoutInflater.from");
        cb_apply_pref = bottomSheetView.findViewById(R.id.cb_apply_pref);
        cg_category = bottomSheetView.findViewById(R.id.cg_category);
        cg_weather = bottomSheetView.findViewById(R.id.cg_weather);
        cg_mood = bottomSheetView.findViewById(R.id.cg_mood);
        close_btn = bottomSheetView.findViewById(R.id.close_btn);
        cg_budget = bottomSheetView.findViewById(R.id.cg_budget);
        btn_confirm_filter = bottomSheetView.findViewById(R.id.btn_confirm_filter);
        requestQueuepf = Singleton.getsInstance(getActivity()).getRequestQueue();

        chp_category_list = new ArrayList<>();
        chp_mood_list = new ArrayList<>();
        chp_mood_list = new ArrayList<>();

        for (int i = 0; i < home_categ_list.size(); i++) {
            chp_category_list.add(home_categ_list.get(i).getCateg_name());
        }
        chp_mood_list = Arrays.asList("Old", "New", "Mix", "Trend");
        chp_weather_list = Arrays.asList("Hot", "Cold");
        chp_budget = Arrays.asList("₱₱", "₱₱₱", "₱₱₱₱");

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

                btn_confirm_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("filterCateg", String.valueOf(category_list.size()));
                        Log.d("filterWeather", String.valueOf(weather_list.size()));
                        int budget = 0;
                        Bundle bundle = new Bundle();
                        FilterFragment fragment = new FilterFragment();
                        bundle.putInt("userId", userId);
                        bundle.putSerializable("categlist", (Serializable) category_list);
                        if (mood_list.size() != 0) {
                            bundle.putString("mood", mood_list.get(0));
                        }
                        if (budget_list.size() != 0) {
                            if (budget_list.get(0).equals("₱₱"))
                                budget = 99;
                            else if (budget_list.get(0).equals("₱₱₱"))
                                budget = 999;
                            else if (budget_list.get(0).equals("₱₱₱₱"))
                                budget = 9999;

                            bundle.putInt("budget", budget);
                        }
                        bundle.putSerializable("weatherlist", (Serializable) weather_list);
                        bundle.putSerializable("productList", (Serializable) food_for_you_list);
                        bundle.putSerializable("storeList", (Serializable) home_store_rec_list);
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueuepf.add(jsonArrayRequestpf);



        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setPeekHeight(10000);
        bottomSheetDialog.show();
    }

    public static double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        // Convert latitude and longitude to radians
        double startLatitudeRad = Math.toRadians(startLatitude);
        double startLongitudeRad = Math.toRadians(startLongitude);
        double endLatitudeRad = Math.toRadians(endLatitude);
        double endLongitudeRad = Math.toRadians(endLongitude);

        // Calculate the difference between latitudes and longitudes
        double latitudeDiff = endLatitudeRad - startLatitudeRad;
        double longitudeDiff = endLongitudeRad - startLongitudeRad;

        // Calculate the distance using Haversine formula
        double a = Math.sin(latitudeDiff / 2) * Math.sin(latitudeDiff / 2)
                + Math.cos(startLatitudeRad) * Math.cos(endLatitudeRad)
                * Math.sin(longitudeDiff / 2) * Math.sin(longitudeDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        DecimalFormat df = new DecimalFormat(".#");
        double distance = Double.parseDouble(df.format(EARTH_RADIUS * c));

        return distance;
    }

}
