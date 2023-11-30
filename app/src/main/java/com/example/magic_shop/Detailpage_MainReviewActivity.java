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
    private Button btnBack;
    private Button btnBag;
    private Button btnHome;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_review);

        btnBuy = findViewById(R.id.btn_buy);
        btnProduct = findViewById(R.id.productBtn);
        btnSize = findViewById(R.id.sizeBtn);
        btnAsk = findViewById(R.id.askBtn);
        btnAllReview = findViewById(R.id.allReviewBtn);
        btnBack = findViewById(R.id.back_btn);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);


        btnBuy.setVisibility(View.VISIBLE);
        btnProduct.setVisibility(View.VISIBLE);
        btnSize.setVisibility(View.VISIBLE);
        btnAsk.setVisibility(View.VISIBLE);
        btnAllReview.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnBag.setVisibility(View.VISIBLE);
        btnHome.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });


        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });



    }
}
