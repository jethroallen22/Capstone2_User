package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.AddonModel;

import java.util.List;

public class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.ViewHolder> {

    Context context;
    List<AddonModel> list;
    public AddonAdapter(Context context, List<AddonModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddonAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.addon_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddonAdapter.ViewHolder holder, int position) {
        holder.tv_addon_name.setText(list.get(position).getAddon_name());
        holder.tv_addon_price.setText("+ P" + list.get(position).getAddon_price().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_addon_name;
        TextView tv_addon_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_addon_name = itemView.findViewById(R.id.tv_addon_name);
            tv_addon_price = itemView.findViewById(R.id.tv_addon_price);
        }
    }
}
