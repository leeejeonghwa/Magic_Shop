package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Seller_MypageMainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_mypage_main);
        getWindow().setWindowAnimations(0);

        Button btn_logout = (Button) findViewById(R.id.btn_logout);

        // SessionManager를 통해 로그인 상태 확인
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        String userID = sessionManager.getUserId();
        String userPassword = sessionManager.getUserPassword();
        String userName = sessionManager.getUserName();
        String userNickname = sessionManager.getUserNickname();
        String userType = sessionManager.getUserType();

        if (sessionManager.isLoggedIn()) {
            // 로그인한 경우
            btn_logout.setVisibility(View.VISIBLE);

            // 로그아웃 버튼 클릭 시 로그아웃 수행
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 로그아웃 처리
                    sessionManager.logout();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // 로그인하지 않은 경우
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        Button imageButton = (Button) findViewById(R.id.btn_setting);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_SettingActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_list = (Button) findViewById(R.id.btn_product_list);
        btn_product_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_register = (Button) findViewById(R.id.btn_product_register);
        btn_product_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductRegisterActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_revise = (Button) findViewById(R.id.btn_product_revise);
        btn_product_revise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductReviseActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_delete = (Button) findViewById(R.id.btn_product_delete);
        btn_product_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductDeleteActivity.class);
                startActivity(intent);
            }
        });

        Button btn_order_delivery_list = (Button) findViewById(R.id.btn_order_delivery_list);
        btn_order_delivery_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_OrderDeliveryListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_refund_exchange_list = (Button) findViewById(R.id.btn_refund_exchange_list);
        btn_refund_exchange_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_RefundExchangeListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_review_write = (Button) findViewById(R.id.btn_review_list);
        btn_review_write.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ReviewListActivity.class);
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
                        String userID = jsonObject.getString("userID");
                        String userPassword = jsonObject.getString("userPassword");
                        String userNickname = jsonObject.getString("userNickname");
                        String userType = jsonObject.getString("userType");

                        TextView userNameTextView = findViewById(R.id.seller_name);
                        userNameTextView.setText(userName);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(Seller_MypageMainActivity.this, userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Seller_MypageMainActivity.this);
        queue.add(loginRequest);
    }

}
