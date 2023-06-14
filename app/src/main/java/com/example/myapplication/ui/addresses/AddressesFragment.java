package com.example.myapplication.ui.addresses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AddressAdapter;
import com.example.myapplication.databinding.FragmentAddressesBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.activities.models.AddressModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressesFragment extends Fragment {
    private FragmentAddressesBinding binding;
    private RequestQueue requestQueueAddress;


    private static String JSON_URL_ADDRESSES="http://192.168.68.106/android_register_login/apiaddresses.php";

    //Address List Recycler View
    RecyclerView rv_addresses;
    List<AddressModel> address_list;
    AddressAdapter addressAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddressesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_addresses = root.findViewById(R.id.rv_addresses);
        address_list = new ArrayList<>();
//        address_list.add(new AddressModel("Home","143 Evangelista Street, Barangay Banalo, Bacoor City"));
//        address_list.add(new AddressModel("Mema Lang","Esterling Heights Subdivision, Guintorilan City"));
//        addressAdapter = new AddressAdapter(getActivity(),address_list);
//        rv_addresses.setAdapter(addressAdapter);
        rv_addresses.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_addresses.setHasFixedSize(true);
        rv_addresses.setNestedScrollingEnabled(false);
        requestQueueAddress = Singleton.getsInstance(getActivity()).getRequestQueue();
        extractAddresses();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Database to Category Function
    public void extractAddresses(){
        long hello_id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_ADDRESSES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        long address_id = jsonObject.getLong("address_id");
                        String address_name = jsonObject.getString("address_name");
                        String address_location = jsonObject.getString("address_location");
                        long user_id = jsonObject.getLong("user_id");

                        Log.d("Address", address_name + address_location);
                        AddressModel addressModel = new AddressModel(address_id,address_name,address_location,user_id);
                        address_list.add(addressModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addressAdapter = new AddressAdapter(getActivity(),address_list);
                    rv_addresses.setAdapter(addressAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueAddress.add(jsonArrayRequest);
    }

}