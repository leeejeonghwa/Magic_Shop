package com.example.magic_shop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChangenameRequest extends StringRequest {
    private static final String URL = "http://210.117.175.207:8976/change_name.php"; // 실제 서버 URL로 변경
    private Map<String, String> params;

    public ChangenameRequest(String userID, String newName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("newName", newName);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}