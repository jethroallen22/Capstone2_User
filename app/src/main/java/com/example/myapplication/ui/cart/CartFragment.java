package com.example.myapplication.ui.cart;

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

import com.example.myapplication.R;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.CartModel;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.ui.order.OrderFragment;
import com.example.myapplication.ui.store.StoreFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements RecyclerViewInterface {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;

    //Cart List Recycler View
    RecyclerView rv_cart;
    List<OrderModel> cart_list;
    CartAdapter cartAdapter;

    Button btn_remove;
    CheckBox cb_cart_item;
    CheckBox checkBox;

    List<OrderModel> temp_order_list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cart_list = new ArrayList<>();

        Bundle bundle = new Bundle();
        cart_list = (List<OrderModel>) getArguments().getSerializable("tempOrderList");
//        Log.d("CART FRAG: ", String.valueOf(temp_order_list.size()));

        rv_cart = root.findViewById(R.id.rv_cart);
//        cart_list = new ArrayList<>();
//        cart_list.add(new CartModel(R.drawable.burger_mcdo,"McDonalds - Binondo", 3, "45", "3.5"));
//        cart_list.add(new CartModel(R.drawable.burger_mcdo,"McDonalds - Abad Santos", 5, "30", "1.5"));

//        for(int i = 0;i < temp_order_list.size(); i++){
//            CartModel cartModel = new CartModel(temp_order_list);
//            cart_list.add(cartModel);
//        }
        cartAdapter = new CartAdapter(getActivity(),cart_list, this);
        rv_cart.setAdapter(cartAdapter);
        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_cart.setHasFixedSize(true);
        rv_cart.setNestedScrollingEnabled(false);

        btn_remove = root.findViewById(R.id.btn_remove);
        checkBox = root.findViewById(R.id.checkBox);
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
    public void onItemClickSearch(int pos) {

    }

    @Override
    public void onItemClick(int position) {
        Log.d("TAG", "Success");
        Bundle bundle = new Bundle();
        //bundle.putString("StoreName", cart_list.get(position).getStore_name());

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(bundle);
        bundle.putSerializable("order_item_list", (Serializable) cart_list.get(position).getOrderItem_list());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
        Log.d("Test","Test success");
    }
}