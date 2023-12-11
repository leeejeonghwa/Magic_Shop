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

public class Seller_ProductReviseActivity extends AppCompatActivity {

    private RegisteredProductManager registeredProductManager;

    public Context context;

    private ProductReviseAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_revise);
        getWindow().setWindowAnimations(0);

        registeredProductManager = RegisteredProductManager.getInstance(this);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserId();

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btn_product_revise_request_list = (Button) findViewById(R.id.btn_product_revise_request_list);
        btn_product_revise_request_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seller_ProductReviseRequestListActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_revise_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        registeredProductManager.checkUserId(userID);

        adapter = new ProductReviseAdapter(registeredProductManager.getRegisteredProductList(), this);
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



    public class ProductReviseAdapter extends RecyclerView.Adapter<ProductReviseAdapter.ProductReviseViewHolder> {
        private List<ProductItem> productReviseList;
        private Context context;

        ProductReviseAdapter(List<ProductItem> productReviseList, Context context) {
            this.productReviseList = productReviseList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductReviseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.seller_item_product_revise, parent, false);
            return new ProductReviseViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductReviseViewHolder holder, int position) {
            ProductItem productItem = productReviseList.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() { return productReviseList.size(); }

        public class ProductReviseViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productColorTextView;

            private final ImageView productMainImageView;
            private final Context context;

            public ProductReviseViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productColorTextView = itemView.findViewById(R.id.productQuantity);
                Button productReviseButton = itemView.findViewById(R.id.btn_product_revise);
                productMainImageView = itemView.findViewById(R.id.productImage);

                productReviseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            ProductItem productItem = productReviseList.get(position);
                            Intent intent = new Intent(context, Seller_ProductReviseRequestActivity.class);
                            intent.putExtra("id", productItem.brandName);
                            intent.putExtra("date", productItem.date);
                            intent.putExtra("productName", productItem.productName);
                            intent.putExtra("productSize", productItem.productSize);
                            intent.putExtra("productColor", productItem.productColor);
                            intent.putExtra("productId", productItem.id);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            void bind(ProductItem productItem) {
                dateTextView.setText(productItem.date);
                productNameTextView.setText(productItem.productName);
                productSizeTextView.setText(productItem.productSize);
                productColorTextView.setText(productItem.productColor);
                if (productItem.mainImage == null) {
                    Log.d("이미지", "null");
                }
                else {
                    productMainImageView.setImageBitmap(productItem.mainImage);
                }
            }
        }
    }
}

