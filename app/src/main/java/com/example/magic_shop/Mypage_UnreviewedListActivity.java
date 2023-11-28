package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Mypage_UnreviewedListActivity extends AppCompatActivity {

    public List<UnreviewedItem> getUnreviewedList() {
        List<UnreviewedItem> unreviewedList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        unreviewedList.add(new UnreviewedItem("상품 A", "2023-11-25"));
        unreviewedList.add(new UnreviewedItem("상품 B", "2023-11-25"));

        return unreviewedList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_unreviewed_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_reviewed_list = (Button) findViewById(R.id.btn_reviewed_list);
        btn_reviewed_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_ReviewedListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.unreviewed_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<UnreviewedItem> unreviewedList = getUnreviewedList();
        UnreviewedAdapter adapter = new UnreviewedAdapter(unreviewedList, this);
        recyclerView.setAdapter(adapter);
    }

    public class UnreviewedItem {
        String productName;
        String purchaseConfirmationDate;

        public UnreviewedItem(String productName, String purchaseConfirmationDate) {
            this.productName = productName;
            this.purchaseConfirmationDate = purchaseConfirmationDate;
        }
    }

    public class UnreviewedAdapter extends RecyclerView.Adapter<UnreviewedAdapter.UnreviewedViewHolder> {
        private List<UnreviewedItem> unreviewedList;
        private Context context;

        UnreviewedAdapter(List<UnreviewedItem> unreviewedList, Context context) {
            this.unreviewedList = unreviewedList;
            this.context = context;
        }

        @NonNull
        @Override
        public UnreviewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_unreviewed, parent, false);
            return new UnreviewedViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull UnreviewedViewHolder holder, int position) {
            UnreviewedItem unreviewedItem = unreviewedList.get(position);
            holder.bind(unreviewedItem);
        }

        @Override
        public int getItemCount() { return unreviewedList.size(); }

        public class UnreviewedViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final Button reviewWriteButton;
            private final Context context;

            public UnreviewedViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.purchaseConfirmationDate);
                productNameTextView = itemView.findViewById(R.id.productName);
                reviewWriteButton = itemView.findViewById(R.id.btn_review_write);

                reviewWriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            UnreviewedItem unreviewedItem = unreviewedList.get(position);
                            Intent intent = new Intent(context, Mypage_ReviewWriteActivity.class);
                            intent.putExtra("date", unreviewedItem.purchaseConfirmationDate);
                            intent.putExtra("productName", unreviewedItem.productName);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            void bind(UnreviewedItem unreviewedItem) {
                dateTextView.setText(unreviewedItem.purchaseConfirmationDate);
                productNameTextView.setText(unreviewedItem.productName);
            }
        }
    }
}

