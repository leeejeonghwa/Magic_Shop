package com.example.magic_shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private Button productBtn1;
    private Button productBtn2;
    private Button productBtn3;
    private Button productBtn4;
    private TextView productName1;
    private TextView productName2;
    private TextView productName3;
    private TextView productName4;
    private TextView productBrand1;
    private TextView productBrand2;
    private TextView productBrand3;
    private TextView productBrand4;
    private TextView productPrice1;
    private TextView productPrice2;
    private TextView productPrice3;
    private TextView productPrice4;
    private Button search_btn;
    private Button shoppingcart_btn;
    private Button mypage_btn;
    private Button categorySearch;

    private String productID;
    private String productID1;
    private String productID2;
    private String productID3;
    private String productID4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setWindowAnimations(0);

        search_btn = findViewById(R.id.search_btn);
        shoppingcart_btn = findViewById(R.id.shoppingcart_btn);
        mypage_btn = findViewById(R.id.mypage_id);
        categorySearch = findViewById(R.id.category_search_id);
        productBtn1 = findViewById(R.id.btn_product_id1);
        productBtn2 = findViewById(R.id.btn_product_id2);
        productBtn3 = findViewById(R.id.btn_product_id3);
        productBtn4 = findViewById(R.id.btn_product_id4);
        productName1 = findViewById(R.id.product_name_text_id1);
        productName2 = findViewById(R.id.product_name_text_id2);
        productName3 = findViewById(R.id.product_name_text_id3);
        productName4 = findViewById(R.id.product_name_text_id4);
        productBrand1 = findViewById(R.id.product_brand_text_id1);
        productBrand2 = findViewById(R.id.product_brand_text_id2);
        productBrand3 = findViewById(R.id.product_brand_text_id3);
        productBrand4 = findViewById(R.id.product_brand_text_id4);
        productPrice1 = findViewById(R.id.product_price_text_id1);
        productPrice2 = findViewById(R.id.product_price_text_id2);
        productPrice3 = findViewById(R.id.product_price_text_id3);
        productPrice4 = findViewById(R.id.product_price_text_id4);

        // ProductsLoader를 이용해 데이터를 불러오고, 버튼에 적용
        ProductsLoader productsLoader = new ProductsLoader(getApplicationContext());
        productsLoader.loadProducts(new ProductsLoader.ResponseListener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    for (int i = 0; i < Math.min(response.length(), 4); i++) {
                        JSONObject product = response.getJSONObject(i);

                        String productName = product.getString("product_name");
                        String productBrand = product.getString("seller_id");
                        String productPrice = product.getString("product_price");
                        String base64MainImage = product.getString("main_image");
                        Integer productId = product.getInt("id");
                        String productIdString = String.valueOf(productId);
                        Log.d("ID", productIdString);

                        // 버튼에 상품명 설정
                        switch (i) {
                            case 0:
                                productID1 = productIdString;
                                productName1.setText(productName);
                                productBrand1.setText(productBrand);
                                productPrice1.setText(productPrice);
                                setBase64Image(productBtn1, base64MainImage);
                                break;
                            case 1:
                                productID2 = productIdString;
                                productName2.setText(productName);
                                productBrand2.setText(productBrand);
                                productPrice2.setText(productPrice);
                                setBase64Image(productBtn2, base64MainImage);
                                break;
                            case 2:
                                productID3 = productIdString;
                                productName3.setText(productName);
                                productBrand3.setText(productBrand);
                                productPrice3.setText(productPrice);
                                setBase64Image(productBtn3, base64MainImage);
                                break;
                            case 3:
                                productID4 = productIdString;
                                productName4.setText(productName);
                                productBrand4.setText(productBrand);
                                productPrice4.setText(productPrice);
                                setBase64Image(productBtn4, base64MainImage);
                                break;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                // 데이터 불러오기에 실패했을 때의 처리
                Toast.makeText(HomeActivity.this, "Error loading products: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        setButtonListeners();
    }

    private void setButtonListeners() {
        // 각 버튼에 대한 클릭 리스너 설정
        productBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상품 정보를 이용하여 세부 이미지를 불러오는 메서드 호출
                startActivityForProduct(
                        productName1.getText().toString(),
                        productBrand1.getText().toString(),
                        productPrice1.getText().toString(),
                        productID1

                );
            }
        });

        productBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForProduct(
                    productName2.getText().toString(),
                    productBrand2.getText().toString(),
                    productPrice2.getText().toString(),
                    productID2
                );
            }
        });

        productBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForProduct(
                        productName3.getText().toString(),
                        productBrand3.getText().toString(),
                        productPrice3.getText().toString(),
                        productID3
                );
            }
        });

        productBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForProduct(
                        productName4.getText().toString(),
                        productBrand4.getText().toString(),
                        productPrice4.getText().toString(),
                        productID3
                );
            }
        });

        //검색 버튼
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //쇼핑카트 버튼
        shoppingcart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        //마이페이지 버튼
        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                if (sessionManager.isLoggedIn()) {
                    // 이미 로그인된 경우 마이페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), Mypage_MainActivity.class);
                    startActivity(intent);
                } else {
                    // 로그인되지 않은 경우 로그인 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //카테고리 검색 버튼
        categorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startActivityForProduct(String productName, String productBrand, String productPrice,String productID) {
        Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
        // 상품 정보 및 이미지를 Intent에 추가하여 Detail 페이지로 전달
        intent.putExtra("product_name", productName);
        intent.putExtra("seller_id", productBrand);
        intent.putExtra("product_price", productPrice);
        intent.putExtra("id", productID);
        Log.d("IntentID", productID);
        startActivity(intent);
    }

    private void setBase64Image(Button button, String base64Image) {
        // Base64로 인코딩된 이미지를 디코딩하여 버튼의 배경에 설정
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        button.setBackground(new BitmapDrawable(getResources(), decodedByte));

    }


}
