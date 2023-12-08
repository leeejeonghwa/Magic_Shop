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

public class Seller_ProductReviseRequestListActivity extends AppCompatActivity {

    private ReviseRequestManager reviseRequestManager;

    private ProductReviseRequestAdapter adapter;

    private String userID;


    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_revise_request_list);
        getWindow().setWindowAnimations(0);

        reviseRequestManager = ReviseRequestManager.getInstance(this);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        userID = sessionManager.getUserId();
        reviseRequestManager.checkUserId(userID);

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

        adapter = new ProductReviseRequestAdapter(reviseRequestManager.getReviseProductRequestList(), this);
        recyclerView.setAdapter(adapter);
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        reviseRequestManager.fetchDataFromServer(new ReviseRequestManager.OnDataReceivedListener() {
            @Override
            public void onDataReceived() {

                String str = Integer.toString(reviseRequestManager.getReviseProductRequestList().size());
                Log.d("fetch", str);
                updateUI();
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
    }


    public class ProductReviseRequestAdapter extends RecyclerView.Adapter<ProductReviseRequestAdapter.ProductReviseRequestViewHolder> {
        private List<ProductItem> productReviseRequestList;
        private Context context;

        ProductReviseRequestAdapter(List<ProductItem> productReviseRequestList, Context context) {
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
            ProductItem productItem = productReviseRequestList.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() { return productReviseRequestList.size(); }

        public class ProductReviseRequestViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productQuantifyTextView;

            private final ImageView productMainImageView;
            private final Context context;

            public ProductReviseRequestViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productQuantifyTextView = itemView.findViewById(R.id.productQuantify);
                productMainImageView = itemView.findViewById(R.id.productImage);
            }

            void bind(ProductItem productItem) {
                dateTextView.setText(productItem.date);
                productNameTextView.setText(productItem.productName);
                productSizeTextView.setText(productItem.productSize);
                productQuantifyTextView.setText(productItem.productColor);
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


