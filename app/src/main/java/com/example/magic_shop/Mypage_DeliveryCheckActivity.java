package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Mypage_DeliveryCheckActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_delivery_check);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_OrderDeliveryListActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        TextView product_name = findViewById(R.id.productName);
        TextView trackingNumber = findViewById(R.id.trackingNumber);

        product_name.setText(intent.getStringExtra("productName"));
        trackingNumber.setText(intent.getStringExtra("trackingNumber"));
    }
}
