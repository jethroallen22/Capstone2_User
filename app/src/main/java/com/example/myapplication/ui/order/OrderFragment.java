package com.example.myapplication.ui.order;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.CartModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.store.StoreFragment;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment implements RecyclerViewInterface {

    //
    RecyclerView rv_order_items;
    List<OrderItemModel> order_item_list;
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
    String JSON_URL = "http://192.168.68.109/mosibus_php/user/paymongo/";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrderViewModel orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tv_store_name = root.findViewById(R.id.tv_store_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        btn_place_order = root.findViewById(R.id.btn_place_order);
        webPay = root.findViewById(R.id.webPay);
        Bundle bundle = this.getArguments();
        order_item_list = new ArrayList<>();
        order_item_list = (List<OrderItemModel>) getArguments().getSerializable("order_item_list");

//        if (bundle != null){
//            store_name = bundle.getString("StoreName");
//            tv_store_name.setText(store_name);
//            Log.d("StoreName",store_name);
//        }

        rv_order_items = root.findViewById(R.id.rv_order_items);
//        order_item_list = new ArrayList<>();
//        order_item_list.add(new OrderItemModel("Burger Mcdo", 3, 135F));
//        order_item_list.add(new OrderItemModel("Chicken Ala King", 2, 215F));
        orderItemsAdapter = new OrderItemsAdapter(getActivity(),order_item_list,this);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        for (i = 0; i < order_item_list.size(); i++)
            total_price += order_item_list.get(i).getItemPrice();
        Log.d("TotalPrice", String.valueOf(total_price));
        tv_total_price.setText(String.valueOf(total_price));

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("Order", String.valueOf(order));
                PayM(String.valueOf(total_price));
                webPay.setInitialScale(100);
                webPay.loadUrl(JSON_URL+"index.php");
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