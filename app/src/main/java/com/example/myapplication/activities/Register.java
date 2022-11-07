package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText register_name_text_input, register_email_text_input,
            register_number_text_input, register_password_text_input;
    private Button signup_btn;
    private TextView tv_login_btn;

    //Workspace IP
    private static String URL_SIGNUP = "http://10.11.1.164/android_register_login/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        init();

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Register.this.startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rname = register_name_text_input.getText().toString().trim();
                String remail = register_email_text_input.getText().toString().trim();
                String rnumber = register_number_text_input.getText().toString().trim();
                String rpassword = register_password_text_input.getText().toString().trim();

                SignUp(rname, remail, rnumber,rpassword);
            }
        });

    }

    public void init(){
        signup_btn = (Button) findViewById(R.id.signup_btn);
        tv_login_btn = (TextView) findViewById(R.id.tv_login_btn);
    }

    private void SignUp(String register_name_text_input,  String register_email_text_input,
                        String register_number_text_input, String register_password_text_input){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    /*
                    JSONArray jsonArray = jsonObject.getJSONArray("login");


                    if (success.equals("1")){
                        for (int i = 0; i < jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name").trim();
                            String email = object.getString("email").trim();

                            Toast.makeText(Login.this, "Success Login. \nYour Name : "
                                    + name + "\nYour Email : "
                                    + email, Toast.LENGTH_SHORT).show();
                        }
                    }
                     */
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    Register.this.startActivity(intent);
                } catch (JSONException e) {
                    /*
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Error! "+ e.toString(),Toast.LENGTH_SHORT).show();*/

                    Toast.makeText(Register.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", register_name_text_input);
                params.put("email", register_email_text_input);
                params.put("number", register_number_text_input);
                params.put("password", register_password_text_input);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}