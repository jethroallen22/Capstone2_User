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
import com.example.myapplication.ui.order.OrderFragment;
import com.example.myapplication.ui.ordersummary.OrderSummaryFragment;

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
    List<OrderModel> order_list;
    List<OrderItemModel> order_item_list;
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
        order_list = new ArrayList<>();
        order_item_list = new ArrayList<>();

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
                        Log.d("extractOrder", String.valueOf(jsonObject7.getInt("idOrder")));
                        int idOrder = jsonObject7.getInt("idOrder");
                        float orderItemTotalPrice = (float) jsonObject7.getDouble("orderItemTotalPrice");
                        String orderStatus = jsonObject7.getString("orderStatus");
                        int store_idStore = jsonObject7.getInt("store_idStore");
                        int users_id = jsonObject7.getInt("users_id");
                        String storeName = jsonObject7.getString("storeName");

                        if (jsonObject7.getInt("users_id") == userId){
                            //Extract OrderItems DB
                            JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderitemget.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    List<OrderItemModel> temp_order_item;
                                    temp_order_item = new ArrayList<>();
                                    Log.d("extractOrderItemResponse", String.valueOf(response));
                                    for (int i=0; i < response.length(); i++){
                                        try {
                                            JSONObject jsonObject8 = response.getJSONObject(i);
                                            int idProduct2 = jsonObject8.getInt("idProduct");
                                            int idStore2 = jsonObject8.getInt("idStore");
                                            int idUser2 = jsonObject8.getInt("idUser");
                                            int idOrder2 = jsonObject8.getInt("idOrder");
                                            String productName2 = jsonObject8.getString("productName");
                                            int itemPrice2 = jsonObject8.getInt("itemPrice");
                                            int itemQuantity2 = jsonObject8.getInt("itemQuantity");
                                            float totalPrice2 = (float) jsonObject8.getDouble("totalPrice");
                                            Log.d("OrderID" , String.valueOf(idOrder));
                                            Log.d("OrderItemOrderID" , String.valueOf(jsonObject8.getInt("idOrder")));
                                            if (jsonObject8.getInt("idOrder") == idOrder){
                                                Log.d("OrderItemMatch", "Match!");
                                                temp_order_item.add(new OrderItemModel(idProduct2, idStore2, idUser2, idOrder2, productName2, itemPrice2, itemQuantity2, totalPrice2));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("Error", String.valueOf(e));
                                        }
                                    }
                                    Log.d("tempOrderItem", String.valueOf(temp_order_item.size()));
                                    order_item_list = temp_order_item;
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error", String.valueOf(error));
                                }
                            });

                            requestQueue.add(jsonArrayRequest);
                            Log.d("orderItemList", String.valueOf(order_item_list.size()));
                            order_list.add(new OrderModel(idOrder, orderItemTotalPrice, orderStatus, store_idStore, users_id, storeName, order_item_list));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.d("orderListSize", String.valueOf(order_list.size()));
                activityAdapter = new ActivityAdapter(getActivity(),order_list, ActivitiesFragment.this);
                rv_activity.setAdapter(activityAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_activity.setLayoutManager(layoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue3.add(jsonArrayRequest7);





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
        Log.d("order", String.valueOf(order_list.get(position).getOrderItem_list().size()));
        bundle.putParcelable("orderActivity", order_list.get(position));

        OrderSummaryFragment fragment = new OrderSummaryFragment();
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
    public void onItemClickCategory(int pos) {

    }
}