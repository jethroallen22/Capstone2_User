package com.example.myapplication.ui.checkout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.databinding.FragmentCheckout3Binding;
import com.example.myapplication.databinding.FragmentCheckoutBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.interfaces.VolleyCallback;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout3Fragment extends Fragment implements com.example.myapplication.interfaces.VolleyCallback {

    private FragmentCheckout3Binding binding;
    OrderModel orderModel;
    private static String JSON_URL;
    private IPModel ipModel;
    int counter = 0;

    RequestQueue requestQueueOrder;
    List<OrderModel> orderModelList;

    private Home mActivity;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        Bundle bundle = this.getArguments();
        if (bundle != null)
            orderModel = bundle.getParcelable("order");
        orderModelList = new ArrayList<>();
        requestQueueOrder = Singleton.getsInstance(getActivity()).getRequestQueue();

        binding.nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "apiorder1.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("1 ", result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject object = jsonArray.getJSONObject(0);
                            //callback.onSuccessResponse(object.getInt("idOrder"));
                            int orderId = object.getInt("idOrder");
                            Log.d("objectIDOrder", String.valueOf(object.getInt("idOrder")));
                            orderModel.setIdOrder(orderId);

                            for (int k = 0; k < orderModel.getOrderItem_list().size(); k++){
                                orderModel.getOrderItem_list().get(k).setIdOrder(orderId);
                                Log.d("hatdog2", String.valueOf(orderModel.getOrderItem_list().get(k).getIdOrder()));
                                Log.d("hatdog2.1", String.valueOf(orderModel.getIdOrder()));
                            }
                            Log.d("hatdog3", String.valueOf(orderModel.getOrderItem_list().size()));

                            RequestQueue requestQueue2 = Volley.newRequestQueue(mActivity);
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

                        } catch (JSONException e) {
                            Log.d("order:", "catch");
                            // Toast.makeText(Register.this, "Catch ",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(get, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("orderItemTotalPrice", String.valueOf(orderModel.getOrderItemTotalPrice()));
                        params.put("orderStatus", orderModel.getOrderStatus());
                        params.put("store_idStore", String.valueOf(orderModel.getStore_idstore()));
                        params.put("users_id", String.valueOf(orderModel.getUsers_id()));
                        return params;
                    }

                };
                requestQueue.add(stringRequest);

                HomeFragment fragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Home) getActivity();
    }

    @Override
    public void onSuccessResponse(int result) {
        // Handle the data returned by the StringRequest
        Log.d("TAG", "Result: " + result);
        orderModel.setIdOrder(result);
        Log.d("idOrder", String.valueOf(orderModel.getIdOrder()));
    }

    private void getDataFromServer(final VolleyCallback callback) {

//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "apiorder1.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String result) {
//                Log.d("1 ", result);
//                try {
//                    JSONArray jsonArray = new JSONArray(result);
//                    JSONObject object = jsonArray.getJSONObject(0);
//                    callback.onSuccessResponse(object.getInt("idOrder"));
//                    int orderId = object.getInt("idOrder");
//                    orderModel.setIdOrder(orderId);
//
////                            for (int k = 0; k < orderModel.getOrderItem_list().size(); k++){
////                                orderModel.getOrderItem_list().get(k).setIdOrder(orderId);
////                                Log.d("hatdog2", String.valueOf(orderModel.getOrderItem_list().get(k).getIdOrder()));
////                                Log.d("hatdog2.1", String.valueOf(orderModel.getIdOrder()));
////                            }
//                    Log.d("hatdog3", String.valueOf(orderModel.getOrderItem_list().size()));
//
//                    Log.d("orderrr", String.valueOf(orderId));
//
//                } catch (JSONException e) {
//                    Log.d("order:", "catch");
//                    // Toast.makeText(Register.this, "Catch ",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Toast.makeText(get, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
//            }
//
//
//        }) {
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("orderItemTotalPrice", String.valueOf(orderModel.getOrderItemTotalPrice()));
//                params.put("orderStatus", orderModel.getOrderStatus());
//                params.put("store_idStore", String.valueOf(orderModel.getStore_idstore()));
//                params.put("users_id", String.valueOf(orderModel.getUsers_id()));
//                return params;
//            }
//
//        };
//        requestQueue.add(stringRequest);


    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}