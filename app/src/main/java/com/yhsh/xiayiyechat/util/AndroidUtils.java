package com.yhsh.xiayiyechat.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.concurrent.Executors;

/**
 * @author xiayiye5
 * 所有Android中用到的工具类
 */
public class AndroidUtils {
    private AndroidUtils() {
    }

    private static AndroidUtils androidUtils = new AndroidUtils();

    public static AndroidUtils getInstance() {
        return androidUtils;
    }

    /**
     * 设置全屏的方法
     *
     * @param context 上下文
     */
    public void setFullScreen(Activity context) {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 开启单个线程池的方法
     *
     * @param runnable runnable
     */
    public void openThread(Runnable runnable) {
        Executors.newSingleThreadExecutor().execute(runnable);
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context Activity
     * @return 返回宽度
     */
    public int getScreenWidth(Activity context) {
        Point point = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context Activity
     * @return 返回宽度
     */
    public int getScreenHeight(Activity context) {
        Point point = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(point);
        return point.y;
    }

    /**
     * 检查App是否登录的方法
     *
     * @return 是否登录的结果
     */
    public boolean isLogin() {
//        return !AppConfig.getInstance().getUid().isEmpty() && !"-1".equals(AppConfig.getInstance().getUid());
        return false;
    }

    /**
     * 设置状态栏透明的方法Android>=api21
     *
     * @param activity 透明页面 配合fitsSystemWindows属性使用实现沉浸式状态栏
     */
    public void setTittleTransparent(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
