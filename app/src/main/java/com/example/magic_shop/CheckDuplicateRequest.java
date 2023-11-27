package com.example.magic_shop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckDuplicateRequest extends StringRequest {
    final static private String URL = "http://210.117.175.207:6005/CheckDuplicateID.php";
    private Map<String, String> parameters;

    public CheckDuplicateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}