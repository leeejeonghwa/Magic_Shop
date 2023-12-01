package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Seller_ReviewListActivity extends AppCompatActivity {

    public List<ReviewedItem> getReviewedList() {
        List<ReviewedItem> reviewedList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        reviewedList.add(new ReviewedItem("2023-11-29", "상품 A", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("2023-11-29", "상품 B", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("2023-11-29", "상품 A", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("2023-11-29", "상품 A", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("2023-11-29", "상품 A", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("2023-11-29", "상품 A", "옷이 예뻐요"));
        // ... 추가적인 데이터

        return reviewedList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_review_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.seller_review_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<ReviewedItem> reviewedList = getReviewedList();
        ReviewedAdapter adapter = new ReviewedAdapter(reviewedList, this);
        recyclerView.setAdapter(adapter);
    }

    public class ReviewedItem {
        String date;
        String productName;
        String comment;

        public ReviewedItem(String date, String productName, String comment) {
            this.date = date;
            this.productName = productName;
            this.comment = comment;
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
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_review, parent, false);
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
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView commentTextView;
            private final Context context;

            public ReviewedViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                commentTextView = itemView.findViewById(R.id.comment);
            }

            void bind(ReviewedItem reviewedItem) {
                dateTextView.setText(reviewedItem.date);
                productNameTextView.setText(reviewedItem.productName);
                commentTextView.setText(reviewedItem.comment);
            }
        }
    }
}

