package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Mypage_ReviewWriteActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity_review_write);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_review_list = (Button) findViewById(R.id.btn_review_list);
        btn_review_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage_ReviewListActivity.class);
                startActivity(intent);
            }
        });
    }
}
