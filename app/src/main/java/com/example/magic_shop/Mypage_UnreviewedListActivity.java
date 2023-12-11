package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class Mypage_UnreviewedListActivity extends AppCompatActivity {

    private List<OrderItem> orderList;
    private OrderAdapter orderAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_unreviewed_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

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

        Button btn_review_list = (Button) findViewById(R.id.btn_reviewed_list);
        btn_review_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_ReviewedListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.unreviewed_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList, this);
        recyclerView.setAdapter(orderAdapter);

        // 사용자 아이디 (실제 사용자 아이디로 변경)
        String userID = sessionManager.getUserId();

        // 주문 데이터 가져오기
        getOrderUnreviewedData(userID, this);
    }

    public void getOrderUnreviewedData(String userID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetOrderUnreviewedDataRequest request = new GetOrderUnreviewedDataRequest(userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 처리하는 코드
                        Log.d("OrderUnreviewedDetails", "Volley Response: " + response);

                        try {
                            // JSON 응답 파싱
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                // 주문 데이터가 있는 경우
                                JSONArray ordersArray = jsonObject.getJSONArray("orders");
                                List<OrderItem> orderList = getOrderList(ordersArray);

                                // 어댑터 갱신
                                orderAdapter.setOrderList(orderList);
                                orderAdapter.notifyDataSetChanged();
                            } else {
                                // 주문 데이터가 없는 경우
                                String message = jsonObject.getString("message");
                                Log.e("OrderUnreviewedDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("OrderUnreviewedDetails", "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OrderUnreviewedDetails", "Volley Error: " + error.getMessage());
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

                // 제품 객체 추출
                JSONObject productsObject = orderObject.getJSONObject("products");

                // 제품 세부 정보 추출
                String productImage = productsObject.getString("main_image");
                String productID = productsObject.getString("productID");
                String productName = productsObject.getString("product_name");
                int productPrice = productsObject.getInt("product_price");
                String sellerID = productsObject.getString("seller_id");

                // OrderItem 생성 및 목록에 추가
                OrderItem orderItem = new OrderItem(orderID, paymentDate, totalAmount, productID, productName, productImage, productPrice, sellerID);
                orderList.add(orderItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderList;
    }


    public class OrderItem {
        int orderID;
        String paymentDate;
        int totalAmount;
        String productID;
        String productName;
        String productImage;
        int productPrice;
        String sellerID;

        public OrderItem(int orderID, String paymentDate, int totalAmount, String productID, String productName, String productImage, int productPrice, String sellerID) {
            this.orderID = orderID;
            this.paymentDate = paymentDate;
            this.totalAmount = totalAmount;
            this.productID = productID;
            this.productName = productName;
            this.productImage = productImage;
            this.productPrice = productPrice;
            this.sellerID = sellerID;
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
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_order_unreviewed, parent, false);
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
            private final ImageView productImageView;
            private final Button reviewWriteButton;
            private final Context context;

            public OrderViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.order_date);
                productNameTextView = itemView.findViewById(R.id.order_productName);
                productPriceTextView = itemView.findViewById(R.id.order_productPrice);
                productBrandTextView = itemView.findViewById(R.id.order_brandName);
                productImageView= itemView.findViewById(R.id.productImage);
                reviewWriteButton = itemView.findViewById(R.id.btn_review_write);

                reviewWriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            OrderItem orderItem = orderList.get(position);
                            Intent intent = new Intent(context, Mypage_ReviewWriteActivity.class);
                            intent.putExtra("orderID", String.valueOf(orderItem.orderID));
                            intent.putExtra("sellerID", orderItem.sellerID);
                            intent.putExtra("productID", orderItem.productID);
                            intent.putExtra("productName", orderItem.productName);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            void bind(OrderItem orderItem) {
                dateTextView.setText(orderItem.paymentDate);
                productNameTextView.setText(orderItem.productName);
                productPriceTextView.setText((String.valueOf(orderItem.productPrice)) + "원");
                productBrandTextView.setText(orderItem.sellerID);

                byte[] decodedString = Base64.decode(orderItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);
            }
        }
    }
}

