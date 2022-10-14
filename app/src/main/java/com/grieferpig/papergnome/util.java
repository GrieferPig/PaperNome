package com.grieferpig.papergnome;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class util {
    final String firstinstall_tag = "WOW_YOU_FOUND_A_NOTHING";
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
        if (_s.r(firstinstall_tag).equals("")) {
            _s.w(firstinstall_tag, "grieferpig.xyz");
            setLS(90);
            setLN(4);
            setLB(4);
            //setV(1);
            setVIB(true);
            setBEEP(config.SOUND_DEF);
        }
    }

    public void clear() {
        _s.w(firstinstall_tag, "");
    }

    public int VOLUME() {
//        return _s.rf(confItem.VOLUME.name());
        return ((AudioManager)this.helper.getSystemService(Context.AUDIO_SERVICE))
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public boolean VIBRATE() {
        return _s.rb(confItem.VIBRATE.name());
    }

    public int LAST_SPEED() {
        return _s.ri(confItem.LAST_SPEED.name());
    }

    public int LAST_NOTETIME() {
        return _s.ri(confItem.LAST_NOTETIME.name());
    }

    public int LAST_BARTIME() {return _s.ri(confItem.LAST_BARTIME.name());}

    public int BEEP() {
        return _s.ri(confItem.BEEP_SOUND.name());
    }

    public void setV(int _t) {
//        _s.w(confItem.VOLUME.name(), _t);
        AudioManager mAudioManager = (AudioManager) this.helper.getSystemService(Context.AUDIO_SERVICE);
//        int _max_vol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //Log.d("TAG", "setV: "+(int)(_t*100));
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,_t, 0);
        //Log.d("TTTAG", mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)+"/"+mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
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

    public Display getDisplay(Activity activity) {
        //WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        WindowManager wm = activity.getWindowManager();
        if (wm != null) {
            return wm.getDefaultDisplay();
        } else {
            return null;
        }
    }
}
