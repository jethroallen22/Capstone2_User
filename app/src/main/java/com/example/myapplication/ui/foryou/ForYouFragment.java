package com.example.myapplication.ui.foryou;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.myapplication.R;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.TagModel;
import com.example.myapplication.adapters.FilterAdapter;
import com.example.myapplication.databinding.FragmentForYouBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.ui.filter.FilterSelectFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForYouFragment extends Fragment implements RecyclerViewInterface {

    private FragmentForYouBinding binding;
    private RequestQueue requestQueue, requestQueueTag, requestQueueOuter, requestQueueInner, requestQueuepf;
    private static String JSON_URL;
    private IPModel ipModel;
    List<ProductModel> food_for_you_list, tempProductModelList;
    RecyclerView rv_filter;
    //Search
    SearchView searchView;
    //For Product Bottomsheet
    TextView product_name, product_resto, product_price, product_description, tv_counter;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;
    ImageView btn_filter;
    int userId = 0;
    String userName = "", weather;
    float wallet;
    private static final int EARTH_RADIUS = 6371; // Radius of the Earth in kilometers
    double curLat, curLong;
    List<TagModel> tagModelList;

    boolean exist = false;

    FilterAdapter filterAdapter;
    List<String> tag_list, preferences;


    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
        preferences = new ArrayList<>();
        rv_filter.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_filter.setNestedScrollingEnabled(false);
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueTag = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueOuter = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueInner = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueuepf = Singleton.getsInstance(getActivity()).getRequestQueue();
        searchView = root.findViewById(R.id.sv_for_you_page);
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

        extractFoodForYou();

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
                if(tag_list.size() != 0) {
                    Log.d(TAG, "if meron frequency");
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
                            Log.d(TAG, "============================================");
                            Log.d(TAG, "TagProduct: " + tagModelList.size());
                            for (TagModel tagProduct : tagModelList) {
                                Log.d(TAG, "TagProduct: " + tagProduct.getTagname());
                            }
                            JsonArrayRequest jsonArrayRequestOuter = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apifoodfilter.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d(TAG, "============================================");
                                    Log.d(TAG, String.valueOf(response));
                                    for (int i = 0; i < response.length(); i++) {
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
                                            int percentage = jsonObjectFoodforyou.getInt("percentage");

                                            ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                                    productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
                                            foodfyModel.setProductRestoCategory(storeCategory);
                                            foodfyModel.setPercentage(percentage);
                                            List<TagModel> tempTagModelList = new ArrayList<>();
//                                        tempTagModelList.add(new TagModel(idProduct, idStore, productTag));
                                            //tempTagModelList.add(new TagModel(idProduct,idStore,storeCategory));

                                            for (TagModel tagModel : tagModelList) {
                                                if (tagModel.getIdProduct() == idProduct) {
                                                    tempTagModelList.add(tagModel);
                                                }
                                            }
                                            foodfyModel.setTags_list(tempTagModelList);
                                            food_for_you_list.add(foodfyModel);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    Log.d(TAG, "============================================");
                                    Log.d(TAG, String.valueOf("ProductFullSize: " + food_for_you_list.size()));
                                    List<ProductModel> temp = new ArrayList<>();
                                    for (ProductModel productModel : food_for_you_list) {
                                        boolean isMatch = false;
                                        Log.d(TAG, "============================================");
                                        Log.d(TAG, "ProductModel: " + productModel.getProductName());
                                        for (TagModel tagModel : productModel.getTags_list()) {
                                            for (String tag : tag_list) {
                                                if (tagModel.getTagname().equalsIgnoreCase(tag)) {
                                                    tagModel.setMatch(true);
                                                    isMatch = true;
                                                    Log.d(TAG, productModel.getProductName());
                                                }
                                            }
                                        }
                                        if (isMatch) {
                                            temp.add(productModel);
                                            isMatch = false;
                                        }
                                    }
                                    Log.d(TAG, "============================================");
                                    Log.d(TAG, "Product: " + temp.size());
                                    for (ProductModel productModel : temp)
                                        Log.d(TAG, "Product: " + productModel.getProductName());

                                    searchView.findViewById(R.id.sv_for_you_page);
                                    tempProductModelList = new ArrayList<>();
                                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                        @Override
                                        public boolean onQueryTextSubmit(String query) {
                                            tempProductModelList.clear();
                                            for (ProductModel product : food_for_you_list) {
                                                if (product.getProductName().toLowerCase().contains(query.toLowerCase())) {
                                                    tempProductModelList.add(product);
                                                } else {
                                                    for (TagModel tag : product.getTags_list()) {
                                                        if (tag.getTagname().toLowerCase().contains(query.toLowerCase())) {
                                                            tempProductModelList.add(product);
                                                            break; // Exit the loop if a matching tag is found
                                                        }
                                                    }
                                                }
                                            }

                                            Collections.sort(tempProductModelList, (product1, product2) -> {
                                                int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                                int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                                return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                            });
                                            exist = true;
                                            filterAdapter = new FilterAdapter(getActivity(), tempProductModelList, ForYouFragment.this);
                                            rv_filter.setAdapter(filterAdapter);
                                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                            rv_filter.setLayoutManager(layoutManager);

                                            return false;
                                        }

                                        @Override
                                        public boolean onQueryTextChange(String newText) {
                                            tempProductModelList.clear();
                                            if (newText.length() > 0) {
                                                for (ProductModel product : food_for_you_list) {
                                                    if (product.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                                                        tempProductModelList.add(product);
                                                    } else {
                                                        for (TagModel tag : product.getTags_list()) {
                                                            if (tag.getTagname().toLowerCase().contains(newText.toLowerCase())) {
                                                                tempProductModelList.add(product);
                                                                break; // Exit the loop if a matching tag is found
                                                            }
                                                        }
                                                    }
                                                }

                                                Collections.sort(tempProductModelList, (product1, product2) -> {
                                                    int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                                    int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                                    return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                                });
                                                exist = true;
                                                filterAdapter = new FilterAdapter(getActivity(), tempProductModelList, ForYouFragment.this);
                                                rv_filter.setAdapter(filterAdapter);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                                rv_filter.setLayoutManager(layoutManager);
                                            } else {
                                                Collections.sort(food_for_you_list, (product1, product2) -> {
                                                    int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                                    int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                                    return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                                });
                                                exist = false;
                                                filterAdapter = new FilterAdapter(getActivity(), food_for_you_list, ForYouFragment.this);
                                                rv_filter.setAdapter(filterAdapter);
                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                                rv_filter.setLayoutManager(layoutManager);

                                            }
                                            return true;
                                        }
                                    });

                                    Collections.sort(food_for_you_list, (product1, product2) -> {
                                        int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                        int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                        return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                    });
                                    exist = false;
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
                } else {
                    Log.d(TAG, "else no frequency");
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
                                    Log.d(TAG, "============================================");
                                    Log.d(TAG, "TagProduct: " + tagModelList.size());
                                    for (TagModel tagProduct : tagModelList) {
                                        Log.d(TAG, "TagProduct: " + tagProduct.getTagname());
                                    }
                                    JsonArrayRequest jsonArrayRequestOuter = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apifoodfilter.php", null, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            Log.d(TAG, "============================================");
                                            Log.d(TAG, String.valueOf(response));
                                            for (int i = 0; i < response.length(); i++) {
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
                                                    int percentage = jsonObjectFoodforyou.getInt("percentage");

                                                    ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                                            productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
                                                    foodfyModel.setProductRestoCategory(storeCategory);
                                                    foodfyModel.setPercentage(percentage);
                                                    List<TagModel> tempTagModelList = new ArrayList<>();
//                                        tempTagModelList.add(new TagModel(idProduct, idStore, productTag));
                                                    //tempTagModelList.add(new TagModel(idProduct,idStore,storeCategory));

                                                    for (TagModel tagModel : tagModelList) {
                                                        if (tagModel.getIdProduct() == idProduct) {
                                                            tempTagModelList.add(tagModel);
                                                        }
                                                    }
                                                    foodfyModel.setTags_list(tempTagModelList);
                                                    food_for_you_list.add(foodfyModel);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.d(TAG, "============================================");
                                            Log.d(TAG, String.valueOf("ProductFullSize: " + food_for_you_list.size()));
                                            List<ProductModel> temp = new ArrayList<>();
                                            for (ProductModel productModel : food_for_you_list) {
                                                boolean isMatch = false;
                                                Log.d(TAG, "============================================");
                                                Log.d(TAG, "ProductModel: " + productModel.getProductName());
                                                for (TagModel tagModel : productModel.getTags_list()) {
                                                    for (String tag : preferences) {
                                                        if (tagModel.getTagname().equalsIgnoreCase(tag)) {
                                                            tagModel.setMatch(true);
                                                            isMatch = true;
                                                            Log.d(TAG, productModel.getProductName());
                                                        }
                                                    }
                                                }
                                                if (isMatch) {
                                                    temp.add(productModel);
                                                    isMatch = false;
                                                }
                                            }
                                            Log.d(TAG, "============================================");
                                            Log.d(TAG, "Product: " + temp.size());
                                            for (ProductModel productModel : temp)
                                                Log.d(TAG, "Product: " + productModel.getProductName());

                                            searchView.findViewById(R.id.sv_for_you_page);
                                            tempProductModelList = new ArrayList<>();
                                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                                @Override
                                                public boolean onQueryTextSubmit(String query) {
                                                    tempProductModelList.clear();
                                                    for (ProductModel product : food_for_you_list) {
                                                        if (product.getProductName().toLowerCase().contains(query.toLowerCase())) {
                                                            tempProductModelList.add(product);
                                                        } else {
                                                            for (TagModel tag : product.getTags_list()) {
                                                                if (tag.getTagname().toLowerCase().contains(query.toLowerCase())) {
                                                                    tempProductModelList.add(product);
                                                                    break; // Exit the loop if a matching tag is found
                                                                }
                                                            }
                                                        }
                                                    }

                                                    Collections.sort(tempProductModelList, (product1, product2) -> {
                                                        int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                                        int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                                        return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                                    });
                                                    exist = true;
                                                    filterAdapter = new FilterAdapter(getActivity(), tempProductModelList, ForYouFragment.this);
                                                    rv_filter.setAdapter(filterAdapter);
                                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                                    rv_filter.setLayoutManager(layoutManager);

                                                    return false;
                                                }

                                                @Override
                                                public boolean onQueryTextChange(String newText) {
                                                    tempProductModelList.clear();
                                                    if (newText.length() > 0) {
                                                        for (ProductModel product : food_for_you_list) {
                                                            if (product.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                                                                tempProductModelList.add(product);
                                                            } else {
                                                                for (TagModel tag : product.getTags_list()) {
                                                                    if (tag.getTagname().toLowerCase().contains(newText.toLowerCase())) {
                                                                        tempProductModelList.add(product);
                                                                        break; // Exit the loop if a matching tag is found
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        Collections.sort(tempProductModelList, (product1, product2) -> {
                                                            int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                                            int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                                            return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                                        });
                                                        exist = true;
                                                        filterAdapter = new FilterAdapter(getActivity(), tempProductModelList, ForYouFragment.this);
                                                        rv_filter.setAdapter(filterAdapter);
                                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                                        rv_filter.setLayoutManager(layoutManager);
                                                    } else {
                                                        Collections.sort(food_for_you_list, (product1, product2) -> {
                                                            int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                                            int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                                            return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                                        });
                                                        exist = false;
                                                        filterAdapter = new FilterAdapter(getActivity(), food_for_you_list, ForYouFragment.this);
                                                        rv_filter.setAdapter(filterAdapter);
                                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                                        rv_filter.setLayoutManager(layoutManager);

                                                    }
                                                    return true;
                                                }
                                            });

                                            Collections.sort(food_for_you_list, (product1, product2) -> {
                                                int matchedTagsCount1 = calculateMatchedTagsCount(product1.getTags_list());
                                                int matchedTagsCount2 = calculateMatchedTagsCount(product2.getTags_list());
                                                return Integer.compare(matchedTagsCount2, matchedTagsCount1); // Sort in descending order
                                            });
                                            exist = false;
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
                            error.printStackTrace();
                        }
                    });
                    requestQueuepf.add(jsonArrayRequestpf);
                }
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

    }

    @Override
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickSearch(int position, int recyclerViewId) {

    }


    @Override
    public void onItemClickSearch(int pos) {

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
        if (exist){
            product_image.setImageBitmap(tempProductModelList.get(position).getBitmapImage());
            product_name.setText(tempProductModelList.get(position).getProductName());
            product_resto.setText(tempProductModelList.get(position).getProductRestoName());
            product_description.setText(tempProductModelList.get(position).getProductDescription());
            product_price.setText("P" + tempProductModelList.get(position).getProductPrice());
            tv_counter.setText(Integer.toString(product_count));
            btn_add_to_cart.setEnabled(false);
        } else {
            product_image.setImageBitmap(food_for_you_list.get(position).getBitmapImage());
            product_name.setText(food_for_you_list.get(position).getProductName());
            product_resto.setText(food_for_you_list.get(position).getProductRestoName());
            product_description.setText(food_for_you_list.get(position).getProductDescription());
            product_price.setText("P" + food_for_you_list.get(position).getProductPrice());
            tv_counter.setText(Integer.toString(product_count));
            btn_add_to_cart.setEnabled(false);
        }

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
}
