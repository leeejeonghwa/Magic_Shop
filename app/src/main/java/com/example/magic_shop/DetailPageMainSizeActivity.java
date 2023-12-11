package com.example.magic_shop;

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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailPageMainSizeActivity extends AppCompatActivity {

    private Button btnBuy;
    private Button btnReview;
    private Button btnAsk;
    private Button btnProduct;
    private Button btnBack;
    private String productName;
    private String productPrice, productID;
    private String sellerId;
    private ImageView productMainImage;
    private ImageView productSizeImage;
    private ProductDetailedImageLoader productDetailedImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main_size);
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

            productMainImage = findViewById(R.id.mainImage);
            productSizeImage = findViewById(R.id.sizeImage);

            productDetailedImageLoader = new ProductDetailedImageLoader(this);

            loadDetailedImages(this.productName);
        }


        btnBuy = findViewById(R.id.btn_buy);
        btnReview = findViewById(R.id.reviewBtn);
        btnProduct = findViewById(R.id.productBtn);
        btnAsk = findViewById(R.id.askBtn);
        btnBack = findViewById(R.id.back_btn);

        btnBuy.setVisibility(View.VISIBLE);
        btnReview.setVisibility(View.VISIBLE);
        btnProduct.setVisibility(View.VISIBLE);
        btnAsk.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);


        // btnBuy의 클릭 이벤트 처리
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailPageMainOptionSelectActivity.class);
                intent.putExtra("id", productID);
                startActivity(intent);
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailPageMainReviewActivity.class);

                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);

                Log.d("Detailpage_MainReview", "Product Name: " + productName);
                Log.d("Detailpage_MainReview", "Seller ID: " + sellerId);
                Log.d("Detailpage_MainReview", "Product Price: " + productPrice);

                startActivity(intent);
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailPageMainActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);

                Log.d("Detailpage_MainReview", "Product Name: " + productName);
                Log.d("Detailpage_MainReview", "Seller ID: " + sellerId);
                Log.d("Detailpage_MainReview", "Product Price: " + productPrice);

                startActivity(intent);
            }
        });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailPageMainAskActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);

                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailPageMainActivity.class);
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
                    setBase64Image(productMainImage, imagesObject.getString("main_image"));
                    setBase64Image(productSizeImage, imagesObject.getString("size_image"));
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

