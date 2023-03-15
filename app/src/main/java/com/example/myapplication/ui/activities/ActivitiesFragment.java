package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivityAdapter;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.databinding.FragmentActivitiesBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.ActivityModel;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivitiesFragment extends Fragment implements RecyclerViewInterface {

    private FragmentActivitiesBinding binding;
    private static String JSON_URL;
    private IPModel ipModel;
    int userId;
    List<OrderItemModel> temp_order_item;
    List<OrderModel> order_list;
    //Activity List Recycler View
    RecyclerView rv_activity;
    ActivityAdapter activityAdapter;
    RequestQueue requestQueue3, requestQueue;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("name") != null) {
            userId = intent.getIntExtra("id", 0);
            Log.d("HOMEuserID", String.valueOf(userId));
        } else {
            Log.d("HOME FRAG name", "FAIL");
        }
        temp_order_item = new ArrayList<>();
        order_list = new ArrayList<>();

        rv_activity = root.findViewById(R.id.rv_activities);
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue3 = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractOrders();

        return root;
    }

    //Order DB
    public void extractOrders(){
        JsonArrayRequest jsonArrayRequest7= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject7 = response.getJSONObject(i);
                        if (jsonObject7.getInt("users_id") == userId){
                            Log.d("extractOrder", String.valueOf(jsonObject7.getInt("idOrder")));
                            int idOrder = jsonObject7.getInt("idOrder");
                            float orderItemTotalPrice = (float) jsonObject7.getDouble("orderItemTotalPrice");
                            String orderStatus = jsonObject7.getString("orderStatus");
                            int store_idStore = jsonObject7.getInt("store_idStore");
                            int users_id = jsonObject7.getInt("users_id");

                            //Extract OrderItems DB
                            JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderitemget.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d("extractOrderItemResponse", String.valueOf(response));
                                    for (int i=0; i < response.length(); i++){
                                        try {
                                            JSONObject jsonObject8 = response.getJSONObject(i);
                                            Log.d("OrderID" , String.valueOf(idOrder));
                                            Log.d("OrderItemOrderID" , String.valueOf(jsonObject8.getInt("idOrder")));
                                            if (jsonObject8.getInt("idOrder") == idOrder){
                                                Log.d("extractOrderItem", String.valueOf(jsonObject8.getInt("idOrder")));
                                                int idProduct = jsonObject8.getInt("idProduct");
                                                int idStore = jsonObject8.getInt("idStore");
                                                int idUser = jsonObject8.getInt("idUser");
                                                int idOrder = jsonObject8.getInt("idOrder");
                                                String productName = jsonObject8.getString("productName");
                                                int itemPrice = jsonObject8.getInt("itemPrice");
                                                int itemQuantity = jsonObject8.getInt("itemQuantity");
                                                float totalPrice = (float) jsonObject8.getDouble("totalPrice");

                                                temp_order_item.add(new OrderItemModel(idProduct, idStore, idUser, idOrder, productName, itemPrice, itemQuantity, totalPrice));

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("Error", String.valueOf(e));
                                        }
                                    }
                                    Log.d("tempOrderItem", String.valueOf(temp_order_item.size()));

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error", String.valueOf(error));
                                }
                            });

                            order_list.add(new OrderModel(idOrder, orderItemTotalPrice, orderStatus, store_idStore, users_id, temp_order_item));
                            requestQueue.add(jsonArrayRequest);
                        }

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
        requestQueue3.add(jsonArrayRequest7);

        activityAdapter = new ActivityAdapter(getActivity(),order_list, ActivitiesFragment.this);
        rv_activity.setAdapter(activityAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_activity.setLayoutManager(layoutManager);



    }
