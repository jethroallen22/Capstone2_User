package com.example.myapplication.ui.addresses;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AddressAdapter;
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.databinding.FragmentActivitiesBinding;
import com.example.myapplication.databinding.FragmentAddressesBinding;
import com.example.myapplication.models.AddressModel;
import com.example.myapplication.models.NotificationModel;
import com.example.myapplication.ui.activities.ActivitiesViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddressesFragment extends Fragment {

    private AddressesViewModel mViewModel;
    private FragmentAddressesBinding binding;

    //Address List Recycler View
    RecyclerView rv_addresses;
    List<AddressModel> address_list;
    AddressAdapter addressAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddressesViewModel addressesViewModel =
                new ViewModelProvider(this).get(AddressesViewModel.class);

        binding = FragmentAddressesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_addresses = root.findViewById(R.id.rv_addresses);
        address_list = new ArrayList<>();
        address_list.add(new AddressModel("Home","143 Evangelista Street, Barangay Banalo, Bacoor City"));
        address_list.add(new AddressModel("Mema Lang","Esterling Heights Subdivision, Guintorilan City"));
        addressAdapter = new AddressAdapter(getActivity(),address_list);
        rv_addresses.setAdapter(addressAdapter);
        rv_addresses.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_addresses.setHasFixedSize(true);
        rv_addresses.setNestedScrollingEnabled(false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}