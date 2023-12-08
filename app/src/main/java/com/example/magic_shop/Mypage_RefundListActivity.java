package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mypage_RefundListActivity extends AppCompatActivity {

    public List<RefundItem> RefundList;
    public RefundAdapter refundAdapter;
    public Context context;


    public List<RefundItem> getRefundList(String jsonResponse) throws JSONException {
        List<RefundItem> refundList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "Refund" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("refund");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String orderID = jsonObject.getString(("orderID"));
            String sellerID = jsonObject.getString(("sellerID"));
            String productID = jsonObject.getString(("productID"));
            String productPrice = jsonObject.getString(("productPrice"));

            RefundItem refundItem = new RefundItem(orderID, sellerID, productID, productPrice);

            refundList.add(refundItem);
        }

        return refundList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_refund_list);
        getWindow().setWindowAnimations(0);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserId();

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_exchange_list = (Button) findViewById(R.id.btn_exchange_list);
        btn_exchange_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_ExchangeListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.refund_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_RefundListActivity", "서버 응답: " + response);

                    List<RefundItem> refundList = getRefundList(response);

                    if (refundAdapter == null) {
                        Log.d("Mypage_RefundListActivity", "Adapter is null. Creating new adapter.");
                        refundAdapter = new RefundAdapter(refundList, context);
                        recyclerView.setAdapter(refundAdapter);
                    } else {
                        Log.d("Mypage_RefundListActivity", "Adapter exists. Updating data.");
                        refundAdapter.setRefundList(refundList);
                        refundAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RefundGetRequest refundGetRequest = new RefundGetRequest(Mypage_RefundListActivity.this, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_RefundListActivity.this);
        queue.add(refundGetRequest);
    }


    public class RefundItem {
        String refundID;
        String sellerID;
        String productID;
        String productPrice;

        public RefundItem(String refundID, String sellerID, String productID, String productPrice) {
            this.refundID = refundID;
            this.sellerID = sellerID;
            this.productID = productID;
            this.productPrice = productPrice;

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
            private final TextView sellerIDTextView;
            private final TextView productIDTextView;
            private final TextView productPriceTextView;
            private final Context context;

            public RefundViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                sellerIDTextView = itemView.findViewById(R.id.sellerID);
                productIDTextView = itemView.findViewById(R.id.productID);
                productPriceTextView = itemView.findViewById(R.id.productPrice);
            }

            void bind(RefundItem refundItem) {
                sellerIDTextView.setText(refundItem.sellerID);
                productIDTextView.setText(refundItem.productID);
                productPriceTextView.setText(refundItem.productPrice);
            }
        }
    }
}

