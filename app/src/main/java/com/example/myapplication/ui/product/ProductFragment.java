package com.example.myapplication.ui.product;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AddonAdapter;
import com.example.myapplication.adapters.ChooseAdapter;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.databinding.FragmentProductBinding;
import com.example.myapplication.models.AddonModel;
import com.example.myapplication.models.ChooseModel;
import com.example.myapplication.ui.cart.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private ProductViewModel mViewModel;
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



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductViewModel cartViewModel =
                new ViewModelProvider(this).get(ProductViewModel.class);

        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        product_image = root.findViewById(R.id.iv_product_imagee);
        product_name = root.findViewById(R.id.tv_product_namee);
        product_description = root.findViewById(R.id.tv_product_description);
        product_price = root.findViewById(R.id.tv_product_pricee);


        Bundle bundle = this.getArguments();
        int prod_image = bundle.getInt("Image");
        String prod_name = bundle.getString("Name");
        String prod_description = bundle.getString("Description");
        Float prod_price = bundle.getFloat("Price");

        product_image.setImageResource(prod_image);
        product_name.setText(prod_name);
        product_price.setText("P " + prod_price.toString());
        product_description.setText(prod_description);


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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}