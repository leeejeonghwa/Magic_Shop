package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Seller_ChangePasswordActivity extends AppCompatActivity {

    private EditText et_pass, et_newpass, et_newpass_check;
    private SessionManager sessionManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setWindowAnimations(0);

        et_pass = findViewById(R.id.editTextCurrentPassword);
        et_newpass = findViewById(R.id.editTextNewPassword);
        et_newpass_check = findViewById(R.id.editTextNewPasswordCheck);

        sessionManager = new SessionManager(getApplicationContext());

        Button btn_back = findViewById(R.id.btn_back);
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

        Button btn_change = findViewById(R.id.btn_change_password_confirm);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = et_pass.getText().toString();
                String user_new_pass = et_newpass.getText().toString();
                String user_new_pass_check = et_newpass_check.getText().toString();
                // 세션 매니저를 통해 현재 사용자 ID 및 비밀번호 가져오기
                String userID = sessionManager.getUserId();
                String userPassword = sessionManager.getUserPassword();

                if (!user_new_pass.equals(user_new_pass_check)) {
                    // 새로운 비밀번호와 확인용 비밀번호가 일치하지 않는 경우 처리
                    Toast.makeText(Seller_ChangePasswordActivity.this, "새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 기존 비밀번호 확인
                if (pass.equals(userPassword)) {
                    // 기존 비밀번호 확인 성공
                    sendChangePasswordRequest(userID, user_new_pass);
                } else {
                    // 기존 비밀번호 확인 실패
                    Toast.makeText(Seller_ChangePasswordActivity.this, "기존 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendChangePasswordRequest(String userID, String newPassword) {
        Log.d("Mypage_ChangePassword", "비밀번호 변경 요청: userID=" + userID);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // 서버 응답에서 "userId" 키가 있는지 확인
                    if (jsonObject.has("userId")) {
                        String userID = jsonObject.getString("userId");
                        // TODO: 이제 userID를 사용할 수 있습니다.
                    }

                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        // 변경 성공에 대한 처리
                        Log.d("Mypage_ChangePassword", "비밀번호 변경 성공");
                        Toast.makeText(Seller_ChangePasswordActivity.this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        // 변경 성공 후 다음 화면으로 이동하는 코드 추가
                        Intent intent = new Intent(Seller_ChangePasswordActivity.this, Mypage_SettingActivity.class);
                        startActivity(intent);
                    } else {
                        // 변경 실패에 대한 처리
                        Log.d("Mypage_ChangePassword", "비밀번호 변경 실패");
                        Toast.makeText(Seller_ChangePasswordActivity.this, "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON 파싱 오류가 발생한 경우
                    Log.e("Mypage_ChangePassword", "서버 응답 파싱 오류: " + response);
                    Toast.makeText(Seller_ChangePasswordActivity.this, "서버 응답을 처리하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // 비밀번호 변경 요청
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(userID, newPassword, responseListener);
        RequestQueue changePasswordQueue = Volley.newRequestQueue(Seller_ChangePasswordActivity.this);
        changePasswordQueue.add(changePasswordRequest);
    }
}

