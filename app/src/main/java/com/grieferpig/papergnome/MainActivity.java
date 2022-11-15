// TODO: Refactor this shit
// TODO: This is not refactor-able i gave up
// TODO: What

package com.grieferpig.papergnome;

import static android.view.MotionEvent.ACTION_UP;
import static com.grieferpig.papergnome.config.SOUND_DEF;
import static com.grieferpig.papergnome.config.SOUND_HIT;
import static com.grieferpig.papergnome.config.SOUND_SINE;
import static com.grieferpig.papergnome.config.sneaksy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    // DEBUGGGGGGGGGG
    private final String TAG = "Info";

    TextView bpm_now, tv_noteTime, tv_barTime, about;
    Switch vib_beat;
    SeekBar volume_seek;

    boolean stop = false;
    int bpm;
    int BeatAmount, BeatDuration;

    int verTouchCounter = 0;

    SoundPool sp1 = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
    SoundPool sp2 = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
    util u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        u = new util(vibrator, this);
        bpm = u.LAST_SPEED();
        BeatDuration = u.LAST_BEATDURATION();
        BeatAmount = u.LAST_BEATAMOUNT();

        volume_seek = findViewById(R.id.volume_seek);
        volume_seek.setMax(u.getMaxVolume());
        volume_seek.setProgress(u.VOLUME(), true);
        volume_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                u.setV(progress);
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
            u.clear();
            return true;
        });
        about.setText("节拍器 " + BuildConfig.VERSION_NAME + ", 版本 " + BuildConfig.VERSION_CODE + ", " + BuildConfig.BUILD_TYPE + "\rBy GrieferPig");
        about.setTextSize((float) 10.0);
        loadSound();

    }

    public void initTimeSigSelector(NumberPicker BeatAmountPicker, NumberPicker BeatDurationPicker) {
        // NumberPicker mareTimeBayPickle = findPonyByName(R.name.Sprout);

        BeatAmountPicker.setValue(BeatAmount);

        BeatAmountPicker.setOnValueChangedListener((picker, oldVal, newVal) -> setBeatAmount(newVal));

        BeatDurationPicker.setOnValueChangedListener((picker, oldVal, newVal) -> setBeatDuration((int) Math.pow(2, newVal)));

        String[] data = {"2", "4", "8"};
        BeatDurationPicker.setMinValue(1);
        BeatDurationPicker.setMaxValue(data.length);
        BeatDurationPicker.setDisplayedValues(data);
        switch (BeatDuration) {
            case 2:
                BeatDurationPicker.setValue(1);
                break;

            case 4:
                BeatDurationPicker.setValue(2);
                break;

            case 8:
                BeatDurationPicker.setValue(3);
                break;
        }
    }


    private void switchSound() {
        switch (u.BEEP()) {
            case SOUND_HIT:
                u.setBEEP(SOUND_SINE);
                break;
            case SOUND_SINE:
                u.setBEEP(SOUND_DEF);
                break;
            case SOUND_DEF:
                u.setBEEP(SOUND_HIT);
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

    @SuppressLint("ClickableViewAccessibility")
    void SetSneaksyMsg(@IdRes int sneaksyId) {
        findViewById(sneaksyId).setOnTouchListener((v, event) -> {
            if (event.getAction() == ACTION_UP) {
                verTouchCounter = verTouchCounter + 1;
                if (verTouchCounter == 8) {
                    Date d = new Date(System.currentTimeMillis());
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
                    String fD = formatter.format(d);
                    String msg;
                    Log.d(TAG, "onTouch: " + fD);
                    if (fD.equals("07-16")) {
                        // hardcoded love, immutable during lifetime
                        // really
                        //
                        // she's amazing
                        msg = "Happy Birthday!";
                    } else {
                        msg = sneaksy.get((int) (Math.random() * 10));
                    }
                    Snackbar.make(findViewById(R.id.mainLayer), msg, Snackbar.LENGTH_SHORT).show();
                    verTouchCounter = 0;
                }
            }
            return false;
        });
    }

    public void addBpm(boolean is10x, View v) {
        if (is10x) {
            bpm = bpm + 10;
        } else {
            bpm++;
        }
        u.setLS(bpm);
        if (bpm > 240) {
            bpm = 240;
            u.setLS(240);
            Snackbar.make(v, "已经最快啦~", Snackbar.LENGTH_SHORT).show();
        }
    }

    // set bpm

    public void minusBpm(boolean is10x, View v) {
        if (is10x) {
            bpm = bpm - 10;
        } else {
            bpm--;
        }
        u.setLS(bpm);
        if (bpm < 10) {
            bpm = 10;
            u.setLS(10);
            Snackbar.make(v, "已经最慢啦~", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void setBeatDuration(int time) {
        this.noteTime = time;
        u.setLN(time);
    }

    // set time signature

    public void setBeatAmount(int time) {
        this.barTime = time;
        u.setLB(time);
    }

    class counting extends Thread {
        @Override
        public void run() {
            int BarNow = 1;
            int BeatNow = 1;
            stop = false;
            sp1.play(1, 1, 1, 0, 0, 1);
            u.vL();
            while (!stop) {
                try {
                    Thread.sleep((long) NomeTimer.bpmToSecond(bpm, barTime)); // wait until next beat
                } catch (InterruptedException ignored) {
                }
                BeatNow = BeatNow + 1;
                if (!stop && BeatNow == noteTime + 1) {
                    BarNow = BarNow + 1;
                    BeatNow = 1;
                    sp1.play(1, 1, 1, 0, 0, 1);
                    u.vL();
                } else {
                    if (!stop) {
                        sp2.play(1, 1, 1, 0, 0, 1);
                        //u.vS();
                    }
                }
            }
        }
    }
}
