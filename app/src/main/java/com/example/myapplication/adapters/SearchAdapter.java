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
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.models.TagModel;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.activities.models.SearchModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private final Context context;
    private final List<SearchModel> list;
    private final int recyclerViewId;

    public SearchAdapter(Context context, List<SearchModel> list, RecyclerViewInterface recyclerViewInterface, int recyclerViewId) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerViewId = recyclerViewId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false), recyclerViewInterface, recyclerViewId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("search", "Name: " + list.get(position).getSearchName());
        if (list.get(position).getTagModelList() != null)
            Log.d("search", "ListSize: " + list.get(position).getTagModelList().size());

        holder.iv_search_item_img.setImageBitmap(list.get(position).getBitmapImage());
        holder.tv_search_item_name.setText(list.get(position).getSearchName());
        if (list.get(position).getTagModelList() == null){
            Log.d("search", "TAG: null" );
            holder.cg_tags.setVisibility(View.INVISIBLE);
        } else {
            holder.cg_tags.setVisibility(View.VISIBLE);
            for (TagModel tagModel : list.get(position).getTagModelList()) {
                Log.d("search", "TAG: " + tagModel.getTagname());
                Chip chip = new Chip(context);
                chip.setText(tagModel.getTagname());
                chip.setEnabled(false);
//                chip.setHeight(20);
                chip.setTextSize(12);
                chip.setChipBackgroundColorResource(R.color.gray);
                chip.setTextColor(Color.BLACK);
                holder.cg_tags.addView(chip);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_search_item_img;
        TextView tv_search_item_name;
        ChipGroup cg_tags;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, int recyclerViewId) {
            super(itemView);

            iv_search_item_img = itemView.findViewById(R.id.iv_transac_icon);
            tv_search_item_name = itemView.findViewById(R.id.tv_name);
            cg_tags = itemView.findViewById(R.id.cg_tags);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClickSearch(pos, recyclerViewId);
                        }
                    }
                }
            });
        }
    }
}
