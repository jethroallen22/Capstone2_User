package com.example.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.models.DealsModel;
import com.example.myapplication.activities.models.HomeCategoryModel;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.activities.models.OrderItemModel;
import com.example.myapplication.activities.models.OrderModel;
import com.example.myapplication.activities.models.ProductModel;
import com.example.myapplication.activities.models.SearchModel;
import com.example.myapplication.activities.models.StoreModel;
import com.example.myapplication.activities.models.TagModel;
import com.example.myapplication.adapters.HomeCategoryAdapter;
import com.example.myapplication.adapters.HomeDealsAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.HomeStorePopularAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter;
import com.example.myapplication.adapters.HomeStoreRecAdapter2;
import com.example.myapplication.adapters.TabFragmentAdapter;
import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.interfaces.RecyclerViewInterface;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.categories.CategoryFragment;
import com.example.myapplication.ui.filter.FilterFragment;
import com.example.myapplication.ui.moods.MixMoodFragment;
import com.example.myapplication.ui.moods.NewMoodFragment;
import com.example.myapplication.ui.moods.OldMoodFragment;
import com.example.myapplication.ui.moods.TrendMoodFragment;
import com.example.myapplication.ui.search.SearchFragment;
import com.example.myapplication.ui.store.StoreFragment;
import com.example.myapplication.ui.weather.WeatherFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private static String JSON_URL;
    private IPModel ipModel;

    //Getting Bundle
    int userId = 0;
    int tempCount = 0;
    String userName = "", weather;
    HomeFragment homeFragment = this;


    float wallet;

    private static final int EARTH_RADIUS = 6371; // Radius of the Earth in kilometers

    double curLat, curLong;
    private static int moodCtr;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TabFragmentAdapter adapter;


    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("name") != null) {
            userName = intent.getStringExtra("name");
            userId = intent.getIntExtra("id", 0);
            weather = intent.getStringExtra("weather");
            wallet = intent.getFloatExtra("wallet", 0);
            curLat = intent.getDoubleExtra("lat",0);
            curLong = intent.getDoubleExtra("long",0);

            Log.d("HOME FRAG name", userName + userId + " Weather: " + weather);
            Log.d("HOMEuserID", String.valueOf(userId));
            Log.d("weatherHomeFrag", weather);
            Log.d("curLatHome", String.valueOf(curLat));

        } else {
            Log.d("HOME FRAG name", "FAIL");
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            wallet = bundle.getFloat("wallet");
        }

        // Initialize views
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager2 = root.findViewById(R.id.viewPager2);
        viewPager2.setUserInputEnabled(false);

        // Add tabs
        tabLayout.addTab(tabLayout.newTab().setText("For You"));
        tabLayout.addTab(tabLayout.newTab().setText("Home"));

        // Set up adapter
        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new TabFragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Set up page change listener
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                CartFragment fragment = new CartFragment();
                bundle.putInt("userID", userId);
                bundle.putFloat("wallet", wallet);
                fragment.setArguments(bundle);
                Log.d("Bundling tempOrderItemList", String.valueOf(bundle.size()));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });

        return root;
    }

    public static double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        // Convert latitude and longitude to radians
        double startLatitudeRad = Math.toRadians(startLatitude);
        double startLongitudeRad = Math.toRadians(startLongitude);
        double endLatitudeRad = Math.toRadians(endLatitude);
        double endLongitudeRad = Math.toRadians(endLongitude);

        // Calculate the difference between latitudes and longitudes
        double latitudeDiff = endLatitudeRad - startLatitudeRad;
        double longitudeDiff = endLongitudeRad - startLongitudeRad;

        // Calculate the distance using Haversine formula
        double a = Math.sin(latitudeDiff / 2) * Math.sin(latitudeDiff / 2)
                + Math.cos(startLatitudeRad) * Math.cos(endLatitudeRad)
                * Math.sin(longitudeDiff / 2) * Math.sin(longitudeDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        DecimalFormat df = new DecimalFormat(".#");
        double distance = Double.parseDouble(df.format(EARTH_RADIUS * c));

        return distance;
    }

}
