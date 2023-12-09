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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seller_ProductDeleteActivity extends AppCompatActivity {

    private RegisteredProductManager registeredProductManager;

    private ProductDeleteAdapter adapter;

    private String id;

    public Context context;

    private String userID;

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

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        userID = sessionManager.getUserId();

        registeredProductManager = RegisteredProductManager.getInstance(this);
        registeredProductManager.setManager(false);
        registeredProductManager.checkUserId(userID);
        adapter = new ProductDeleteAdapter(registeredProductManager.getRegisteredProductList(), this);

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

    private void registerProduct(String productId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Specify the URL of your PHP script
        String url = "http://210.117.175.207:8976/product_delete.php";

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
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        // 아래 코드를 통해 A 액티비티로 이동합니다.
                        Intent intent = new Intent(context, Mypage_MainActivity.class);
                        // 필요한 경우에 데이터를 전달할 수 있습니다.
                        context.startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occur during the request
                        Log.d("관리자 수정 승인", "fail");

                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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



    public class ProductDeleteAdapter extends RecyclerView.Adapter<ProductDeleteAdapter.ProductDeleteViewHolder> {
        private List<ProductItem> productDeleteList;
        private Context context;

        ProductDeleteAdapter(List<ProductItem> productDeleteList, Context context) {
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
            ProductItem productItem = productDeleteList.get(position);
            holder.bind(productItem);
        }

        @Override
        public int getItemCount() { return productDeleteList.size(); }

        public class ProductDeleteViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productColorTextView;
            private final Button productDeleteButton;

            private final ImageView productMainImageView;

            private final Context context;

            public ProductDeleteViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productColorTextView = itemView.findViewById(R.id.productQuantify);
                productDeleteButton = itemView.findViewById(R.id.btn_product_delete);
                productMainImageView = itemView.findViewById(R.id.productImage);

                productDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            ProductItem productItem = productDeleteList.get(position);

                            id = productItem.id;
                            Log.d("클릭된 상품id", id);

                        }

                        registerProduct(id);
                    }
                });

            }

            void bind(ProductItem productUnregisteredItem) {
                dateTextView.setText(productUnregisteredItem.date);
                productNameTextView.setText(productUnregisteredItem.productName);
                productSizeTextView.setText(productUnregisteredItem.productSize);
                productColorTextView.setText(productUnregisteredItem.productColor);
                if (productUnregisteredItem.mainImage == null) {
                    Log.d("이미지", "null");
                }
                else {
                    productMainImageView.setImageBitmap(productUnregisteredItem.mainImage);
                }

            }
        }
    }
}
