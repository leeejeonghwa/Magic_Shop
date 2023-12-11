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

public class MyPageMainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_main);
        getWindow().setWindowAnimations(0);

        Button btn_logout = (Button) findViewById(R.id.btn_logout);

        // SessionManager를 통해 로그인 상태 확인
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        String userID = sessionManager.getUserID();
        String userPassword = sessionManager.getUserPassword();

        // 로그인 상태 확인
        if (sessionManager.isLoggedIn()) {
            // 로그인된 경우 유저 타입 확인
            String userType = sessionManager.getUserType();
            // 판매자인 경우 판매자 마이페이지로 이동
            if ("B".equals(userType)) {
                Intent intent = new Intent(getApplicationContext(), SellerMyPageMainActivity.class);
                startActivity(intent);
                finish();
                return;
            }

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
        }
        else {
            // 로그인하지 않은 경우
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        Button btn_shopping_basket = (Button) findViewById(R.id.btn_shopping_basket);
        btn_shopping_basket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        Button btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageSettingActivity.class);
                startActivity(intent);
            }
        });

        Button btn_order_delivery_list = (Button) findViewById(R.id.btn_order_delivery_list);
        btn_order_delivery_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageOrderListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_exchange_list = (Button) findViewById(R.id.btn_exchange_list);
        btn_exchange_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageExchangeWaitingListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_refund_list = (Button) findViewById(R.id.btn_refund_list);
        btn_refund_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageRefundWaitingListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_review_write = (Button) findViewById(R.id.btn_review_write);
        btn_review_write.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageUnreviewedListActivity.class);
                startActivity(intent);
            }
        });

        Button category_search_id = (Button) findViewById(R.id.category_search_id);
        category_search_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

        Button go_home_id = (Button) findViewById(R.id.go_home_id);
        go_home_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button btn_qna = (Button) findViewById(R.id.btn_qna);
        btn_qna.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageQuestionListActivity.class);
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

        LoginRequest loginRequest = new LoginRequest(MyPageMainActivity.this, userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyPageMainActivity.this);
        queue.add(loginRequest);

    }
}
