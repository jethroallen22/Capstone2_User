package com.example.myapplication.ui.home;

import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.models.HomeCategoryModel;
import com.example.myapplication.models.HomeFoodForYouModel;
import com.example.myapplication.models.HomeStorePopularModel;
import com.example.myapplication.models.HomeStoreRecModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment{

    private FragmentHomeBinding binding;
    private static String JSON_URL="http://10.11.1.164/android_register_login/api.php";

    //Category Recycler View
    RecyclerView rv_category;
    List<HomeCategoryModel> home_categ_list;
    HomeCategoryAdapter homeCategoryAdapter;

    //Store Reco Recycler View
    RecyclerView rv_home_store_rec;
    List<HomeStoreRecModel> home_store_rec_list;
    HomeStoreRecAdapter homeStoreRecAdapter;

    RecyclerView rv_home_store_rec2;

    // Store Popular Recycler View
    RecyclerView rv_home_pop_store;
    List<HomeStorePopularModel> home_pop_store_list;
    HomeStorePopularAdapter homeStorePopularAdapter;

    //Food For You Recycler View
    RecyclerView rv_food_for_you;
    List<HomeFoodForYouModel> food_for_you_list;
    HomeFoodForYouAdapter homeFoodForYouAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_category = root.findViewById(R.id.rv_category);
        home_categ_list = new ArrayList<>();

        home_categ_list.add(new HomeCategoryModel(R.drawable.mcdo_logo,"Chicken"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.jollibee_logo,"Manok"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.mcdo_logo,"Chicken"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.jollibee_logo,"Manok"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.mcdo_logo,"Chicken"));
        home_categ_list.add(new HomeCategoryModel(R.drawable.jollibee_logo,"Manok"));
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity(),home_categ_list);
        rv_category.setAdapter(homeCategoryAdapter);
        rv_category.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_category.setHasFixedSize(true);
        rv_category.setNestedScrollingEnabled(false);

        rv_home_store_rec = root.findViewById(R.id.home_store_rec);
        home_store_rec_list = new ArrayList<>();
        extractResto();
        /*
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.jollibee_logo, "Jollibee", "Binondo", "Fast Food", 3.5F));
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.burgerking_logo, "Burger King", "Taft Avenue", "Fast Food", 4.3F));
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.mcdo_logo, "Mcdonalds", "Abad Santos", "Fast Food", 4.5F));
        homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),home_store_rec_list);
        rv_home_store_rec.setAdapter(homeStoreRecAdapter);
        rv_home_store_rec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_home_store_rec.setHasFixedSize(true);
        rv_home_store_rec.setNestedScrollingEnabled(false);
         */

        rv_home_pop_store = root.findViewById(R.id.rv_home_store_popular);
        home_pop_store_list = new ArrayList<>();
        home_pop_store_list.add(new HomeStorePopularModel(R.drawable.jollibee_logo,"Jollibee", "Chicken"));
        home_pop_store_list.add(new HomeStorePopularModel(R.drawable.burgerking_logo, "Burger King", "Burger"));
        home_pop_store_list.add(new HomeStorePopularModel(R.drawable.mcdo_logo, "McDonalds", "Chicken"));
        homeStorePopularAdapter = new HomeStorePopularAdapter(getActivity(),home_pop_store_list);
        rv_home_pop_store.setAdapter(homeStorePopularAdapter);
        rv_home_pop_store.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_home_pop_store.setHasFixedSize(true);
        rv_home_pop_store.setNestedScrollingEnabled(false);

        rv_food_for_you = root.findViewById(R.id.rv_home_food_for_you);
        food_for_you_list = new ArrayList<>();
        food_for_you_list.add(new HomeFoodForYouModel(R.drawable.burger_mcdo,"Burger McDo","McDonalds",45F,350));
        food_for_you_list.add(new HomeFoodForYouModel(R.drawable.chicken_joy,"Chicken Joy","Jollibee", 99F,420));
        food_for_you_list.add(new HomeFoodForYouModel(R.drawable.whopper_king,"Whopper King", "BurgerKing",199F,542));
        homeFoodForYouAdapter = new HomeFoodForYouAdapter(getActivity(),food_for_you_list);
        rv_food_for_you.setAdapter(homeFoodForYouAdapter);
        rv_food_for_you.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_food_for_you.setHasFixedSize(true);
        rv_food_for_you.setNestedScrollingEnabled(false);

        rv_home_store_rec2 = root.findViewById(R.id.home_store_rec2);
        home_store_rec_list = new ArrayList<>();
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.jollibee_logo, "Jollibee", "Binondo", "Fast Food", 3.5F));
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.burgerking_logo, "Burger King", "Taft Avenue", "Fast Food", 4.3F));
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.mcdo_logo, "Mcdonalds", "Abad Santos", "Fast Food", 4.5F));
        Collections.shuffle(home_store_rec_list);
        homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),home_store_rec_list);
        rv_home_store_rec2.setAdapter(homeStoreRecAdapter);
        rv_home_store_rec2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_home_store_rec2.setHasFixedSize(true);
        rv_home_store_rec2.setNestedScrollingEnabled(false);


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void extractResto(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray resto_info = new JSONArray(response);

                            for(int i = 0; i < resto_info.length(); i++){
                                JSONObject restoInfoObject = resto_info.getJSONObject(i);

                                int r_id = restoInfoObject.getInt("resto_id");
                                String r_name = restoInfoObject.getString("resto_name");
                                String r_address = restoInfoObject.getString("resto_address");
                                String r_open = restoInfoObject.getString("resto_open");
                                String r_close = restoInfoObject.getString("resto_close");
                                String r_category = restoInfoObject.getString("resto_category");
                                float r_rating = (float) restoInfoObject.getDouble("resto_rating");
                                String r_desc = restoInfoObject.getString("resto_desc");

                                HomeStoreRecModel store = new HomeStoreRecModel(r_id,r_name,r_address,r_category,r_rating);
                                home_store_rec_list.add(store);

                            }
                            homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),home_store_rec_list);
                            rv_home_store_rec.setAdapter(homeStoreRecAdapter);
                            rv_home_store_rec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                            rv_home_store_rec.setHasFixedSize(true);
                            rv_home_store_rec.setNestedScrollingEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}