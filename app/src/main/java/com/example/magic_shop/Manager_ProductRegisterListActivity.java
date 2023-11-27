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

public class Manager_ProductRegisterListActivity extends AppCompatActivity {

    public List<ProductUnregisteredItem> getProductUnregisteredList() {
        List<ProductUnregisteredItem> productUnregisteredList = new ArrayList<>();

        // 예시 데이터를 추가합니다. 실제 데이터는 여기서 가져와야 합니다.
        productUnregisteredList.add(new ProductUnregisteredItem("2023-11-27", "상품 A", "S", "1"));
        productUnregisteredList.add(new ProductUnregisteredItem("2023-11-27", "상품 B", "M", "2"));
        productUnregisteredList.add(new ProductUnregisteredItem("2023-11-27", "상품 C", "L", "3"));
        productUnregisteredList.add(new ProductUnregisteredItem("2023-11-27", "상품 D", "S", "4"));
        productUnregisteredList.add(new ProductUnregisteredItem("2023-11-27", "상품 E", "M", "5"));
        // ... 추가적인 데이터

        return productUnregisteredList;
    }

    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_product_register);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_review_list = (Button) findViewById(R.id.btn_product_registered_list);
        btn_review_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager_ProductRegisterListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_unregistered_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<ProductUnregisteredItem> productUnregisteredList = getProductUnregisteredList();
        ProductUnregisteredAdapter adapter = new ProductUnregisteredAdapter(productUnregisteredList, this);
        recyclerView.setAdapter(adapter);
    }

    public class ProductUnregisteredItem {
        String date;
        String productName;
        String productSize;
        String productQuantify;

        public ProductUnregisteredItem(String date, String productName, String productSize, String productQuantify) {
            this.date = date;
            this.productName = productName;
            this.productSize = productSize;
            this.productQuantify = productQuantify;
        }
    }

    public class ProductUnregisteredAdapter extends RecyclerView.Adapter<ProductUnregisteredAdapter.ProductUnregisteredViewHolder> {
        private List<ProductUnregisteredItem> productUnregisteredList;
        private Context context;

        ProductUnregisteredAdapter(List<ProductUnregisteredItem> productUnregisteredList, Context context) {
            this.productUnregisteredList = productUnregisteredList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductUnregisteredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.manager_item_product_register, parent, false);
            return new ProductUnregisteredViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductUnregisteredViewHolder holder, int position) {
            ProductUnregisteredItem productUnregisteredItem = productUnregisteredList.get(position);
            holder.bind(productUnregisteredItem);
        }

        @Override
        public int getItemCount() { return productUnregisteredList.size(); }

        public class ProductUnregisteredViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productQuantifyTextView;
            private final Button productRegisterButton;
            private final Context context;

            public ProductUnregisteredViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productQuantifyTextView = itemView.findViewById(R.id.productQuantify);
                productRegisterButton = itemView.findViewById(R.id.btn_product_register);

                productRegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            ProductUnregisteredItem productUnregisteredItem = productUnregisteredList.get(position);
                            Intent intent = new Intent(context, Manager_ProductRegisterListActivity.class);
                            intent.putExtra("date", productUnregisteredItem.date);
                            intent.putExtra("productName", productUnregisteredItem.productName);
                            intent.putExtra("trackingNumber", productUnregisteredItem.productSize);
                            intent.putExtra("trackingNumber", productUnregisteredItem.productQuantify);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            void bind(ProductUnregisteredItem productUnregisteredItem) {
                dateTextView.setText(productUnregisteredItem.date);
                productNameTextView.setText(productUnregisteredItem.productName);
                productSizeTextView.setText(productUnregisteredItem.productSize);
                productQuantifyTextView.setText(productUnregisteredItem.productQuantify);
            }
        }
    }
}

