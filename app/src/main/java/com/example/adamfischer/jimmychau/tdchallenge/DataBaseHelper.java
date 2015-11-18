package com.example.adamfischer.jimmychau.tdchallenge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase _db) {

        Log.i(this.getClass().toString() + ".onCreate", "Creating table " + DatabaseAdapter.TABLE_USERS);
        _db.execSQL(DatabaseAdapter.CREATE_TABLE_USERS);


        /***** !!!!!! DELETE BEFORE SUBMISSION  !!!!!! */
        System.err.println("REMEMBER TO DELETE ENTERING TEST DATA!!!!!");
        String UserInsert = "INSERT INTO Users (ID,UserName,Password,FirstName,LastName,Email) VALUES (1,'a','a','a','a','a@a.a'),(2,'b','b','b','b','b@b.b'),(3,'c','c','c','c','c@c.c');";
        _db.execSQL(UserInsert);

        _db.execSQL(DatabaseAdapter.CREATE_TABLE_PROJECTS);
    }
    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");

        // on upgrade drop older tables
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter.TABLE_PROJECTS);
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter.TABLE_USERS);

        // create new tables
        onCreate(_db);
    }

}