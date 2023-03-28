package com.example.myapplication.ui.search;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activities.Store;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.ui.messages.MessagesFragment;
import com.example.myapplication.ui.product.ProductFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
*   Get search query string
*   Scan all restaurant and product in database
*   Store each restau and product name and image in search model
*   After storing add it to SearchArrayList
*   After storing all restau and product in arraylist check if searchquery exist in arraylist
*   If searchquery exists in arraylist display it in recylerview
 */
public class SearchFragment extends Fragment implements RecyclerViewInterface{

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

    //For Product Bottomsheet
    LinearLayout linearLayout;
    TextView product_name, product_resto, product_price, product_description, tv_counter, tv_weather;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;
    int userId = 0;

    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

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
            userId = getArguments().getInt("userId");
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
//
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
        for (int i = 0 ; i < storeModelList.size() ; i++){
            if(tempSearchModelList.get(position).getSearchName().toLowerCase().compareTo(storeModelList.get(i).getStore_name().toLowerCase()) == 0){
                Log.d("Result: ", "Success");
                Bundle bundle = new Bundle();
                StoreFragment fragment = new StoreFragment();
                bundle.putParcelable("StoreClass", storeModelList.get(i));
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        }
        //Scan product list if query exists
        for (int k = 0 ; k < productModelList.size() ; k++){
            if(tempSearchModelList.get(position).getSearchName().toLowerCase().compareTo(productModelList.get(k).getProductName().toLowerCase()) == 0){
                showBottomSheet(position);
            }
        }
    }

    @Override
    public void onItemClickWeather(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {

    }

    public void showBottomSheet(int position){
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.product_bottom_sheet_layout,
                        getActivity().findViewById(R.id.product_bottomSheet_container)
                );
        Log.d(TAG,"bottomSheetView = LayoutInflater.from");
        product_image = bottomSheetView.findViewById(R.id.iv_product_imagee2);
        product_name = bottomSheetView.findViewById(R.id.tv_product_namee2);
        product_resto = bottomSheetView.findViewById(R.id.tv_product_restos);
        product_description = bottomSheetView.findViewById(R.id.tv_product_description2);
        product_price = bottomSheetView.findViewById(R.id.tv_product_pricee2);
        btn_add_to_cart = bottomSheetView.findViewById(R.id.btn_add_to_cart);
        cl_product_add = bottomSheetView.findViewById(R.id.cl_product_add);
        cl_product_minus = bottomSheetView.findViewById(R.id.cl_product_minus);
        tv_counter = bottomSheetView.findViewById(R.id.tv_counter);

        product_image.setImageBitmap(productModelList.get(position).getBitmapImage());
        product_name.setText(productModelList.get(position).getProductName());
        product_resto.setText(productModelList.get(position).getProductRestoName());
        product_description.setText(productModelList.get(position).getProductDescription());
        product_price.setText("P"+productModelList.get(position).getProductPrice());
        tv_counter.setText(Integer.toString(product_count));
        btn_add_to_cart.setEnabled(false);

        //Add count to order
        cl_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_count >= 0 ){
                    cl_product_minus.setClickable(true);
                    btn_add_to_cart.setEnabled(true);
                    product_count +=1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        //Subtract count to order
        cl_product_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product_count == 0){
                    cl_product_minus.setClickable(false);
                    btn_add_to_cart.setEnabled(false);
                }else{
                    product_count -=1;
                    tv_counter.setText(Integer.toString(product_count));
                }
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            int temp_count = 0;
            float tempPrice = 0;
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+"tempCart.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("1 ", result );
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Log.d("TEMP CART INSERT", "success");
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Not Inserted",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("TEMP CART", "catch" );
                            Toast.makeText(getActivity().getApplicationContext(), "Catch ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("temp_productId", String.valueOf(productModelList.get(position).getIdProduct()));
                        params.put("temp_storeId", String.valueOf(productModelList.get(position).getStore_idStore()));
                        params.put("temp_usersId", String.valueOf(userId));
                        params.put("temp_productName", productModelList.get(position).getProductName());
                        params.put("temp_productPrice", String.valueOf(productModelList.get(position).getProductPrice()));
                        params.put("temp_productQuantity", String.valueOf(product_count));
                        params.put("temp_totalProductPrice", String.valueOf(product_count * productModelList.get(position).getProductPrice()));
                        product_count = 0;
                        return params;
                    }

                };


                RequestQueue requestQueueTempCart = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueueTempCart.add(stringRequest);
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}