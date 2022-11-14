package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.CartModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<CartModel> list;

    public CartAdapter(Context context, List<CartModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false), recyclerViewInterface);
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
        CheckBox cb_cart_item;
        TextView tv_cart_store_name;
        TextView tv_cart_item_info;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_cart_item_img = itemView.findViewById(R.id.iv_cart_item_img);
            cb_cart_item = itemView.findViewById(R.id.cb_cart_item);
            tv_cart_store_name = itemView.findViewById(R.id.tv_cart_store_name);
            tv_cart_item_info = itemView.findViewById(R.id.tv_cart_item_info);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });


        }

        public void removeItem(){
            if (cb_cart_item.isChecked()){
                list.remove(getAdapterPosition());
                //list.notify(getAdapterPosition());
            }
        }
    }
}



