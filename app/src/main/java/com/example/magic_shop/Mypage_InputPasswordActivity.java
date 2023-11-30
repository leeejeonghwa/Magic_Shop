package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Mypage_InputPasswordActivity extends AppCompatActivity {
    private EditText et_pass;
    private SessionManager sessionManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mypage_activity_input_password);
        et_pass = findViewById(R.id.editTextCurrentPassword);
        getWindow().setWindowAnimations(0);
        sessionManager = new SessionManager(getApplicationContext());

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_SettingActivity.class);
                startActivity(intent);
            }
        });

        Button btn_change_password_check = (Button) findViewById(R.id.btn_change_password_check);
        btn_change_password_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String pass = et_pass.getText().toString();
                // 세션 매니저를 통해 현재 사용자 ID 및 비밀번호 가져오기
                String userID = sessionManager.getUserId();
                String userPassword = sessionManager.getUserPassword();

                // 기존 비밀번호 확인
                if (pass.equals(userPassword)) {
                    Intent intent = new Intent(Mypage_InputPasswordActivity.this, Mypage_UserInfoChangeActivity.class);
                    startActivity(intent);
                    finish(); // 현재 화면 종료
                } else {
                    // 기존 비밀번호 확인 실패
                    Toast.makeText(Mypage_InputPasswordActivity.this, "기존 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }


    });
        }
            }
