package com.example.myapplication.ui.cart;

import androidx.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.order.OrderFragment;
import com.example.myapplication.ui.search.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CartFragment extends Fragment implements RecyclerViewInterface {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;

    //Cart List Recycler View
    RecyclerView rv_cart;
    //List<OrderItemModel> cart_list;
    CartAdapter cartAdapter;

    Button btn_remove;
    CheckBox cb_cart_item;
    CheckBox checkBox;

    List<OrderModel> order_list;
    List<StoreModel> temp_store_list;
    List<OrderItemModel> order_item_list;

    HomeStorePopularAdapter temp_store_adapter;
    RequestQueue requestQueue;
    RequestQueue requestQueueCart;

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

        // to comment out
//        cart_list = (List<OrderModel>) getArguments().getSerializable("tempOrderList");

//        Log.d("CART FRAG: ", String.valueOf(temp_order_list.size()));

        rv_cart = root.findViewById(R.id.rv_cart);
        order_list = new ArrayList<>();
        order_item_list = new ArrayList<>();
//        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
//        rv_cart.setHasFixedSize(true);
//        rv_cart.setNestedScrollingEnabled(false);
        requestQueueCart = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractStoreCartItem();

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

    public void extractPopular(){
        JsonArrayRequest jsonArrayRequest3 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apipopu.php", null, new Response.Listener<JSONArray>() {
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
                        temp_store_list.add(store3);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest3);
    }

    public void extractStore(){
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
                        temp_store_list.add(store3);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void extractStoreCartItem(){

        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apicart.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                float tempPrice = 0;
                int product_count =0;
                int temp_count = 0;
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectCart = response.getJSONObject(i);
                        if (jsonObjectCart.getInt("temp_usersId") == userID) {
                            Log.d("PRODNAME", jsonObjectCart.getString("temp_productName"));
                            Log.d("CARTITEM", response.toString());
                            int c_productId = jsonObjectCart.getInt("temp_productId");
                            int c_storeId = jsonObjectCart.getInt("temp_storeId");
                            int c_usersId = jsonObjectCart.getInt("temp_usersId");
                            String c_productName = jsonObjectCart.getString("temp_productName");
                            double c_productPrice = jsonObjectCart.getDouble("temp_productPrice");
                            int c_productQuantity = jsonObjectCart.getInt("temp_productQuantity");
                            double c_totalProductPrice = jsonObjectCart.getDouble("temp_totalProductPrice");
                            String c_storeName = jsonObjectCart.getString("storeName");
                            String c_storeImage = jsonObjectCart.getString("storeImage");

                            OrderItemModel orderItemModel = new OrderItemModel(c_productId, c_storeId, (float) (c_productPrice * c_productQuantity), c_productQuantity,
                                    c_productName);

                            Log.d("BEFORE", String.valueOf(userID));
                            Log.d("BEFOREDB", String.valueOf(c_usersId));
                            if(order_list.isEmpty()){
                                Log.d("STOREMATCH", c_productName + " Empty " + c_storeName);
                                order_item_list = new ArrayList<>();
                                order_item_list.add(orderItemModel);
                                order_list.add(new OrderModel((float) c_totalProductPrice,"pending",c_storeId,
                                        c_storeImage,c_storeName,
                                        c_usersId, order_item_list));
                            }else{
                                for (int h = 0; h < order_list.size(); h++) {
                                    Log.d("Inside for", String.valueOf(order_list.size()));
                                    //Check if Order already exist in CartList
                                    if (order_list.get(h).getStore_name().toLowerCase().trim().compareTo(c_storeName.toLowerCase().trim()) == 0) {
                                        Log.d("STOREMATCH", c_productName + " MATCH " + c_storeName);
                                        // Check if order item already exist
                                        for (int k = 0 ; k < order_list.get(h).getOrderItem_list().size() ; k++){
                                            if(c_productName.toLowerCase().trim().compareTo(order_list.get(h).getOrderItem_list().get(k).getProductName().toLowerCase().trim()) == 0){
//                                                temp_count = c_productQuantity;
                                                int tempItemQuantity = 0;
                                                tempItemQuantity = order_list.get(h).getOrderItem_list().get(k).getItemQuantity();
                                                Log.d("TempQty", String.valueOf(tempItemQuantity));
                                                tempItemQuantity += c_productQuantity;
                                                Log.d("TempQty", String.valueOf(tempItemQuantity));
                                                order_list.get(h).getOrderItem_list().get(k).setItemQuantity(tempItemQuantity);
                                                tempItemQuantity = 0;
                                                Log.d("QtyIf", String.valueOf(order_list.size()));
                                                break;
                                            } else{
                                                order_list.get(h).getOrderItem_list().add(orderItemModel);
                                                Log.d("QtyElse", String.valueOf(order_list.size()));
                                                break;
                                            }

                                        }
                                        break;
                                    } else{
                                        Log.d("STOREMATCH !0", c_productName + " NOT MATCH " + c_storeName);
                                        order_item_list = new ArrayList<>();
                                        order_item_list.add(orderItemModel);
                                        order_list.add(new OrderModel((float) c_totalProductPrice,"pending",c_storeId,
                                                c_storeImage,c_storeName,
                                                c_usersId, order_item_list));
                                        Log.d("Added OL", String.valueOf(i));
                                        break;
                                    }
                                }
                            }
                        }
                        Log.d("MINE OL SIZE", String.valueOf(order_list.size()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.d("CARTSIZE", String.valueOf(order_list.size()));
                for (int i = 0 ; i < order_list.size() ; i++){
                    for (int j = 0 ; j < order_list.get(i).getOrderItem_list().size() ; j++)
                        Log.d("CARTSIZE", String.valueOf(order_list.get(i).getOrderItem_list().get(j).getProductName()));
                }
                cartAdapter = new CartAdapter(getActivity(),order_list, CartFragment.this);
                rv_cart.setAdapter(cartAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_cart.setLayoutManager(layoutManager);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        requestQueueCart.add(jsonArrayRequest1);
        Log.d("OUTSIDE LIST", String.valueOf(order_list.size()));

    }
}