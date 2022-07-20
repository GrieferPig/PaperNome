package com.grieferpig.papergnome;

import static android.view.MotionEvent.ACTION_UP;

import androidx.cardview.widget.CardView;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.snackbar.Snackbar;
import com.tandong.bottomview.BottomView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements View.OnTouchListener {

    RealtimeBlurView blur_layer;
    BottomView bv, bv1, bv2;
    TextView bpm_now, tv_noteTime, about;
    CardView bpmsetCard;
    Switch vib_beat;
    SeekBar volume_seek;
    ImageView dragger;
    Button switch_sound;

    int lastY = -1000;

    boolean settingShown = false;

    boolean stop = false;
    int bpm;
    int noteTime;

    CardView _settings;

    int swidth = 0;
    int sheight = 0;

    int verTouchCounter = 0;

    SoundPool sp1 = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
    SoundPool sp2 = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
    util u;

    public void setFocus(boolean bool) {
        findViewById(R.id.bpmsetCard).setEnabled(bool);
        findViewById(R.id.jiepaisetcard).setEnabled(bool);
        settingShown = !bool;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        u = new util(vibrator, this);
        u.clear();
        u.initConf();
        Display _display = u.getDisplay(this);
        sheight = _display.getHeight();
        swidth = _display.getWidth();
        bpm = u.LAST_SPEED();
        noteTime = u.LAST_NOTETIME();
        setContentView(R.layout.activity_main);
        ViewGroup.LayoutParams constParams = findViewById(R.id.settingconstraint).getLayoutParams();
        constParams.height = sheight;
        findViewById(R.id.settingconstraint).setLayoutParams(constParams);
        setFocus(true);
        _settings = findViewById(R.id.settingCard);
        dragger = findViewById(R.id.dragger);
        setTopMargin(_settings, sheight);
        dragger.setOnTouchListener(this);
        vib_beat = findViewById(R.id.vib_beat);
        vib_beat.setChecked(u.VIBRATE());
        vib_beat.setOnCheckedChangeListener((buttonView, isChecked) -> u.setVIB(isChecked));
        ArrayList<String> _hidden_texts = new ArrayList<String>();
        _hidden_texts.addAll(Arrays.asList(
                "You found a nothing!",
                "Open-Source on Github!",
                "Very Buggy!",
                "Aww Izzy!",
                "never gonna give you up",
                "Friendship is Magic!",
                "bRuH",
                "Sub Me On bilibili!",
                "https://grieferpig.xyz!",
                "Make Your Mark!"));
        findViewById(R.id.ver).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == ACTION_UP) {
                    verTouchCounter = verTouchCounter + 1;
                    if (verTouchCounter == 8) {
                        Snackbar.make(findViewById(R.id.mainLayer), _hidden_texts.get((int) (Math.random() * 10)), Snackbar.LENGTH_SHORT).show();
                        verTouchCounter = 0;
                    }
                }
                return false;
            }
        });
        volume_seek = findViewById(R.id.volume_seek);
        volume_seek.setMax(100);
        volume_seek.setProgress((int) (u.VOLUME() * 100), true);
        volume_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float _p = (float) progress;
                u.setV(_p / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        about = findViewById(R.id.ver);
        about.setOnLongClickListener(v -> {
            //u.clear();
            return true;
        });
        about.setText("节拍器 " + BuildConfig.VERSION_NAME + " ,Version " + BuildConfig.VERSION_CODE + ", " + BuildConfig.BUILD_TYPE + "\rBy GrieferPig");
        about.setTextSize((float) 8.0);
        loadSound();
        blur_layer = findViewById(R.id.blur_layer);
        bpm_now = findViewById(R.id.tv_bpm);
        tv_noteTime = findViewById(R.id.tv_noteTime);
        bpm_now.setText(bpm + "");
        switch_sound = findViewById(R.id.switch_sound);
        switch_sound.setText("切换节拍声音: "+config.sounds[u.BEEP()][1]);
        tv_noteTime.setText(noteTime + "");
        bpmsetCard = findViewById(R.id.bpmsetCard);
        bpmsetCard.setOnLongClickListener(v -> {
            bounceViewEffect(v);
            new Handler().postDelayed(() -> {
                ValueAnimator anim = ValueAnimator.ofFloat(0.2f, 1f);
                anim.setDuration(300);
                anim.addUpdateListener(animation -> {
                    float currentValue = (float) animation.getAnimatedValue();
                    blur_layer.setAlpha(currentValue);
                });
                anim.start();
            }, 200);
            bv2.showBottomView(false);
            Thread _count = new counting();
            _count.setDaemon(true);
            _count.setPriority(Thread.MAX_PRIORITY);
            _count.start();
            return true;
        });
        bv = new BottomView(this, R.style.BottomViewTheme_Transparent, R.layout.popup_layout);
        bv.onItemClick(R.id.popup_bpm_add, v -> {
            minusBpm(false, v);
            bounceViewEffect(v);
        });
        bv.onItemLongClick(R.id.popup_bpm_add, v -> {
            minusBpm(true, v);
            bounceViewEffect(v);
            return true;
        });
        bv.onItemClick(R.id.popup_bpm_minus, v -> {
            addBpm(false, v);
            bounceViewEffect(v);
        });
        bv.onItemLongClick(R.id.popup_bpm_minus, v -> {
            addBpm(true, v);
            bounceViewEffect(v);
            return true;
        });

        bv1 = new BottomView(this, R.style.BottomViewTheme_Transparent, R.layout.layout);
        bv1.onItemClick(R.id.popup_noteTime_add, v -> {
            addNoteTime(v);
            bounceViewEffect(v);
        });
        bv1.onItemClick(R.id.popup_noteTime_minus, v -> {
            minusNoteTime(v);
            bounceViewEffect(v);
        });

        bv2 = new BottomView(this, R.style.BottomViewTheme_Transparent, R.layout.popup_count);
        updateBpmSpeedView();
        updateNoteTimeSpeedView();
    }

    /**
     * sound handling
     */

    private void switchSound() {
        switch (u.BEEP()) {
            case config.SOUND_HIT:
                u.setBEEP(config.SOUND_SINE);
                break;
            case config.SOUND_SINE:
                u.setBEEP(config.SOUND_DEF);
                break;
            case config.SOUND_DEF:
                u.setBEEP(config.SOUND_HIT);
                break;
        }
    }

    private void loadSound() {
        sp1 = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
        sp2 = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
        switch (u.BEEP()) {
            case config.SOUND_HIT:
                sp1.load(this, R.raw.hit_high, 1);
                sp2.load(this, R.raw.hit_low, 2);
                break;
            case config.SOUND_SINE:
                sp1.load(this, R.raw.sine_high, 1);
                sp2.load(this, R.raw.sine_low, 2);
                break;
            case config.SOUND_DEF:
                sp1.load(this, R.raw.def_high, 1);
                sp2.load(this, R.raw.def_low, 2);
                break;
        }
    }

    /**
     * ui handling
     */

    // handling swipe-up pop menu
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                // 按下
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // 点击
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!settingShown) {
                    // 滑动
                    if (lastY == -1000) {
                        lastY = (int) distanceY;
                    } else {
                        if (lastY < distanceY) {
                            if ((distanceY - lastY) > 80) {
                                new Handler().postDelayed(() -> {
                                    ValueAnimator anim1 = ValueAnimator.ofFloat(sheight, 0f);
                                    anim1.setDuration(200);
                                    anim1.addUpdateListener(animation -> {
                                        float currentValue = (float) animation.getAnimatedValue();
                                        setTopMargin(_settings, (int) currentValue);
                                    });
                                    anim1.start();
                                }, 0);
                                setFocus(false);
                                lastY = -1000;
                            }
                        } else {
                            lastY = -1000;
                        }
                    }
                    Log.d("TAG", "onScroll: " + distanceX);
                }
                //setTopMargin(_settings, 0); //向左滑（与滑动动画配合效果较好）
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // 长按
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        });
        gestureDetector.onTouchEvent(ev); // 让GestureDetector响应触碰事件
        super.dispatchTouchEvent(ev); // 让Activity响应触碰事件
        return false;
    }

    // handling clicks

    public void onBpmSettingClick(View v) {
        bounceViewEffect(v);
        new Handler().postDelayed(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(0.2f, 1f);
            anim.setDuration(300);
            anim.addUpdateListener(animation -> {
                float currentValue = (float) animation.getAnimatedValue();
                blur_layer.setAlpha(currentValue);
            });
            anim.start();
        }, 200);
        bv.showBottomView(false);
    }

    public void onClosePopupClick(View v) {
        bounceViewEffect(v);
        new Handler().postDelayed(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.2f);
            anim.setDuration(300);
            anim.addUpdateListener(animation -> {
                float currentValue = (float) animation.getAnimatedValue();
                blur_layer.setAlpha(currentValue);
            });
            anim.start();
        }, 200);
        bv.dismissBottomView();
    }

    // add/minus bpm

    public void addBpm(boolean is10x, View v) {
        if (is10x) {
            bpm = bpm + 10;
            u.setLS(bpm);
            if (bpm > 240) {
                bpm = 240;
                u.setLS(240);
                Snackbar.make(v, "已经最快啦~", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            bpm++;
            u.setLS(bpm);
            if (bpm > 240) {
                bpm = 240;
                u.setLS(240);
                Snackbar.make(v, "已经最快啦~", Snackbar.LENGTH_SHORT).show();
            }
        }
        updateBpmSpeedView();
    }

    public void minusBpm(boolean is10x, View v) {
        if (is10x) {
            bpm = bpm - 10;
            u.setLS(bpm);
            if (bpm < 10) {
                bpm = 10;
                u.setLS(10);
                Snackbar.make(v, "已经最慢啦~", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            bpm--;
            u.setLS(bpm);
            if (bpm < 10) {
                bpm = 10;
                u.setLS(10);
                Snackbar.make(v, "已经最慢啦~", Snackbar.LENGTH_SHORT).show();
            }
        }
        updateBpmSpeedView();
    }

    // add/minus notetime

    public void addNoteTime(View v) {
        noteTime++;
        u.setLN(noteTime);
        if (noteTime > 12) {
            u.setLN(12);
            noteTime = 12;
            Snackbar.make(v, "已经最大啦~", Snackbar.LENGTH_SHORT).show();
        }
        updateNoteTimeSpeedView();
    }

    public void minusNoteTime(View v) {
        noteTime--;
        u.setLN(noteTime);
        if (noteTime < 1) {
            noteTime = 1;
            u.setLN(1);
            Snackbar.make(v, "已经最小啦~", Snackbar.LENGTH_SHORT).show();
        }
        if (noteTime == 1) {
            u.setLN(1);
        }
        updateNoteTimeSpeedView();
    }

    // syncing changed settings

    private void updateBpmSpeedView() {
        bv.updateItemView(this, R.id.popup_bpm_label, bpm + "");
        bpm_now.setText(bpm + "");
    }

    private void updateNoteTimeSpeedView() {
        bv1.updateItemView(this, R.id.popup_noteTime_label, noteTime + "");
        tv_noteTime.setText(noteTime + "");
    }

    // bunch of visual effects here

    private void bounceViewEffect(View v) {
        runOnUiThread(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.85f);
            anim.setDuration(120);
            anim.addUpdateListener(animation -> {
                float currentValue = (float) animation.getAnimatedValue();
                v.setScaleX(currentValue);
                v.setScaleY(currentValue);
            });
            anim.start();
            new Handler().postDelayed(() -> {
                ValueAnimator anim1 = ValueAnimator.ofFloat(0.85f, 1f);
                anim1.setDuration(120);
                anim1.addUpdateListener(animation -> {
                    float currentValue = (float) animation.getAnimatedValue();
                    v.setScaleX(currentValue);
                    v.setScaleY(currentValue);
                });
                anim1.start();
            }, 170);
        });
    }

    public void onBarTimeChangeClick(View v){
        bounceViewEffect(v);
        new Handler().postDelayed(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(0.2f, 1f);
            anim.setDuration(300);
            anim.addUpdateListener(animation -> {
                float currentValue = (float) animation.getAnimatedValue();
                blur_layer.setAlpha(currentValue);
            });
            anim.start();
        }, 200);
        bv1.showBottomView(false);
    }

    public void onClosePopupClick1(View v) {
        bounceViewEffect(v);
        new Handler().postDelayed(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.2f);
            anim.setDuration(300);
            anim.addUpdateListener(animation -> {
                float currentValue = (float) animation.getAnimatedValue();
                blur_layer.setAlpha(currentValue);
            });
            anim.start();
        }, 200);
        bv1.dismissBottomView();
    }

    public void onClosePopupClick2(View v) {
        bounceViewEffect(v);
        new Handler().postDelayed(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(1f, 0.2f);
            anim.setDuration(300);
            anim.addUpdateListener(animation -> {
                float currentValue = (float) animation.getAnimatedValue();
                blur_layer.setAlpha(currentValue);
            });
            anim.start();
        }, 200);
        bv2.dismissBottomView();
        stop = true;
    }

    public void onChangeSoundClick(View v){
        switchSound();
        loadSound();
        switch_sound.setText("切换节拍声音: "+config.sounds[u.BEEP()][1]);
    }

    // animating a card's rIsE aNd FaLl
    public boolean onTouch(View view, MotionEvent event) {
        int lastY = 0;
        float _top = 0;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //将点下的点的坐标保存
                lastY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                //计算出需要移动的距离
                int dy = (int) event.getRawY() - lastY;
                //将移动距离加上，现在本身距离边框的位置
                int top = view.getTop() + dy - 10;
                _top = top;
                //获取到layoutParams然后改变属性，在设置回去
                setTopMargin(_settings, top);
                lastY = (int) event.getY();
                //f*** integer

                blur_layer.setAlpha(1 - (_top / sheight));
                break;
            case ACTION_UP:
                float scrollPercent = 1 - blur_layer.getAlpha();
                if (scrollPercent <= .3) {
                    float final_top = sheight - (blur_layer.getAlpha() * sheight);
                    new Handler().postDelayed(() -> {
                        ValueAnimator anim = ValueAnimator.ofFloat(blur_layer.getAlpha(), 1f);
                        anim.setDuration(100);
                        anim.addUpdateListener(animation -> {
                            float currentValue = (float) animation.getAnimatedValue();
                            blur_layer.setAlpha(currentValue);
                        });
                        anim.start();
                        ValueAnimator anim1 = ValueAnimator.ofFloat(final_top, 0f);
                        anim1.setDuration(100);
                        anim1.addUpdateListener(animation -> {
                            float currentValue = (float) animation.getAnimatedValue();
                            setTopMargin(_settings, (int) currentValue);
                        });
                        anim1.start();
                    }, 0);
                } else {
                    float final_top1 = sheight - (blur_layer.getAlpha() * sheight);
                    float _sheight = sheight;
                    new Handler().postDelayed(() -> {
                        ValueAnimator anim = ValueAnimator.ofFloat(blur_layer.getAlpha(), 0f);
                        anim.setDuration(150);
                        anim.addUpdateListener(animation -> {
                            float currentValue = (float) animation.getAnimatedValue();
                            blur_layer.setAlpha(currentValue);
                        });
                        anim.start();
                        ValueAnimator anim1 = ValueAnimator.ofFloat(final_top1, _sheight);
                        anim1.setDuration(150);
                        anim1.addUpdateListener(animation -> {
                            float currentValue = (float) animation.getAnimatedValue();
                            setTopMargin(_settings, (int) currentValue);
                        });
                        anim1.start();
                    }, 0);
                    setFocus(true);
                }
                break;
        }
        //刷新界面
        _settings.invalidate();
        return true;
    }

    public static void setTopMargin(View view, int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, topMargin,
                layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    /**
     * counting thread
     */

    class counting extends Thread {
        @Override
        public void run() {
            int bar_now = 1;
            int noteTime_now = 1;
            bv2.updateItemView(MainActivity.this, R.id.bars_now, bar_now + "");
            bv2.updateItemView(MainActivity.this, R.id.noteTime_now, noteTime_now + "");
            stop = false;
            try {
                sleep(1500);
            } catch (InterruptedException ignored) {
            }
            if (!stop) {
                sp1.play(1, u.VOLUME(), u.VOLUME(), 0, 0, 1);
                u.vL();
            }
            while (!stop) {
                Log.d("notetime", "run: "+noteTime_now);
                try {
                    Thread.sleep((long) NomeTimer.bpmToSecond(bpm)); // wait until next beat
                } catch (InterruptedException ignored) {
                }
                noteTime_now = noteTime_now + 1;
                if (!stop && noteTime_now == noteTime + 1) {
                    bar_now = bar_now + 1;
                    noteTime_now = 1;
                    sp1.play(1, u.VOLUME(), u.VOLUME(), 0, 0, 1);
                    u.vL();
                } else {
                    if (!stop) {
                        sp2.play(1, u.VOLUME(), u.VOLUME(), 0, 0, 1);
                        u.vS();
                    }
                }
                bv2.updateItemView(MainActivity.this, R.id.bars_now, bar_now + "");
                bv2.updateItemView(MainActivity.this, R.id.noteTime_now, noteTime_now + "");
            }
        }
    }

}