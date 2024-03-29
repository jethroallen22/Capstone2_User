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

import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.StoreModel;

import java.util.List;

public class HomeStorePopularAdapter extends RecyclerView.Adapter<HomeStorePopularAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    List<StoreModel> list;

    public HomeStorePopularAdapter(List<StoreModel> list, Context context, RecyclerViewInterface recyclerViewInterface) {
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
        holder.iv_store_image.setImageBitmap(list.get(position).getBitmapImage());
        holder.tv_store_name.setText(list.get(position).getStore_name());
        holder.tv_store_category.setText(list.get(position).getStore_category());
        holder.tv_distance.setText(list.get(position).getDistance() + "km away");

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

        TextView tv_distance;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_store_image = itemView.findViewById(R.id.iv_pop_store);
            tv_store_name = itemView.findViewById(R.id.tv_pop_store_name);
            tv_store_category = itemView.findViewById(R.id.tv_pop_store_category);
            card_view = itemView.findViewById(R.id.card_view);
            tv_distance = itemView.findViewById(R.id.tv_distance);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickStorePopular(pos);
                        }
                    }
                }
            });
        }
    }
}
