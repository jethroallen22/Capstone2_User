package com.example.myapplication.ui.checkout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCheckout3Binding;
import com.example.myapplication.models.OrderModel;

public class Checkout3Fragment extends Fragment{

    private FragmentCheckout3Binding binding;
    OrderModel orderModel;
    TextView merchant,id, amount, total;
    String store_name;
    float wallet;
    String dateTimeString;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        amount = root.findViewById(R.id.tv_amount3);
        merchant = root.findViewById(R.id.tv_merchantname);
        total = root.findViewById(R.id.tv_total_amount);

        Bundle bundle = getArguments();
        if (bundle != null){
            orderModel = bundle.getParcelable("order");
            store_name = orderModel.getStore_name();
            wallet = bundle.getFloat("wallet");
            dateTimeString = bundle.getString("datetime");
            Log.d("Checkout3" , "wallet: " + wallet);
            Log.d("Checkout3" , "amount: " + amount);
            Log.d("Checkout3" , "datetime: " + dateTimeString);
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
                bundle.putFloat("wallet", wallet);
                bundle.putString("datetime", dateTimeString);
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