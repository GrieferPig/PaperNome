package com.grieferpig.papergnome;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;

public class util {
    final String firstInstall_tag = "WOW_YOU_FOUND_A_NOTHING";
    Vibrator v;
    StorageManager _s;
    Context helper;

    public util(Vibrator v, Context a) {
        this.v = v;
        this.helper = a;
        initConf();
    }

    public void vL() {
        if (VIBRATE()) {
            this.v.vibrate(300);
        }
    }

    public void vS() {
        if (VIBRATE()) {
            this.v.vibrate(150);
        }
    }

    public void initConf() {
        _s = new StorageManager((Activity) this.helper);
        if (_s.r(firstInstall_tag).equals("")) {
            _s.w(firstInstall_tag, "grieferpig.xyz");
            setLS(90);
            setLN(4);
            setLB(4);
            setVIB(true);
            setBEEP(config.SOUND_DEF);
        }
    }

    public void clear() {
        _s.w(firstInstall_tag, "");
    }

    public int VOLUME() {
        return ((AudioManager)this.helper.getSystemService(Context.AUDIO_SERVICE))
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public boolean VIBRATE() {
        return _s.rb(confItem.VIBRATE.name());
    }

    public int LAST_SPEED() {
        return _s.ri(confItem.LAST_SPEED.name());
    }

    public int LAST_BEATDURATION() {
        return _s.ri(confItem.LAST_NOTETIME.name());
    }

    public int LAST_BEATAMOUNT() {return _s.ri(confItem.LAST_BARTIME.name());}

    public int BEEP() {
        return _s.ri(confItem.BEEP_SOUND.name());
    }

    public void setV(int _t) {
        AudioManager mAudioManager = (AudioManager) this.helper.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,_t, 0);
    }

    public int getMaxVolume(){
        return ((AudioManager)this.helper.getSystemService(Context.AUDIO_SERVICE))
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public void setLS(int _t) {
        _s.w(confItem.LAST_SPEED.name(), _t);
    }

    public void setLN(int _t) {
        _s.w(confItem.LAST_NOTETIME.name(), _t);
    }

    public void setLB(int _t) {_s.w(confItem.LAST_BARTIME.name(), _t);}

    public void setVIB(boolean _t) {
        _s.w(confItem.VIBRATE.name(), _t);
    }

    public void setBEEP(int _t) {
        _s.w(confItem.BEEP_SOUND.name(), _t);
    }
}
