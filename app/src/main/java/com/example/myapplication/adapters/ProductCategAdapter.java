package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.activities.models.ProductCategModel;

import java.util.List;

public class ProductCategAdapter extends RecyclerView.Adapter<ProductCategAdapter.ViewHolder> {

    Context context;
    List<ProductCategModel> list;
    private final RecyclerViewInterface recyclerViewInterface;

    public ProductCategAdapter(Context context, List<ProductCategModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ProductCategAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductCategAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_categ_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategAdapter.ViewHolder holder, int position) {
        ProductCategModel productCategModel = list.get(position);
        holder.tv_categ.setText(productCategModel.getCateg());

        Log.d("wiw", String.valueOf(list.size()));
        ProductAdapter productAdapter = new ProductAdapter(context,productCategModel.getList(),recyclerViewInterface);
        holder.rv_product.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_categ;
        RecyclerView rv_product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_categ = itemView.findViewById(R.id.tv_categ);
            rv_product = itemView.findViewById(R.id.rv_product);
            rv_product.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}