package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Mypage_OrderDeliveryListActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_order_delivery_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_delivery_check = (Button) findViewById(R.id.btn_delivery_check);
        btn_delivery_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_DeliveryCheckActivity.class);
                startActivity(intent);
            }
        });
    }

}