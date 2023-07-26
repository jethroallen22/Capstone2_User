package com.example.myapplication.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.myapplication.R;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.TagModel;
import com.example.myapplication.adapters.SearchAdapter;
import com.example.myapplication.databinding.FragmentSearchProductBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchProductFragment extends Fragment implements RecyclerViewInterface {
    private FragmentSearchProductBinding binding;
    List<SearchModel> searchModelList, searchModelListProduct, searchModelListStore, tempSearchModelListProduct, tempSearchModelListStore;
    List<ProductModel> productModelList;
    LinearLayout ll_search_products;
    TextView tv_search_products;
    RecyclerView rv_search_products;
    SearchAdapter productAdapter;
    SearchView searchView;
    String getSearchQuery;
    int userId;
    //For Product Bottomsheet
    LinearLayout linearLayout;
    TextView product_name, product_resto, product_price, product_description, tv_counter, tv_weather;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;
    private static String JSON_URL;
    private IPModel ipModel;
    private boolean isQueryTextChange = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        SearchFragment searchFragment = (SearchFragment) getParentFragment();
        if(searchFragment != null){
            getSearchQuery = searchFragment.getGetSearchQuery();
            searchModelList = searchFragment.getSearchModelList();
            productModelList = searchFragment.getProductModelList();
            userId = searchFragment.getUserId();
            Log.d("Search Result Search List: ", String.valueOf(searchModelList.size()));
            Log.d("Search Result Product List: ", String.valueOf(productModelList.size()));
        }

        searchModelListProduct = new ArrayList<>();
        searchModelListStore = new ArrayList<>();
        if (getSearchQuery.length() > 0) {
            for (ProductModel product : productModelList) {
                if (product.getProductName().toLowerCase().contains(getSearchQuery.toLowerCase())) {
                    SearchModel searchModel = new SearchModel(product.getProductImage(), product.getProductName());
                    searchModel.setStock(product.getStock());
                    searchModel.setTagModelList(product.getTags_list());
                    searchModelListProduct.add(searchModel);
                } else {
                    for (TagModel tag : product.getTags_list()) {
                        if (tag.getTagname().toLowerCase().contains(getSearchQuery.toLowerCase())) {
                            SearchModel searchModel = new SearchModel(product.getProductImage(), product.getProductName());
                            searchModel.setStock(product.getStock());
                            searchModel.setTagModelList(product.getTags_list());
                            searchModelListProduct.add(searchModel);
                            break; // Exit the loop if a matching tag is found
                        }
                    }
                }
            }

            ll_search_products = root.findViewById(R.id.ll_search_products);
            tv_search_products = root.findViewById(R.id.tv_search_products);
            rv_search_products = root.findViewById(R.id.rv_search_products);
            if(searchModelListProduct != null) {
                ll_search_products.setVisibility(View.VISIBLE);
                tv_search_products.setVisibility(View.VISIBLE);
                productAdapter = new SearchAdapter(getActivity(), searchModelListProduct, SearchProductFragment.this, rv_search_products.getId());
                rv_search_products.setAdapter(productAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_search_products.setLayoutManager(layoutManager);
                rv_search_products.setHasFixedSize(true);
                rv_search_products.setNestedScrollingEnabled(false);
            } else {
                ll_search_products.setVisibility(View.INVISIBLE);
                tv_search_products.setVisibility(View.INVISIBLE);
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
                    for (ProductModel product : productModelList) {
                        if (product.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                            SearchModel searchModel = new SearchModel(product.getProductImage(), product.getProductName());
                            searchModel.setStock(product.getStock());
                            searchModel.setTagModelList(product.getTags_list());
                            tempSearchModelListProduct.add(searchModel);
                        } else {
                            for (TagModel tag : product.getTags_list()) {
                                if (tag.getTagname().toLowerCase().contains(newText.toLowerCase())) {
                                    SearchModel searchModel = new SearchModel(product.getProductImage(), product.getProductName());
                                    searchModel.setStock(product.getStock());
                                    searchModel.setTagModelList(product.getTags_list());
                                    tempSearchModelListProduct.add(searchModel);
                                    break; // Exit the loop if a matching tag is found
                                }
                            }
                        }
                    }
                    if(tempSearchModelListProduct != null) {
                        ll_search_products.setVisibility(View.VISIBLE);
                        productAdapter = new SearchAdapter(getActivity(), tempSearchModelListProduct, SearchProductFragment.this, rv_search_products.getId());
                        rv_search_products.setAdapter(productAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        rv_search_products.setLayoutManager(layoutManager);
                        rv_search_products.setHasFixedSize(true);
                        rv_search_products.setNestedScrollingEnabled(false);
                    }
                } else{
                    if(searchModelListProduct != null) {
                        ll_search_products.setVisibility(View.VISIBLE);
                        tv_search_products.setVisibility(View.VISIBLE);
                        productAdapter = new SearchAdapter(getActivity(), searchModelListProduct, SearchProductFragment.this, rv_search_products.getId());
                        rv_search_products.setAdapter(productAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        rv_search_products.setLayoutManager(layoutManager);
                        rv_search_products.setHasFixedSize(true);
                        rv_search_products.setNestedScrollingEnabled(false);
                    } else {
                        ll_search_products.setVisibility(View.INVISIBLE);
                        tv_search_products.setVisibility(View.INVISIBLE);
                    }
                }
                return true;
            }
        });

        return root;
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
        if (recyclerViewId == R.id.rv_search_products) {
            for (int k = 0; k < productModelList.size(); k++) {
                if (isQueryTextChange) {
                    if (tempSearchModelListProduct.get(position).getSearchName().equalsIgnoreCase(productModelList.get(k).getProductName())) {
                        showBottomSheet(k);
                        break;
                    }
                } else {
                    if (searchModelListProduct.get(position).getSearchName().equalsIgnoreCase(productModelList.get(k).getProductName())) {
                        showBottomSheet(k);
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