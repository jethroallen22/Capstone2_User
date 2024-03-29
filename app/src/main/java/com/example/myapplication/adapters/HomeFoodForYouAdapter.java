package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.ProductModel;

import java.util.List;

public class HomeFoodForYouAdapter extends RecyclerView.Adapter<HomeFoodForYouAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    List<ProductModel> list;

    public HomeFoodForYouAdapter(Context context, List<ProductModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public HomeFoodForYouAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_food_for_you_item,parent,false),recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFoodForYouAdapter.ViewHolder holder, int position) {
        holder.iv_food_for_you.setImageBitmap(list.get(position).getBitmapImage());
        holder.tv_fff_prod_name.setText(list.get(position).getProductName());
        holder.tv_fff_store_name.setText(list.get(position).getProductRestoName());
        holder.tv_fff_prod_price.setText("P " + list.get(position).getProductPrice());
        if(list.get(position).getStock() != null) {
            if (list.get(position).getStock().equals("stocked")) {
                holder.cl_stock.setVisibility(View.INVISIBLE);
            } else {
                holder.cl_stock.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_food_for_you;
        TextView tv_fff_prod_name;
        TextView tv_fff_store_name;
        TextView tv_fff_prod_price;
        TextView tv_fff_prod_cal;
        ConstraintLayout cl_stock;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_food_for_you = itemView.findViewById(R.id.iv_food_for_you);
            tv_fff_prod_name = itemView.findViewById(R.id.tv_fff_prod_name);
            tv_fff_store_name = itemView.findViewById(R.id.tv_fff_store_name);
            tv_fff_prod_price = itemView.findViewById(R.id.tv_fff_prod_price);
            //tv_fff_prod_cal = itemView.findViewById(R.id.tv_fff_prod_cal);
            cl_stock = itemView.findViewById(R.id.cl_stock);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            if(list.get(pos).getStock().equals("stocked"))
                                recyclerViewInterface.onItemClickForYou(pos);
                        }
                    }
                }
            });

        }
    }
}
