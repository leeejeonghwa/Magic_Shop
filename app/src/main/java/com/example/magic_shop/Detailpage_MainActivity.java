package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Detailpage_MainActivity extends AppCompatActivity {

    private Button btnBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main);

        btnBuy = findViewById(R.id.btn_buy);



        // 초기 상태에서는 btnBuy를 보이고 btnOption을 숨깁니다.
        btnBuy.setVisibility(View.VISIBLE);


        // btnBuy의 클릭 이벤트 처리
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainObtionSelectActivity.class);
                startActivity(intent);
            }
        });

    }

}
