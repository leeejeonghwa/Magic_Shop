package com.example.magic_shop;

import android.content.Intent;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_confirm_password, et_name, et_nickname;
    private Button btn_register, btnCheckDuplicate,backButton;
    private RadioGroup radioGroup;
    private Response.ErrorListener errorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_pass);
        et_name = findViewById(R.id.et_name);
        et_nickname = findViewById(R.id.et_nickname);
        radioGroup = findViewById(R.id.radio_group);

        btnCheckDuplicate = findViewById(R.id.btn_duplicate_confirmation);
        btnCheckDuplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RegisterActivity", "중복확인 버튼 클릭됨");
                String userID = et_id.getText().toString();

                // 백그라운드 스레드에서 실행
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkDuplicate(userID);
                    }
                }).start();
            }
        });


        backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);} });


        btn_register = findViewById(R.id.register_button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RegisterActivity", "회원가입 버튼 클릭됨");
                String userID = et_id.getText().toString();
                String password = et_pass.getText().toString();
                String confirmPassword = et_confirm_password.getText().toString();

                if (!password.equals(confirmPassword)) {
                    showAlert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                } else {
                    String userType = getUserType(); // Now returns a String ("A" or "B")
                    if (!userType.isEmpty()) {
                        performRegistration(
                                et_name.getText().toString(),
                                userID,
                                password,
                                et_nickname.getText().toString(),
                                userType
                        );
                    } else {
                        showAlert("사용자 유형을 선택하세요.");
                    }
                }
            }
        });
        // 에러 리스너 초기화
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 응답을 처리하는 코드 추가
                showAlert("서버 응답 중 오류가 발생했습니다.");
            }
        };
    }

    private void checkDuplicate(String userID) {
        CheckDuplicateRequest checkDuplicateRequest = new CheckDuplicateRequest(userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("RegisterActivity", "서버 확인");

                            // Check if the response starts with "<br" indicating an error
                            if (response.startsWith("<br")) {
                                // Handle non-JSON response
                                handleNonJsonResponse(response);
                                return; // Stop further processing
                            }

                            // If not an error, try to parse the JSON
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isDuplicate = jsonObject.getBoolean("isDuplicate");

                            if (isDuplicate) {
                                showAlert("이미 사용 중인 아이디입니다.");
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showAlert("서버 응답을 처리하는 중 오류가 발생했습니다.");
                        }
                    }
                },
                errorListener);

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(checkDuplicateRequest);
    }


    private void handleNonJsonResponse(String response) {
        // 응답에서 데이터를 추출하고 필드를 채웁니다.
        String[] data = response.split("\\s+"); // 공백으로 분리
        for (String item : data) {
            // '=>'를 기준으로 각 항목을 분리하여 키-값 쌍을 얻습니다.
            String[] keyValue = item.split("=>");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                // 키에 기반하여 해당 필드를 업데이트합니다.
                switch (key) {
                    case "[userPassword]":
                        et_pass.setText(value);
                        break;
                    case "[userNickname]":
                        et_nickname.setText(value);
                        break;
                    case "[userName]":
                        et_name.setText(value);
                        break;
                    case "[userClassification]":
                        // 값에 따라 사용자 유형을 업데이트합니다.
                        if (value.equals("A")) {
                            radioGroup.check(R.id.radio_button_purchaser);
                        } else if (value.equals("B")) {
                            radioGroup.check(R.id.radio_button_seller);
                        }
                        break;
                    case "[userID]":
                        et_id.setText(value);
                        break;
                }
            }
        }
    }

    private String getUserType() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radio_button_purchaser) {
            return "A"; // "구매자"
        } else if (selectedId == R.id.radio_button_seller) {
            return "B"; // "판매자"
        }

        return ""; // 기본값 또는 선택되지 않은 경우
    }

    private void performRegistration(String userName, String userID, String userPass, String userNickname, String userType) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("RegisterActivity", "서버 응답: " + response); // 디버깅을 위한 이 줄을 추가

                    // Check if the response starts with "<br" indicating an error
                    if (response.startsWith("<br")) {
                        // Handle non-JSON response
                        handleNonJsonResponse(response);
                        return; // Stop further processing
                    }

                    // If not an error, try to parse the JSON
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                        // Finish the current activity before starting the new one
                        finish();

                        // Start the LoginActivity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON 파싱에 실패한 경우
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // RegisterRequest 생성 시 errorListener 전달
        RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, userNickname, userType, responseListener, errorListener);
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
