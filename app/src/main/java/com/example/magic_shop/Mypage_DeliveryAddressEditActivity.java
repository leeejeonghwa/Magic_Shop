package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Mypage_DeliveryAddressEditActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_delivery_address_edit);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_DeliveryAddressManageActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        TextView address_name = findViewById(R.id.address_name);
        TextView address_phoneNumber = findViewById(R.id.address_phone_number);
        TextView address = findViewById(R.id.address);

        address_name.setText(intent.getStringExtra("name"));
        address_phoneNumber.setText(intent.getStringExtra("phoneNumber"));
        address.setText(intent.getStringExtra("address"));
    }
}