package com.example.myapplication.ui.categories;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentCategoryBinding;
import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.store.StoreFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements RecyclerViewInterface{

    private FragmentCategoryBinding binding;

    List<StoreModel> categoryListModel;
    List<StoreModel> tempCategoryListModel;

    String getCategoryQuery;

    HomeStoreRecAdapter homeStoreRecAdapter;

    RecyclerViewInterface recyclerViewInterface;

    RecyclerView rv_category;

    TextView tv_category;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tv_category = root.findViewById(R.id.tv_category);

        Bundle bundle = this.getArguments();
        categoryListModel = new ArrayList<>();
        tempCategoryListModel = new ArrayList<>();

        if(bundle != null){
            getCategoryQuery = bundle.getString("categoryString");
            categoryListModel = (List<StoreModel>) getArguments().getSerializable("StoreList");
            Log.d("Store List: ", String.valueOf(categoryListModel.size()));
            tv_category.setText(getCategoryQuery);
        }

        rv_category = root.findViewById(R.id.rv_category);

        for (int i = 0 ; i < categoryListModel.size() ; i++){
            if (categoryListModel.get(i).getStore_category().toLowerCase().compareTo(getCategoryQuery.toLowerCase()) == 0){
                StoreModel storeModel = categoryListModel.get(i);
                tempCategoryListModel.add(storeModel);
            }
        }
        Log.d("CategSize", String.valueOf(tempCategoryListModel.size()));

          homeStoreRecAdapter = new HomeStoreRecAdapter(getActivity(),tempCategoryListModel,CategoryFragment.this);
          rv_category.setAdapter(homeStoreRecAdapter);
          RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
          rv_category.setLayoutManager(layoutManager);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
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
        for (int i = 0 ; i < categoryListModel.size() ; i++){
            if(tempCategoryListModel.get(position).getStore_name().toLowerCase().compareTo(categoryListModel.get(i).getStore_name().toLowerCase()) == 0){
                Log.d("Result: ", "Success");
                Bundle bundle = new Bundle();
                StoreFragment fragment = new StoreFragment();
                bundle.putParcelable("StoreClass", categoryListModel.get(i));
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        }
    }

    @Override
    public void onItemClickStoreRec2(int position) {

    }

    @Override
    public void onItemClickSearch(int position) {

    }

    @Override
    public void onItemClickWeather(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {

    }
}