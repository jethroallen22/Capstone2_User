package com.example.myapplication.ui.checkout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCheckout2Binding;
import com.example.myapplication.databinding.FragmentCheckoutBinding;
import com.example.myapplication.models.OrderModel;

public class Checkout2Fragment extends Fragment {

    private FragmentCheckout2Binding binding;
    OrderModel orderModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = this.getArguments();
        if (bundle != null)
            orderModel = bundle.getParcelable("order");

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("order", orderModel);
                Checkout3Fragment fragment = new Checkout3Fragment();
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

}