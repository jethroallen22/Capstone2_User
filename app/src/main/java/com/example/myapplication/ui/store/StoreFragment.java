package com.example.myapplication.ui.store;

import static androidx.navigation.Navigation.findNavController;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.Home;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentStoreBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.HomeFoodForYouModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.product.ProductFragment;

import java.util.ArrayList;
import java.util.List;

public class StoreFragment extends Fragment implements RecyclerViewInterface {

    private FragmentStoreBinding binding;

    ImageView store_image;
    TextView store_name;
    TextView store_address;

    public int stor_image;
    public String stor_name;
    public String stor_address;
    public String stor_category;

    //Food For You Recycler View
    RecyclerView rv_food_for_you;
    List<ProductModel> food_for_you_list;
    HomeFoodForYouAdapter homeFoodForYouAdapter;

    //Product List Recycler View
    RecyclerView rv_products;
    List<HomeFoodForYouModel> products_list;
    ProductAdapter productAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StoreViewModel storeViewModel =
                new ViewModelProvider(this).get(StoreViewModel.class);

        binding = FragmentStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        store_image = root.findViewById(R.id.iv_store_image);
        store_name = root.findViewById(R.id.tv_store_name_main);
        store_address = root.findViewById(R.id.tv_store_address);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            stor_image = bundle.getInt("StoreImage");
            stor_name = bundle.getString("StoreName");
            stor_address = bundle.getString("StoreAddress");
            stor_category = bundle.getString("StoreCategory");

            store_image.setImageResource(stor_image);
            store_name.setText(stor_name);
            store_address.setText(stor_address);
        }

        rv_food_for_you = root.findViewById(R.id.rv_home_food_for_you);
        food_for_you_list = new ArrayList<>();
        food_for_you_list.add(new ProductModel(R.drawable.burger_mcdo, "Burger McDo", "Lorem Ipsum Dolor Amet","McDonalds", 45F, 350));
        food_for_you_list.add(new ProductModel(R.drawable.chicken_joy, "Chicken Joy", "Lorem Ipsum Dolor Amet","Jollibee", 99F, 420));
        food_for_you_list.add(new ProductModel(R.drawable.whopper_king, "Whopper King", "Lorem Ipsum Dolor Amet","BurgerKing", 199F, 542));
        homeFoodForYouAdapter = new HomeFoodForYouAdapter(getActivity(), food_for_you_list, this);
        rv_food_for_you.setAdapter(homeFoodForYouAdapter);
        rv_food_for_you.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_food_for_you.setHasFixedSize(true);
        rv_food_for_you.setNestedScrollingEnabled(false);

        final String TAG = "Testing";

        rv_products = root.findViewById(R.id.rv_products);
        products_list = new ArrayList<>();
        products_list.add(new HomeFoodForYouModel(R.drawable.burger_mcdo, "Burger McDo", "McDonalds", 45F, 350));
        Log.d(TAG, "prod1");
        products_list.add(new HomeFoodForYouModel(R.drawable.chicken_joy, "Chicken Joy", "Jollibee", 99F, 420));
        Log.d(TAG, "prod2");
        products_list.add(new HomeFoodForYouModel(R.drawable.whopper_king, "Whopper King", "BurgerKing", 199F, 542));
        Log.d(TAG, "prod3");
        productAdapter = new ProductAdapter(getActivity(), products_list);
        rv_products.setAdapter(productAdapter);
        rv_products.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_products.setHasFixedSize(true);
        rv_products.setNestedScrollingEnabled(false);

        binding.fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test","Success1");
                //Navigation.findNavController(view).navigate(R.id.action_nav_store_to_nav_home);
                HomeFragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,homeFragment).commit();
                Log.d("Test","Success2");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onItemClickForYou(int position) {


        Bundle bundle = new Bundle();

        //Put Store Info
        bundle.putInt ("StoreImage", stor_image);
        bundle.putString("StoreName", stor_name);
        bundle.putString("StoreAddress", stor_address);
        bundle.putString("StoreCategory", stor_category);

        //Put Product Info
        bundle.putInt("Image", food_for_you_list.get(position).getProduct_image());
        bundle.putString("Name", food_for_you_list.get(position).getProduct_name());
        bundle.putString("Description", food_for_you_list.get(position).getProduct_description());
        bundle.putString("StoreName", food_for_you_list.get(position).getStore_name());
        bundle.putFloat("Price", food_for_you_list.get(position).getProduct_price());
        bundle.putInt("Calorie", food_for_you_list.get(position).getProduct_calories());

        ProductFragment productFragment = new ProductFragment();
        productFragment.setArguments(bundle);Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,productFragment).commit();

    }


    @Override
    public void onItemClickStorePopular(int position) {

    }
}