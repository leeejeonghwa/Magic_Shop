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

public class Mypage_ExchangeFinishedListActivity extends AppCompatActivity {

    public List<ExchangeItem> exchangeList;
    public ExchangeAdapter exchangeAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_exchange_finished_list);
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

        Button btn_exchange_waiting_list = (Button) findViewById(R.id.btn_exchange_waiting_list);
        btn_exchange_waiting_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_ExchangeWaitingListActivity.class);
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
        String userID = sessionManager.getUserId();

        // 데이터 가져오기
        getExchangeFinishedData(userID, this);
    }

    public void getExchangeFinishedData(String userID, Context context) {
        // Volley 요청 큐 생성
        RequestQueue queue = Volley.newRequestQueue(context);

        // 주문 데이터 요청
        GetExchangeFinishedRequest request = new GetExchangeFinishedRequest(userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 처리하는 코드
                        Log.d("ExchangeFinishedDetails", "Volley Response: " + response);

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
                                Log.e("ExchangeFinishedDetails", "Error: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ExchangeFinishedDetails", "JSON Parsing Error: " + e.getMessage());
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

                String exchangeTime = exchangeObject.getString("createdTime");
                String sellerID = exchangeObject.getString("sellerID");
                String productName = exchangeObject.getString("product_name");
                String productPrice = exchangeObject.getString("product_price");
                String productImage = exchangeObject.getString("main_image");
                String content = exchangeObject.getString("content");

                ExchangeItem exchangeItem = new ExchangeItem(exchangeTime, sellerID, productName, productPrice, productImage, content);
                exchangeList.add(exchangeItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return exchangeList;
    }


    public class ExchangeItem {
        String exchangeTime;
        String sellerID;
        String productName;
        String productPrice;
        String productImage;
        String content;

        public ExchangeItem(String exchangeTime, String sellerID, String productName, String productPrice, String productImage, String content) {
            this.exchangeTime = exchangeTime;
            this.sellerID = sellerID;
            this.productName = productName;
            this.productPrice = productPrice;
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
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_exchange, parent, false);
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
            private final TextView exchangeTimeTextView;
            private final TextView sellerIDTextView;
            private final TextView productNameTextView;
            private final TextView productPriceTextView;
            private final ImageView productImageView;
            private final TextView contentTextView;
            private final Context context;

            public ExchangeViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                exchangeTimeTextView = itemView.findViewById(R.id.exchangeTime);
                sellerIDTextView = itemView.findViewById(R.id.sellerID);
                productNameTextView = itemView.findViewById(R.id.productName);
                productPriceTextView = itemView.findViewById(R.id.productPrice);
                productImageView = itemView.findViewById(R.id.productImage);
                contentTextView = itemView.findViewById(R.id.content);

            }

            void bind(ExchangeItem exchangeItem) {
                exchangeTimeTextView.setText(exchangeItem.exchangeTime);
                sellerIDTextView.setText(exchangeItem.sellerID);
                productNameTextView.setText(exchangeItem.productName);
                productPriceTextView.setText((String.valueOf(exchangeItem.productPrice))+"원");
                contentTextView.setText(exchangeItem.content);

                byte[] decodedString = Base64.decode(exchangeItem.productImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);
            }
        }
    }
}

