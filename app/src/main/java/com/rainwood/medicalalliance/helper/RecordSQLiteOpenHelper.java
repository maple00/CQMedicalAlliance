package com.rainwood.medicalalliance.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Author: a797s
 * @Date: 2019/12/5 12:00
 * @Desc: 历史记录查询DBHelper
 */

public class RecordSQLiteOpenHelper  extends SQLiteOpenHelper {

    // 搜索框历史记录
    private static String name = "searchHistory.db";
    private static Integer version = 1;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //打开数据库，建立了一个叫records的表，里面只有一列name来存储历史记录：
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
