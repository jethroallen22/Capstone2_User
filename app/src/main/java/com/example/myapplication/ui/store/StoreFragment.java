package com.example.myapplication.ui.store;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.myapplication.activities.Login;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentStoreBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.HomeFoodForYouModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.product.ProductFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreFragment extends Fragment implements RecyclerViewInterface {

    private FragmentStoreBinding binding;
    private RequestQueue requestQueueFood,requestQueueProd;

    ImageView store_image;
    TextView store_name;
    TextView store_address;
    TextView store_description;

    public long stor_id;
    public int stor_image;
    public String stor_name;
    public String stor_address;
    public String stor_category;
    public String stor_description;

    private static String JSON_URL="http://192.168.68.117/android_register_login/";


    //Food For You Recycler View
    RecyclerView rv_food_for_you;
    List<ProductModel> food_for_you_list;
    HomeFoodForYouAdapter homeFoodForYouAdapter;

    //Product List Recycler View
    RecyclerView rv_products;
    List<ProductModel> products_list;
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
        store_description = root.findViewById(R.id.tv_store_description);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            stor_id = bundle.getLong("StoreId");
            stor_image = bundle.getInt("StoreImage");
            stor_name = bundle.getString("StoreName");
            stor_address = bundle.getString("StoreAddress");
            stor_category = bundle.getString("StoreCategory");
            stor_description = bundle.getString("StoreDescription");

            store_image.setImageResource(stor_image);
            store_name.setText(stor_name);
            store_address.setText(stor_address);
            store_description.setText(stor_description);
        }

        rv_food_for_you = root.findViewById(R.id.rv_home_food_for_you);
        food_for_you_list = new ArrayList<>();
        rv_food_for_you.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_food_for_you.setHasFixedSize(true);
        rv_food_for_you.setNestedScrollingEnabled(false);
        requestQueueFood = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractFoodforyou();


        final String TAG = "Testing";

        rv_products = root.findViewById(R.id.rv_products);
        products_list = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), products_list);
        rv_products.setAdapter(productAdapter);
        rv_products.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_products.setHasFixedSize(true);
        rv_products.setNestedScrollingEnabled(false);
        requestQueueProd = Singleton.getsInstance(getActivity()).getRequestQueue();
        SendtoDb(stor_id);

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


    public void extractFoodforyou(){
        StoreFragment storeFragment = this;
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

                        ProductModel foodfyModel = new ProductModel(idProduct,store_idStore,productName,productDescription,productPrice,productImage,productServingSize,productTag,productPrepTime,storeName);
                        food_for_you_list.add(foodfyModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    homeFoodForYouAdapter = new HomeFoodForYouAdapter(getActivity(),food_for_you_list,storeFragment);
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

    private void SendtoDb(long stor_id){
        Log.d("SendtoDB: ", "im in");
        JsonArrayRequest jsonArrayRequestProd= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiprod.php", null, new Response.Listener<JSONArray>() {
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

                        if(store_idStore == stor_id){
                            ProductModel productModel = new ProductModel(idProduct,store_idStore,productName,productDescription,productPrice,
                                                            productImage,productServingSize,productTag,productPrepTime,storeName);
                            products_list.add(productModel);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    productAdapter = new ProductAdapter(getActivity(),products_list);
                    rv_products.setAdapter(productAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error in SendtoDB: ", String.valueOf(error));
            }
        });
        requestQueueProd.add(jsonArrayRequestProd);
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
        bundle.putString("Image", food_for_you_list.get(position).getProductImage());
        bundle.putString("Name", food_for_you_list.get(position).getProductName());
        bundle.putString("Description", food_for_you_list.get(position).getProductDescription());
        bundle.putString("StoreName", String.valueOf(food_for_you_list.get(position).getStore_idStore()));
        bundle.putFloat("Price", food_for_you_list.get(position).getProductPrice());

        ProductFragment productFragment = new ProductFragment();
        productFragment.setArguments(bundle);Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,productFragment).commit();

    }


    @Override
    public void onItemClickStorePopular(int position) {

    }

    @Override
    public void onItemClick(int position) {

    }
}