package com.example.myapplication.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.ui.foryou.ForYouFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.homepage.HomePageFragment;

public class TabFragmentAdapter extends FragmentStateAdapter {
    public TabFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new HomePageFragment();
        }
        return new ForYouFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
