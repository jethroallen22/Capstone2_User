package com.example.myapplication.ui.payment;

import static com.example.myapplication.activities.Home.id;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGcashLoginBinding;
import com.example.myapplication.databinding.FragmentSettingsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GcashLoginFragment extends Fragment {

    LinearLayout ll_biometrics;
    private Handler handler;
    ImageView iv_fingerprint;
    int userId;
    double wallet;
    float amount;


    private FragmentGcashLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGcashLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = getArguments();
        if(bundle != null){
            userId = bundle.getInt("id");
            wallet = bundle.getDouble("wallet");
            amount = bundle.getFloat("amount");
            Log.d("Cashin", userId + " " + wallet);
        }

        ll_biometrics = root.findViewById(R.id.ll_biometrics);
        ll_biometrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        return root;
    }

    public void showBottomSheet(){
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.biometrics_bottom_sheet_layout,
                        getActivity().findViewById(R.id.biometrics_bottomSheet_container)
                );
        iv_fingerprint = bottomSheetView.findViewById(R.id.iv_fingerprint);
        iv_fingerprint.setBackgroundResource(R.drawable.baseline_fingerprint_24_gray);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putFloat("amount", amount);
                bundle.putDouble("wallet", wallet);
                GcashSendFragment fragment = new GcashSendFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        }, 3000);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}