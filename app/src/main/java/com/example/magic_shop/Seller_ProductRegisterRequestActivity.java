package com.example.magic_shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

public class Seller_ProductRegisterRequestActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private Product product;

    private Button main_image_btn;

    private Button[] detail_image_btn;

    private Button size_image_btn;

    private Button btn_back;

    private Button selectedImageBtn;

    private EditText productNameEditText;

    private EditText productPriceEditText;

    private EditText colorOption1EditText;

    private EditText colorOption2EditText;

    private CheckBox[] sizeCheckBox;

    private String userID;

    private Button submitBtn;

    private Response.ErrorListener productErrorListener;






    private int [] superCatRadio = {
            R.id.radio_top,
            R.id.radio_outer,
            R.id.radio_pants,
            R.id.radio_skirt,
            R.id.radio_shoes,
            R.id.radio_bag
    };

    private int [] subCatRadio = {
            R.id.radioButton1,
            R.id.radioButton2,
            R.id.radioButton3,
            R.id.radioButton4,
            R.id.radioButton5,
            R.id.radioButton6,
            R.id.radioButton7,
            R.id.radioButton8,
            R.id.radioButton9,
            R.id.radioButton10
    };

    private int subCatRadio_name[][] = {
            {R.string.category_top_3,R.string.category_top_4, R.string.category_top_5, R.string.category_top_6, R.string.category_top_7, R.string.category_top_8, R.string.category_top_9, R.string.category_top_10, R.string.category_top_11, R.string.category_top_12},
            {R.string.category_outer_3, R.string.category_outer_4, R.string.category_outer_5, R.string.category_outer_6, R.string.category_outer_7, R.string.category_outer_8, R.string.category_outer_9, R.string.category_outer_10, R.string.category_outer_11, R.string.category_outer_12},
            { R.string.category_pants_3, R.string.category_pants_4, R.string.category_pants_5, R.string.category_pants_6, R.string.category_pants_7, R.string.category_pants_8, R.string.category_pants_9, R.string.category_pants_10, R.string.category_pants_11, R.string.category_pants_12},
            { R.string.category_skirt_one_piece_3, R.string.category_skirt_one_piece_4, R.string.category_skirt_one_piece_5, R.string.category_skirt_one_piece_6, R.string.category_skirt_one_piece_9, R.string.category_skirt_one_piece_10, R.string.category_skirt_one_piece_11, R.string.category_skirt_one_piece_12,  },
            {R.string.category_shoes_3, R.string.category_shoes_4, R.string.category_shoes_5, R.string.category_shoes_6, R.string.category_shoes_7, R.string.category_shoes_8, R.string.category_shoes_9, R.string.category_shoes_10, R.string.category_shoes_11, R.string.category_shoes_12},
            { R.string.category_bag_3, R.string.category_bag_4, R.string.category_bag_5, R.string.category_bag_6, R.string.category_bag_7, R.string.category_bag_8, R.string.category_bag_9, R.string.category_bag_10, R.string.category_bag_11, R.string.category_bag_12}
    };

    public void init() {

        product = new Product();
        main_image_btn = findViewById(R.id.btn_productimg_find);
        detail_image_btn = new Button[3];
        detail_image_btn[0] = findViewById(R.id.btn_detail1_find);
        detail_image_btn[1] = findViewById(R.id.btn_detail2_find);
        detail_image_btn[2] = findViewById(R.id.btn_detail3_find);
        size_image_btn = findViewById(R.id.btn_sizeinfo_find);
        btn_back = findViewById(R.id.btn_back);
        productNameEditText = findViewById(R.id.productName);
        productPriceEditText = findViewById(R.id.productPrice);
        submitBtn = findViewById(R.id.btn_submit);
        colorOption1EditText = findViewById(R.id.color1);
        colorOption2EditText = findViewById(R.id.color2);

        sizeCheckBox = new CheckBox[3];
        sizeCheckBox[0] = findViewById(R.id.size_s);
        sizeCheckBox[1] = findViewById(R.id.size_m);
        sizeCheckBox[2] = findViewById(R.id.size_l);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_activity_product_register_request);
        getWindow().setWindowAnimations(0);

        // SessionManager를 통해 로그인 상태 확인
        init();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        userID = sessionManager.getUserId();
        product.setSellerId(userID);


        main_image_btn.setOnClickListener(new productImageOnClickListener());
        detail_image_btn[0].setOnClickListener(new productImageOnClickListener());
        detail_image_btn[1].setOnClickListener(new productImageOnClickListener());
        detail_image_btn[2].setOnClickListener(new productImageOnClickListener());
        size_image_btn.setOnClickListener(new productImageOnClickListener());

        submitBtn.setOnClickListener(new RegisterCompleteOnClickListener());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        for (int i : superCatRadio) {
            findViewById(i).setOnClickListener(new superCatRadioClickListener());
        }

        for (int i: subCatRadio) {
            findViewById(i).setOnClickListener(new subCatRadioClickListener());
        }

        productErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 응답을 처리하는 코드 추가
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    // Timeout 또는 연결 오류 처리
                    Log.e("ServerError", "Timeout or No Connection Error");
                } else if (error instanceof AuthFailureError) {
                    // 인증 오류 처리
                    Log.e("ServerError", "Authentication Failure");
                } else if (error instanceof ServerError) {
                    // 서버 오류 처리
                    Log.e("ServerError", "Server Error");
                } else if (error instanceof NetworkError) {
                    // 네트워크 오류 처리
                    Log.e("ServerError", "Network Error");
                } else if (error instanceof ParseError) {
                    // 파싱 오류 처리
                    Log.e("ServerError", "Parse Error");
                }
                showAlert("서버 응답 중 오류가 발생했습니다.");
            }
        };

        sizeCheckBox[0].setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked: 체크박스의 현재 상태 (체크되었으면 true, 그렇지 않으면 false)

            // 여기에 체크박스 상태가 변경될 때 수행할 작업을 추가
            if (isChecked) {
                product.setSizeS("Y");
                Log.d("sizeS", product.getSizeS());
            } else {
                product.setSizeS("N");
                Log.d("sizeS", product.getSizeS());
            }
        });

        sizeCheckBox[1].setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked: 체크박스의 현재 상태 (체크되었으면 true, 그렇지 않으면 false)

            // 여기에 체크박스 상태가 변경될 때 수행할 작업을 추가
            if (isChecked) {
                product.setSizeM("Y");
                Log.d("sizeM", product.getSizeM());
            } else {
                product.setSizeM("N");
                Log.d("sizeM", product.getSizeM());
            }
        });

        sizeCheckBox[2].setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked: 체크박스의 현재 상태 (체크되었으면 true, 그렇지 않으면 false)

            // 여기에 체크박스 상태가 변경될 때 수행할 작업을 추가
            if (isChecked) {
                product.setSizeL("Y");
                Log.d("sizeL", product.getSizeL());
            } else {
                product.setSizeL("N");
                Log.d("sizeL", product.getSizeL());
            }
        });


    }



    private class productImageOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            openGallery();
            selectedImageBtn = findViewById(v.getId());
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // 이미지를 선택한 경우
            Uri selectedImageUri = data.getData();
            //product.setImageUrl(selectedImageUri.toString()); // 이미지 경로를 Product 객체에 저장

            // 이미지뷰에 선택한 이미지 표시
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                ImageView imageView = null;

                if (selectedImageBtn == main_image_btn) {
                    imageView = findViewById(R.id.productImage);
                    product.setMainImage(encodeImageToBase64(bitmap));
                    Log.d("이미지 저장", product.getMainImage());

                }
                else if (selectedImageBtn == detail_image_btn[0]){
                    imageView = findViewById(R.id.image_detail1);
                    product.setDetailedImage1(encodeImageToBase64(bitmap));
                    Log.d("이미지 저장", product.getDetailedImage1());
                }
                else if (selectedImageBtn == detail_image_btn[1]){
                    imageView = findViewById(R.id.img_detail2);
                    product.setDetailedImage2(encodeImageToBase64(bitmap));
                    Log.d("이미지 저장", product.getDetailedImage2());
                }
                else if (selectedImageBtn == detail_image_btn[2]) {
                    imageView = findViewById(R.id.img_detail3);
                    product.setDetailedImage3(encodeImageToBase64(bitmap));
                    Log.d("이미지 저장", product.getDetailedImage3());
                }
                else if (selectedImageBtn == size_image_btn) {
                    imageView = findViewById(R.id.img_sizeInfo);
                    product.setSizeImage(encodeImageToBase64(bitmap));
                    Log.d("이미지 저장", product.getSizeImage());
                }
                if (imageView != null) imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }







    private class superCatRadioClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int clickedId = v.getId();
            onCheckedChange(clickedId);

            if (clickedId == superCatRadio[3]){
                //스커트 하위 카테고리 나타나기
                RadioButton button9 = findViewById(R.id.radioButton9);
                button9.setVisibility(View.INVISIBLE);
                RadioButton button10 = findViewById(R.id.radioButton10);
                button10.setVisibility(View.INVISIBLE);
                for (int i=0; i<8; i++) {
                    RadioButton radioButton = findViewById(subCatRadio[i]);
                    radioButton.setText(subCatRadio_name[3][i]);
                }
                product.setCategoryId(4);
            }
            else {
                RadioButton button9 = findViewById(R.id.radioButton9);
                button9.setVisibility(View.VISIBLE);
                RadioButton button10 = findViewById(R.id.radioButton10);
                button10.setVisibility(View.VISIBLE);
                if (clickedId == superCatRadio[0]){

                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[0][i]);
                    }
                    product.setCategoryId(1);
                }
                else if (clickedId == superCatRadio[1]){
                    //아우터 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[1][i]);
                    }
                    product.setCategoryId(2);
                }
                else if (clickedId == superCatRadio[2]){
                    //바지 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[2][i]);
                    }
                    product.setCategoryId(3);
                }

                else if (clickedId == superCatRadio[4]){
                    //신발 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[4][i]);
                    }
                    product.setCategoryId(5);
                }
                else if (clickedId == superCatRadio[5]){
                    //가방 하위 카테고리 나타나기
                    for (int i=0; i<10; i++) {
                        RadioButton radioButton = findViewById(subCatRadio[i]);
                        radioButton.setText(subCatRadio_name[5][i]);
                    }
                    product.setCategoryId(6);
                }
            }


        }

    }

    public void onCheckedChange(int id) {
        for (int i = 0; i < 6; i++) {
            RadioButton radioButton = findViewById(superCatRadio[i]);
            radioButton.setChecked(id == superCatRadio[i]);
        }
    }

    private class subCatRadioClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int clickedId = v.getId();

            onSubCheckedChange(clickedId);
            if (clickedId == R.id.radioButton1) {
                product.setDetailedCategoryId(1);
            }
            else if (clickedId == R.id.radioButton2) {
                product.setDetailedCategoryId(2);
            }
            else if (clickedId == R.id.radioButton3) {
                product.setDetailedCategoryId(3);
            }
            else if (clickedId == R.id.radioButton4) {
                product.setDetailedCategoryId(4);
            }
            else if (clickedId == R.id.radioButton5) {
                product.setDetailedCategoryId(5);
            }
            else if (clickedId == R.id.radioButton6) {
                product.setDetailedCategoryId(6);
            }
            else if (clickedId == R.id.radioButton7) {
                product.setDetailedCategoryId(7);
            }
            else if (clickedId == R.id.radioButton8) {
                product.setDetailedCategoryId(8);
            }
            else if (clickedId == R.id.radioButton9) {
                product.setDetailedCategoryId(9);
            }
            else if (clickedId == R.id.radioButton10) {
                product.setDetailedCategoryId(10);
            }
        }
    }

    public void onSubCheckedChange(int id) {
        for (int i = 0; i < 10; i++) {
            RadioButton radioButton = findViewById(subCatRadio[i]);
            radioButton.setChecked(id == subCatRadio[i]);
        }
    }

    private class RegisterCompleteOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String productName = productNameEditText.getText().toString();
            product.setProductName(productName);
            product.setColor1(colorOption1EditText.getText().toString());
            Log.d("color1", product.getColor1());
            product.setColor2(colorOption2EditText.getText().toString());
            Log.d("color2", product.getColor2());
            if (product.getColor2().equals("")) {
                product.setColor2("N");
                Log.d("color2", product.getColor2());
            }


            int productPrice = Integer.parseInt(productPriceEditText.getText().toString());
            product.setProductPrice(productPrice);
            product.setAllowance("N");


            if (!nullValidate(product) && nullToString()) {
                productRegisterRequest(product.getProductName(), product.getCategoryId(), product.getDetailedCategoryId(), product.getProductPrice(), product.getAllowance(), product.getSellerId(), product.getMainImage(), product.getDetailedImage1(), product.getDetailedImage2(), product.getDetailedImage3(), product.getSizeImage());


                //detailedProductRegisterRequest(product.getProductName(), product.getColor1(), product.getColor2(), product.getSizeS(), product.getSizeM(), product.getSizeL());
                Intent intent = new Intent(getApplicationContext(), Seller_ProductRegisterActivity.class);
                startActivity(intent);

            }


            else {
                showAlert("모든 필수 란을 입력하세요.");
            }



        }
    }

    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Seller_ProductRegisterRequestActivity.this);
        builder.setMessage(message)
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }


    private Boolean nullValidate(Product product) {
        if (product == null) {
            Log.d("객체", "null");
            return true; // 객체 자체가 null이면 모든 속성이 null이라고 판단
        }


        if (product.getProductName() == null) {
            Log.d("이름", "null");
            return true;
        }
        else if (product.getProductPrice() == 0) {
            Log.d("가격", "null");
            return true;
        }
        else if (product.getCategoryId() == 0) {
            Log.d("카테고리", "null");
            return true;
        }
        else if (product.getDetailedCategoryId() == 0) {
            Log.d("세부 카테고리", "null");
            return true;
        }
        else if (product.getAllowance() == null) {
            Log.d("관리자수락", "null");
            return true;
        }
        else if (product.getMainImage() == null) {
            Log.d("메인이미지", "null");
            return true;
        }
        else if (product.getDetailedImage1() == null) {
            Log.d("세부imag", "null");
            return true;
        }
        else if (product.getDetailedImage2() == null) {
            Log.d("세부imag2", "null");
            return true;
        }
        else if (product.getDetailedImage3() == null) {
            Log.d("세부imag3", "null");
            return true;
        }
        else if (product.getSizeImage() == null) {
            Log.d("사이즈이미지", "null");
            return true;
        }
        else if (product.getSellerId() == null) {
            Log.d("셀러id", "null");
            return true;
        }
        else if (product.getColor1() == null) {
            Log.d("옵션컬러1", "null");
            return true;
        }
        else if (!sizeCheckBox[0].isChecked() && !sizeCheckBox[1].isChecked() && !sizeCheckBox[2].isChecked()) {
            Log.d("사이즈 선택", "null");
            return true;
        }
        return false;
    }

    private boolean nullToString() {
        if (product.getColor2() == null || product.getColor2().equals("")) {
            product.setColor2("N");
            Log.d("옵션컬러2", product.getColor2());
        }
        if (product.getSizeS() == null) {
            product.setSizeS("N");
            Log.d("사이즈s", "N");
        }
        if (product.getSizeM() == null) {
            product.setSizeM("N");
            Log.d("사이즈m", "N");
        }
        if (product.getSizeL() == null) {
            product.setSizeL("N");
            Log.d("사이즈l", "N");
        }

        if (product.getSizeS() == null || product.getSizeM() == null || product.getSizeL() == null || product.getColor1().equals("") || product.getColor2().equals("")) {
            return false;
        }

        return true;
    }

    private void detailedProductRegisterRequest(String productName, String color1, String color2, String size_s, String size_m, String size_l) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("detailedProductRR", "서버 응답: " + response); // 디버깅을 위한 이 줄을 추가
                    if (response.startsWith("<br")) {
                        // Handle non-JSON response
                        detailedProductHandleNonJsonResponse(response);
                        return;
                    }

                    // If not an error, try to parse the JSON
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        // 서버가 성공적인 응답을 보낸 경우
                        Log.d("Seller_productRR_detail", "서버 연결 성공");

                    } else {
                        // 서버가 실패 응답을 보낸 경우
                        String errorMessage = jsonResponse.getString("error");
                        // 에러 메시지를 적절히 처리
                        Log.e("Seller_productRR_detail", errorMessage);
                    }
                }
                catch (JSONException e) {
                    // JSON 파싱에 실패한 경우
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 상품 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();

                }
            }
        };
        Seller_DetailedProductRegisterRequest sellerDetailedProductRegisterRequest = new Seller_DetailedProductRegisterRequest(
                product.getProductName(), product.getColor1(), product.getColor2(), product.getSizeS(), product.getSizeM(), product.getSizeL(), responseListener, productErrorListener);


        RequestQueue queue = Volley.newRequestQueue(Seller_ProductRegisterRequestActivity.this);
        queue.add(sellerDetailedProductRegisterRequest);
    }

    private void productRegisterRequest(String productName, int categoryId, int detailedCategory, int productPrice, String allowance, String sellerId, String mainImage, String detailedImage1, String detailedImage2, String detailedImage3, String sizeImage) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    Log.d("productRegisterRequest", "서버 응답: " + response); // 디버깅을 위한 이 줄을 추가
                    detailedProductRegisterRequest(product.getProductName(), product.getColor1(), product.getColor2(), product.getSizeS(), product.getSizeM(), product.getSizeL());
                    // Check if the response starts with "<br" indicating an error
                    if (response.startsWith("<br")) {
                        // Handle non-JSON response
                        productHandleNonJsonResponse(response);
                        return; // Stop further processing
                    }
                    // If not an error, try to parse the JSON
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        // 서버가 성공적인 응답을 보낸 경우
                        Log.d("Seller_productRR", "서버 연결 성공");

                    } else {
                        // 서버가 실패 응답을 보낸 경우
                        String errorMessage = jsonResponse.getString("error");
                        // 에러 메시지를 적절히 처리
                        Log.e("Seller_productRR", errorMessage);
                    }
                }
                catch (JSONException e) {
                    // JSON 파싱에 실패한 경우
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 응답 형식 오류로 상품 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        };

        Seller_ProductRegisterRequest sellerProductRegisterRequest = new Seller_ProductRegisterRequest(
                product.getProductName(), product.getCategoryId(), product.getDetailedCategoryId(), product.getProductPrice(), product.getAllowance(), product.getSellerId(), product.getMainImage(), product.getDetailedImage1(), product.getDetailedImage2(), product.getDetailedImage3(), product.getSizeImage(), responseListener, productErrorListener);
        RequestQueue queue = Volley.newRequestQueue(Seller_ProductRegisterRequestActivity.this);
        queue.add(sellerProductRegisterRequest);
    }

    private void detailedProductHandleNonJsonResponse(String response) {
        String[] data = response.split("\\s+"); // 공백으로 분리
        for (String item : data) {
            String[] keyValue = item.split("=>");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                // 키에 기반하여 해당 필드를 업데이트합니다.
                switch (key) {
                    case "[product_id]":

                        break;
                    case "[color_id1]":

                        break;
                    case "[color_id2]":

                        break;
                    case "[size_s]":

                        break;
                    case "[size_m]":

                        break;
                    case "[size_l]":

                        break;

                }

            }
        }
    }

    private void productHandleNonJsonResponse(String response) {
        // 응답에서 데이터를 추출하고 필드를 채웁니다.
        String[] data = response.split("\\s+"); // 공백으로 분리
        for (String item : data) {
            String[] keyValue = item.split("=>");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                ImageView imageView;

                // 키에 기반하여 해당 필드를 업데이트합니다.
                switch (key) {
                    case "[productName]":
                        productNameEditText.setText(value);
                        break;
                    case "[categoryId]":
                        // categoryId에 따라 superCatRadio나 subCatRadio 중 하나를 체크하도록 수정
                        // 예시로 categoryId가 4 이하일 때는 superCatRadio, 그 외에는 subCatRadio 사용
                        int categoryId = Integer.parseInt(value);
                        if (categoryId <= 4) {
                            onCheckedChange(superCatRadio[categoryId - 1]);
                        } else {
                            onSubCheckedChange(subCatRadio[categoryId - 5]);
                        }
                        break;
                    case "[detailedCategoryId]":
                        // detailedCategoryId에 따라 subCatRadio 중 하나를 체크하도록 수정
                        int detailedCategoryId = Integer.parseInt(value);
                        onSubCheckedChange(subCatRadio[detailedCategoryId - 1]);
                        break;
                    case "[productPrice]":
                        productPriceEditText.setText(value);
                        break;
                    case "[allowance]":
                        product.setAllowance("N");
                        break;
                    case "[sellerId]":
                        // sellerId에 따라 처리/
                        break;
                    case "[mainImage]":
                        imageView = findViewById(R.id.productImage);
                        imageView.setImageBitmap(decodeBase64Image(value));
                        break;
                    case "[detailedImage1]":
                        // detailedImage1에 따라 이미지뷰에 이미지를 표시
                        // 예시: detail_image_btn[0].setImageBitmap(decodeBase64Image(value));
                        imageView = findViewById(R.id.image_detail1);
                        imageView.setImageBitmap(decodeBase64Image(value));
                        break;
                    case "[detailedImage2]":
                        // detailedImage2에 따라 이미지뷰에 이미지를 표시
                        // 예시: detail_image_btn[1].setImageBitmap(decodeBase64Image(value));
                        imageView = findViewById(R.id.img_detail2);
                        imageView.setImageBitmap(decodeBase64Image(value));
                        break;
                    case "[detailedImage3]":
                        // detailedImage3에 따라 이미지뷰에 이미지를 표시
                        // 예시: detail_image_btn[2].setImageBitmap(decodeBase64Image(value));
                        imageView = findViewById(R.id.img_detail3);
                        imageView.setImageBitmap(decodeBase64Image(value));
                        break;
                    case "[sizeImage]":
                        // sizeImage에 따라 이미지뷰에 이미지를 표시
                        // 예시: size_image_btn.setImageBitmap(decodeBase64Image(value));
                        imageView = findViewById(R.id.img_sizeInfo);
                        imageView.setImageBitmap(decodeBase64Image(value));
                        break;
                }

            }
        }
    }


    // 이미지를 Base64로 인코딩하는 메서드
    private String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageToBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        String temp = "";
        try{
            temp = URLEncoder.encode(imageToBase64, "UTF-8");
        }
        catch (Exception e) {
            Log.d("exception", e.toString());
        }
        return temp;
    }

    private Bitmap decodeBase64Image(String encodedImage) {
        byte[] decodedByteArray = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }




}
