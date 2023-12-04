package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeliveryAddressEditRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/deliveryAddressEdit.php";
    private final Map<String, String> map;

    public DeliveryAddressEditRequest(String userID, String addressID, String deliveryAddressName, String recipient,
                                      String phoneNumber, String address, String addressDetail, String deliveryRequest,
                                      String defaultDeliveryAddress, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("addressID", addressID);
        map.put("deliveryAddressName", deliveryAddressName);
        map.put("recipient", recipient);
        map.put("phoneNumber", phoneNumber);
        map.put("address", address);
        map.put("addressDetail", addressDetail);
        map.put("deliveryRequest", deliveryRequest);
        map.put("defaultDeliveryAddress", defaultDeliveryAddress);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

