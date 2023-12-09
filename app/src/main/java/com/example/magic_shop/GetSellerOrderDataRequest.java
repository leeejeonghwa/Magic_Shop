package com.example.magic_shop;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetSellerOrderDataRequest extends StringRequest {

    private static final String URL = "http://210.117.175.207:8976/Seller_order_detail.php";
    private Map<String, String> params;

    public GetSellerOrderDataRequest(String seller_id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("seller_id", seller_id);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
