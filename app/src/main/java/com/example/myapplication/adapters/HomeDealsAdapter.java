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
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.DealsModel;

import java.util.List;

public class HomeDealsAdapter extends RecyclerView.Adapter<HomeDealsAdapter.ViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<DealsModel> list;

    public HomeDealsAdapter(Context context, List<DealsModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public HomeDealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeDealsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_deals,parent,false),recyclerViewInterface);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDealsAdapter.ViewHolder holder, int position) {
        holder.iv_deal_store.setImageBitmap(list.get(position).getBitmapImage());
        holder.tv_deal_store_name.setText(list.get(position).getStoreName());
        holder.tv_deal_description.setText(list.get(position).getStoreCategory());
        holder.tv_deal_percentage.setText(list.get(position).getPercentage() + "% off");
        holder.tv_distance3.setText(list.get(position).getDistance() + "km away");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_deal_store;
        TextView tv_deal_store_name;
        TextView tv_deal_description;
        TextView tv_deal_percentage;

        TextView tv_distance3;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_deal_store = itemView.findViewById(R.id.iv_deal_store);
            tv_deal_store_name = itemView.findViewById(R.id.tv_deal_store_name);
            tv_deal_description = itemView.findViewById(R.id.tv_deal_store_category);
            tv_deal_percentage = itemView.findViewById(R.id.tv_deal_percentage);
            tv_distance3 = itemView.findViewById(R.id.tv_distance3);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickDeals(pos);
                        }
                    }
                }
            });
        }
    }
}
