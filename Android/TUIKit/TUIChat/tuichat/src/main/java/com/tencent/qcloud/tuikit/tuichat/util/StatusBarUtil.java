package com.tencent.qcloud.tuikit.tuichat.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

public class StatusBarUtil {

 public    static void setCustomStatusBar(@ColorInt int color, Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 方法1：直接设置状态栏颜色
            context.getWindow().setStatusBarColor(color);

            // 方法2：清除所有可能的主题覆盖
//            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            context. getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // 方法3：设置深色/浅色状态栏图标
           setStatusBarIconsColor(!isColorDark(color),context);

            // 方法4：强制重绘
            context. getWindow().getDecorView().invalidate();
        }
    }
    /**
     * 判断颜色是否为深色
     */
    public static boolean isColorDark(@ColorInt int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }


    // 强制设置状态栏图标颜色
    private static void setStatusBarIconsColor(boolean dark,Activity context) {
        View decorView =    context.getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (dark) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
        }

        decorView.setSystemUiVisibility(systemUiVisibility);
    }
}