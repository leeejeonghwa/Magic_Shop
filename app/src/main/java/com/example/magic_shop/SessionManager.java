package com.example.magic_shop;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

public class SessionManager extends AppCompatActivity{
    private static final String PREF_NAME = "LoginPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USER_PASSWORD = "userPassword";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_NICKNAME = "userNickname";
    private static final String KEY_USER_TYPE = "userType";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setUserInfo(boolean isLoggedIn, String userID, String userPassword, String userName, String userNickname, String userType) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.putString(KEY_USER_ID, userID);
        editor.putString(KEY_USER_PASSWORD, userPassword);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_NICKNAME, userNickname);
        editor.putString(KEY_USER_TYPE, userType);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserID() {
        return pref.getString(KEY_USER_ID, "");
    }

    public String getUserPassword() {
        return pref.getString(KEY_USER_PASSWORD, "");
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserNickname() {
        return pref.getString(KEY_USER_NICKNAME, "");
    }

    public String getUserType() {
        return pref.getString(KEY_USER_TYPE, "");
    }


    // 다른 사용자 정보를 가져오는 메소드들 추가

    public void logout() {
        editor.clear();
        editor.apply();
    }

}
