package com.example.magic_shop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RefundDataRequest extends StringRequest {
    private static final String URL = "http://210.117.175.207:8976/search_refund.php";
    private final Map<String, String> parameters;

    public RefundDataRequest(String userID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
