package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CategorySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_selection);

        // 새 페이지의 동작을 여기에 추가

        findViewById(R.id.btn1_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼을 클릭했을 때 SecondActivity로 이동하는 Intent 생성
                Intent intent = new Intent(CategorySelectionActivity.this, CategoryProductListActivity.class);
                startActivity(intent); // Intent를 사용하여 SecondActivity 시작
            }
        });
    }


}
