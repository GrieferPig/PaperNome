package com.grieferpig.papergnome;

import android.app.Activity;
import android.os.Vibrator;
import android.view.Display;
import android.view.WindowManager;

public class util {
    final String firstinstall_tag = "WOW_YOU_FOUND_A_NOTHING";
    Vibrator v;
    Activity a;
    StorageManager _s;

    public util(Vibrator v, Activity a){
        this.v = v;
        this.a = a;
        initConf();
    }

    public void vL(){
        if(VIBRATE()) {
            this.v.vibrate(300);
        }
    }

    public void vS(){
        if(VIBRATE()) {
            this.v.vibrate(150);
        }
    }

    public void initConf(){
        _s = new StorageManager(a);
        if(_s.r(firstinstall_tag).equals("")){
            _s.w(firstinstall_tag, "grieferpig.xyz");
            setLS(90);
            setLB(4);
            setLN(4);
            setV(1);
            setVIB(true);
            setBEEP("hit");
        }
    }

    public void clear(){
        _s.w(firstinstall_tag, "");
    }

    public float VOLUME(){
        return _s.rf(confItem.VOLUME.name());
    }

    public boolean VIBRATE(){
        return _s.rb(confItem.VIBRATE.name());
    }

    public int LAST_SPEED(){
        return _s.ri(confItem.LAST_SPEED.name());
    }

    public int LAST_BAR(){
        return _s.ri(confItem.LAST_BAR.name());
    }
    public int LAST_NOTETIME(){
        return _s.ri(confItem.LAST_NOTETIME.name());
    }

    public void setV(float _t){
        _s.w(confItem.VOLUME.name(), _t);
    }

    public void setLS(int _t){
        _s.w(confItem.LAST_SPEED.name(), _t);
    }

    public void setLB(int _t){
        _s.w(confItem.LAST_BAR.name(), _t);
    }

    public void setLN(int _t){
        _s.w(confItem.LAST_NOTETIME.name(), _t);
    }

    public void setVIB(boolean _t){
        _s.w(confItem.VIBRATE.name(), _t);
    }

    public void setBEEP(String _t){
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
