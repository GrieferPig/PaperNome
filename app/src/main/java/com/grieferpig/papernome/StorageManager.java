package com.grieferpig.papernome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class StorageManager {
    private final Context _operator;
    private SharedPreferences.Editor editor;
    private String key;
    private final SharedPreferences pref;

    public StorageManager(Activity _operator2) {
        this._operator = _operator2;
        String str = config.settingStoragePath;
        this.key = str;
        this.pref = _operator2.getSharedPreferences(str, 0);
    }

    public StorageManager(Context _operator2, String key2) {
        this._operator = _operator2;
        this.key = key2;
        this.pref = _operator2.getSharedPreferences(key2, 0);
    }

    public StorageManager setKey(String key2) {
        this.key = key2;
        return this;
    }

    public StorageManager w(String label, String text) {
        SharedPreferences.Editor edit = this.pref.edit();
        this.editor = edit;
        edit.putString(label, text);
        this.editor.apply();
        return this;
    }

    public StorageManager w(String label, boolean text) {
        SharedPreferences.Editor edit = this.pref.edit();
        this.editor = edit;
        edit.putBoolean(label, text);
        this.editor.apply();
        return this;
    }

    public StorageManager w(String label, int text) {
        SharedPreferences.Editor edit = this.pref.edit();
        this.editor = edit;
        edit.putInt(label, text);
        this.editor.apply();
        return this;
    }

    public StorageManager w(String label, float text) {
        SharedPreferences.Editor edit = this.pref.edit();
        this.editor = edit;
        edit.putFloat(label, text);
        this.editor.apply();
        return this;
    }

    public StorageManager w(String label, long text) {
        SharedPreferences.Editor edit = this.pref.edit();
        this.editor = edit;
        edit.putLong(label, text);
        this.editor.apply();
        return this;
    }

    public String r(String key2) {
        return this.pref.getString(key2, config.settingStorageDefDataString);
    }

    public boolean rb(String key2) {
        return this.pref.getBoolean(key2, config.settingStorageDefDataBoolean);
    }

    public int ri(String key2) {
        return this.pref.getInt(key2, config.settingStorageDefDataInt);
    }

    public float rf(String key2) {
        return this.pref.getFloat(key2, config.settingStorageDefDataFloat);
    }

    public long rl(String key2) {
        return this.pref.getLong(key2, config.settingStorageDefDataLong);
    }
}
