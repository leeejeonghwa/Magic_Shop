package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        Button btn_manager = (Button) findViewById(R.id.btn_manager);
        btn_manager.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager_MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_seller = (Button) findViewById(R.id.btn_seller);
        btn_seller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_MypageMainActivity.class);
                startActivity(intent);
            }
        });

        Button imageButton = (Button) findViewById(R.id.btn_setting);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_SettingActivity.class);
                startActivity(intent);
            }
        });

        Button btn_order_delivery_list = (Button) findViewById(R.id.btn_order_delivery_list);
        btn_order_delivery_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_OrderDeliveryListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_refund_exchange_list = (Button) findViewById(R.id.btn_refund_exchange_list);
        btn_refund_exchange_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_RefundExchangeListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_review_write = (Button) findViewById(R.id.btn_review_write);
        btn_review_write.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_UnreviewedListActivity.class);
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

        String userID = getIntent().getStringExtra("userID"); // 로그인 액티비티에서 전달받은 아이디
        String userPassword = getIntent().getStringExtra("userPassword"); // 로그인 액티비티에서 전달받은 비밀번

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
                        String userClassification = jsonObject.getString("userClassification");

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

        LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_MainActivity.this);
        queue.add(loginRequest);

    }

}
