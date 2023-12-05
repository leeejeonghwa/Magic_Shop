package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Seller_DetailedProductRegisterRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/detailed_product_register_request.php";
    private final Map<String, String> map;

    public Seller_DetailedProductRegisterRequest(String productName, String color1, String color2, String size_s, String size_m, String size_l, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("productName", productName);
        map.put("color_id1", color1);
        map.put("color_id2", color2);
        map.put("size_s", size_s);
        map.put("size_m", size_m);
        map.put("size_l", size_l);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
