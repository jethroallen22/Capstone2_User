package com.example.myapplication.ui.payment;

import static com.example.myapplication.activities.Home.id;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCashInBinding;
import com.example.myapplication.models.IPModel;

public class CashInFragment extends Fragment {

    private FragmentCashInBinding binding;
    //School IP
    private static String JSON_URL;
    private IPModel ipModel;
    int userId;
    double wallet, temp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        binding = FragmentCashInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if(bundle != null){
            userId = bundle.getInt("id");
            wallet = bundle.getDouble("wallet");
            Log.d("Cashin", userId + " " + wallet);
        }

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putFloat("amount", Float.parseFloat(String.valueOf(binding.etAmount.getText())));
                bundle.putInt("id", id);
                bundle.putDouble("wallet", wallet);
                GcashLoginFragment fragment = new GcashLoginFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}