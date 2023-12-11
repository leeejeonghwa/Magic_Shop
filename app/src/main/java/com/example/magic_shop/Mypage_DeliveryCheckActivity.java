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

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        Intent intent = getIntent();

        TextView productNameTextView = findViewById(R.id.productName);
        TextView trackingNumberTextView = findViewById(R.id.trackingNumber);

        productNameTextView.setText(intent.getStringExtra("productName"));
        trackingNumberTextView.setText(intent.getStringExtra("trackingNumber"));
    }
}
