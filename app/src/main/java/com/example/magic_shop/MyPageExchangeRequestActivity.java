package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPageExchangeRequestActivity extends AppCompatActivity {

    private EditText contentEditText;
    private TextView brandNameTextView, productNameTextView;
    private ImageView productMainImage;
    private Response.ErrorListener errorListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_exchange_request);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserID();

        Intent intent = getIntent();

        brandNameTextView = findViewById(R.id.sellerID);
        productNameTextView = findViewById(R.id.productName);
        productMainImage = findViewById(R.id.productImage);
        contentEditText = findViewById(R.id.editTextContent);

        String orderID = intent.getStringExtra("orderID");
        String productID = intent.getStringExtra("productID");
        brandNameTextView.setText(intent.getStringExtra("sellerID"));
        productNameTextView.setText(intent.getStringExtra("productName"));
        String productImage = intent.getStringExtra("productImage");

        byte[] decodedString = Base64.decode(productImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        productMainImage.setImageBitmap(decodedByte);


        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btnExchangeRequest = (Button) findViewById(R.id.btn_exchange_request);
        btnExchangeRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String brandName = brandNameTextView.getText().toString();
                String content = contentEditText.getText().toString();

                if (!content.isEmpty()) {

                    plusExchange(
                            orderID,
                            brandName,
                            productID,
                            userID,
                            content
                    );

                    Intent intent = new Intent(getApplicationContext(), MyPageExchangeWaitingListActivity.class);
                    startActivity(intent);

                } else {
                    showAlert();
                }
            }
        });
    }

    private void handleNonJsonResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String productID = jsonResponse.getString("productID");
            String userID = jsonResponse.getString("userID");
            String content = jsonResponse.getString("content");

            // 텍스트 뷰에 값을 설정합니다.
//            editTextProductID.setText(productID);
//            editTextRecipient.setText(recipient);
//            editTextPhoneNumber.setText(phoneNumber);
//            editTextAddress.setText(address);
//            editTextDeliveryAddressRequest.setText(deliveryRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 리뷰 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    private void plusExchange(String orderID, String brandName, String productID, String userID, String content) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_ExchangeRequestActivity", " plusExchange() 서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        handleNonJsonResponse(response);
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String successMessage = "교환 신청에 성공하였습니다.";
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MyPageExchangeWaitingListActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "교환 신청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Mypage_ExchangeRequestActivity", "JSON 파싱 오류: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 교환 신청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Mypage_ExchangeRequestActivity", "예외 발생: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "알 수 없는 오류로 교환 신청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            ExchangeRequest exchangeRequest = new ExchangeRequest(orderID, brandName, productID, userID,
                    content, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(MyPageExchangeRequestActivity.this);
            queue.add(exchangeRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Mypage_ExchangeRequestActivity", "plusExchangeRequest 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPageExchangeRequestActivity.this);
        builder.setMessage("모든 필드를 채워주세요.")
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}

