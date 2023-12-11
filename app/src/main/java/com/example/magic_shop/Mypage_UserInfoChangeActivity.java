package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Mypage_UserInfoChangeActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private EditText nicknameEditText, nameEditText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_change);
        getWindow().setWindowAnimations(0);

        nameEditText = findViewById(R.id.editTextName);
        nicknameEditText = findViewById(R.id.editTextUserNickname);
        Button btnNameChange = findViewById(R.id.btn_name_change);
        Button btnNicknameChange = findViewById(R.id.btn_user_nickname_change);

        sessionManager = new SessionManager(getApplicationContext());


        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
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


        btnNameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 입력한 이름 가져오기
                String name = nameEditText.getText().toString();

                // 세션 매니저를 사용하여 사용자 ID 및 비밀번호 가져오기
                String userID = sessionManager.getUserId();
                //String userPassword = sessionManager.getUserPassword();

                // 서버에 이름 변경 요청 보내기
                ChangenameRequest changenameRequest = new ChangenameRequest(userID, name, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                // 이름 변경 성공
                                Log.d("NameChange", "Name changed successfully");
                                Intent intent = new Intent(Mypage_UserInfoChangeActivity.this, Mypage_SettingActivity.class);
                                startActivity(intent);
                                finish();                            }

                            else {
                                // 이름 변경 실패
                                Log.d("NameChange", "Failed to change name");
                                // 이후 필요한 UI 업데이트나 다른 작업 수행
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // RequestQueue에 요청을 추가
                RequestQueue requestQueue = Volley.newRequestQueue(Mypage_UserInfoChangeActivity.this);
                requestQueue.add(changenameRequest);
            }
        });

        btnNicknameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 입력한 이름 가져오기
                String nickname = nicknameEditText.getText().toString();

                // 세션 매니저를 사용하여 사용자 ID 및 비밀번호 가져오기
                String userID = sessionManager.getUserId();

                // 서버에 이름 변경 요청 보내기
                ChangenicknameRequest changenicknameRequest = new ChangenicknameRequest(userID,nickname, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                // 이름 변경 성공
                                Log.d("nicknameChange", "nickName changed successfully");
                                // 이후 필요한 UI 업데이트나 다른 작업 수행
                                // 변경 성공 후 다음 화면으로 이동하는 코드 추가
                                Intent intent = new Intent(Mypage_UserInfoChangeActivity.this, Mypage_SettingActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 이름 변경 실패
                                Log.d("nicknameChange", "Failed to change name");
                                // 이후 필요한 UI 업데이트나 다른 작업 수행
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // RequestQueue에 요청을 추가
                RequestQueue requestQueue = Volley.newRequestQueue(Mypage_UserInfoChangeActivity.this);
                requestQueue.add(changenicknameRequest);
            }
        });




    }
}
