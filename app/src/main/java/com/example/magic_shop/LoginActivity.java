package com.example.magic_shop;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText userIDEditText, passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.loginpage);
        getWindow().setWindowAnimations(0);

        userIDEditText = findViewById(R.id.et_id);
        passEditText = findViewById(R.id.et_pass);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        Button btnBack = findViewById(R.id.btn_back);
        Button btnManage = findViewById(R.id.manage_btn);

        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
                startActivity(intent);
            }
        });


        // 회원가입 버튼을 클릭 시 수행
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = userIDEditText.getText().toString();
                System.out.println(userID);
                String userPassword = passEditText.getText().toString();
                System.out.println(userPassword);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                String userID = jsonObject.getString("userID");
                                String userPassword = jsonObject.getString("userPassword");
                                String userName = jsonObject.getString("userName");
                                String userNickname = jsonObject.getString("userNickname");
                                String userType = jsonObject.getString("userType");

                                // SessionManager를 통해 로그인 여부를 true로 설정
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.setUserInfo(true, userID, userPassword, userName, userNickname, userType);

                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                                // 사용자 유형에 따라 리다이렉션 설정
                                Intent intent;
                                if ("A".equals(userType)) {
                                    intent = new Intent(LoginActivity.this, MyPageMainActivity.class);
                                } else if ("B".equals(userType)) {
                                    intent = new Intent(LoginActivity.this, SellerMyPageMainActivity.class);
                                } else {
                                    // 다른 사용자 유형 처리 또는 오류 표시
                                    Toast.makeText(getApplicationContext(), "유효하지 않은 사용자 유형", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                intent.putExtra("userID", userID);
                                intent.putExtra("userPassword", userPassword);
                                startActivity(intent);
                            } else { // 로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(LoginActivity.this, userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
