package com.davelabs.wakemehome.persistence.db;

import com.davelabs.wakemehome.persistence.db.DBSearchedLocationStoreContract.SearchedLocationContract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSearchedLocationStoreDBHelper extends SQLiteOpenHelper {
	  // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "searchedLocations.db";
    
    public static final String SQL_CREATE_TABLE = 
    		"CREATE TABLE " + SearchedLocationContract.TABLE_NAME + " (" +
    		SearchedLocationContract._ID + " INTEGER PRIMARY KEY, " +
    		SearchedLocationContract.COLUMN_NAME_SEARCH_QUERY + " TEXT, " +
    		SearchedLocationContract.COLUMN_NAME_LAT + " REAL, " +
    		SearchedLocationContract.COLUMN_NAME_LNG + " REAL, " +
    		SearchedLocationContract.COLUMN_NAME_IS_HOME + " INTEGER, " +
    		SearchedLocationContract.COLUMN_NAME_IS_PINNED + " INTEGER" +
    		" )";

    public DBSearchedLocationStoreDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not worrying about this yet
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}