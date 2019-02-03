package com.example.rohan.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rohan on 9/5/18.
 */

public class databasehandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static  final String DATABASE_NAME = "pro.db";
    public static final String TABLE_NAME = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    public databasehandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="CREATE TABLE "+TABLE_NAME+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME+" TEXT"+ ");";
        sqLiteDatabase.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    public void add(product p){
        ContentValues c  = new ContentValues();
        c.put(COLUMN_NAME,p.getName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,c);
        db.close();
    }
    public void delete(String pname){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + "=\"" + pname + "\";");
    }
    public String display(){
        String s="";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE 1";
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("name"))!=null)
            {
                s+=c.getString(c.getColumnIndex("name"));
                s+="\n";
            }
            c.moveToNext();
        }
        db.close();
        return s;
    }
}
