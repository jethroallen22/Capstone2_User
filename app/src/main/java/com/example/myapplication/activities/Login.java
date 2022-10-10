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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText login_email_text_input, login_password_text_input;
    private Button login_btn;
    private TextView tv_register_btn;
    private static String URL_LOGIN = "http://10.187.184.154/android_register_login/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        init();

        tv_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Login.this.startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input checker
                String mEmail = login_email_text_input.getText().toString().trim();
                String mPass = login_password_text_input.getText().toString().trim();

                if (mEmail.isEmpty() || mPass.isEmpty()){
                    if (mEmail.isEmpty())
                        login_email_text_input.setError("Please insert Email!");
                    if (mPass.isEmpty())
                        login_password_text_input.setError("Please insert Password!");
                } else {
                    LogIn(mEmail, mPass);
                }
            }
        });
    }

    public void init(){
        login_email_text_input = findViewById(R.id.login_email_text_input);
        login_password_text_input = findViewById(R.id.login_password_text_input);

        login_btn = findViewById(R.id.login_btn);
        tv_register_btn = findViewById(R.id.tv_register_btn);
    }

    //Login
    private void LogIn(String login_email_text_input, String login_password_text_input){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
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
                    Login.this.startActivity(intent);
                } catch (JSONException e) {
                    /*
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Error! "+ e.toString(),Toast.LENGTH_SHORT).show();*/

                    Toast.makeText(Login.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", login_email_text_input);
                params.put("password", login_password_text_input);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}