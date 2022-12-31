package com.grieferpig.papernome;

import java.util.Arrays;
import java.util.List;

public final class config {
    public static final int SOUND_DEF = 2;
    public static final int SOUND_HIT = 0;
    public static final int SOUND_SINE = 1;
    public static boolean settingStorageDefDataBoolean = false;
    public static float settingStorageDefDataFloat = 0.0f;
    public static int settingStorageDefDataInt = 0;
    public static long settingStorageDefDataLong = 0;
    public static String settingStorageDefDataString = "";
    public static String settingStoragePath = BuildConfig.APPLICATION_ID;
    public static List<String> sneaksy = Arrays.asList(
            "You found a nothing!",
            "Me@Github!",
            "Very Buggy!",
            "Izzy moOOonbow!",
            "Never gonna give you up!",
            "Friendship is Magic!",
            "Based!",
            "Me@Bilibili!",
            "Try YeetOS!",
            "Try Pegasus Launcher!"
    );
    public static Sound[] sounds = {
            new Sound("hit", R.string.wooden_fish),
            new Sound("sine", R.string.sine),
            new Sound("def", R.string.trad_metronome)
    };
}

class Sound {
    int idRes;
    String storedName;

    public Sound(String storedName2, int idRes2) {
        this.storedName = storedName2;
        this.idRes = idRes2;
    }
}