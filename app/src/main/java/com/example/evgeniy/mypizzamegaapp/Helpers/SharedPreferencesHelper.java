package com.example.evgeniy.mypizzamegaapp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String APP_TAG = "localeSharedPreferences";
    private static final String TAG_TOKEN = "app_session_token";
    private static final String TAG_LOGIN = "app_login";
    private static final String TAG_USER_ID = "app_user_id";

    public static void putLogin(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(TAG_LOGIN, value);
        edit.apply();
    }

    public static String getLogin(Context context, String def) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        return sp.getString(TAG_LOGIN, def);
    }

    public static void removeLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(TAG_LOGIN);
        editor.apply();
    }

    public static boolean hasLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        return sp.contains(TAG_LOGIN);
    }

    public static void putToken(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(TAG_TOKEN, value);
        edit.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        return sp.getString(TAG_TOKEN, null);
    }

    public static boolean hssToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        return sp.contains(TAG_TOKEN);
    }

    public static void removeToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(TAG_TOKEN);
        editor.apply();
    }

    public static void logout(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }


//    public static void putInt(Context context, String key, int value) {
//        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putInt(key, value);
//        edit.apply();
//    }
//
//    public static void putBoolean(Context context, String key, boolean value) {
//        SharedPreferences sp = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putBoolean(key, value);
//        edit.apply();
//    }

}
