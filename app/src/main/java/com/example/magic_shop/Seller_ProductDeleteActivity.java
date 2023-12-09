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

public class Seller_ProductDeleteActivity extends AppCompatActivity {

    public List<ProductDeleteItem> getProductDeleteList() {
        List<ProductDeleteItem> productDeleteList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        productDeleteList.add(new ProductDeleteItem("2023-11-27", "상품 A", "S", "1"));
        productDeleteList.add(new ProductDeleteItem("2023-11-27", "상품 B", "M", "2"));
        productDeleteList.add(new ProductDeleteItem("2023-11-27", "상품 C", "L", "3"));
        productDeleteList.add(new ProductDeleteItem("2023-11-27", "상품 D", "S", "4"));
        productDeleteList.add(new ProductDeleteItem("2023-11-27", "상품 E", "M", "5"));
        // ... 추가적인 데이터

        return productDeleteList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_delete);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<ProductDeleteItem> productDeleteList = getProductDeleteList();
        ProductDeleteAdapter adapter = new ProductDeleteAdapter(productDeleteList, this);
        recyclerView.setAdapter(adapter);
    }

    public class ProductDeleteItem {
        String date;
        String productName;
        String productSize;
        String productQuantity;

        public ProductDeleteItem(String date, String productName, String productSize, String productQuantity) {
            this.date = date;
            this.productName = productName;
            this.productSize = productSize;
            this.productQuantity = productQuantity;
        }
    }

    public class ProductDeleteAdapter extends RecyclerView.Adapter<ProductDeleteAdapter.ProductDeleteViewHolder> {
        private List<ProductDeleteItem> productDeleteList;
        private Context context;

        ProductDeleteAdapter(List<ProductDeleteItem> productDeleteList, Context context) {
            this.productDeleteList = productDeleteList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductDeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_product_delete, parent, false);
            return new ProductDeleteViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductDeleteViewHolder holder, int position) {
            ProductDeleteItem productDeleteItem = productDeleteList.get(position);
            holder.bind(productDeleteItem);
        }

        @Override
        public int getItemCount() { return productDeleteList.size(); }

        public class ProductDeleteViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productQuantityTextView;
            private final Button productDeleteButton;
            private final Context context;

            public ProductDeleteViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productQuantityTextView = itemView.findViewById(R.id.productQuantity);
                productDeleteButton = itemView.findViewById(R.id.btn_product_delete);

            }

            void bind(ProductDeleteItem productUnregisteredItem) {
                dateTextView.setText(productUnregisteredItem.date);
                productNameTextView.setText(productUnregisteredItem.productName);
                productSizeTextView.setText(productUnregisteredItem.productSize);
                productQuantityTextView.setText(productUnregisteredItem.productQuantity);
            }
        }
    }
}
