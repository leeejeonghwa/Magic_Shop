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

public class SellerProductRegisterRequestListActivity extends AppCompatActivity {


    private ProductRegisterRequestManager productRegisterRequestManager;

    public Context context;

    private ProductRegisterRequestAdapter adapter;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_register_request_list);
        getWindow().setWindowAnimations(0);

        productRegisterRequestManager = ProductRegisterRequestManager.getInstance(this);
        productRegisterRequestManager.setManager(false);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String userID = sessionManager.getUserID();



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
                Intent intent = new Intent(getApplicationContext(), SellerProductRegisterActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_register_request_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        productRegisterRequestManager.checkUserId(userID);

        adapter = new ProductRegisterRequestAdapter(productRegisterRequestManager.getProductRegisterRequestList(), this);
        //List<ProductRegisterRequestItem> productRegisterRequestList = getProductRegisterRequestList();
        //ProductRegisterRequestAdapter adapter = new ProductRegisterRequestAdapter(productRegisterRequestList, this);
        recyclerView.setAdapter(adapter);

        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        productRegisterRequestManager.fetchDataFromServer(new ProductRegisterRequestManager.OnDataReceivedListener() {
            @Override
            public void onDataReceived() {
                String str = Integer.toString(productRegisterRequestManager.getProductRegisterRequestList().size());
                Log.d("fetch", str);
                updateUI();
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
    }

    public class ProductRegisterRequestAdapter extends RecyclerView.Adapter<ProductRegisterRequestAdapter.ProductRegisterRequestViewHolder> {
        private List<ProductItem> productRegisterRequestList;
        private Context context;

        ProductRegisterRequestAdapter(List<ProductItem> productRegisterRequestList, Context context) {
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
            ProductItem productItem = productRegisterRequestList.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() {
            return productRegisterRequestList.size();
        }

        public class ProductRegisterRequestViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productColorTextView;

            private final ImageView productMainImageView;
            private final Context context;

            public ProductRegisterRequestViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productColorTextView = itemView.findViewById(R.id.productColor);
                productMainImageView = itemView.findViewById(R.id.productImage);

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


