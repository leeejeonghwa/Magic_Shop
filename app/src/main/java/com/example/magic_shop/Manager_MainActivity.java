package com.example.magic_shop;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Manager_MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_main);
        getWindow().setWindowAnimations(0);

        Button btn_seller_list = (Button) findViewById(R.id.btn_seller_list);
        btn_seller_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager_SellerListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_seller_register_request_list = (Button) findViewById(R.id.btn_seller_register_request_list);
        btn_seller_register_request_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager_SellerRegisterRequestListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_register_list = (Button) findViewById(R.id.btn_product_register_list);
        btn_product_register_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager_ProductRegisterListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_revise_list = (Button) findViewById(R.id.btn_product_revise_list);
        btn_product_revise_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager_ProductReviseListActivity.class);
                startActivity(intent);
            }
        });
    }
}
