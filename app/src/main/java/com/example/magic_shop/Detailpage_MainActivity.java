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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.magic_shop.ProductDetailedImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Detailpage_MainActivity extends AppCompatActivity {

    private Button btnBuy;
    private Button btnReview;
    private Button btnSize;
    private Button btnAsk;
    private Button btnHome;
    private Button btnSearch;
    private Button btnBag;
    private Button btnBack;
    private ImageView mainImage;
    private ImageView detailedImage1;
    private ImageView detailedImage2;
    private ImageView detailedImage3;
    private String productName;
    private String productPrice;
    private String sellerId;
    private String productID;
    private ProductDetailedImageLoader productDetailedImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage_activity_main);
        getWindow().setWindowAnimations(0);


        Intent intent = getIntent();
        if (intent != null) {
            this.productName = intent.getStringExtra("product_name");
            this.productPrice = intent.getStringExtra("product_price");
            this.sellerId = intent.getStringExtra("seller_id");
            this.productID=intent.getStringExtra("id");


            // 받아온 상품명을 화면에 표시
            TextView productTextView = findViewById(R.id.productText);
            TextView priceTextView = findViewById(R.id.priceText);
            TextView sellerTextView = findViewById(R.id.brandText);

            productTextView.setText(this.productName);
            priceTextView.setText(this.productPrice);
            sellerTextView.setText(this.sellerId);

            mainImage = findViewById(R.id.mainImage);
            detailedImage1 = findViewById(R.id.detailedImage1);
            detailedImage2 = findViewById(R.id.detailedImage2);
            detailedImage3 = findViewById(R.id.detailedImage3);

            productDetailedImageLoader = new ProductDetailedImageLoader(this);

            // 디테일 페이지에서 상세 이미지를 가져오고 화면에 표시
            loadDetailedImages(this.productName);
        }


        btnBuy = findViewById(R.id.btn_buy);btnReview = findViewById(R.id.reviewBtn);
        btnSize = findViewById(R.id.sizeBtn);
        btnAsk = findViewById(R.id.askBtn);
        btnHome = findViewById(R.id.home_btn);
        btnSearch = findViewById(R.id.search_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnBack = findViewById(R.id.back_btn);

        btnBuy.setVisibility(View.VISIBLE);
        btnReview.setVisibility(View.VISIBLE);
        btnSize.setVisibility(View.VISIBLE);
        btnAsk.setVisibility(View.VISIBLE);
        btnHome.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
        btnBag.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);



        // btnBuy의 클릭 이벤트 처리
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainOptionSelectActivity.class);
                intent.putExtra("id", productID);
                intent.putExtra("seller_id",sellerId);

                startActivity(intent);
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detailpage_MainReviewActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("product_price", productPrice);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("id",productID);

                startActivity(intent);
            }
        });


        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainSizeActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("product_price", productPrice);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("id",productID);

                startActivity(intent);
            }
        });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_MainAskActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("product_price", productPrice);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("id",productID);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    }
    private void loadDetailedImages(String productName) {
        productDetailedImageLoader.loadDetailedImages(productName, new ProductDetailedImageLoader.DetailedImageResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    // 이미지를 디코딩하고 화면에 표시
                    JSONObject imagesObject = response.getJSONObject(0);
                    setBase64Image(mainImage, imagesObject.getString("main_image"));
                    setBase64Image(detailedImage1, imagesObject.getString("detailed_image1"));
                    setBase64Image(detailedImage2, imagesObject.getString("detailed_image2"));
                    setBase64Image(detailedImage3, imagesObject.getString("detailed_image3"));
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