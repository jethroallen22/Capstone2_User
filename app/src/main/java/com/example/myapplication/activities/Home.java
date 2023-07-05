package com.example.myapplication.activities;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.R;
import com.example.myapplication.activities.models.NotificationModel;
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.adapters.TabFragmentAdapter;
import com.example.myapplication.interfaces.Singleton;
import com.example.myapplication.activities.models.IPModel;
import com.example.myapplication.activities.models.UserModel;
import com.example.myapplication.ui.activities.ActivitiesFragment;
import com.example.myapplication.ui.cart.CartFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.notifications.NotificationsFragment;
import com.example.myapplication.ui.payment.PaymentFragment;
import com.example.myapplication.ui.preferences.PreferencesFragment;
import com.example.myapplication.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.databinding.ActivityHomeBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    public static String name = "";
    public static int id;
    FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    ImageView iv_user_image;
    TextView tv_view_profile;
    TextView tv_user_name;
    private static String JSON_URL;
    private IPModel ipModel;
    private RequestQueue requestQueue1;
    List<UserModel> userList;
    UserModel userModel;
    String image, weather;
    Handler root, notif_root;
    Dialog filterDialog;
    float wallet;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TabFragmentAdapter adapter;
    NotificationManager manager;
    RequestQueue requestQueue;
    int lastDisplayedNotificationId = -1;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getIntent();
        if(intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name");
            id = intent.getIntExtra("id",0);
            image = intent.getStringExtra("image");
            weather = intent.getStringExtra("weather");
            wallet = intent.getFloatExtra("wallet", 0.0F);
            Log.d("HOME FRAG name", name + id + image);
        } else {
            Log.d("HOME FRAG name", "FAIL");
        }
        userList = new ArrayList();
        requestQueue1 = Singleton.getsInstance(this).getRequestQueue();
        requestQueue = Singleton.getsInstance(this).getRequestQueue();

        root = new Handler();
        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                profile_user();
                root.postDelayed(this, 1000);
            }
        }, 1000);

        notif_root = new Handler();
        notif_root.postDelayed(new Runnable() {
            @Override
            public void run() {
                homeNtoification();
                root.postDelayed(this, 5000);
            }
        }, 5000);
        Log.d("USERSIZE", String.valueOf(userList.size()));
        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        /// Set the Bundle for the specific MenuItem

        MenuItem paymentItem = navigationView.getMenu().findItem(R.id.nav_payment);
        Bundle bundle = new Bundle();
        bundle.putInt("userId", id);
        paymentItem.setIntent(new Intent().putExtras(bundle));

        // Handle the click event of the MenuItem
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",id);
                    bundle.putParcelable("user", userModel);
                    bundle.putFloat("wallet", wallet);
                    HomeFragment fragment = new HomeFragment();
                    fragment.setArguments(bundle);
//                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                } else if (item.getItemId() == R.id.nav_payment) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    bundle.putDouble("wallet", wallet);
                    PaymentFragment fragment = new PaymentFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                } else if (item.getItemId() == R.id.nav_cart){
                    Bundle bundle = new Bundle();
                    bundle.putInt("userID", id);
                    bundle.putFloat("wallet", wallet);
                    CartFragment fragment = new CartFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                } else if (item.getItemId() == R.id.nav_activities){
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    ActivitiesFragment fragment = new ActivitiesFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                } else if(item.getItemId() == R.id.nav_notifications){
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    NotificationsFragment fragment = new NotificationsFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                } else if(item.getItemId() == R.id.nav_preferences){
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    PreferencesFragment fragment = new PreferencesFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                } else if (item.getItemId() == R.id.nav_logout){
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                drawer.closeDrawers();
                return true;
            }
        });


        //LOGOUT!!!
