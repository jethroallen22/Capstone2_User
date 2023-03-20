package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.ProductCategModel;

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
        holder.tv_categ.setText(list.get(position).getCateg());
        ProductAdapter productAdapter = new ProductAdapter(context,list.get(position).getList(),recyclerViewInterface);
        holder.rv_product.setAdapter(productAdapter);
        holder.rv_product.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.rv_product.setHasFixedSize(true);
        holder.rv_product.setNestedScrollingEnabled(false);
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
        }
    }
}
