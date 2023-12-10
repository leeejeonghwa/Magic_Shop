package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Detailpage_ProductInquiryPageActivity extends AppCompatActivity {

    private Button btnBack, btnEnroll, btnCancellation, btnBag, btnHome, btnSearch;
    private EditText editTextSubject, editTextContent;
    private RadioGroup radioGroup;
    private Response.ErrorListener errorListener;
    private String productName;
    private String productPrice;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_inquiry_page);

        btnBack = findViewById(R.id.back_btn);
        btnEnroll = findViewById(R.id.btn_enroll);
        btnCancellation = findViewById(R.id.btn_cancellation);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);

        btnBack.setVisibility(View.VISIBLE);
        btnEnroll.setVisibility(View.VISIBLE);
        btnCancellation.setVisibility(View.VISIBLE);
        btnBag.setVisibility(View.VISIBLE);
        btnHome.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);

        editTextSubject = findViewById(R.id.editTextSubject);
        editTextContent = findViewById(R.id.editTextContent);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserId();




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainAskActivity.class);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("product_name", productName);
                resultIntent.putExtra("seller_id", sellerId);
                resultIntent.putExtra("product_price", productPrice);

                // 결과를 설정하고 현재 활동을 종료합니다.
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btnCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainAskActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
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
                // TODO sellerID, productID intent로 가져온 걸 참조해야 함
                String sellerID = "dlwjdghk";
                String productID = "1";

                String content = editTextContent.getText().toString();
                String subject = editTextSubject.getText().toString();

                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton checkedRadioButton = findViewById(checkedId);

                if (checkedRadioButton != null) {
                    String type = checkedRadioButton.getText().toString();

                    if (!type.isEmpty() && !subject.isEmpty() && !content.isEmpty()) {
                        plusQuestion(sellerID, productID, userID, type, subject, content);

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

    private void handleNonJsonResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String sellerID = jsonResponse.getString("sellerID");
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
    private void plusQuestion(String sellerID, String productID, String userID, String type, String subject, String content) {
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
                        Intent intent = new Intent(getApplicationContext(), Detailpage_MainAskActivity.class);
                        startActivity(intent);
                    }
                    else {
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
            QuestionPlusRequest QuestionPlusRequest = new QuestionPlusRequest(sellerID, productID, userID, type, subject,
                    content, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(Detailpage_ProductInquiryPageActivity.this);
            queue.add(QuestionPlusRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Detailpage_ProductInquiryPageActivity", "plusQuestionWrite 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Detailpage_ProductInquiryPageActivity.this);
        builder.setMessage(message)
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }

}
