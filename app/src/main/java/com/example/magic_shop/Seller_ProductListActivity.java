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

public class Seller_ProductListActivity extends AppCompatActivity {

    public List<ProductItem> getProductList() {
        List<ProductItem> productList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        productList.add(new ProductItem("2023-11-27", "상품 A", "S", "1"));
        productList.add(new ProductItem("2023-11-27", "상품 B", "M", "2"));
        productList.add(new ProductItem("2023-11-27", "상품 C", "L", "3"));
        productList.add(new ProductItem("2023-11-27", "상품 D", "S", "4"));
        productList.add(new ProductItem("2023-11-27", "상품 E", "M", "5"));
        // ... 추가적인 데이터

        return productList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_list);
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

        List<ProductItem> productList = getProductList();
        ProductAdapter adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);
    }

    public class ProductItem {
        String date;
        String productName;
        String productSize;
        String productQuantify;

        public ProductItem(String date, String productName, String productSize, String productQuantify) {
            this.date = date;
            this.productName = productName;
            this.productSize = productSize;
            this.productQuantify = productQuantify;
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private List<ProductItem> productList;
        private Context context;

        ProductAdapter(List<ProductItem> productList, Context context) {
            this.productList = productList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_product, parent, false);
            return new ProductViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            ProductItem productItem = productList.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() { return productList.size(); }

        public class ProductViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productQuantifyTextView;
            private final Context context;

            public ProductViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productQuantifyTextView = itemView.findViewById(R.id.productQuantify);
            }

            void bind(ProductItem productRegisterItem) {
                dateTextView.setText(productRegisterItem.date);
                productNameTextView.setText(productRegisterItem.productName);
                productSizeTextView.setText(productRegisterItem.productSize);
                productQuantifyTextView.setText(productRegisterItem.productQuantify);
            }
        }
    }
}


