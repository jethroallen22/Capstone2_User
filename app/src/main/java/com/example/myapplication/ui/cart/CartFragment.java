package com.example.myapplication.ui.cart;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AddressAdapter;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.models.AddressModel;
import com.example.myapplication.models.CartModel;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;

    //Cart List Recycler View
    RecyclerView rv_cart;
    List<CartModel> cart_list;
    CartAdapter cartAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_cart = root.findViewById(R.id.rv_cart);
        cart_list = new ArrayList<>();
        cart_list.add(new CartModel(R.drawable.burger_mcdo,"McDonalds - Binondo", 3, "45", "3.5"));
        cart_list.add(new CartModel(R.drawable.burger_mcdo,"McDonalds - Abad Santos", 5, "30", "1.5"));
        cartAdapter = new CartAdapter(getActivity(),cart_list);
        rv_cart.setAdapter(cartAdapter);
        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_cart.setHasFixedSize(true);
        rv_cart.setNestedScrollingEnabled(false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}