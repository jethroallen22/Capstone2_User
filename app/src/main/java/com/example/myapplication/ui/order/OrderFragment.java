package com.example.myapplication.ui.order;

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
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.ui.settings.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    //
    RecyclerView rv_order_items;
    List<OrderItemModel> order_item_list;
    OrderItemsAdapter orderItemsAdapter;

    private OrderViewModel mViewModel;
    private FragmentOrderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrderViewModel orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_order_items = root.findViewById(R.id.rv_order_items);
        order_item_list = new ArrayList<>();
        order_item_list.add(new OrderItemModel("Burger Mcdo", 3, 135F));
        order_item_list.add(new OrderItemModel("Chicken Ala King", 2, 215F));
        orderItemsAdapter = new OrderItemsAdapter(getActivity(),order_item_list);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}