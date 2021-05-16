package com.example.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {



    public DBHelper(Context context) {
        super(context,"UserTasks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table task(id INTEGER PRIMARY KEY AUTOINCREMENT, Task TEXT NOT NULL, Description TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists task");
    }

    public boolean insertUserTasks(String Task, String Description){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task", Task);
        contentValues.put("Description", Description);
        long result = DB.insert("task",null,contentValues);
        return result != -1;
    }
    public boolean updateUserTasks(String Task, String Description, String id){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task", Task);
        contentValues.put("Description", Description);
        long result = DB.update("task",contentValues,"id=?",new String[]{String.valueOf(id)});
        return result != -1;
    }
    public boolean deleteUserData(String id){
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("task", "id=?", new String[]{String.valueOf(id)});
        return result != -1;
    }
    public Cursor getAllData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("SELECT id, Task, Description FROM task", null);
    }
    public Cursor getInfo(String id){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("SELECT Task, Description FROM task WHERE id="+id, null);
    }
}