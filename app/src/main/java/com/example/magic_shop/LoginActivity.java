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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        getWindow().setWindowAnimations(0);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);


        // 회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString();
                System.out.println(userID);
                String userPass = et_pass.getText().toString();
                System.out.println(userPass);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                // 로그인 성공 시 로그인 상태를 저장
                                // SessionManager를 통해 로그인 여부를 true로 설정
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.setLogin(true);

                                String userID = jsonObject.getString("userID");
                                String userPassword = jsonObject.getString("userPassword");
                                String userType = jsonObject.getString("userType"); // 사용자 유형 추가

                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                                // 사용자 유형에 따라 리다이렉션 설정
                                Intent intent;
                                if ("A".equals(userType)) {
                                    intent = new Intent(LoginActivity.this, Mypage_MainActivity.class);
                                } else if ("B".equals(userType)) {
                                    intent = new Intent(LoginActivity.this, Seller_MypageMainActivity.class);
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
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(LoginActivity.this, userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });


    }
}
