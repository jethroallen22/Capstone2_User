package com.example.myapplication.ui.checkout;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myapplication.activities.Preferences;
import com.example.myapplication.databinding.FragmentCheckout3Binding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.activities.models.OrderModel;
import com.example.myapplication.ui.ordersummary.OrderSummaryFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout3Fragment extends Fragment{

    private FragmentCheckout3Binding binding;
    OrderModel orderModel;

    TextView merchant,id, amount, total;

    String store_name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        amount = root.findViewById(R.id.tv_amount3);
        merchant = root.findViewById(R.id.tv_merchantname);
        total = root.findViewById(R.id.tv_total_amount);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            orderModel = bundle.getParcelable("order");
            store_name = orderModel.getStore_name();
            Log.d("C3StoreName",store_name);
            merchant.setText(store_name);
            //merchant.setText(orderModel.getStore_name());
            amount.setText(String.valueOf(orderModel.getOrderItemTotalPrice()));
            total.setText(String.valueOf(orderModel.getOrderItemTotalPrice()));
        }


        binding.nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("order", orderModel);
                Checkout4Fragment fragment = new Checkout4Fragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}