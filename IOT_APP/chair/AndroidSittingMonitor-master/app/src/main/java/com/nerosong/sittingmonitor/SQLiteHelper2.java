package com.nerosong.sittingmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper2 extends SQLiteOpenHelper
{
    private static final String CREATE_CHAIR="create table chair2("
            + "id integer primary key autoincrement,"
            + "st2 string,"
            + "num2 string)";
    /*SQLiteHelper的四个参数，上下文，数据库名字，null,版本号（任意数字）*/
    public SQLiteHelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*上面那个太过复杂，所以需要重载一个简单的方法：通过构造方法，完成数据库的创建*/
    public SQLiteHelper2(Context context){
        super(context,"chairdb2.db",null,1);
    }

    /*通过OnCreate方法，实现数据表的创建*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHAIR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
