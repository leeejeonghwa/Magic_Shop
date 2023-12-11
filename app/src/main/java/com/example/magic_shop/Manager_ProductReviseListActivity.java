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

public class Manager_ProductReviseListActivity extends AppCompatActivity {

    private ReviseRequestManager reviseRequestManager;

    private  ProductReviseAdapter adapter;


    public Context context;

    private String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_product_revise);
        getWindow().setWindowAnimations(0);


        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.product_revise_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reviseRequestManager = ReviseRequestManager.getInstance(this);
        reviseRequestManager.setManager(true);


        adapter = new ProductReviseAdapter(reviseRequestManager.getReviseProductRequestList(), this);


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
                        Toast.makeText(Manager_ProductReviseListActivity.this, response, Toast.LENGTH_SHORT).show();
                        // 아래 코드를 통해 A 액티비티로 이동합니다.
                        Intent intent = new Intent(context, Manager_MainActivity.class);
                        // 필요한 경우에 데이터를 전달할 수 있습니다.
                        context.startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occur during the request
                        Log.d("관리자 수정 승인", "fail");

                        Toast.makeText(Manager_ProductReviseListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
            View view = LayoutInflater.from(context).inflate(R.layout.manager_item_product_revise, parent, false);
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
            private final TextView brandNameTextView;
            private final TextView productNameTextView;
            private final TextView productSizeTextView;
            private final TextView productColorTextView;

            private final ImageView productMainImageView;
            private final Context context;

            public ProductReviseViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                brandNameTextView = itemView.findViewById(R.id.sellerName);
                productNameTextView = itemView.findViewById(R.id.productName);
                productSizeTextView = itemView.findViewById(R.id.productSize);
                productColorTextView = itemView.findViewById(R.id.productQuantity);
                Button productReviseButton = itemView.findViewById(R.id.btn_product_revise_manager);
                productMainImageView = itemView.findViewById(R.id.productImage);

                productReviseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 클릭 이벤트 처리
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // 다음 화면으로 이동하는 코드
                            ProductItem productItem = productReviseList.get(position);

                            id = productItem.id;
                            Log.d("클릭된 상품id", id);

                        }

                        registerProduct(id);

                    }
                });


            }

            void bind(ProductItem productUnregisteredItem) {
                dateTextView.setText(productUnregisteredItem.date);
                brandNameTextView.setText(productUnregisteredItem.sellerId);
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

