package com.example.myapplication.ui.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.order.OrderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartFragment extends Fragment implements RecyclerViewInterface {

    private FragmentCartBinding binding;
    //Cart List Recycler View
    RecyclerView rv_cart;
    CartAdapter cartAdapter;
    Button btn_remove;
    CheckBox cb_cart_item;
    CheckBox checkBox;
    List<OrderModel> order_list;
    List<StoreModel> temp_store_list;
    List<OrderItemModel> order_item_list;
    RequestQueue requestQueueCart, requestQueueStore;
    int userID = 0;
    float wallet;
    private Handler handler;
    private Runnable myRunnable;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        order_list = new ArrayList<>();
        order_item_list = new ArrayList<>();
        temp_store_list = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle != null) {
            userID = bundle.getInt("userID");
            wallet = bundle.getFloat("wallet");
            Log.d("USERID", String.valueOf(userID));
        }
        rv_cart = root.findViewById(R.id.rv_cart);
        requestQueueCart = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueStore = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractStoreCartItem();
        btn_remove = root.findViewById(R.id.btn_remove);
        checkBox = root.findViewById(R.id.checkBox2);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    //
                    deleteAll();
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
    public void onItemClickSearch(int position, int recyclerViewId) {

    }

    @Override
    public void onItemClickCategory(int position) {

    }

    @Override
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickVoucher(int pos) {

    }

    @Override
    public void onItemClickSearch(int pos) {

    }

    @Override
    public void onItemClickWeather(int position) {

    }


    @Override
    public void onItemClick(int position) {
//        handler.removeCallbacks(myRunnable);
        Bundle bundle = new Bundle();
        bundle.putParcelable("order", order_list.get(position));
        bundle.putFloat("wallet", wallet);
        Log.d("orderItem", "========================");
        Log.d("orderItem", "Size: " + order_list.get(position).getOrderItem_list().size());
        Log.d("orderItem", "Qty: " + order_list.get(position).getOrderItem_list().get(0).getItemQuantity());
        for (OrderModel orderModel : order_list) {
            Log.d("orderItem", "StoreName: " + orderModel.getStore_name());
            for (OrderItemModel orderItemModel : orderModel.getOrderItem_list()) {
                Log.d("orderItem", "name: " + orderItemModel.getProductName());
                Log.d("orderItem", "qty: " + orderItemModel.getItemQuantity());
            }
        }
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }

    public void extractStoreCartItem() {
        order_list.clear();
        order_item_list.clear();
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apicart.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        int temp_not = 0;
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
                            String c_storeImage = jsonObjectCart.getString("storeImage");
                            OrderItemModel orderItemModel = new OrderItemModel(c_productId, c_storeId, userID, c_productName, (float) c_productPrice, c_productQuantity,
                                    (float) (c_productPrice * c_productQuantity));
                            // order_item_list.add(orderItemModel);
                            Log.d("BEFORE", String.valueOf(userID));
                            Log.d("BEFOREDB", String.valueOf(c_usersId));
                            Log.d("OItem", "ItemName: " + c_productName);
                            Log.d("OItem", "QTY: " + c_productQuantity);
                            if (order_list.isEmpty()) {
                                Log.d("STOREMATCH", c_productName + " Empty " + c_storeName);
                                order_item_list = new ArrayList<>();
                                order_item_list.add(orderItemModel);
                                order_list.add(new OrderModel((float) c_totalProductPrice, "pending", c_storeId,
                                        c_storeImage, c_storeName,
                                        c_usersId, order_item_list));
                            } else {
                                for (int h = 0; h < order_list.size(); h++) {
                                    Log.d("Inside for", String.valueOf(order_list.size()));
                                    // Check if Order already exists in CartList
                                    if (order_list.get(h).getStore_name().equalsIgnoreCase(c_storeName)) {
                                        Log.d("STOREMATCH", c_productName + " MATCH " + c_storeName);
                                        // Check if order item already exists
                                        boolean itemFound = false;
                                        for (int k = 0; k < order_list.get(h).getOrderItem_list().size(); k++) {
                                            if (c_productName.equalsIgnoreCase(order_list.get(h).getOrderItem_list().get(k).getProductName())) {
                                                // Update the quantity of the existing item
                                                Log.d("inside storematch", order_list.get(h).getOrderItem_list().get(k).getProductName());
                                                int tempItemQuantity = order_list.get(h).getOrderItem_list().get(k).getItemQuantity();
                                                tempItemQuantity += c_productQuantity;
                                                order_list.get(h).getOrderItem_list().get(k).setItemQuantity(tempItemQuantity);
                                                itemFound = true;
                                                break;
                                            }
                                        }
                                        if (!itemFound) {
                                            // If the item doesn't exist, add it to the order_item_list
                                            order_list.get(h).getOrderItem_list().add(orderItemModel);
                                            Log.d("orderItem !itemfound", "name: " + orderItemModel.getProductName());
                                            Log.d("orderItem !itemfound", "qty: " + orderItemModel.getItemQuantity());
                                        }
                                    } else {
                                        // The store doesn't match, continue with the next store
                                        Log.d("STOREMATCH !0", c_productName + " NOT MATCH " + c_storeName);
                                        temp_not++;
                                        Log.d("Temp_not", String.valueOf(temp_not));
                                        Log.d("orderItem", "========================");
                                        Log.d("orderItem", "qty: " + orderItemModel.getItemQuantity());
                                        if (temp_not == order_list.size()) {
                                            Log.d("OItem", "qty: " + orderItemModel.getItemQuantity());
                                            // Create a new instance of OrderItemModel for each occurrence
                                            order_item_list = new ArrayList<>();
                                            order_item_list.add(orderItemModel);
                                            Log.d("cartqty", "name: " + c_productName);
                                            Log.d("cartqty", "qty1: " + c_productQuantity);
                                            Log.d("cartqty", "qty2: " + orderItemModel.getItemQuantity());
                                            order_list.add(new OrderModel((float) c_totalProductPrice, "pending", c_storeId,
                                                    c_storeImage, c_storeName,
                                                    c_usersId, order_item_list));
                                            Log.d("Added OL", String.valueOf(i));
                                        }
                                    }
                                }
                            }
                        }

                        Log.d("MINE OL SIZE", String.valueOf(order_list.size()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                Log.d("CartSize", String.valueOf(order_list.size()));

                for (OrderModel orderModel : order_list) {
                    float tempTotal = 0;
                    for (OrderItemModel orderItemModel : orderModel.getOrderItem_list()) {
                        tempTotal += orderItemModel.getTotalPrice();
                    }
                    orderModel.setOrderItemTotalPrice(tempTotal);
                }
                cartAdapter = new CartAdapter(getActivity(), order_list, CartFragment.this);
                rv_cart.setAdapter(cartAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_cart.setLayoutManager(layoutManager);
                cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        deleteProduct(position);
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueCart.add(jsonArrayRequest1);
        Log.d("OUTSIDE LIST", String.valueOf(order_list.size()));

    }


    public void deleteProduct(int position) {
        RequestQueue queue = Singleton.getsInstance(getContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "deleteCartAll.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("CartDelete", "inside onResponse");
                        // Item deleted successfully, now remove it from local data and notify the adapter
                        order_list.remove(position);
                        cartAdapter.notifyItemRemoved(position);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", String.valueOf(error));
                // Error occurred, handle it appropriately (e.g., show an error message)
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("idStore", String.valueOf(order_list.get(position).getStore_idstore()));
                paramV.put("idUser", String.valueOf(order_list.get(position).getUsers_id()));
                return paramV;
            }
        };

        queue.add(stringRequest);
    }

    public void deleteAll() {
        RequestQueue queue = Singleton.getsInstance(getContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "deleteAllCartItems.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("CartDelete", "inside onResponse");
                        // Item deleted successfully, now remove it from local data and notify the adapter
                        order_list.clear();
//                        cartAdapter.notifyItemRemoved(position);
                        cartAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", String.valueOf(error));
                // Error occurred, handle it appropriately (e.g., show an error message)
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("idUser", String.valueOf(userID));
                return paramV;
            }
        };

        queue.add(stringRequest);
    }

}