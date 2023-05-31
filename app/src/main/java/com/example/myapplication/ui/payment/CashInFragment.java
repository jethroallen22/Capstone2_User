package com.example.myapplication.ui.payment;

import static com.example.myapplication.activities.Home.id;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCashInBinding;
import com.example.myapplication.databinding.FragmentMessagesBinding;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.Map;

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
                temp = Float.parseFloat(String.valueOf(binding.etAmount.getText()));
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
                        wallet += temp;
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