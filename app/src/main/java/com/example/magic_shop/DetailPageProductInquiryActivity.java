package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailPageProductInquiryActivity extends AppCompatActivity {

    private EditText subjectEditText, contentEditText;
    private RadioGroup radioGroup;
    private Response.ErrorListener errorListener;
    private String productID;
    private String productName;
    private String productPrice;
    private String brandName;
    private String productMainImage;
    private ImageView productMainImageView;
    private ProductDetailedImageLoader productDetailedImageLoader;
    private TextView productNameTextView, brandNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_inquiry_page);

        Button btnBack = findViewById(R.id.back_btn);
        Button btnEnroll = findViewById(R.id.btn_enroll);
        Button btnCancellation = findViewById(R.id.btn_cancellation);
        Button btnHome = findViewById(R.id.home_btn);
        Button btnBag = findViewById(R.id.bag_btn);
        Button btnSearch = findViewById(R.id.search_btn);

        btnBack.setVisibility(View.VISIBLE);
        btnEnroll.setVisibility(View.VISIBLE);
        btnCancellation.setVisibility(View.VISIBLE);
        btnBag.setVisibility(View.VISIBLE);
        btnHome.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);

        productNameTextView = findViewById(R.id.productName);
        brandNameTextView = findViewById(R.id.brandName);
        subjectEditText = findViewById(R.id.editTextSubject);
        contentEditText = findViewById(R.id.editTextContent);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserID();

        Intent intent = getIntent();
        if (intent != null) {
            this.productName = intent.getStringExtra("product_name");
            this.brandName = intent.getStringExtra("seller_id");
            this.productID = intent.getStringExtra("id");

            // 받아온 상품명을 화면에 표시
            TextView productNameTextView = findViewById(R.id.productName);
            TextView brandNameTextView = findViewById(R.id.brandName);

            productNameTextView.setText(this.productName);
            brandNameTextView.setText(this.brandName);

            productMainImageView = findViewById(R.id.mainImage);

            productDetailedImageLoader = new ProductDetailedImageLoader(this);

            loadDetailedImages(this.productName);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(getApplicationContext(), DetailPageMainAskActivity.class);
                resultIntent.putExtra("product_name", productName);
                resultIntent.putExtra("seller_id", brandName);

                // 결과를 설정하고 현재 활동을 종료합니다.
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailPageMainAskActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });


        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    View childView = group.getChildAt(i);

                    if (childView instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) childView;

                        // 선택된 RadioButton 이외의 나머지 버튼을 해제합니다.
                        if (radioButton.getId() != checkedId) {
                            radioButton.setChecked(false);
                        }
                    }
                }
            }
        });


        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String brandName = brandNameTextView.getText().toString();
                String content = contentEditText.getText().toString();
                String subject = subjectEditText.getText().toString();

                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton checkedRadioButton = findViewById(checkedId);

                if (checkedRadioButton != null) {
                    String type = checkedRadioButton.getText().toString();

                    if (!type.isEmpty() && !subject.isEmpty() && !content.isEmpty()) {
                        plusQuestion(brandName, productID, userID, type, subject, content);

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        showAlert("모든 필드를 채워주세요.");
                    }
                } else {
                    showAlert("라디오 버튼을 선택해주세요.");
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

    private void handleNonJsonResponse (String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String brandName = jsonResponse.getString("sellerID");
            String productID = jsonResponse.getString("productID");
            String userID = jsonResponse.getString("userID");
            String type = jsonResponse.getString("type");
            String subject = jsonResponse.getString("subject");
            String content = jsonResponse.getString("content");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 문의 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    private void plusQuestion(String brandName, String productID, String userID, String type, String subject, String content) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Detailpage_ProductInquiryPageActivity", " plusQuestion() 서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        handleNonJsonResponse(response);
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String successMessage = "문의 등록에 성공하였습니다.";
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), DetailPageMainAskActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "문의 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Detailpage_ProductInquiryPageActivity", "JSON 파싱 오류: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 문의 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Detailpage_ProductInquiryPageActivity", "예외 발생: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "알 수 없는 오류로 문의 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            PlusQuestionRequest QuestionPlusRequest = new PlusQuestionRequest(brandName, productID, userID, type, subject,
                    content, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(DetailPageProductInquiryActivity.this);
            queue.add(QuestionPlusRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Detailpage_ProductInquiryPageActivity", "plusQuestionWrite 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }

    private void showAlert (String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPageProductInquiryActivity.this);
        builder.setMessage(message)
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}
