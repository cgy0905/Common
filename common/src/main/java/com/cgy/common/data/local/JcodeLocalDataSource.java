package com.cgy.common.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.cgy.common.data.JcodeDataSource;
import com.cgy.common.module.find.model.JcodeEntity;
import com.llf.basemodel.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by cgy
 * 2018/7/19  15:15
 * 发现数据库操作工具类
 */
public class JcodeLocalDataSource implements JcodeDataSource{
    private DBHelper sqliteOpenHelper;
    private static JcodeLocalDataSource INSTANCE;

    private JcodeLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        sqliteOpenHelper = new DBHelper(context);
    }

    public static JcodeLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new JcodeLocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * 增加事务 保证数据的安全性 两步操作有一步不正确可以回滚
     *
     * @param entity
     * @return
     */
    @Override
    public long saveJcode(@NonNull JcodeEntity entity) {
        long rowId = 0;
        checkNotNull(entity);
        SQLiteDatabase database = sqliteOpenHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("imgUrl", entity.getImgUrl());
            values.put("title", entity.getTitle());
            values.put("detailUrl", entity.getDetailUrl());
            values.put("content", entity.getContent());
            values.put("author", entity.getAuthor());
            values.put("authorImg", entity.getAuthorImg());
            values.put("watch", entity.getWatch());
            values.put("comments", entity.getComments());
            values.put("hobby", entity.getLike());
            rowId = database.insert("jcode", "imgUrl", values);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.d("插入数据库出错");
        } finally {
            database.endTransaction();
            database.close();
        }
        return rowId;
    }

    /**
     * 删除一条记录
     * 返回值是受影响的行数 -1表示失败
     * @param title
     * @return
     */
    @Override
    public int deleteJcode(@NonNull String title) {
        checkNotNull(title);
        SQLiteDatabase database = sqliteOpenHelper.getWritableDatabase();
        int deleteResult = database.delete("jcode", "title=?", new String[]{title});
        database.close();
        return deleteResult;
    }

    @Override
    public void deleteAllJcodes() {
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        db.delete("jcode", null, null);
        db.close();
    }

    @Override
    public void getJcodes(@NonNull LoadJcodesCallback callback) {
        List<JcodeEntity> data = new ArrayList<>();
        SQLiteDatabase readableDatabase = sqliteOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from jcode", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                JcodeEntity entity = new JcodeEntity();
                entity.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
                entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                entity.setDetailUrl(cursor.getString(cursor.getColumnIndex("detailUrl")));
                entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
                entity.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                entity.setAuthorImg(cursor.getString(cursor.getColumnIndex("authorImg")));
                entity.setWatch(cursor.getString(cursor.getColumnIndex("watch")));
                entity.setComments(cursor.getString(cursor.getColumnIndex("comments")));
                entity.setLike(cursor.getString(cursor.getColumnIndex("hobby")));
                data.add(entity);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        readableDatabase.close();
        if (data.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(data);
        }
    }

    @Override
    public void getJcode(@NonNull String title, @NonNull GetJcodeCallback callback) {
        JcodeEntity entity = null;
        SQLiteDatabase readableDatabase = sqliteOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query("jcode", new String[]{"title", "detailUrl", "content"}, "title=?", new String[]{title}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToNext()) {
                entity = new JcodeEntity();
                entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                entity.setDetailUrl(cursor.getString(cursor.getColumnIndex("detailUrl")));
                entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        readableDatabase.close();
        if (entity != null) {
            callback.onTaskLoaded(entity);
        } else {
            callback.onDataNotAvailable();
        }

    }

    @Override
    public void refreshJcodes(@NonNull JcodeEntity entity) {
        checkNotNull(entity);
        SQLiteDatabase sqLiteDatabase = sqliteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("imgUrl", entity.getImgUrl());
        values.put("title", entity.getTitle());
        values.put("detailUrl", entity.getDetailUrl());
        values.put("content", entity.getContent());
        values.put("author", entity.getAuthor());
        values.put("authorImg", entity.getAuthorImg());
        values.put("watch", entity.getWatch());
        values.put("comments", entity.getComments());
        values.put("hobby", entity.getLike());

        sqLiteDatabase.update("jcode", values, "id=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }
}