//        navigationView.getMenu().getItem(7).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem item) {
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                return false;
//            }
//        });



        Log.d("USERSIZE", String.valueOf(userList.size()));
        iv_user_image = navigationView.getHeaderView(0).findViewById(R.id.iv_user_image);
        tv_user_name = navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        tv_view_profile = navigationView.getHeaderView(0).findViewById(R.id.tv_view_profile);


        tv_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle bundle = new Bundle()
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                //bundle.putSerializable("user", (Serializable) userList);
                bundle.putParcelable("user", userModel);
                Log.d("USERTEST: ", String.valueOf(userList.size()));

                ProfileFragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                Log.d("CLICK", "CLICK");
            }
        });


    }

//    public void filterModal(){
//        filterDialog = new Dialog(this);
//        filterDialog.setContentView(R.layout.filter_modal);
//        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        SeekBar sb_budget;
//        Button btn_confirm_filter;
//        ImageView close_modal;
//        TextView tv_set_budget;
//
////        tv_set_budget = filterDialog.findViewById(R.id.tv_set_budget);
//        sb_budget = filterDialog.findViewById(R.id.sb_budget);
//        btn_confirm_filter = filterDialog.findViewById(R.id.btn_confirm_filter);
//        close_modal = filterDialog.findViewById(R.id.close_modal);
//        int filterValue;
//
//
//        close_modal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                filterDialog.dismiss();
//            }
//        });
//
//        sb_budget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int filterValue;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                filterValue = progress;
//                String budget = "₱ " + filterValue;
//                Log.d("budget", String.valueOf(filterValue));
//                Log.d("budget", budget);
//
//                btn_confirm_filter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d("FilterValue", String.valueOf(filterValue));
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("budget", filterValue);
//                        FilterFragment fragment = new FilterFragment();
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
//                        filterDialog.dismiss();
//                    }
//                });
//
////                tv_set_budget.setText(budget);
////                Log.d("budget", tv_set_budget.getText().toString());
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//
//
//        filterDialog.show();
//    }

    public void homeNtoification(){
        JsonArrayRequest jsonArrayRequestBalance = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apihomenotifget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("notif_response", String.valueOf(response.length()));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.d("notif_id_json", String.valueOf(jsonObject.getInt("iduser") + " " + id));
                        if(jsonObject.getInt("iduser") == id && jsonObject.getString("type").equalsIgnoreCase("orderprocess")) {
                            int idnotifread = jsonObject.getInt("idnotif");
                            String description = jsonObject.getString("description");
                            Log.d("notif_id_json", String.valueOf(idnotifread + " " + lastDisplayedNotificationId));
                            if (idnotifread != lastDisplayedNotificationId) {
                                // Display the notification only if the ID is different
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My Notification");
                                builder.setContentTitle("Mosibus");
                                builder.setContentText(description);
                                builder.setSmallIcon(R.drawable.logo);
                                builder.setAutoCancel(true);
                                lastDisplayedNotificationId = idnotifread; // Update the last displayed ID

                                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                                managerCompat.notify(1, builder.build());

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
                                    NotificationManager manager = getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(channel);
                                }
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
            }
        });
        requestQueue.add(jsonArrayRequestBalance);
    }

    public void profile_user(){

        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "profile.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response Product: ", String.valueOf(response.length()));
                for (int i=0; i < response.length(); i++){
                    try {
                        //
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
                        if (jsonObjectRec1.getInt("id") == id) {

                            //USER DB
                            int id = jsonObjectRec1.getInt("id");
                            String name = jsonObjectRec1.getString("name");
                            String image = jsonObjectRec1.getString("image");
                            String email = jsonObjectRec1.getString("email");
                            String contact = jsonObjectRec1.getString("contact");
                            String password = jsonObjectRec1.getString("password");

                            userModel = new UserModel(id, image, name, email, contact, password);
                            Log.d("USERTEST: ", String.valueOf(userList.size()));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(userModel.getBitmapImage() != null) {
                    iv_user_image.setImageBitmap(userModel.getBitmapImage());
                } else
                    iv_user_image.setImageResource(R.drawable.logo);
                tv_user_name.setText(userModel.getName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OnError P: ", String.valueOf(error));
            }
        });
        requestQueue1.add(jsonArrayRequestRec1);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}