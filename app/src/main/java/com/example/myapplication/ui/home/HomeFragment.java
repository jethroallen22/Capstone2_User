package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.example.myapplication.models.HomeStoreRecModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.product.ProductFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private FragmentHomeBinding binding;
    private RequestQueue requestQueueRec1,requestQueueRec2, requestQueueCateg;
    private static String JSON_URL_REC="http://10.11.1.164/android_register_login/api.php";
    private static String JSON_URL_CATEG="http://10.11.1.164/android_register_login/apicateg.php";


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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        requestQueueRec1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        home_store_rec_list = new ArrayList<>();
        extractDataRec1();


        rv_home_pop_store = root.findViewById(R.id.rv_home_store_popular);
        home_pop_store_list = new ArrayList<>();
        homeStorePopularAdapter = new HomeStorePopularAdapter(home_pop_store_list, getActivity(), this);
        rv_home_pop_store.setAdapter(homeStorePopularAdapter);
        rv_home_pop_store.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_home_pop_store.setHasFixedSize(true);
        rv_home_pop_store.setNestedScrollingEnabled(false);

        rv_food_for_you = root.findViewById(R.id.rv_home_food_for_you);
        food_for_you_list = new ArrayList<>();
        food_for_you_list.add(new ProductModel(R.drawable.burger_mcdo,"Burger McDo","Lorem Ipsum Dolor Amet","McDonalds",45F,350));
        food_for_you_list.add(new ProductModel(R.drawable.chicken_joy,"Chicken Joy","Lorem Ipsum Dolor Amet","Jollibee", 99F,420));
        food_for_you_list.add(new ProductModel(R.drawable.whopper_king,"Whopper King", "Lorem Ipsum Dolor Amet","BurgerKing",199F,542));
        homeFoodForYouAdapter = new HomeFoodForYouAdapter(getActivity(),food_for_you_list,this);
        rv_food_for_you.setAdapter(homeFoodForYouAdapter);
        rv_food_for_you.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_food_for_you.setHasFixedSize(true);
        rv_food_for_you.setNestedScrollingEnabled(false);


        //STORE REC 2
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        rv_home_store_rec2.setHasFixedSize(true);
        rv_home_store_rec2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_home_store_rec2.setNestedScrollingEnabled(false);
        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        requestQueueRec2 = Singleton.getsInstance(getActivity()).getRequestQueue();
        home_store_rec_list2 = new ArrayList<>();
        extractDataRec1();
        extractDataRec2();

        Collections.shuffle(home_store_rec_list2);

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

    //Store Recommendation for RecView 1 and 2 Function
    public void extractDataRec1(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_REC, null, new Response.Listener<JSONArray>() {
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

                        StoreModel store = new StoreModel(r_image,r_name,r_description,r_location,
                                r_category, (float) r_rating);
                        home_store_rec_list.add(store);

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

        requestQueueRec1.add(jsonArrayRequest);
    }

    public void extractDataRec2(){
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, JSON_URL_REC, null, new Response.Listener<JSONArray>() {
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

                        StoreModel store2 = new StoreModel(r_image,r_name,r_description,r_location,
                                r_category, (float) r_rating);
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

        requestQueueRec2.add(jsonArrayRequest2);
    }

    //Popular Recommendation Function
    //Category Function
    public void extractCateg(){

        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL_CATEG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject1 = response.getJSONObject(i);

                        String categ_image = jsonObject1.getString("categ_image");
                        String categ_name = jsonObject1.getString("categ_name");

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


    @Override
    public void onItemClickForYou(int position) {

        Bundle bundle = new Bundle();
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
        Log.d("TAG", "Success");
    }

    @Override
    public void onItemClickStorePopular(int position) {

        Log.d("TAG", "Success");
        Bundle bundle = new Bundle();
        bundle.putString("Image", home_pop_store_list.get(position).getStore_image());
        bundle.putString("StoreName", home_pop_store_list.get(position).getStore_name());
        bundle.putString("StoreAddress", "Esterling Heights Subdivision, Guintorilan City");
        bundle.putString("StoreCategory", home_pop_store_list.get(position).getStore_category());
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,fragment).commit();
        Log.d("TAG", "Success");

    }
}