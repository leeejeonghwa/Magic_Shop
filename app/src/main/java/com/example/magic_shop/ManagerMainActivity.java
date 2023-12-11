package com.example.magic_shop;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManagerMainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_main);
        getWindow().setWindowAnimations(0);

        Button btn_seller_list = (Button) findViewById(R.id.btn_seller_list);
        btn_seller_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerSellerListActivity.class);
                startActivity(intent);
            }
        });


        Button btn_product_register_list = (Button) findViewById(R.id.btn_product_register_list);
        btn_product_register_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerProductRegisterListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_product_revise_list = (Button) findViewById(R.id.btn_product_revise_list);
        btn_product_revise_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerProductReviseListActivity.class);
                startActivity(intent);
            }
        });
    }
}
