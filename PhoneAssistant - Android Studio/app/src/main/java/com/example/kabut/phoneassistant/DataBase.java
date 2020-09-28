package com.example.kabut.phoneassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "BazaDeDate";
  public static final String TABLE_NAME = "Friends";
  public static final String COL_1 = "Nume";
  public static final String COL_2 = "Number";
  public static final String COL_3 = "Email";



    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table  " + TABLE_NAME  + "(Nume TEXT , Number INTEGER, Email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
     onCreate(db);
    }

    public boolean InsertData(String nume , String numar , String email) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1,nume);
        contentValues.put(COL_2,numar);
        contentValues.put(COL_3,email);

       long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT * FROM Friends WHERE  Nume =\'" + name + "\'";
        Cursor res = db.rawQuery(q,null);
        return res;
    }

    public Cursor getEmail(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT Email FROM Friends WHERE  Nume =\'" + name + "\'";
        Cursor res = db.rawQuery(q,null);
        return res;
    }

    public Cursor getNumber(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT Number FROM Friends WHERE  Nume =\'" + name + "\'";
        Cursor res = db.rawQuery(q,null);
        return res;
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT * FROM Friends";
        Cursor res = db.rawQuery(q,null);
        return res;
    }

      public Integer Delete(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Friends","Nume = ?", new String[] {name});
        }

}
