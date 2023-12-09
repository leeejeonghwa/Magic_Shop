package com.example.magic_shop;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeliveryAddressDeleteRequest extends StringRequest {
    final static private String URL = "http://210.117.175.207:8976/delivery_address_delete.php";
    private final Map<String, String> map;

    public DeliveryAddressDeleteRequest(String userID, String addressID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("addressID", addressID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
