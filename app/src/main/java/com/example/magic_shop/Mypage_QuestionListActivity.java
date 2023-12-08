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

public class Mypage_QuestionListActivity extends AppCompatActivity {

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

            String ID = jsonObject.getString(("ID"));
            String subject = jsonObject.getString(("subject"));
            String content = jsonObject.getString(("content"));
            String userID = jsonObject.getString(("userID"));

            QuestionItem questionItem = new QuestionItem(ID, subject, content, userID);

            questionList.add(questionItem);
        }

        return questionList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_question_list);
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

        Button btn_answer_list = (Button) findViewById(R.id.btn_answer_list);
        btn_answer_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_AnswerListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_question_plus = (Button) findViewById(R.id.btn_question_plus);
        btn_question_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Mypage_QuestionWriteActivity.class);
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
                    Log.d("Mypage_QuestionListActivity", "서버 응답: " + response);

                    List<QuestionItem> questionList = getQuestionList(response);

                    if (questionAdapter == null) {
                        Log.d("Mypage_QuestionListActivity", "Adapter is null. Creating new adapter.");
                        questionAdapter = new QuestionAdapter(questionList, context);
                        recyclerView.setAdapter(questionAdapter);
                    } else {
                        Log.d("Mypage_QuestionListActivity", "Adapter exists. Updating data.");
                        questionAdapter.setQuestionList(questionList);
                        questionAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        QuestionGetRequest questionGetRequest = new QuestionGetRequest(Mypage_QuestionListActivity.this, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_QuestionListActivity.this);
        queue.add(questionGetRequest);
    }


    public class QuestionItem {
        String ID;
        String subject;
        String content;
        String userID;

        public QuestionItem(String ID, String subject, String content, String userID) {
            this.ID = ID;
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
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_question, parent, false);
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
            private final TextView subjectTextView;
            private final TextView contentTextView;
            private final Context context;

            public QuestionViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                subjectTextView = itemView.findViewById(R.id.subject);
                contentTextView = itemView.findViewById(R.id.content);
                userIDTextView = itemView.findViewById(R.id.userID);
            }

            void bind(QuestionItem questionItem) {
                subjectTextView.setText(questionItem.subject);
                contentTextView.setText(questionItem.content);
                userIDTextView.setText(questionItem.userID);
            }
        }
    }
}

