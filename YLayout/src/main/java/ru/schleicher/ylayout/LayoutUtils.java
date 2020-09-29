package ru.schleicher.ylayout;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.DisplayMetrics;

public class LayoutUtils {

    private static final String TAG = "layout:";
    private static DisplayMetrics displayMetrics; // cache displaymetrics

    private static DisplayMetrics assureDisplayMetrics(){
        if (displayMetrics != null) {
            return displayMetrics;
        }
        try {
            displayMetrics = LayoutUtils.getApplicationUsingReflection().getResources().getDisplayMetrics();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return displayMetrics;
    }

    public static int hairline() {

        int gap = 1;
        try {
            DisplayMetrics metrics = assureDisplayMetrics();
            if (metrics.ydpi > 350) {
                gap = 2;
            }
        } catch (Exception e) {
            //
        }
        return gap;
    }

    /**
     * <p>Scales dp pixel to sp</p>
     * <p>Scale-independent Pixels - this is like the dp unit, but it is also scaled by the user's font size preference.</p>
     * <p>It is recommended you use this unit when specifying font sizes, so they will be adjusted for both the screen density and user's preference.</p>
     *
     * @param px set the dp size
     * @return sp size
     */
    public static int sp(int px) {
        return (int) (dp(px) / assureDisplayMetrics().scaledDensity);
    }

    /**
     * <p>convert int to dp</p>
     * @param px of the element
     * @return converted dp
     */
    public static int dp(int px) {
        float scale = assureDisplayMetrics().density;
        return (int) (px * scale);
    }

    @SuppressLint("PrivateApi")
    public static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
    }

}
