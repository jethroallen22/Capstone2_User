package com.example.myapplication.ui.moods;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentNewMoodBinding;
import com.example.myapplication.databinding.FragmentOldMoodBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.ProductModel;
import com.example.myapplication.models.SearchModel;
import com.example.myapplication.models.StoreModel;
import com.example.myapplication.interfaces.Singleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OldMoodFragment extends Fragment implements RecyclerViewInterface {

    private FragmentOldMoodBinding binding;

    List<ProductModel> productModelList , tempProductModelList;
    RecyclerView rvOld;
    ProductAdapter productAdapter;
    RequestQueue requestQueue;
    int userId = 0;

    private static String JSON_URL;
    private IPModel ipModel;

    //For Product Bottomsheet
    LinearLayout linearLayout;
    TextView product_name, product_resto, product_price, product_description, tv_counter, tv_weather;
    RoundedImageView product_image;
    ConstraintLayout cl_product_add;
    ConstraintLayout cl_product_minus;
    Button btn_add_to_cart;
    int product_count = 0;
    ImageView iv_icon_old;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOldMoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        iv_icon_old = root.findViewById(R.id.iv_icon_old);
        iv_icon_old.setImageResource(R.drawable.return_to_the_past);
        productModelList = new ArrayList<>();
        tempProductModelList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null){
            productModelList = new ArrayList<>();
            productModelList = (List<ProductModel>) getArguments().getSerializable("productList");
            userId = bundle.getInt("userId");
            Collections.shuffle(productModelList);
            Log.d("Size", String.valueOf(productModelList.size()));
            for (int i = 0 ; i < productModelList.size() ; i++){
                Log.d("ProdName", String.valueOf(productModelList.get(i).getIdProduct()));
            }
        }
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        rvOld = root.findViewById(R.id.rv_old_mood);
        rvOld.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvOld.setHasFixedSize(true);
        rvOld.setNestedScrollingEnabled(false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderhistoryget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ResponseJson", String.valueOf(response));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idProduct = jsonObject.getInt("idProduct");
                        int idUser = jsonObject.getInt("idUser");
                        Log.d("UserIDBundle", String.valueOf(userId));
                        Log.d("UserIDDB", String.valueOf(idUser));

                        if(idUser == userId){
                            Log.d("UserID", "MATCH");
                            Log.d("ListSize", String.valueOf(productModelList.size()));
                            for(int j = 0 ; j < productModelList.size() ; j++){
                                Log.d("IdProductBundle", String.valueOf(idProduct));
                                Log.d("IdProductDB", String.valueOf(productModelList.get(j).getIdProduct()));
                                if (productModelList.get(j).getIdProduct() == idProduct)
                                    tempProductModelList.add(productModelList.get(j));
                            }
                            Log.d("tempList", String.valueOf(tempProductModelList.size()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    productAdapter = new ProductAdapter(getActivity(),tempProductModelList, OldMoodFragment.this);
                    rvOld.setAdapter(productAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);



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
    public void onItemClickWeather(int position) {

    }

    @Override
    public void onItemClickCategory(int pos) {
        showBottomSheet(pos);
    }

    @Override
    public void onItemClickDeals(int pos) {

    }

    @Override
    public void onItemClickVoucher(int pos) {

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

        product_image.setImageBitmap(tempProductModelList.get(position).getBitmapImage());
        product_name.setText(tempProductModelList.get(position).getProductName());
        product_resto.setText(tempProductModelList.get(position).getProductRestoName());
        product_description.setText(tempProductModelList.get(position).getProductDescription());
        product_price.setText("P"+tempProductModelList.get(position).getProductPrice());
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
                        params.put("temp_productId", String.valueOf(tempProductModelList.get(position).getIdProduct()));
                        params.put("temp_storeId", String.valueOf(tempProductModelList.get(position).getStore_idStore()));
                        params.put("temp_usersId", String.valueOf(userId));
                        params.put("temp_productName", tempProductModelList.get(position).getProductName());
                        params.put("temp_productPrice", String.valueOf(tempProductModelList.get(position).getProductPrice()));
                        params.put("temp_productQuantity", String.valueOf(product_count));
                        params.put("temp_totalProductPrice", String.valueOf(product_count * tempProductModelList.get(position).getProductPrice()));
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