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

public class Mypage_ExchangeListActivity extends AppCompatActivity {

    public List<ExchangeItem> ExchangeList;
    public ExchangeAdapter exchangeAdapter;
    public Context context;


    public List<ExchangeItem> getExchangeList(String jsonResponse) throws JSONException {
        List<ExchangeItem> exchangeList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "Exchange" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("exchange");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String exchangeID = jsonObject.getString(("exchangeID"));
            String sellerID = jsonObject.getString(("sellerID"));
            String productID = jsonObject.getString(("productID"));

            ExchangeItem exchangeItem = new ExchangeItem(exchangeID, sellerID, productID);

            exchangeList.add(exchangeItem);
        }

        return exchangeList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_exchange_list);
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

        Button btn_refund_list = (Button) findViewById(R.id.btn_refund_list);
        btn_refund_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_RefundListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.exchange_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_ExchangeListActivity", "서버 응답: " + response);

                    List<ExchangeItem> exchangeList = getExchangeList(response);

                    if (exchangeAdapter == null) {
                        Log.d("Mypage_ExchangeListActivity", "Adapter is null. Creating new adapter.");
                        exchangeAdapter = new ExchangeAdapter(exchangeList, context);
                        recyclerView.setAdapter(exchangeAdapter);
                    } else {
                        Log.d("Mypage_ExchangeListActivity", "Adapter exists. Updating data.");
                        exchangeAdapter.setExchangeList(exchangeList);
                        exchangeAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ExchangeGetRequest questionGetRequest = new ExchangeGetRequest(Mypage_ExchangeListActivity.this, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_ExchangeListActivity.this);
        queue.add(questionGetRequest);
    }


    public class ExchangeItem {
        String exchangeID;
        String sellerID;
        String productID;

        public ExchangeItem(String exchangeID, String sellerID, String productID) {
            this.exchangeID = exchangeID;
            this.sellerID = sellerID;
            this.productID = productID;

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
            private final TextView sellerIDTextView;
            private final TextView productIDTextView;
            private final Context context;

            public ExchangeViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                sellerIDTextView = itemView.findViewById(R.id.sellerID);
                productIDTextView = itemView.findViewById(R.id.productID);
            }

            void bind(ExchangeItem exchangeItem) {
                sellerIDTextView.setText(exchangeItem.sellerID);
                productIDTextView.setText(exchangeItem.productID);
            }
        }
    }
}

