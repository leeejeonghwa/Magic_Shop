package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Seller_ProductRegisterRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/productRegisterRequest.php";

    private final Map<String, String> map;

    public Seller_ProductRegisterRequest(String productName, int categoryId, int detailedCategory, int productPrice, String allowance, String sellerId, String mainImage, String detailedImage1, String detailedImage2, String detailedImage3, String sizeImage, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("productName", productName);
        map.put("categoryId", String.valueOf(categoryId));
        map.put("detailedCategoryId", String.valueOf(detailedCategory));
        map.put("productPrice", String.valueOf(productPrice));
        map.put("allowance", allowance);
        map.put("sellerId", sellerId);

        // 여기에 이미지를 Base64로 인코딩하여 추가합니다.
        map.put("mainImage", mainImage);
        map.put("detailedImage1", detailedImage1);
        map.put("detailedImage2", detailedImage2);
        map.put("detailedImage3", detailedImage3);
        map.put("sizeImage", sizeImage);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
