package com.example.myapplication.ui.cart;

import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.order.OrderFragment;
import com.example.myapplication.ui.search.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CartFragment extends Fragment implements RecyclerViewInterface {

    private FragmentCartBinding binding;
    //Cart List Recycler View
    RecyclerView rv_cart;
    CartAdapter cartAdapter;
    Button btn_remove;
    CheckBox cb_cart_item;
    CheckBox checkBox;
    List<OrderModel> order_list;
    List<StoreModel> temp_store_list;
    List<OrderItemModel> order_item_list;
    RequestQueue requestQueueCart, requestQueueStore;
    int userID = 0;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        order_list = new ArrayList<>();
        order_item_list = new ArrayList<>();
        temp_store_list = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle != null) {
//            temp_store_list = (List<StoreModel>) bundle.getSerializable("storeList");
            userID = bundle.getInt("userID");
            Log.d("USERID", String.valueOf(userID));
        }

        rv_cart = root.findViewById(R.id.rv_cart);
        requestQueueCart = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueueStore = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractStoreCartItem();

        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                order_list = new ArrayList<>();
                order_item_list = new ArrayList<>();
                extractStoreCartItem();
                //Log.d("OrderStatus", order.getOrderStatus());
                root.postDelayed(this, 1000);
            }
        }, 1000);



        btn_remove = root.findViewById(R.id.btn_remove);
        cb_cart_item = root.findViewById(R.id.cb_cart_item);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){

                }
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


    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        float tempTotal = 0;
        for(int i = 0 ; i < order_list.get(position).getOrderItem_list().size() ; i++){
            tempTotal+= order_list.get(position).getOrderItem_list().get(i).getTotalPrice();
        }
        order_list.get(position).setOrderItemTotalPrice(tempTotal);
        bundle.putParcelable("order", order_list.get(position));

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }

    public void extractStoreCartItem(){

        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apicart.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                 for (int i=0; i < response.length(); i++){
                    try {
                        int temp_not = 0;
                        JSONObject jsonObjectCart = response.getJSONObject(i);
                        if (jsonObjectCart.getInt("temp_usersId") == userID) {
                            int c_productId = jsonObjectCart.getInt("temp_productId");
                            int c_storeId = jsonObjectCart.getInt("temp_storeId");
                            int c_usersId = jsonObjectCart.getInt("temp_usersId");
                            String c_productName = jsonObjectCart.getString("temp_productName");
                            double c_productPrice = jsonObjectCart.getDouble("temp_productPrice");
                            int c_productQuantity = jsonObjectCart.getInt("temp_productQuantity");
                            double c_totalProductPrice = jsonObjectCart.getDouble("temp_totalProductPrice");
                            String c_storeName = jsonObjectCart.getString("storeName");
                            String c_storeImage = jsonObjectCart.getString("storeImage");

                            OrderItemModel orderItemModel = new OrderItemModel(c_productId, c_storeId, userID, c_productName, (float) c_productPrice, c_productQuantity,
                                    (float) (c_productPrice * c_productQuantity));

                            Log.d("BEFORE", String.valueOf(userID));
                            Log.d("BEFOREDB", String.valueOf(c_usersId));
                            if(order_list.isEmpty()){
                                Log.d("STOREMATCH", c_productName + " Empty " + c_storeName);
                                order_item_list = new ArrayList<>();
                                order_item_list.add(orderItemModel);
                                order_list.add(new OrderModel((float) c_totalProductPrice,"pending",c_storeId,
                                        c_storeImage,c_storeName,
                                        c_usersId, order_item_list));
                            }else{
                                for (int h = 0; h < order_list.size(); h++) {
                                    Log.d("Inside for", String.valueOf(order_list.size()));
                                    //Check if Order already exist in CartList
                                    if (order_list.get(h).getStore_name().toLowerCase().trim().compareTo(c_storeName.toLowerCase().trim()) == 0) {
                                        Log.d("STOREMATCH", c_productName + " MATCH " + c_storeName);
                                        // Check if order item already exist
                                        for (int k = 0 ; k < order_list.get(h).getOrderItem_list().size() ; k++){
                                            if(c_productName.toLowerCase().trim().compareTo(order_list.get(h).getOrderItem_list().get(k).getProductName().toLowerCase().trim()) == 0){
//                                                temp_count = c_productQuantity;
                                                int tempItemQuantity = 0;
                                                tempItemQuantity = order_list.get(h).getOrderItem_list().get(k).getItemQuantity();
                                                tempItemQuantity += c_productQuantity;
                                                order_list.get(h).getOrderItem_list().get(k).setItemQuantity(tempItemQuantity);
                                                tempItemQuantity = 0;
                                                break;
                                            } else{
                                                order_list.get(h).getOrderItem_list().add(orderItemModel);
                                                break;
                                            }
                                        }
                                    } else{
                                        Log.d("STOREMATCH !0", c_productName + " NOT MATCH " + c_storeName);
                                        temp_not++;
                                        Log.d("Temp_not", String.valueOf(temp_not));
                                        if(temp_not == order_list.size()) {
                                            order_item_list = new ArrayList<>();
                                            order_item_list.add(orderItemModel);
                                            order_list.add(new OrderModel((float) c_totalProductPrice, "pending", c_storeId,
                                                    c_storeImage, c_storeName,
                                                    c_usersId, order_item_list));
                                            Log.d("Added OL", String.valueOf(i));
                                        }
                                    }
                                }
                            }
                        }

                        Log.d("MINE OL SIZE", String.valueOf(order_list.size()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                 Log.d("CartSize", String.valueOf(order_list.size()));
                 cartAdapter = new CartAdapter(getActivity(),order_list, CartFragment.this);
                 rv_cart.setAdapter(cartAdapter);
                 RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                 rv_cart.setLayoutManager(layoutManager);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueCart.add(jsonArrayRequest1);
        Log.d("OUTSIDE LIST", String.valueOf(order_list.size()));

    }
}