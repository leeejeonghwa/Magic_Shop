package com.example.magic_shop;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

public class SessionManager extends AppCompatActivity {
    private static final String PREF_NAME = "LoginPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_AV_RESTART = "avdRestart";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private boolean isLoggedInNow;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        isLoggedInNow = pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLogin(boolean isLoggedIn) {
        isLoggedInNow = isLoggedIn;
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return isLoggedInNow;
    }

    public void resetLogin() {
        isLoggedInNow = false;
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.putBoolean(KEY_AV_RESTART, false);
        // 사용자 정보 초기화
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_EMAIL);
        editor.apply();
    }

    public boolean isAVDRestarted() {
        return pref.getBoolean(KEY_AV_RESTART, false);
    }

    public void setAVDRestarted(boolean isRestarted) {
        editor.putBoolean(KEY_AV_RESTART, isRestarted);
        editor.apply();
    }
}
