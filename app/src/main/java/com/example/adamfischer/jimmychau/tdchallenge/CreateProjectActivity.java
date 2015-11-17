package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateProjectActivity extends Activity {

    Spinner mySpinner;
    EditText projectName;

    int selectedType;
    String selectedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        mySpinner = (Spinner)findViewById(R.id.typeSpinner);

        // Event for spinner - on item select
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch(position){
                    case 0:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 1:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 2:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 3:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 4:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 5:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 6:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 7:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 8:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 9:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 10:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 11:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 12:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 13:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 14:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                    case 15:
                        //selectedType = parentView.getItemAtPosition(position).toString();
                        selectedType = position;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_project, menu);
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

    public void startOnClick(View view) {
        projectName = (EditText)findViewById(R.id.projectNameEditText);
        selectedName = projectName.getText().toString();

        Intent i = new Intent(this, CreateProjectExtendedActivity.class);
        i.putExtra("sType", selectedType);
        i.putExtra("sName", selectedName);
        startActivity(i);
    }

    public void cancelOnClick(View view) {
        finish();
    }
}
