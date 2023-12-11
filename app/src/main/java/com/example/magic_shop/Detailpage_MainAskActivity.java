package com.example.magic_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Detailpage_MainAskActivity extends AppCompatActivity {

    private String productName;
    private String productPrice;
    private String sellerId;
    private ImageView mainImage;
    private ProductDetailedImageLoader productDetailedImageLoader;
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_ask);
        getWindow().setWindowAnimations(0);

        Intent intent = getIntent();
        if (intent != null) {
            productName = intent.getStringExtra("product_name");
            sellerId = intent.getStringExtra("seller_id");
            productPrice = intent.getStringExtra("product_price");
            productID = intent.getStringExtra("id");

            // 받아온 상품명을 화면에 표시
            TextView productTextView = findViewById(R.id.productText);
            TextView priceTextView = findViewById(R.id.priceText);
            TextView sellerTextView = findViewById(R.id.brandText);

            productTextView.setText(this.productName);
            priceTextView.setText(this.productPrice);
            sellerTextView.setText(this.sellerId);

            mainImage = findViewById(R.id.mainImage);

            productDetailedImageLoader = new ProductDetailedImageLoader(this);

            loadDetailedImages(this.productName);
        }

        Button btnBuy = findViewById(R.id.btn_buy);
        Button btnReview = findViewById(R.id.reviewBtn);
        Button btnProduct = findViewById(R.id.productBtn);
        Button btnSize = findViewById(R.id.sizeBtn);
        Button btnProductAsk = findViewById(R.id.askProductBtn);
        Button btnOrderAsk = findViewById(R.id.askOrdertBtn);
        Button btnBack = findViewById(R.id.back_btn);
        Button btnHome = findViewById(R.id.home_btn);
        Button btnBag = findViewById(R.id.bag_btn);
        Button btnSearch = findViewById(R.id.search_btn);

        btnBuy.setVisibility(View.VISIBLE);
        btnReview.setVisibility(View.VISIBLE);
        btnProduct.setVisibility(View.VISIBLE);
        btnSize.setVisibility(View.VISIBLE);
        btnProductAsk.setVisibility(View.VISIBLE);
        btnOrderAsk.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnBag.setVisibility(View.VISIBLE);
        btnHome.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);

        // btnBuy의 클릭 이벤트 처리
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainOptionSelectActivity.class);
                intent.putExtra("id", productID);

                startActivity(intent);
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainReviewActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);
                startActivity(intent);
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);
                startActivity(intent);
            }
        });

        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainSizeActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);
                startActivity(intent);
            }
        });

        btnProductAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_ProductInquiryPageActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);

               startActivity(intent);

            }
        });

        btnOrderAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_OrderInquiryPageActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);

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

        //쇼핑카트 버튼
        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingBasketActivity.class);
                startActivity(intent);
            }
        });

        btnProductAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_ProductInquiryPageActivity.class);
                startActivity(intent);
            }
        });

        btnOrderAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_OrderInquiryPageActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadDetailedImages(String productName) {
        productDetailedImageLoader.loadDetailedImages(productName, new ProductDetailedImageLoader.DetailedImageResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    // 이미지를 디코딩하고 화면에 표시
                    JSONObject imagesObject = response.getJSONObject(0);
                    setBase64Image(mainImage, imagesObject.getString("main_image"));
                    // 추가적인 이미지가 있다면 계속해서 설정해주면 됩니다.
                } catch (JSONException e) {
                    e.printStackTrace();
                    // 에러 처리
                    Toast.makeText(getApplicationContext(), "Failed to parse image data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                // 에러 처리
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
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
