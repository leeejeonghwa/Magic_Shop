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

public class MyPageSettingActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_setting);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        String userID = sessionManager.getUserID();
        String userPassword = sessionManager.getUserPassword();
//        String userName = sessionManager.getUserName();
//        String userNickname = sessionManager.getUserNickname();
//        String userType = sessionManager.getUserType();

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button btnHome = (Button) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button btnUserInfo = (Button) findViewById(R.id.btn_user_info);
        btnUserInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageInputPasswordActivity.class);
                startActivity(intent);
            }
        });

        Button btnChangePassword = (Button) findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        Button btnDeliveryAddressManage = (Button) findViewById(R.id.btn_delivery_address_manage);
        btnDeliveryAddressManage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageDeliveryAddressManageActivity.class);
                startActivity(intent);
            }
        });

        Button btnRefundAccountManage = (Button) findViewById(R.id.btn_refund_account_manage);
        btnRefundAccountManage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageRefundAccountManageActivity.class);
                startActivity(intent);
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_SettingActivity", "서버 응답");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String userName = jsonObject.getString("userName");
                        String userNickname = jsonObject.getString("userNickname");


                        TextView userNameTextView = findViewById(R.id.user_name_view);
                        TextView userNicknameTextView = findViewById(R.id.nickname_view);

                        userNameTextView.setText(userName);
                        userNicknameTextView.setText(userNickname);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(MyPageSettingActivity.this, userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyPageSettingActivity.this);
        queue.add(loginRequest);


        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("refund", "서버 응답1");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String bank = jsonObject.getString("bank");
                        String account = jsonObject.getString("account_number");


                        TextView accountview = findViewById(R.id.account_view);
                        TextView bankview = findViewById(R.id.bankname_view);

                        bankview.setText(bank);
                        accountview.setText(account);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RefundDataRequest refunddataRequest= new RefundDataRequest(userID, responseListener1, null );
        RequestQueue queue1 = Volley.newRequestQueue(MyPageSettingActivity.this);
        queue1.add(refunddataRequest);
    }

}
