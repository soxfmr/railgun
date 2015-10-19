package com.soxfmr.railgun.utils;

public class Args {

    public static boolean isEmpty(String arg0) {
        return arg0 == null || arg0.length() <= 0;
    }

    public static String capitalize(String arg0) {
        if (Args.isEmpty(arg0))
            return arg0;

        char upper = Character.toUpperCase(arg0.charAt(0));
        return arg0.length() > 1 ? upper + arg0.substring(1) : upper + "";
    }

    public static boolean intToBool(int i) {
        return i > 0;
    }

    public static int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

}
