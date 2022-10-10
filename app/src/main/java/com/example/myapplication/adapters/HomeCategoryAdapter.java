package com.example.myapplication.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.HomeCategoryModel;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    Context context;
    List<HomeCategoryModel> list;

    public HomeCategoryAdapter(Context context, List<HomeCategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryAdapter.ViewHolder holder, int position) {
        holder.iv_categ_icon.setImageResource(list.get(position).getImage());
        holder.tv_categ_name.setText(list.get(position).getCateg_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_categ_icon;
        TextView tv_categ_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_categ_icon = itemView.findViewById(R.id.iv_categ_icon);
            tv_categ_name = itemView.findViewById(R.id.tv_categ_name);
        }
    }
}
