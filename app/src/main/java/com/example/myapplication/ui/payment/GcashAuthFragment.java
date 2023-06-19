package com.example.myapplication.ui.payment;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.myapplication.activities.Home.id;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.databinding.FragmentGcashAuthBinding;
import com.example.myapplication.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GcashAuthFragment extends Fragment {

    Button btn_submit_auth;
    int userId;
    double wallet;
    float amount;
    private static String JSON_URL;
    private IPModel ipModel;
    NotificationManager manager;
    List<Integer> randomInt;

    private FragmentGcashAuthBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGcashAuthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        Bundle bundle = getArguments();
        if(bundle != null){
            userId = bundle.getInt("id");
            amount = bundle.getFloat("amount");
            wallet = bundle.getDouble("wallet");
            Log.d("Cashin", userId + " " + wallet);
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

        btn_submit_auth = root.findViewById(R.id.btn_submit_auth);
        btn_submit_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putFloat("amount", amount);
                bundle.putDouble("wallet", wallet);
                GcashReceiptFragment fragment = new GcashReceiptFragment();
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