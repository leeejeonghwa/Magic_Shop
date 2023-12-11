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

public class Mypage_RefundAccountManageActivity extends AppCompatActivity {
    private EditText nameEditText, accountnumberEditText, bankEditText;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_account_manage);
        getWindow().setWindowAnimations(0);

        nameEditText = findViewById(R.id.editTextName);
        accountnumberEditText = findViewById(R.id.editTextAccountNumber);
        bankEditText = findViewById(R.id.editTextBank);

        sessionManager = new SessionManager(getApplicationContext());

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btnRefundAccountChange = findViewById(R.id.btn_refund_account_change_confirm);
        btnRefundAccountChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String accountNumber = accountnumberEditText.getText().toString();
                String bankName = bankEditText.getText().toString();
                String userID = sessionManager.getUserId();

                if (name.isEmpty() || accountNumber.isEmpty() || bankName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 계좌 확인 및 등록/업데이트 로직 호출
                    performBankAccountRegistration(userID, name, bankName, accountNumber);
                }
            }
        });
    }

    private void performBankAccountRegistration(String userID, String username, String bankName, String accountNumber) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("refund", "서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean accountExists = jsonResponse.getBoolean("accountExists");

                    if (accountExists) {
                        // 이미 등록된 계좌가 있으면 업데이트 수행
                        updateBankAccount(userID, username, bankName, accountNumber);
                    } else {
                        // 등록된 계좌가 없으면 새로 등록
                        registerBankAccount(userID, username, bankName, accountNumber);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 계좌 확인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // CheckAccountRequest 클래스를 이용해 서버 요청 처리
        CheckAccountRequest checkAccountRequest = new CheckAccountRequest(userID, responseListener, null, getApplicationContext());
        RequestQueue queue = Volley.newRequestQueue(Mypage_RefundAccountManageActivity.this);
        queue.add(checkAccountRequest);
    }

    private void registerBankAccount(String userID, String username, String bankName, String accountNumber) {
        // 등록 요청 처리
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("refund", "서버 응답: " + response);

                    // 응답이 "<br"로 시작되는지 확인하여 오류 여부 판단
                    if (response.startsWith("<br")) {
                        // JSON이 아닌 응답 처리
                        return; // 추가 처리 중지
                    }

                    // 오류가 아니라면 JSON 파싱 시도
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "환불계좌 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                        // 현재 화면을 종료하고 새로운 화면 시작
                        finish();

                        // 적절한 화면 시작 (예: Mypage_SettingActivity)
                        Intent intent = new Intent(Mypage_RefundAccountManageActivity.this, Mypage_SettingActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "환불계좌 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON 파싱 실패 시
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 환불계좌 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // BankAccountRegisterRequest 클래스를 이용해 서버 요청 처리
        RefundAccountRegistrationRequest refundAccountRegistrationRequest = new RefundAccountRegistrationRequest(userID, username, bankName, accountNumber, responseListener, null, getApplicationContext());
        RequestQueue queue = Volley.newRequestQueue(Mypage_RefundAccountManageActivity.this);
        queue.add(refundAccountRegistrationRequest);
    }

    private void updateBankAccount(String userID, String username, String bankName, String accountNumber) {
        // 업데이트 요청 처리
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("refund", "서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "환불계좌 업데이트에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                        // 현재 화면을 종료하고 새로운 화면 시작
                        finish();

                        // 적절한 화면 시작 (예: Mypage_SettingActivity)
                        Intent intent = new Intent(Mypage_RefundAccountManageActivity.this, Mypage_SettingActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "환불계좌 업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 환불계좌 업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // UpdateAccountRequest 클래스를 이용해 서버 요청 처리
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest(userID, username, bankName, accountNumber, responseListener, null, getApplicationContext());
        RequestQueue queue = Volley.newRequestQueue(Mypage_RefundAccountManageActivity.this);
        queue.add(updateAccountRequest);
    }
}
