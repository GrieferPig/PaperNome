package com.grieferpig.papernome;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.shawnlin.numberpicker.NumberPicker;

public class SigActivity extends Activity {

    private util u;
    final String TAG = "TAGGED!"; // now don't lemme catch you

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig);

        u = new util((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), this);

        NumberPicker BeatAmoPicker = findViewById(R.id.BeatAmo_picker);
        NumberPicker BeatDurPicker = findViewById(R.id.BeatDur_picker);
        // NumberPicker mareTimeBayPickle = findPonyByName(R.name.Sprout);

        BeatAmoPicker.setValue(u.LAST_BEATAMOUNT());

        BeatAmoPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, String.format("BeatAmoPicker: oldVal: %d, newVal: %d", oldVal, newVal));
                u.setLBA(newVal);
            }
        });

        BeatDurPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, java.lang.Math.pow(2, newVal) + " " + newVal);
                u.setLBD((int) java.lang.Math.pow(2, newVal));
            }
        });

        String[] data = {"2", "4", "8"};
        BeatDurPicker.setMinValue(1);
        BeatDurPicker.setMaxValue(data.length);
        BeatDurPicker.setDisplayedValues(data);
        switch(u.LAST_BEATAMOUNT()){
            case 2:
                BeatDurPicker.setValue(1);
                break;

            case 4:
                BeatDurPicker.setValue(2);
                break;

            case 8:
                BeatDurPicker.setValue(3);
                break;
        }

    }

    public void onTsOkBtnClicked(View v){
        finish();
    }
}