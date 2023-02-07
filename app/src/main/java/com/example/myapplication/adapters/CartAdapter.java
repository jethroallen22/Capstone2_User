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

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.CartModel;
import com.example.myapplication.models.OrderModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<OrderModel> list;

    public CartAdapter(Context context, List<OrderModel> list, RecyclerViewInterface recyclerViewInterface) {
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

//        if(list.size() != 0) {
//            int i, j = 0;
//            int total_quantity = 0;
//            Log.d("CART ADAPTER: ", String.valueOf(list.size()));
//            Log.d("CART ADAPTER: ", String.valueOf(position));
//
//            for (i = 0; i < list.get(position).getOrder_list().size(); i++) {
//                Log.d("INSIDE FOR LOOP", String.valueOf(i));
//                for (j=0; j<list.get(position).getOrder_list().get(i).getOrderItem_list().size();j++){
//                    int temp_quantity = 0;
//                    temp_quantity = list.get(position).getOrder_list().get(i).getOrderItem_list().get(j).getItemQuantity();
//
//                    total_quantity = temp_quantity + total_quantity;
//
//                }
//                Glide.with(context)
//                        .load(list.get(position).getOrder_list().get(i).getStore_image())
//                        .into(holder.iv_cart_item_img);
//                holder.tv_cart_store_name.setText(list.get(position).getOrder_list().get(i).getStore_name());
//                holder.tv_cart_item_info.setText(total_quantity + " items");
//
//            }
////            holder.tv_order_info.setText(text);
////            holder.tv_order_item_info.setText("Qty: " + list.get(position).getOrderItem_list().size());
//        }
        Glide.with(context)
                .load(list.get(position).getStore_image())
                .into(holder.iv_cart_item_img);
        holder.tv_cart_store_name.setText(list.get(position).getStore_name());
        holder.tv_cart_item_info.setText(list.get(position).getOrderItem_list().size() + " items");


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



