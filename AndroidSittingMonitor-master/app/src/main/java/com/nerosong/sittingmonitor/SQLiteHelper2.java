package com.nerosong.sittingmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper2 extends SQLiteOpenHelper {
    /*SQLiteHelper的四个参数，上下文，数据库名字，null,版本号（任意数字）*/
    public SQLiteHelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*上面那个太过复杂，所以需要重载一个简单的方法：通过构造方法，完成数据库的创建*/
    public SQLiteHelper2(Context context){
        super(context,"recorddb",null,1);
    }

    /*通过OnCreate方法，实现数据表的创建*/
    @Override
    public void onCreate(SQLiteDatabase db) {
//        private String fileName;
//        private String applicant;
//        private String approver;
//        private String date;
//        private String if_Right;


        db.execSQL("create table records(filename varchar2(30) ,applicant varchar2(10),approver varchar2(10),date varchar2(20),if_Right varchar2(10))");

        db.execSQL("insert into records values('与一号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('与二号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('与三号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('与四号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('与五号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('与六号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('与七号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('与八号公司的合同','员工一号','领导一号','2019/10/1','是')");
        db.execSQL("insert into records values('1','员工一号','领导一号','2019/10/1','是')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
