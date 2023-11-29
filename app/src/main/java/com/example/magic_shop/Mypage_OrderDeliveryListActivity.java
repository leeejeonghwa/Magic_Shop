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

public class Mypage_OrderDeliveryListActivity extends AppCompatActivity {

    public List<OrderItem> getOrderList() {
        List<OrderItem> orderList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        orderList.add(new OrderItem("2023-11-20", "상품 A", "000000000000"));
        orderList.add(new OrderItem("2023-11-21", "상품 B", "111111111111"));
        orderList.add(new OrderItem("2023-11-22", "상품 C", "222222222222"));
        orderList.add(new OrderItem("2023-11-23", "상품 D", "222222222222"));
        orderList.add(new OrderItem("2023-11-24", "상품 E", "222222233333"));
        // ... 추가적인 데이터

        return orderList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_order_delivery_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.order_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<OrderItem> orderList = getOrderList(); // 여러 배송지 정보를 가져오는 메서드
        OrderAdapter adapter = new OrderAdapter(orderList, this);
        recyclerView.setAdapter(adapter);
    }

    public class OrderItem {
        String date;
        String productName;
        String trackingNumber;

        public OrderItem(String date, String productName, String trackingNumber) {
            this.date = date;
            this.productName = productName;
            this.trackingNumber = trackingNumber;
        }
    }

    public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private List<OrderItem> orderList;
        private Context context;

        OrderAdapter(List<OrderItem> orderList, Context context) {
            this.orderList = orderList;
            this.context = context;
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_order, parent, false);
            return new OrderViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            OrderItem orderItem = orderList.get(position);
            holder.bind(orderItem);
        }

        @Override
        public int getItemCount() { return orderList.size(); }

        public class OrderViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView trackingNumberTextView;
            public final Button deliveryCheckButton;
            public final Button reviewWriteButton;
            private final Context context;

            public OrderViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.order_date);
                productNameTextView = itemView.findViewById(R.id.order_productName);
                trackingNumberTextView = itemView.findViewById(R.id.order_trackingNumber);
                deliveryCheckButton = itemView.findViewById(R.id.btn_deliveryCheck);
                reviewWriteButton = itemView.findViewById(R.id.btn_review_write);

                deliveryCheckButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            OrderItem orderItem = orderList.get(position);
                            Intent intent = new Intent(context, Mypage_DeliveryCheckActivity.class);
                            intent.putExtra("date", orderItem.date);
                            intent.putExtra("productName", orderItem.productName);
                            intent.putExtra("trackingNumber", orderItem.trackingNumber);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            void bind(OrderItem orderItem) {
                dateTextView.setText(orderItem.date);
                productNameTextView.setText(orderItem.productName);
                trackingNumberTextView.setText(orderItem.trackingNumber);
            }
        }
    }
}