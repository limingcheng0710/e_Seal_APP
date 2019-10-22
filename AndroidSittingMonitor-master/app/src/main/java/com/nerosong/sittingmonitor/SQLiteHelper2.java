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

//        db.execSQL("insert into records values('与1号公司的合同','员工1号','领导1号','2019/10/1','是')");
//        db.execSQL("insert into records values('与2号公司的合同','员工2号','领导2号','2019/10/1','是')");
//        db.execSQL("insert into records values('与3号公司的合同','员工3号','领导3号','2019/10/1','是')");
//        db.execSQL("insert into records values('与4号公司的合同','员工4号','领导4号','2019/10/1','是')");
//        db.execSQL("insert into records values('与5号公司的合同','员工5号','领导5号','2019/10/1','是')");
//        db.execSQL("insert into records values('与6号公司的合同','员工6号','领导6号','2019/10/1','是')");
//        db.execSQL("insert into records values('与7号公司的合同','员工7号','领导7号','2019/10/1','是')");
//        db.execSQL("insert into records values('与8号公司的合同','员工8号','领导8号','2019/10/1','是')");
//        db.execSQL("insert into records values('与9号公司的合同','员工9号','领导9号','2019/10/1','是')");
//        db.execSQL("insert into records values('与10号公司的合同','员工10号','领导10号','2019/10/1','是')");
//        db.execSQL("insert into records values('与11号公司的合同','员工11号','领导11号','2019/10/2','是')");
//        db.execSQL("insert into records values('与12号公司的合同','员工12号','领导12号','2019/10/2','是')");
//        db.execSQL("insert into records values('与13号公司的合同','员工13号','领导13号','2019/10/2','是')");
//        db.execSQL("insert into records values('与14号公司的合同','员工14号','领导14号','2019/10/2','是')");
//        db.execSQL("insert into records values('与15号公司的合同','员工15号','领导15号','2019/10/2','是')");
//        db.execSQL("insert into records values('与16号公司的合同','员工16号','领导16号','2019/10/2','是')");
//        db.execSQL("insert into records values('与17号公司的合同','员工17号','领导17号','2019/10/2','是')");
//        db.execSQL("insert into records values('与18号公司的合同','员工18号','领导18号','2019/10/2','是')");
//        db.execSQL("insert into records values('与19号公司的合同','员工19号','领导19号','2019/10/2','是')");
//        db.execSQL("insert into records values('与20号公司的合同','员工20号','领导20号','2019/10/2','是')");
//        db.execSQL("insert into records values('与21号公司的合同','员工21号','领导21号','2019/10/2','是')");
//        db.execSQL("insert into records values('与22号公司的合同','员工22号','领导22号','2019/10/2','是')");
//        db.execSQL("insert into records values('与23号公司的合同','员工23号','领导23号','2019/10/2','是')");
//        db.execSQL("insert into records values('与24号公司的合同','员工24号','领导24号','2019/10/2','是')");
//        db.execSQL("insert into records values('与25号公司的合同','员工25号','领导25号','2019/10/2','是')");
//        db.execSQL("insert into records values('与26号公司的合同','员工26号','领导26号','2019/10/2','是')");
//        db.execSQL("insert into records values('与27号公司的合同','员工27号','领导27号','2019/10/2','是')");
//        db.execSQL("insert into records values('与28号公司的合同','员工28号','领导28号','2019/10/2','是')");
//        db.execSQL("insert into records values('与29号公司的合同','员工29号','领导29号','2019/10/2','是')");
        db.execSQL("insert into records values('与30号公司的合同','员工30号','领导30号','2019/10/2','是')");

        db.execSQL("insert into records values('玉玺密制合同','员工','老板','2019/10/1','是')");
        db.execSQL("insert into records values('玉玺密制合同','冒充员工','老板','2019/10/1','是')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
