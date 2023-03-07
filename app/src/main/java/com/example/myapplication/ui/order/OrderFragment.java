package com.example.myapplication.ui.order;

import androidx.lifecycle.ViewModelProvider;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activities.Home;
import com.example.myapplication.activities.Login;
import com.example.myapplication.activities.Register;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.CartModel;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.checkout.CheckoutFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.store.StoreFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment implements RecyclerViewInterface {

    //
    RecyclerView rv_order_items;
    List<OrderItemModel> order_item_list;
    OrderModel orderModel;
    OrderItemsAdapter orderItemsAdapter;

    private OrderViewModel mViewModel;
    private FragmentOrderBinding binding;

    TextView tv_store_name;
    TextView tv_total_price;
    Button btn_place_order;
    WebView webPay;
    private String store_name;
    private int i;
    private float total_price;
    OrderFragment orderFragment = this;
    //School IP
    private static String JSON_URL;
    private IPModel ipModel;
    int orderID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrderViewModel orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        tv_store_name = root.findViewById(R.id.tv_store_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        btn_place_order = root.findViewById(R.id.btn_place_order);
        webPay = root.findViewById(R.id.webPay);
        Bundle bundle = this.getArguments();

        order_item_list = new ArrayList<>();
        //order_item_list = (List<OrderItemModel>) getArguments().getSerializable("order_item_list");

        if (bundle != null){
            orderModel = bundle.getParcelable("order");
            store_name = orderModel.getStore_name();
            tv_store_name.setText(store_name);
            Log.d("StoreName",store_name);
        }

        rv_order_items = root.findViewById(R.id.rv_order_items);
//        order_item_list = new ArrayList<>();
//        order_item_list.add(new OrderItemModel("Burger Mcdo", 3, 135F));
//        order_item_list.add(new OrderItemModel("Chicken Ala King", 2, 215F));
        orderItemsAdapter = new OrderItemsAdapter(getActivity(),orderModel.getOrderItem_list(),this);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        for (i = 0; i < orderModel.getOrderItem_list().size(); i++)
            total_price += orderModel.getOrderItem_list().get(i).getItemPrice();
        Log.d("TotalPrice", String.valueOf(total_price));
        tv_total_price.setText(String.valueOf(total_price));

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            int orderId = 0;
            @Override
            public void onClick(View v) {

                //Log.d("Order", String.valueOf(order));
                //PayM(String.valueOf(total_price));
                //webPay.setInitialScale(100);
                //webPay.loadUrl(JSON_URL+"index.php");
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "apiorder1.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("1 ", result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject object = jsonArray.getJSONObject(0);
                            orderId = object.getInt("idOrder");
                            Log.d("orderrr", String.valueOf(orderId));
                        } catch (JSONException e) {
                            Log.d("order:", "catch");
                            // Toast.makeText(Register.this, "Catch ",Toast.LENGTH_SHORT).show();
                        }
                        orderID = orderId;
                    }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(get, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("orderItemTotalPrice", String.valueOf(total_price));
                            params.put("orderStatus", orderModel.getOrderStatus());
                            params.put("store_idStore", String.valueOf(orderModel.getStore_idstore()));
                            params.put("users_id", String.valueOf(orderModel.getUsers_id()));
                            return params;
                        }

                    };
                    requestQueue.add(stringRequest);

                    //for(int j = 0; j < orderModel.getOrderItem_list().size(); i++) {
                RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, JSON_URL + "apiorderitem.php", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {
                        Log.d("hello", result);
                    }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(get, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                    }) {
                    protected Map<String, String> getParams(){
                        Map<String, String> params2 = new HashMap<>();
                        params2.put("idProduct", String.valueOf(orderModel.getOrderItem_list().get(0).getProduct_idProduct()));
                        params2.put("idStore", String.valueOf(orderModel.getStore_idstore()));
                        params2.put("idUser", String.valueOf(orderModel.getUsers_id()));
                        params2.put("idOrder", String.valueOf(orderId));
                        Log.d("OrderID", String.valueOf(orderID));
                        params2.put("productName", String.valueOf(orderModel.getOrderItem_list().get(0).getProductName()));
                        params2.put("itemPrice", String.valueOf(orderModel.getOrderItemTotalPrice()));
                        params2.put("itemQuantity", String.valueOf(orderModel.getOrderItem_list().get(0).getItemQuantity()));
                        params2.put("totalPrice", String.valueOf(orderModel.getOrderItemTotalPrice()));
                        return params2;
                    }
                };
                requestQueue2.add(stringRequest2);
                //}



                //Bundle
                Bundle bundle3 = new Bundle();
                CheckoutFragment fragment3 = new CheckoutFragment();
                bundle3.putSerializable("tempOrderList", (Serializable) total_price);
                fragment3.setArguments(bundle3);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment3).commit();
                Log.d("END" , "END");


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
    public void onItemClick(int position) {

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

    private void PayM(String amount){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+"index.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("value", amount);
                Log.d("Params: ", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}