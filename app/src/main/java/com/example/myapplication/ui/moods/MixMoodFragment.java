package com.example.myapplication.ui.moods;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentMixMoodBinding;
import com.example.myapplication.databinding.FragmentPaymentBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.categories.CategoryFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.payment.PaymentViewModel;
import com.example.myapplication.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MixMoodFragment extends Fragment implements RecyclerViewInterface {

    private FragmentMixMoodBinding binding;
    List<ProductModel> productModelList;
    RecyclerView rvMix;
    ProductAdapter productAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMixMoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if (bundle != null){
            productModelList = new ArrayList<>();
            productModelList = (List<ProductModel>) getArguments().getSerializable("productList");
            Collections.shuffle(productModelList);
            Log.d("Size", String.valueOf(productModelList.size()));
            for (int i = 0 ; i < productModelList.size() ; i++){
                Log.d("ProdName", String.valueOf(productModelList.get(i).getIdProduct()));
            }
        }
        rvMix = root.findViewById(R.id.rv_mix_mood);
        productAdapter = new ProductAdapter(getActivity(),productModelList, MixMoodFragment.this);
        rvMix.setAdapter(productAdapter);
        rvMix.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvMix.setHasFixedSize(true);
        rvMix.setNestedScrollingEnabled(false);




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClickForYou(int position) {

    }

    @Override
    public void onItemClickStorePopular(int position) {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClickStoreRec(int position) {

    }

    @Override
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickSearch(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {

    }
}