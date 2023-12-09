package com.example.magic_shop;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketUpdateRequest extends StringRequest {
    private static final String URL = "http://210.117.175.207:8976/BasketUpdate.php"; // 바스켓 업데이트를 처리하는 PHP 파일의 경로로 수정
    private final Map<String, String> map;

    public BasketUpdateRequest(List<Integer> basket_ids, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("basket_ids", convertProductIDsToString(basket_ids));

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    private String convertProductIDsToString(List<Integer> basket_ids) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < basket_ids.size(); i++) {
            builder.append(basket_ids.get(i));
            if (i < basket_ids.size() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
}
