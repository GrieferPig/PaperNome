package com.grieferpig.papergnome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class StorageManager {

    private final Context _operator;
    private String key;
    private final SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public StorageManager(Activity _operator){
        this._operator = _operator;
        this.key = config.settingStoragePath;
        pref = _operator.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public StorageManager(Context _operator, String key){
        this._operator = _operator;
        this.key = key;
        pref = _operator.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public StorageManager setKey(String key) {
        this.key = key;
        return this;
    }

    public StorageManager w(String label, String text){
        editor = pref.edit();
        editor.putString(label,text);
        editor.apply();
        return this;
    }

    public StorageManager w(String label, boolean text){
        editor = pref.edit();
        editor.putBoolean(label,text);
        editor.apply();
        return this;
    }

    public StorageManager w(String label, int text){
        editor = pref.edit();
        editor.putInt(label,text);
        editor.apply();
        return this;
    }

    public StorageManager w(String label, float text){
        editor = pref.edit();
        editor.putFloat(label, text);
        editor.apply();
        return this;
    }

    public StorageManager w(String label, long text){
        editor = pref.edit();
        editor.putLong(label,text);
        editor.apply();
        return this;
    }

    public String r(String key){
        return pref.getString(key, config.settingStorageDefDataString);
    }

    public boolean rb(String key){
        return pref.getBoolean(key, config.settingStorageDefDataBoolean);
    }

    public int ri(String key){
        return pref.getInt(key, config.settingStorageDefDataInt);
    }

    public float rf(String key){
        return pref.getFloat(key, config.settingStorageDefDataFloat);
    }

    public long rl(String key){
        return pref.getLong(key, config.settingStorageDefDataLong);
    }
}
