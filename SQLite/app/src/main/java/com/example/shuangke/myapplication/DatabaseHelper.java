package com.example.shuangke.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shuangke on 6/22/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Student.db";//the student.db is not uppercase or lowercase sensetive
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "MARKS";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);//super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //exectute what query you pass inside of this method
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String name, String surname, String marks){
        SQLiteDatabase db = this.getWritableDatabase();//going to create your database and table
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }
}
