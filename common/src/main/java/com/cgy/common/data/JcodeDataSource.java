package com.cgy.common.data;

import android.support.annotation.NonNull;

import com.cgy.common.module.find.model.JcodeEntity;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/19  15:16
 */
public interface JcodeDataSource {
    interface LoadJcodesCallback {
        void onTasksLoaded(List<JcodeEntity> data);

        void onDataNotAvailable();
    }

    interface GetJcodeCallback {
        void onTaskLoaded(JcodeEntity entity);

        void onDataNotAvailable();
    }

    long saveJcode(@NonNull JcodeEntity entity);

    void deleteAllJcodes();

    int deleteJcode(@NonNull String title);

    void getJcodes(@NonNull LoadJcodesCallback callback);

    void getJcode(@NonNull String title, @NonNull GetJcodeCallback callback);

    void refreshJcodes(@NonNull JcodeEntity entity);
}
