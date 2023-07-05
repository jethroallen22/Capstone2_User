package com.example.myapplication.ui.checkout;

import static com.example.myapplication.activities.Home.id;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCheckoutBinding;
import com.example.myapplication.activities.models.OrderModel;
import com.example.myapplication.ui.payment.GcashSendFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CheckoutFragment extends Fragment {

    private FragmentCheckoutBinding binding;
    OrderModel orderModel;

    String store_name;

    LinearLayout ll_biometrics;
    private Handler handler;
    ImageView iv_fingerprint;
    int userId;
    float wallet;
    float amount;
    String dateTimeString;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = getArguments();
        if(bundle != null){
            userId = bundle.getInt("id");
            orderModel = bundle.getParcelable("order");
            wallet = bundle.getFloat("wallet");
            amount = bundle.getFloat("amount");
            dateTimeString = bundle.getString("datetime");
            Log.d("Checkout1" , "wallet: " + wallet);
            Log.d("Checkout1" , "amount: " + amount);
            Log.d("Checkout1" , "datetime: " + dateTimeString);
            Log.d("Cashin", userId + " " + wallet);
        }

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
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
                bundle.putFloat("wallet", wallet);
                bundle.putParcelable("order", orderModel);
                bundle.putString("datetime", dateTimeString);
                Checkout2Fragment fragment = new Checkout2Fragment();
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