//    public void extractOrders(){
//        Log.d("extractOrders", "Called");
//        JsonArrayRequest jsonArrayRequestRec3 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "testAll.php", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.e("Bug", String.valueOf(response));
//                Log.d("Response Order: ", String.valueOf(response.length()));
//                for (int i=0; i < response.length(); i++){
//                    try {
//                        Log.d("Try O: ", "Im in");
//                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
//
//                        //USERS DB
//                        String name = jsonObjectRec1.getString("name");
//
//                        //ORDER DB
//                        int idOrder = jsonObjectRec1.getInt("idOrder");
//                        int orderItemTotalPrice = jsonObjectRec1.getInt("orderItemTotalPrice");
//                        String orderStatus = jsonObjectRec1.getString("orderStatus");
//                        int store_idstore = jsonObjectRec1.getInt("store_idstore");
//
//                        //OrderItem DB
//                        int idItem = jsonObjectRec1.getInt("idItem");
//                        Log.d("Test", String.valueOf(idItem));
//                        int product_idProduct = jsonObjectRec1.getInt("product_idProduct");
//                        int order_idOrder = jsonObjectRec1.getInt("order_idOrder");
//                        float ItemPrice = (float) jsonObjectRec1.getDouble("ItemPrice");
//                        int ItemQuantity = jsonObjectRec1.getInt("ItemQuantity");
//
//                        //PRODUCT DB
//                        String productName = jsonObjectRec1.getString("productName");
//
//                        OrderItemModel orderItemModel1 = new OrderItemModel(product_idProduct, store_idstore, userId, order_idOrder, productName, itemPrice, itemQuantity, totalPrice);
//                        order_item_list.add(orderItemModel);
//                        Log.d("ORDER ITEM LIST: ", String.valueOf(order_item_list.get(i).getIdItem()));
//                        temp_order_item.add(order_item_list.get(i));
//
//                        OrderModel orderModel = new OrderModel(idOrder, orderItemTotalPrice, orderStatus, store_idstore, name, temp_order_item);
//                        temp_order.add(orderModel);
//
//                        if((idOrder != temp_idOrder && idOrder != 0) || response.length() == 1 || i == response.length()-1){
//                            Log.d("Inside If", String.valueOf(order_item_list.size()));
//                            temp_order_item = new ArrayList<>();
//                            for(int j=0; j < order_item_list.size(); j++){
//                                Log.d("Inside For", String.valueOf(order_item_list.size()));
//                                if((temp_idOrder == order_item_list.get(j).getOrder_idOrder()) || (response.length() == 1)){
//                                    Log.d("Inside If", String.valueOf(order_item_list.size()));
//                                    temp_order_item.add(order_item_list.get(j));
//                                    Log.d("TEMP LIST: ", String.valueOf(i));
//                                }
//                            }
//
////                            Log.d("TEMP LIST: ", temp_order_item.get(i).getProductName());
//                            if(i != 0){
//                                OrderModel orderModel2 = new OrderModel(temp_order.get(i-1).getIdOrder(), temp_order.get(i-1).getOrderItemTotalPrice(), temp_order.get(i-1).getOrderStatus(), temp_order.get(i-1).getStore_idstore(), temp_order.get(i-1).getUsers_name(), temp_order_item);
//                                order_list.add(orderModel2);
//                            }
//                            if(response.length()==1 || i == response.length()-1){
//                                if (i == response.length()-1 && temp_idOrder != idOrder){
//                                    OrderModel orderModel2 = new OrderModel(temp_order.get(i).getIdOrder(), temp_order.get(i).getOrderItemTotalPrice(), temp_order.get(i).getOrderStatus(), temp_order.get(i).getStore_idstore(), temp_order.get(i).getUsers_name(), Collections.singletonList(order_item_list.get(i)));
//                                    order_list.add(orderModel2);
//                                }else{
//                                    OrderModel orderModel2 = new OrderModel(temp_order.get(i).getIdOrder(), temp_order.get(i).getOrderItemTotalPrice(), temp_order.get(i).getOrderStatus(), temp_order.get(i).getStore_idstore(), temp_order.get(i).getUsers_name(), temp_order_item);
//                                    order_list.add(orderModel2);
//                                }
//
//                            }
//                            Log.d("ORDER LIST: ", "Just added #" + i);
////                            if(i!=0){
////                                Log.d("ORDER MODEL: ", String.valueOf(order_list.get(i).getIdOrder()));
////                            }
//
//                        }
//                        temp_idOrder = idOrder;
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("Order Size" , String.valueOf(temp_order.size()));
//                    if(i != 0){
//                        activityAdapter = new ActivityAdapter(getActivity(), order_list, ActivitiesFragment.this);
//                        rv_activity.setAdapter(activityAdapter);
//                    }
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("OnError O: ", String.valueOf(error));
//            }
//        });
//        requestQueue3.add(jsonArrayRequestRec3);
//    }

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
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClickStoreRec(int position) {

    }

    @Override
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickSearch(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {

    }
}