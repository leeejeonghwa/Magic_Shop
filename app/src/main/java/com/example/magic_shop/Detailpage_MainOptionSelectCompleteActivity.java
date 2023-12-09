package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Detailpage_MainOptionSelectCompleteActivity extends AppCompatActivity {
    private Button btnAdd, btnMinus, btnOption;
    private Button btnBag;
    private Button btnHome;
    private Button btnSearch;
    private TextView Count;
    private int count = 1;
    private String sizeS, sizeM, sizeL, color1, color2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_option_select_complete);

        Intent intent = getIntent();
        if (intent != null) {
            this.sizeS = intent.getStringExtra("size_s");
            this.sizeM = intent.getStringExtra("size_m");
            this.sizeL = intent.getStringExtra("size_l");
            this.color1 = intent.getStringExtra("color_id1");
            this.color2 = intent.getStringExtra("color_id2"); // 추가: color 값을 가져옴

            Button sSize = findViewById(R.id.btn_size_S);
            Button mSize = findViewById(R.id.btn_size_M);
            Button lSize = findViewById(R.id.btn_size_L);
            Button colorID1 = findViewById(R.id.btn_color1);
            Button colorID2 = findViewById(R.id.btn_color2);

            sSize.setText(this.sizeS);
            mSize.setText(this.sizeM);
            lSize.setText(this.sizeL);
            colorID1.setText(this.color1);
            colorID2.setText(this.color2);

        }

        Count = findViewById(R.id.count);
        Count.setText(count + "");
        btnAdd = findViewById(R.id.btn_add);
        btnMinus = findViewById(R.id.btn_minus);
        btnOption = findViewById(R.id.btn_option);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);

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
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainOptionSelectActivity.class);
                startActivity(intent);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });


        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });
    }

}
