package com.example.myapplication.ui.cart;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.activities.Store;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.CartModel;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.order.OrderFragment;
import com.example.myapplication.ui.search.SearchFragment;
import com.example.myapplication.ui.store.StoreFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;


public class CartFragment extends Fragment implements RecyclerViewInterface {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;

    //Cart List Recycler View
    RecyclerView rv_cart;
    List<CartModel> cart_list;
    CartAdapter cartAdapter;

    Button btn_remove;
    CheckBox cb_cart_item;
    CheckBox checkBox;

    List<OrderModel> temp_order_list;
    List<StoreModel> temp_store_list;
    List<OrderItemModel> cart_item_list;

    HomeStorePopularAdapter temp_store_adapter;
    RequestQueue requestQueue;
    RequestQueue requestQueueCart;

    List<OrderItemModel> order_item_temp_list;
    List<OrderModel> order_temp_list;
    int temp_count;
    float tempPrice;
    int product_count;

    int userID = 0;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if(bundle != null) {
            userID = bundle.getInt("userID");
            Log.d("USERID", String.valueOf(userID));
        }

        temp_store_list = new ArrayList<>();
        order_item_temp_list = new ArrayList<>();
        order_temp_list = new ArrayList<>();
        cart_item_list = new ArrayList<>();


        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
//        extractStore();

        Log.d("STORESIZELINE112", String.valueOf(temp_store_list.size()));

        rv_cart = root.findViewById(R.id.rv_cart);
        cart_list = new ArrayList<>();
        requestQueueCart = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractStoreCartItem();
        Log.d("SIZE", String.valueOf(order_item_temp_list.size()));
//
//
//        cart_list = new ArrayList<>();
//        cart_list.add(new CartModel(R.drawable.burger_mcdo,"McDonalds - Binondo", 3, "45", "3.5"));
//        cart_list.add(new CartModel(R.drawable.burger_mcdo,"McDonalds - Abad Santos", 5, "30", "1.5"));

//        for(int i = 0;i < temp_order_list.size(); i++){
//            CartModel cartModel = new CartModel(temp_order_list);
//            cart_list.add(cartModel);
//        }



