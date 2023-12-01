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

public class LoginRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/Login.php";
    private Map<String, String> map;
    private Context mContext;

    // 수정: 생성자에 Context 추가
    public LoginRequest(Context context, String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        mContext = context;

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    // 수정: mContext로 변경
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d("ServerResponse", "Server Response: " + json);

            JSONObject jsonObject = new JSONObject(json);

            // optString을 사용하여 userType을 가져오고, 기본값은 빈 문자열("")로 설정
            String userType = jsonObject.optString("userType", "");

            Intent intent = new Intent("userType");
            intent.putExtra("userType", userType);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }

        return super.parseNetworkResponse(response);
    }
}
