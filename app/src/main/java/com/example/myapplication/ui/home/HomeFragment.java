package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private FragmentHomeBinding binding;
    private RequestQueue requestQueueRec1,requestQueueRec2, requestQueueCateg, requestQueuePopu, requestQueueFood;

    private static String JSON_URL="http://192.168.68.117/android_register_login/";


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

    //For Product Bottomsheet
    LinearLayout linearLayout;
    TextView product_name,product_resto,product_price,product_description,tv_counter;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;

    //Getting Bundle

    int userId = 0;
    String userName = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       //name = ;




        Intent intent = getActivity().getIntent();
        if(intent.getStringExtra("name") != null) {
            userName = intent.getStringExtra("name");
            userId = intent.getIntExtra("id",0);
            Log.d("HOME FRAG name", userName + userId);
        } else {
            Log.d("HOME FRAG name", "FAIL");
        }

        order_item_temp_list = new ArrayList<>();
        order_temp_list = new ArrayList<>();
        //HOME CATEGORY
        /*
        home_categ_list.add(new HomeCategoryModel(R.drawable.mcdo_logo,"Chicken"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.jollibee_logo,"Manok"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.mcdo_logo,"Chicken"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.jollibee_logo,"Manok"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.mcdo_logo,"Chicken"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.jollibee_logo,"Manok"));
         */
        rv_category = root.findViewById(R.id.rv_category);
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity().getApplicationContext(),home_categ_list);
        rv_category.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_category.setHasFixedSize(true);
        rv_category.setNestedScrollingEnabled(false);
        requestQueueCateg = Singleton.getsInstance(getActivity()).getRequestQueue();
        home_categ_list = new ArrayList<>();
        extractCateg();


        //STORE REC 1
        rv_home_store_rec = root.findViewById(R.id.home_store_rec);
        rv_home_store_rec.setHasFixedSize(true);
        rv_home_store_rec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_home_store_rec.setNestedScrollingEnabled(false);
        home_store_rec_list = new ArrayList<>();
        requestQueueRec1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractDataRec1();


        rv_home_pop_store = root.findViewById(R.id.rv_home_store_popular);
        home_pop_store_list = new ArrayList<>();
        rv_home_pop_store.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_home_pop_store.setHasFixedSize(true);
        rv_home_pop_store.setNestedScrollingEnabled(false);
        requestQueuePopu = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractPopular();

        rv_food_for_you = root.findViewById(R.id.rv_home_food_for_you);
        food_for_you_list = new ArrayList<>();
        rv_food_for_you.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_food_for_you.setHasFixedSize(true);
        rv_food_for_you.setNestedScrollingEnabled(false);
        requestQueueFood = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractFoodforyou();

        //STORE REC 2
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        rv_home_store_rec2.setHasFixedSize(true);
        rv_home_store_rec2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_home_store_rec2.setNestedScrollingEnabled(false);
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        requestQueueRec2 = Singleton.getsInstance(getActivity()).getRequestQueue();
        home_store_rec_list2 = new ArrayList<>();
        extractDataRec2();

        Collections.shuffle(home_store_rec_list2);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Work in Progress!!! Magreredirect dapat sa cart screen", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                CartFragment cartFragment = new CartFragment();
                Log.d("CART", "Success");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,cartFragment).commit();
            }
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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

    //Store Recommendation for RecView 1 and 2 Function
    public void extractDataRec1(){
        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"api.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
                        long r_id = jsonObjectRec1.getLong("idStore");
                        long m_id = jsonObjectRec1.getLong("merchant_idMerchant");
                        String r_image = jsonObjectRec1.getString("storeImage");
                        String r_name = jsonObjectRec1.getString("storeName");
                        String r_description = jsonObjectRec1.getString("storeDescription");
                        String r_location = jsonObjectRec1.getString("storeLocation");
                        String r_category = jsonObjectRec1.getString("storeCategory");
                        float r_rating = (float) jsonObjectRec1.getDouble("storeRating");
                        int r_popularity = jsonObjectRec1.getInt("storePopularity");
                        String r_open = jsonObjectRec1.getString("storeStartTime");
                        String r_close = jsonObjectRec1.getString("storeEndTime");
                        String r_tags = jsonObjectRec1.getString("storeTag");

                        StoreModel rec = new StoreModel(r_id,m_id,r_image,r_name,r_description,r_location,r_category,
                                                        (float) r_rating, r_popularity, r_open, r_close, r_tags);
                        home_store_rec_list.add(rec);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),home_store_rec_list);
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
                        long r_id = jsonObject.getLong("idStore");
                        long m_id = jsonObject.getLong("merchant_idMerchant");
                        String r_image = jsonObject.getString("storeImage");
                        String r_name = jsonObject.getString("storeName");
                        String r_description = jsonObject.getString("storeDescription");
                        String r_location = jsonObject.getString("storeLocation");
                        String r_category = jsonObject.getString("storeCategory");
                        float r_rating = (float) jsonObject.getDouble("storeRating");
                        int r_popularity = jsonObject.getInt("storePopularity");
                        String r_open = jsonObject.getString("storeStartTime");
                        String r_close = jsonObject.getString("storeEndTime");
                        String r_tags = jsonObject.getString("storeTag");

                        StoreModel store2 = new StoreModel(r_id,m_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_popularity, r_open, r_close, r_tags);
                        home_store_rec_list2.add(store2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    homeStoreRecAdapter2 = new HomeStoreRecAdapter(getActivity(),home_store_rec_list2);
                    rv_home_store_rec2.setAdapter(homeStoreRecAdapter2);


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
        HomeFragment homeFragment = this;

        JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apipopu.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectPop = response.getJSONObject(i);
                        long r_id = jsonObjectPop.getLong("idStore");
                        long m_id = jsonObjectPop.getLong("merchant_idMerchant");
                        String r_image = jsonObjectPop.getString("storeImage");
                        String r_name = jsonObjectPop.getString("storeName");
                        String r_description = jsonObjectPop.getString("storeDescription");
                        String r_location = jsonObjectPop.getString("storeLocation");
                        String r_category = jsonObjectPop.getString("storeCategory");
                        float r_rating = (float) jsonObjectPop.getDouble("storeRating");
                        int r_popularity = jsonObjectPop.getInt("storePopularity");
                        String r_open = jsonObjectPop.getString("storeStartTime");
                        String r_close = jsonObjectPop.getString("storeEndTime");
                        String r_tags = jsonObjectPop.getString("storeTag");

                        StoreModel store3 = new StoreModel(r_id,m_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_popularity, r_open, r_close, r_tags);
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
    public void extractCateg(){

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
    }

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
                        int store_idStore = jsonObjectFoodforyou.getInt("store_idStore");
                        String productName = jsonObjectFoodforyou.getString("productName");
                        String productDescription = jsonObjectFoodforyou.getString("productDescription");
                        float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                        String productImage = jsonObjectFoodforyou.getString("productImage");
                        String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
                        String productTag = jsonObjectFoodforyou.getString("productTag");
                        int productPrepTime = jsonObjectFoodforyou.getInt("productPrepTime");
                        String storeName = jsonObjectFoodforyou.getString("storeName");

                        ProductModel foodfyModel = new ProductModel(idProduct,store_idStore,productName,productDescription,productPrice,productImage,
                                                                    productServingSize,productTag,productPrepTime,storeName);
                        food_for_you_list.add(foodfyModel);

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

        /*Bundle bundle = new Bundle();
        bundle.putInt("Image", food_for_you_list.get(position).getProduct_image());
        bundle.putString("Name", food_for_you_list.get(position).getProduct_name());
        bundle.putString("Description", food_for_you_list.get(position).getProduct_description());
        bundle.putString("StoreName", food_for_you_list.get(position).getStore_name());
        bundle.putFloat("Price", food_for_you_list.get(position).getProduct_price());
        bundle.putInt("Calorie", food_for_you_list.get(position).getProduct_calories());
        ProductFragment productFragment = new ProductFragment();
        productFragment.setArguments(bundle);
        Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,productFragment).commit();
        Log.d("TAG", "Success");*/
        showBottomSheet(position);

    }



    @Override
    public void onItemClickStorePopular(int position) {

        Log.d("CLICKPOPU", "Success");
        Bundle bundle = new Bundle();
        bundle.putLong("StoreId", home_pop_store_list.get(position).getStore_id());
        bundle.putString("Image", home_pop_store_list.get(position).getStore_image());
        bundle.putString("StoreName", home_pop_store_list.get(position).getStore_name());
        bundle.putString("StoreAddress", home_pop_store_list.get(position).getStore_location());
        bundle.putString("StoreCategory", home_pop_store_list.get(position).getStore_category());
        bundle.putString("StoreDescription", home_pop_store_list.get(position).getStore_category());
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,fragment).commit();

    }

    @Override
    public void onItemClick(int position) {

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

        //product_image.setImageResource(food_for_you_list.get(position).getProductImage());
        Glide.with(getActivity()).load(food_for_you_list.get(position).getProductImage()).into(product_image);
        product_name.setText(food_for_you_list.get(position).getProductName());
        product_resto.setText(food_for_you_list.get(position).getProductRestoName());
        product_description.setText(food_for_you_list.get(position).getProductDescription());
        product_price.setText("P"+food_for_you_list.get(position).getProductPrice());

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this,"Success!!!",Toast.LENGTH_SHORT).show();
                //if(arraylist.isEmpty())
                //orderlist.add.orderModel
                //else
                float tempPrice = 0;
                Log.d("ADD TO CART: ", "BEFORE ORDER_ITEM");
                Log.d("idProduct: ", String.valueOf(food_for_you_list.get(position).getIdProduct()));
                Log.d("idProduct: ", String.valueOf(food_for_you_list.get(position).getProductPrice()));
                Log.d("idProduct: ", String.valueOf(product_count));
                Log.d("idProduct: ", String.valueOf(food_for_you_list.get(position).getProductName()));
                order_item_temp_list.add(new OrderItemModel(6,food_for_you_list.get(position).getIdProduct(),
                        food_for_you_list.get(position).getProductPrice()*product_count, product_count,10 ,
                                food_for_you_list.get(position).getProductName()));
                Log.d("ADD TO CART: ", "AFTER ORDER_ITEM");
                for (int i = 0 ; i < order_item_temp_list.size() ; i++){
                    tempPrice += order_item_temp_list.get(i).getItemPrice();
                }
//                order_temp_list.add(new OrderModel(6,tempPrice,"preparing",food_for_you_list.get(position).getStore_idStore(),
//                                                  userId, order_item_temp_list));
                Log.d("ordertemp", String.valueOf(order_temp_list));

                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList(order_temp_list);
                CartFragment fragment = new CartFragment();
                //bundle.putSerializable("OrderSummary", order_list);
                bundle.putParcelableArrayList("tempOrderList", (ArrayList<? extends Parcelable>) order_item_temp_list);
                //order.putParcelable("Order",order_list.get(position));
                fragment.setArguments(bundle);
                Log.d("Bundling tempOrderList", "Success");
                bottomSheetDialog.dismiss();
            }
        });

        //Add count to order
        cl_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_count >= 0 ){
                    cl_product_minus.setClickable(true);
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
                }else{
                    product_count -=1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }



}