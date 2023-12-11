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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPageReviewWriteActivity extends AppCompatActivity {

    private EditText contentEditText;
    private TextView brandNameTextView, productNameTextView;
    private String orderID, productID;
    private String productName;
    private String productPrice;
    private String brandName;
    private String productMainImage;
    private ImageView productMainImageView;
    private RatingBar productScoreRatingBar;
    private ProductDetailedImageLoader productDetailedImageLoader;
    private Response.ErrorListener errorListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_review_write);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserID();

        brandNameTextView = findViewById(R.id.sellerID);
        productNameTextView = findViewById(R.id.productName);
        contentEditText = findViewById(R.id.editTextContent);
        productScoreRatingBar = findViewById(R.id.productScore);

        Intent intent = getIntent();
        if (intent != null) {
            this.orderID = intent.getStringExtra("orderID");
            this.productID = intent.getStringExtra("productID");
            this.productName = intent.getStringExtra("productName");
            this.productPrice = intent.getStringExtra("productPrice");
            this.brandName = intent.getStringExtra("sellerID");

            // 받아온 상품명을 화면에 표시
            TextView productNameTextView = findViewById(R.id.productName);
            TextView brandNameTextView = findViewById(R.id.brandName);
            TextView productPriceTextView = findViewById(R.id.productPrice);

            productNameTextView.setText(this.productName);
            brandNameTextView.setText(this.brandName);
            productPriceTextView.setText(this.brandName);

            productMainImageView = findViewById(R.id.mainImage);

            productDetailedImageLoader = new ProductDetailedImageLoader(this);

            loadDetailedImages(this.productName);
        }

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageUnreviewedListActivity.class);
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

                    Intent intent = new Intent(getApplicationContext(), MyPageReviewedListActivity.class);
                    startActivity(intent);

                } else {
                    showAlert();
                }
            }
        });
    }

    private void loadDetailedImages(String productName) {
        productDetailedImageLoader.loadDetailedImages(productName, new ProductDetailedImageLoader.DetailedImageResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    // 이미지를 디코딩하고 화면에 표시
                    JSONObject imagesObject = response.getJSONObject(0);
                    setBase64Image(productMainImageView, imagesObject.getString("main_image"));
                    // 추가적인 이미지가 있다면 계속해서 설정해주면 됩니다.
                } catch (JSONException e) {
                    e.printStackTrace();
                    // 에러 처리
                    Toast.makeText(getApplicationContext(), "Failed to parse image data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                // 에러 처리
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBase64Image(ImageView imageView, String base64Image) {
        // Base64로 인코딩된 이미지를 디코딩하여 ImageView에 설정
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
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
                        Intent intent = new Intent(getApplicationContext(), MyPageReviewedListActivity.class);
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
            PlusReviewRequest reviewPlusRequest = new PlusReviewRequest(orderID, brandName, productID, userID,
                    content, productScore, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(MyPageReviewWriteActivity.this);
            queue.add(reviewPlusRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Mypage_ReviewWriteActivity", "plusReviewWrite 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPageReviewWriteActivity.this);
        builder.setMessage("모든 필드를 채워주세요.")
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}

