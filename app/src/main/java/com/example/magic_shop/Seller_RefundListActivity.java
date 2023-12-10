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
import android.widget.Toast;

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

public class Seller_RefundListActivity extends AppCompatActivity {

    public List<RefundItem> refundList;
    public RefundAdapter refundAdapter;
    private Response.ErrorListener errorListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_refund_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_MypageMainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_exchange_list = (Button) findViewById(R.id.btn_exchange_list);
        btn_exchange_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ExchangeListActivity.class);
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
        getSellerRefundData(userID, this);
    }

    public void getSellerRefundData(String userID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetSellerRefundRequest request = new GetSellerRefundRequest(userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 처리하는 코드
                        Log.d("SellerRefundDetails", "Volley Response: " + response);

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
                                Log.e("SellerRefundDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("SellerRefundDetails", "JSON Parsing Error: " + e.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SellerRefundDetails", "Volley Error: " + error.getMessage());
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

                String refundID = refundObject.getString("refundID");
                String orderID = refundObject.getString("orderID");
                String sellerID = refundObject.getString("seller_id");
                String productID = refundObject.getString("productID");
                String userID = refundObject.getString("userID");
                String productName = refundObject.getString("product_name");
                String productImage = refundObject.getString("main_image");
                String content = refundObject.getString("content");

                RefundItem refundItem = new RefundItem(refundID, orderID, sellerID, productID, userID, productName, productImage, content);
                refundList.add(refundItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return refundList;
    }


    public class RefundItem {
        String refundID;
        String orderID;
        String sellerID;
        String productID;
        String userID;
        String productName;
        String productImage;
        String content;

        public RefundItem(String refundID, String orderID, String sellerID, String productID, String userID, String productName, String productImage, String content) {
            this.refundID = refundID;
            this.orderID = orderID;
            this.sellerID = sellerID;
            this.productID = productID;
            this.userID = userID;
            this.productName = productName;
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
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_refund, parent, false);
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
            private final TextView userIDTextView;
            private final TextView productNameTextView;
            private final ImageView productImageView;
            private final TextView contentTextView;
            private final Button refundButton;
            private final Context context;

            public RefundViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                userIDTextView = itemView.findViewById(R.id.userID);
                productNameTextView = itemView.findViewById(R.id.productName);
                productImageView = itemView.findViewById(R.id.productImage);
                contentTextView = itemView.findViewById(R.id.content);
                refundButton = itemView.findViewById(R.id.btn_refund);

            }

            void bind(RefundItem refundItem) {
                userIDTextView.setText(refundItem.userID);
                productNameTextView.setText(refundItem.productName);
                contentTextView.setText(refundItem.content);

                byte[] decodedString = Base64.decode(refundItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);

                String refundID = refundItem.refundID;
                String orderID = refundItem.orderID;
                String productID = refundItem.productID;
                String sellerID = refundItem.sellerID;
                String userID = refundItem.userID;

                refundButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        approveRefund(
                                refundID,
                                orderID,
                                sellerID,
                                productID,
                                userID
                        );
                    }
                });
            }
        }
    }

    private void handleNonJsonResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String productID = jsonResponse.getString("productID");
            String userID = jsonResponse.getString("userID");
            String content = jsonResponse.getString("content");

            // 텍스트 뷰에 값을 설정합니다.
//            editTextProductID.setText(productID);
//            editTextRecipient.setText(recipient);
//            editTextPhoneNumber.setText(phoneNumber);
//            editTextAddress.setText(address);
//            editTextDeliveryAddressRequest.setText(deliveryRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 리뷰 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    private void approveRefund(String refundID, String orderID, String sellerID, String productID, String userID) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Seller_RefundListActivity", " approveRefund 서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        handleNonJsonResponse(response);
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String successMessage = "환불을 승인하였습니다.";
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();

                        // 리사이클러뷰 어댑터 갱신
                        getSellerRefundData(userID, Seller_RefundListActivity.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "환불 승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Seller_RefundListActivity", "JSON 파싱 오류: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 환불 승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Seller_RefundListActivity", "예외 발생: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "알 수 없는 오류로 환불 승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            ApproveRefundRequest approveRefundRequest = new ApproveRefundRequest(refundID, orderID, sellerID, productID, userID
                    , responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(Seller_RefundListActivity.this);
            queue.add(approveRefundRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Seller_RefundListActivity", "approveRefund 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }
}

