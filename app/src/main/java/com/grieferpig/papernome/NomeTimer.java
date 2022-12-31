package com.grieferpig.papernome;

public class NomeTimer {
    public static float bpmToSecond(int bpm, int barTime) {
        return ((60.0f / ((float) bpm)) * 1000.0f) / (((float) barTime) / 4.0f);
    }
}
