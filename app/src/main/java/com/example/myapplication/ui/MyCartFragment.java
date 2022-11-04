package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.models.CartModel;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    List<CartModel> list;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;

    public MyCartFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){

        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        /*recyclerView = view.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        list.add(new CartModel(R.drawable.s1,"OrderModel 1","30","4.3"));
        list.add(new CartModel(R.drawable.s2,"OrderModel 1","$20","4.3"));
        list.add(new CartModel(R.drawable.fav1,"OrderModel 1","40","4.3"));

        list.add(new CartModel(R.drawable.s1,"OrderModel 1","$30","4.3"));
        list.add(new CartModel(R.drawable.s2,"OrderModel 1","$20","4.3"));
        list.add(new CartModel(R.drawable.fav1,"OrderModel 1","30","4.3"));*/

        //cartAdapter = new CartAdapter(list);

        return view;
    }
}
