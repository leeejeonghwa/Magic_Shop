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

public class Mypage_ReviewedListActivity extends AppCompatActivity {

    public List<ReviewedItem> getReviewedList() {
        List<ReviewedItem> reviewedList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        reviewedList.add(new ReviewedItem("상품 A", "2023-11-25", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("상품 B", "2023-11-25", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("상품 C", "2023-11-25", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("상품 D", "2023-11-25", "옷이 예뻐요"));
        reviewedList.add(new ReviewedItem("상품 E", "2023-11-25", "옷이 예뻐요"));
        // ... 추가적인 데이터

        return reviewedList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_reviewed_list);
        getWindow().setWindowAnimations(0);

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

        List<ReviewedItem> reviewedList = getReviewedList(); // 여러 배송지 정보를 가져오는 메서드
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

