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
import com.example.myapplication.activities.models.MethodModel;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder>{

    Context context;
    List<MethodModel> list;

    public PaymentMethodAdapter(Context context, List<MethodModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentMethodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentMethodAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.method_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getName());
        holder.iv_logo.setImageResource(list.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_logo;
        TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_logo = itemView.findViewById(R.id.iv_transac_icon);
            tv_name = itemView.findViewById(R.id.tv_name);

        }
    }
}
