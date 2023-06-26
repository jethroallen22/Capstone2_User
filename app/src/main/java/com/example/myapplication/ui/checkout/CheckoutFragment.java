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
import com.example.myapplication.databinding.FragmentCheckoutBinding;
import com.example.myapplication.activities.models.OrderModel;

public class CheckoutFragment extends Fragment {

    private FragmentCheckoutBinding binding;
    OrderModel orderModel;

    TextView merchant,id, amount;

    String store_name;
    public CheckoutFragment(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("order", orderModel);
                Checkout2Fragment fragment = new Checkout2Fragment();
                fragment.setArguments(bundle);
                store_name = orderModel.getStore_name();
                Log.d("C1StoreName",store_name);
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