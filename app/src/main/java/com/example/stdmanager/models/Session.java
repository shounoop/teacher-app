package com.example.stdmanager.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    //    In Android Java, SharedPreferences is a mechanism provided by the Android framework
    //    for storing and retrieving key-value pairs of primitive data types.
    //    It is commonly used to store small amounts of application settings or
    //    user preferences
    private final SharedPreferences prefs;

    public Session(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void set(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String get(String key) {
        return prefs.getString(key, "");
    }
}