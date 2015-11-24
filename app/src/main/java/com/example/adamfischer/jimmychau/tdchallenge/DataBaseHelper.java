/*
Project: TD Challenge
Challenge: This particular challenge revolves around Social Media and Opportunities
           in Finance leveraging the mobile platform.
Coder: Jimmy Chau & Adam Fischer
Date: November 18, 2015
Course: INFO-5102 GUI Development
*/

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

        String testInsert = "INSERT INTO Users (ID,UserName,Password,FirstName,LastName,Email,Balance) VALUES " +
                "(1,'jchau','123456','Jimmy','Chau','jimmy_chau@hotmail.com',2500)," +
                "(2,'afischer','123456','Adam','Fischer','a_fischer@gmail.com',5000)," +
                "(3,'lwong','123456','Lianne','Wong','lwong@fanshaweonline.ca',30000);";
        _db.execSQL(testInsert);
        testInsert = "INSERT INTO Projects (ID,UserID,Name,Type,Blurb,Date,Goal,Donated) VALUES " +
                "(1,3,'Pulse','Technology','Stills. Timelapse. Video. Complete control of your DSLR or Mirrorless camera from your smartphone. Wirelessly.','2015-12-25',75000,74500)," +
                "(2,3,'G-RO','Technology','The world`s best carry-on bag - its patented \"all-terrain\" wheels change EVERYTHING!','2016-03-01',100000,10500)," +
                "(3,1,'MPL Macro','Technology','Take amazing close up pictures with your smart phone!','2015-12-25',15000,300)," +
                "(4,1,'UnoSWU','Food','Yummy','2016-01-07',200000,2000)," +
                "(5,1,'LUMA ACTIVE','Art','Fun','2015-12-25',100000,100)," +
                "(6,1,'WhatsUpp flock','Food','Yummy','2016-01-07',200000,2000)," +
                "(7,1,'Bubblz','category','blurb','2016-01-07',15000,2000)," +
                "(8,2,'grocerEstore','category','blurb','2016-01-07',15000,2000)," +
                "(9,2,'Mentor Connect Global','category','blurb','2016-01-07',15000,2000)," +
                "(10,2,'MathTutorial','category','blurb','2016-01-07',15000,2000)," +
                "(11,2,'WireWiz','category','blurb','2016-01-07',15000,2000)," +
                "(12,2,'Fraternitree','category','blurb','2016-01-07',15000,2000);";
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