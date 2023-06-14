package com.example.myapplication.ui.store;

import static androidx.navigation.Navigation.findNavController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activities.Home;
import com.example.myapplication.activities.Login;
import com.example.myapplication.activities.Register;
import com.example.myapplication.activities.Store;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.adapters.ProductCategAdapter;
import com.example.myapplication.databinding.FragmentStoreBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.DealsModel;
import com.example.myapplication.models.HomeFoodForYouModel;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductCategModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.product.ProductFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreFragment extends Fragment implements RecyclerViewInterface {

    private FragmentStoreBinding binding;
    private RequestQueue requestQueueFood,requestQueueProd,requestQueueTempCart;

    ImageView store_image;
    TextView store_name;
    TextView store_address;
    TextView store_description;

    public long stor_id;
    public String stor_image;
    public String stor_name;
    public String stor_address;
    public String stor_category;
    public String stor_description;

    Bundle bundle;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;


    List<OrderItemModel> order_item_temp_list;
    List<OrderModel> order_temp_list;
    List<ProductCategModel> product_categ_list;

    //Food For You Recycler View
    RecyclerView rv_food_for_you;
    List<ProductModel> food_for_you_list, temp_product_list;
    HomeFoodForYouAdapter homeFoodForYouAdapter;
    ProductCategAdapter productCategAdapter;

    //For Product Bottomsheet
    LinearLayout linearLayout;
    TextView product_name,product_resto,product_price,product_description,tv_counter;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;

    //Product List Recycler View
    RecyclerView rv_products;
    List<ProductModel> products_list;
    ProductAdapter productAdapter;

    int userId = 0;
    String userName = "";
    Context context;
    int categPos;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        store_image = root.findViewById(R.id.iv_store_image);
        store_name = root.findViewById(R.id.tv_store_name_main);
        store_address = root.findViewById(R.id.tv_store_address);
        store_description = root.findViewById(R.id.tv_store_description);

        bundle = this.getArguments();
//        Log.d("Result Store: " , bundle.getParcelable("StoreClass"));
        if(bundle != null){
            if (bundle.getParcelable("StoreClass") != null){
                StoreModel storeModel = (StoreModel) (bundle.getParcelable("StoreClass"));
                stor_id = storeModel.getStore_id();
                stor_image = storeModel.getStore_image();
                stor_name = storeModel.getStore_name();
                stor_address = storeModel.getStore_location();
                stor_category = storeModel.getStore_category();
                stor_description = storeModel.getStore_description();
                userId = bundle.getInt("user");

                store_image.setImageBitmap(storeModel.getBitmapImage());
                store_name.setText(stor_name);
                store_address.setText(stor_address);
                store_description.setText(stor_description);

            }
        }

        order_item_temp_list = new ArrayList<>();
        order_temp_list = new ArrayList<>();
        product_categ_list = new ArrayList<>();

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
//        productAdapter = new ProductAdapter(getActivity(), products_list, StoreFragment.this);
//        rv_products.setAdapter(productAdapter);

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

        binding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("ON CLICK", "SUCCESS");
//                Bundle bundle2 = new Bundle();
//                CartFragment fragment2 = new CartFragment();
//                bundle2.putSerializable("tempOrderList", (Serializable) order_temp_list);
//                fragment2.setArguments(bundle2);
//                Log.d("Bundling tempOrderItemList", String.valueOf(bundle2.size()));
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment2).commit();
//                Log.d("END" , "END");

                Bundle bundle = new Bundle();
                CartFragment fragment = new CartFragment();

                bundle.putInt("userID", userId);
                fragment.setArguments(bundle);
                Log.d("Bundling tempOrderItemList", String.valueOf(bundle.size()));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
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
                        String weather = jsonObjectFoodforyou.getString("weather");

                        if(idStore == stor_id) {
                            ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage, productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
                            food_for_you_list.add(foodfyModel);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if  (bundle.getParcelable("deals") != null){
                    DealsModel dealsModel = bundle.getParcelable("deals");
                    for (int i = 0;i < food_for_you_list.size();i++){
                        food_for_you_list.get(i).setProductPrice((food_for_you_list.get(i).getProductPrice() * (100 - dealsModel.getPercentage())) /100);
                    }
                }
                homeFoodForYouAdapter = new HomeFoodForYouAdapter(getActivity(),food_for_you_list,StoreFragment.this);
                rv_food_for_you.setAdapter(homeFoodForYouAdapter);
                int temp_not = 0;
                for(int j = 0 ; j < food_for_you_list.size() ; j++){
                    if(product_categ_list.isEmpty()){
                        //Log.d("STOREMATCH", c_productName + " Empty " + c_storeName);
                        temp_product_list = new ArrayList<>();
                        temp_product_list.add(food_for_you_list.get(j));
                        product_categ_list.add(new ProductCategModel(food_for_you_list.get(j).getProductTag(), temp_product_list));
                    }else{
                        for (int h = 0; h < product_categ_list.size(); h++) {
                            //Log.d("Inside for", String.valueOf(order_list.size()));
                            //Check if Order already exist in CartList
                            if (product_categ_list.get(h).getCateg().toLowerCase().compareTo(food_for_you_list.get(j).getProductTag()) == 0) {
                                // Check if order item already exist
                                product_categ_list.get(h).getList().add(food_for_you_list.get(j));
                            } else {// if(order_list.get(h).getStore_name().toLowerCase().trim().compareTo(c_storeName.toLowerCase().trim()) == 1){
                                Log.d("NEWORDER", "INSIDE NOT MATCH");
                                temp_not++;
                                if(temp_not == product_categ_list.size()) {
                                    //Log.d("NEWORDER", String.valueOf(order_list.size()));
                                    float totalTotal = 0;
                                    temp_product_list = new ArrayList<>();
                                    temp_product_list.add(food_for_you_list.get(j));
                                    product_categ_list.add(new ProductCategModel(food_for_you_list.get(j).getProductTag(), temp_product_list));
                                    break;
                                }
                            }
                        }
                    }
                }
                Log.d("CategSize", String.valueOf(product_categ_list.size()));
                productCategAdapter = new ProductCategAdapter(getActivity(), product_categ_list, StoreFragment.this);
                rv_products.setAdapter(productCategAdapter);
                rv_products.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rv_products.setHasFixedSize(true);
                rv_products.setNestedScrollingEnabled(false);
                Log.d("productCategSize", String.valueOf(product_categ_list.size()));
                for(int i = 0 ; i < product_categ_list.size() ; i++){
                    Log.d("categ", product_categ_list.get(i).getCateg());
                    for(int j = 0 ; j < product_categ_list.get(i).getList().size() ; j++)
                        Log.d("product", product_categ_list.get(i).getList().get(j).getProductName());
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
                        String weather = jsonObjectFoodforyou.getString("weather");

                        Log.d("storeid", String.valueOf(stor_id));
                        if(idStore == stor_id){
                            Log.d("storeid", String.valueOf(idStore));
                            ProductModel productModel = new ProductModel(idProduct,idStore,productName,productDescription,productPrice,
                                                            productImage,productServingSize,productTag,productPrepTime,storeName,storeImage, weather);
                            products_list.add(productModel);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    productAdapter = new ProductAdapter(getActivity(),products_list, StoreFragment.this);
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
        showBottomSheet(position);
    }


    @Override
    public void onItemClickStorePopular(int position) {

    }

    @Override
    public void onItemClick(int position) {
        categPos = position;
        Log.d("clickONITEM:", String.valueOf(categPos));
    }

    @Override
    public void onItemClickStoreRec(int position) {

    }

    @Override
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickCategory(int position) {
        showBottomSheetCateg(position);
    }

    @Override
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickVoucher(int pos) {

    }

    @Override
    public void onItemClickSearch(int pos)


    {

    }

    @Override
    public void onItemClickWeather(int position) {

    }

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
        product_image.setImageBitmap(food_for_you_list.get(position).getBitmapImage());
        product_name.setText(food_for_you_list.get(position).getProductName());
        product_resto.setText(food_for_you_list.get(position).getProductRestoName());
        product_description.setText(food_for_you_list.get(position).getProductDescription());
        product_price.setText("P"+food_for_you_list.get(position).getProductPrice());

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+"tempCart.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("1 ", result );
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Log.d("TEMP CART INSERT", "success");
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Not Inserted",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("TEMP CART", "catch" );
                            Toast.makeText(getActivity().getApplicationContext(), "Catch ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
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

    public void showBottomSheetCateg(int position){
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
//        for (int i = 0 ; i < product_categ_list.size() ; i++){
////            for(int j = 0 ; j < product_categ_list.get(i).getList().size() ; j++){
////                if(product_categ_list.get(i).getCateg().equals( product_categ_list.get(i).getList().get(j).getProductTag())){
////                    categPos = i;
////                }
////            }
//            if(product_categ_list.get(i).getCateg().equals( product_categ_list.get(i).getList().get(position).getProductTag()))
//                    categPos = i;
//        }

        Log.d("clickClick", String.valueOf(categPos));
        //product_image.setImageResource(food_for_you_list.get(position).getProductImage());
        product_image.setImageBitmap(product_categ_list.get(categPos).getList().get(position).getBitmapImage());
        product_name.setText(product_categ_list.get(categPos).getList().get(position).getProductName());
        product_resto.setText(product_categ_list.get(categPos).getList().get(position).getProductRestoName());
        product_description.setText(product_categ_list.get(categPos).getList().get(position).getProductDescription());
        product_price.setText("P"+product_categ_list.get(categPos).getList().get(position).getProductPrice());

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+"tempCart.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("1 ", result );
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Log.d("TEMP CART INSERT", "success");
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Not Inserted",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("TEMP CART", "catch" );
                            Toast.makeText(getActivity().getApplicationContext(), "Catch ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("temp_productId", String.valueOf(product_categ_list.get(categPos).getList().get(position).getIdProduct()));
                        params.put("temp_storeId", String.valueOf(product_categ_list.get(categPos).getList().get(position).getStore_idStore()));
                        params.put("temp_usersId", String.valueOf(userId));
                        params.put("temp_productName", product_categ_list.get(categPos).getList().get(position).getProductName());
                        params.put("temp_productPrice", String.valueOf(product_categ_list.get(categPos).getList().get(position).getProductPrice()));
                        params.put("temp_productQuantity", String.valueOf(product_count));
                        params.put("temp_totalProductPrice", String.valueOf(product_count * product_categ_list.get(categPos).getList().get(position).getProductPrice()));
                        product_count = 0;
                        return params;
                    }

                };
                RequestQueue requestQueueTempCart = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueueTempCart.add(stringRequest);
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