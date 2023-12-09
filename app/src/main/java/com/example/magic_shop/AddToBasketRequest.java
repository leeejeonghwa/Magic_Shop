package com.example.magic_shop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddToBasketRequest extends StringRequest {
    private static final String URL = "http://your_server_domain/add_to_basket.php"; // 실제 서버 URL로 변경
    private Map<String, String> params;

    public AddToBasketRequest(String userID, String productID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("productID", productID);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
