package com.example.harshil.charotarexplore;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by harshil on 28-01-2017.
 */

public class cached {
    private static final String PREFERENCE_NAME = "CACHED_DATA";

    public void setUser_id(String user_id, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putString("user_id", user_id);
        editor.apply();
    }

    public String getUser_id(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("user_id", "");
    }

    public void setUser_name(String user_name, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putString("user_name", user_name);
        editor.apply();
    }

    public String getUser_name(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("user_name", "");
    }

    public void setCountry_code(String country_code, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putString("country_code", country_code);
        editor.apply();
    }

    public String getCountry_code(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("country_code", "");
    }

    public void setContact_number(String contact_number, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putString("contact_number", contact_number);
        editor.apply();
    }

    public String getContact_number(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("contact_number", "");
    }

    public void setUser_avatar(String user_avatar, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putString("user_avatar", user_avatar);
        editor.apply();
    }

    public String getUser_avatar(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("user_avatar", "");
    }

    public void clearCache(Context context) {
        context.getSharedPreferences(PREFERENCE_NAME, 0).edit().clear().apply();
    }
}
