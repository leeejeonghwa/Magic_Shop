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

public class Seller_ProductRegisterRequestListActivity extends AppCompatActivity {

    public List<ProductRegisterRequestItem> getProductRegisterRequestList() {
        List<ProductRegisterRequestItem> productRegisterRequestList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        productRegisterRequestList.add(new ProductRegisterRequestItem("2023-11-27", "상품 A", "S", "1"));
        productRegisterRequestList.add(new ProductRegisterRequestItem("2023-11-27", "상품 B", "M", "2"));
        productRegisterRequestList.add(new ProductRegisterRequestItem("2023-11-27", "상품 C", "L", "3"));
        productRegisterRequestList.add(new ProductRegisterRequestItem("2023-11-27", "상품 D", "S", "4"));
        productRegisterRequestList.add(new ProductRegisterRequestItem("2023-11-27", "상품 E", "M", "5"));
        // ... 추가적인 데이터

        return productRegisterRequestList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_register_request_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_MypageMainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_list = (Button) findViewById(R.id.btn_product_list);
        btn_product_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductRegisterActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_register_request_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<ProductRegisterRequestItem> productRegisterRequestList = getProductRegisterRequestList();
        ProductRegisterRequestAdapter adapter = new ProductRegisterRequestAdapter(productRegisterRequestList, this);
        recyclerView.setAdapter(adapter);
    }

    public class ProductRegisterRequestItem {
        String date;
        String productName;
        String productSize;
        String productQuantify;

        public ProductRegisterRequestItem(String date, String productName, String productSize, String productQuantify) {
            this.date = date;
            this.productName = productName;
            this.productSize = productSize;
            this.productQuantify = productQuantify;
        }
    }

    public class ProductRegisterRequestAdapter extends RecyclerView.Adapter<ProductRegisterRequestAdapter.ProductRegisterRequestViewHolder> {
        private List<ProductRegisterRequestItem> productRegisterRequestList;
        private Context context;

        ProductRegisterRequestAdapter(List<ProductRegisterRequestItem> productRegisterRequestList, Context context) {
            this.productRegisterRequestList = productRegisterRequestList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductRegisterRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_product_register_request, parent, false);
            return new ProductRegisterRequestViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductRegisterRequestViewHolder holder, int position) {
            ProductRegisterRequestItem productRegisterRequestItem = productRegisterRequestList.get(position);
            holder.bind(productRegisterRequestItem);
        }

        @Override
        public int getItemCount() { return productRegisterRequestList.size(); }

        public class ProductRegisterRequestViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productQuantifyTextView;
            private final Context context;

            public ProductRegisterRequestViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productQuantifyTextView = itemView.findViewById(R.id.productQuantify);
            }

            void bind(ProductRegisterRequestItem productRegisterRequestItem) {
                dateTextView.setText(productRegisterRequestItem.date);
                productNameTextView.setText(productRegisterRequestItem.productName);
                productSizeTextView.setText(productRegisterRequestItem.productSize);
                productQuantifyTextView.setText(productRegisterRequestItem.productQuantify);
            }
        }
    }
}


