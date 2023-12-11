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

public class Mypage_AnswerListActivity extends AppCompatActivity {

    public List<AnswerItem> answerList;
    public AnswerAdapter answerAdapter;
    public Context context;


    public List<AnswerItem> getAnswerList(String jsonResponse) throws JSONException {
        List<AnswerItem> answerList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "review" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("answer");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String questionID = jsonObject.getString(("questionID"));
            String brandName = jsonObject.getString(("sellerID"));
            String productID = jsonObject.getString(("productID"));
            String subject = jsonObject.getString(("subject"));
            String content = jsonObject.getString(("content"));
            String answerContent = jsonObject.getString(("answerContent"));

            AnswerItem answerItem = new AnswerItem(questionID, brandName, productID, subject, content, answerContent);

            answerList.add(answerItem);
        }

        return answerList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_answer_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserId();

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_question_list = (Button) findViewById(R.id.btn_question_list);
        btn_question_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_QuestionListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.answer_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_AnswerListActivity", "서버 응답: " + response);

                    List<AnswerItem> answerList = getAnswerList(response);

                    if (answerAdapter == null) {
                        Log.d("Mypage_AnswerListActivity", "Adapter is null. Creating new adapter.");
                        answerAdapter = new AnswerAdapter(answerList, context);
                        recyclerView.setAdapter(answerAdapter);
                    } else {
                        Log.d("Mypage_AnswerListActivity", "Adapter exists. Updating data.");
                        answerAdapter.setAnswerList(answerList);
                        answerAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AnswerGetRequest answerGetRequest = new AnswerGetRequest(Mypage_AnswerListActivity.this, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_AnswerListActivity.this);
        queue.add(answerGetRequest);
    }


    public class AnswerItem {
        String questionID;
        String brandName;
        String productID;
        String subject;
        String content;
        String answerContent;

        public AnswerItem(String questionID, String brandName, String productID, String subject, String content, String answerContent) {
            this.questionID = questionID;
            this.brandName = brandName;
            this.productID = productID;
            this.subject = subject;
            this.content = content;
            this.answerContent = answerContent;

        }
    }


    public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
        private List<AnswerItem> answerList;
        private Context context;

        AnswerAdapter(List<AnswerItem> answerList, Context context) {
            this.answerList = answerList;
            this.context = context;
        }

        public void setAnswerList(List<AnswerItem> answerList) {
            this.answerList = answerList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_answer, parent, false);
            return new AnswerViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
            AnswerItem answerItem = answerList.get(position);
            holder.bind(answerItem);
        }

        @Override
        public int getItemCount() { return answerList.size(); }

        public class AnswerViewHolder extends RecyclerView.ViewHolder {
            private final TextView brandNameTextView;
            private final TextView productIDTextView;
            private final TextView subjectTextView;
            private final TextView contentTextView;
            private final TextView answerContentTextView;
            private final Context context;

            public AnswerViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                brandNameTextView = itemView.findViewById(R.id.sellerID);
                productIDTextView = itemView.findViewById(R.id.productID);
                subjectTextView = itemView.findViewById(R.id.subject);
                contentTextView = itemView.findViewById(R.id.content);
                answerContentTextView = itemView.findViewById(R.id.answerContent);
            }

            void bind(AnswerItem questionItem) {
                brandNameTextView.setText(questionItem.brandName);
                productIDTextView.setText(questionItem.productID);
                subjectTextView.setText(questionItem.subject);
                contentTextView.setText(questionItem.content);
                answerContentTextView.setText(questionItem.answerContent);
            }
        }
    }
}

