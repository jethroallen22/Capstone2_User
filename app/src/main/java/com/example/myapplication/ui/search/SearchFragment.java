package com.example.myapplication.ui.search;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.myapplication.R;
import com.example.myapplication.activities.Store;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.messages.MessagesFragment;
import com.example.myapplication.ui.product.ProductFragment;
import com.example.myapplication.ui.store.StoreFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
*   Get search query string
*   Scan all restaurant and product in database
*   Store each restau and product name and image in search model
*   After storing add it to SearchArrayList
*   After storing all restau and product in arraylist check if searchquery exist in arraylist
*   If searchquery exists in arraylist display it in recylerview
 */
public class SearchFragment extends Fragment implements RecyclerViewInterface{

    private SearchViewModel mViewModel;
    private FragmentSearchBinding binding;

    //Search Query
    SearchView searchView;
    String getSearchQuery;
    List<SearchModel> searchModelList;
    List<SearchModel> tempSearchModelList;
    List<StoreModel> storeModelList;
    List<ProductModel> productModelList;
    RecyclerView rv_search;
    SearchAdapter searchAdapter;
    RecyclerViewInterface recyclerViewInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_search = root.findViewById(R.id.rv_search);
        searchView = root.findViewById(R.id.searchView2);
        searchView.setIconified(false);

        Bundle bundle = this.getArguments();
        searchModelList = new ArrayList<>();
        tempSearchModelList = new ArrayList<>();
        storeModelList = new ArrayList<>();
        productModelList = new ArrayList<>();

        if(bundle != null){
            getSearchQuery = bundle.getString("search");
            searchModelList = (List<SearchModel>) getArguments().getSerializable("SearchList");
            storeModelList = (List<StoreModel>) getArguments().getSerializable("StoreList");
            productModelList = (List<ProductModel>) getArguments().getSerializable("ProductList");
            Log.d("Search Result Search List: ", String.valueOf(searchModelList.size()));
            Log.d("Search Result Store List: ", String.valueOf(storeModelList.size()));
            Log.d("Search Result Product List: ", String.valueOf(productModelList.size()));
        }

        /////////////////////////////////////
        if (getSearchQuery.length()>0){
            for (int i = 0 ; i < searchModelList.size() ; i++){
                if (searchModelList.get(i).getSearchName().toLowerCase().contains(getSearchQuery.toLowerCase()) ||
                        searchModelList.get(i).getSearchTag().toLowerCase().contains(getSearchQuery.toLowerCase())){
                    SearchModel searchModel = new SearchModel(searchModelList.get(i).getSearchImage(),searchModelList.get(i).getSearchName(),searchModelList.get(i).getSearchTag());
                    tempSearchModelList.add(searchModel);
                }
            }
            searchAdapter = new SearchAdapter(getActivity(),tempSearchModelList, SearchFragment.this);
            rv_search.setAdapter(searchAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            rv_search.setLayoutManager(layoutManager);
            rv_search.setHasFixedSize(true);
            rv_search.setNestedScrollingEnabled(false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if(query.length() > 0 ){
//                    for (int i = 0 ; i < searchModelList.size() ; i++){
//                        if (searchModelList.get(i).getSearchName().toLowerCase().contains(query.toLowerCase()) ||
//                                searchModelList.get(i).getSearchTag().toLowerCase().contains(query.toLowerCase())){
//                            SearchModel searchModel = new SearchModel(searchModelList.get(i).getSearchImage(),searchModelList.get(i).getSearchName(),searchModelList.get(i).getSearchTag());
//                            tempSearchModelList.add(searchModel);
//                        }
//                    }
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    rv_search.setLayoutManager(layoutManager);
//                    SearchAdapter searchAdapter = new SearchAdapter(getContext(),tempSearchModelList, recyclerViewInterface);
//                    rv_search.setAdapter(searchAdapter);
//                } else{
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    rv_search.setLayoutManager(layoutManager);
//                    SearchAdapter searchAdapter = new SearchAdapter(getContext(),searchModelList, recyclerViewInterface);
//                    rv_search.setAdapter(searchAdapter);
//
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tempSearchModelList = new ArrayList<>();
                if(newText.length()>0){
                    for (int i = 0; i < searchModelList.size() ; i++){
                        if (searchModelList.get(i).getSearchName().toLowerCase().contains(newText.toLowerCase()) ||
                                searchModelList.get(i).getSearchTag().toLowerCase().contains(newText.toLowerCase())){
                            SearchModel searchModel = new SearchModel(searchModelList.get(i).getSearchImage(),searchModelList.get(i).getSearchName(),searchModelList.get(i).getSearchTag());
                            tempSearchModelList.add(searchModel);
                        }
                    }
                    searchAdapter = new SearchAdapter(getActivity(),tempSearchModelList, SearchFragment.this);
                    rv_search.setAdapter(searchAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rv_search.setLayoutManager(layoutManager);
                } else{
                    searchAdapter = new SearchAdapter(getActivity(),searchModelList, SearchFragment.this);
                    rv_search.setAdapter(searchAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rv_search.setLayoutManager(layoutManager);

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
    public void onItemClickSearch(int position) {
        Bundle bundle = new Bundle();
        StoreFragment fragment = new StoreFragment();
        StoreModel storeModel = storeModelList.get(position);
        bundle.putParcelable("StoreClass", storeModel);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
        Log.d("CLICK", "CLICK!!!");
        //Scan store list if query exists
//        for (int i = 0 ; i < storeModelList.size() ; i++){
//            if(tempSearchModelList.get(pos).getSearchName().toLowerCase().compareTo(storeModelList.get(i).getStore_name().toLowerCase()) == 0){
//                Log.d("Result: ", "Success");
//                Bundle bundle = new Bundle();
//                StoreFragment fragment = new StoreFragment();
//                bundle.putSerializable("StoreModel", (Serializable) storeModelList.get(i));
//                fragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
//            }
//        }



        //Scan product list if query exists
//        for (int k = 0 ; k < productModelList.size() ; k++){
//            if(tempSearchModelList.get(pos).getSearchName().toLowerCase().compareTo(productModelList.get(k).getProductName().toLowerCase()) == 0){
//                Bundle bundle = new Bundle();
//                ProductFragment fragment = new ProductFragment();
//                bundle.putSerializable("ProductModel", (Serializable) productModelList.get(k));
//                fragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
//            }
//        }
    }
}