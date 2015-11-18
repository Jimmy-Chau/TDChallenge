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
        _db.execSQL(DatabaseAdapter.CREATE_TABLE_PROJECTS);

        /***** !!!!!! DELETE BEFORE SUBMISSION  !!!!!! */
        System.err.println("REMEMBER TO DELETE ENTERING TEST DATA!!!!!");
        String testInsert = "INSERT INTO Users (ID,UserName,Password,FirstName,LastName,Email,Balance) VALUES (1,'a','a','a','a','a@a.a',0),(2,'b','b','b','b','b@b.b',0),(3,'c','c','c','c','c@c.c',0);";
        _db.execSQL(testInsert);
        testInsert = "INSERT INTO `Projects` (ID,UserID,Name,Type,Blurb,Date,Goal,Donated) VALUES (1,1,'My Project','Art','Fun','2015-12-25',100000,100),(2,1,'Bacon','Food','Yummy','2016-01-07',200000,2000),(3,2,'Their Project','Art','Fun','2015-12-25',100000,100),(4,2,'Their Bacon','Food','Yummy','2016-01-07',200000,2000),(5,3,'Their Other Project','Art','Fun','2015-12-25',100000,100),(6,3,'Their Other Bacon','Food','Yummy','2016-01-07',200000,2000);";
        _db.execSQL(testInsert);
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