package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailPageMainOptionSelectActivity extends AppCompatActivity {
    private Button btnColor1;
    private Button btnColor2;
    private Button btnSizeS;
    private Button btnSizeM;
    private Button btnSizeL;

    private String productID;
    private String color;

    private TextView productNameTextView;
    private TextView productPriceTextView;
    private String productName;

    private String brandName;
    private String productPrice;
    private String image;

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


        Button btnOption = findViewById(R.id.btn_option);
        btnColor1 = findViewById(R.id.btn_color1);
        btnColor2 = findViewById(R.id.btn_color2);
        btnSizeS = findViewById(R.id.btn_size_S);
        btnSizeM = findViewById(R.id.btn_size_M);
        btnSizeL = findViewById(R.id.btn_size_L);
//        btnBack = findViewById(R.id.back_btn);
        Button btnHome = findViewById(R.id.home_btn);
        Button btnBag = findViewById(R.id.bag_btn);
        Button btnSearch = findViewById(R.id.search_btn);
//        productNameTextView = findViewById(R.id.text_productName);
//        productPriceTextView = findViewById(R.id.text_productPrice);





        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor2.getText().toString();
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


        btnSizeS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(btnSizeS.getText().toString());



            }
        });

        btnSizeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveToNextActivity(btnSizeM.getText().toString());


            }
        });

        btnSizeL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(btnSizeL.getText().toString());



            }
        });

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailPageMainActivity.class);
                intent.putExtra("seller_id", brandName);
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

        GetProductOptionRequest optionRequest = new GetProductOptionRequest(
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
                                brandName = options.getString("seller_id");
                                productPrice = options.getString("product_price");
                                image = options.getString("main_image");
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
        btnSizeS.setText(sizeS);
        btnSizeM.setText(sizeM);
        btnSizeL.setText(sizeL);

    }

    private void moveToNextActivity(String size) {
        Intent intent = new Intent(getApplicationContext(), DetailPageMainOptionSelectCompleteActivity.class);
        String option = color + " / " + size;
        int productAsIntID = Integer.parseInt(productID);
        intent.putExtra("productID",productAsIntID);
        intent.putExtra("color", color);
        intent.putExtra("size", size);
        intent.putExtra("option", option);
        intent.putExtra("product_name", productName);
        intent.putExtra("brandName", brandName);
        intent.putExtra("product_price", productPrice);
        intent.putExtra("main_image", image);

        Log.d("price", productPrice);

        startActivity(intent);
    }




}
