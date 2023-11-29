package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ShoppingBasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_basket);

        findViewById(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingBasketActivity.this, OrderFormActivity.class);
                startActivity(intent); // Intent를 사용하여 SecondActivity 시작
            }
        });
    }




}
