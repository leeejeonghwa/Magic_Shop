package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Seller_QuestionListActivity extends AppCompatActivity {

    public List<QuestionItem> questionList;
    public QuestionAdapter questionAdapter;
    public Context context;


    public List<QuestionItem> getQuestionList(String jsonResponse) throws JSONException {
        List<QuestionItem> questionList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "review" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("question");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String questionID = jsonObject.getString(("questionID"));
            String sellerID = jsonObject.getString(("sellerID"));
            String productID = jsonObject.getString(("productID"));
            String subject = jsonObject.getString(("subject"));
            String content = jsonObject.getString(("content"));
            String userID = jsonObject.getString(("userID"));

            QuestionItem questionItem = new QuestionItem(questionID, sellerID, productID, subject, content, userID);

            questionList.add(questionItem);
        }

        return questionList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_question_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String sellerID = sessionManager.getUserId();

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_MypageMainActivity.class);
                startActivity(intent);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.question_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Seller_QuestionListActivity", "서버 응답: " + response);

                    List<QuestionItem> questionList = getQuestionList(response);

                    if (questionAdapter == null) {
                        Log.d("Seller_QuestionListActivity", "Adapter is null. Creating new adapter.");
                        questionAdapter = new QuestionAdapter(questionList, context);
                        recyclerView.setAdapter(questionAdapter);
                    } else {
                        Log.d("Seller_QuestionListActivity", "Adapter exists. Updating data.");
                        questionAdapter.setQuestionList(questionList);
                        questionAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SellerQuestionGetRequest sellerQuestionGetRequest = new SellerQuestionGetRequest(Seller_QuestionListActivity.this, sellerID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Seller_QuestionListActivity.this);
        queue.add(sellerQuestionGetRequest);
    }


    public class QuestionItem {
        String questionID;
        String sellerID;
        String productID;
        String subject;
        String content;
        String userID;

        public QuestionItem(String questionID, String sellerID, String productID, String subject, String content, String userID) {
            this.questionID = questionID;
            this.sellerID = sellerID;
            this.productID = productID;
            this.subject = subject;
            this.content = content;
            this.userID = userID;

        }
    }


    public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
        private List<QuestionItem> questionList;
        private Context context;

        QuestionAdapter(List<QuestionItem> questionList, Context context) {
            this.questionList = questionList;
            this.context = context;
        }

        public void setQuestionList(List<QuestionItem> questionList) {
            this.questionList = questionList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_question, parent, false);
            return new QuestionViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
            QuestionItem questionItem = questionList.get(position);
            holder.bind(questionItem);
        }

        @Override
        public int getItemCount() { return questionList.size(); }

        public class QuestionViewHolder extends RecyclerView.ViewHolder {
            private final TextView userIDTextView;
            private final TextView productIDTextView;
            private final TextView subjectTextView;
            private final TextView contentTextView;
            public final Button answerWriteButton;
            private final Context context;

            public QuestionViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                userIDTextView = itemView.findViewById(R.id.userID);
                productIDTextView = itemView.findViewById(R.id.productID);
                subjectTextView = itemView.findViewById(R.id.subject);
                contentTextView = itemView.findViewById(R.id.content);
                answerWriteButton = itemView.findViewById(R.id.btn_answer_write);

                // 답변 작성
                answerWriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            QuestionItem questionItem = questionList.get(position);
                            Intent intent = new Intent(context, Seller_AnswerWriteActivity.class);
                            intent.putExtra("questionID", questionItem.questionID);
                            intent.putExtra("productID", questionItem.productID);
                            intent.putExtra("subject", questionItem.subject);
                            intent.putExtra("content", questionItem.content);
                            intent.putExtra("userID", questionItem.userID);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            void bind(QuestionItem questionItem) {
                productIDTextView.setText(questionItem.productID);
                subjectTextView.setText(questionItem.subject);
                contentTextView.setText(questionItem.content);
                userIDTextView.setText(questionItem.userID);
            }
        }
    }
}

