package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Detailpage_MainOptionSelectCompleteActivity extends AppCompatActivity {
    private Button btnBasket;
    private Button btnBag;
    private Button btnHome;
    private Button btnSearch;
    private Button btnBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_option_select_complete);

        Intent intent = getIntent();
        if (intent != null) {

            String sizeValue = intent.getStringExtra("size");
            String colorValue = intent.getStringExtra("color"); // 추가: color 값을 가져옴
            String option = intent.getStringExtra("option");


            // sizeValue를 이용하여 필요한 작업 수행
            if (sizeValue != null) {
                // 예시: size 텍스트뷰에 값을 설정
                TextView sizeTextView = findViewById(R.id.size);
                sizeTextView.setText(sizeValue);
            }

            if (colorValue != null) {
                // 예시: size 텍스트뷰에 값을 설정
                TextView sizeTextView = findViewById(R.id.color);
                sizeTextView.setText(colorValue);
            }
        }

        btnBasket = findViewById(R.id.btn_basket);
        btnBuy = findViewById(R.id.btn_buy);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);


        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), OrderFormActivity.class); 결제하기 누르면 주문서 페이지로 넘어가게 하는 건데 안넘어간다링...
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
