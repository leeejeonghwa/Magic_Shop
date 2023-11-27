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

public class Manager_ProductRegisteredActivity extends AppCompatActivity {

    public List<ProductRegisteredItem> getProductRegisteredList() {
        List<ProductRegisteredItem> productRegisteredList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        productRegisteredList.add(new ProductRegisteredItem("2023-11-27", "상품 A", "S", "3"));
        // ... 추가적인 데이터

        return productRegisteredList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_product_registered);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_review_list = (Button) findViewById(R.id.btn_product_unregistered_list);
        btn_review_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager_ProductUnregisteredActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_registered_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<ProductRegisteredItem> productRegisteredList = getProductRegisteredList(); // 여러 배송지 정보를 가져오는 메서드
        ProductRegisteredAdapter adapter = new ProductRegisteredAdapter(productRegisteredList, this);
        recyclerView.setAdapter(adapter);
    }

    public class ProductRegisteredItem {
        String date;
        String productName;
        String productSize;
        String productQuantify;

        public ProductRegisteredItem(String date, String productName, String productSize, String productQuantify) {
            this.date = date;
            this.productName = productName;
            this.productSize = productSize;
            this.productQuantify = productQuantify;
        }
    }

    public class ProductRegisteredAdapter extends RecyclerView.Adapter<ProductRegisteredAdapter.ProductRegisteredViewHolder> {
        private List<ProductRegisteredItem> productRegisteredList;
        private Context context;

        ProductRegisteredAdapter(List<ProductRegisteredItem> productRegisteredList, Context context) {
            this.productRegisteredList = productRegisteredList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductRegisteredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.manager_item_product_registered, parent, false);
            return new ProductRegisteredViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductRegisteredViewHolder holder, int position) {
            ProductRegisteredItem productRegisteredItem = productRegisteredList.get(position);
            holder.bind(productRegisteredItem);
        }

        @Override
        public int getItemCount() { return productRegisteredList.size(); }

        public class ProductRegisteredViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productQuantifyTextView;
            private final Context context;

            public ProductRegisteredViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productQuantifyTextView = itemView.findViewById(R.id.productQuantify);
            }

            void bind(ProductRegisteredItem productRegisteredItem) {
                dateTextView.setText(productRegisteredItem.date);
                productNameTextView.setText(productRegisteredItem.productName);
                productSizeTextView.setText(productRegisteredItem.productSize);
                productQuantifyTextView.setText(productRegisteredItem.productQuantify);
            }
        }
    }
}

