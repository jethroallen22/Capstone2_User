package com.example.myapplication.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.CartModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartModel> list;

    public CartAdapter(Context context, List<CartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_cart_item_img.setImageResource(list.get(position).getStore_image());
        holder.tv_cart_store_name.setText(list.get(position).getStore_name());
        holder.tv_cart_item_info.setText(list.get(position).getItem_count() + " items ● " +
                list.get(position).getTime() + "min ● " +
                list.get(position).getDistance() + "km");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_cart_item_img;
        //CheckBox cb_cart_item;
        TextView tv_cart_store_name;
        TextView tv_cart_item_info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_cart_item_img = itemView.findViewById(R.id.iv_cart_item_img);
            //cb_cart_item = itemView.findViewById(R.id.cb_cart_item);
            tv_cart_store_name = itemView.findViewById(R.id.tv_cart_store_name);
            tv_cart_item_info = itemView.findViewById(R.id.tv_cart_item_info);

        }
    }
}



