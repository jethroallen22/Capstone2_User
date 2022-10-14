package com.example.myapplication.ui.activities;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentActivitiesBinding;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.ui.cart.CartViewModel;

public class ActivitiesFragment extends Fragment {

    private ActivitiesViewModel mViewModel;
    private FragmentActivitiesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ActivitiesViewModel activitiesViewModel =
                new ViewModelProvider(this).get(ActivitiesViewModel.class);

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}