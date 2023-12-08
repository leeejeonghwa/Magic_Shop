package com.example.magic_shop;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProductDetailedImageLoader {

    private static final String BASE_URL_DETAILED_IMAGE = "http://210.117.175.207:8976/productDetailedImage.php";

    private final Context context;

    public ProductDetailedImageLoader(Context context) {
        this.context = context;
    }

    public void loadDetailedImages(String productName, final DetailedImageResponseListener listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        try {
            String encodedProductName = URLEncoder.encode(productName, "UTF-8");
            encodedProductName = encodedProductName.replace("+", "%20");
            String url = BASE_URL_DETAILED_IMAGE + "?product_name=" + encodedProductName;
            Log.d("RequestURL", "Request URL: " + url);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (listener != null) {
                                listener.onSuccess(response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (listener != null) {
                                listener.onError(error.getMessage());
                            }
                        }
                    }
            );

            requestQueue.add(jsonArrayRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("UnsupportedEncoding", "UnsupportedEncodingException: " + e.getMessage());
            if (listener != null) {
                listener.onError("UnsupportedEncodingException: " + e.getMessage());
            }
        }
    }

    public interface DetailedImageResponseListener {
        void onSuccess(JSONArray response);
        void onError(String errorMessage);
    }
}
