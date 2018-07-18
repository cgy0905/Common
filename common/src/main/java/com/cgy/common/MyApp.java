package com.cgy.common;

import android.os.Build;
import android.os.StrictMode;

import com.amitshekhar.DebugDB;
import com.bumptech.glide.Glide;
import com.llf.basemodel.base.BaseApplication;
import com.llf.basemodel.utils.AppInfoUtil;

/**
 * Created by cgy
 * 2018/7/18  15:32
 */
public class MyApp extends BaseApplication {
    //用于判断应用是否被强杀
    public static int appStatus = -1;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 解决7.0无法使用file://格式的URI的第二种方法
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        if (AppInfoUtil.isApkInDebug(this)) {
            DebugDB.getAddressLog();
        }
    }

    /**
     * 内存紧张时会走这个方法
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
        //1.清理内存中的图片
        //2.清理掉Activity只保留Root Activity
        switch (level) {
            case TRIM_MEMORY_COMPLETE:
                //表示 App 退出到后台 并且已经处于LRU List比较靠前的位置
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL:
                //表示 App 正在正常运行，但是系统已经开始根据 LRU List 的缓存规则杀掉了一部分缓存的进程
                break;
            case TRIM_MEMORY_UI_HIDDEN:
                Glide.get(this).clearMemory();
                break;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
}
