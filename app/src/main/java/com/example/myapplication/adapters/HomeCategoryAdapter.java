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

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.HomeCategoryModel;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    List<HomeCategoryModel> list;

    public HomeCategoryAdapter(Context context, List<HomeCategoryModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
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
        Glide.with(context)
                .load(list.get(position).getCateg_image())
                .into(holder.iv_categ_icon);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickCategory(pos);
                        }
                    }
                }
            });
        }
    }
}
