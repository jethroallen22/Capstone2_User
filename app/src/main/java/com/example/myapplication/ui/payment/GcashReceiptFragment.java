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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.databinding.FragmentGcashReceiptBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GcashReceiptFragment extends Fragment {

    private FragmentGcashReceiptBinding binding;
    TextView tv_amount, tv_total_amount, tv_date;
    Button btn_next;
    int userId;
    double wallet;
    float amount;
    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGcashReceiptBinding.inflate(inflater, container, false);
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

        //Initialize
        tv_amount = root.findViewById(R.id.tv_amount);
        tv_total_amount = root.findViewById(R.id.tv_total_amount);
        tv_date = root.findViewById(R.id.tv_date);
        btn_next = root.findViewById(R.id.btn_next);

        tv_amount.setText("P" + amount);
        tv_total_amount.setText("P" + amount);

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateTimeString = dateFormat.format(currentTime);
        tv_date.setText(dateTimeString);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "update_wallet.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("On Res", "inside on res");
                                } catch (Throwable e) {
                                    Log.d("Catch", String.valueOf(e));
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        wallet += amount;
                        Log.d("Cashin", String.valueOf(wallet));
                        paramV.put("id", String.valueOf(userId));
                        paramV.put("wallet", String.valueOf(wallet));
                        Log.d("Cashin", "success");
                        return paramV;
                    }
                };
                queue.add(stringRequest);

                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putDouble("wallet", wallet);
                PaymentFragment fragment = new PaymentFragment();
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