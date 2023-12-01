package com.example.magic_shop;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateAccountRequest extends StringRequest {
    final static private String URL = "http://210.117.175.207:8976/update_refund.php";
    private Map<String, String> params;
    private Context mContext;

    public UpdateAccountRequest(String userID, String account_holder, String bank, String account_number, Response.Listener<String> listener, Response.ErrorListener errorListener, Context context) {
        super(Method.POST, URL, listener, errorListener);
        mContext = context;

        params = new HashMap<>();
        params.put("userID", userID);
        params.put("account_holder", account_holder);
        params.put("bank", bank);
        params.put("account_number", account_number);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
