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
import com.example.myapplication.ui.filter.FilterSelectFragment;
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
    private RequestQueue requestQueuepf, requestQueue, requestQueueTag, requestQueueOuter, requestQueueInner;

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
    List<String> tag_list;


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
        } else {
            Log.d("HOME FRAG name", "FAIL");
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            wallet = bundle.getFloat("wallet");
        }
        rv_filter = root.findViewById(R.id.rv_for_you);
        food_for_you_list = new ArrayList<>();
        tag_list = new ArrayList<>();
        rv_filter.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_filter.setNestedScrollingEnabled(false);
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueTag = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueOuter = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueInner = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractFoodForYou();

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
                Bundle bundle = new Bundle();
                FilterSelectFragment fragment = new FilterSelectFragment();
                bundle.putInt("userId", userId);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
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

    //Food For You
//    public void extractFoodForYou() {
//        tagModelList = new ArrayList<>();
//        List<ProductModel> food_for_you_list = new ArrayList<>();
//        String TAG = "foryou";
//
//        JsonArrayRequest jsonArrayRequestInner = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apifrequency.php", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d("Trend Resp", String.valueOf(response));
//                boolean productExistsInOrderItems = false;
//
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject jsonObject = response.getJSONObject(i);
//                        int idUser = jsonObject.getInt("idUser");
//                        String tagname = jsonObject.getString("tagname");
//                        if (idUser == userId)
//                            tag_list.add(tagname);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.d(TAG, "TagDBList: " + tag_list);
//                JsonArrayRequest jsonArrayRequestTag = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apitag.php", null, new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject jsonObjectTag = response.getJSONObject(i);
//                                int idProduct = jsonObjectTag.getInt("idProduct");
//                                int idStore = jsonObjectTag.getInt("idStore");
//                                String tagname = jsonObjectTag.getString("tagname");
//                                tagModelList.add(new TagModel(idProduct, idStore, tagname));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Log.d(TAG, "TagModelList: " + tagModelList);
//                        JsonArrayRequest jsonArrayRequestOuter = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apifoodfilter.php", null, new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                for (int i = 0; i < response.length(); i++) {
//                                    try {
//                                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
//                                        int idProduct = jsonObjectFoodforyou.getInt("idProduct");
//                                        int idStore = jsonObjectFoodforyou.getInt("idStore");
//                                        String productName = jsonObjectFoodforyou.getString("productName");
//                                        String productDescription = jsonObjectFoodforyou.getString("productDescription");
//                                        float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
//                                        String productImage = jsonObjectFoodforyou.getString("productImage");
//                                        String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
//                                        String productTag = jsonObjectFoodforyou.getString("productTag");
//                                        int productPrepTime = jsonObjectFoodforyou.getInt("productPrepTime");
//                                        String storeName = jsonObjectFoodforyou.getString("storeName");
//                                        String storeImage = jsonObjectFoodforyou.getString("storeImage");
//                                        String storeCategory = jsonObjectFoodforyou.getString("storeCategory");
//                                        String weather = jsonObjectFoodforyou.getString("weather");
//
//                                        ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
//                                                productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
//                                        foodfyModel.setProductRestoCategory(storeCategory);
//                                        List<TagModel> tempTagModelList = new ArrayList<>();
//                                        tempTagModelList.add(new TagModel(idProduct, idStore, productTag));
//
//                                        for (TagModel tagModel : tagModelList) {
//                                            if (tagModel.getIdProduct() == idProduct) {
//                                                tempTagModelList.add(tagModel);
//                                            }
//                                        }
//                                        foodfyModel.setTags_list(tempTagModelList);
//
//                                        for (String tag : tag_list) {
//                                            boolean tagMatched = false;
//                                            for (TagModel tagModel : foodfyModel.getTags_list()) {
//                                                if (tagModel.getTagname().equalsIgnoreCase(tag)) {
//                                                    tagMatched = true;
//                                                    break;
//                                                }
//                                            }
//                                            if (tagMatched) {
//                                                food_for_you_list.add(foodfyModel);
//                                                Log.d("pasok", "TagTrue");
//                                                break; // Break the loop if any tag matches
//                                            }
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                // Sort the list based on matched tags count in descending order
//                                Collections.sort(food_for_you_list, (product1, product2) -> {
//                                    int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
//                                    int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
//                                    return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
//                                });
//
//                                // Set up the adapter with the sorted list
//                                Log.d("foryou", "ProductSize:" + food_for_you_list.size());
//                                filterAdapter = new FilterAdapter(getActivity(), food_for_you_list, ForYouFragment.this);
//                                rv_filter.setAdapter(filterAdapter);
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // Handle error
//                            }
//                        });
//                        requestQueueOuter.add(jsonArrayRequestOuter);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
//                    }
//                });
//                requestQueueTag.add(jsonArrayRequestTag);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Handle error
//            }
//        });
//        requestQueueInner.add(jsonArrayRequestInner);
//    }

    public void extractFoodForYou(){
        tagModelList = new ArrayList<>();
        String TAG = "foryou";

        JsonArrayRequest jsonArrayRequestInner = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifrequency.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Trend Resp", String.valueOf(response));
                boolean productExistsInOrderItems = false;

                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idUser = jsonObject.getInt("idUser");
                        String tagname = jsonObject.getString("tagname");
                        if(idUser == userId)
                            tag_list.add(tagname);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Frequency: " + tag_list.size());
                for (String tag : tag_list)
                    Log.d(TAG, "Frequency: " + tag);
                JsonArrayRequest jsonArrayRequestTag = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apitag.php", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObjectTag = response.getJSONObject(i);
                                int idProduct = jsonObjectTag.getInt("idProduct");
                                int idStore = jsonObjectTag.getInt("idStore");
                                String tagname = jsonObjectTag.getString("tagname");
                                tagModelList.add(new TagModel(idProduct, idStore, tagname));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(TAG, "TagProduct: " + tagModelList.size());
                        for (TagModel tagProduct: tagModelList)
                            Log.d(TAG, "TagProduct: " + tagProduct.getTagname());
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

                                        ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                                productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
                                        foodfyModel.setProductRestoCategory(storeCategory);
                                        List<TagModel> tempTagModelList = new ArrayList<>();
//                                        tempTagModelList.add(new TagModel(idProduct, idStore, productTag));
                                        //tempTagModelList.add(new TagModel(idProduct,idStore,storeCategory));

                                        for (TagModel tagModel: tagModelList){
                                            if(tagModel.getIdProduct() == idProduct){
                                                tempTagModelList.add(tagModel);
                                            }
                                        }
                                        foodfyModel.setTags_list(tempTagModelList);
                                        food_for_you_list.add(foodfyModel);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                List<ProductModel> temp = new ArrayList<>();
                                boolean isMatch = false;
                                for (ProductModel productModel: food_for_you_list) {
                                    for (TagModel tagModel : productModel.getTags_list()) {
                                        for (String tag : tag_list) {
                                            if (tagModel.getTagname().equalsIgnoreCase(tag)) {
                                                tagModel.setMatch(true);
                                                isMatch = true;
                                                Log.d("pasok", productModel.getProductName());
                                            }
                                        }
                                    }
                                    if (isMatch) {
                                        temp.add(productModel);
                                        isMatch = false;
                                    }
                                }
                                Log.d(TAG, "Product: " + temp.size());
                                for (ProductModel productModel: temp)
                                    Log.d(TAG, "Product: " + productModel.getProductName());
                                Collections.sort(food_for_you_list, (product1, product2) -> {
                                    int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                    int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                    return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                });
                                filterAdapter = new FilterAdapter(getActivity(), food_for_you_list, ForYouFragment.this);
                                rv_filter.setAdapter(filterAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestQueueOuter.add(jsonArrayRequestOuter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueueTag.add(jsonArrayRequestTag);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });
        requestQueueInner.add(jsonArrayRequestInner);
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
        showBottomSheet(pos);
    }

    @Override
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickVoucher(int pos) {

    }

    private int calculateMatchedTagsCount(List<TagModel> tags) {
        int count = 0;
        for (TagModel tag : tags) {
            if (tag.isMatch()) {
                count++;
            }
        }
        return count;
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
