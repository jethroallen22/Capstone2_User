package com.example.myapplication.ui.payment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.PaymentMethodAdapter;
import com.example.myapplication.adapters.RefundMethodAdapter;
import com.example.myapplication.adapters.TransacHistoryAdapter;
import com.example.myapplication.databinding.FragmentPaymentBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.MethodModel;
import com.example.myapplication.models.TransacHistoryModel;
import com.example.myapplication.models.WalletModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding binding;
    private static String JSON_URL;
    private IPModel ipModel;
    TextView tv_balance;
    Button btn_cash_in;
    RecyclerView rv_pay_method, rv_ref_method, rv_transac_history;
    PaymentMethodAdapter paymentMethodAdapter;
    RefundMethodAdapter refundMethodAdapter;
    TransacHistoryAdapter transacHistoryAdapter;
    List<MethodModel> methodModelList;
    List<TransacHistoryModel> transacHistoryModelList;
    RequestQueue requestQueue, requestQueueBalance;
    int userId = 0;
    WalletModel walletModel;
    List<WalletModel> walletModelList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tv_balance = root.findViewById(R.id.tv_balance_avail);
        btn_cash_in = root.findViewById(R.id.btn_cash_in);
        rv_pay_method = root.findViewById(R.id.rv_pay_method);
        rv_ref_method = root.findViewById(R.id.rv_ref_method);
        rv_transac_history = root.findViewById(R.id.rv_transac_history);

        methodModelList = new ArrayList<>();
        walletModelList = new ArrayList<>();
        transacHistoryModelList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("id");
        } else {
            Log.d("PaymentFragment", "FAIL");
        }

        methodModelList.add(new MethodModel(R.drawable.gcash,"Gcash"));
        refundMethodAdapter = new RefundMethodAdapter(getActivity(),methodModelList);
        rv_ref_method.setAdapter(refundMethodAdapter);
        rv_ref_method.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_ref_method.setHasFixedSize(true);
        rv_ref_method.setNestedScrollingEnabled(false);

        paymentMethodAdapter = new PaymentMethodAdapter(getActivity(),methodModelList);
        rv_pay_method.setAdapter(paymentMethodAdapter);
        rv_pay_method.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_pay_method.setHasFixedSize(true);
        rv_pay_method.setNestedScrollingEnabled(false);
        requestQueueBalance = Singleton.getsInstance(getActivity()).getRequestQueue();
        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                String tmp = "₱ ";
                tv_balance.setText(tmp);


                JsonArrayRequest jsonArrayRequestBalance = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiwalletget.php", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                double wallet = jsonObject.getDouble("wallet");

                                walletModel = new WalletModel(id,wallet);
                                walletModelList.add(walletModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0 ; i < walletModelList.size() ; i++){
                            if (walletModelList.get(i).getUserId() == userId) {
                                String tmp = String.valueOf(walletModelList.get(i).getWallet());
                                Log.d("Match", "IDread: " + walletModelList.get(i).getUserId() + " ID: " + userId);
                                tv_balance.setText("₱ "+ tmp);
                                Log.d("Match", tv_balance.getText() + " " + walletModelList.get(i).getWallet());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                    }
                });
                requestQueueBalance.add(jsonArrayRequestBalance);
                root.postDelayed(this, 1000);
            }
        }, 1000);

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}