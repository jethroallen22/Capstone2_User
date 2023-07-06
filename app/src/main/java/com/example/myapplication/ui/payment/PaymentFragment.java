package com.example.myapplication.ui.payment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
    double tmpWallet = 0;
    List<WalletModel> walletModelList;
    private boolean stopExecution = false;
    private Handler handler;
    private Runnable myRunnable;

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
            tmpWallet = bundle.getDouble("wallet");
            binding.tvBalanceAvail.setText("₱ " + tmpWallet);
            Log.d("userId", String.valueOf(userId));
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
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        getTransacHistory();
        handler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                getAvailBalance();
            }
        };
        handler.postDelayed(myRunnable, 1000);

//        root.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(!stopExecution) {
//                    getAvailBalance();
//                    root.postDelayed(this, 3000);
//                }
//            }
//        }, 3000);

        binding.btnCashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopExecution = true;
                handler.removeCallbacks(myRunnable);
                String fullString = binding.tvBalanceAvail.getText().toString();
                int startIndex = 2;
                double wallet = Double.parseDouble(fullString.substring(startIndex));
                Bundle bundle = new Bundle();
                bundle.putInt("id",userId);
                bundle.putDouble("wallet", wallet);
                CashInFragment fragment = new CashInFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        return root;
    }

    public void getAvailBalance(){
        JsonArrayRequest jsonArrayRequestBalance = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiwalletget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        final double wallet = jsonObject.getDouble("wallet");
                        if(id == PaymentFragment.this.userId)
                            binding.tvBalanceAvail.setText("₱ " + wallet);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
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
    }

    public void getTransacHistory(){

        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apitransacget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        int id = jsonObject2.getInt("userId");
                        if(id == userId) {
                            String transacType = jsonObject2.getString("transacType");
                            double amount = jsonObject2.getDouble("amount");
                            String date = jsonObject2.getString("date");
                            TransacHistoryModel transacHistoryModel = new TransacHistoryModel(transacType, date, amount);
                            transacHistoryModelList.add(transacHistoryModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                transacHistoryAdapter = new TransacHistoryAdapter(getActivity(),transacHistoryModelList);
                rv_transac_history.setAdapter(transacHistoryAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_transac_history.setLayoutManager(layoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequestFoodforyou);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}