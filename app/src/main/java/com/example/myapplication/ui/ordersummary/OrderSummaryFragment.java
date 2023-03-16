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
import android.widget.LinearLayout;
import android.widget.TextView;

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
    LinearLayout ll_prep_line, ll_prep_circle, ll_pup_line, ll_pup_circle;
    Button btn_proceed, btn_cancel_order;
    private static String JSON_URL;
    private IPModel ipModel;

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
        btn_proceed = root.findViewById(R.id.btn_proceed);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if(bundle.getParcelable("order") != null)
                tempOrder = bundle.getParcelable("order");
            else
                tempOrder = bundle.getParcelable("orderActivity");
        }
        order = tempOrder;
        tv_order_id.setText(String.valueOf(order.getIdOrder()));
        tv_name.setText(String.valueOf(order.getStore_name()));
        tv_total_price.setText(String.valueOf(order.getOrderItemTotalPrice()));
        //tv_address.setText(order.getAddress());
        //tv_distance.setText("Distance from you: " + order.getDistance() + "km");

        order_item_list = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
//        order_item_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now().getHour()), "3.5", order_item_list, order_item_list.size(), 123F));
//        order_item_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now().getHour()), "3.5", order_item_list, order_item_list.size(), 123F));

        orderItemsAdapter = new OrderItemsAdapter(getActivity(), order.getOrderItem_list(), OrderSummaryFragment.this);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                readStatus();
                root.postDelayed(this,5000);
            }
        },1000);



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
                        if (jsonObject7.getInt("idOrder") == order.getOrderItem_list().get(0).getIdOrder()){
                        String orderStatus = jsonObject7.getString("orderStatus");

                        if (orderStatus.equals("preparing")){
                            ll_prep_line.setBackgroundColor(Color.parseColor("#E09F3E"));
                            ll_prep_circle.setBackgroundResource(R.drawable.bg_yellow_round);
                        } else if (orderStatus.equals("pickup")){
                            ll_pup_line.setBackgroundColor(Color.parseColor("#335C67"));
                            ll_pup_circle.setBackgroundResource(R.drawable.bg_bluegreen_round);
                        }
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
        requestQueue.add(jsonArrayRequest7);
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