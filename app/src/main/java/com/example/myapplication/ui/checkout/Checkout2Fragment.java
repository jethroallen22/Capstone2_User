package com.example.myapplication.ui.checkout;

import static androidx.core.content.ContextCompat.getSystemService;

import static com.example.myapplication.activities.Home.id;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCheckout2Binding;
import com.example.myapplication.activities.models.OrderModel;
import com.example.myapplication.ui.payment.GcashSendFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Checkout2Fragment extends Fragment {

    private FragmentCheckout2Binding binding;
    OrderModel orderModel;
    int[] randomNumbers;
    NotificationManager manager;
    List<Integer> randomInt;
    float wallet;
    String dateTimeString;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = getArguments();

        if (bundle != null){
            orderModel = bundle.getParcelable("order");
            wallet = bundle.getFloat("wallet");
            dateTimeString = bundle.getString("datetime");
            Log.d("Checkout2" , "wallet: " + wallet);
            Log.d("Checkout2" , "datetime: " + dateTimeString);
        }
        randomInt = new ArrayList<>();
        randomInt.add(0);
        randomInt.add(1);
        randomInt.add(2);
        randomInt.add(3);
        randomInt.add(4);
        randomInt.add(5);
        randomInt.add(6);
        randomInt.add(7);
        randomInt.add(8);
        randomInt.add(9);

        String numbersText = "";
        for (int i = 0; i < 6; i++) {
            Collections.shuffle(randomInt);
            numbersText += randomInt.get(0);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "My Notification");
        builder.setContentTitle("GCash");
        builder.setContentText( numbersText + "is your authentication code. For your protection, do not share this code with anyone.");
        builder.setSmallIcon(R.drawable.gcash);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity().getApplicationContext());
        managerCompat.notify(1, builder.build());

        NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
        manager = (NotificationManager) getSystemService(getActivity().getApplicationContext(), NotificationManager.class);
        manager.createNotificationChannel(channel);

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("order", orderModel);
                bundle.putFloat("wallet", wallet);
                bundle.putString("datetime", dateTimeString);
                Checkout3Fragment fragment = new Checkout3Fragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
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

