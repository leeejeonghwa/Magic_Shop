package com.example.magic_shop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveOrderRequest extends StringRequest {

    private static final String URL = "http://210.117.175.207:8976/SaveOrder.php";
    private final Map<String, String> map;

    public SaveOrderRequest(String userID, List<Integer> productIDs, int totalAmount, String paymentMethod, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("productIDs", convertProductIDsToString(productIDs));
        map.put("totalAmount", String.valueOf(totalAmount));
        map.put("paymentMethod", paymentMethod);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    private String convertProductIDsToString(List<Integer> productIDs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < productIDs.size(); i++) {
            builder.append(productIDs.get(i));
            if (i < productIDs.size() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
}
