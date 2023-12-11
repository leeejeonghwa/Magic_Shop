package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Mypage_ReviewWriteActivity extends AppCompatActivity {

    private EditText contentEditText;
    private TextView brandNameTextView, productNameTextView;
    private RatingBar productScoreRatingBar;
    private Response.ErrorListener errorListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_review_write);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserId();

        Intent intent = getIntent();

        brandNameTextView = findViewById(R.id.sellerID);
        productNameTextView = findViewById(R.id.productName);
        contentEditText = findViewById(R.id.editTextContent);
        productScoreRatingBar = findViewById(R.id.productScore);

        String orderID = intent.getStringExtra("orderID");
        String productID = intent.getStringExtra("productID");
        brandNameTextView.setText(intent.getStringExtra("sellerID"));
        productNameTextView.setText(intent.getStringExtra("productName"));

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_UnreviewedListActivity.class);
                startActivity(intent);
            }
        });

        Button btnReviewSubmit = (Button) findViewById(R.id.btn_review_submit);
        btnReviewSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String brandName = brandNameTextView.getText().toString();
                String content = contentEditText.getText().toString();
                float rating = productScoreRatingBar.getRating();
                int convertedRating = (int) rating;
                String productScore = String.valueOf(convertedRating);

                if (!content.isEmpty()) {

                    plusReview(
                            orderID,
                            brandName,
                            productID,
                            userID,
                            content,
                            productScore
                    );

                    Intent intent = new Intent(getApplicationContext(), Mypage_ReviewedListActivity.class);
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
            String score = jsonResponse.getString("score");

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
    private void plusReview(String orderID, String brandName, String productID, String userID, String content, String productScore) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_ReviewWriteActivity", " plusReview() 서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        handleNonJsonResponse(response);
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String successMessage = "리뷰 등록에 성공하였습니다.";
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), Mypage_ReviewedListActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "리뷰 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Mypage_ReviewWriteActivity", "JSON 파싱 오류: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 리뷰 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Mypage_ReviewWriteActivity", "예외 발생: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "알 수 없는 오류로 리뷰 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            ReviewPlusRequest reviewPlusRequest = new ReviewPlusRequest(orderID, brandName, productID, userID,
                    content, productScore, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(Mypage_ReviewWriteActivity.this);
            queue.add(reviewPlusRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Mypage_ReviewWriteActivity", "plusReviewWrite 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Mypage_ReviewWriteActivity.this);
        builder.setMessage("모든 필드를 채워주세요.")
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}

