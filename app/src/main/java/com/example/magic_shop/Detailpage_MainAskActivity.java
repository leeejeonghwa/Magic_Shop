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

    private Button btnBuy;
    private Button btnReview;
    private Button btnSize;
    private Button btnProduct;
    private Button btnProductAsk;
    private Button btnOrderAsk;
    private Button btnBack;
    private Button btnBag;
    private Button btnHome;
    private Button btnSearch;
    private String productName;
    private String productPrice;
    private String sellerId;
    private ImageView mainImage;
    private ProductDetailedImageLoader productDetailedImageLoader;
    private static final int REQUEST_CODE_PRODUCT_INQUIRY = 1001;
    private static final int REQUEST_CODE_ORDER_INQUIRY = 1002;

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

        btnBuy = findViewById(R.id.btn_buy);
        btnReview = findViewById(R.id.reviewBtn);
        btnProduct = findViewById(R.id.productBtn);
        btnSize = findViewById(R.id.sizeBtn);
        btnProductAsk = findViewById(R.id.askProductBtn);
        btnOrderAsk = findViewById(R.id.askOrdertBtn);
        btnBack = findViewById(R.id.back_btn);
        btnHome = findViewById(R.id.home_btn);
        btnBag = findViewById(R.id.bag_btn);
        btnSearch = findViewById(R.id.search_btn);

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

                startActivityForResult(intent, REQUEST_CODE_PRODUCT_INQUIRY);

            }
        });

        btnOrderAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Detailpage_OrderInquiryPageActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("seller_id", sellerId);
                intent.putExtra("product_price", productPrice);

                startActivityForResult(intent, REQUEST_CODE_ORDER_INQUIRY);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PRODUCT_INQUIRY) {
                // 상품 문의의 결과 처리
                productName = data.getStringExtra("product_name");
                sellerId = data.getStringExtra("seller_id");
                productPrice = data.getStringExtra("product_price");
            } else if (requestCode == REQUEST_CODE_ORDER_INQUIRY) {
                // 주문 문의의 결과 처리
                productName = data.getStringExtra("product_name");
                sellerId = data.getStringExtra("seller_id");
                productPrice = data.getStringExtra("product_price");
            }

            // 받은 값으로 UI를 업데이트합니다.
            TextView productTextView = findViewById(R.id.productText);
            TextView priceTextView = findViewById(R.id.priceText);
            TextView sellerTextView = findViewById(R.id.brandText);

            productTextView.setText(productName);
            priceTextView.setText(productPrice);
            sellerTextView.setText(sellerId);
        }
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
