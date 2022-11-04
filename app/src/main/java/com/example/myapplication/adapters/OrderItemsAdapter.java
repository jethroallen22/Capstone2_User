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
import com.example.myapplication.models.OrderItemModel;

import org.w3c.dom.Text;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    Context context;
    List<OrderItemModel> list;

    public OrderItemsAdapter(Context context, List<OrderItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsAdapter.ViewHolder holder, int position) {
        holder.iv_close_btn.setImageResource(R.drawable.ic_baseline_close_24);
        holder.tv_order_item_name.setText(list.get(position).getProduct_name());
        holder.tv_order_item_qty.setText("Qty:" + list.get(position).getQuantity() + "x");
        holder.tv_order_total_price.setText("P " + list.get(position).getTotal_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {
        
        ImageView iv_close_btn;
        TextView tv_order_item_name;
        TextView tv_order_item_qty;
        TextView tv_order_total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            iv_close_btn = itemView.findViewById(R.id.iv_close_btn);
            tv_order_item_name = itemView.findViewById(R.id.tv_order_item_name);
            tv_order_item_qty = itemView.findViewById(R.id.tv_order_item_qty);
            tv_order_total_price = itemView.findViewById(R.id.tv_order_item_total_price);
        }
    }
}
