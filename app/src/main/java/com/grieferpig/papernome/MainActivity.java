/**
 * Fun fact: there was once that this project's files somehow vanished into thin air
 * when I restarted my laptop. I've searched anywhere on my disk and tried a few of data rescue
 * software but with no luck. And coincidentally (and unfortunately) I did not upload
 * that version of code to github, and all I have is a compiled apk of that version
 * in my watch. To describe the process of how the code vanished:
 * "It was like 'poof', disappeared."
 * - Izzy Moonbow, "My Little Pony: A New Generation"
 * so tragically, I have to decompile that apk and reorganize the code, which is a REALLY PAINFUL process
 * BTW, I've wrote tons of fun comments like this in that version of code, and since comments does
 * not compiled into binaries, you know what happens
 * tHe FuN HaS BeEn DoUbLeD!
 */

package com.grieferpig.papernome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private final String TAG = "Surprise motherfucker:"; // Ah, don't mind it

    int BeatAmount, BeatDuration, bpm, verTouchCounter = 0;
    Button btn_start;
    ImageView btn_bpm_minus, btn_bpm_add;
    boolean isAnimating, isStartBtnHidden = false;

    ScrollView sv_main;
    util u;
    SeekBar volumeSeeker;

    TextView tv_bpm, tv_time_sig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        u = new util((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), this);
        Animation startBtn_floatDown = AnimationUtils.loadAnimation(this, R.anim.start_btn_float_down);
        final Animation startBtn_floatUp = AnimationUtils.loadAnimation(this, R.anim.start_btn_float_up);

        bpm = u.LAST_SPEED();

        btn_start = findViewById(R.id.btn_start);
        tv_bpm = findViewById(R.id.tv_bpm);
        tv_time_sig = findViewById(R.id.tv_time_sig);
        btn_bpm_minus = findViewById(R.id.btn_bpm_minus);
        btn_bpm_add = findViewById(R.id.btn_bpm_add);

        startBtn_floatDown.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
                isStartBtnHidden = true;
            }

            public void onAnimationEnd(Animation animation) {
                runOnUiThread(() -> btn_start.setVisibility(View.GONE));
                isAnimating = false;
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        startBtn_floatUp.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
                isStartBtnHidden = false;
            }

            public void onAnimationEnd(Animation animation) {
                runOnUiThread(() -> btn_start.setVisibility(View.VISIBLE));
                isAnimating = false;
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        Thread floatBackThread = new Thread() {
            public void run() {
                try {
                    Log.d(TAG, "run: START");
                    sleep(3000);
                    if (isStartBtnHidden) {
                        runOnUiThread(() -> btn_start.startAnimation(startBtn_floatUp));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Log.d(TAG, "run: INTERRUPTED");
                }
            }
        };
        ScrollView scrollView = findViewById(R.id.sv_main);
        sv_main = scrollView;
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> new Thread() {
            public void run() {
                if (!isAnimating) {
                    runOnUiThread(() -> {
                                if (oldScrollY > scrollY) {
                                    if (isStartBtnHidden) {
                                        btn_start.startAnimation(startBtn_floatUp);
                                    }
                                } else if (!isStartBtnHidden) {
                                    btn_start.startAnimation(startBtn_floatDown);
                                    if (!floatBackThread.isInterrupted()) {
                                        floatBackThread.interrupt();
                                    }
                                    floatBackThread.start();
                                }
                            }
                    );
                }
            }
        }.start());
        btn_start.setOnLongClickListener(v -> {
            u.clear();
            Toast.makeText(this, "Reset OK", Toast.LENGTH_SHORT).show();
            updateSpeedAndSig();
            return true;
        });

        btn_bpm_minus.setOnClickListener(v -> {
            minusBpm(false, v);
            updateSpeedAndSig();
        });
        btn_bpm_add.setOnClickListener(v -> {
            addBpm(false, v);
            updateSpeedAndSig();
        });
        btn_bpm_add.setOnLongClickListener(v -> {
            addBpm(true, v);
            updateSpeedAndSig();
            return true;
        });
        btn_bpm_minus.setOnLongClickListener(v -> {
            minusBpm(true, v);
            updateSpeedAndSig();
            return true;
        });

        updateSpeedAndSig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSpeedAndSig();
    }

    private void updateSpeedAndSig() {
        tv_bpm.setText(String.valueOf(u.LAST_SPEED()));
        tv_time_sig.setText(String.format(Locale.getDefault(), "%d/%d", u.LAST_BEATAMOUNT(), u.LAST_BEATDURATION()));
    }

    private void switchSound() {
        switch (u.BEEP()) {
            case 0:
                u.setBEEP(1);
                return;
            case 1:
                u.setBEEP(2);
                return;
            case 2:
                u.setBEEP(0);
                return;
            default:
        }
    }

    public void addBpm(boolean is10x, View v) {
        if (is10x) {
            bpm += 10;
        } else {
            bpm++;
        }
        u.setLS(bpm);
        if (bpm > 240) {
            bpm = 240;
            u.setLS(240);
            Snackbar.make(v, R.string.sb_fastest, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void minusBpm(boolean is10x, View v) {
        if (is10x) {
            bpm -= 10;
        } else {
            bpm--;
        }
        u.setLS(bpm);
        if (bpm < 10) {
            bpm = 10;
            u.setLS(10);
            Snackbar.make(v, R.string.sb_slowest, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void setBeatDuration(int time) {
        BeatDuration = time;
        u.setLBD(time);
    }

    public void setBeatAmount(int time) {
        this.BeatAmount = time;
        u.setLBA(time);
    }

    private void SetSneaksyMsg(int sneaksyId) {
        // sneaksy strikes again
        View imNotFlanksy = findViewById(sneaksyId);
        imNotFlanksy.setOnTouchListener((imNotFlanksy1, event) -> {
            String msg;
            if (event.getAction() == MotionEvent.ACTION_UP) {
//                int i = verTouchCounter + 1; // ?
//                verTouchCounter = i; // wtf
                verTouchCounter++;
                if (verTouchCounter == 8) {
                    String fD = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
                    if (fD.equals("07-16")) {
                        // hardcoded love, immutable during lifetime
                        // really
                        //
                        // she's amazing
                        msg = "Happy Birthday!";
                    } else {
                        msg = config.sneaksy.get((int) (Math.random() * 10.0));
                    }
                    Snackbar.make(imNotFlanksy1, msg, Snackbar.LENGTH_SHORT).show();
                    verTouchCounter = 0;
                }
            }
            imNotFlanksy1.performClick(); // to avoid Android Studio's freaking warning
            return false;
        });
    }

    public void onStartBtnClick(View v) {
        startActivity(new Intent(this, CountActivity.class));
    }

    public void onSigBtnClick(View v) {
        startActivity(new Intent(this, SigActivity.class));
    }
}