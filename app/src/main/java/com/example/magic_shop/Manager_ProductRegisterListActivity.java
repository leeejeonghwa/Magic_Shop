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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager_ProductRegisterListActivity extends AppCompatActivity {

    private ProductRegisterRequestManager registeredProductManager;

    private ProductUnregisteredAdapter adapter;


    public Context context;

    public String productId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_product_register);
        getWindow().setWindowAnimations(0);
        //registeredProductManager.setManager(true);


        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.product_unregistered_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        registeredProductManager = ProductRegisterRequestManager.getInstance(this);

        registeredProductManager.setManager(true);

        adapter = new ProductUnregisteredAdapter(registeredProductManager.getProductRegisterRequestList(), this);
        recyclerView.setAdapter(adapter);
        fetchDataFromServer();

    }

    private void fetchDataFromServer() {
        registeredProductManager.fetchDataFromServer(new ProductRegisterRequestManager.OnDataReceivedListener() {
            @Override
            public void onDataReceived() {
                String str = Integer.toString(registeredProductManager.getProductRegisterRequestList().size());
                Log.d("fetch", str);
                updateUI();
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
    }

    private void registerProduct(String productId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Specify the URL of your PHP script
        String url = "http://210.117.175.207:8976/allowance_product_register.php";

        // Create a HashMap to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("productId", String.valueOf(productId));

        // Create a new StringRequest using POST method
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server (if needed)
                        Log.d("관리자 수정 승인", "서버 연결 성공");
                        Toast.makeText(Manager_ProductRegisterListActivity.this, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Manager_MainActivity.class);
                        // 필요한 경우에 데이터를 전달할 수 있습니다.
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occur during the request
                        Log.d("관리자 수정 승인", "fail");

                        Toast.makeText(Manager_ProductRegisterListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public class ProductUnregisteredAdapter extends RecyclerView.Adapter<ProductUnregisteredAdapter.ProductUnregisteredViewHolder> {
        private List<ProductItem> productUnregisteredList;
        private Context context;

        ProductUnregisteredAdapter(List<ProductItem> productUnregisteredList, Context context) {
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
            ProductItem productItem = productUnregisteredList.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() { return productUnregisteredList.size(); }

        public class ProductUnregisteredViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView sellerNameTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productColorTextView;
            private final Button productRegisterButton;

            private final ImageView productMainImageView;
            private final Context context;

            public ProductUnregisteredViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                sellerNameTextView = itemView.findViewById(R.id.sellerName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productColorTextView = itemView.findViewById(R.id.productQuantity);
                productRegisterButton = itemView.findViewById(R.id.btn_product_register);
                productMainImageView = itemView.findViewById(R.id.productImage);

                productRegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            ProductItem productItem = productUnregisteredList.get(position);

                            productId = productItem.id;
                            Log.d("클릭된 상품id", productId);
                        }

                        registerProduct(productId);
                    }
                });
            }

            void bind(ProductItem productItem) {
                dateTextView.setText(productItem.date);
                sellerNameTextView.setText(productItem.sellerId);
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

