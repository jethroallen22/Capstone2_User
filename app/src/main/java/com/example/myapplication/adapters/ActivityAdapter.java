package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.OrderModel;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    Context context;
    List<OrderModel> list;
    private final RecyclerViewInterface recyclerViewInterface;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemClickFeedback(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public ActivityAdapter(Context context, List<OrderModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item,parent,false), recyclerViewInterface, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder holder, int position) {
        holder.iv_activity_icon.setImageResource(R.drawable.ic_baseline_fastfood_24);
        holder.tv_activity_name.setText(list.get(position).getStore_name());
        holder.tv_activity_address.setText("ID: " + list.get(position).getIdOrder());
        holder.tv_activity_price.setText("P " + list.get(position).getOrderItemTotalPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_activity_icon;
        TextView tv_activity_name;
        TextView tv_activity_address;
        TextView tv_activity_date;
        TextView tv_activity_price;
        Button btn_activity_reorder, btn_feedback;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, OnItemClickListener listener) {
            super(itemView);

            iv_activity_icon = itemView.findViewById(R.id.iv_activity_icon);
            tv_activity_name = itemView.findViewById(R.id.tv_activity_name);
            tv_activity_address = itemView.findViewById(R.id.tv_activity_address);
            tv_activity_date = itemView.findViewById(R.id.tv_activity_date);
            tv_activity_price = itemView.findViewById(R.id.tv_activity_price);
            btn_activity_reorder = itemView.findViewById(R.id.btn_activity_reorder);
            btn_feedback = itemView.findViewById(R.id.btn_feedback);

            btn_activity_reorder.setOnClickListener(new View.OnClickListener() {
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

            btn_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            listener.onItemClickFeedback(pos);
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
    }
}
