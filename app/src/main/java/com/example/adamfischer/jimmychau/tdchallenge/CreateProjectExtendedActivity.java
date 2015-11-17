package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateProjectExtendedActivity extends Activity {

    String cType;
    String cName;
    EditText projectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project_extended);

        cType = getIntent().getExtras().getString("sType");
        cName = getIntent().getExtras().getString("sName");

        projectTitle = (EditText)findViewById(R.id.projectTitleEditText);

        projectTitle.setText(cName);

        Toast.makeText(CreateProjectExtendedActivity.this, cType + " " + cName, Toast.LENGTH_LONG).show();
    }

    public void onStart(){
        super.onStart();

        EditText txtDate=(EditText)findViewById(R.id.durationEditText);
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
}
