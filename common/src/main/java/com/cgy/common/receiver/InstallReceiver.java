package com.cgy.common.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.llf.basemodel.utils.LogUtil;

import java.io.File;

/**
 * Created by cgy
 * 2018/7/20  17:33
 *
 * 下载完成后的广播接收
 */
public class InstallReceiver extends BroadcastReceiver{

    private static String TAG = "InstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            LogUtil.d(TAG + "收到下载完成的广播");
            install(context);
        }
    }

    /**
     * 通过隐式意图调用系统安装程序安装Apk
     * 适配7.0
     * @param context
     *
     */
    public static void install(Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "xiuqu.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //由于没有在Activity环境下启动Activity 设置下面标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.cgy.common.provider666", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
