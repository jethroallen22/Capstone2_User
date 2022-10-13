package com.example.myapplication.interfaces;

import android.hardware.lights.LightState;

import com.example.myapplication.models.HomeStoreRecModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Retrofit {
    @GET("resto_info")
        Call<List<HomeStoreRecModel>> getDataSet();
}
