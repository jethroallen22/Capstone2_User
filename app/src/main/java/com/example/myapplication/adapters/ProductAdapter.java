package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.HomeFoodForYouModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<HomeFoodForYouModel> list;

    public ProductAdapter(Context context, List<HomeFoodForYouModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.iv_product_image.setImageResource(list.get(position).getImage());
        holder.tv_product_name.setText(list.get(position).getProduct_name());
        holder.tv_product_price.setText(list.get(position).getProduct_price().toString());
        holder.tv_product_cal.setText(list.get(position).getProduct_calories() + "cal");


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_product_image;
        TextView tv_product_name;
        TextView tv_product_price;
        TextView tv_product_cal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            tv_product_cal = itemView.findViewById(R.id.tv_product_cal);

        }
    }
}
