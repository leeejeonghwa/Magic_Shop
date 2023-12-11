package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlusQuestionRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/questionPlus.php";
    private final Map<String, String> map;

    public PlusQuestionRequest(String sellerID, String productID, String userID, String type, String subject, String content, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("sellerID", sellerID);
        map.put("productID", productID);
        map.put("userID", userID);
        map.put("type", type);
        map.put("subject", subject);
        map.put("content", content);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

