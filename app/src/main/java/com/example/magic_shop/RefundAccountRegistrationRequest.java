package com.example.magic_shop;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RefundAccountRegistrationRequest extends StringRequest {

    // PHP 스크립트의 URL을 수정하세요
    private static final String URL = "http://210.117.175.207:8976/process_refund.php"; // 실제 서버 URL로 변경
    private Map<String, String> map;
    private Context mContext;

    // 업데이트: userID 및 응답 리스너를 처리하는 생성자 추가
    public RefundAccountRegistrationRequest(String userID, String account_holder, String bank, String account_number, Response.Listener<String> listener, Response.ErrorListener errorListener, Context context) {
        super(Method.POST, URL, listener, errorListener);

        mContext = context;

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("bank_holder", account_holder);
        map.put("bank_name", bank);
        map.put("bank_account", account_number);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
