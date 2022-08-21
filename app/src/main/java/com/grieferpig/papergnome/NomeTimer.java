package com.grieferpig.papergnome;

public class NomeTimer {
    public static float bpmToSecond(int bpm, int barTime){
        return (60/(float)bpm * 1000)/((float)barTime/4);
    }
}
