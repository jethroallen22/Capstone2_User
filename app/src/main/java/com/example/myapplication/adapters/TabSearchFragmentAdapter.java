package com.example.myapplication.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.ui.foryou.ForYouFragment;
import com.example.myapplication.ui.homepage.HomePageFragment;
import com.example.myapplication.ui.search.SearchAllFragment;
import com.example.myapplication.ui.search.SearchProductFragment;
import com.example.myapplication.ui.search.SearchStoreFragment;

public class TabSearchFragmentAdapter extends FragmentStateAdapter {
    public TabSearchFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new SearchProductFragment();
        } else if (position == 2){
            return new SearchStoreFragment();
        } else
        return new SearchAllFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}