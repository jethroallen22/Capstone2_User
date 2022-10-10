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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment{

    private FragmentHomeBinding binding;

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
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.jollibee_logo, "Jollibee", "Binondo", "Fast Food", 3.5F));
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.burgerking_logo, "Burger King", "Taft Avenue", "Fast Food", 4.3F));
        home_store_rec_list.add(new HomeStoreRecModel(R.drawable.mcdo_logo, "Mcdonalds", "Abad Santos", "Fast Food", 4.5F));
        homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),home_store_rec_list);
        rv_home_store_rec.setAdapter(homeStoreRecAdapter);
        rv_home_store_rec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_home_store_rec.setHasFixedSize(true);
        rv_home_store_rec.setNestedScrollingEnabled(false);

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

}