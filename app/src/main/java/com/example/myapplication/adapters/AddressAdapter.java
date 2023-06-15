package com.example.myapplication.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.models.AddressModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<AddressModel> list;

    public AddressAdapter(Context context, List<AddressModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        holder.iv_address_icon.setImageResource(R.drawable.ic_baseline_location_on_24_teal);
        holder.tv_address_name.setText(list.get(position).getAddress_name());
        holder.tv_address_address.setText(list.get(position).getAddress_address());
        holder.iv_address_edit.setImageResource(R.drawable.ic_baseline_edit_24);
        holder.iv_address_delete.setImageResource(R.drawable.ic_baseline_delete_24);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_address_icon;
        TextView tv_address_name;
        TextView tv_address_address;
        ImageView iv_address_edit;
        ImageView iv_address_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_address_icon = itemView.findViewById(R.id.iv_address_icon);
            tv_address_name = itemView.findViewById(R.id.tv_address_name);
            tv_address_address = itemView.findViewById(R.id.tv_address_address);
            iv_address_edit = itemView.findViewById(R.id.iv_address_edit);
            iv_address_delete = itemView.findViewById(R.id.iv_address_delete);

            iv_address_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Confirm delete").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No",dialogClickListener).show();
                }
            });
        }
    }
}
