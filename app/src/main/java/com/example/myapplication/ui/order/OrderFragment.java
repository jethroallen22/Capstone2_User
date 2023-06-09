package com.example.myapplication.ui.order;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.EditText;
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
import com.example.myapplication.activities.Home;
import com.example.myapplication.activities.Login;
import com.example.myapplication.activities.Register;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.CartModel;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.checkout.CheckoutFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment implements RecyclerViewInterface {

    //
    RecyclerView rv_order_items;
    OrderModel orderModel;
    OrderItemsAdapter orderItemsAdapter;
    private FragmentOrderBinding binding;
    TextView tv_store_name;
    TextView tv_total_price;

    TextView tv_total_discount;
    TextView tv_final_price;
    TextView tv_total2;

    TextView tv_total;

    TextView tv_voucher;

    ConstraintLayout cv_discount;

    BottomSheetDialog voucherDialog;
    Button btn_place_order;
    WebView webPay;
    private String store_name;
    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    private RequestQueue requestQueueVoucher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        tv_store_name = root.findViewById(R.id.tv_store_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        tv_voucher = root.findViewById(R.id.tv_voucher);
        btn_place_order = root.findViewById(R.id.btn_place_order);
        webPay = root.findViewById(R.id.webPay);

        tv_total_discount = root.findViewById(R.id.tv_total_discount);
        tv_final_price = root.findViewById(R.id.tv_final_price);
        tv_total2 = root.findViewById(R.id.tv_total2);
        tv_total = root.findViewById(R.id.tv_total);
        cv_discount = root.findViewById(R.id.cv_discount);


        voucherDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);

        Bundle bundle = this.getArguments();

        if (bundle != null){
            orderModel = bundle.getParcelable("order");
            Log.d("ofragOILSize", String.valueOf(orderModel.getOrderItem_list().size()));
            store_name = orderModel.getStore_name();
            tv_store_name.setText(store_name);
            Log.d("StoreName",store_name);
        }

        rv_order_items = root.findViewById(R.id.rv_order_items);
        orderItemsAdapter = new OrderItemsAdapter(getActivity(),orderModel.getOrderItem_list(),this);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);
        tv_total_price.setText(String.valueOf(orderModel.getOrderItemTotalPrice()));

        requestQueueVoucher = Singleton.getsInstance(getActivity()).getRequestQueue();

        orderItemsAdapter.setOnItemClickListener(new OrderItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                deleteProduct(position);
            }
        });

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("order", orderModel);
                CheckoutFragment fragment = new CheckoutFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        tv_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoucherBottomSheet();
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
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickSearch(int pos) {

    }

    @Override
    public void onItemClickWeather(int position) {

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

    public void deleteProduct(int position){
        RequestQueue queue = Singleton.getsInstance(getContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "deleteCartItem.php",
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
                for(int i = 0 ; i < orderModel.getOrderItem_list().size() ; i++)
                    Log.d("ProductList", "ID: " + orderModel.getOrderItem_list().get(i).getIdProduct() + " " + orderModel.getOrderItem_list().get(i).getProductName());
                Log.d("ProductNameDelete", "ID: " + orderModel.getOrderItem_list().get(position).getIdProduct() + " " + orderModel.getOrderItem_list().get(position).getProductName());
                paramV.put("idProduct", String.valueOf(orderModel.getOrderItem_list().get(position).getIdProduct()));
                paramV.put("idUser", String.valueOf(orderModel.getUsers_id()));
                orderModel.getOrderItem_list().remove(position);
                orderItemsAdapter.notifyItemRemoved(position);
                return paramV;
            }
        };

        queue.add(stringRequest);


    }

    //BottomSheet for Vouchers
    public void showVoucherBottomSheet() {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.vouchers_bottom_sheet_layout, getActivity().findViewById(R.id.voucher_bottomSheet_container));

        BottomSheetDialog voucherDialog = new BottomSheetDialog(getActivity());
        voucherDialog.setContentView(view);

        TextView tv_voucher_status = view.findViewById(R.id.tv_voucher_status);
        EditText et_voucher_code = view.findViewById(R.id.et_voucher_code);
        Button bt_voucher = view.findViewById(R.id.bt_voucher);

        //OnClick for Apply in Voucher Bottomsheet
        bt_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //JsonArrayRequest for Reading Vouchers DB
                JsonArrayRequest jsonArrayRequestVouchers = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apivouchers.php", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i < response.length(); i++){
                            try {
                                JSONObject jsonObjectVoucher = response.getJSONObject(i);
                                int voucherId = jsonObjectVoucher.getInt("voucherId");
                                String voucherName = jsonObjectVoucher.getString("voucherName");
                                int storeId = jsonObjectVoucher.getInt("storeId");
                                int voucherAmount = jsonObjectVoucher.getInt("voucherAmount");
                                int voucherMin = jsonObjectVoucher.getInt("voucherMin");


                                if(Integer.parseInt(et_voucher_code.getText().toString().trim()) == voucherId) {
                                    if (orderModel.getOrderItemTotalPrice() >= voucherMin) {
                                        cv_discount.setVisibility(View.VISIBLE);
                                        tv_total.setText("Subtotal");
                                        tv_total2.setVisibility(View.VISIBLE);
                                        tv_total_discount.setVisibility(View.VISIBLE);
                                        tv_total2.setVisibility(View.VISIBLE);
                                        tv_total2.setVisibility(View.VISIBLE);
                                        tv_final_price.setVisibility(View.VISIBLE);
                                        tv_total_discount.setText("-" + voucherAmount);
                                        tv_final_price.setText(String.valueOf(orderModel.getOrderItemTotalPrice() - voucherAmount));
                                       orderModel.setOrderItemTotalPrice(orderModel.getOrderItemTotalPrice() - voucherAmount);
                                        voucherDialog.dismiss();
                                    }else {
                                        tv_voucher_status.setText("Total Amount did not meet the Voucher Minimum Requirements!");
                                    }
                                } else {
                                    tv_voucher_status.setText("Voucher does not exist!");
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
                requestQueueVoucher.add(jsonArrayRequestVouchers);
                //
            }
        });

        voucherDialog.show();
    }

}