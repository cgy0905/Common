package com.cgy.common.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by cgy
 * 2018/7/20  17:47
 *
 *
 * 上传多张图片的service
 * http://blog.csdn.net/lmj623565791/article/details/47143563
 */
public class UploadImgService extends IntentService{

    private static final String ACTION_UPLOAD_IMG = "com.cgy.xiuqu.intentservice.action.UPLOAD_IMAGE";
    public static final String EXTRA_IMG_PATH = "com.cgy.xiuqu.intentservice.extra.IMG_PATH";
    public static final String UPLOAD_RESULT = "com.zhy.xiuqu.intentservice.UPLOAD_RESULT";

    public UploadImgService() {
        super("UploadImgService");
    }

    public static void startUploadImg(Context context, String path) {
        Intent intent = new Intent(context, UploadImgService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, path);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String action = intent.getAction();
        if (ACTION_UPLOAD_IMG.equals(action)) {
            final String path = intent.getStringExtra(EXTRA_IMG_PATH);
            handleUploadImg(path);
        }
    }

    private void handleUploadImg(String path) {
        /**
         * 1.开始做上传图片的耗时操作
         * 2.把结果返回 可以用广播接收
         */
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(UPLOAD_RESULT);
        intent.putExtra(EXTRA_IMG_PATH, path);
        sendBroadcast(intent);
    }
}
