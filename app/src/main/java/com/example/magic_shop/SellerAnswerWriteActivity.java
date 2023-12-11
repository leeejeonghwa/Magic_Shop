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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SellerAnswerWriteActivity extends AppCompatActivity {

    private TextView textViewSubject, textViewContent;
    private EditText editTextAnswerContent;
    private Response.ErrorListener errorListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_answer_write);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserID();
        String writer = sessionManager.getUserNickname();

        editTextAnswerContent = findViewById(R.id.editTextAnswerContent);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerQuestionListActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        textViewSubject = findViewById(R.id.textViewSubject);
        textViewContent = findViewById(R.id.textViewContent);

        String questionID = intent.getStringExtra("questionID");
        textViewSubject.setText(intent.getStringExtra("subject"));
        textViewContent.setText(intent.getStringExtra("content"));


        Button btn_answer_submit = (Button) findViewById(R.id.btn_answer_submit);
        btn_answer_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String answerContent = editTextAnswerContent.getText().toString();

                if (!answerContent.isEmpty()) {

                    plusAnswerContent(
                            questionID,
                            answerContent
                    );

                    Intent intent = new Intent(getApplicationContext(), SellerQuestionListActivity.class);
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
            String answerContent = jsonResponse.getString("answerContent");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 리뷰 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    private void plusAnswerContent(String questionID, String answerContent) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Seller_AnswerWriteActivity", " plusAnswerContent() 서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        handleNonJsonResponse(response);
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String successMessage = "답변 등록에 성공하였습니다.";
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), SellerQuestionListActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "답변 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Seller_AnswerWriteActivity", "JSON 파싱 오류: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 답변 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Seller_AnswerWriteActivity", "예외 발생: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "알 수 없는 오류로 답변 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            AnswerPlusRequest answerPlusRequest = new AnswerPlusRequest(questionID, answerContent, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(SellerAnswerWriteActivity.this);
            queue.add(answerPlusRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Seller_AnswerWriteActivity", "plusAnswerWrite 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SellerAnswerWriteActivity.this);
        builder.setMessage(message)
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}

