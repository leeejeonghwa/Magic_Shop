package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button search_btn = (Button) findViewById(R.id.search_btn);
        Button mypage_btn = (Button) findViewById(R.id.mypage_id);
        Button categorySearch = (Button) findViewById(R.id.category_search_id);
        Button topCat_btn = (Button) findViewById(R.id.btn_top_id);
        Button pantsCat_btn = (Button) findViewById(R.id.btn_pants_id);
        Button skirtCat_btn = (Button) findViewById(R.id.btn_skirt_one_piece_id);
        Button outerCat_btn = (Button) findViewById(R.id.btn_outer_id);
        Button bagCat_btn = (Button) findViewById(R.id.btn_bag_id);
        Button shoesCat_btn = (Button) findViewById(R.id.btn_shoes_id);
        Button product_btn1 = (Button) findViewById(R.id.btn_product_id1);
        Button product_btn2 = (Button) findViewById(R.id.btn_product_id2);
        Button product_btn3 = (Button) findViewById(R.id.btn_product_id3);
        Button product_btn4 = (Button) findViewById(R.id.btn_product_id4);


        //검색 버튼
        search_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

        //쇼핑카트 버튼
        Button shoppingcart_btn = (Button) findViewById(R.id.shoppingcart_btn);
        shoppingcart_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Mypage_MainActivity.class); //쇼핑카트로 바꿔주기
                startActivity(intent);
            }
        });

        //마이페이지 버튼
        mypage_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        //카테고리 검색 버튼
        categorySearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

        //상의 카테고리 선택 버튼
        topCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //하의 카테고리 선택 버튼
        pantsCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //스커트 카테고리 선택 버튼
        skirtCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //아우터 카테고리 선택 버튼
        outerCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //가방 카테고리 선택 버튼
        bagCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //신발 카테고리 선택 버튼
        shoesCat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CategoryProductListActivity.class);
                startActivity(intent);
            }
        });

        //상품1
        product_btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });

        //상품 2
        product_btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });

        //상품 3
        product_btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });

        //상품 4
        product_btn4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
