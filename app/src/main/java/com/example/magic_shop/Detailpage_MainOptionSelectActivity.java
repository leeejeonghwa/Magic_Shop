package com.example.magic_shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Detailpage_MainOptionSelectActivity extends AppCompatActivity {
    private Button btnOption;
    private Button btnColor1;
    private Button btnColor2;
    private Button btnSize_S;
    private Button btnSize_M;
    private Button btnSize_L;
    private Button btnBag;
    private Button btnHome;
    private Button btnSearch;
    private String productID;
    private String color;
    private String productName;
    private String productPrice;
    private String image, sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_obtion_select);


        Intent intent = getIntent();
        if (intent != null) {
            this.productID = intent.getStringExtra("id");
            Log.d("option productID", productID);


        }

        loadOptions(productID);


        btnOption = findViewById(R.id.btn_option);
        btnColor1 = findViewById(R.id.btn_color1);
        btnColor2 = findViewById(R.id.btn_color2);
        btnSize_S = findViewById(R.id.btn_size_S);
        btnSize_M = findViewById(R.id.btn_size_M);
        btnSize_L = findViewById(R.id.btn_size_L);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);

        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor1.getText().toString();
                Log.d("ColorClick", "Color selected: " + color);
            }
        });


        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor1.getText().toString();
                Log.d("ColorClick", "Color selected: " + color);
            }
        });


        btnSize_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(btnSize_S.getText().toString());
            }
        });

        btnSize_M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveToNextActivity(btnSize_M.getText().toString());


            }
        });

        btnSize_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(btnSize_L.getText().toString());

            }
        });

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_name",productName);
                intent.putExtra("product_price",productPrice);
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
    private void loadOptions(String productID) {
        Log.d("OptionsRequest", "Sending request for product ID: " + productID);

        ProductGetOptionRequest optionRequest = new ProductGetOptionRequest(
                productID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("OptionsResponse", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                JSONObject options = jsonArray.getJSONObject(0);
                                // 여기에서 옵션 정보를 가져와서 버튼에 설정하는 작업을 수행하세요.
                                String color1 = options.getString("color_id1");
                                String color2 = options.getString("color_id2");
                                String sizeS = options.getString("size_s");
                                String sizeM = options.getString("size_m");
                                String sizeL = options.getString("size_l");
                                productName = options.getString("product_name");
                                productPrice = options.getString("product_price");
                                image = options.getString("main_image");
                                sellerId = options.getString("seller_id");
                                Log.d("option", color1);

                                // 가져온 정보를 버튼에 설정
                                setOptionsOnButtons(color1, color2, sizeS, sizeM, sizeL);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 파싱 오류 처리
                            Toast.makeText(getApplicationContext(), "Failed to parse options data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 처리
                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // 요청을 큐에 추가
        Volley.newRequestQueue(getApplicationContext()).add(optionRequest);
    }

    // 버튼에 옵션 정보 설정
    private void setOptionsOnButtons(String color1, String color2, String sizeS, String sizeM, String sizeL) {
        btnColor1.setText(color1);
        btnColor2.setText(color2);
        btnSize_S.setText(sizeS);
        btnSize_M.setText(sizeM);
        btnSize_L.setText(sizeL);

    }

    private void moveToNextActivity(String size) {
        Intent intent = new Intent(getApplicationContext(), Detailpage_MainOptionSelectCompleteActivity.class);
        String option = color + " / " + size;
        intent.putExtra("productID", productID);
        intent.putExtra("color", color);
        intent.putExtra("size", size);
        intent.putExtra("option", option);
        intent.putExtra("product_name", productName);
        intent.putExtra("product_price", productPrice);
        intent.putExtra("main_image", image);
        intent.putExtra("seller_id",sellerId);
        Log.d("price", productPrice);

        startActivity(intent);
    }
}
