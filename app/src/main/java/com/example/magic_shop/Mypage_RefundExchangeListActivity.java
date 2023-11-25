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

public class Mypage_RefundExchangeListActivity extends AppCompatActivity {

    public List<RefundExchangeItem> getRefundExchangeList() {
        java.util.List<RefundExchangeItem> refundExchangeList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        refundExchangeList.add(new RefundExchangeItem("2023-11-20", "교환 완료", "상품 A", "10000원"));
        refundExchangeList.add(new RefundExchangeItem("2023-11-20", "환불 완료", "상품 B", "20000원"));
        refundExchangeList.add(new RefundExchangeItem("2023-11-20", "환불 완료", "상품 C", "30000원"));
        refundExchangeList.add(new RefundExchangeItem("2023-11-20", "교환 완료", "상품 D", "40000원"));
        refundExchangeList.add(new RefundExchangeItem("2023-11-20", "교환 완료", "상품 E", "500000원"));
        // ... 추가적인 데이터

        return refundExchangeList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_refund_exchange_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.refund_exchange_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<RefundExchangeItem> refundExchangeList = getRefundExchangeList(); // 여러 배송지 정보를 가져오는 메서드
        RefundExchangeAdapter adapter = new RefundExchangeAdapter(refundExchangeList, this);
        recyclerView.setAdapter(adapter);
    }

    public class RefundExchangeItem {
        String date;
        String refundExchangeState;
        String productName;
        String refundExchangePrice;

        public RefundExchangeItem(String date, String refundExchangeState, String productName, String refundExchangePrice) {
            this.date = date;
            this.refundExchangeState = refundExchangeState;
            this.productName = productName;
            this.refundExchangePrice = refundExchangePrice;
        }
    }

    public class RefundExchangeAdapter extends RecyclerView.Adapter<RefundExchangeAdapter.RefundExchangeViewHolder> {
        private List<RefundExchangeItem> refundExchangeList;
        private Context context;

        RefundExchangeAdapter(List<RefundExchangeItem> refundExchangeList, Context context) {
            this.refundExchangeList = refundExchangeList;
            this.context = context;
        }

        @NonNull
        @Override
        public RefundExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_refund_exchange, parent, false);
            return new RefundExchangeViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull RefundExchangeViewHolder holder, int position) {
            RefundExchangeItem refundExchangeItem = refundExchangeList.get(position);
            holder.bind(refundExchangeItem);
        }

        @Override
        public int getItemCount() { return refundExchangeList.size(); }

        public class RefundExchangeViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView refundExchangeStateTextView;
            private final TextView productNameTextView;
            private final TextView refundExchangePriceTextView;
            private final Context context;

            public RefundExchangeViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.order_date);
                refundExchangeStateTextView = itemView.findViewById(R.id.refund_exchange_state);
                productNameTextView = itemView.findViewById(R.id.order_productName);
                refundExchangePriceTextView = itemView.findViewById(R.id.refund_exchange_price);
            }

            void bind(RefundExchangeItem refundExchangeItem) {
                dateTextView.setText(refundExchangeItem.date);
                refundExchangeStateTextView.setText(refundExchangeItem.refundExchangeState);
                productNameTextView.setText(refundExchangeItem.productName);
                refundExchangePriceTextView.setText(refundExchangeItem.refundExchangePrice);
            }
        }
    }
}
