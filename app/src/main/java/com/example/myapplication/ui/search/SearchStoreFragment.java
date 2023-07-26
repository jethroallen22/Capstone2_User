package com.example.myapplication.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentSearchStoreBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.ui.store.StoreFragment;

import java.util.ArrayList;
import java.util.List;


public class SearchStoreFragment extends Fragment implements RecyclerViewInterface {

    private FragmentSearchStoreBinding binding;
    List<SearchModel> searchModelList, searchModelListProduct, searchModelListStore, tempSearchModelListProduct, tempSearchModelListStore;
    List<StoreModel> storeModelList;
    LinearLayout ll_search_stores;
    TextView tv_search_stores;
    RecyclerView rv_search_stores;
    SearchAdapter storeAdapter;
    SearchView searchView;
    String getSearchQuery;
    int userId;
    private static String JSON_URL;
    private IPModel ipModel;
    private boolean isQueryTextChange = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        SearchFragment searchFragment = (SearchFragment) getParentFragment();
        if(searchFragment != null){
            getSearchQuery = searchFragment.getGetSearchQuery();
            searchModelList = searchFragment.getSearchModelList();
            storeModelList = searchFragment.getStoreModelList();
            userId = searchFragment.getUserId();
            Log.d("Search Result Search List: ", String.valueOf(searchModelList.size()));
            Log.d("Search Result Store List: ", String.valueOf(storeModelList.size()));
        }
        searchModelListProduct = new ArrayList<>();
        searchModelListStore = new ArrayList<>();
        if (getSearchQuery.length() > 0) {
            for (StoreModel store : storeModelList) {
                if (store.getStore_name().toLowerCase().contains(getSearchQuery.toLowerCase()) ||
                        store.getStore_category().toLowerCase().contains(getSearchQuery.toLowerCase())) {
                    SearchModel searchModel = new SearchModel(store.getStore_image(), store.getStore_name(), store.getStore_category());
                    searchModel.setStock("stocked");
                    searchModelListStore.add(searchModel);
                }
            }
            ll_search_stores = root.findViewById(R.id.ll_search_stores);
            tv_search_stores = root.findViewById(R.id.tv_search_stores);
            rv_search_stores = root.findViewById(R.id.rv_search_stores);
            if(searchModelListStore != null){
                ll_search_stores.setVisibility(View.VISIBLE);
                tv_search_stores.setVisibility(View.VISIBLE);
                storeAdapter = new SearchAdapter(getActivity(), searchModelListStore, SearchStoreFragment.this, rv_search_stores.getId());
                rv_search_stores.setAdapter(storeAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_search_stores.setLayoutManager(layoutManager);
                rv_search_stores.setHasFixedSize(true);
                rv_search_stores.setNestedScrollingEnabled(false);
            } else {
                ll_search_stores.setVisibility(View.INVISIBLE);
                tv_search_stores.setVisibility(View.INVISIBLE);
            }
        }

        searchView = root.findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isQueryTextChange = true;
                tempSearchModelListProduct = new ArrayList<>();
                tempSearchModelListStore = new ArrayList<>();
                if (newText.length() > 0) {
                    for (StoreModel store : storeModelList) {
                        if (store.getStore_name().toLowerCase().contains(newText.toLowerCase()) ||
                                store.getStore_category().toLowerCase().contains(newText.toLowerCase())) {
                            SearchModel searchModel = new SearchModel(store.getStore_image(), store.getStore_name(), store.getStore_category());
                            searchModel.setStock("stocked");
                            tempSearchModelListStore.add(searchModel);
                        }
                    }
                    if(tempSearchModelListStore != null){
                        ll_search_stores.setVisibility(View.VISIBLE);
                        storeAdapter = new SearchAdapter(getActivity(), tempSearchModelListStore, SearchStoreFragment.this, rv_search_stores.getId());
                        rv_search_stores.setAdapter(storeAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        rv_search_stores.setLayoutManager(layoutManager);
                        rv_search_stores.setHasFixedSize(true);
                        rv_search_stores.setNestedScrollingEnabled(false);
                    }
                } else{
                   if(searchModelListStore != null){
                        ll_search_stores.setVisibility(View.VISIBLE);
                        tv_search_stores.setVisibility(View.VISIBLE);
                        storeAdapter = new SearchAdapter(getActivity(), searchModelListStore, SearchStoreFragment.this, rv_search_stores.getId());
                        rv_search_stores.setAdapter(storeAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        rv_search_stores.setLayoutManager(layoutManager);
                        rv_search_stores.setHasFixedSize(true);
                        rv_search_stores.setNestedScrollingEnabled(false);
                   }  else {
                        ll_search_stores.setVisibility(View.INVISIBLE);
                        tv_search_stores.setVisibility(View.INVISIBLE);

                   }

                }
                return true;
            }
        });

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
    public void onItemClickSearch(int position, int recyclerViewId) {
        if (recyclerViewId == R.id.rv_search_stores) {
            for (int i = 0; i < storeModelList.size(); i++) {
                if(isQueryTextChange) {
                    if (tempSearchModelListStore.get(position).getSearchName().equalsIgnoreCase(storeModelList.get(i).getStore_name())) {
                        Log.d("Result: ", "Success");
                        Bundle bundle = new Bundle();
                        StoreFragment fragment = new StoreFragment();
                        bundle.putParcelable("StoreClass", storeModelList.get(i));
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                        break;
                    }
                } else {
                    if(searchModelListStore.get(position).getSearchName().equalsIgnoreCase(storeModelList.get(i).getStore_name())) {
                        Log.d("Result: ", "Success");
                        Bundle bundle = new Bundle();
                        StoreFragment fragment = new StoreFragment();
                        bundle.putParcelable("StoreClass", storeModelList.get(i));
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onItemClickSearch(int position) {

    }

    @Override
    public void onItemClickWeather(int position) {

    }

    @Override
    public void onItemClickCategory(int position) {

    }

    @Override
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickVoucher(int pos) {

    }
}