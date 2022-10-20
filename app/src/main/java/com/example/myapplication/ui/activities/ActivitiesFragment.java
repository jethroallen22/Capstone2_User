package com.example.myapplication.ui.activities;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivityAdapter;
import com.example.myapplication.adapters.HomeFoodForYouAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.databinding.FragmentActivitiesBinding;
import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.models.ActivityModel;
import com.example.myapplication.models.HomeFoodForYouModel;
import com.example.myapplication.ui.cart.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesFragment extends Fragment {

    private ActivitiesViewModel mViewModel;
    private FragmentActivitiesBinding binding;

    //Activity List Recycler View
    RecyclerView rv_activity;
    List<ActivityModel> activity_list;
    ActivityAdapter activityAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ActivitiesViewModel activitiesViewModel =
                new ViewModelProvider(this).get(ActivitiesViewModel.class);

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_activity = root.findViewById(R.id.rv_activities);
        activity_list = new ArrayList<>();
        activity_list.add(new ActivityModel("Mcdo","143 Evangelista Street, Barangay Banalo, Bacoor City",309F,"18 Oct 2022, 9:00PM"));
        activity_list.add(new ActivityModel("Burger King","Esterling Heights Subdivision, Guintorilan City",421F,"17 Oct 2022, 9:00PM"));
        activity_list.add(new ActivityModel("Burger King - Cancelled","Esterling Heights Subdivision, Guintorilan City",421F,"17 Oct 2022, 9:00PM"));
        activityAdapter = new ActivityAdapter(getActivity(),activity_list);
        rv_activity.setAdapter(activityAdapter);
        rv_activity.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_activity.setHasFixedSize(true);
        rv_activity.setNestedScrollingEnabled(false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}