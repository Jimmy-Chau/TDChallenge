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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    Button btnLogin,btnRegister;
    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // create a instance of SQLite Database
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter = databaseAdapter.open();

        // Get The Reference Of Buttons
        btnLogin=(Button)findViewById(R.id.loginButton);
        btnRegister=(Button)findViewById(R.id.registerButton);

        // Set OnClick Listener on SignUp button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        // get the References of views
        final EditText editTextUserName=(EditText)findViewById(R.id.usernameEditText);
        final EditText editTextPassword=(EditText)findViewById(R.id.passwordEditText);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                UserData userData = databaseAdapter.getUser(userName);

                // check if the Stored password matches with  Password entered by user
                if (userData != null && password.equals(userData.getPassword())) {
                    Toast.makeText(LoginActivity.this, "Congrats: Login Successful", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(v.getContext(), MainActivity.class);

                    i.putExtra("userData", userData);

                    startActivity(i);

                } else {
                    Toast.makeText(LoginActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        databaseAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
