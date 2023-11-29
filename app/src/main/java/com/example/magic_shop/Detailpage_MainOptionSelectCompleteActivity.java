package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Detailpage_MainOptionSelectCompleteActivity extends AppCompatActivity {
    private Button btnAdd, btnMinus, btnOption;
    private TextView Count;
    private int count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_option_select_complete);

        Intent intent = getIntent();
        if (intent != null) {
            String sizeValue = intent.getStringExtra("size");
            String colorValue = intent.getStringExtra("color"); // 추가: color 값을 가져옴

            // sizeValue를 이용하여 필요한 작업 수행
            if (sizeValue != null) {
                // 예시: size 텍스트뷰에 값을 설정
                TextView sizeTextView = findViewById(R.id.size);
                sizeTextView.setText(sizeValue);
            }

            if (colorValue != null) {
                // 예시: size 텍스트뷰에 값을 설정
                TextView sizeTextView = findViewById(R.id.color);
                sizeTextView.setText(colorValue);
            }
        }

        Count = findViewById(R.id.count);
        Count.setText(count + "");
        btnAdd = findViewById(R.id.btn_add);
        btnMinus = findViewById(R.id.btn_minus);
        btnOption = findViewById(R.id.btn_option);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                Count.setText(count + "");
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                Count.setText(count + "");
            }
        });

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainObtionSelectActivity.class);
                startActivity(intent);
            }
        });
    }

}
