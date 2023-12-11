package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPageDeliveryAddressPlusActivity extends AppCompatActivity {

    private EditText editTextAddressName, editTextRecipient, editTextPhoneNumber, editTextAddress,
            editTextAddressDetail, editTextDeliveryAddressRequest;
    private CheckBox delivery_address_default_check;
    private Response.ErrorListener errorListener;

    @SuppressLint("CutPasteId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_delivery_address_plus);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserID();

        editTextAddressName = findViewById(R.id.editTextAddressName);
        editTextRecipient = findViewById(R.id.editTextRecipient);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextAddressDetail = findViewById(R.id.editTextAddressDetail);
        editTextDeliveryAddressRequest = findViewById(R.id.editTextDeliveryAddressRequest);
        delivery_address_default_check = findViewById(R.id.delivery_address_default_check);

        Button btn_back = (Button) findViewById(R.id.btn_back);
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

        Button btn_delivery_address_plus_check = (Button) findViewById(R.id.btn_delivery_address_plus_check);
        btn_delivery_address_plus_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String deliveryAddressName = editTextAddressName.getText().toString();
                String recipient = editTextRecipient.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String address = editTextAddress.getText().toString();
                String addressDetail = editTextAddressDetail.getText().toString();
                String deliveryAddressRequest = editTextDeliveryAddressRequest.getText().toString();

                if (!deliveryAddressName.isEmpty() &&
                        !recipient.isEmpty() &&
                        !phoneNumber.isEmpty() &&
                        !address.isEmpty() &&
                        !addressDetail.isEmpty() &&
                        !deliveryAddressRequest.isEmpty()) {

                    boolean isDefaultDeliveryAddress = delivery_address_default_check.isChecked();
                    String defaultDeliveryAddress = isDefaultDeliveryAddress ? "1" : "0";

                    boolean isDefault = defaultDeliveryAddress.equals("1");

                    // 기존 기본 배송지 0으로 업데이트하고 추가
                    if (isDefault) {
                        resetDefaultDeliveryAddressesAndAddNew(
                                userID,
                                deliveryAddressName,
                                recipient,
                                phoneNumber,
                                address,
                                addressDetail,
                                deliveryAddressRequest,
                                defaultDeliveryAddress
                        );
                    }

                    else {
                        plusDeliveryAddress(
                                userID,
                                deliveryAddressName,
                                recipient,
                                phoneNumber,
                                address,
                                addressDetail,
                                deliveryAddressRequest,
                                defaultDeliveryAddress
                        );
                    }

                    Intent intent = new Intent(getApplicationContext(), MyPageDeliveryAddressManageActivity.class);
                    startActivity(intent);

                } else {
                    showAlert("모든 필드를 채워주세요.");
                }
            }
        });
    }

    private void handleNonJsonResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String userID = jsonResponse.getString("userID");
            String deliveryAddressName = jsonResponse.getString("deliveryAddressName");
            String recipient = jsonResponse.getString("recipient");
            String phoneNumber = jsonResponse.getString("phoneNumber");
            String address = jsonResponse.getString("address");
            String deliveryRequest = jsonResponse.getString("deliveryRequest");
            String defaultDeliveryAddress = jsonResponse.getString("defaultDeliveryAddress");

            // 텍스트 뷰에 값을 설정합니다.
            editTextAddressName.setText(deliveryAddressName);
            editTextRecipient.setText(recipient);
            editTextPhoneNumber.setText(phoneNumber);
            editTextAddress.setText(address);
            editTextDeliveryAddressRequest.setText(deliveryRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetDefaultDeliveryAddressesAndAddNew(
            String userID, String deliveryAddressName, String recipient, String phoneNumber,
            String address, String addressDetail, String detailRequest, String defaultDeliveryAddress) {

        // 기존 기본 배송지를 모두 0으로 설정
        resetDefaultDeliveryAddresses(userID, new OnResetComplete() {
            @Override
            public void onResetComplete() {
                // 모두 0으로 설정된 후에 새로운 배송지를 추가
                plusDeliveryAddress(
                        userID,
                        deliveryAddressName,
                        recipient,
                        phoneNumber,
                        address,
                        addressDetail,
                        detailRequest,
                        defaultDeliveryAddress
                );
            }
        });
    }

    private void resetDefaultDeliveryAddresses(String userID, OnResetComplete onResetComplete) {
        // 기본 배송지를 모두 0으로 설정하는 서버 요청 코드
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_DeliveryAddressPlusActivity", " resetDefaultDeliveryAddresses() 서버 응답: " + response);

                    // 서버 응답 처리
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Log.d("Mypage_DeliveryAddressPlusActivity", " 기본 배송지를 모두 0으로 설정 성공");
                        // 콜백을 통해 완료를 알림
                        onResetComplete.onResetComplete();
                    } else {
                        Log.e("Mypage_DeliveryAddressPlusActivity", " 기본 배송지를 모두 0으로 설정 실패");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Mypage_DeliveryAddressPlusActivity", " JSON 파싱 오류: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Mypage_DeliveryAddressPlusActivity", " 예외 발생: " + e.getMessage());
                }
            }
        };

        // 서버 요청 클래스
        ResetDefaultDeliveryAddressRequest resetRequest = new ResetDefaultDeliveryAddressRequest(userID, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(MyPageDeliveryAddressPlusActivity.this);
        queue.add(resetRequest);
    }


    @SuppressLint("LongLogTag")
    private void plusDeliveryAddress(String userID, String deliveryAddressName, String recipient, String phoneNumber,
                                     String address, String addressDetail, String detailRequest, String defaultDeliveryAddress) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_DeliveryAddressPlusActivity", " deliveryAddressPlus() 서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        handleNonJsonResponse(response);
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String successMessage = "배송지 정보 등록에 성공하였습니다.";
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MyPageDeliveryAddressManageActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "배송지 정보 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Mypage_DeliveryAddressPlusActivity", "JSON 파싱 오류: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 배송지 정보 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Mypage_DeliveryAddressPlusActivity", "예외 발생: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "알 수 없는 오류로 배송지 정보 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            PlusDeliveryAddressRequest deliveryAddressPlusRequest = new PlusDeliveryAddressRequest(userID,
                    deliveryAddressName, recipient, phoneNumber, address, addressDetail, detailRequest,
                    defaultDeliveryAddress, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(MyPageDeliveryAddressPlusActivity.this);
            queue.add(deliveryAddressPlusRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Mypage_DeliveryAddressPlusActivity", "deliveryAddressPlus 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPageDeliveryAddressPlusActivity.this);
        builder.setMessage(message)
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}
interface OnResetComplete {
    void onResetComplete();
}