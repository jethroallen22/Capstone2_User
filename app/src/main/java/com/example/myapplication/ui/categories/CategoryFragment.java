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

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private CategoryViewModel mViewModel;

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
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tv_category = root.findViewById(R.id.tv_category);

        Bundle bundle = this.getArguments();
        categoryListModel = new ArrayList<>();
        tempCategoryListModel = new ArrayList<>();

        if(bundle != null){
            getCategoryQuery = bundle.getString("categoryName");
            categoryListModel = (List<StoreModel>) getArguments().getSerializable("tempStoreList");
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
        homeStoreRecAdapter = new HomeStoreRecAdapter(getContext(),tempCategoryListModel, recyclerViewInterface);
        rv_category.setAdapter(homeStoreRecAdapter);
        rv_category.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_category.setHasFixedSize(true);
        rv_category.setNestedScrollingEnabled(false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        // TODO: Use the ViewModel
    }

}