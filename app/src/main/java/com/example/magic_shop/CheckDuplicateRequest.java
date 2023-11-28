package com.example.magic_shop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckDuplicateRequest extends StringRequest {
    private static final String URL = "http://210.117.175.207:8976/CheckDuplicateID.php";
    private final Map<String, String> parameters;

    public CheckDuplicateRequest(String userID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
