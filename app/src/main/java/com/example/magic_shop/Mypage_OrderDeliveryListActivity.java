package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mypage_OrderDeliveryListActivity extends AppCompatActivity {

    private List<OrderItem> orderList;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_order_delivery_list);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.order_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList, this);
        recyclerView.setAdapter(adapter);

        // 사용자 아이디 (실제 사용자 아이디로 변경)
        String userID = sessionManager.getUserId();

        // 주문 데이터 가져오기
        getOrderData(userID, this);
    }

    public void getOrderData(String userID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetOrderDataRequest request = new GetOrderDataRequest(userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 처리하는 코드
                        Log.d("OrderDetails", "Volley Response: " + response);

                        try {
                            // JSON 응답 파싱
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                // 주문 데이터가 있는 경우
                                JSONArray ordersArray = jsonObject.getJSONArray("orders");
                                List<OrderItem> orderList = getOrderList(ordersArray);

                                // 어댑터 갱신
                                adapter.setOrderList(orderList);
                                adapter.notifyDataSetChanged();
                            } else {
                                // 주문 데이터가 없는 경우
                                String message = jsonObject.getString("message");
                                Log.e("OrderDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("OrderDetails", "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OrderDetails", "Volley Error: " + error.getMessage());
                    }
                });

        // 요청을 Volley 큐에 추가
        queue.add(request);
    }

    public List<OrderItem> getOrderList(JSONArray jsonArray) {
        List<OrderItem> orderList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject orderObject = jsonArray.getJSONObject(i);

                // 주문 세부 정보 추출
                int orderId = orderObject.getInt("id");
                String paymentDate = orderObject.getString("paymentDate");
                int totalAmount = orderObject.getInt("totalAmount");

                // 제품 객체 추출
                JSONObject productsObject = orderObject.getJSONObject("products");

                // 제품 세부 정보 추출
                String productName = productsObject.getString("product_name");
                int productPrice = productsObject.getInt("product_price");
                String sellerId = productsObject.getString("seller_id");

                // OrderItem 생성 및 목록에 추가
                OrderItem orderItem = new OrderItem(orderId, paymentDate, totalAmount, productName, productPrice, sellerId);
                orderList.add(orderItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public class OrderItem {
        int orderId;
        String paymentDate;
        int totalAmount;
        String productName;
        int productPrice;
        String sellerId;

        public OrderItem(int orderId, String paymentDate, int totalAmount, String productName, int productPrice, String sellerId) {
            this.orderId = orderId;
            this.paymentDate = paymentDate;
            this.totalAmount = totalAmount;
            this.productName = productName;
            this.productPrice = productPrice;
            this.sellerId = sellerId;
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
        public int getItemCount() {
            return orderList.size();
        }

        public void setOrderList(List<OrderItem> orderList) {
            this.orderList = orderList;
        }

        public class OrderViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView, productPriceTextView, productBrandTextView;
            private final Context context;

            public OrderViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.order_date);
                productNameTextView = itemView.findViewById(R.id.order_productName);
                productPriceTextView = itemView.findViewById(R.id.order_productprice);
                productBrandTextView = itemView.findViewById(R.id.order_brandName);
            }

            void bind(OrderItem orderItem) {
                dateTextView.setText(orderItem.paymentDate);
                productNameTextView.setText(orderItem.productName);
                productPriceTextView.setText(String.valueOf(orderItem.productPrice));
                productBrandTextView.setText(orderItem.sellerId);
            }
        }
    }
}
