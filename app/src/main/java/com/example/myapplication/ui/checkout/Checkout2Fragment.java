package com.example.myapplication.ui.checkout;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCheckout2Binding;
import com.example.myapplication.databinding.FragmentCheckoutBinding;
import com.example.myapplication.models.OrderModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Checkout2Fragment extends Fragment {

    private FragmentCheckout2Binding binding;
    OrderModel orderModel;
    int[] randomNumbers;
    NotificationManager manager;
    List<Integer> randomInt;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = this.getArguments();
        if (bundle != null)
            orderModel = bundle.getParcelable("order");
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

