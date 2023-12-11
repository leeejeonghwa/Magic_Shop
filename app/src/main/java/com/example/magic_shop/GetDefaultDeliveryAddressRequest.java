package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetDefaultDeliveryAddressRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/get_default_delivery_address.php";
    private Map<String, String> map;

    // Context 매개변수를 가진 생성자
    public GetDefaultDeliveryAddressRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
