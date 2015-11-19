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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class EditActivity extends Activity {

    DatabaseAdapter databaseAdapter;

    // Data about project
    private static ProjectData pd;

    EditText pt;
    EditText blurb;
    Spinner ts;
    EditText date;
    EditText goal;

    int typePosition;

    String name;
    String type;
    String bl;
    String da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // create a instance of SQLite Database
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter = databaseAdapter.open();

        pt = (EditText) findViewById(R.id.projectTitleEditText);
        blurb = (EditText) findViewById(R.id.blurbEditText);
        ts = (Spinner) findViewById(R.id.typeSpinner);
        date = (EditText) findViewById(R.id.durationEditText);
        goal = (EditText) findViewById(R.id.goalEditText);

        pd = (ProjectData) getIntent().getExtras().getSerializable("iItem");

        String[] array = getResources().getStringArray(R.array.type_selection);

        typePosition = Arrays.asList(array).indexOf(pd.getType());

        pt.setText(pd.getName());
        blurb.setText(pd.getBlurb());
        ts.setSelection(typePosition);
        //pl.setText();
        date.setText(pd.getDate());
        goal.setText(pd.getGoal() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

    public void deleteOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do this action");
        builder.setMessage("do you want confirm this action?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                databaseAdapter.deleteProject(pd.getName());

                dialog.dismiss();

                finish();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void cancelOnClick(View view) {
        finish();
    }

    public void onSaveClick(View view) {
        name = pt.getText().toString();
        type = ts.getSelectedItem().toString();
        bl = blurb.getText().toString();
        da = date.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do this action");
        builder.setMessage("do you want confirm this action?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do do my action here

                        if (name.equals("")) {
                            Toast.makeText(getApplicationContext(), "Project title is empty!", Toast.LENGTH_LONG).show();
                        } else if (type.equals("")) {
                            Toast.makeText(getApplicationContext(), "Category not selected!", Toast.LENGTH_LONG).show();
                        } else if (bl.equals("")) {
                            Toast.makeText(getApplicationContext(), "Shot blurb is empty!", Toast.LENGTH_LONG).show();
                        } else if (da.equals("")) {
                            Toast.makeText(getApplicationContext(), "Date is empty!", Toast.LENGTH_LONG).show();
                        } else {
                            pd.setBlurb(blurb.getText().toString());
                            pd.setDate(date.getText().toString());
                            pd.setName(pt.getText().toString());
                            pd.setType(ts.getSelectedItem().toString());

                            databaseAdapter.updateProject(pd);

                            dialog.dismiss();
                            finish();
                        }
                    }

                }

        );

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()

                {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // I do not need any action here you might
                        dialog.dismiss();
                    }
                }

        );

        AlertDialog alert = builder.create();
        alert.show();
    }
}
