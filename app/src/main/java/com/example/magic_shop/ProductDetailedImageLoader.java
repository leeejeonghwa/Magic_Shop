package com.example.magic_shop;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ProductDetailedImageLoader {

    private static final String BASE_URL_DETAILED_IMAGE = "http://210.117.175.207:8976/productDetailedImage.php";

    private final Context context;

    public ProductDetailedImageLoader(Context context) {
        this.context = context;
    }

    public void loadDetailedImages(String productName, final DetailedImageResponseListener listener) {
        try {
            if (productName != null) {
                String encodedProductName = URLEncoder.encode(productName, StandardCharsets.UTF_8.toString());
                encodedProductName = encodedProductName.replace("+", "%20");
                String url = BASE_URL_DETAILED_IMAGE + "?product_name=" + encodedProductName;
                Log.d("ProductDetailImageLoad", "Request URL: " + url);

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,
                        response -> {
                            if (listener != null) {
                                listener.onSuccess(response);
                            }
                        },
                        error -> {
                            if (listener != null) {
                                listener.onError(error.getMessage());
                            }
                        }
                ));
            } else {
                if (listener != null) {
                    listener.onError("Product name is null");
                }
            }
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
