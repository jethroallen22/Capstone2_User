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
import com.example.myapplication.interfaces.JsonArrayResponseCallback;
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

public class Payment2Fragment extends Fragment implements JsonArrayResponseCallback {

    private FragmentPaymentBinding binding;
    private static String JSON_URL;
    private IPModel ipModel;

    //Payment Recycler View
    RecyclerView rv_pay_method;
    List<MethodModel> method_list;
    PaymentMethodAdapter paymentMethodAdapter;

    //Refund Recycler View;
    RecyclerView rv_ref_method;
    RefundMethodAdapter refundMethodAdapter;

    //Transaction History Recycler View
    RecyclerView rv_transac_history;
    List<TransacHistoryModel> transac_history_list;
    TransacHistoryAdapter transacHistoryAdapter;
    RequestQueue requestQueue, requestQueueBalance;
    int userId = 0;
//    float wallet;
    List<WalletModel> walletModelList;
    WalletModel walletModel;
    TextView tv_avail_balance;
    int tmpId=0;
    double tmpWallet;
    Button btn_cashIn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("id");
//            wallet = bundle.getFloat("wallet", 0.0F);
//            walletText = String.valueOf(wallet);
//            Log.d("WalletText", walletText);

        } else {
            Log.d("HOME FRAG name", "FAIL");
        }

        tv_avail_balance = root.findViewById(R.id.tv_balance_avail);
        btn_cashIn = root.findViewById(R.id.btn_cash_in);

        tv_avail_balance.setText("");
        method_list = new ArrayList<>();
        walletModelList = new ArrayList<>();
        method_list.add(new MethodModel(R.drawable.gcash,"Gcash"));

        rv_ref_method = root.findViewById(R.id.rv_ref_method);
        refundMethodAdapter = new RefundMethodAdapter(getActivity(),method_list);
        rv_ref_method.setAdapter(refundMethodAdapter);
        rv_ref_method.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_ref_method.setHasFixedSize(true);
        rv_ref_method.setNestedScrollingEnabled(false);

        rv_pay_method = root.findViewById(R.id.rv_pay_method);
        paymentMethodAdapter = new PaymentMethodAdapter(getActivity(),method_list);
        rv_pay_method.setAdapter(paymentMethodAdapter);
        rv_pay_method.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_pay_method.setHasFixedSize(true);
        rv_pay_method.setNestedScrollingEnabled(false);
        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                Payment2Fragment paymentFragment = new Payment2Fragment();
                getAvailBalance(paymentFragment);
            }
        }, 1000);


//        binding.btnCashIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                for (int i = 0 ; i < walletModelList.size() ; i++){
////                    if(walletModelList.get(i).getUserId() == userId){
////                        tmpId = walletModelList.get(i).getUserId();
////                        tmpWallet = walletModelList.get(i).getWallet();
////                        break;
////                    }
////                }
//                Bundle bundle = new Bundle();
//                Log.d("CashinID", tmpId + " " + tmpWallet);
//                bundle.putInt("id",tmpId);
//                bundle.putDouble("wallet",tmpWallet);
//                CashInFragment fragment = new CashInFragment();
//                fragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
//            }
//        });
        //getAvailBalance();
        getTransacHistory();


        return root;
    }

    public void getAvailBalance(JsonArrayResponseCallback callback){
        requestQueueBalance = Singleton.getsInstance(getActivity()).getRequestQueue();
        JsonArrayRequest jsonArrayRequestBalance = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiwalletget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("WalletLength", String.valueOf(response.length()));
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        double wallet;
                        if (id == userId) {
                            Log.d("Match", "IDread: " + id + " ID: " + userId);
                            wallet = jsonObject.getDouble("wallet");
                            callback.onSuccess(wallet);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //tv_avail_balance.setText(walletText[0]);
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
        transac_history_list = new ArrayList<>();
        transac_history_list = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();

        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apitransacget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Transac", String.valueOf(response.length()));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("userId");
                        if(id == userId) {
                            String transacType = jsonObject.getString("transacType");
                            double amount = jsonObject.getDouble("amount");
                            String date = jsonObject.getString("date");

                            TransacHistoryModel transacHistoryModel = new TransacHistoryModel(transacType, date, amount);
                            transac_history_list.add(transacHistoryModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("TransacList", String.valueOf(transac_history_list.size()));
                    transacHistoryAdapter = new TransacHistoryAdapter(getActivity(),transac_history_list);
                    binding.rvTransacHistory.setAdapter(transacHistoryAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    binding.rvTransacHistory.setLayoutManager(layoutManager);
                }
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

    @Override
    public void onSuccess(double value) {
        Log.d("double", String.valueOf(value));
        String tmp = "₱ " + String.valueOf(value);
        tmpWallet = value;
        Log.d("tmp", tmp);
//        tv_avail_balance = binding.tvBalance;
        //tv_avail_balance.setText(tmp);

//        btn_cashIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                Log.d("CashinID", tmpId + " " + tmpWallet);
//                bundle.putInt("id",userId);
//                bundle.putDouble("wallet",value);
//                CashInFragment fragment = new CashInFragment();
//                fragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
//            }
//        });
    }
}

