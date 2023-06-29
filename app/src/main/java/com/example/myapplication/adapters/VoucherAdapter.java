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

import java.util.ArrayList;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {

    private Context context;
    private List<VoucherModel> list;
    private final RecyclerViewInterface recyclerViewInterface;
    private OnItemClickListener listener;
    private List<Integer> clickedPositions; // List to store clicked positions

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public VoucherAdapter(Context context, List<VoucherModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
        this.clickedPositions = new ArrayList<>(); // Initialize the clicked positions list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_voucher.setText("Get " + list.get(position).getVoucherAmount() + " Php Discount, for a Minimum Purchase of " + list.get(position).getVoucherMin() + " Php!");

        // Disable button if the position is in the clickedPositions list
        if (clickedPositions.contains(position)) {
            holder.bt_claim.setEnabled(false);
        } else {
            holder.bt_claim.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_voucher;
        private LinearLayout ll_voucher;
        private Button bt_claim;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tv_voucher = itemView.findViewById(R.id.tv_voucher2);
            ll_voucher = itemView.findViewById(R.id.ll_voucher);
            bt_claim = itemView.findViewById(R.id.bt_claim);

            bt_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            if (!clickedPositions.contains(pos)) { // Check if position is not already clicked
                                clickedPositions.add(pos); // Add clicked position to the list
                                bt_claim.setEnabled(false); // Disable the button
                                listener.onItemClick(pos);
                            }
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClickVoucher(pos);
                        }
                    }
                }
            });
        }
    }
}
