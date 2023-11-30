package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Detailpage_ProductIquiryPageActivity extends AppCompatActivity {
    private Button btnBack;
    private Button btnEnroll;
    private Button btnCancellation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_inquiry_page);

        btnBack = findViewById(R.id.back_btn);
        btnEnroll = findViewById(R.id.btn_enroll);
        btnCancellation = findViewById(R.id.btn_Cancellation);

        btnBack.setVisibility(View.VISIBLE);
        btnEnroll.setVisibility(View.VISIBLE);
        btnCancellation.setVisibility(View.VISIBLE);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainAskActivity.class);
                startActivity(intent);
            }
        });

        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainAskActivity.class);
                startActivity(intent);
            }
        });

        btnCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainAskActivity.class);
                startActivity(intent);
            }
        });

    }

}
