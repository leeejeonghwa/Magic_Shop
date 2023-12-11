package com.example.magic_shop;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Seller_ReviewListActivity extends AppCompatActivity {

    private List<ReviewedItem> reviewedList;
    private ReviewedAdapter reviewedAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_review_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_MypageMainActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.seller_review_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reviewedList = new ArrayList<>();
        reviewedAdapter = new ReviewedAdapter(reviewedList, this);
        recyclerView.setAdapter(reviewedAdapter);

        // 사용자 아이디 (실제 사용자 아이디로 변경)
        String sellerID = sessionManager.getUserId();

        // 리뷰 데이터 가져오기
        getReviewedData(sellerID, this);
    }

    public void getReviewedData(String sellerID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetSellerAllReviewRequest request = new GetSellerAllReviewRequest(sellerID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 처리하는 코드
                        Log.d("ReviewedDetails", "Volley Response: " + response);

                        try {
                            // JSON 응답 파싱
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                // 리뷰 데이터가 있는 경우
                                JSONArray reviewsArray = jsonObject.getJSONArray("reviews");
                                List<ReviewedItem> reviewedList = getReviewedList(reviewsArray);

                                // 어댑터 갱신
                                reviewedAdapter.setReviewedList(reviewedList);
                                reviewedAdapter.notifyDataSetChanged();
                            } else {
                                // 리뷰 데이터가 없는 경우
                                String message = jsonObject.getString("message");
                                Log.e("ReviewedDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ReviewedDetails", "JSON Parsing Error: " + e.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ReviewedDetails", "Volley Error: " + error.getMessage());
                    }
                });

        // 요청을 Volley 큐에 추가
        queue.add(request);
    }

    public List<ReviewedItem> getReviewedList(JSONArray jsonArray) {
        List<ReviewedItem> reviewedList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject reviewedObject = jsonArray.getJSONObject(i);

                String createdTime = reviewedObject.getString("createdTime");
                String userID = reviewedObject.getString("userID");
                String productName = reviewedObject.getString("product_name");
                String productImage = reviewedObject.getString("main_image");
                String productScore = reviewedObject.getString("productScore");
                String content = reviewedObject.getString("content");

                ReviewedItem reviewedItem = new ReviewedItem(createdTime, userID, productName, productImage, productScore, content);
                reviewedList.add(reviewedItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewedList;
    }


    public class ReviewedItem {
        String createdTime;
        String userID;
        String productName;
        String productImage;
        String productScore;
        String content;

        public ReviewedItem(String createdTime, String userID, String productName, String productImage,
                            String productScore, String content) {
            this.createdTime = createdTime;
            this.userID = userID;
            this.productName = productName;
            this.productImage = productImage;
            this.productScore = productScore;
            this.content = content;
        }
    }

    public class ReviewedAdapter extends RecyclerView.Adapter<ReviewedAdapter.ReviewedViewHolder> {
        private List<ReviewedItem> reviewedList;
        private Context context;

        ReviewedAdapter(List<ReviewedItem> reviewedList, Context context) {
            this.reviewedList = reviewedList;
            this.context = context;
        }

        @NonNull
        @Override
        public ReviewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_review, parent, false);
            return new ReviewedViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewedViewHolder holder, int position) {
            ReviewedItem reviewedItem = reviewedList.get(position);
            holder.bind(reviewedItem);
        }

        @Override
        public int getItemCount() {
            return reviewedList.size();
        }

        public void setReviewedList(List<ReviewedItem> reviewedList) {
            this.reviewedList = reviewedList;
        }

        public class ReviewedViewHolder extends RecyclerView.ViewHolder {
            private final TextView createdTimeTextView;
            private final TextView userIDTextView;
            private final TextView productNameTextView;
            private final ImageView productImageView;
            private final TextView contentTextView;
            private final RatingBar productScoreRatingBar;
            private final Context context;

            public ReviewedViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                createdTimeTextView = itemView.findViewById(R.id.createdTime);
                userIDTextView = itemView.findViewById(R.id.userID);
                productNameTextView = itemView.findViewById(R.id.productName);
                productImageView = itemView.findViewById(R.id.productImage);
                contentTextView = itemView.findViewById(R.id.content);
                productScoreRatingBar = itemView.findViewById(R.id.productScore);

            }

            void bind(ReviewedItem reviewedItem) {
                createdTimeTextView.setText(reviewedItem.createdTime);
                userIDTextView.setText(reviewedItem.userID);
                productNameTextView.setText(reviewedItem.productName);
                productScoreRatingBar.setRating(Integer.valueOf(reviewedItem.productScore));
                contentTextView.setText(reviewedItem.content);

                byte[] decodedString = Base64.decode(reviewedItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);
            }
        }
    }
}

