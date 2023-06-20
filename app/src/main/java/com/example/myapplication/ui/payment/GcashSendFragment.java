package com.example.myapplication.ui.payment;

import static com.example.myapplication.activities.Home.id;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGcashSendBinding;
import com.example.myapplication.databinding.FragmentSettingsBinding;
public class GcashSendFragment extends Fragment {

    private FragmentGcashSendBinding binding;
    TextView tv_amount, tv_total_amount;
    Button btn_send;
    int userId;
    double wallet;
    float amount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGcashSendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = getArguments();
        if(bundle != null){
            userId = bundle.getInt("id");
            wallet = bundle.getDouble("wallet");
            amount = bundle.getFloat("amount");
            Log.d("Cashin", userId + " " + wallet);
        }

        tv_amount = root.findViewById(R.id.tv_amount);
        tv_total_amount = root.findViewById(R.id.tv_total_amount);
        btn_send = root.findViewById(R.id.btn_send);

        tv_amount.setText("P" + amount);
        tv_total_amount.setText("P" + amount);
        btn_send.setText("Send P" + amount);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putFloat("amount", amount);
                bundle.putDouble("wallet", wallet);
                GcashAuthFragment fragment = new GcashAuthFragment();
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