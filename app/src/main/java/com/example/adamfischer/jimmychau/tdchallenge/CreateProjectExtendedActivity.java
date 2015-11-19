/*
Project: TD Challenge
Challenge: This particular challenge revolves around Social Media and Opportunities
           in Finance leveraging the mobile platform.
Coder: Jimmy Chau & Adam Fischer
Date: November 18, 2015
Course: INFO-5102 GUI Development
*/

package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateProjectExtendedActivity extends Activity {

    int cType;
    String cName;
    long userID;
    EditText projectTitle, txtBlurb, txtDuration, txtGoal;
    Spinner tSpinner;
    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project_extended);

        userID = getIntent().getExtras().getLong("userID");
        Log.d("create2", "" + userID);

        // create a instance of SQLite Database
        databaseAdapter = new DatabaseAdapter(this).open();

        cType = getIntent().getExtras().getInt("sType");
        cName = getIntent().getExtras().getString("sName");

        projectTitle = (EditText) findViewById(R.id.projectTitleEditText);

        projectTitle.setText(cName);

        tSpinner = (Spinner) findViewById(R.id.typeSpinner);

        tSpinner.setSelection(cType);

        //other view refs
        txtBlurb = (EditText) findViewById(R.id.blurbEditText);
        txtDuration = (EditText) findViewById(R.id.durationEditText);
        txtGoal = (EditText) findViewById(R.id.goalEditText);

        //Toast.makeText(CreateProjectExtendedActivity.this, cType + " " + cName, Toast.LENGTH_LONG).show();
    }

    public void onStart() {
        super.onStart();

        EditText txtDate = (EditText) findViewById(R.id.durationEditText);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    DateDialog dialog = new DateDialog(view);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_project_extended, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cancelOnClick(View view) {
        finish();
    }

    public void onSaveClick(View view) {

        String name = projectTitle.getText().toString();
        String type = tSpinner.getSelectedItem().toString();// getItemAtPosition(position).toString()
        String blurb = txtBlurb.getText().toString();
        String date = txtDuration.getText().toString();

        String goalStr = txtGoal.getText().toString();
        goalStr = goalStr.replace(".", "");

        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Project title is empty!", Toast.LENGTH_LONG).show();
        } else if (type.equals("")) {
            Toast.makeText(getApplicationContext(), "Category not selected!", Toast.LENGTH_LONG).show();
        } else if (blurb.equals("")) {
            Toast.makeText(getApplicationContext(), "Shot blurb is empty!", Toast.LENGTH_LONG).show();
        } else if (date.equals("")) {
            Toast.makeText(getApplicationContext(), "Date is empty!", Toast.LENGTH_LONG).show();
        } else {
            long goal = -1;
            try {
                goal = Long.parseLong(goalStr);
            } catch (NumberFormatException ex) {
                Log.e("CreateProjectExtended", ex.getMessage());
                Toast.makeText(this, "Invalid goal value", Toast.LENGTH_LONG).show();
            }

            ProjectData newProject = new ProjectData(0, userID, name, type, blurb, date, goal, 0);

            Log.i("CreateProjectExtended", "Attempting to add new project: " + name);

            long newID = databaseAdapter.addProject(newProject);

            if (newID == -1) {
                Log.e("CreateProjectExtended", "Error adding project");
                Toast.makeText(this, "Failed to add new project", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Added new project " + name, Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }
}
