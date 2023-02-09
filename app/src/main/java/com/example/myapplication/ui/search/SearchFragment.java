package com.example.myapplication.ui.search;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.myapplication.R;
import com.example.myapplication.activities.Login;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentMessagesBinding;
import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.HomeCategoryModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.messages.MessagesViewModel;

import java.util.ArrayList;
import java.util.List;

/*
*   Get search query string
*   Scan all restaurant and product in database
*   Store each restau and product name and image in search model
*   After storing add it to SearchArrayList
*   After storing all restau and product in arraylist check if searchquery exist in arraylist
*   If searchquery exists in arraylist display it in recylerview
 */
public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private FragmentSearchBinding binding;

    //Search Query
    String getSearchQuery;
    List<SearchModel> searchModelList;
    List<SearchModel> tempSearchModelList;
    RecyclerView rv_search;
    SearchAdapter searchAdapter;
    RecyclerViewInterface recyclerViewInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = this.getArguments();
        searchModelList = new ArrayList<>();
        tempSearchModelList = new ArrayList<>();

        if(bundle != null){
            getSearchQuery = bundle.getString("search");
            searchModelList = (List<SearchModel>) getArguments().getSerializable("SearchList");
            Log.d("Search Result: ", String.valueOf(searchModelList.size()));
        }

        rv_search = root.findViewById(R.id.rv_search);

        /////////////////////////////////////
        if (getSearchQuery.length()>0){
            for (int i = 0 ; i < searchModelList.size() ; i++){
                if (searchModelList.get(i).getSearchName().toLowerCase().contains(getSearchQuery.toLowerCase()) ||
                        searchModelList.get(i).getSearchTag().toLowerCase().contains(getSearchQuery.toLowerCase())){
                    SearchModel searchModel = new SearchModel(searchModelList.get(i).getSearchImage(),searchModelList.get(i).getSearchName(),searchModelList.get(i).getSearchTag());
                    tempSearchModelList.add(searchModel);
                }
            }
            searchAdapter = new SearchAdapter(getActivity().getApplicationContext(),tempSearchModelList, recyclerViewInterface);
            rv_search.setAdapter(searchAdapter);
            rv_search.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
            rv_search.setHasFixedSize(true);
            rv_search.setNestedScrollingEnabled(false);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}