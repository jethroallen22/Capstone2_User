package com.example.myapplication.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.ChooseModel;
import com.example.myapplication.models.VoucherModel;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {

    Context context;
    List<VoucherModel> list;

    public VoucherAdapter(Context context, List<VoucherModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ViewHolder holder, int position) {
        holder.tv_voucher.setText("Get " + list.get(position).getVoucherAmount() +" Php Discount, for a Minimun Purchase of " + list.get(position).getVoucherMin() +
                " Php!");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_voucher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_voucher = itemView.findViewById(R.id.tv_voucher);

        }
    }
}
