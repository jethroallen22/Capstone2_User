package com.example.myapplication.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.example.myapplication.activities.models.ProductModel;
import com.example.myapplication.activities.models.TagModel;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    Context context;
    List<ProductModel> list;
    private final RecyclerViewInterface recyclerViewInterface;

    public FilterAdapter(Context context, List<ProductModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilterAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        ProductModel productModel = list.get(position);
        holder.iv_product_image.setImageBitmap(productModel.getBitmapImage());
        holder.tv_product_name.setText(productModel.getProductName());
        holder.tv_product_price.setText(String.valueOf(productModel.getProductPrice()));
//        holder.tv_product_cal.setText(productModel.getProductTag());
        Log.d("witwiw", productModel.getPercentage() + "%");
        if(productModel.getPercentage() != 0) {
            holder.cv_banner.setVisibility(View.VISIBLE);
            holder.tv_deal_percentage.setText(productModel.getPercentage() + "% off");
        } else
            holder.cv_banner.setVisibility(View.INVISIBLE);
        if (list.get(position).getTags_list() == null){
            holder.cg_tags.setVisibility(View.INVISIBLE);
        } else {
            holder.cg_tags.setVisibility(View.VISIBLE);
            for (TagModel tagModel : list.get(position).getTags_list()) {
                if(tagModel.isMatch()){
                    Chip chip = new Chip(context);
                    chip.setText(tagModel.getTagname());
                    chip.setEnabled(false);
                    chip.setHeight(35);
                    chip.setTextSize(12);
                    chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chip.setTextColor(Color.BLACK);
                    holder.cg_tags.addView(chip);
                }
            }
        }

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

        ChipGroup cg_tags;

        CardView cv_banner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            //tv_product_cal = itemView.findViewById(R.id.tv_product_cal);
            cv_banner = itemView.findViewById(R.id.cv_banner);
            tv_deal_percentage = itemView.findViewById(R.id.tv_deal_percentage);
            cg_tags = itemView.findViewById(R.id.cg_tags);

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
