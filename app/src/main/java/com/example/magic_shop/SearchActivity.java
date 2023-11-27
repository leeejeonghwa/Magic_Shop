package com.example.magic_shop;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){


        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Button cancel_btn = findViewById(R.id.cancel_btn2);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 여기에 이전 페이지로 이동하는 코드를 작성합니다.
                // 예를 들어, 현재는 단순히 현재 액티비티를 종료하는 코드를 사용합니다.
                finish();
            }
        });
    }
}
