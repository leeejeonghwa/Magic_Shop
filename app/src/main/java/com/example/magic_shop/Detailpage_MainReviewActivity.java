package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Detailpage_MainReviewActivity extends AppCompatActivity {
    private Button btnBuy;
    private Button btnProduct;
    private Button btnSize;
    private Button btnAsk;
    private Button btnAllReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_review);

        btnBuy = findViewById(R.id.btn_buy);
        btnProduct = findViewById(R.id.productBtn);
        btnSize = findViewById(R.id.sizeBtn);
        btnAsk = findViewById(R.id.askBtn);
        btnAllReview = findViewById(R.id.allReviewBtn);


        btnBuy.setVisibility(View.VISIBLE);
        btnProduct.setVisibility(View.VISIBLE);
        btnSize.setVisibility(View.VISIBLE);
        btnAsk.setVisibility(View.VISIBLE);
        btnAllReview.setVisibility(View.VISIBLE);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainOptionSelectActivity.class);
                startActivity(intent);
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });

        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainSizeActivity.class);
                startActivity(intent);
            }
        });

        btnAllReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_AllReviewActivity.class);
                startActivity(intent);
            }
        });







    }
}
