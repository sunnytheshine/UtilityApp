package com.app.utilityapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Database model to handle small cirle config
 * Created by sukhvindersingh on 2017-09-16.
 */

public class DBModel  extends SQLiteOpenHelper {

    public DBModel(Context applicationcontext) {
        super(applicationcontext, AppConfig.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS "+AppConfig.DB_TABLE+" (type VARCHAR,x VARCHAR,y VARCHAR, radius VARCHAR, h VARCHAR, w VARCHAR, name VARCHAR);";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS '"+AppConfig.DB_TABLE+"'";
        database.execSQL(query);
        onCreate(database);
    }
    public void insert(HashMap<String,String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", queryValues.get("type"));
        values.put("x", queryValues.get("x"));
        values.put("y", queryValues.get("y"));
        values.put("radius", queryValues.get("radius"));
        values.put("h", queryValues.get("h"));
        values.put("w", queryValues.get("w"));
        values.put("name", queryValues.get("name"));
        Log.d("insert", String.valueOf(queryValues));
        database.insert(AppConfig.DB_TABLE, null, values);
        database.close();
    }

    public Boolean select(String id) {
        Boolean wordList = false;
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+AppConfig.DB_TABLE+" where type='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            database.close();
            wordList = true;
        }
        database.close();
        return wordList;
    }

    public int update(HashMap <String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("x", queryValues.get("x"));
        values.put("y", queryValues.get("y"));
        values.put("radius", queryValues.get("radius"));
        values.put("h", queryValues.get("h"));
        values.put("w", queryValues.get("w"));
        Log.d("update", String.valueOf(queryValues));
        int res=database.update(AppConfig.DB_TABLE, values, "type" + " = ?", new String[] { queryValues.get("type") });
        database.close();
        return  res;

    }

    public HashMap getdata(String id) {
        HashMap wordList = new HashMap();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+AppConfig.DB_TABLE+" where type='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                wordList.put("type",cursor.getString(cursor.getColumnIndex("type")));
                wordList.put("x",cursor.getString(cursor.getColumnIndex("x")));
                wordList.put("y",cursor.getString(cursor.getColumnIndex("y")));
                wordList.put("radius",cursor.getString(cursor.getColumnIndex("radius")));
                wordList.put("w",cursor.getString(cursor.getColumnIndex("w")));
                wordList.put("h",cursor.getString(cursor.getColumnIndex("h")));
                wordList.put("name",cursor.getString(cursor.getColumnIndex("name")));

            } while (cursor.moveToNext());
            database.close();
        }
        database.close();
        return wordList;
    }

    public int update1(HashMap <String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", queryValues.get("type"));
        values.put("name", queryValues.get("name"));
        Log.d("update1", String.valueOf(queryValues));
        int res=database.update(AppConfig.DB_TABLE, values, "type=?", new String[] { queryValues.get("type") });
        database.close();
        return res;
    }
}