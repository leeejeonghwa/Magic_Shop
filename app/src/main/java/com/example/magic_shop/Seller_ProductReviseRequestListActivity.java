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

public class Seller_ProductReviseRequestListActivity extends AppCompatActivity {

    public List<ProductReviseRequestItem> getProductReviseRequestList() {
        List<ProductReviseRequestItem> productReviseRequestList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        productReviseRequestList.add(new ProductReviseRequestItem("2023-11-27", "상품 A", "S", "1"));
        productReviseRequestList.add(new ProductReviseRequestItem("2023-11-27", "상품 B", "M", "2"));
        productReviseRequestList.add(new ProductReviseRequestItem("2023-11-27", "상품 C", "L", "3"));
        productReviseRequestList.add(new ProductReviseRequestItem("2023-11-27", "상품 D", "S", "4"));
        productReviseRequestList.add(new ProductReviseRequestItem("2023-11-27", "상품 E", "M", "5"));
        // ... 추가적인 데이터

        return productReviseRequestList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_revise_request_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_product_list = (Button) findViewById(R.id.btn_product_list);
        btn_product_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductReviseActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_revise_request_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<ProductReviseRequestItem> productReviseRequestList = getProductReviseRequestList();
        ProductReviseRequestAdapter adapter = new ProductReviseRequestAdapter(productReviseRequestList, this);
        recyclerView.setAdapter(adapter);
    }

    public class ProductReviseRequestItem {
        String date;
        String productName;
        String productSize;
        String productQuantity;

        public ProductReviseRequestItem(String date, String productName, String productSize, String productQuantity) {
            this.date = date;
            this.productName = productName;
            this.productSize = productSize;
            this.productQuantity = productQuantity;
        }
    }

    public class ProductReviseRequestAdapter extends RecyclerView.Adapter<ProductReviseRequestAdapter.ProductReviseRequestViewHolder> {
        private List<ProductReviseRequestItem> productReviseRequestList;
        private Context context;

        ProductReviseRequestAdapter(List<ProductReviseRequestItem> productReviseRequestList, Context context) {
            this.productReviseRequestList = productReviseRequestList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductReviseRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_product_revise_request, parent, false);
            return new ProductReviseRequestViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductReviseRequestViewHolder holder, int position) {
            ProductReviseRequestItem productReviseRequestItem = productReviseRequestList.get(position);
            holder.bind(productReviseRequestItem);
        }

        @Override
        public int getItemCount() { return productReviseRequestList.size(); }

        public class ProductReviseRequestViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productQuantityTextView;
            private final Context context;

            public ProductReviseRequestViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productQuantityTextView = itemView.findViewById(R.id.productQuantity);
            }

            void bind(ProductReviseRequestItem productReviseRequestItem) {
                dateTextView.setText(productReviseRequestItem.date);
                productNameTextView.setText(productReviseRequestItem.productName);
                productSizeTextView.setText(productReviseRequestItem.productSize);
                productQuantityTextView.setText(productReviseRequestItem.productQuantity);
            }
        }
    }
}


