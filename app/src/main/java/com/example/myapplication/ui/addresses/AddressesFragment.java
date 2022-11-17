package com.example.myapplication.ui.addresses;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AddressAdapter;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.databinding.FragmentActivitiesBinding;
import com.example.myapplication.databinding.FragmentAddressesBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.AddressModel;
import com.example.myapplication.models.HomeCategoryModel;
import com.example.myapplication.models.NotificationModel;
import com.example.myapplication.ui.activities.ActivitiesViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressesFragment extends Fragment {

    private AddressesViewModel mViewModel;
    private FragmentAddressesBinding binding;
    private RequestQueue requestQueueAddress;


    private static String JSON_URL_ADDRESSES="http://192.168.68.105/android_register_login/apiaddresses.php";

    //Address List Recycler View
    RecyclerView rv_addresses;
    List<AddressModel> address_list;
    AddressAdapter addressAdapter;

    ImageView address_delete;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddressesViewModel addressesViewModel =
                new ViewModelProvider(this).get(AddressesViewModel.class);

        binding = FragmentAddressesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_addresses = root.findViewById(R.id.rv_addresses);
        address_delete = root.findViewById(R.id.iv_address_delete);
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_ADDRESSES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String address_id = jsonObject.getString("address_id");
                        String address_name = jsonObject.getString("address_name");
                        String address_location = jsonObject.getString("address_location");
                        String user_id = jsonObject.getString("user_id");

                        Log.d("Address", address_name + address_location);

                        AddressModel addModel = new AddressModel(address_name,address_location);
                        address_list.add(addModel);

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