// Hey adventurer, yes you, guess you found this app's code on github haha well done gj
// As an award, I've hid a easter egg in this app which nobody knows except me (now including you but probably someone who is faster than you have already revealed the secrets lol ).
// This easter egg is packed into the final app you downloaded on the app market, so if you found it, you can show off to your friends and claim you "hacked" your watch (not exactly but it looks cool anyway)
// I'll give you a hint: *whisper* it's definitely not inside config.java. Don't even think about opening that file.
// If you found it please email me @ grieferpig@163.com about your finding and your nickname. First one who answers the 3 questions below will get his/her name listed in the app.
// - How do you found this code
// - What is the easter egg (what does it do, how to activate it, etc)
// - Where does this easter egg located in the code
// - IMPORTANT: What event does this easter egg tribute for
// - Don't forget to add your nickname (not your real name) so I can add your name to the app.
// Now go start your scavenger hunt and claim your prize! First to the key, first to the egg!

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
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.google.android.material.snackbar.Snackbar;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    // DEBUG
    private final String TAG = "Info";

    SeekBar volumeSeeker;

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

        loadSound();
    }

    public void test(View v){
        Log.d(TAG, "Wtf");
    }

    void initTimeSigSelector(NumberPicker BeatAmountPicker, NumberPicker BeatDurationPicker) {
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

    void initAboutTextView(@IdRes int aboutId){
        TextView about = findViewById(aboutId);
        about.setOnLongClickListener(v -> {
            u.clear();
            return true;
        });
        about.setText(
                getString(R.string.about, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.BUILD_TYPE)
        );
    }

    void initVolumeSeeker(@IdRes int volumeSeekerId){
        volumeSeeker = findViewById(volumeSeekerId);
        volumeSeeker.setMax(u.getMaxVolume());
        volumeSeeker.setProgress(u.VOLUME(), true);
        volumeSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    @SuppressWarnings("deprecation")
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
        this.BeatDuration = time;
        u.setLN(time);
    }

    // set time signature

    public void setBeatAmount(int time) {
        this.BeatAmount = time;
        u.setLB(time);
    }

    @SuppressLint("ClickableViewAccessibility")
    void SetSneaksyMsg(@IdRes int sneaksyId) {
        // Sneaksy Message? Nah totally not suspicious, right?
        View imNotFlanksy = findViewById(sneaksyId);
        imNotFlanksy.setOnTouchListener((v, event) -> {
            if (event.getAction() == ACTION_UP) {
                // Ooh a counter! I loves counters. They always make things worse just like Discord. Cool huh?
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

                        // NVM it's me thinking 'bout my gf again but this comment is not the easter egg.
                        // seriously
                        // Hint: the easter egg is touchable
                        msg = "Happy Birthday!";
                    } else {
                        msg = sneaksy.get((int) (Math.random() * 10));
                    }
                    Snackbar.make(imNotFlanksy, msg, Snackbar.LENGTH_SHORT).show();
                    verTouchCounter = 0;
                }
            }
            return false;
        });
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
                    //noinspection BusyWait
                    Thread.sleep((long) NomeTimer.bpmToSecond(bpm, BeatDuration)); // wait until next beat
                } catch (InterruptedException ignored) {
                }
                BeatNow = BeatNow + 1;
                if (!stop && BeatNow == BeatAmount + 1) {
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
