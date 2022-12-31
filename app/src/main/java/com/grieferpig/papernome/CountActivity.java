package com.grieferpig.papernome;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class CountActivity extends Activity {

    // why I hate builders
    private final AudioAttributes sPAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
    SoundPool sp1, sp2;
    util u;
    boolean stop = false;
    int bpm, BeatDuration, BeatAmount;

    TextView tv_beatNow, tv_barNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        u = new util((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), this);
        sp1 = new SoundPool.Builder()
                .setAudioAttributes(sPAttributes)
                .build();
        sp2 = new SoundPool.Builder()
                .setAudioAttributes(sPAttributes)
                .build();
        bpm = u.LAST_SPEED();
        BeatDuration = u.LAST_BEATDURATION();
        BeatAmount = u.LAST_BEATAMOUNT();
        loadSound();

        tv_beatNow = findViewById(R.id.tv_beatNow);
        tv_barNow = findViewById(R.id.tv_barNow);

        Log.d("TAG", String.format("%s %s %s", bpm, BeatDuration, BeatAmount));

        sp1.setOnLoadCompleteListener((soundPool, sampleId, status) -> new counting().start());
    }

    private void loadSound() {
        sp1.release();
        sp2.release();
        sp1 = new SoundPool.Builder()
                .setAudioAttributes(sPAttributes)
                .build();
        sp2 = new SoundPool.Builder()
                .setAudioAttributes(sPAttributes)
                .build();
        switch (u.BEEP()) {
            case 0:
                sp1.load(this, R.raw.hit_high, 1);
                sp2.load(this, R.raw.hit_low, 2);
                return;
            case 1:
                sp1.load(this, R.raw.sine_high, 1);
                sp2.load(this, R.raw.sine_low, 2);
                return;
            case 2:
                sp1.load(this, R.raw.def_high, 1);
                sp2.load(this, R.raw.def_low, 2);
                return;
            default:
        }
    }

    class counting extends Thread {
        public void run() {
            int BarNow = 1;
            int BeatNow = 1;
            stop = false;
            u.vL();
            sp1.play(1, 1, 1, 0, 0, 1);
            while (!stop) {
                try {
                    Thread.sleep((long) NomeTimer.bpmToSecond(bpm, BeatDuration));
                } catch (InterruptedException ignored) {}
                BeatNow++;
                if (!stop && BeatNow == BeatAmount + 1) {
                    BarNow++;
                    BeatNow = 1;
                    sp1.play(1, 1, 1, 0, 0, 1);
                    u.vL();
                } else if (!stop) {
                    sp2.play(1, 1, 1, 0, 0, 1);
                }
                int finalBarNow = BarNow;
                int finalBeatNow = BeatNow;
                runOnUiThread(() -> {
                    tv_barNow.setText(String.format(Locale.getDefault(),"%d", finalBarNow));
                    tv_beatNow.setText(String.format(Locale.getDefault(),"%d", finalBeatNow));
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        stop = true;
        super.onDestroy();
    }

    public void onCloseBtnClicked(View v){
        finish();
    }
}

