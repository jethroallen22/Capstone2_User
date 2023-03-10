package com.example.myapplication.ui.notifications;

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
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    //Notification List Recycler View
    RecyclerView rv_notification;
    List<NotificationModel> notification_list;
    NotificationAdapter notificationAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_notification = root.findViewById(R.id.rv_notification);
        notification_list = new ArrayList<>();
        notification_list.add(new NotificationModel(R.drawable.burger_mcdo,"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas a sem risus. Suspendisse potenti. Fusce vel commodo est. Phasellus congue odio a lacus blandit elementum."));
        notification_list.add(new NotificationModel(R.drawable.chicken_joy,"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas a sem risus. Suspendisse potenti. Fusce vel commodo est. Phasellus congue odio a lacus blandit elementum."));
        notification_list.add(new NotificationModel(R.drawable.whopper_king,"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas a sem risus. Suspendisse potenti. Fusce vel commodo est. Phasellus congue odio a lacus blandit elementum."));
        notificationAdapter = new NotificationAdapter(getActivity(),notification_list);
        rv_notification.setAdapter(notificationAdapter);
        rv_notification.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        rv_notification.setHasFixedSize(true);
        rv_notification.setNestedScrollingEnabled(false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}