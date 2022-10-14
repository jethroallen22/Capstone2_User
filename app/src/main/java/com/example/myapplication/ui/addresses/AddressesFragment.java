package com.example.myapplication.ui.addresses;

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
import com.example.myapplication.databinding.FragmentAddressesBinding;
import com.example.myapplication.ui.activities.ActivitiesViewModel;

public class AddressesFragment extends Fragment {

    private AddressesViewModel mViewModel;
    private FragmentAddressesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddressesViewModel addressesViewModel =
                new ViewModelProvider(this).get(AddressesViewModel.class);

        binding = FragmentAddressesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}