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
import com.example.myapplication.models.ActivityModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    Context context;
    List<ActivityModel> list;

    public ActivityAdapter(Context context, List<ActivityModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder holder, int position) {
        holder.iv_activity_icon.setImageResource(R.drawable.ic_baseline_fastfood_24);
        holder.tv_activity_name.setText(list.get(position).getStore_name());
        holder.tv_activity_address.setText(list.get(position).getStore_address());
        holder.tv_activity_price.setText("P " + list.get(position).getPrice().toString());
        holder.tv_activity_date.setText(list.get(position).getDate_time());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_activity_icon = itemView.findViewById(R.id.iv_activity_icon);
            tv_activity_name = itemView.findViewById(R.id.tv_activity_name);
            tv_activity_address = itemView.findViewById(R.id.tv_activity_address);
            tv_activity_date = itemView.findViewById(R.id.tv_activity_date);
            tv_activity_price = itemView.findViewById(R.id.tv_activity_price);
            btn_activity_reorder = itemView.findViewById(R.id.btn_activity_reorder);
        }
    }
}
