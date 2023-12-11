package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SellerProductReviseRequestActivity extends AppCompatActivity {

    private EditText productNameET;
    private EditText productPriceET;
    private String productId;

    private String productName;

    private  String productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_revise_request);
        getWindow().setWindowAnimations(0);

        init();

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Retrieve values from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
        }

        Button btn_submit = findViewById(R.id.btn_revise_request_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values from the EditText fields
                productName = productNameET.getText().toString().trim();
                productPrice = productPriceET.getText().toString().trim();

                // Check if the values are not empty
                if (!productName.isEmpty() && !productPrice.isEmpty()) {
                    // Send data to PHP script for processing
                    registerProduct(productId, productName, productPrice);
                    Intent intent = new Intent(getApplicationContext(), SellerProductReviseActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void init() {
        productNameET = findViewById(R.id.productName);
        productPriceET = findViewById(R.id.productPrice);
    }

    private void registerProduct(String productId, String productName, String productPrice) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Specify the URL of your PHP script
        String url = "http://210.117.175.207:8976/revise_product.php";

        // Create a HashMap to hold the parameters
        Map<String, String> params = new HashMap<>();
        params.put("productId", String.valueOf(productId));
        params.put("productName", productName);

        params.put("productPrice", productPrice);



        // Create a new StringRequest using POST method
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server (if needed)
                        Log.d("판매자 수정 등록", "서버 연결 성공");
                        Toast.makeText(SellerProductReviseRequestActivity.this, response, Toast.LENGTH_SHORT).show();
                        // 아래 코드를 통해 A 액티비티로 이동합니다.
                        Intent intent = new Intent(getApplicationContext(), MyPageMainActivity.class);
                        // 필요한 경우에 데이터를 전달할 수 있습니다.
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occur during the request
                        Log.d("판매자 수정 등록", "fail");

                        Toast.makeText(SellerProductReviseRequestActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

}

