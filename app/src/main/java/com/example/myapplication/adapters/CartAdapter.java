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
import com.example.myapplication.models.OrderModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<OrderModel> list;
    private CartAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CartAdapter.OnItemClickListener clickListener){
        listener = clickListener;
    }

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
        holder.iv_cart_item_img.setImageBitmap(list.get(position).getBitmapImage());
        holder.tv_cart_store_name.setText(list.get(position).getStore_name() + " " + list.get(position).getOrderItem_list().size());
        holder.tv_cart_item_info.setText(list.get(position).getOrderItem_list().size() + " item/s");
        Log.d("QtyAdapt", String.valueOf(list.get(position).getOrderItem_list().get(0).getProductName()));
        Log.d("CARTADAPT", list.get(position).getStore_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_cart_item_img;
//        CheckBox cb_cart_item;
        TextView tv_cart_store_name;
        TextView tv_cart_item_info;

        ImageView iv_close_btn2;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_cart_item_img = itemView.findViewById(R.id.iv_cart_item_img);
//            cb_cart_item = itemView.findViewById(R.id.cb_voucher);
            tv_cart_store_name = itemView.findViewById(R.id.tv_voucher);
            tv_cart_item_info = itemView.findViewById(R.id.tv_cart_item_info);
            iv_close_btn2 = itemView.findViewById(R.id.iv_close_btn2);

            iv_close_btn2.setOnClickListener(new View.OnClickListener() {
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

//        public void removeItem(){
//            if (cb_cart_item.isChecked()){
//                list.remove(getAdapterPosition());
//                //list.notify(getAdapterPosition());
//            }
//        }
    }
}



