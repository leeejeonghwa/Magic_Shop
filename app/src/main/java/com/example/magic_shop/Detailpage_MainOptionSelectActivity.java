package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Detailpage_MainOptionSelectActivity extends AppCompatActivity {
    private Button btnOption;
    private Button btnColor1;
    private Button btnColor2;
    private Button btnColorSelect;
    private Button btnSizeSelect;
    private Button btnSize_S;
    private Button btnSize_M;
    private Button btnSize_L;
    private Button btnBack;
    private Button btnBag;
    private Button btnHome;
    private Button btnSearch;
    private String size_s; // 현재 선택된 사이즈 S
    private String size_m; // 현재 선택된 사이즈 M
    private String size_l; // 현재 선택된 사이즈 L
    private String productName;
    private String sizeS, sizeM, sizeL, color1, color2;
    private ProductGetOptionRequest optionRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_obtion_select);

        optionRequest = new ProductGetOptionRequest(this);

        Intent intent = getIntent();
        if (intent != null) {
            this.productName = intent.getStringExtra("product_name");

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


        btnOption = findViewById(R.id.btn_option);
        btnColor1 = findViewById(R.id.btn_color1);
        btnColor2 = findViewById(R.id.btn_color2);
        btnColorSelect = findViewById(R.id.btn_color_select);
        btnSizeSelect = findViewById(R.id.btn_size_select);
        btnSize_S = findViewById(R.id.btn_size_S);
        btnSize_M = findViewById(R.id.btn_size_M);
        btnSize_L = findViewById(R.id.btn_size_L);
        btnBack = findViewById(R.id.back_btn);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);

        // 초기 상태에서는 btnBuy를 보이고 btnOption을 숨깁니다.
        btnOption.setVisibility(View.VISIBLE);
        btnColorSelect.setVisibility(View.VISIBLE);
        btnSizeSelect.setVisibility(View.VISIBLE);
        btnColor1.setVisibility(View.GONE);
        btnColor2.setVisibility(View.GONE);
        btnSize_S.setVisibility(View.GONE);
        btnSize_M.setVisibility(View.GONE);
        btnSize_L.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        btnBag.setVisibility(View.VISIBLE);
        btnHome.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);



        // btnBuy의 클릭 이벤트 처리
        btnColorSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // btnBuy를 숨기고 btnOption을 보이도록 변경
                btnColor1.setVisibility(View.VISIBLE);
                btnColor2.setVisibility(View.VISIBLE);
                btnSizeSelect.setVisibility(View.GONE);

                loadOptions(productName); // productName이 여기서 초기화되도록 수정
            }
        });

        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // btnBuy를 숨기고 btnOption을 보이도록 변경
                btnColor1.setVisibility(View.GONE);
                btnColor2.setVisibility(View.GONE);
                btnColorSelect.setVisibility(View.VISIBLE);
                btnSizeSelect.setVisibility(View.VISIBLE);

            }
        });

        btnSizeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // btnBuy를 숨기고 btnOption을 보이도록 변경
                btnSize_S.setVisibility(View.VISIBLE);
                btnSize_M.setVisibility(View.VISIBLE);
                btnSize_L.setVisibility(View.VISIBLE);

                loadOptions(productName);
            }
        });

        btnSize_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSize_M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnSize_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
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

    private void loadOptions(String productName) {
        optionRequest.optionload(productName, new ProductGetOptionRequest.OptionResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                // 받아온 옵션을 처리
                try {
                    JSONObject options = response.getJSONObject(0);
                    color1 = options.getString("color_id1");
                    color2 = options.getString("color_id2");
                    size_s = options.getString("size_s");
                    size_m = options.getString("size_m");
                    size_l = options.getString("size_l");

                    // navigateToNextPage 메서드에서 Intent에 데이터 추가
                    navigateToNextPage(color1, color2, size_s, size_m, size_l);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON 파싱 오류 처리
                    Toast.makeText(getApplicationContext(), "옵션 데이터 파싱 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                // 옵션을 로드할 수 없는 경우의 오류 처리
                Toast.makeText(getApplicationContext(), "에러: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToNextPage(String color1, String color2, String size_s, String size_m, String size_l) {
        Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
        // 옵션 정보를 Intent에 추가
        intent.putExtra("color_id1", color1);
        intent.putExtra("color_id2", color2);
        intent.putExtra("size_s", size_s);
        intent.putExtra("size_m", size_m);
        intent.putExtra("size_l", size_l);
        startActivity(intent);
    }
}
