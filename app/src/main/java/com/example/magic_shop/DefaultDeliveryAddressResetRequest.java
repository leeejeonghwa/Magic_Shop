package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DefaultDeliveryAddressResetRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/default_delivery_address_reset.php";
    private final Map<String, String> map;

    public DefaultDeliveryAddressResetRequest(String userID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

