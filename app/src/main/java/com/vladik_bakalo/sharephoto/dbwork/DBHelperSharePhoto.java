package com.vladik_bakalo.sharephoto.dbwork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Владислав on 23.01.2017.
 */

public class DBHelperSharePhoto extends SQLiteOpenHelper implements BaseColumns {
    public static final String DB_NAME = "share_photo_db";
    public static final int DB_VERSION = 1;
    //Tables
    public static final String TBL_NAME_HISTORY = "HISTORY";
    //Columns
    public static final String CM_LINK = "LINK";
    public static final String CM_DATE_TIME = "DATE_TIME";
    public static final String CM_THUMP_URL = "THUMB_URL";

    public DBHelperSharePhoto(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDataBase(db, 0, DB_VERSION);
    }
    public void updateMyDataBase(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion < newVersion)
        {
            db.execSQL("CREATE TABLE " + TBL_NAME_HISTORY + "("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CM_LINK + " TEXT, "
                    + CM_DATE_TIME + " TEXT, "
                    + CM_THUMP_URL  + " TEXT);");
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDataBase(db, oldVersion, newVersion);
    }
}
