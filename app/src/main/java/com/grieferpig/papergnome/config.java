package com.grieferpig.papergnome;

/**
 * Author:GrieferPig
 * Last Modify: 10/19/2020
 * Version: 0.6.1
 * <p>
 * This class has no functions but defines a bunch of variables
 * that can access anywhere in this project
 */

public class config {
    //config your repo Url here
    //public static String repoUrl = "http://test.xydia.papercraft.top/manifests";

    public static String settingStoragePath = "com.grieferpig.papernome";
    public static String settingStorageDefDataString = "";
    public static boolean settingStorageDefDataBoolean = false;
    public static int settingStorageDefDataInt = 0;
    public static float settingStorageDefDataFloat = 0;
    public static long settingStorageDefDataLong = 0;

    final public static int SOUND_HIT = 0;
    final public static int SOUND_SINE = 1;
    final public static int SOUND_SNAP = 2;

    public static String[][] sounds = new String[][]{
            {"hit", "木鱼声"},
            {"sine", "正弦波"},
            {"snap", "响指"}
    };
}
