package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
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
import com.example.myapplication.models.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<ProductModel> list;
    private final RecyclerViewInterface recyclerViewInterface;

    public ProductAdapter(Context context, List<ProductModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductModel productModel = list.get(position);
        holder.iv_product_image.setImageBitmap(productModel.getBitmapImage());
        holder.tv_product_name.setText(productModel.getProductName());
        holder.tv_product_price.setText(String.valueOf(productModel.getProductPrice()));
        //holder.tv_product_cal.setText(productModel.getProductTag());
        Log.d("witwiw", productModel.getPercentage() + "%");
        if(list.get(position).getStock() != null) {
            if (list.get(position).getStock().equals("stocked")) {
                holder.cv_stock.setVisibility(View.INVISIBLE);
            } else {
                holder.cv_stock.setVisibility(View.VISIBLE);
            }
        }
        if(productModel.getPercentage() != 0) {
            holder.cv_banner.setVisibility(View.VISIBLE);
            holder.tv_deal_percentage.setText(productModel.getPercentage() + "% off");
        } else
            holder.cv_banner.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_product_image;
        TextView tv_product_name;
        TextView tv_product_price;
        TextView tv_product_cal;

        TextView tv_deal_percentage;

        CardView cv_banner;
        CardView cv_stock;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            tv_product_cal = itemView.findViewById(R.id.tv_product_cal);
            cv_banner = itemView.findViewById(R.id.cv_banner);
            tv_deal_percentage = itemView.findViewById(R.id.tv_deal_percentage);
            cv_stock = itemView.findViewById(R.id.cv_stock);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            if (list.get(pos).getStock()!=null && list.get(pos).getStock().equals("stocked"))
                                recyclerViewInterface.onItemClickCategory(pos);
                        }
                    }
                }
            });

        }
    }
}
