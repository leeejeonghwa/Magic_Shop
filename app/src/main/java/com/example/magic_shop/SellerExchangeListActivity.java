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

public class SellerExchangeListActivity extends AppCompatActivity {

    public List<ExchangeItem> exchangeList;
    public ExchangeAdapter exchangeAdapter;
    private Response.ErrorListener errorListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_exchange_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerMyPageMainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_refund_list = (Button) findViewById(R.id.btn_refund_list);
        btn_refund_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerRefundListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.exchange_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        exchangeList = new ArrayList<>();
        exchangeAdapter = new ExchangeAdapter(exchangeList, this);
        recyclerView.setAdapter(exchangeAdapter);

        // 사용자 아이디 (실제 사용자 아이디로 변경)
        String userID = sessionManager.getUserID();

        // 데이터 가져오기
        getSellerExchangeData(userID, this);
    }

    public void getSellerExchangeData(String userID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetSellerExchangeRequest request = new GetSellerExchangeRequest(userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 처리하는 코드
                        Log.d("ExchangeListDetails", "Volley Response: " + response);

                        try {
                            // JSON 응답 파싱
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                // 리뷰 데이터가 있는 경우
                                JSONArray exchangesArray = jsonObject.getJSONArray("exchanges");
                                List<ExchangeItem> exchangeList = getExchangeList(exchangesArray);

                                // 어댑터 갱신
                                exchangeAdapter.setExchangeList(exchangeList);
                                exchangeAdapter.notifyDataSetChanged();
                            } else {
                                // 리뷰 데이터가 없는 경우
                                String message = jsonObject.getString("message");
                                Log.e("ExchangeListDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ExchangeListDetails", "JSON Parsing Error: " + e.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ExchangeFinishedDetails", "Volley Error: " + error.getMessage());
                    }
                });

        // 요청을 Volley 큐에 추가
        queue.add(request);
    }

    public List<ExchangeItem> getExchangeList(JSONArray jsonArray) {
        List<ExchangeItem> exchangeList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject exchangeObject = jsonArray.getJSONObject(i);

                String exchangeID = exchangeObject.getString("exchangeID");
                String orderID = exchangeObject.getString("orderID");
                String sellerID = exchangeObject.getString("seller_id");
                String productID = exchangeObject.getString("productID");
                String userID = exchangeObject.getString("userID");
                String productName = exchangeObject.getString("product_name");
                String productImage = exchangeObject.getString("main_image");
                String content = exchangeObject.getString("content");

                ExchangeItem exchangeItem = new ExchangeItem(exchangeID, orderID, sellerID, productID, userID, productName, productImage, content);
                exchangeList.add(exchangeItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return exchangeList;
    }


    public class ExchangeItem {
        String exchangeID;
        String orderID;
        String sellerID;
        String productID;
        String userID;
        String productName;
        String productImage;
        String content;

        public ExchangeItem(String exchangeID, String orderID, String sellerID, String productID, String userID, String productName, String productImage, String content) {
            this.exchangeID = exchangeID;
            this.orderID = orderID;
            this.sellerID = sellerID;
            this.productID = productID;
            this.userID = userID;
            this.productName = productName;
            this.productImage = productImage;
            this.content = content;

        }
    }


    public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> {
        private List<ExchangeItem> exchangeList;
        private Context context;

        ExchangeAdapter(List<ExchangeItem> exchangeList, Context context) {
            this.exchangeList = exchangeList;
            this.context = context;
        }

        public void setExchangeList(List<ExchangeItem> exchangeList) {
            this.exchangeList = exchangeList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_exchange, parent, false);
            return new ExchangeViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
            ExchangeItem exchangeItem = exchangeList.get(position);
            holder.bind(exchangeItem);
        }

        @Override
        public int getItemCount() { return exchangeList.size(); }

        public class ExchangeViewHolder extends RecyclerView.ViewHolder {
            private final TextView userIDTextView;
            private final TextView productNameTextView;
            private final ImageView productImageView;
            private final TextView contentTextView;
            private final Button exchangeButton;
            private final Context context;

            public ExchangeViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                userIDTextView = itemView.findViewById(R.id.userID);
                productNameTextView = itemView.findViewById(R.id.productName);
                productImageView = itemView.findViewById(R.id.productImage);
                contentTextView = itemView.findViewById(R.id.content);
                exchangeButton = itemView.findViewById(R.id.btn_exchange);

            }

            void bind(ExchangeItem exchangeItem) {
                userIDTextView.setText(exchangeItem.userID);
                productNameTextView.setText(exchangeItem.productName);
                contentTextView.setText(exchangeItem.content);

                byte[] decodedString = Base64.decode(exchangeItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);

                String exchangeID = exchangeItem.exchangeID;
                String orderID = exchangeItem.orderID;
                String productID = exchangeItem.productID;
                String sellerID = exchangeItem.sellerID;
                String userID = exchangeItem.userID;

                exchangeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        approveExchange(
                                exchangeID,
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
    private void approveExchange(String exchangeID, String orderID, String sellerID, String productID, String userID) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Seller_ExchangeListActivity", " approveExchange 서버 응답: " + response);

                    if (response.startsWith("<br")) {
                        handleNonJsonResponse(response);
                        return;
                    }

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String successMessage = "교환을 승인하였습니다.";
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();

                        // 리사이클러뷰 어댑터 갱신
                        getSellerExchangeData(userID, SellerExchangeListActivity.this);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "교환 승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Seller_ExchangeListActivity", "JSON 파싱 오류: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 교환 승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Seller_ExchangeListActivity", "예외 발생: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "알 수 없는 오류로 교환 승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        try {
            ApproveExchangeRequest approveExchangeRequest = new ApproveExchangeRequest(exchangeID, orderID, sellerID, productID, userID
                    , responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(SellerExchangeListActivity.this);
            queue.add(approveExchangeRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Mypage_ReviewWriteActivity", "plusReviewWrite 함수 내부에서 예외 발생: " + e.getMessage());
        }
    }
}

