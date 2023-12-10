package com.example.magic_shop;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){


        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Button cancel_btn = findViewById(R.id.cancel_btn2);

        Button search_btn = (Button) findViewById(R.id.search_btn);
        EditText query = (EditText) findViewById(R.id.edit_query);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = query.getText().toString();

                if(!searchTerm.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                    // Intent에 searchTerm을 추가합니다.
                    intent.putExtra("searchTerm", searchTerm);

                    startActivity(intent);
                } else {
                    Toast.makeText(SearchActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }



}
