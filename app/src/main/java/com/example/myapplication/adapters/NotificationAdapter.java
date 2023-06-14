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
import com.example.myapplication.activities.models.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context context;
    List<NotificationModel> list;

    public NotificationAdapter(Context context, List<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        holder.iv_notif_img.setImageResource(R.drawable.ic_baseline_money_24);
        holder.tv_notif_description.setText(list.get(position).getDescription());
        holder.tv_notif_title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_notif_img;
        TextView tv_notif_description;
        TextView tv_notif_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_notif_img = itemView.findViewById(R.id.iv_notif_img);
            tv_notif_description = itemView.findViewById(R.id.tv_notif_description);
            tv_notif_title = itemView.findViewById(R.id.tv_notif_title);
        }
    }
}
