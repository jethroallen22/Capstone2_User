package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.HomeStoreRecModel;
import com.example.myapplication.models.StoreModel;

import java.util.List;

public class HomeStoreRecAdapter extends RecyclerView.Adapter<HomeStoreRecAdapter.ViewHolder> {

    Context context;
    List<StoreModel> list;

    public HomeStoreRecAdapter(FragmentActivity context, List<StoreModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeStoreRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_store_rec_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getStore_image())
                .into(holder.iv_store_image);
        holder.tv_store_name.setText(list.get(position).getStore_name());
        holder.tv_store_location.setText(list.get(position).getStore_location());
        holder.tv_store_category.setText(list.get(position).getStore_category());
        holder.tv_store_rating.setText(list.get(position).getStore_rating().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_store_image;
        TextView tv_store_name;
        TextView tv_store_location;
        TextView tv_store_category;
        TextView tv_store_rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_store_image = itemView.findViewById(R.id.iv_food_image);
            tv_store_name = itemView.findViewById(R.id.tv_food_name);
            tv_store_location = itemView.findViewById(R.id.tv_food_price);
            tv_store_category = itemView.findViewById(R.id.tv_store_category);
            tv_store_rating = itemView.findViewById(R.id.tv_store_rating);
        }
    }
}
