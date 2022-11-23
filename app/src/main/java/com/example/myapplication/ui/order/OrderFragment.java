package com.example.myapplication.ui.order;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.OrderItemsAdapter;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.ui.settings.SettingsViewModel;

import org.w3c.dom.Text;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    //
    RecyclerView rv_order_items;
    List<OrderItemModel> order_item_list;
    OrderItemsAdapter orderItemsAdapter;

    private OrderViewModel mViewModel;
    private FragmentOrderBinding binding;

    TextView tv_store_name;
    TextView tv_total_price;
    Button btn_place_order;
    private String store_name;
    private int i;
    private float total_price;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrderViewModel orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tv_store_name = root.findViewById(R.id.tv_store_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        btn_place_order = root.findViewById(R.id.btn_place_order);
        Bundle bundle = this.getArguments();

        if (bundle != null){
            store_name = bundle.getString("StoreName");
            tv_store_name.setText(store_name);
            Log.d("StoreName",store_name);
        }

        rv_order_items = root.findViewById(R.id.rv_order_items);
        order_item_list = new ArrayList<>();
//        order_item_list.add(new OrderItemModel("Burger Mcdo", 3, 135F));
//        order_item_list.add(new OrderItemModel("Chicken Ala King", 2, 215F));
        orderItemsAdapter = new OrderItemsAdapter(getActivity(),order_item_list);
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        for (i = 0; i < order_item_list.size(); i++)
            total_price += order_item_list.get(i).getItemPrice();
        Log.d("TotalPrice", String.valueOf(total_price));
        tv_total_price.setText(String.valueOf(total_price));

//        btn_place_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OrderModel order = new OrderModel();
//                order.setName(store_name);
//                order.setDistance("4km");
//                order.setOrderItem_list(order_item_list);
//                order.setItem_count(order_item_list.size());
//                order.setTotal(total_price);
//                order.setTime(String.valueOf(LocalDateTime.now()));
//                Log.d("Order", String.valueOf(order));
//            }
//        });

        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}