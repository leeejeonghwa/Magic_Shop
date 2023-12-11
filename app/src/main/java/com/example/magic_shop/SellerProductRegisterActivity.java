package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SellerProductRegisterActivity extends AppCompatActivity {

    private RegisteredProductManager registeredProductManager;

    private String userID;

    public Context context;

    private ProductAdapter adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_register);
        getWindow().setWindowAnimations(0);

        registeredProductManager = RegisteredProductManager.getInstance(this);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        userID = sessionManager.getUserID();

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_product_register_plus = (Button) findViewById(R.id.btn_product_register_plus);
        btn_product_register_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerProductRegisterRequestActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_register_request_list = (Button) findViewById(R.id.btn_product_register_request_list);
        btn_product_register_request_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerProductRegisterRequestListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        registeredProductManager.checkUserId(userID);

        adapter = new ProductAdapter(registeredProductManager.getRegisteredProductList(), this);

        recyclerView.setAdapter(adapter);

        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        registeredProductManager.fetchDataFromServer(new RegisteredProductManager.OnDataReceivedListener() {
            @Override
            public void onDataReceived() {
                String str = Integer.toString(registeredProductManager.getRegisteredProductList().size());
                Log.d("fetch", str);
                updateUI();
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
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
            private final TextView productColorTextView;

            private final ImageView productMainImageView;

            private final Context context;

            public ProductViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productColorTextView = itemView.findViewById(R.id.productQuantity);
                productMainImageView = itemView.findViewById(R.id.productImage);

            }

            void bind(ProductItem productRegisterItem) {
                dateTextView.setText(productRegisterItem.date);
                productNameTextView.setText(productRegisterItem.productName);
                productSizeTextView.setText(productRegisterItem.productSize);
                productColorTextView.setText(productRegisterItem.productColor);
                if (productRegisterItem.mainImage == null) {
                    Log.d("이미지", "null");
                }
                else {
                    productMainImageView.setImageBitmap(productRegisterItem.mainImage);
                }
            }
        }
    }
}


