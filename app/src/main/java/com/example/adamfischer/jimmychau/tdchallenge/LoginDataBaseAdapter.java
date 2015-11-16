package com.example.adamfischer.jimmychau.tdchallenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID"+" integer primary key autoincrement,"
                + "USERNAME text not null unique, PASSWORD text not null, FIRSTNAME text not null, LASTNAME text not null, EMAIL text);";
    // Variable to hold the database instance
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelper dbHelper;
    public  LoginDataBaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public  LoginDataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public long insertEntry(UserData user) {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.

        newValues.put("USERNAME", user.getUserName());
        newValues.put("PASSWORD", user.getPassword());
        newValues.put("FIRSTNAME", user.getFirstName());
        newValues.put("LASTNAME", user.getLastName());
        newValues.put("EMAIL", user.getEmail());

        // Insert the row into your table
        return db.insert("LOGIN", null, newValues);
    }

    public int deleteEntry(String UserName) {
        //String id=String.valueOf(ID);
        String where="USERNAME=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    /**
     *
     * @param userName User name to search for
     * @return UserData of row, null if not found
     */
    public UserData getSingleEntry(String userName) {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        UserData entry = null;

        if(cursor.getCount() > 0) { // UserName exists
            cursor.moveToFirst();

            long id = cursor.getLong(cursor.getColumnIndex("ID"));
            String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
            String firstName = cursor.getString(cursor.getColumnIndex("FIRSTNAME"));
            String lastName = cursor.getString(cursor.getColumnIndex("LASTNAME"));
            String email = cursor.getString(cursor.getColumnIndex("EMAIL"));

            entry = new UserData(id, userName, password, firstName, lastName, email);
        }

        cursor.close();
        return entry;
    }

    /**
     *
     * @param user
     * @return number of rows affected,
     */
    public int updateEntry(UserData user) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("USERNAME", user.getUserName());
        updatedValues.put("PASSWORD", user.getPassword());
        updatedValues.put("FIRSTNAME", user.getFirstName());
        updatedValues.put("LASTNAME", user.getLastName());
        updatedValues.put("EMAIL", user.getEmail());

        String where="ID=" + user.getID();
        return db.update("LOGIN",updatedValues, where, null);
    }
}