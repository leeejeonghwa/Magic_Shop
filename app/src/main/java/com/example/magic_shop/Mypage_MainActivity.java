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

public class Mypage_MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_main);
        getWindow().setWindowAnimations(0);

        Button btnLogout = (Button) findViewById(R.id.btn_logout);

        // SessionManager를 통해 로그인 상태 확인
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        String userID = sessionManager.getUserId();
        String userPassword = sessionManager.getUserPassword();

        // 로그인 상태 확인
        if (sessionManager.isLoggedIn()) {
            // 로그인된 경우 유저 타입 확인
            String userType = sessionManager.getUserType();
            // 판매자인 경우 판매자 마이페이지로 이동
            if ("B".equals(userType)) {
                Intent intent = new Intent(getApplicationContext(), Seller_MypageMainActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            btnLogout.setVisibility(View.VISIBLE);

            // 로그아웃 버튼 클릭 시 로그아웃 수행
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 로그아웃 처리
                    sessionManager.logout();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {
            // 로그인하지 않은 경우
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        Button btnShoppingBasket = (Button) findViewById(R.id.btn_shopping_basket);
        btnShoppingBasket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        Button btnSetting = (Button) findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_SettingActivity.class);
                startActivity(intent);
            }
        });

        Button btnOrderDeliveryList = (Button) findViewById(R.id.btn_order_delivery_list);
        btnOrderDeliveryList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_OrderListActivity.class);
                startActivity(intent);
            }
        });

        Button btnExchangeList = (Button) findViewById(R.id.btn_exchange_list);
        btnExchangeList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_ExchangeWaitingListActivity.class);
                startActivity(intent);
            }
        });

        Button btnRefundList = (Button) findViewById(R.id.btn_refund_list);
        btnRefundList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_RefundWaitingListActivity.class);
                startActivity(intent);
            }
        });

        Button btnReviewWrite = (Button) findViewById(R.id.btn_review_write);
        btnReviewWrite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_UnreviewedListActivity.class);
                startActivity(intent);
            }
        });

        Button categorySearchID = (Button) findViewById(R.id.category_search_id);
        categorySearchID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

        Button btnGoHomeID = (Button) findViewById(R.id.go_home_id);
        btnGoHomeID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button btnQnA = (Button) findViewById(R.id.btn_qna);
        btnQnA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_QuestionListActivity.class);
                startActivity(intent);
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_MainActivity", "서버 응답");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String userName = jsonObject.getString("userName");
                        String userID = jsonObject.getString("userID");
                        String userPassword = jsonObject.getString("userPassword");
                        String userNickname = jsonObject.getString("userNickname");
                        String userType = jsonObject.getString("userType");

                        // 닉네임을 표시할 View에 표시
                        TextView nicknameTextView = findViewById(R.id.user_name);
                        nicknameTextView.setText(userNickname);
                    } else {
                        // 서버에서 닉네임을 가져오지 못한 경우에 대한 처리
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(Mypage_MainActivity.this, userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_MainActivity.this);
        queue.add(loginRequest);

    }
}
