package com.example.myapplication.ui.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.os.Bundle;
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
import com.example.myapplication.R;
import com.example.myapplication.activities.Store;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.HomeCategoryModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.product.ProductFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
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
    //School IP (Change IP Address to your IP)
    private static String JSON_URL_REC="http://192.168.68.105/android_register_login/api.php";
    private static String JSON_URL_CATEG="http://192.168.68.105/android_register_login/apicateg.php";
    private static String JSON_URL_POPU="http://192.168.68.105/android_register_login/apipopu.php";
    private static String JSON_URL_FOOD="http://192.168.68.105/android_register_login/apifood.php";


    //Cafe IP
    /*
    private static String JSON_URL_REC="http://10.11.1.164/android_register_login/api.php";
    private static String JSON_URL_CATEG="http://10.11.1.164/android_register_login/apicateg.php";
    private static String JSON_URL_POPU="http://10.11.1.164/android_register_login/apipopu.php";
    */


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
    TextView product_name,product_calorie,product_price,product_description,tv_counter;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //HOME CATEGORY
        rv_category = root.findViewById(R.id.rv_category);
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
        requestQueueRec1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        home_store_rec_list = new ArrayList<>();
        extractDataRec1();

        //POPULAR
        rv_home_pop_store = root.findViewById(R.id.rv_home_store_popular);
        home_pop_store_list = new ArrayList<>();
        rv_home_pop_store.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_home_pop_store.setHasFixedSize(true);
        rv_home_pop_store.setNestedScrollingEnabled(false);
        requestQueuePopu = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractPopular();

        //FOOD FOR YOU
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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Work in Progress!!! Magreredirect dapat sa cart screen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Database to Recommendation 1
    public void extractDataRec1(){

        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.GET, JSON_URL_REC, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
                        String r_image = jsonObjectRec1.getString("r_image");
                        String r_name = jsonObjectRec1.getString("r_name");
                        String r_description = jsonObjectRec1.getString("r_description");
                        String r_location = jsonObjectRec1.getString("r_location");
                        String r_category = jsonObjectRec1.getString("r_category");
                        float r_rating = (float) jsonObjectRec1.getDouble("r_rating");
                        int r_popularity = jsonObjectRec1.getInt("r_popularity");

                        StoreModel rec = new StoreModel(r_image,r_name,r_description,r_location,r_category, (float) r_rating, r_popularity);

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

    //Database to Recommended 2
    public void extractDataRec2(){
        JsonArrayRequest jsonArrayRequestRec2 = new JsonArrayRequest(Request.Method.GET, JSON_URL_REC, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String r_image = jsonObject.getString("r_image");
                        String r_name = jsonObject.getString("r_name");
                        String r_description = jsonObject.getString("r_description");
                        String r_location = jsonObject.getString("r_location");
                        String r_category = jsonObject.getString("r_category");
                        double r_rating = jsonObject.getDouble("r_rating");
                        int r_popularity = jsonObject.getInt("r_popularity");

                        StoreModel store2 = new StoreModel(r_image,r_name,r_description,r_location,
                                r_category, (float) r_rating, r_popularity);
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

    //Database to Popular
    public void extractPopular(){
        HomeFragment homeFragment = this;

        JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest(Request.Method.GET, JSON_URL_POPU, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectPop = response.getJSONObject(i);

                        String popu_image = jsonObjectPop.getString("r_image");
                        String popu_name = jsonObjectPop.getString("r_name");
                        String popu_description = jsonObjectPop.getString("r_description");
                        String popu_location = jsonObjectPop.getString("r_location");
                        String popu_category = jsonObjectPop.getString("r_category");
                        double popu_rating = jsonObjectPop.getDouble("r_rating");
                        int popu_popularity = jsonObjectPop.getInt("r_popularity");


                        StoreModel store3 = new StoreModel(popu_image,popu_name,popu_description, popu_location,popu_category, (float) popu_rating,popu_popularity);
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

    //Database to Category Function
    public void extractCateg(){

        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL_CATEG, null, new Response.Listener<JSONArray>() {
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

    //Food Function
    public void extractFoodforyou(){
        HomeFragment homeFragment = this;
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL_FOOD, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
                        String p_image = jsonObjectFoodforyou.getString("p_image");
                        String p_name = jsonObjectFoodforyou.getString("p_name");
                        String p_description = jsonObjectFoodforyou.getString("p_description");
                        float p_price = (float) jsonObjectFoodforyou.getDouble("p_price");
                        int p_calories =  jsonObjectFoodforyou.getInt("p_name");
                        String pR_id = jsonObjectFoodforyou.getString("r_id");
                        String pR_name = jsonObjectFoodforyou.getString("r_name");

                        ProductModel foodfyModel = new ProductModel(p_image,p_name,p_description,pR_name,p_price,p_calories);
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

//        Bundle bundle = new Bundle();
//        bundle.putString("Image", food_for_you_list.get(position).getProduct_image());
//        bundle.putString("Name", food_for_you_list.get(position).getProduct_name());
//        bundle.putString("Description", food_for_you_list.get(position).getProduct_description());
//        bundle.putString("StoreName", food_for_you_list.get(position).getStore_name());
//        bundle.putFloat("Price", food_for_you_list.get(position).getProduct_price());
//        bundle.putInt("Calorie", food_for_you_list.get(position).getProduct_calories());
//        ProductFragment productFragment = new ProductFragment();
//        productFragment.setArguments(bundle);
//        Log.d("TAG", "Success");
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,productFragment).commit();
//        Log.d("TAG", "Success");
        showBottomSheet(position);
    }

    @Override
    public void onItemClickStorePopular(int position) {

        Log.d("TAG", "Success");
        Bundle bundle = new Bundle();
        bundle.putString("Image", home_pop_store_list.get(position).getStore_image());
        bundle.putString("StoreName", home_pop_store_list.get(position).getStore_name());
        bundle.putString("StoreAddress", home_pop_store_list.get(position).getStore_location());
        bundle.putString("StoreCategory", home_pop_store_list.get(position).getStore_category());
        bundle.putString("StoreDescription", home_pop_store_list.get(position).getStore_category());
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,fragment).commit();
        Log.d("TAG", "Success");
    }

    @Override
    public void onItemClick(int position) {

    }

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
        product_calorie = bottomSheetView.findViewById(R.id.tv_product_caloriee2);
        product_description = bottomSheetView.findViewById(R.id.tv_product_description2);
        product_price = bottomSheetView.findViewById(R.id.tv_product_pricee2);
        btn_add_to_cart = bottomSheetView.findViewById(R.id.btn_add_to_cart);
        cl_product_add = bottomSheetView.findViewById(R.id.cl_product_add);
        cl_product_minus = bottomSheetView.findViewById(R.id.cl_product_minus);
        tv_counter = bottomSheetView.findViewById(R.id.tv_counter);

        //product_image.setImageResource(food_for_you_list.get(position).getProduct_image());
        product_name.setText(food_for_you_list.get(position).getProduct_name());
        product_calorie.setText(Integer.toString(food_for_you_list.get(position).getProduct_calories()) + " Cals");
        product_description.setText(food_for_you_list.get(position).getProduct_description());
        product_price.setText("P"+food_for_you_list.get(position).getProduct_price().toString());

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this,"Success!!!",Toast.LENGTH_SHORT).show();
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