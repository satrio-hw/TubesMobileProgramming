package com.example.my2020sms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my2020sms.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME ="draft";
    public static final String UID ="_id";
    public static final String RECIEVER = "reciever";
    public static final String BODY = "body";

    public  SQLiteHelper(@Nullable Context context){
        super(context, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"("+
                UID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                RECIEVER+" VARCHAR(255),"+
                BODY+" VARCHAR(500));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("LOG_TAG","Upgrading database from version "+
                i+" to "+i1+" which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
