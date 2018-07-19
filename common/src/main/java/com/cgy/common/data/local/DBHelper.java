package com.cgy.common.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cgy
 * 2018/7/19  15:20
 * <p>
 * 数据库创建工具类
 * 修改表需要更新数据库版本
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "common.db";

    private String CREATE_TABLE = "create table jcode(id integer primary key autoincrement, imgUrl text, title VARCHAR(100), detailUrl VARCHAR(100), content text, author VARCHAR(20), authorImg VARCHAR(100), watch VARCHAR(10), comments VARCHAR(10), hobby VARCHAR(10))";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
    }
}
