package com.example.shuangke.emotiondetection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shuangke on 6/22/2017.
 */

public class DataHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Emotion.db";//the student.db is not uppercase or lowercase sensetive
    public static final String TABLE_NAME = "emotion_score_table";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "TIME";
    public static final String COL_2 = "SMILE";
    public static final String COL_3 = "JOY";
    public static final String COL_4 = "SADNESS";
    public static final String COL_5 = "SURPRISE";
    public static final String COL_6 = "ANGER";
    public static final String COL_7 = "CONTEMPT";
    public static final String COL_8 = "DISGUST";
    public static final String COL_9 = "ENGAGEMENT";
    public static final String COL_10 = "FEAR";
    public static final String COL_11 = "VALENCE";
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT ,TIME TEXT TEXT,SMILE TEXT,JOY TEXT," +
                "SADNESS TEXT,SURPRISE TEXT,ANGER TEXT,CONTEMPT TEXT,DISGUST TEXT,ENGAGEMENT TEXT," +
                "FEAR TEXT,VALENCE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String time,String smile, String joy, String sadness, String surprise,String anger,
                              String contempt,String disgust,String engagement,String fear,String valence){
        SQLiteDatabase db = this.getWritableDatabase();//going to create your database and table
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1,time);
        contentValues.put(COL_2,smile);
        contentValues.put(COL_3,joy);
        contentValues.put(COL_4,sadness);
        contentValues.put(COL_5,surprise);
        contentValues.put(COL_6,anger);
        contentValues.put(COL_7,contempt);
        contentValues.put(COL_8,disgust);
        contentValues.put(COL_9,engagement);
        contentValues.put(COL_10,fear);
        contentValues.put(COL_11,valence);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }
}
