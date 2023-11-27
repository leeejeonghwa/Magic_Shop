package com.example.magic_shop;

import android.content.Intent;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_nickname;
    private Button btn_register, btnCheckDuplicate;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        et_nickname = findViewById(R.id.et_nickname);
        radioGroup = findViewById(R.id.radio_group);

        btnCheckDuplicate = findViewById(R.id.btn_duplicate_confirmation);
        btnCheckDuplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                CheckDuplicateRequest checkDuplicateRequest = new CheckDuplicateRequest(userID, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isDuplicate = jsonObject.getBoolean("isDuplicate");

                            if (isDuplicate) {
                                showAlert("이미 사용 중인 아이디입니다.");
                            } else {
                                Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(checkDuplicateRequest);
            }
        });

        btn_register = findViewById(R.id.register_button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                String userNickname = et_nickname.getText().toString();
                int userType = getUserType();

                CheckDuplicateRequest checkDuplicateRequest = new CheckDuplicateRequest(userID, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isDuplicate = jsonObject.getBoolean("isDuplicate");

                            if (isDuplicate) {
                                showAlert("이미 사용 중인 아이디입니다.");
                            } else {
                                performRegistration(userName, userID, userPass, userNickname, userType);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(checkDuplicateRequest);
            }
        });
    }

    private int getUserType() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radio_button_purchaser) {
            return 0; // "구매자"
        } else if (selectedId == R.id.radio_button_seller) {
            return 1; // "판매자"
        }

        return -1; // 기본값 또는 선택되지 않은 경우
    }

    private void performRegistration(String userName, String userID, String userPass, String userNickname, int userType) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(userName, userID, userPass, userNickname, userType, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(message)
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}
