package com.example.magic_shop;

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

public class Mypage_RefundWaitingListActivity extends AppCompatActivity {

    public List<RefundItem> refundList;
    public RefundAdapter refundAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_refund_waiting_list);
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

        Button btn_refund_finished_list = (Button) findViewById(R.id.btn_refund_finished_list);
        btn_refund_finished_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_RefundFinishedListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.refund_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        refundList = new ArrayList<>();
        refundAdapter = new RefundAdapter(refundList, this);
        recyclerView.setAdapter(refundAdapter);

        // 사용자 아이디 (실제 사용자 아이디로 변경)
        String userID = sessionManager.getUserId();

        // 데이터 가져오기
        getRefundWaitingData(userID, this);
    }

    public void getRefundWaitingData(String userID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetRefundWaitingRequest request = new GetRefundWaitingRequest(userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 처리하는 코드
                        Log.d("RefundFinishedDetails", "Volley Response: " + response);

                        try {
                            // JSON 응답 파싱
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                // 리뷰 데이터가 있는 경우
                                JSONArray refundsArray = jsonObject.getJSONArray("refunds");
                                List<RefundItem> refundList = getRefundList(refundsArray);

                                // 어댑터 갱신
                                refundAdapter.setRefundList(refundList);
                                refundAdapter.notifyDataSetChanged();
                            } else {
                                // 리뷰 데이터가 없는 경우
                                String message = jsonObject.getString("message");
                                Log.e("RefundFinishedDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("RefundFinishedDetails", "JSON Parsing Error: " + e.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RefundFinishedDetails", "Volley Error: " + error.getMessage());
                    }
                });

        // 요청을 Volley 큐에 추가
        queue.add(request);
    }

    public List<RefundItem> getRefundList(JSONArray jsonArray) {
        List<RefundItem> refundList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject refundObject = jsonArray.getJSONObject(i);

                String refundTime = refundObject.getString("createdTime");
                String brandName = refundObject.getString("sellerID");
                String productName = refundObject.getString("product_name");
                String productPrice = refundObject.getString("product_price");
                String productImage = refundObject.getString("main_image");
                String content = refundObject.getString("content");

                RefundItem refundItem = new RefundItem(refundTime, brandName, productName, productPrice, productImage, content);
                refundList.add(refundItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return refundList;
    }


    public class RefundItem {
        String refundTime;
        String brandName;
        String productName;
        String productPrice;
        String productImage;
        String content;

        public RefundItem(String refundTime, String brandName, String productName, String productPrice, String productImage, String content) {
            this.refundTime = refundTime;
            this.brandName = brandName;
            this.productName = productName;
            this.productPrice = productPrice;
            this.productImage = productImage;
            this.content = content;

        }
    }


    public class RefundAdapter extends RecyclerView.Adapter<RefundAdapter.RefundViewHolder> {
        private List<RefundItem> refundList;
        private Context context;

        RefundAdapter(List<RefundItem> refundList, Context context) {
            this.refundList = refundList;
            this.context = context;
        }

        public void setRefundList(List<RefundItem> refundList) {
            this.refundList = refundList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RefundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_refund, parent, false);
            return new RefundViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull RefundViewHolder holder, int position) {
            RefundItem refundItem = refundList.get(position);
            holder.bind(refundItem);
        }

        @Override
        public int getItemCount() { return refundList.size(); }

        public class RefundViewHolder extends RecyclerView.ViewHolder {
            private final TextView refundTimeTextView;
            private final TextView brandNameTextView;
            private final TextView productNameTextView;
            private final ImageView productImageView;
            private final TextView contentTextView;
            private final TextView productPriceTextView;
            private final Context context;

            public RefundViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                refundTimeTextView = itemView.findViewById(R.id.refundTime);
                brandNameTextView = itemView.findViewById(R.id.sellerID);
                productNameTextView = itemView.findViewById(R.id.productName);
                productPriceTextView = itemView.findViewById(R.id.productPrice);
                productImageView = itemView.findViewById(R.id.productImage);
                contentTextView = itemView.findViewById(R.id.content);

            }

            void bind(RefundItem refundItem) {
                refundTimeTextView.setText(refundItem.refundTime);
                brandNameTextView.setText(refundItem.brandName);
                productNameTextView.setText(refundItem.productName);
                productPriceTextView.setText((refundItem.productPrice)+"원");
                contentTextView.setText(refundItem.content);

                byte[] decodedString = Base64.decode(refundItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);
            }
        }
    }
}

