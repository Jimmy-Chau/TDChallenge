/*
Project: TD Challenge
Challenge: This particular challenge revolves around Social Media and Opportunities
           in Finance leveraging the mobile platform.
Coder: Jimmy Chau & Adam Fischer
Date: November 18, 2015
Course: INFO-5102 GUI Development
*/

package com.example.adamfischer.jimmychau.tdchallenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseAdapter {
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 2;

    /********* Tables names *******/
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
    static final String USERS_BALANCE = "Balance";

    // Projects
    static final String PROJECTS_ID = "ID";
    static final String PROJECTS_USER_ID = "UserID";
    static final String PROJECTS_NAME = "Name";
    static final String PROJECTS_TYPE = "Type";
    static final String PROJECTS_BLURB = "Blurb";
    static final String PROJECTS_DATE = "Date";
    static final String PROJECTS_GOAL = "Goal";
    static final String PROJECTS_DONATED = "Donated";

    // SQL Statement to create a new database.
//    static final String DATABASE_CREATE = "create table "+"LOGIN"+
//            "( " +"ID"+" integer primary key autoincrement,"
//                + "USERNAME text not null unique, PASSWORD text not null, FIRSTNAME text not null, LASTNAME text not null, EMAIL text);";

    static final String CREATE_TABLE_USERS =
            "CREATE TABLE "+TABLE_USERS+" (" +
                USERS_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERS_USERNAME      + " TEXT NOT NULL UNIQUE," +
                USERS_PASSWORD      + " TEXT NOT NULL," +
                USERS_FIRST_NAME    + " TEXT NOT NULL," +
                USERS_LAST_NAME     + " TEXT NOT NULL," +
                USERS_EMAIL         + " TEXT," +
                USERS_BALANCE       + " INTEGER NOT NULL" +
            ")";
     static final String CREATE_TABLE_PROJECTS =
             "CREATE TABLE "+TABLE_PROJECTS+" (" +
                PROJECTS_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PROJECTS_USER_ID    + " INTEGER NOT NULL," +
                PROJECTS_NAME       + " TEXT NOT NULL UNIQUE," +
                PROJECTS_TYPE       + " TEXT NOT NULL," +
                PROJECTS_BLURB      + " TEXT," +
                PROJECTS_DATE       + " TEXT NOT NULL," +
                PROJECTS_GOAL       + " INTEGER NOT NULL," +
                PROJECTS_DONATED    + " INTEGER NOT NULL," +
                "FOREIGN KEY ("+PROJECTS_USER_ID+") REFERENCES "+TABLE_USERS+"("+USERS_ID+")" +
            ")";

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
        newValues.put(USERS_BALANCE, user.getBalance());
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
                USERS_USERNAME + "=?",    // selection
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
            long balance = cursor.getLong(cursor.getColumnIndex(USERS_BALANCE));
            user = new UserData(id, userName, password, firstName, lastName, email, balance);
        }

        cursor.close();
        return user;
    }

    /**
     * Adds amount to user's balance.
     * @param depositAmount
     * @param user
     * @return user's new balance
     */
    public long depositAmountToUser(long depositAmount, UserData user) {
        if (depositAmount < 0
           && (Math.abs(depositAmount) > user.getBalance())
        ) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        long oldBalance = user.getBalance();
        long newBalance = user.getBalance() + depositAmount;

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(USERS_BALANCE, newBalance);

        String user_id = Long.toString(user.getID());
        int updatedRows = db.update(
                TABLE_USERS,
                updatedValues,
                USERS_ID + "=?",
                new String[]{user_id}
        );

        if (updatedRows == 1) {
            return newBalance;
        }

        return oldBalance;
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
        updatedValues.put(USERS_BALANCE, user.getBalance());

        String user_id = Long.toString(user.getID());
        return db.update(
                TABLE_USERS,
                updatedValues,
                USERS_ID + "=?",
                new String[]{user_id}
        );
    }

    /***********************************************************************************************
        Project methods
     **********************************************************************************************/

    /**
     *
     * @param fromUser
     * @param toProject
     * @param amount
     * @return long[2] where index 0 is new user balance and index 1 is new project balance
     */
    public long[] donate(UserData fromUser, ProjectData toProject, long amount) {
        if (amount < 0 || amount > fromUser.getBalance()) {
            throw new IllegalArgumentException("Donation amount invalid0");
        }

        final int NEW_USER_BAL_IDX = 0;
        final int NEW_PROJ_BAL_IDX = 1;
        long newBalances[] = {-1, -1};

        db.beginTransaction();
        try {
            newBalances[NEW_USER_BAL_IDX] = depositAmountToUser( (-amount), fromUser);

            long oldProjectBalance = toProject.getDonated();
            long newProjectBalance = oldProjectBalance + amount;

            ContentValues updatedProjectValues = new ContentValues();
            updatedProjectValues.put(PROJECTS_DONATED, newProjectBalance);

            String project_id = Long.toString(toProject.getID());
            int updatedRows = db.update(
                    TABLE_PROJECTS,
                    updatedProjectValues,
                    PROJECTS_ID + "=?",
                    new String[]{project_id}
            );

            if (updatedRows == 1) {
                newBalances[NEW_PROJ_BAL_IDX] = newProjectBalance;
            } else {
                newBalances[NEW_PROJ_BAL_IDX] = oldProjectBalance;
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return newBalances;
    }

    public long addProject(ProjectData project) {
        ContentValues newValues = new ContentValues();

        newValues.put(PROJECTS_USER_ID, project.getUserID());
        newValues.put(PROJECTS_NAME, project.getName());
        newValues.put(PROJECTS_TYPE, project.getType());
        newValues.put(PROJECTS_BLURB, project.getBlurb());
        newValues.put(PROJECTS_DATE, project.getDate());
        newValues.put(PROJECTS_GOAL, project.getGoal());
        newValues.put(PROJECTS_DONATED, project.getDonated());

        return db.insert(TABLE_PROJECTS, null, newValues);
    }

    public ArrayList<ProjectData> getProjectsFor(long userID) {
        Cursor cursor = db.query(TABLE_PROJECTS, null, PROJECTS_USER_ID+"="+userID, null, null, null, null);

        ArrayList<ProjectData> projects = new ArrayList<>();

        if(cursor.getCount() > 0) { // Projects exists for user
            while (cursor.moveToNext()) {
                projects.add(projectDataFromCursor(cursor));
            }
        }

        cursor.close();
        return projects;
    }

    /**
     *
     * @param excludeUserID the id to exclude when retrieving projects
     * @return
     */
    public ArrayList<ProjectData> getOtherProjects(long excludeUserID) {
        String userIDStr = Long.toString(excludeUserID);
        Cursor cursor = db.query(
                TABLE_PROJECTS,             // table
                null,                    // columns[]
                PROJECTS_USER_ID+"!=?",  // selection
                new String[]{userIDStr}, // selectionArgs[]
                null,           // groupBy
                null,                    // having
                PROJECTS_DATE,           // orderBy
                "10");                   // limit

        ArrayList<ProjectData> projects = new ArrayList<>();
        if(cursor.getCount() > 0) { // Projects exists for user
            while (cursor.moveToNext()) {
                projects.add(projectDataFromCursor(cursor));
            }
        }

        cursor.close();
        return projects;
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
            project = projectDataFromCursor(cursor);
        }

        cursor.close();
        return project;
    }

    /**
     * Helper method to create ProjectData instance from Cursor object data
     * @param cursor
     * @return
     */
    private ProjectData projectDataFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(PROJECTS_ID));
        long userID = cursor.getLong(cursor.getColumnIndex(PROJECTS_USER_ID));
        String name = cursor.getString(cursor.getColumnIndex(PROJECTS_NAME));
        String type = cursor.getString(cursor.getColumnIndex(PROJECTS_TYPE));
        String blurb = cursor.getString(cursor.getColumnIndex(PROJECTS_BLURB));
        String date = cursor.getString(cursor.getColumnIndex(PROJECTS_DATE));
        long goal = cursor.getLong(cursor.getColumnIndex(PROJECTS_GOAL));
        long donated = cursor.getLong(cursor.getColumnIndex(PROJECTS_DONATED));

        return new ProjectData(id, userID , name, type, blurb, date, goal, donated);
    }
    
    public int deleteProject(String projectName) {
        return db.delete(
                TABLE_PROJECTS,
                PROJECTS_NAME + "=?",
                new String[]{projectName}
        );
    }

    public int updateProject(ProjectData project) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(PROJECTS_NAME, project.getName());
        updatedValues.put(PROJECTS_BLURB, project.getBlurb());
        updatedValues.put(PROJECTS_DATE, project.getDate());
        updatedValues.put(PROJECTS_GOAL, project.getGoal());
        updatedValues.put(PROJECTS_TYPE, project.getType());
        updatedValues.put(PROJECTS_USER_ID, project.getUserID());
        updatedValues.put(PROJECTS_DONATED, project.getDonated());

        String project_id = Long.toString(project.getID());
        return db.update(
                TABLE_PROJECTS,
                updatedValues,
                PROJECTS_ID + "=?",
                new String[]{project_id}
        );
    }
}