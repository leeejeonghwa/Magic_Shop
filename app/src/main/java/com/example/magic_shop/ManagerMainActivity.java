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

        Button btnBrandName = (Button) findViewById(R.id.btn_seller_list);
        btnBrandName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerSellerListActivity.class);
                startActivity(intent);
            }
        });


        Button btnProductRegisterList = (Button) findViewById(R.id.btn_product_register_list);
        btnProductRegisterList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerProductRegisterListActivity.class);
                startActivity(intent);
            }
        });

        Button btnProductReviseList = (Button) findViewById(R.id.btn_product_revise_list);
        btnProductReviseList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagerProductReviseListActivity.class);
                startActivity(intent);
            }
        });
    }
}
