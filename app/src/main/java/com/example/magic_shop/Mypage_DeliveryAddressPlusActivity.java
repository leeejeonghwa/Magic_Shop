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

public class Mypage_DeliveryAddressPlusActivity extends AppCompatActivity {

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
        String userID = sessionManager.getUserId();

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
                boolean isDefaultDeliveryAddress = delivery_address_default_check.isChecked();

                if (!deliveryAddressName.isEmpty() &&
                        !recipient.isEmpty() &&
                        !phoneNumber.isEmpty() &&
                        !address.isEmpty() &&
                        !addressDetail.isEmpty() &&
                        !deliveryAddressRequest.isEmpty()) {

                    deliveryAddressPlus(
                            userID,
                            deliveryAddressName,
                            recipient,
                            phoneNumber,
                            address,
                            addressDetail,
                            deliveryAddressRequest,
                            isDefaultDeliveryAddress
                    );

                    Intent intent = new Intent(getApplicationContext(), Mypage_DeliveryAddressManageActivity.class);
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

    @SuppressLint("LongLogTag")
    private void deliveryAddressPlus(String userID, String deliveryAddressName, String recipient, String phoneNumber,
                                     String address, String addressDetail, String detailRequest, boolean isDefaultDeliveryAddress) {
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
                        Toast.makeText(getApplicationContext(), "배송지 정보 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(Mypage_DeliveryAddressPlusActivity.this, Mypage_DeliveryAddressManageActivity.class);
                        startActivity(intent);
                    } else {
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
            String defaultDeliveryAddress = isDefaultDeliveryAddress ? "1" : "0";
            DeliveryAddressPlusRequest deliveryAddressPlusRequest = new DeliveryAddressPlusRequest(userID,
                    deliveryAddressName, recipient, phoneNumber, address, addressDetail, detailRequest,
                    defaultDeliveryAddress, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(Mypage_DeliveryAddressPlusActivity.this);
            queue.add(deliveryAddressPlusRequest);
            }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Mypage_DeliveryAddressPlusActivity", "deliveryAddressPlus 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }


    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Mypage_DeliveryAddressPlusActivity.this);
        builder.setMessage(message)
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }

}


