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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.example.myapplication.ui.order.OrderFragment;
import com.example.myapplication.ui.ordersummary.OrderSummaryActivityFragment;
import com.example.myapplication.ui.ordersummary.OrderSummaryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitiesFragment extends Fragment implements RecyclerViewInterface {

    private FragmentActivitiesBinding binding;
    private static String JSON_URL;
    private IPModel ipModel;
    int userId;
    List<OrderModel> orderModelList;
    List<OrderItemModel> order_item_list;
    //Activity List Recycler View
    RecyclerView rv_activity;
    ActivityAdapter activityAdapter;
    RequestQueue outerRequest, innerRequest;


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
        order_item_list = new ArrayList<>();
        final List<OrderModel> order_list = new ArrayList<>();
        rv_activity = root.findViewById(R.id.rv_activities);
        outerRequest = Singleton.getsInstance(getActivity()).getRequestQueue();
        innerRequest = Singleton.getsInstance(getActivity()).getRequestQueue();
        //extractOrders();

        JsonArrayRequest jsonArrayRequest7= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i=0; i < response.length(); i++) {
                        JSONObject jsonObject7 = response.getJSONObject(i);
                        Log.d("extractOrder", String.valueOf(jsonObject7.getInt("idOrder")));
                        int idOrder = jsonObject7.getInt("idOrder");
                        float orderItemTotalPrice = (float) jsonObject7.getDouble("orderItemTotalPrice");
                        String orderStatus = jsonObject7.getString("orderStatus");
                        int store_idStore = jsonObject7.getInt("store_idStore");
                        int users_id = jsonObject7.getInt("users_id");
                        String storeName = jsonObject7.getString("storeName");

                        if (jsonObject7.getInt("users_id") == userId) {
                            //Extract OrderItems DB
                            final OrderModel tempOrderModel = new OrderModel();
                            tempOrderModel.setIdOrder(idOrder);
                            tempOrderModel.setOrderItemTotalPrice(orderItemTotalPrice);
                            tempOrderModel.setOrderStatus(orderStatus);
                            tempOrderModel.setStore_idstore(store_idStore);
                            tempOrderModel.setUsers_id(users_id);
                            tempOrderModel.setStore_name(storeName);

                            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiorderitemget.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    List<OrderItemModel> tempOrderList2 = new ArrayList<>();
                                    Log.d("extractOrderItemResponse", String.valueOf(response));
                                    try {
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject jsonObject8 = response.getJSONObject(i);
                                            Log.d("OrderID", String.valueOf(idOrder));
                                            Log.d("OrderItemOrderID", String.valueOf(jsonObject8.getInt("idOrder")));
                                            if (jsonObject8.getInt("idOrder") == idOrder && jsonObject8.getString("itemStatus") == "ordered") {
                                                int idProduct2 = jsonObject8.getInt("idProduct");
                                                int idStore2 = jsonObject8.getInt("idStore");
                                                int idUser2 = jsonObject8.getInt("idUser");
                                                int idOrder2 = jsonObject8.getInt("idOrder");
                                                String productName2 = jsonObject8.getString("productName");
                                                int itemPrice2 = jsonObject8.getInt("itemPrice");
                                                int itemQuantity2 = jsonObject8.getInt("itemQuantity");
                                                float totalPrice2 = (float) jsonObject8.getDouble("totalPrice");
                                                OrderItemModel tempOrderItem;
                                                Log.d("OrderItemMatch", "Match!");
                                                tempOrderItem = new OrderItemModel(idProduct2, idStore2, idUser2, idOrder2, productName2, itemPrice2, itemQuantity2, totalPrice2);
                                                tempOrderList2.add(tempOrderItem);
                                            }
                                        }
                                        tempOrderModel.setOrderItem_list(tempOrderList2);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("Error", String.valueOf(e));
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error", String.valueOf(error));
                                }
                            });

                            innerRequest.add(jsonArrayRequest);
                            order_list.add(tempOrderModel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                orderModelList = order_list;
                activityAdapter = new ActivityAdapter(getActivity(),orderModelList, ActivitiesFragment.this);
                rv_activity.setAdapter(activityAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_activity.setLayoutManager(layoutManager);


                activityAdapter.setOnItemClickListener(new ActivityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("order", orderModelList.get(position));
                        OrderFragment fragment = new OrderFragment();
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        outerRequest.add(jsonArrayRequest7);

        return root;
    }

    //Order DB
//    public void extractOrders(){
//
//
//        JsonArrayRequest jsonArrayRequest7= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderget.php", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    for (int i=0; i < response.length(); i++) {
//                        JSONObject jsonObject7 = response.getJSONObject(i);
//                        Log.d("extractOrder", String.valueOf(jsonObject7.getInt("idOrder")));
//                        int idOrder = jsonObject7.getInt("idOrder");
//                        float orderItemTotalPrice = (float) jsonObject7.getDouble("orderItemTotalPrice");
//                        String orderStatus = jsonObject7.getString("orderStatus");
//                        int store_idStore = jsonObject7.getInt("store_idStore");
//                        int users_id = jsonObject7.getInt("users_id");
//                        String storeName = jsonObject7.getString("storeName");
//
//                        if (jsonObject7.getInt("users_id") == userId) {
//                            //Extract OrderItems DB
//                            final OrderModel tempOrderModel = new OrderModel();
//                            tempOrderModel.setIdOrder(idOrder);
//                            tempOrderModel.setOrderItemTotalPrice(orderItemTotalPrice);
//                            tempOrderModel.setOrderStatus(orderStatus);
//                            tempOrderModel.setStore_idstore(store_idStore);
//                            tempOrderModel.setUsers_id(users_id);
//                            tempOrderModel.setStore_name(storeName);
//
//                            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiorderitemget.php", null, new Response.Listener<JSONArray>() {
//                                @Override
//                                public void onResponse(JSONArray response) {
//                                    List<OrderItemModel> tempOrderList2 = new ArrayList<>();
//                                    Log.d("extractOrderItemResponse", String.valueOf(response));
//                                    try {
//                                        for (int i = 0; i < response.length(); i++) {
//                                            JSONObject jsonObject8 = response.getJSONObject(i);
//                                            Log.d("OrderID", String.valueOf(idOrder));
//                                            Log.d("OrderItemOrderID", String.valueOf(jsonObject8.getInt("idOrder")));
//                                            if (jsonObject8.getInt("idOrder") == idOrder) {
//                                                int idProduct2 = jsonObject8.getInt("idProduct");
//                                                int idStore2 = jsonObject8.getInt("idStore");
//                                                int idUser2 = jsonObject8.getInt("idUser");
//                                                int idOrder2 = jsonObject8.getInt("idOrder");
//                                                String productName2 = jsonObject8.getString("productName");
//                                                int itemPrice2 = jsonObject8.getInt("itemPrice");
//                                                int itemQuantity2 = jsonObject8.getInt("itemQuantity");
//                                                float totalPrice2 = (float) jsonObject8.getDouble("totalPrice");
//                                                OrderItemModel tempOrderItem;
//                                                Log.d("OrderItemMatch", "Match!");
//                                                tempOrderItem = new OrderItemModel(idProduct2, idStore2, idUser2, idOrder2, productName2, itemPrice2, itemQuantity2, totalPrice2);
//                                                tempOrderList2.add(tempOrderItem);
//                                            }
//                                        }
//                                        tempOrderModel.setOrderItem_list(tempOrderList2);
//                                        order_list.add(tempOrderModel);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                        Log.d("Error", String.valueOf(e));
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.d("Error", String.valueOf(error));
//                                }
//                            });
//
//                            innerRequest.add(jsonArrayRequest);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                activityAdapter = new ActivityAdapter(getActivity(),order_list, ActivitiesFragment.this);
//                rv_activity.setAdapter(activityAdapter);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//                rv_activity.setLayoutManager(layoutManager);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        outerRequest.add(jsonArrayRequest7);
//
//
//
//
//
//    }

    public void deleteProduct(int position){
        //Log.d("Delete Product", order_list.get(position).getProductName());

//        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "deleteProd.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String result) {
//                        Log.d("On Res", "inside on res");
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Volley Error", String.valueOf(error));
//            }
//        }){
//            protected Map<String, String> getParams(){
//                Map<String, String> paramV = new HashMap<>();
//                paramV.put("idProduct", String.valueOf(product_list.get(position).getIdProduct()));
//                Log.d("PARAM", product_list.get(position).getProductName());
//                return paramV;
//            }
//        };
//
//        queue.add(stringRequest);
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
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        Log.d("order", String.valueOf(orderModelList.get(position).getOrderItem_list().size()));
        bundle.putParcelable("orderActivity", orderModelList.get(position));

        OrderSummaryActivityFragment fragment = new OrderSummaryActivityFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
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
    public void onItemClickWeather(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {

    }
}