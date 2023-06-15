package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.models.ChooseModel;

import java.util.List;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    Context context;
    List<ChooseModel> list;

    public ChooseAdapter(Context context, List<ChooseModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChooseAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseAdapter.ViewHolder holder, int position) {
        holder.tv_choose_name.setText(list.get(position).getChoose_name());
        holder.tv_choose_price.setText("+ P" + list.get(position).getChoose_price().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_choose_name;
        TextView tv_choose_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_choose_name = itemView.findViewById(R.id.tv_choose_name);
            tv_choose_price = itemView.findViewById(R.id.tv_choose_price);
        }
    }
}