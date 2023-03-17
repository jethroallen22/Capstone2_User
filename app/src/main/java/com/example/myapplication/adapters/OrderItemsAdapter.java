package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.OrderItemModel;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    List<OrderItemModel> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public OrderItemsAdapter(Context context, List<OrderItemModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public OrderItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartorder_item,parent,false), recyclerViewInterface, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsAdapter.ViewHolder holder, int position) {
        holder.tv_order_item_name.setText(list.get(position).getProductName());
        holder.tv_order_item_qty.setText("Qty:" + list.get(position).getItemQuantity() + "x");
        Log.d("QTY: " , String.valueOf(list.get(position).getItemQuantity()));
        holder.tv_order_total_price.setText("P " + list.get(position).getTotalPrice());
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

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, OnItemClickListener listener) {
            super(itemView);
            
            iv_close_btn = itemView.findViewById(R.id.iv_close_btn);
            tv_order_item_name = itemView.findViewById(R.id.tv_order_item_name);
            tv_order_item_qty = itemView.findViewById(R.id.tv_order_item_qty);
            tv_order_total_price = itemView.findViewById(R.id.tv_order_item_total_price);


            iv_close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
