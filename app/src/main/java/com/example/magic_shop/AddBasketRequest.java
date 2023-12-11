package com.example.magic_shop;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddBasketRequest extends StringRequest {
    private static final String URL = "http://210.117.175.207:8976/addShoppingBasket.php"; // 실제 서버 URL로 변경
    private Map<String, String> params;

    public AddBasketRequest(String userID, int productID, String productOption, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();


        params.put("userID", userID);
        params.put("productID", String.valueOf(productID));
        params.put("productOption", productOption);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
