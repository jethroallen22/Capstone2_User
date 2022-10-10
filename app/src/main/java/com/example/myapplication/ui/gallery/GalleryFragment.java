package com.example.myapplication.ui.gallery;

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

import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentGalleryBinding;
import com.example.myapplication.models.HomeFoodForYouModel;
import com.example.myapplication.models.HomeStoreRecModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    //Food For You Recycler View
    RecyclerView rv_food_for_you;
    List<HomeFoodForYouModel> food_for_you_list;
    HomeFoodForYouAdapter homeFoodForYouAdapter;

    //Product List Recycler View
    RecyclerView rv_products;
    List<HomeFoodForYouModel> products_list;
    ProductAdapter productAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        final String TAG = "Testing";

        rv_products = root.findViewById(R.id.rv_products);
        products_list = new ArrayList<>();
        products_list.add(new HomeFoodForYouModel(R.drawable.burger_mcdo,"Burger McDo","McDonalds",45F,350));
        Log.d(TAG, "prod1");
        products_list.add(new HomeFoodForYouModel(R.drawable.chicken_joy,"Chicken Joy","Jollibee", 99F,420));
        Log.d(TAG, "prod2");
        products_list.add(new HomeFoodForYouModel(R.drawable.whopper_king,"Whopper King", "BurgerKing",199F,542));
        Log.d(TAG, "prod3");
        productAdapter = new ProductAdapter(getActivity(),products_list);
        rv_products.setAdapter(productAdapter);
        rv_products.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_products.setHasFixedSize(true);
        rv_products.setNestedScrollingEnabled(false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}