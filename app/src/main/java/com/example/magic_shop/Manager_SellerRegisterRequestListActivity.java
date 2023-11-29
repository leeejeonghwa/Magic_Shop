package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Manager_SellerRegisterRequestListActivity extends AppCompatActivity {

    public List<SellerRegisterRequestItem> getSellerRegisterRequestList() {
        List<SellerRegisterRequestItem> sellerRegisterRequestList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        sellerRegisterRequestList.add(new SellerRegisterRequestItem("2023-11-27", "브랜드 A"));


        return sellerRegisterRequestList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_seller_register_request_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.seller_register_request_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<SellerRegisterRequestItem> sellerRegisterRequestList = getSellerRegisterRequestList();
        SellerRegisterRequestAdapter adapter = new SellerRegisterRequestAdapter(sellerRegisterRequestList, this);
        recyclerView.setAdapter(adapter);
    }

    public class SellerRegisterRequestItem {
        String date;
        String sellerName;

        public SellerRegisterRequestItem(String date, String sellerName) {
            this.date = date;
            this.sellerName = sellerName;
        }
    }

    public class SellerRegisterRequestAdapter extends RecyclerView.Adapter<SellerRegisterRequestAdapter.SellerRegisterRequestViewHolder> {
        private List<SellerRegisterRequestItem> sellerRegisterRequestList;
        private Context context;

        SellerRegisterRequestAdapter(List<SellerRegisterRequestItem> sellerRegisterRequestList, Context context) {
            this.sellerRegisterRequestList = sellerRegisterRequestList;
            this.context = context;
        }

        @NonNull
        @Override
        public SellerRegisterRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.manager_item_seller_register_request, parent, false);
            return new SellerRegisterRequestViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull SellerRegisterRequestViewHolder holder, int position) {
            SellerRegisterRequestItem sellerRegisterRequestItem = sellerRegisterRequestList.get(position);
            holder.bind(sellerRegisterRequestItem);
        }

        @Override
        public int getItemCount() { return sellerRegisterRequestList.size(); }

        public class SellerRegisterRequestViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView sellerNameTextView;
            private final Context context;

            public SellerRegisterRequestViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                sellerNameTextView = itemView.findViewById(R.id.sellerName);
            }

            void bind(SellerRegisterRequestItem sellerRegisterRequestItem) {
                dateTextView.setText(sellerRegisterRequestItem.date);
                sellerNameTextView.setText(sellerRegisterRequestItem.sellerName);
            }
        }
    }
}


