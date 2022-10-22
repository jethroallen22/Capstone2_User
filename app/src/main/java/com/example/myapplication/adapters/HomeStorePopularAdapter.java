package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.StoreModel;

import java.util.List;

public class HomeStorePopularAdapter extends RecyclerView.Adapter<HomeStorePopularAdapter.ViewHolder> {

    Context context;
    List<StoreModel> list;
    private final RecyclerViewInterface recyclerViewInterface;

    public HomeStorePopularAdapter(Context context, List<StoreModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }



    @NonNull
    @Override
    public HomeStorePopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_store_popular_item,parent,false),recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeStorePopularAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(list.get(position).getStore_image())
                .into(holder.iv_store_image);
        holder.tv_store_name.setText(list.get(position).getStore_name());
        holder.tv_store_category.setText(list.get(position).getStore_category());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_store_image;
        TextView tv_store_name;
        TextView tv_store_category;
        CardView card_view;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_store_image = itemView.findViewById(R.id.iv_pop_store);
            tv_store_name = itemView.findViewById(R.id.tv_pop_store_name);
            tv_store_category = itemView.findViewById(R.id.tv_pop_store_category);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
