package com.example.myapplication.ui.checkout;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myapplication.activities.Home;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.activities.models.OrderModel;
import com.example.myapplication.databinding.FragmentCheckout4Binding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.ui.ordersummary.OrderSummaryFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout4Fragment extends Fragment {

    private FragmentCheckout4Binding binding;
    OrderModel orderModel;
    private static String JSON_URL;
    private IPModel ipModel;
    RequestQueue requestQueueOrder;
    List<OrderModel> orderModelList;
    private Home mActivity;

    NotificationManager manager;

    TextView total, id3, amount;

    Button btn_next;
    float wallet;
    String dateTimeString;

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout4Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        total = root.findViewById(R.id.tv_total_amount);
        amount = root.findViewById(R.id.tv_amount);
        btn_next = root.findViewById(R.id.btn_next4);

        Bundle bundle = getArguments();
        if (bundle != null) {
            orderModel = bundle.getParcelable("order");
            wallet = bundle.getFloat("wallet");
            dateTimeString = bundle.getString("datetime");
            Log.d("Checkout4" , "wallet: " + wallet);
            Log.d("Checkout4" , "amount: " + amount);
            Log.d("Checkout4" , "datetime: " + dateTimeString);
        }
        orderModelList = new ArrayList<>();
        requestQueueOrder = Singleton.getsInstance(getActivity()).getRequestQueue();

        Log.d("checkout", String.valueOf(orderModel.getOrderItemTotalPrice()));
        total.setText(String.valueOf(orderModel.getOrderItemTotalPrice()));
        Log.d("checkout", total.getText().toString());
        amount.setText(String.valueOf(orderModel.getOrderItemTotalPrice()));


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServer();
                sendtoAvailVoucherDb(orderModel.getUsers_id(), orderModel.getVoucher_id());
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Home) getActivity();
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
                params.put("datetime", dateTimeString);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}