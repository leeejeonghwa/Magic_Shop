package com.example.magic_shop;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class ProductsLoader {

    final static private String URL = "http://210.117.175.207:8976/productMainImage.php";

    private final Context context;

    public ProductsLoader(Context context) {
        this.context = context;
    }

    public void loadProducts(final ResponseListener<JSONArray> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
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
    }

    public interface ResponseListener<T> {
        void onSuccess(T response);
        void onError(String errorMessage);
    }
}
