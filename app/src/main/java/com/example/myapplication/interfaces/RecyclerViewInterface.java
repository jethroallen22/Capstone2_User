package com.example.myapplication.interfaces;

public interface RecyclerViewInterface {
    void onItemClickForYou(int position);
    void onItemClickStorePopular(int position);
    void onItemClick(int position);
    void onItemClickStoreRec(int position);
    void onItemClickStoreRec2(int position);

    void onItemClickSearch(int position);
    void onItemClickWeather(int position);
    void onItemClickCategory(int position);

    void onItemClickDeals(int pos);
}
