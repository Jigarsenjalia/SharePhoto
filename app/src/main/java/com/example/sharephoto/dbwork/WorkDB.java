package com.example.sharephoto.dbwork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Владислав on 25.01.2017.
 */

public class WorkDB {
    private SQLiteDatabase database;
    private DBHelperSharePhoto dbHelper;
    private Cursor data;
    public WorkDB(Context appContext) {

        this.dbHelper = new DBHelperSharePhoto(appContext);
        this.database = this.dbHelper.getWritableDatabase();
    }
    public Cursor getCursorHistory()
    {
         data = database.query(DBHelperSharePhoto.TBL_NAME_HISTORY,
                new String[]{DBHelperSharePhoto.CM_DATE_TIME,
                        DBHelperSharePhoto.CM_LINK,
                        DBHelperSharePhoto.CM_THUMP_URL,
                        DBHelperSharePhoto._ID}, null, null, null, null, DBHelperSharePhoto._ID + " DESC" );
        return data;
    }
    public void writePhotoDataToDB(String imgUrl, String thumbUrl)
    {
        ContentValues contentValues = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        contentValues.put(DBHelperSharePhoto.CM_DATE_TIME, dateFormat.format(date));
        contentValues.put(DBHelperSharePhoto.CM_LINK, imgUrl);
        contentValues.put(DBHelperSharePhoto.CM_THUMP_URL, thumbUrl);

        database.insert(DBHelperSharePhoto.TBL_NAME_HISTORY, null, contentValues);
    }
    public boolean isHasHistory()
    {
        Cursor cursorBool = database.query(true, DBHelperSharePhoto.TBL_NAME_HISTORY,
                new String[]{DBHelperSharePhoto._ID}, null, null, null, null, null, null);
        int count = cursorBool.getCount();
        cursorBool.close();
        if(count == 0)
        {
            return false;
        }
        return true;
    }
    public void closeAllConnections()
    {
        if (database != null)
        {
            database.close();
        }
        if(data != null)
        {
            database.close();
        }
    }
}
