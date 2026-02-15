package dev.onethecrazy.util;

public class ColorUtil {
    public static int whiteWithAlpha(float alpha0to1){
        return Math.round(255 * alpha0to1) << 24 | 0xFFFFFF;
    }
}
