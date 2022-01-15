package com.tandong.bottomview;

/**
 * Created by office on 2017/8/26.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.IdRes;

/**
 * BottomView
 *
 * @author TanDong
 */
public class BottomView {
    private View convertView;
    private Context context;
    private int theme;
    private Dialog bv;
    private int animationStyle;
    private boolean isTop = false;

    public BottomView(Context c, int theme, View convertView) {
        this.theme = theme;
        this.context = c;
        this.convertView = convertView;
        if (theme == 0) {
            bv = new Dialog(context);
        } else {
            bv = new Dialog(context, theme);
        }
        bv.setCanceledOnTouchOutside(false);
        bv.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bv.setContentView(convertView);
        Window wm = bv.getWindow();
        WindowManager m = wm.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = wm.getAttributes();
        p.width = (int) (d.getWidth() * 1);
        if (isTop) {
            p.gravity = Gravity.TOP;
        } else {
            p.gravity = Gravity.BOTTOM;
        }
        wm.setWindowAnimations(R.style.BottomToTopAnim);
        wm.setAttributes(p);
    }
    WindowManager m;

    public BottomView(Context c, int theme, int resource) {
        this.theme = theme;
        this.context = c;
        this.convertView = View.inflate(c, resource, null);
        if (theme == 0) {
            this.bv = new Dialog(context);
        } else {
            this.bv = new Dialog(context, theme);
        }
        this.bv.setCanceledOnTouchOutside(false);
        this.bv.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.bv.setContentView(convertView);
        Window wm = this.bv.getWindow();
        m = wm.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = wm.getAttributes();
        p.width = (int) (d.getWidth() * 1);
        if (isTop) {
            p.gravity = Gravity.TOP;
        } else {
            p.gravity = Gravity.BOTTOM;
        }
        wm.setWindowAnimations(R.style.BottomToTopAnim);
        wm.setAttributes(p);
    }

    public void showBottomView(boolean CanceledOnTouchOutside) {
        this.bv.show();
    }

    public void setTopIfNecessary() {
        isTop = true;
    }

    public void setAnimation(int animationStyle) {
        this.animationStyle = animationStyle;
    }

    public View getView() {
        return convertView;
    };

    public void dismissBottomView() {
        if (this.bv != null) {
            this.bv.dismiss();
        }
    }

    public void onItemClick(@IdRes int id, View.OnClickListener listener){
        this.bv.findViewById(id).setOnClickListener(listener);
    }

    public void onItemLongClick(@IdRes int id, View.OnLongClickListener listener){
        this.bv.findViewById(id).setOnLongClickListener(listener);
    }

    public void updateItemView(Activity handler, @IdRes int id, String str){
        handler.runOnUiThread(() -> {
            TextView tv = (TextView)this.bv.findViewById(id);
            tv.setText(str);
        });
    }
}
