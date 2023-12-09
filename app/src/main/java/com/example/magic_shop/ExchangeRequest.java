package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/exchangeRequest.php";
    private final Map<String, String> map;

    public ExchangeRequest(String orderID, String sellerID, String productID, String userID, String content,
                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("orderID", orderID);
        map.put("sellerID", sellerID);
        map.put("productID", productID);
        map.put("userID", userID);
        map.put("content", content);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
