package com.example.myapplication.adapters;

import android.content.Context;
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
import com.example.myapplication.models.HomeFoodForYouModel;
import com.example.myapplication.models.ProductModel;

import org.w3c.dom.Text;

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
        Glide.with(context)
                .load(list.get(position).getProductImage())
                .into(holder.iv_food_for_you);
        holder.tv_fff_prod_name.setText(list.get(position).getProductName());
        //holder.tv_fff_store_name.setText(list.get(position).getStore_idStore());
        holder.tv_fff_prod_price.setText("P " + list.get(position).getProductPrice());
        //holder.tv_fff_prod_cal.setText(list.get(position).getProduct_calories() + "cal");


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

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            iv_food_for_you = itemView.findViewById(R.id.iv_food_for_you);
            tv_fff_prod_name = itemView.findViewById(R.id.tv_fff_prod_name);
            tv_fff_store_name = itemView.findViewById(R.id.tv_fff_store_name);
            tv_fff_prod_price = itemView.findViewById(R.id.tv_fff_prod_price);
            tv_fff_prod_cal = itemView.findViewById(R.id.tv_fff_prod_cal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickForYou(pos);
                        }
                    }
                }
            });

        }
    }
}
