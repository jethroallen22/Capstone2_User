package com.example.myapplication.ui.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AddonAdapter;
import com.example.myapplication.adapters.ChooseAdapter;
import com.example.myapplication.databinding.FragmentProductBinding;
import com.example.myapplication.activities.models.AddonModel;
import com.example.myapplication.activities.models.ChooseModel;
import com.example.myapplication.activities.models.ProductModel;
import com.example.myapplication.activities.models.StoreModel;
import com.example.myapplication.ui.store.StoreFragment;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;

    //Choose Recycler View
    RecyclerView rv_choose;
    List<ChooseModel> choose_list;
    ChooseAdapter chooseAdapter;

    //Addon Recyler View
    RecyclerView rv_addon;
    List<AddonModel> addon_list;
    AddonAdapter addonAdapter;

    TextView product_name;
    TextView product_price;
    ImageView product_image;
    TextView product_description;

    ProductModel productModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Initialize
        product_image = root.findViewById(R.id.iv_product_imagee);
        product_name = root.findViewById(R.id.tv_product_namee);
        product_description = root.findViewById(R.id.tv_product_description);
        product_price = root.findViewById(R.id.tv_product_pricee);


        Bundle bundle = this.getArguments();
        String prod_image;
        String prod_name;
        String prod_description;
        Float prod_price;

        if(bundle != null){
            if (bundle.getParcelable("ProductClass") != null){
                productModel = (ProductModel) (bundle.getParcelable("ProductClass"));
                prod_image = productModel.getProductImage();
                prod_name = productModel.getProductName();
                prod_description = productModel.getProductDescription();
                prod_price = productModel.getProductPrice();

                Glide.with(getActivity().getApplicationContext())
                        .load(prod_image)
                        .into(product_image);
                product_name.setText(prod_name);
                product_description.setText(prod_description);
                product_price.setText("P " + prod_price);
            }
        }

        rv_choose = root.findViewById(R.id.rv_choose);
        choose_list = new ArrayList<>();

        choose_list.add(new ChooseModel("Coke", 25F));
        choose_list.add(new ChooseModel("Sprite", 25F));
        choose_list.add(new ChooseModel("Royal", 25F));
        choose_list.add(new ChooseModel("Iced Coffee", 25F));
        choose_list.add(new ChooseModel("Water", 25F));

        chooseAdapter = new ChooseAdapter(getActivity(),choose_list);
        rv_choose.setAdapter(chooseAdapter);
        rv_choose.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_choose.setHasFixedSize(true);
        rv_choose.setNestedScrollingEnabled(false);

        rv_addon = root.findViewById(R.id.rv_addon);
        addon_list = new ArrayList<>();

        addon_list.add(new AddonModel("Coke", 25F));
        addon_list.add(new AddonModel("Sprite", 25F));
        addon_list.add(new AddonModel("Royal", 25F));
        addon_list.add(new AddonModel("Iced Coffee", 25F));
        addon_list.add(new AddonModel("Water", 25F));

        addonAdapter = new AddonAdapter(getActivity(),addon_list);
        rv_addon.setAdapter(addonAdapter);
        rv_addon.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_addon.setHasFixedSize(true);
        rv_addon.setNestedScrollingEnabled(false);

        binding.fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test","Success1");
                //Navigation.findNavController(view).navigate(R.id.action_nav_product_to_nav_home);

                /* storeFragment = new StoreFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_home, storeFragment);
                transaction.commit();*/
                //storeFragment.setArguments(bundle);
                //Log.d("TAG", "Success");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,storeFragment).commit();

                /*bundle.putInt ("StoreImage", store_image);
                bundle.putString("StoreName", store_name);
                bundle.putString("StoreAddress", store_address);
                bundle.putString("StoreCategory", store_category);*/
                List<StoreModel> list = new ArrayList<>();
                list = (List<StoreModel>) getArguments().getSerializable("StoreList");

                for (int i = 0 ; i < list.size() ; i++){
                    if(list.get(i).getStore_id() == productModel.getStore_idStore()){
                        Bundle bundle = new Bundle();
                        StoreFragment fragment = new StoreFragment();
                        bundle.putParcelable("StoreClass", list.get(i));
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                    }
                }
//                StoreFragment storeFragment = new StoreFragment();
//                storeFragment.setArguments(bundle);
//                Log.d("TAG", "Success");
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,storeFragment).commit();
//                Log.d("Test","Success2");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}