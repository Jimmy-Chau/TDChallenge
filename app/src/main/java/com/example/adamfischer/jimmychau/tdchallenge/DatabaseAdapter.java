package com.example.adamfischer.jimmychau.tdchallenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 2;

    /* Table Names */
    static final String TABLE_USERS = "Users";
    static final String TABLE_PROJECTS = "Projects";

    /********* Column names *******/
    // Users
    static final String USERS_ID = "ID";
    static final String USERS_USERNAME = "Username";
    static final String USERS_PASSWORD = "Password";
    static final String USERS_FIRST_NAME = "FirstName";
    static final String USERS_LAST_NAME = "LastName";
    static final String USERS_EMAIL = "Email";
    // Projects
    static final String PROJECTS_ID = "ID";
    static final String PROJECTS_USER_ID = "UserID";
    static final String PROJECTS_NAME = "Name";
    static final String PROJECTS_TYPE = "Type";
    static final String PROJECTS_BLURB = "Blurb";
    static final String PROJECTS_DATE = "Date";
    static final String PROJECTS_GOAL = "Goal";

    // SQL Statement to create a new database.
//    static final String DATABASE_CREATE = "create table "+"LOGIN"+
//            "( " +"ID"+" integer primary key autoincrement,"
//                + "USERNAME text not null unique, PASSWORD text not null, FIRSTNAME text not null, LASTNAME text not null, EMAIL text);";

    static final String DATABASE_CREATE =
            "CREATE TABLE "+TABLE_USERS+" (" +
                USERS_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERS_USERNAME      + " TEXT NOT NULL UNIQUE," +
                USERS_PASSWORD      + " TEXT NOT NULL," +
                USERS_FIRST_NAME    + " TEXT NOT NULL," +
                USERS_LAST_NAME     + " TEXT NOT NULL," +
                USERS_EMAIL         + " TEXT" +
            ");" +
            "CREATE TABLE "+TABLE_PROJECTS+" (" +
                PROJECTS_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PROJECTS_USER_ID    + " INTEGER NOT NULL," +
                PROJECTS_NAME       + " TEXT NOT NULL UNIQUE," +
                PROJECTS_TYPE       + " TEXT NOT NULL," +
                PROJECTS_BLURB      + " TEXT," +
                PROJECTS_DATE       + " TEXT NOT NULL," +
                PROJECTS_GOAL       + " INTEGER NOT NULL," +
                "FOREIGN KEY ("+PROJECTS_USER_ID+") REFERENCES "+TABLE_USERS+"("+USERS_ID+")" +
            ");";

    // Variable to hold the database instance
    public SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;

    // Database open/upgrade helper
    private DataBaseHelper dbHelper;

    public DatabaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    /***********************************************************************************************
        User methods
     **********************************************************************************************/
    public long addUser(UserData user) {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.

        newValues.put(USERS_USERNAME, user.getUserName());
        newValues.put(USERS_PASSWORD, user.getPassword());
        newValues.put(USERS_FIRST_NAME, user.getFirstName());
        newValues.put(USERS_LAST_NAME, user.getLastName());
        newValues.put(USERS_EMAIL, user.getEmail());
        // Insert the row into your table
        return db.insert(TABLE_USERS, null, newValues);
    }

    public int deleteUser(String userName) {
        return db.delete(
                TABLE_USERS,
                USERS_USERNAME + "=?",
                new String[]{userName}
        );
    }

    /**
     *
     * @param userName User name to search for
     * @return UserData of row, null if not found
     */
    public UserData getUser(String userName) {
        Cursor cursor = db.query(
                TABLE_USERS,            // table
                null,                   // columns[]
                USERS_USERNAME+"=?",    // selection
                new String[]{userName}, // selectionArgs[]
                null,                   // groupBy
                null,                   // having
                null);                  // orderBy

        UserData user = null;
        if(cursor.getCount() > 0) { // UserName exists
            cursor.moveToFirst();

            long id = cursor.getLong(cursor.getColumnIndex(USERS_ID));
            String password = cursor.getString(cursor.getColumnIndex(USERS_PASSWORD));
            String firstName = cursor.getString(cursor.getColumnIndex(USERS_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(USERS_LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndex(USERS_EMAIL));

            user = new UserData(id, userName, password, firstName, lastName, email);
        }

        cursor.close();
        return user;
    }

    /**
     *
     * @param user UserData for user to update
     * @return number of rows affected,
     */
    public int updateUser(UserData user) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(USERS_USERNAME, user.getUserName());
        updatedValues.put(USERS_PASSWORD, user.getPassword());
        updatedValues.put(USERS_FIRST_NAME, user.getFirstName());
        updatedValues.put(USERS_LAST_NAME, user.getLastName());
        updatedValues.put(USERS_EMAIL, user.getEmail());

        String user_id = Long.toString(user.getID());
        return db.update(
                TABLE_USERS,
                updatedValues,
                USERS_ID + "=?",
                new String[] {user_id}
        );
    }

    /***********************************************************************************************
        Project methods
     **********************************************************************************************/
    public long addProject(ProjectData project) {
        ContentValues newValues = new ContentValues();

        newValues.put(PROJECTS_ID, project.getID());
        newValues.put(PROJECTS_USER_ID, project.getUserID());
        newValues.put(PROJECTS_NAME, project.getName());
        newValues.put(PROJECTS_TYPE, project.getType());
        newValues.put(PROJECTS_BLURB, project.getBlurb());
        newValues.put(PROJECTS_DATE, project.getDate());
        newValues.put(PROJECTS_GOAL, project.getGoal());

        return db.insert(TABLE_PROJECTS, null, newValues);
    }

    /**
     *
     * @param projectName Project name to search for
     * @return ProjectData of row, null if not found
     */
    public ProjectData getProject(String projectName) {
        Cursor cursor = db.query(
                TABLE_PROJECTS,         // table
                null,                   // columns[]
                PROJECTS_NAME+"=?",    // selection
                new String[]{projectName}, // selectionArgs[]
                null,                   // groupBy
                null,                   // having
                null);                  // orderBy

        ProjectData project = null;
        if(cursor.getCount() > 0) { // Project exists
            cursor.moveToFirst();

            long id = cursor.getLong(cursor.getColumnIndex(PROJECTS_ID));
            long userID = cursor.getLong(cursor.getColumnIndex(PROJECTS_USER_ID));
            String name = cursor.getString(cursor.getColumnIndex(PROJECTS_NAME));
            String type = cursor.getString(cursor.getColumnIndex(PROJECTS_TYPE));
            String blurb = cursor.getString(cursor.getColumnIndex(PROJECTS_BLURB));
            String date = cursor.getString(cursor.getColumnIndex(PROJECTS_DATE));
            long goal = cursor.getLong(cursor.getColumnIndex(PROJECTS_GOAL));

            project = new ProjectData(id, userID , name, type, blurb, date, goal);
        }

        cursor.close();
        return project;
    }
}