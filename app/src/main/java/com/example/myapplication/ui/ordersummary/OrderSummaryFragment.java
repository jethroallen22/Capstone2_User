package com.example.myapplication.ui.ordersummary;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.databinding.FragmentOrderSummaryBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.order.OrderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSummaryFragment extends Fragment implements RecyclerViewInterface {

    private FragmentOrderSummaryBinding binding;
    //Cart List Recycler View
    RecyclerView rv_order_items;
    OrderModel order, tempOrder;
    List<OrderItemModel> order_item_list;
    OrderItemsAdapter orderItemsAdapter;
    TextView tv_order_id, tv_name, tv_total_price, tv_order_date;
    LinearLayout ll_prep_line, ll_prep_circle, ll_pup_line, ll_pup_circle, ll_comp_line, ll_comp_circle;
    ImageView iv_pending, iv_preparing, iv_pickup, iv_complete;
    Button btn_proceed, btn_cancel_order;
    private static String JSON_URL;
    private IPModel ipModel;
    String orderStatus;
    RatingBar rb_order_rating;

    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentOrderSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        rv_order_items = root.findViewById(R.id.rv_order_items);
        tv_order_id = root.findViewById(R.id.tv_order_id);
        tv_name = root.findViewById(R.id.tv_store_name_or);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        tv_order_date = root.findViewById(R.id.tv_order_date);
        ll_prep_line = root.findViewById(R.id.ll_prep_line);
        ll_prep_circle = root.findViewById(R.id.ll_prep_circle);
        ll_pup_line = root.findViewById(R.id.ll_pup_line);
        ll_pup_circle = root.findViewById(R.id.ll_pup_circle);
        ll_comp_circle = root.findViewById(R.id.ll_comp_circle);
        ll_comp_line = root.findViewById(R.id.ll_comp_line);
        btn_proceed = root.findViewById(R.id.btn_proceed);
        iv_pending = root.findViewById(R.id.iv_pending);
        iv_preparing = root.findViewById(R.id.iv_preparing);
        iv_pickup = root.findViewById(R.id.iv_pickup);
        iv_complete = root.findViewById(R.id.iv_complete);
        rb_order_rating = root.findViewById(R.id.rb_order_rating);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if(bundle.getParcelable("order") != null)
                tempOrder = bundle.getParcelable("order");
            else
                tempOrder = bundle.getParcelable("orderActivity");
        }
        order = tempOrder;
        iv_pending.setImageResource(R.drawable.ic_baseline_pending_actions_24);
        iv_preparing.setImageResource(R.drawable.ic_baseline_outdoor_grill_24);
        iv_pickup.setImageResource(R.drawable.ic_baseline_clean_hands_24);
        iv_complete.setImageResource(R.drawable.ic_baseline_check_24);
        tv_order_id.setText("Order ID: " + String.valueOf(order.getIdOrder()));
        tv_name.setText(String.valueOf(order.getStore_name()));
        tv_total_price.setText(String.valueOf(order.getOrderItemTotalPrice()));

        order_item_list = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getContext()).getRequestQueue();
        orderItemsAdapter = new OrderItemsAdapter(getActivity(), order.getOrderItem_list(), OrderSummaryFragment.this);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        rb_order_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                rb_order_rating.setEnabled(false);
                // Do something with the rating value
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "apirating.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String result) {
                                    Log.d("On Res", "inside on res");
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Volley Error", String.valueOf(error));
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("rating", String.valueOf(rating));
                            paramV.put("idOrder", String.valueOf(order.getIdOrder()));
                            return paramV;
                        }
                    };

                    queue.add(stringRequest);

                RequestQueue queue2 = Volley.newRequestQueue(getActivity().getApplicationContext());

                StringRequest stringRequest2 = new StringRequest(Request.Method.POST,JSON_URL+ "update_rating.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String result) {
                                Log.d("On Res", "inside on res");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error", String.valueOf(error));
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramD = new HashMap<>();
                        paramD.put("store_idStore", String.valueOf(order.getStore_idstore()));
                        return paramD;
                    }
                };

                queue2.add(stringRequest2);
            }
        });


        Log.d("Receipt: ", "Receipt");
        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                readStatus();
                //Log.d("OrderStatus", order.getOrderStatus());
                root.postDelayed(this, 1000);
            }
            }, 1000);
        orderItemsAdapter.setOnItemClickListener(new OrderItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(order.getOrderStatus().equals("pending")) {
                    Log.d("orderStatusIFSTMNT", "Pending");
                    deleteProduct(position);
                }else if(order.getOrderStatus().equals("preparing")){
                    Log.d("orderStatusIFSTMNT", "Preparing");
                    Toast.makeText(getContext(), "We're sorry, but your order is now being prepared and can no longer be cancelled. We appreciate your understanding and hope you enjoy your meal.", Toast.LENGTH_SHORT).show();
                }else if(order.getOrderStatus().equals("pickup")){
                    Log.d("orderStatusIFSTMNT", "Pickup");
                    Toast.makeText(getContext(), "We're sorry, but your order is now ready for pickup and can no longer be cancelled. We appreciate your understanding and hope you enjoy your meal.", Toast.LENGTH_SHORT).show();
                }else if(order.getOrderStatus().equals("complete"))
                    Toast.makeText(getContext(), "We're sorry, but your order is already complete", Toast.LENGTH_SHORT).show();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment fragment = new HomeFragment();
                Log.d("TAG", "Success");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void UpdateStatus(int idOrder, String status){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+ "testO.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", String.valueOf(error));
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idOrder", String.valueOf(idOrder));
                params.put("orderStatus", status);
//                Log.d("Params: ", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void readStatus(){
        JsonArrayRequest jsonArrayRequest7= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("readstatus", String.valueOf(response));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject7 = response.getJSONObject(i);
                        Log.d("OrderID", String.valueOf(order.getOrderItem_list().get(0).getIdOrder()));
                        Log.d("OrderIDdb", String.valueOf(jsonObject7.getInt("idOrder")));
                        final String orderStatus = jsonObject7.getString("orderStatus");
                        if (jsonObject7.getInt("idOrder") == order.getOrderItem_list().get(0).getIdOrder()){
                            Log.d("Match", "Match");
                            OrderSummaryFragment.this.orderStatus = orderStatus;
                            Log.d("OrderStatusInside", OrderSummaryFragment.this.orderStatus);
                            Log.d("readstatus", orderStatus);
                            if(orderStatus.equals("pending")){
                                order.setOrderStatus(orderStatus);
                                ll_prep_line.setBackgroundColor(Color.parseColor("#979797"));
                                ll_prep_circle.setBackgroundResource(R.drawable.bg_gray_round);
                                ll_pup_line.setBackgroundColor(Color.parseColor("#979797"));
                                ll_pup_circle.setBackgroundResource(R.drawable.bg_gray_round);
                                ll_comp_line.setBackgroundColor(Color.parseColor("#979797"));
                                ll_comp_circle.setBackgroundResource(R.drawable.bg_gray_round);

                            } else if (orderStatus.equals("preparing")){
                                order.setOrderStatus(orderStatus);
                                ll_prep_line.setBackgroundColor(Color.parseColor("#67335F"));
                                ll_prep_circle.setBackgroundResource(R.drawable.bg_light_yellow_round);
                            } else if (orderStatus.equals("pickup")){
                                order.setOrderStatus(orderStatus);
                                ll_prep_line.setBackgroundColor(Color.parseColor("#67335F"));
                                ll_prep_circle.setBackgroundResource(R.drawable.bg_light_yellow_round);
                                ll_pup_line.setBackgroundColor(Color.parseColor("#E09F3E"));
                                ll_pup_circle.setBackgroundResource(R.drawable.bg_yellow_round);
                            } else if (orderStatus.equals("complete")){
                                order.setOrderStatus(orderStatus);
                                ll_prep_line.setBackgroundColor(Color.parseColor("#67335F"));
                                ll_prep_circle.setBackgroundResource(R.drawable.bg_light_yellow_round);
                                ll_pup_line.setBackgroundColor(Color.parseColor("#E09F3E"));
                                ll_pup_circle.setBackgroundResource(R.drawable.bg_yellow_round);
                                ll_comp_line.setBackgroundColor(Color.parseColor("#335C67"));
                                ll_comp_circle.setBackgroundResource(R.drawable.bg_bluegreen_round);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    order.setOrderStatus(orderStatus);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest7);
        //Log.d("OrderStatus", OrderSummaryFragment.this.orderStatus);

    }

    public void deleteProduct(int position){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "deleteOrderItem.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("On Res", "inside on res");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", String.valueOf(error));
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                for(int i = 0 ; i < order.getOrderItem_list().size() ; i++)
                    Log.d("ProductList", "ID: " + order.getOrderItem_list().get(i).getIdProduct() + " " + order.getOrderItem_list().get(i).getProductName());
                Log.d("ProductNameDelete", "ID: " + order.getOrderItem_list().get(position).getIdProduct() + " " + order.getOrderItem_list().get(position).getProductName());
                paramV.put("idProduct", String.valueOf(order.getOrderItem_list().get(position).getIdProduct()));
                paramV.put("idOrder", String.valueOf(order.getIdOrder()));
                paramV.put("totalAmount", String.valueOf(order.getOrderItem_list().get(position).getTotalPrice()));
                paramV.put("userId", String.valueOf(order.getUsers_id()));
                paramV.put("itemStatus", "cancelled");
                order.getOrderItem_list().remove(position);
                orderItemsAdapter.notifyItemRemoved(position);
                return paramV;
            }
        };

        queue.add(stringRequest);

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
    public void onItemClickWeather(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {

    }
}