package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Seller_SettingActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_setting);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        String userID = sessionManager.getUserId();
        String userPassword = sessionManager.getUserPassword();
        String userName = sessionManager.getUserName();
        String userNickname = sessionManager.getUserNickname();
        String userType = sessionManager.getUserType();

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_home = (Button) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button btn_user_info = (Button) findViewById(R.id.btn_user_info);
        btn_user_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_InputPasswordActivity.class);
                startActivity(intent);
            }
        });

        Button btn_change_password = (Button) findViewById(R.id.btn_change_password);
        btn_change_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        Button btn_refund_account_manage = (Button) findViewById(R.id.btn_refund_account_manage);
        btn_refund_account_manage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_RefundAccountManageActivity.class);
                startActivity(intent);
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Seller_SettingActivity", "서버 응답");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String userName = jsonObject.getString("userName");
                        String userID = jsonObject.getString("userID");
                        String userPassword = jsonObject.getString("userPassword");
                        String userNickname = jsonObject.getString("userNickname");
                        String userType = jsonObject.getString("userType");

                        TextView userNameTextView = findViewById(R.id.user_name_view);
                        userNameTextView.setText(userName);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(Seller_SettingActivity.this, userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Seller_SettingActivity.this);
        queue.add(loginRequest);
    }

}
