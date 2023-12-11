package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MyPageOrderListActivity extends AppCompatActivity {

    private List<OrderItem> orderList;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_order_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageMainActivity.class);
                startActivity(intent);
            }
        });

        Button btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
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
        String userID = sessionManager.getUserID();

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
                int orderID = orderObject.getInt("id");
                String paymentDate = orderObject.getString("paymentDate");
                int totalAmount = orderObject.getInt("totalAmount");

                // 현재 상태 추출
                String exchangeStatus = orderObject.getString("exchangeStatus");
                String refundStatus = orderObject.getString("refundStatus");

                // 제품 객체 추출
                JSONObject productsObject = orderObject.getJSONObject("products");
                //제품 상세 정보 추출
                String productImage = productsObject.getString("main_image");
                String productID = productsObject.getString("productID");
                String productName = productsObject.getString("product_name");
                int productPrice = productsObject.getInt("product_price");
                String sellerID = productsObject.getString("seller_id");


                // OrderItem 생성 및 목록에 추가
                OrderItem orderItem = new OrderItem(exchangeStatus, refundStatus,orderID, paymentDate,
                        totalAmount, productID, productName, productPrice, sellerID ,productImage);
                orderList.add(orderItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public class OrderItem {
        String exchangeStatus;
        String refundStatus;
        int orderID;
        String paymentDate;
        int totalAmount;
        String productID;
        String productName;
        int productPrice;
        String brandName;
        String productImage;

        public OrderItem(String exchangeStatus, String refundStatus, int orderID, String paymentDate, int totalAmount, String productID, String productName, int productPrice, String brandName, String productImage) {
            this.exchangeStatus = exchangeStatus;
            this.refundStatus = refundStatus;
            this.orderID = orderID;
            this.productID = productID;
            this.productImage = productImage;
            this.paymentDate = paymentDate;
            this.totalAmount = totalAmount;
            this.productName = productName;
            this.productPrice = productPrice;
            this.brandName = brandName;
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
            private final TextView orderStatusTextView;
            private final TextView dateTextView;
            private final TextView productNameTextView, productPriceTextView, productBrandTextView;
            private final ImageView productImageView;
            private final Button exchangeButton;
            private final Button refundButton;
            private final Context context;

            public OrderViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                orderStatusTextView = itemView.findViewById(R.id.order_status);
                dateTextView = itemView.findViewById(R.id.order_date);
                productNameTextView = itemView.findViewById(R.id.order_productName);
                productPriceTextView = itemView.findViewById(R.id.order_productPrice);
                productBrandTextView = itemView.findViewById(R.id.order_brandName);
                productImageView= itemView.findViewById(R.id.productImage);
                exchangeButton = itemView.findViewById(R.id.btn_exchange);
                refundButton = itemView.findViewById(R.id.btn_refund);

                exchangeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            OrderItem orderItem = orderList.get(position);
                            Intent intent = new Intent(context, MyPageExchangeRequestActivity.class);
                            intent.putExtra("orderID", String.valueOf(orderItem.orderID));
                            intent.putExtra("sellerID", orderItem.brandName);
                            intent.putExtra("productID", orderItem.productID);
                            intent.putExtra("productName", orderItem.productName);
                            intent.putExtra("productImage", orderItem.productImage);
                            context.startActivity(intent);
                        }
                    }
                });

                refundButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            OrderItem orderItem = orderList.get(position);
                            Intent intent = new Intent(context, MyPageRefundRequestActivity.class);
                            intent.putExtra("orderID", String.valueOf(orderItem.orderID));
                            intent.putExtra("sellerID", orderItem.brandName);
                            intent.putExtra("productID", orderItem.productID);
                            intent.putExtra("productName", orderItem.productName);
                            intent.putExtra("productImage", orderItem.productImage);
                            context.startActivity(intent);
                        }
                    }
                });

            }

            void bind(OrderItem orderItem) {
                dateTextView.setText(orderItem.paymentDate);
                productNameTextView.setText(orderItem.productName);
                productPriceTextView.setText((orderItem.productPrice)+"원");
                productBrandTextView.setText(orderItem.brandName);

                byte[] decodedString = Base64.decode(orderItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);

                // 교환 및 환불 상태에 따라 orderStatusTextView 설정
                if (orderItem.exchangeStatus.equals("N") && orderItem.refundStatus.equals("N")) {
                    orderStatusTextView.setVisibility(View.GONE); // 교환 및 환불 상태가 모두 N이면 TextView를 숨김
                    exchangeButton.setVisibility(View.VISIBLE); // 교환 버튼을 보이게 설정
                    refundButton.setVisibility(View.VISIBLE); // 환불 버튼을 보이게 설정
                } else {
                    orderStatusTextView.setVisibility(View.VISIBLE); // TextView를 다시 보이게 설정
                    exchangeButton.setVisibility(View.GONE); // 교환 버튼을 숨김
                    refundButton.setVisibility(View.GONE); // 환불 버튼을 숨김

                    if (orderItem.exchangeStatus.equals("W")) {
                        orderStatusTextView.setText("교환 승인 대기");
                    } else if (orderItem.exchangeStatus.equals("Y")) {
                        orderStatusTextView.setText("교환 승인");
                    } else if (orderItem.refundStatus.equals("W")) {
                        orderStatusTextView.setText("환불 승인 대기");
                    } else if (orderItem.refundStatus.equals("Y")) {
                        orderStatusTextView.setText("환불 승인");
                    }
                }
            }
        }
    }
}
