package com.example.magic_shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GetDeliveryAddressRequest extends StringRequest {

    final static private String URL = "http://210.117.175.207:8976/get_delivery_address.php";
    private Map<String, String> map;
    private Context mContext;

    // Context 매개변수를 가진 생성자
    public GetDeliveryAddressRequest(Context context, String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        mContext = context;

        map = new HashMap<>();
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    // 네트워크 응답을 파싱하여 배송지 주소 데이터를 처리합니다.
    @SuppressLint("LongLogTag")
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d("DeliveryAddressResponse", "배송지 주소 응답: " + json);

            // JSON 응답을 처리하여 배송지 주소 데이터를 가져옵니다.
            JSONObject responseObject = new JSONObject(json);

            // 서버 응답에 따라 키를 조정해야 합니다.
            if (responseObject.getBoolean("success")) {
                JSONArray deliveryAddressesArray = responseObject.getJSONArray("deliveryAddresses");

                // 필요한 경우 배송지 주소 데이터를 브로드캐스트합니다.
                Intent intent = new Intent("deliveryAddressData");
                intent.putExtra("deliveryAddressData", deliveryAddressesArray.toString());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            } else {
                // 서버 응답이 실패한 경우 처리
                Log.e("DeliveryAddressGetRequest", "서버 응답 실패: " + responseObject.getString("message"));
            }

        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }

        return super.parseNetworkResponse(response);
    }
}
