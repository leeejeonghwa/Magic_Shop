package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApproveRefundRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/approveRefund.php";
    private final Map<String, String> map;

    public ApproveRefundRequest(String refundID, String orderID, String sellerID, String productID, String userID,
                                  Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("refundID", refundID);
        map.put("orderID", orderID);
        map.put("sellerID", sellerID);
        map.put("productID", productID);
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
