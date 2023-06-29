package com.example.myapplication.ui.order;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.myapplication.activities.models.DealsModel;
import com.example.myapplication.activities.models.OrderItemModel;
import com.example.myapplication.activities.models.WalletModel;
import com.example.myapplication.adapters.HomeDealsAdapter;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.adapters.VoucherAdapter;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.activities.models.OrderModel;
import com.example.myapplication.activities.models.VoucherModel;
import com.example.myapplication.ui.checkout.CheckoutFragment;
import com.example.myapplication.ui.ordersummary.OrderSummaryFragment;
import com.example.myapplication.ui.payment.PaymentFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderFragment extends Fragment implements RecyclerViewInterface {

    //
    RecyclerView rv_order_items;
    OrderModel orderModel;
    OrderItemsAdapter orderItemsAdapter;
    private FragmentOrderBinding binding;
    TextView tv_store_name;
    TextView tv_total_price;
    Button btn_place_order;
    private String store_name;
    //School IP
    private static String JSON_URL;
    private IPModel ipModel;
    RadioGroup rg_payment;
    RadioButton radio_wallet, radio_gcash;
    String payment_method;
    float wallet;
    String walletText;
    NotificationManager manager;
    RequestQueue requestQueueOrder, requestQueueVoucher, requestQueueAvailVoucher, requestQueueBalance;

    RecyclerView rv_vouchers;

    TextView tv_voucher_order;

    List<VoucherModel> voucher_list;
    VoucherAdapter voucherAdapter;
    Handler handler;
    Runnable myRunnable;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();


        requestQueueVoucher = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueAvailVoucher = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueBalance = Singleton.getsInstance(getActivity()).getRequestQueue();


        tv_store_name = root.findViewById(R.id.tv_store_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        btn_place_order = root.findViewById(R.id.btn_place_order);
        rg_payment = root.findViewById(R.id.rg_payment);
        radio_gcash = root.findViewById(R.id.radio_gcash);
        radio_wallet = root.findViewById(R.id.radio_wallet);
        tv_voucher_order = root.findViewById(R.id.tv_voucher_order);
        Bundle bundle = this.getArguments();
        voucher_list = new ArrayList<>();
        if (bundle != null){
            orderModel = bundle.getParcelable("order");
            wallet = bundle.getFloat("wallet");
            Log.d("ofragOILSize", String.valueOf(orderModel.getOrderItem_list().size()));
            store_name = orderModel.getStore_name();
            tv_store_name.setText(store_name);
            Log.d("StoreName",store_name);
        }

        handler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                getAvailBalance();
            }
        };
        handler.postDelayed(myRunnable, 1000);
        btn_place_order.setEnabled(false);
        rv_order_items = root.findViewById(R.id.rv_order_items);
        requestQueueOrder = Singleton.getsInstance(getActivity()).getRequestQueue();
        getDiscount();



        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = root.findViewById(checkedId);
                String selectedOption = radioButton.getText().toString();
                String walletText = "Wallet: P" + wallet;
                // Perform actions based on the selected option
                // Perform actions based on the selected option
                if (selectedOption.equals(walletText)) {
                    payment_method = "wallet";
                    btn_place_order.setEnabled(true);
                } else if (selectedOption.equals("Gcash")) {
                    payment_method = "gcash";
                    btn_place_order.setEnabled(true);
                }
            }
        });

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payment_method == "wallet") {
                    if(orderModel.getOrderItemTotalPrice() <= wallet){
                        getDataFromServer();
                        sendtoAvailVoucherDb(orderModel.getUsers_id(), orderModel.getVoucher_id());
                    } else {
                        Toast.makeText(getContext(), "You don't have sufficient balance in your wallet, please cash in for additional amount", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("order", orderModel);
                    CheckoutFragment fragment = new CheckoutFragment(orderModel);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                }
            }
        });

        tv_voucher_order.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showVoucherBottomSheet();
            }
        });

        return root;
    }

    public void getDiscount(){
        RecyclerViewInterface recyclerViewInterface = OrderFragment.this;
        JsonArrayRequest jsonArrayRequestDeals = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apideals.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("DealsResponse", String.valueOf(response));
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int dealId = jsonObject.getInt("dealsId");
                        int storeId = jsonObject.getInt("storeId");
                        int percentage = jsonObject.getInt("percentage");
                        int productId = jsonObject.getInt("productId");
                        String startDate = jsonObject.getString("startDate");
                        String endDate = jsonObject.getString("endDate");
                        DealsModel deal = new DealsModel(dealId, storeId, percentage);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date currentDate = new Date(); // Current date
                        try {
                            Date startDateTemp = dateFormat.parse(startDate);
                            Date endDateTemp = dateFormat.parse(endDate);

                            if (currentDate.after(startDateTemp)) {
                                if (currentDate.before(endDateTemp)) {
                                    float temp = 0;
                                    for (OrderItemModel orderItemModel: orderModel.getOrderItem_list()){
                                        Log.d("OrderFragment", "ReadDealProdID: " + productId + " OrderItemIdProd: " + orderItemModel.getIdProduct());
                                        if(orderItemModel.getIdProduct() == productId) {
                                            orderItemModel.setTotalPrice(orderItemModel.getTotalPrice() - ((orderItemModel.getTotalPrice() * percentage) / 100));
                                        }
                                        temp += orderItemModel.getTotalPrice();
                                    }
                                    orderModel.setOrderItemTotalPrice(temp);
                                } else {
                                    Log.d("InvalidDeal", deal.getStoreName() + ": Date is after end date");
                                }
                            } else {
                                Log.d("InvalidDeal", deal.getStoreName() + ": Date is before start date");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d("ParseException", e.getMessage());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("JSONException", e.getMessage());
                    }
                }
                orderItemsAdapter = new OrderItemsAdapter(getActivity(),orderModel.getOrderItem_list(),recyclerViewInterface);
                rv_order_items.setAdapter(orderItemsAdapter);
                rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                rv_order_items.setHasFixedSize(true);
                rv_order_items.setNestedScrollingEnabled(false);
                tv_total_price.setText(String.valueOf(orderModel.getOrderItemTotalPrice()));
                orderItemsAdapter.setOnItemClickListener(new OrderItemsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        deleteProduct(position);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("VolleyError", error.getMessage());
            }
        });
        requestQueueOrder.add(jsonArrayRequestDeals);
    }

    public void getAvailBalance(){
        //walletModel = new WalletModel(0,0);
        JsonArrayRequest jsonArrayRequestBalance = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiwalletget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        final double wallet = jsonObject.getDouble("wallet");
                        if(id == orderModel.getUsers_id()) {
                            OrderFragment.this.wallet = (float) wallet;
                            walletText = "Wallet: P" + wallet;
                            radio_wallet.setText(walletText);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
            }
        });
        requestQueueBalance.add(jsonArrayRequestBalance);
    }

    private void getDataFromServer() {

        float temp = wallet - orderModel.getOrderItemTotalPrice();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, JSON_URL + "update_wallet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("On Res", "inside on res");
                        } catch (Throwable e) {
                            Log.d("Catch", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                Log.d("Cashin", String.valueOf(temp));
                paramV.put("id", String.valueOf(orderModel.getUsers_id()));
                paramV.put("wallet", String.valueOf(temp));
                Log.d("Cashin", "success");
                return paramV;
            }
        };
        queue.add(stringRequest1);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "apiorderpost.php", new Response.Listener<String>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(String result) {
                Log.d("1 ", result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject object = jsonArray.getJSONObject(0);
                    int orderId = object.getInt("idOrder");
                    orderModel.setIdOrder(orderId);

                    for (int k = 0; k < orderModel.getOrderItem_list().size(); k++){
                        orderModel.getOrderItem_list().get(k).setIdOrder(orderId);
                    }

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, JSON_URL + "apiorderitem.php", new Response.Listener<String>() {

                        @Override
                        public void onResponse(String result) {
                            Log.d("onResponse", result);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(get, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                            Log.d("Catch", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params2 = new HashMap<>();
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(orderModel.getOrderItem_list());
                            params2.put("data", jsonArray);
                            Log.d("idProduct", String.valueOf(orderModel.getOrderItem_list().get(0).getIdProduct()));
                            Log.d("hatdog", String.valueOf(params2));
                            return params2;
                        }
                    };
                    requestQueue2.add(stringRequest2);


                    RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest3 = new StringRequest(Request.Method.POST, JSON_URL + "apiorderhistorypost.php", new Response.Listener<String>() {

                        @Override
                        public void onResponse(String result) {
                            Log.d("response", result);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(get, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                            Log.d("Catch", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params2 = new HashMap<>();
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(orderModel.getOrderItem_list());
                            params2.put("data", jsonArray);
                            Log.d("idProduct", String.valueOf(orderModel.getOrderItem_list().get(0).getIdProduct()));
                            Log.d("hatdog", String.valueOf(params2));
                            return params2;
                        }
                    };
                    requestQueue3.add(stringRequest3);

                } catch (JSONException e) {
                    Log.d("order:", "catch");
                    // Toast.makeText(Register.this, "Catch ",Toast.LENGTH_SHORT).show();
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "My Notification");
                builder.setContentTitle("Mosibus");
                builder.setContentText("Your have successfully placed your order!");
                builder.setSmallIcon(R.drawable.logo);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity().getApplicationContext());
                managerCompat.notify(1, builder.build());

                NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
                manager = (NotificationManager) getSystemService(getActivity().getApplicationContext(), NotificationManager.class);
                manager.createNotificationChannel(channel);

                Log.d("OrderIDCheckout3", String.valueOf(orderModel.getIdOrder()));
                orderModel.setOrderStatus("pending");
                Bundle bundle = new Bundle();
                Log.d("orderStatus", orderModel.getOrderStatus());
                bundle.putParcelable("order", orderModel);
                bundle.putFloat("wallet", temp);
                OrderSummaryFragment fragment = new OrderSummaryFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(get, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @SuppressLint("MissingPermission")
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderItemTotalPrice", String.valueOf(orderModel.getOrderItemTotalPrice()));
                params.put("orderStatus", "pending");
                params.put("store_idStore", String.valueOf(orderModel.getStore_idstore()));
                params.put("users_id", String.valueOf(orderModel.getUsers_id()));
                params.put("iduser", String.valueOf(orderModel.getUsers_id()));
                params.put("type", "order");
                params.put("title", "Your order from " + orderModel.getStore_name() + " has been placed!");
                params.put("description", "Your order from " + orderModel.getStore_name() + " has successfully been placed. Please standby for further update as we prepare your order.");

                return params;
            }

        };
        requestQueue.add(stringRequest);


//

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
                        orderModel.getOrderItem_list().remove(position);
                        orderItemsAdapter.notifyItemRemoved(position);
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
                return paramV;
            }
        };

        queue.add(stringRequest);


    }

    public void showVoucherBottomSheet() {


        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.vouchers_bottom_sheet_layout, getActivity().findViewById(R.id.voucher_bottomSheet_container));

        BottomSheetDialog voucherDialog = new BottomSheetDialog(getActivity());
        voucherDialog.setContentView(view);




        TextView tv_voucher_status = view.findViewById(R.id.tv_voucher_status);
        //Button bt_apply_voucher = view.findViewById(R.id.bt_apply_voucher);
        rv_vouchers = view.findViewById(R.id.rv_vouchers);
        //JsonArrayRequest for Reading Vouchers DB
        if(voucher_list.size() == 0) {
            voucher_list = new ArrayList<>();
            JsonArrayRequest jsonArrayRequestVouchers = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apivouchers.php", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("voucher", "Voucher: " + response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObjectVoucher = response.getJSONObject(i);
                            int voucherId = jsonObjectVoucher.getInt("voucherId");
                            String voucherName = jsonObjectVoucher.getString("voucherName");
                            int storeId = jsonObjectVoucher.getInt("storeId");
                            int voucherAmount = jsonObjectVoucher.getInt("voucherAmount");
                            int voucherMin = jsonObjectVoucher.getInt("voucherMin");
                            String startDateString = jsonObjectVoucher.getString("startDate");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date startDate = dateFormat.parse(startDateString);

                            String endDateString = jsonObjectVoucher.getString("endDate");
                            SimpleDateFormat dateFormatend = new SimpleDateFormat("yyyy-MM-dd");
                            Date endDate = dateFormatend.parse(endDateString);

                            LocalDate curDate = LocalDate.now();
                            Date curdate = dateFormat.parse(String.valueOf(curDate));
                            String status = jsonObjectVoucher.getString("status");


                            // orderModel.getOrderItemTotalPrice() >= voucherMin
                            VoucherModel voucherModel = new VoucherModel(voucherId, voucherName, voucherAmount, voucherMin, startDate, endDate);
                            //voucher_list.add(voucherModel);

                            //JsonArrayRequest for Reading Vouchers DB
                            JsonArrayRequest jsonArrayRequestAvailVouchers = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiavailvouchers.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d("voucher", "AvailVoucher: " + response);
                                    boolean voucherAvailed = false;
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject jsonObjectAvailVoucher = response.getJSONObject(i);
                                            int availVoucherId = jsonObjectAvailVoucher.getInt("availVoucherId");
                                            int availVoucherId2 = jsonObjectAvailVoucher.getInt("voucherId");
                                            int availUserId = jsonObjectAvailVoucher.getInt("userId");

                                            if (availUserId == orderModel.getUsers_id() && voucherModel.getVoucherId() == availVoucherId2) {
                                                voucherAvailed = true;
                                                break;
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (!voucherAvailed) {
                                        if (curdate.before(voucherModel.getEndDate())) {
                                            if (storeId == orderModel.getStore_idstore()) {
                                                voucher_list.add(voucherModel);
                                            }
                                        }
                                    }
                                    voucherAdapter = new VoucherAdapter(getActivity(), voucher_list, OrderFragment.this);
                                    rv_vouchers.setAdapter(voucherAdapter);
                                    rv_vouchers.setLayoutManager(new LinearLayoutManager(getContext()));
                                    voucherAdapter.setOnItemClickListener(new VoucherAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            Log.d("witwiw", voucher_list.get(position).getVoucherName());
                                            float tempTotalPrice;
                                            tempTotalPrice = orderModel.getOrderItemTotalPrice() - voucher_list.get(position).getVoucherAmount();
                                            tv_total_price.setText("P" + tempTotalPrice);
                                            orderModel.setOrderItemTotalPrice(tempTotalPrice);
                                            orderModel.setVoucher_id(voucher_list.get(position).getVoucherId());
                                            voucher_list.remove(position);
                                            voucherAdapter.notifyItemRemoved(position);
                                            voucherDialog.dismiss();
                                        }

                                    });

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            requestQueueAvailVoucher.add(jsonArrayRequestAvailVouchers);
                            //

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("sizelist", String.valueOf(voucher_list.size()));

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueueVoucher.add(jsonArrayRequestVouchers);
        } else {
            voucherAdapter = new VoucherAdapter(getActivity(), voucher_list, OrderFragment.this);
            rv_vouchers.setAdapter(voucherAdapter);
            rv_vouchers.setLayoutManager(new LinearLayoutManager(getContext()));
            voucherAdapter.setOnItemClickListener(new VoucherAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Log.d("witwiw", voucher_list.get(position).getVoucherName());
                    float tempTotalPrice;
                    tempTotalPrice = orderModel.getOrderItemTotalPrice() - voucher_list.get(position).getVoucherAmount();
                    tv_total_price.setText("P" + tempTotalPrice);
                    orderModel.setOrderItemTotalPrice(tempTotalPrice);
                    orderModel.setVoucher_id(voucher_list.get(position).getVoucherId());
                    voucher_list.remove(position);
                    voucherAdapter.notifyItemRemoved(position);
                    voucherDialog.dismiss();
                }

            });
        }
        //


        //OnClick for Apply in Voucher Bottomsheet
//        bt_apply_voucher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                voucherDialog.dismiss();
//            }
//        });

        voucherDialog.show();
    }

    private void sendtoAvailVoucherDb(int userId,int voucherId){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, JSON_URL + "apiavailvoucher.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("On Res", "inside on res");
                        } catch (Throwable e) {
                            Log.d("Catch", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("userId", String.valueOf(userId));
                paramV.put("voucherId", String.valueOf(voucherId));
                return paramV;
            }
        };
        queue.add(stringRequest1);


    }


}