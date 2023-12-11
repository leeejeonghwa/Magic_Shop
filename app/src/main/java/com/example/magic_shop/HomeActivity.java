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

    private Button btnProduct1;
    private Button btnProduct2;
    private Button btnProduct3;
    private Button btnProduct4;
    private TextView productName1TextView;
    private TextView productName2TextView;
    private TextView productName3TextView;
    private TextView productName4TextView;
    private TextView productBrand1TextView;
    private TextView productBrand2TextView;
    private TextView productBrand3TextView;
    private TextView productBrand4TextView;
    private TextView productPrice1TextView;
    private TextView productPrice2TextView;
    private TextView productPrice3TextView;
    private TextView productPrice4TextView;
    private Button btnSearch;
    private Button btnShoppingCart;
    private Button btnMyPage;
    private Button btnCategorySearch;

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

        btnSearch = findViewById(R.id.search_btn);
        btnShoppingCart = findViewById(R.id.shoppingcart_btn);
        btnMyPage = findViewById(R.id.mypage_id);
        btnCategorySearch = findViewById(R.id.category_search_id);
        btnProduct1 = findViewById(R.id.btn_product_id1);
        btnProduct2 = findViewById(R.id.btn_product_id2);
        btnProduct3 = findViewById(R.id.btn_product_id3);
        btnProduct4 = findViewById(R.id.btn_product_id4);
        productName1TextView = findViewById(R.id.product_name_text_id1);
        productName2TextView = findViewById(R.id.product_name_text_id2);
        productName3TextView = findViewById(R.id.product_name_text_id3);
        productName4TextView = findViewById(R.id.product_name_text_id4);
        productBrand1TextView = findViewById(R.id.product_brand_text_id1);
        productBrand2TextView = findViewById(R.id.product_brand_text_id2);
        productBrand3TextView = findViewById(R.id.product_brand_text_id3);
        productBrand4TextView = findViewById(R.id.product_brand_text_id4);
        productPrice1TextView = findViewById(R.id.product_price_text_id1);
        productPrice2TextView = findViewById(R.id.product_price_text_id2);
        productPrice3TextView = findViewById(R.id.product_price_text_id3);
        productPrice4TextView = findViewById(R.id.product_price_text_id4);

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
                                productName1TextView.setText(productName);
                                productBrand1TextView.setText(productBrand);
                                productPrice1TextView.setText(productPrice);
                                setBase64Image(btnProduct1, base64MainImage);
                                break;
                            case 1:
                                productID2 = productIdString;
                                productName2TextView.setText(productName);
                                productBrand2TextView.setText(productBrand);
                                productPrice2TextView.setText(productPrice);
                                setBase64Image(btnProduct2, base64MainImage);
                                break;
                            case 2:
                                productID3 = productIdString;
                                productName3TextView.setText(productName);
                                productBrand3TextView.setText(productBrand);
                                productPrice3TextView.setText(productPrice);
                                setBase64Image(btnProduct3, base64MainImage);
                                break;
                            case 3:
                                productID4 = productIdString;
                                productName4TextView.setText(productName);
                                productBrand4TextView.setText(productBrand);
                                productPrice4TextView.setText(productPrice);
                                setBase64Image(btnProduct4, base64MainImage);
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
        btnProduct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상품 정보를 이용하여 세부 이미지를 불러오는 메서드 호출
                startActivityForProduct(
                        productName1TextView.getText().toString(),
                        productBrand1TextView.getText().toString(),
                        productPrice1TextView.getText().toString(),
                        productID1

                );
            }
        });

        btnProduct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForProduct(
                    productName2TextView.getText().toString(),
                    productBrand2TextView.getText().toString(),
                    productPrice2TextView.getText().toString(),
                    productID2
                );
            }
        });

        btnProduct3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForProduct(
                        productName3TextView.getText().toString(),
                        productBrand3TextView.getText().toString(),
                        productPrice3TextView.getText().toString(),
                        productID3
                );
            }
        });

        btnProduct4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForProduct(
                        productName4TextView.getText().toString(),
                        productBrand4TextView.getText().toString(),
                        productPrice4TextView.getText().toString(),
                        productID3
                );
            }
        });

        //검색 버튼
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //쇼핑카트 버튼
        btnShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        //마이페이지 버튼
        btnMyPage.setOnClickListener(new View.OnClickListener() {
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
        btnCategorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startActivityForProduct(String productName, String brandName, String productPrice,String productID) {
        Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
        // 상품 정보 및 이미지를 Intent에 추가하여 Detail 페이지로 전달
        intent.putExtra("product_name", productName);
        intent.putExtra("seller_id", brandName);
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
