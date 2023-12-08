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
import android.widget.RatingBar;
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

public class Mypage_UnreviewedListActivity extends AppCompatActivity {

    public List<UnreviewedItem> unreviewedList;
    public UnreviewedAdapter unreviewedAdapter;
    public Context context;


    public List<UnreviewedItem> getReviewedList(String jsonResponse) throws JSONException {
        List<UnreviewedItem> unreviewedList = new ArrayList<>();

        // 전체 응답을 JSONObject로 변환
        JSONObject responseJson = new JSONObject(jsonResponse);

        // "review" 필드의 값을 JSONArray로 가져오기
        JSONArray jsonArray = responseJson.getJSONArray("review");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String reviewID = jsonObject.getString(("reviewID"));
            String sellerID = jsonObject.getString(("sellerID"));
            String productID = jsonObject.getString(("productID"));
            String userID = jsonObject.getString(("userID"));

            UnreviewedItem reviewedItem = new UnreviewedItem(reviewID, sellerID, productID, userID);

            unreviewedList.add(reviewedItem);
        }

        return unreviewedList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_unreviewed_list);
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

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Mypage_UnreviewedListActivity", "서버 응답: " + response);

                    List<UnreviewedItem> unreviewedList = getReviewedList(response);

                    if (unreviewedAdapter == null) {
                        Log.d("Mypage_UnreviewedListActivity", "Adapter is null. Creating new adapter.");
                        unreviewedAdapter = new UnreviewedAdapter(unreviewedList, context);
                        recyclerView.setAdapter(unreviewedAdapter);
                    } else {
                        Log.d("Mypage_UnreviewedListActivity", "Adapter exists. Updating data.");
                        unreviewedAdapter.setUnreviewedList(unreviewedList);
                        unreviewedAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UnreviewGetRequest unreviewGetRequest = new UnreviewGetRequest(Mypage_UnreviewedListActivity.this, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage_UnreviewedListActivity.this);
        queue.add(unreviewGetRequest);
    }


    public class UnreviewedItem {
        String reviewID;
        String sellerID;
        String productID;
        String userID;

        public UnreviewedItem(String reviewID, String sellerID, String productID, String userID) {
            this.reviewID = reviewID;
            this.sellerID = sellerID;
            this.productID = productID;
            this.userID = userID;
        }
    }


    public class UnreviewedAdapter extends RecyclerView.Adapter<UnreviewedAdapter.UnreviewedViewHolder> {
        private List<UnreviewedItem> unreviewedList;
        private Context context;

        UnreviewedAdapter(List<UnreviewedItem> unreviewedList, Context context) {
            this.unreviewedList = unreviewedList;
            this.context = context;
        }

        public void setUnreviewedList(List<UnreviewedItem> unreviewedList) {
            this.unreviewedList = unreviewedList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UnreviewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.mypage_item_unreviewed, parent, false);
            return new UnreviewedViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull UnreviewedViewHolder holder, int position) {
            UnreviewedItem unreviewedItem = unreviewedList.get(position);
            holder.bind(unreviewedItem);
        }

        @Override
        public int getItemCount() { return unreviewedList.size(); }

        public class UnreviewedViewHolder extends RecyclerView.ViewHolder {
            private final TextView sellerIDTextView;
            private final TextView productIDTextView;
            private final Context context;

            public UnreviewedViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                sellerIDTextView = itemView.findViewById(R.id.sellerID);
                productIDTextView = itemView.findViewById(R.id.productID);
            }

            void bind(UnreviewedItem unreviewedItem) {
                sellerIDTextView.setText(unreviewedItem.sellerID);
                productIDTextView.setText(unreviewedItem.productID);

            }
        }
    }
}

