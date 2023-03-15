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
import com.example.myapplication.models.ActivityModel;
import com.example.myapplication.models.OrderModel;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    Context context;
    List<OrderModel> list;
    private final RecyclerViewInterface recyclerViewInterface;

    public ActivityAdapter(Context context, List<OrderModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item,parent,false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder holder, int position) {
        holder.iv_activity_icon.setImageResource(R.drawable.ic_baseline_fastfood_24);
        holder.tv_activity_name.setText(list.get(position).getStore_name());
        holder.tv_activity_address.setText("ID: " + list.get(position).getIdOrder());
        holder.tv_activity_price.setText("P " + list.get(position).getOrderItemTotalPrice());
        //holder.tv_activity_date.setText(list.get(position).getDate_time());
        /*holder.btn_activity_reorder.setOnClickListener(View view){
            Snackbar.make(view, "Work in Progress!!! Magreredirect dapat sa cart screen", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        };*/
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
        Button btn_activity_reorder;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_activity_icon = itemView.findViewById(R.id.iv_activity_icon);
            tv_activity_name = itemView.findViewById(R.id.tv_activity_name);
            tv_activity_address = itemView.findViewById(R.id.tv_activity_address);
            tv_activity_date = itemView.findViewById(R.id.tv_activity_date);
            tv_activity_price = itemView.findViewById(R.id.tv_activity_price);
            btn_activity_reorder = itemView.findViewById(R.id.btn_activity_reorder);

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
