package com.example.myapplication.ui.notifications;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.NotificationModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    //Notification List Recycler View
    RecyclerView rv_notification;
    List<NotificationModel> notification_list;
    NotificationAdapter notificationAdapter;

    private static String JSON_URL;
    private IPModel ipModel;
    RequestQueue requestQueue;
    int userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if(bundle != null){
            userId = bundle.getInt("id");
            Log.d("notif_id", String.valueOf(userId));
        }

        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();
        rv_notification = root.findViewById(R.id.rv_notification);
        notification_list = new ArrayList<>();
        getNotification();
        return root;
    }

    public void getNotification(){
        JsonArrayRequest jsonArrayRequestBalance = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apinotifget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("notif_response", String.valueOf(response.length()));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.d("notif_id_json", String.valueOf(jsonObject.getInt("iduser") + " " + userId));
                        if(jsonObject.getInt("iduser") == userId || jsonObject.getInt("iduser") == 0) {
                            int iduser = jsonObject.getInt("iduser");
                            String title = jsonObject.getString("title");
                            String description = jsonObject.getString("description");
                            String type = jsonObject.getString("type");
                            String date = jsonObject.getString("date");
                            Log.d("notif_match", "MATCH");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            NotificationModel notificationModel = new NotificationModel(title, description, type, dateFormat.parse(date));

                            Date curDate = new Date();
                            try {
                                Date dateTemp = dateFormat.parse(date);
                                long timeDifference = curDate.getTime() - dateTemp.getTime();
                                long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifference);

                                if (daysDifference <= 7) {
                                    notification_list.add(notificationModel);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.d("ParseException", e.getMessage());
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (NotificationModel notif: notification_list)
                    Log.d("notif", notif.getDate() + " " + notif.getTitle());
                notificationAdapter = new NotificationAdapter(getActivity(),notification_list);
                rv_notification.setAdapter(notificationAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_notification.setLayoutManager(layoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
            }
        });
        requestQueue.add(jsonArrayRequestBalance);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}