package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AddonAdapter;
import com.example.myapplication.adapters.ChooseAdapter;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.models.AddonModel;
import com.example.myapplication.models.ChooseModel;
import com.example.myapplication.models.HomeCategoryModel;
import com.example.myapplication.models.HomeStoreRecModel;

import java.util.ArrayList;
import java.util.List;

public class Product extends AppCompatActivity {

    //Choose Recycler View
    RecyclerView rv_choose;
    List<ChooseModel> choose_list;
    ChooseAdapter chooseAdapter;

    //Addon Recyler View
    RecyclerView rv_addon;
    List<AddonModel> addon_list;
    AddonAdapter addonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().hide();

        rv_choose = findViewById(R.id.rv_choose);
        choose_list = new ArrayList<>();

        choose_list.add(new ChooseModel("Coke", 25F));
        choose_list.add(new ChooseModel("Sprite", 25F));
        choose_list.add(new ChooseModel("Royal", 25F));
        choose_list.add(new ChooseModel("Iced Coffee", 25F));
        choose_list.add(new ChooseModel("Water", 25F));

        chooseAdapter = new ChooseAdapter(this,choose_list);
        rv_choose.setAdapter(chooseAdapter);
        rv_choose.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rv_choose.setHasFixedSize(true);
        rv_choose.setNestedScrollingEnabled(false);

        rv_addon = findViewById(R.id.rv_addon);
        addon_list = new ArrayList<>();

        addon_list.add(new AddonModel("Coke", 25F));
        addon_list.add(new AddonModel("Sprite", 25F));
        addon_list.add(new AddonModel("Royal", 25F));
        addon_list.add(new AddonModel("Iced Coffee", 25F));
        addon_list.add(new AddonModel("Water", 25F));

        addonAdapter = new AddonAdapter(this,addon_list);
        rv_addon.setAdapter(addonAdapter);
        rv_addon.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rv_addon.setHasFixedSize(true);
        rv_addon.setNestedScrollingEnabled(false);
    }
}