        btn_remove = root.findViewById(R.id.btn_remove);
        cb_cart_item = root.findViewById(R.id.cb_cart_item);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){

                }
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

    }

    @Override
    public void onItemClickStorePopular(int position) {

    }

    @Override
    public void onItemClickStoreRec(int position) {

    }

    @Override
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickCategory(int position) {

    }

    @Override
    public void onItemClickSearch(int pos) {

    }



    @Override
    public void onItemClick(int position) {
        Log.d("TAG", "Success");
        Bundle bundle = new Bundle();
        //bundle.putString("StoreName", cart_list.get(position).getStore_name());

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(bundle);
        //bundle.putSerializable("order_item_list", (Serializable) cart_list.get(position).getOrderItem_list());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
        Log.d("Test","Test success");
    }

    public void extractStore(){
        List<StoreModel> storeModelList;
        storeModelList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apipopu.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectPop = response.getJSONObject(i);
                        long r_id = jsonObjectPop.getLong("idStore");
                        String r_image = jsonObjectPop.getString("storeImage");
                        String r_name = jsonObjectPop.getString("storeName");
                        String r_description = jsonObjectPop.getString("storeDescription");
                        String r_location = jsonObjectPop.getString("storeLocation");
                        String r_category = jsonObjectPop.getString("storeCategory");
                        float r_rating = (float) jsonObjectPop.getDouble("storeRating");
                        int r_popularity = jsonObjectPop.getInt("storePopularity");
                        String r_open = jsonObjectPop.getString("storeStartTime");
                        String r_close = jsonObjectPop.getString("storeEndTime");

                        StoreModel store3 = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                (float) r_rating, r_popularity, r_open, r_close);
                        storeModelList.add(store3);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                temp_store_list = storeModelList;
                Log.d("STOREMODELLISTSIZE", String.valueOf(temp_store_list
                        .size()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void extractStoreCartItem(){
        extractStore();
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apicart.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                OrderItemModel orderItemModel;
                Log.d("CARTITEM", response.toString());
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectCart = response.getJSONObject(i);
                        if (jsonObjectCart.getInt("temp_usersId") == userID) {
                            int c_productId = jsonObjectCart.getInt("temp_productId");
                            int c_storeId = jsonObjectCart.getInt("temp_storeId");
                            int c_usersId = jsonObjectCart.getInt("temp_usersId");
                            String c_productName = jsonObjectCart.getString("temp_productName");
                            double c_productPrice = jsonObjectCart.getDouble("temp_productPrice");
                            int c_productQuantity = jsonObjectCart.getInt("temp_productQuantity");
                            double c_totalProductPrice = jsonObjectCart.getDouble("temp_totalProductPrice");
                            String c_storeName = jsonObjectCart.getString("storeName");

                            orderItemModel = new OrderItemModel(c_productId, c_storeId, (float) c_totalProductPrice, c_productQuantity, c_productName);
                            order_item_temp_list.add(orderItemModel);
                        }
//
                        OrderModel orderModel = new OrderModel();
                        orderModel.setOrderItem_list(order_item_temp_list);
                        order_temp_list.add(orderModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    cartAdapter = new CartAdapter(getActivity(),order_temp_list, CartFragment.this);
                    rv_cart.setAdapter(cartAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rv_cart.setLayoutManager(layoutManager);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //Log.d("SIZER.", order_item_temp_list.get(0).getProductName());
        //order_item_temp_list = orderItemModelList;

//        for (int i = 0 ; i < order_item_temp_list.size() ; i++) {
//            if (order_temp_list.isEmpty()) {
//                order_temp_list.add(new OrderModel(6, tempPrice, "pending", order_item_temp_list.get(i).getStore_id(),
//                        order_item_temp_list.get(i).getStore_id(), food_for_you_list.get(position).getProductRestoName(),
//                        userId, order_item_temp_list));
//            } else {
//                for (int i = 0; i < order_temp_list.size(); i++) {
//                    //Check if Order already exist in CartList
//                    if (order_temp_list.get(i).getStore_name().compareTo(food_for_you_list.get(position).getProductRestoName()) == 0) {
//                        // Check if order item already exist
//                        for (int k = 0; k < order_temp_list.get(i).getOrderItem_list().size(); k++) {
//                            if (food_for_you_list.get(position).getProductName().compareTo(order_temp_list.get(i).getOrderItem_list().get(k).getProductName()) == 0) {
//                                temp_count = product_count;
//                                int tempItemQuantity = 0;
//                                tempItemQuantity = order_temp_list.get(i).getOrderItem_list().get(k).getItemQuantity();
//                                tempItemQuantity += temp_count;
//                                product_count = 0;
//                                order_temp_list.get(i).getOrderItem_list().get(k).setItemQuantity(tempItemQuantity);
//                                tempItemQuantity = 0;
//                            } else {
//                                temp_count = product_count;
//                                order_temp_list.get(i).getOrderItem_list().add(new OrderItemModel(food_for_you_list.get(position).getIdProduct(), food_for_you_list.get(position).getStore_idStore(),
//                                        food_for_you_list.get(position).getProductPrice() * temp_count, temp_count,
//                                        food_for_you_list.get(position).getProductName()));
//                                product_count = 0;
//                            }
//                        }
//                    } else {
//                        order_item_temp_list = new ArrayList<>();
//                        temp_count = product_count;
//                        order_item_temp_list.add(new OrderItemModel(food_for_you_list.get(position).getIdProduct(), food_for_you_list.get(position).getStore_idStore(),
//                                food_for_you_list.get(position).getProductPrice() * temp_count, temp_count,
//                                food_for_you_list.get(position).getProductName()));
//                        product_count = 1;
//                        for (int j = 0; j < order_item_temp_list.size(); j++) {
//                            tempPrice += order_item_temp_list.get(j).getItemPrice();
//                        }
//                        order_temp_list.add(new OrderModel(6, tempPrice, "preparing", food_for_you_list.get(position).getStore_idStore(),
//                                food_for_you_list.get(position).getProductRestoImage(), food_for_you_list.get(position).getProductRestoName(),
//                                userId, order_item_temp_list));
//                    }
//                }
//            }
//        }
        Log.d("SIZES", String.valueOf(cart_item_list.size()));
        requestQueueCart.add(jsonArrayRequest1);
    }
}