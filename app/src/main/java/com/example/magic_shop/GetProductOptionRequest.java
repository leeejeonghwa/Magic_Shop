package com.example.magic_shop;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class GetProductOptionRequest extends StringRequest {

        private static final String URL = "http://210.117.175.207:8976/productOption.php"; // 실제 서버 URL로 변경
        private Map<String, String> params;

        public GetProductOptionRequest(String productID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(Method.GET, URL + "?id=" + productID, listener, errorListener);
            Log.d("ProductDetailImageLoad", "Request URL: " + productID);
            params = new HashMap<>();
        }

        @Override
        protected Map<String, String> getParams() {return params;}


}
