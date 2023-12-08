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
import android.widget.RatingBar;
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

public class Mypage_ReviewedListActivity extends AppCompatActivity {

    public List<ReviewedItem> reviewedList;
    public ReviewedAdapter reviewedAdapter;
    public Context context;


    public List<ReviewedItem> getReviewedList(String jsonResponse) throws JSONException {
        List<ReviewedItem> reviewedList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "review" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("review");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String reviewID = jsonObject.getString(("reviewID"));
            String sellerID = jsonObject.getString(("sellerID"));
            String productID = jsonObject.getString(("productID"));
            String userID = jsonObject.getString(("userID"));
            String content = jsonObject.getString(("content"));
            String productScore = jsonObject.getString(("productScore"));

            ReviewedItem reviewedItem = new ReviewedItem(reviewID, sellerID, productID, userID,
                    content, productScore);

            reviewedList.add(reviewedItem);
        }

        return reviewedList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_reviewed_list);
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

        Button btn_home = (Button) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button btn_review_list = (Button) findViewById(R.id.btn_unreviewed_list);
        btn_review_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_UnreviewedListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.reviewed_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_ReviewedListActivity", "서버 응답: " + response);

                    List<ReviewedItem> reviewedList = getReviewedList(response);

                    if (reviewedAdapter == null) {
                        Log.d("Mypage_ReviewedListActivity", "Adapter is null. Creating new adapter.");
                        reviewedAdapter = new ReviewedAdapter(reviewedList, context);
                        recyclerView.setAdapter(reviewedAdapter);
                    } else {
                        Log.d("Mypage_ReviewedListActivity", "Adapter exists. Updating data.");
                        reviewedAdapter.setReviewedList(reviewedList);
                        reviewedAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ReviewGetRequest reviewGetRequest = new ReviewGetRequest(Mypage_ReviewedListActivity.this, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_ReviewedListActivity.this);
        queue.add(reviewGetRequest);
    }


    public class ReviewedItem {
        String reviewID;
        String sellerID;
        String productID;
        String userID;
        String content;
        String productScore;

        public ReviewedItem(String reviewID, String sellerID, String productID, String userID,
                            String content, String productScore) {
            this.reviewID = reviewID;
            this.sellerID = sellerID;
            this.productID = productID;
            this.userID = userID;
            this.content = content;
            this.productScore = productScore;
        }
    }


    public class ReviewedAdapter extends RecyclerView.Adapter<ReviewedAdapter.ReviewedViewHolder> {
        private List<ReviewedItem> reviewedList;
        private Context context;

        ReviewedAdapter(List<ReviewedItem> reviewedList, Context context) {
            this.reviewedList = reviewedList;
            this.context = context;
        }

        public void setReviewedList(List<ReviewedItem> reviewedList) {
            this.reviewedList = reviewedList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ReviewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_reviewed, parent, false);
            return new ReviewedViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewedViewHolder holder, int position) {
            ReviewedItem reviewedItem = reviewedList.get(position);
            holder.bind(reviewedItem);
        }

        @Override
        public int getItemCount() { return reviewedList.size(); }

        public class ReviewedViewHolder extends RecyclerView.ViewHolder {
            private final TextView sellerIDTextView;
            private final TextView productIDTextView;
            private final TextView contentTextView;
            private final RatingBar productScoreRatingBar;
            private final Context context;

            public ReviewedViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                sellerIDTextView = itemView.findViewById(R.id.sellerID);
                productIDTextView = itemView.findViewById(R.id.productID);
                contentTextView = itemView.findViewById(R.id.content);
                productScoreRatingBar = itemView.findViewById(R.id.productScore);
            }

            void bind(ReviewedItem reviewedItem) {
                sellerIDTextView.setText(reviewedItem.sellerID);
                productIDTextView.setText(reviewedItem.productID);
                contentTextView.setText(reviewedItem.content);

                // productScore 값을 float로 변환하여 RatingBar에 설정
                float rating = Float.parseFloat(reviewedItem.productScore);
                productScoreRatingBar.setRating(rating);
            }
        }
    }
}

