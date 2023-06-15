package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import com.example.myapplication.activities.models.VoucherModel;
import com.example.myapplication.interfaces.RecyclerViewInterface;


import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {

    Context context;
    List<VoucherModel> list;
    private final RecyclerViewInterface recyclerViewInterface;

    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public VoucherAdapter(Context context, List<VoucherModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item,parent,false),recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ViewHolder holder, int position) {
        holder.tv_voucher.setText("Get " + list.get(position).getVoucherAmount() +" Php Discount, for a Minimum Purchase of " + list.get(position).getVoucherMin() +
                " Php!");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_voucher;
        LinearLayout ll_voucher;

        Button bt_claim;

        public ViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tv_voucher = itemView.findViewById(R.id.tv_voucher2);
            ll_voucher = itemView.findViewById(R.id.ll_voucher);
            bt_claim = itemView.findViewById(R.id.bt_claim);

            bt_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickVoucher(pos);
                        }
                    }
                }
            });

        }

    }
}
