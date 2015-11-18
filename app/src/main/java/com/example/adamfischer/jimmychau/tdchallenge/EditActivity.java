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

public class EditActivity extends Activity {

    DatabaseAdapter databaseAdapter;

    // Data about project
    private static ProjectData pd;

    EditText pt;
    EditText blurb;
    Spinner ts;
    EditText pl;
    EditText date;
    EditText goal;

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
        pl = (EditText) findViewById(R.id.locationEditText);
        date = (EditText) findViewById(R.id.durationEditText);
        goal = (EditText) findViewById(R.id.goalEditText);

        pd = (ProjectData)getIntent().getExtras().getSerializable("iItem");

        pt.setText(pd.getName());
        blurb.setText(pd.getBlurb());
       //ts.setSelection();
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
}
