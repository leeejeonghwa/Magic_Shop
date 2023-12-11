package com.example.magic_shop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetBasketItemsRequest extends StringRequest {
    private static final String URL = "http://210.117.175.207:8976/GetBasketItems.php"; // 실제 서버 URL로 변경
    private Map<String, String> params;

    public GetBasketItemsRequest(String userID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL + "?userID=" + userID, listener, errorListener);
        params = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
