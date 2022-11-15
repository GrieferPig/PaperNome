// WHY YOU OPENED THIS FILE? YOU'RE NOT SUPPOSED TO OPEN THIS! GET BACK!
// THERE IS DEFINITELY NO EASTER EGG HERE

package com.grieferpig.papergnome;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.Arrays;
import java.util.List;

/**
 * Author:GrieferPig
 * Last Modify: 10/19/2020
 * Version: 0.6.1
 *
 * This class has no functions but defines a bunch of variables
 * that can access anywhere in this project
 */

// You shouldn't read these lines. Really. It'll waste your precious time
final public class config {

    public static String settingStoragePath = "com.grieferpig.papernome";
    public static String settingStorageDefDataString = "";
    public static boolean settingStorageDefDataBoolean = false;
    public static int settingStorageDefDataInt = 0;
    public static float settingStorageDefDataFloat = 0;
    public static long settingStorageDefDataLong = 0;

    // have to be final to use in switches
    final public static int SOUND_HIT = 0;
    final public static int SOUND_SINE = 1;
    final public static int SOUND_DEF = 2;

    public static Sound[] sounds = new Sound[]{
            new Sound("hit", R.string.wooden_fish),
            new Sound("sine", R.string.sine),
            new Sound("def", R.string.trad_metronome),
    };

    // definitely not THE easter egg
    // don't even think about it
    // see it's just a list of SUSpicious strings what can it do
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
            "Try Pegasus Launcher!");
}

class Sound {
    @NonNull String storedName;
    @StringRes int idRes;

    public Sound(@NonNull String storedName, @StringRes int idRes){
        this.storedName = storedName;
        this.idRes = idRes;
    }
}