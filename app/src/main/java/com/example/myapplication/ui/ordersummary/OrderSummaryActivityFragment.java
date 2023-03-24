package com.example.myapplication.ui.ordersummary;

import android.graphics.Color;
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
import androidx.fragment.app.Fragment;
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
import com.example.myapplication.R;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.databinding.FragmentOrderSummaryBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.order.OrderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSummaryActivityFragment extends Fragment implements RecyclerViewInterface {

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
        orderItemsAdapter = new OrderItemsAdapter(getActivity(), order.getOrderItem_list(), OrderSummaryActivityFragment.this);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        Log.d("Receipt: ", "Activity");
        Log.d("orderitemsize", String.valueOf(order.getOrderItem_list().size()));
        ll_prep_line.setBackgroundColor(Color.parseColor("#67335F"));
        ll_prep_circle.setBackgroundResource(R.drawable.bg_light_yellow_round);
        ll_pup_line.setBackgroundColor(Color.parseColor("#E09F3E"));
        ll_pup_circle.setBackgroundResource(R.drawable.bg_yellow_round);
        ll_comp_line.setBackgroundColor(Color.parseColor("#335C67"));
        ll_comp_circle.setBackgroundResource(R.drawable.bg_bluegreen_round);

        btn_proceed.setText("Reorder");
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("order", order);
                OrderFragment fragment = new OrderFragment();
                fragment.setArguments(bundle);
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