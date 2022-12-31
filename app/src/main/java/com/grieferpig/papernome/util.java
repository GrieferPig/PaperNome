package com.grieferpig.papernome;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;

public class util {
    StorageManager s;
    final String firstInstall_tag = "WOW_YOU_FOUND_A_NOTHING";
    Activity helper;

    Vibrator v;

    public util(Vibrator v, Activity a) {
        this.v = v;
        helper = a;
        initConf();
    }

    public void vL() {
        if (VIBRATE()) {
            v.vibrate(300);
        }
    }
    
    public void vS() {
        if (VIBRATE()) {
            v.vibrate(150);
        }
    }

    public void initConf() {
        StorageManager storageManager = new StorageManager(helper);
        s = storageManager;
        if (storageManager.r("WOW_YOU_FOUND_A_NOTHING").equals("")) {
            s.w("WOW_YOU_FOUND_A_NOTHING", "github.com/GrieferPig");
            setLS(90);
            setLBD(4);
            setLBA(4);
            setVIB(true);
            setBEEP(2);
        }
    }

    public void clear() {
        s.w("WOW_YOU_FOUND_A_NOTHING", "");
    }

    public int VOLUME() {
        return ((AudioManager)helper.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(3);
    }

    public boolean VIBRATE() {
        return s.rb(confItem.VIBRATE.name());
    }

    public int LAST_SPEED() {
        return s.ri(confItem.LAST_SPEED.name());
    }

    public int LAST_BEATDURATION() {
        return s.ri(confItem.LAST_BEATDURATION.name());
    }

    public int LAST_BEATAMOUNT() {
        return s.ri(confItem.LAST_BEATAMOUNT.name());
    }

    public int BEEP() {
        return s.ri(confItem.BEEP_SOUND.name());
    }

    public void setV(int _t) {
        ((AudioManager) helper.getSystemService(Context.AUDIO_SERVICE)).setStreamVolume(3, _t, 0);
    }

    public int getMaxVolume() {
        return ((AudioManager) helper.getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(3);
    }

    public void setLS(int _t) {
        s.w(confItem.LAST_SPEED.name(), _t);
    }

    public void setLBD(int _t) {
        s.w(confItem.LAST_BEATDURATION.name(), _t);
    }

    public void setLBA(int _t) {
        s.w(confItem.LAST_BEATAMOUNT.name(), _t);
    }

    public void setVIB(boolean _t) {
        s.w(confItem.VIBRATE.name(), _t);
    }

    public void setBEEP(int _t) {
        s.w(confItem.BEEP_SOUND.name(), _t);
    }
}
