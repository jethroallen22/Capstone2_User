package com.example.myapplication.ui.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.RealPathUtil;
import com.example.myapplication.activities.Home;
import com.example.myapplication.activities.Login;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.models.OrderItemModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;

    private static String JSON_URL = "http://10.154.162.184/mosibus_php/user/";

    ImageView iv_image_placeholder, iv_edit_name, iv_edit_email, iv_edit_password, iv_edit_contact , iv_save_name, iv_save_email, iv_save_password, iv_save_contact;
    CardView cv_edit_picture;
    EditText tv_edit_name, tv_edit_password, tv_edit_email, tv_edit_contact;
    Button btn_save_edit;
    String image, name, email, contact, password;
    UserModel userModel;
    List<UserModel> userModelList;
    int id;
    Bitmap bitmap;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userModelList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            userModelList = (List<UserModel>) bundle.getSerializable("user");
            Log.d("LISTSIZE", String.valueOf(userModelList.size()));
            id = bundle.getInt("id");
            for (int i = 0 ; i < userModelList.size() ; i++){
                if(id == userModelList.get(i).getId()){
                    name = userModelList.get(i).getName();
                    email = userModelList.get(i).getEmail();
                    password = userModelList.get(i).getPassword();
                    contact = userModelList.get(i).getContact();
                    bitmap = userModelList.get(i).getBitmapImage();
                    Log.d("TESTNAME: ", userModelList.get(i).getName());
                }
            }

        }

        //Initialize views
        iv_image_placeholder = root.findViewById(R.id.iv_profile_placeholder);
        cv_edit_picture = root.findViewById(R.id.cv_edit_picture);
        tv_edit_name = root.findViewById(R.id.tv_profile_name);
        tv_edit_email = root.findViewById(R.id.tv_profile_email);
        tv_edit_contact = root.findViewById(R.id.tv_profile_contact);
        btn_save_edit = root.findViewById(R.id.btn_save_edit);
        iv_edit_name = root.findViewById(R.id.iv_edit_name);
        iv_edit_email = root.findViewById(R.id.iv_edit_email);
        iv_edit_contact = root.findViewById(R.id.iv_edit_contact);
        iv_save_name = root.findViewById(R.id.iv_save_name);
        iv_save_email = root.findViewById(R.id.iv_save_email);
        iv_save_contact = root.findViewById(R.id.iv_save_contact);

        //Disable edit texts
        tv_edit_name.setEnabled(false);
        tv_edit_email.setEnabled(false);
        tv_edit_contact.setEnabled(false);

        //Set default profile image
        iv_image_placeholder.setImageBitmap(bitmap);

        //Set edit text hints
        tv_edit_name.setHint(name);
        tv_edit_email.setHint(email);
        tv_edit_contact.setHint(String.valueOf(contact));

        iv_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_name.setEnabled(true);
            }
        });

        iv_edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_email.setEnabled(true);
            }
        });

        iv_edit_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_contact.setEnabled(true);
            }
        });

        iv_save_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_name.setEnabled(false);
                name = tv_edit_name.getText().toString().trim();
                Log.d("EDIT", name);
            }
        });

        iv_save_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_save_email.setEnabled(false);
                email = tv_edit_email.toString().trim();
            }
        });

        iv_save_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_save_contact.setEnabled(false);
                password = tv_edit_contact.toString().trim();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_image_placeholder.setImageBitmap(bitmap);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        cv_edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        btn_save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    final String name2 = name;
                    final String email2 = email;
                    final String contact2 = contact;
                    final String password2 = password;

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    String url ="update_profile.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "update_prof.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
//
//                                            Bundle bundle = new Bundle();
//                                            UserModel userModel = new UserModel(id, base64Image, name2, email2, contact2, password2);
//                                            bundle.putParcelable("user", userModel);
//                                            HomeFragment fragment = new HomeFragment();
//                                            fragment.setArguments(bundle);
//                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

                                            Intent intent = new Intent(getContext(), Home.class);
                                            intent.putExtra("name",name2);
                                            intent.putExtra("id",id);
                                            intent.putExtra("image", base64Image);
                                            intent.putExtra("email", email2);
                                            intent.putExtra("contact", contact2);
                                            intent.putExtra("password", password2);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
//                                        }
                                    }
                                    catch (Throwable e) {
                                        Log.d("Catch", String.valueOf(e));
                                        //Toast.makeText(Login.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("id", String.valueOf(id));
                            paramV.put("image", base64Image);
                            paramV.put("name", name2);
                            paramV.put("email", email2);
                            paramV.put("contact", contact2);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Context context = getActivity();
                path = RealPathUtil.getRealPath(context, uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                iv_image_placeholder.setImageBitmap(bitmap);
                Log.d("IMAGE", String.valueOf(iv_image_placeholder.getDrawable()));
            }
        } catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}