package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class EditActivity extends Activity {

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

        pt = (EditText) findViewById(R.id.projectTitleEditText);
        blurb = (EditText) findViewById(R.id.blurbEditText);
        ts = (Spinner) findViewById(R.id.typeSpinner);
        pl = (EditText) findViewById(R.id.locationEditText);
        date = (EditText) findViewById(R.id.durationEditText);
        goal = (EditText) findViewById(R.id.goalEditText);

        pd = (ProjectData)getIntent().getExtras().getSerializable("iItem");

        pt.setText(pd.getName().toString());
        blurb.setText(pd.getBlurb().toString());
//        //ts.setSelection(pd.getType());
//        //pl.setText(pd);
       date.setText(pd.getDate().toString());
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
}
