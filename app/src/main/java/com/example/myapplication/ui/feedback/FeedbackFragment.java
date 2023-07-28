package com.example.myapplication.ui.feedback;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivityAdapter;
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.databinding.FragmentFeedbackBinding;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.models.IPModel;
import com.example.myapplication.models.NotificationModel;
import com.example.myapplication.models.OrderModel;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.ordersummary.OrderSummaryFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackFragment extends Fragment {

    private FragmentFeedbackBinding binding;

    //Notification List Recycler View
    RecyclerView rv_notification;
    List<NotificationModel> notification_list;
    ActivityAdapter ActivityAdapter;

    ImageView iv_valid_id_placeholder;

    Button btn_save_feedback;

    CardView cv_upload_id;

    Bitmap bitmap;

    Spinner spinner;

    private static String JSON_URL;
    private IPModel ipModel;
    RequestQueue requestQueue;
    int userId, iduser, idorder;
    String feedback, proof, status;

    OrderModel orderModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if(bundle != null){
            orderModel = bundle.getParcelable("order");
            userId = bundle.getInt("id");
            Log.d("notif_id", String.valueOf(userId));
        }

        iv_valid_id_placeholder = root.findViewById(R.id.iv_valid_id_placeholder);
        ActivityResultLauncher<Intent> activityResultLauncherValidId = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_valid_id_placeholder.setImageBitmap(bitmap);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        cv_upload_id = root.findViewById(R.id.cv_upload_id);
        cv_upload_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherValidId.launch(intent);
            }
        });
        feedback = String.valueOf(root.findViewById(R.id.feedbacktext));
        btn_save_feedback = root.findViewById(R.id.btn_save_feedback);
        btn_save_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    final int idorder2 = orderModel.getIdOrder();
                    final String feedback2 = feedback;
                    final String status2 = "pending";

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "apifeedbackpost.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                    }
                                    catch (Throwable e) {
                                        e.printStackTrace();
                                        //Toast.makeText(Login.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("iduser", String.valueOf(orderModel.getUsers_id()));
                            paramV.put("idorder", String.valueOf(idorder2));
                            paramV.put("feedback", feedback2);
                            paramV.put("proof", base64Image);
                            paramV.put("status", status2);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);

                } else
                    Toast.makeText(getActivity().getApplicationContext(),"Please select an image first!", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                HomeFragment fragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });


        return root;
    }
}