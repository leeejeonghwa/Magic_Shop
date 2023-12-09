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

public class Seller_OrderDeliveryListActivity extends AppCompatActivity {

    private List<SellerOrderItem> sellerOrderList;
    private SellerOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_order_delivery_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.seller_order_delivery_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        sellerOrderList = new ArrayList<>();
        adapter = new SellerOrderAdapter(sellerOrderList, this);
        recyclerView.setAdapter(adapter);

        // 사용자 아이디 (실제 사용자 아이디로 변경)
        String userID = sessionManager.getUserId();

        // 주문 데이터 가져오기
        getSellerOrderData(userID, this);
    }

    public void getSellerOrderData(String userID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetSellerOrderDataRequest request = new GetSellerOrderDataRequest(userID,
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
                                JSONArray ordersArray = jsonObject.getJSONArray("sellerorders");
                                List<SellerOrderItem> sellerOrderList = getSellerOrderList(ordersArray);

                                // 어댑터 갱신
                                adapter.setSellerOrderList(sellerOrderList);
                                adapter.notifyDataSetChanged();
                            } else {
                                // 주문 데이터가 없는 경우
                                String message = jsonObject.getString("message");
                                Log.e("SellerOrderDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("SellerOrderDetails", "JSON Parsing Error: " + e.getMessage());
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

    public List<SellerOrderItem> getSellerOrderList(JSONArray jsonArray) {
        List<SellerOrderItem> sellerOrderList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject sellerOrderObject = jsonArray.getJSONObject(i);

                // 주문 세부 정보 추출
                int orderID = sellerOrderObject.getInt("orderID");
                String userID= sellerOrderObject.getString("customerID");
                String userName = sellerOrderObject.getString("customerName");
                String paymentDate = sellerOrderObject.getString("paymentDate");
                String product_name = sellerOrderObject.getString("product_name");
                int productPrice = sellerOrderObject.getInt("product_price");



                SellerOrderItem sellerOrderItem = new SellerOrderItem(orderID,userID,userName,paymentDate,product_name,productPrice);
                sellerOrderList.add(sellerOrderItem);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sellerOrderList;
    }

    public class SellerOrderItem {
        String userId;
        int productPrice;
        int orderId;
        String paymentDate;

        String userName;


        String productName;






        public SellerOrderItem(int orderId, String userId, String userName,String paymentDate, String productName ,int productPrice) {
            this.orderId = orderId;
            this.userId = userId;
            this.userName = userName;
            this.paymentDate = paymentDate;
            this.productName = productName;
            this.productPrice = productPrice;

        }
    }

    public class SellerOrderAdapter extends RecyclerView.Adapter<SellerOrderAdapter.SellerOrderViewHolder> {
        private List<SellerOrderItem> sellerOrderList;
        private Context context;

        SellerOrderAdapter(List<SellerOrderItem> sellerOrderList, Context context) {
            this.sellerOrderList = sellerOrderList;
            this.context = context;
        }

        @NonNull
        @Override
        public SellerOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.seller_order_item, parent, false);
            return new SellerOrderViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull SellerOrderViewHolder holder, int position) {
            SellerOrderItem  sellerOrderItem = sellerOrderList.get(position);
            holder.bind(sellerOrderItem);
        }

        @Override
        public int getItemCount() {
            return sellerOrderList.size();
        }

        public void setSellerOrderList(List<SellerOrderItem> orderList) {
            this.sellerOrderList = orderList;
        }

        public class SellerOrderViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView, productPriceTextView, userNameTextView,userIdTextView;

            private final Context context;

            public SellerOrderViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.order_date);
                productNameTextView = itemView.findViewById(R.id.tv_product_name);
                productPriceTextView = itemView.findViewById(R.id.tv_product_price);
                userNameTextView = itemView.findViewById(R.id.tv_user_name);
                userIdTextView= itemView.findViewById(R.id.tv_user_id);

            }

            void bind(SellerOrderItem sellerOrderItem) {
                dateTextView.setText(sellerOrderItem.paymentDate);
                userNameTextView.setText(sellerOrderItem.userName);
                userIdTextView.setText(sellerOrderItem.userId);
                productNameTextView.setText(sellerOrderItem.productName);
                productPriceTextView.setText((String.valueOf(sellerOrderItem.productPrice))+"원");

            }
        }
    }
}