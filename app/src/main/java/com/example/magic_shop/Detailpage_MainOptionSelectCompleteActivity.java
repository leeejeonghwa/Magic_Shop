package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Detailpage_MainOptionSelectCompleteActivity extends AppCompatActivity {
    private Button btnBasket;
    private Button btnBag;
    private Button btnHome;
    private Button btnSearch;
    private Button btnBuy;
    private String productPrice, productName ,brandName ,option;
    private String productImage;

    private SessionManager sessionManager;

    private int productID;
    private TextView textProductName, textProductPrice, textBrandName;
    private ImageView main;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_option_select_complete);

        Intent intent = getIntent();

        if (intent != null) {

            String sizeValue = intent.getStringExtra("size");
            String colorValue = intent.getStringExtra("color"); // 추가: color 값을 가져옴
            productPrice = intent.getStringExtra("product_price");
            productID = intent.getIntExtra("productID", 0);
            brandName = intent.getStringExtra("brandName");
            option = intent.getStringExtra("option");
            productName = intent.getStringExtra("product_name");
            productImage = intent.getStringExtra("main_image");

            textProductName = findViewById(R.id.text_productName);
            textBrandName = findViewById(R.id.text_brandName);
            textProductPrice = findViewById(R.id.text_productPrice);
            main = findViewById(R.id.mainImage);

            textBrandName.setText(brandName);
            textProductName.setText(productName);
            textProductPrice.setText(productPrice);
            setBase64Image(main, productImage);


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

        btnBasket = findViewById(R.id.btn_basket);
        btnBuy = findViewById(R.id.btn_buy);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);


        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShoppingBasketActivity.class);

                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sessionManager = new SessionManager(getApplicationContext());

                if (sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), OrderFormActivity.class);

                    ArrayList<String> selectedBrands = new ArrayList<>();
                    ArrayList<String> selectedProductNames = new ArrayList<>();
                    ArrayList<String> options = new ArrayList<>();  // 변경: Options -> options
                    ArrayList<String> selectedPrice = new ArrayList<>();
                    ArrayList<Integer> selectedProductID = new ArrayList<>();
                    ArrayList<String> selectedProductImage = new ArrayList<>();

                    selectedBrands.add(brandName);
                    selectedProductNames.add(productName);
                    options.add(option);  // 변경: Options -> options
                    selectedPrice.add(productPrice);
                    selectedProductID.add(productID);
                    selectedProductImage.add(productImage);
                    int int_product_price = Integer.parseInt(productPrice);

                    intent.putExtra("TOTAL_ITEM_COUNT", 1);
                    intent.putExtra("TOTAL_PRICE", int_product_price);
                    intent.putStringArrayListExtra("SELECTED_BRANDS", selectedBrands);
                    intent.putStringArrayListExtra("SELECTED_PRODUCT_NAMES", selectedProductNames);
                    intent.putStringArrayListExtra("SELECTED_OPTIONS", options);
                    intent.putStringArrayListExtra("SELECTED_PRICE", selectedPrice);
                    intent.putIntegerArrayListExtra("SELECTED_PRODUCT_ID", selectedProductID);
                    intent.putStringArrayListExtra("SELECTED_PRODUCT_IMAGE", selectedProductImage);

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager = new SessionManager(getApplicationContext());
                String userID = sessionManager.getUserId();

                Intent intent = getIntent();
                int productID = intent.getIntExtra("productID", 0);
                String productOption =intent.getStringExtra("option");

                // Make a request to add the item to the basket
                AddToBasketRequest addToBasketRequest = new AddToBasketRequest(userID, productID, productOption,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("Detail", "장바구니 추가 응답: " + response);

                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {

                                        Toast.makeText(Detailpage_MainOptionSelectCompleteActivity.this, "장바구니에 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Detailpage_MainOptionSelectCompleteActivity.this, ShoppingBasketActivity.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(Detailpage_MainOptionSelectCompleteActivity.this, "장바구니에 저장 실패: 서버 응답에 success가 false입니다.", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Detailpage_MainOptionSelectCompleteActivity.this, "장바구니에 저장 실패: 서버 응답을 파싱하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors if needed
                                // 에러 처리 예: 토스트 메시지로 사용자에게 알리기
                                Toast.makeText(Detailpage_MainOptionSelectCompleteActivity.this, "장바구니에 추가 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                // Volley RequestQueue에 요청 추가
                RequestQueue queue = Volley.newRequestQueue(Detailpage_MainOptionSelectCompleteActivity.this);
                queue.add(addToBasketRequest);
            }
        });

    }

    private void setBase64Image(ImageView imageView, String base64Image) {
        // Base64로 인코딩된 이미지를 디코딩하여 ImageView에 설정
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }


}
