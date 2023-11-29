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

public class Manager_SellerListActivity extends AppCompatActivity {

    public List<SellerItem> getSellerList() {
        List<SellerItem> sellerList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        sellerList.add(new SellerItem("2023-11-27", "브랜드 A"));

        // ... 추가적인 데이터

        return sellerList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_seller_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.seller_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<SellerItem> sellerList = getSellerList();
        SellerAdapter adapter = new SellerAdapter(sellerList, this);
        recyclerView.setAdapter(adapter);
    }

    public class SellerItem {
        String date;
        String sellerName;

        public SellerItem(String date, String sellerName) {
            this.date = date;
            this.sellerName = sellerName;
        }
    }

    public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
        private List<SellerItem> sellerList;
        private Context context;

        SellerAdapter(List<SellerItem> sellerList, Context context) {
            this.sellerList = sellerList;
            this.context = context;
        }

        @NonNull
        @Override
        public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.manager_item_seller, parent, false);
            return new SellerViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
            SellerItem sellerItem = sellerList.get(position);
            holder.bind(sellerItem);
        }

        @Override
        public int getItemCount() { return sellerList.size(); }

        public class SellerViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView sellerNameTextView;
            private final Context context;

            public SellerViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                sellerNameTextView = itemView.findViewById(R.id.sellerName);
            }

            void bind(SellerItem sellerItem) {
                dateTextView.setText(sellerItem.date);
                sellerNameTextView.setText(sellerItem.sellerName);
            }
        }
    }
}


