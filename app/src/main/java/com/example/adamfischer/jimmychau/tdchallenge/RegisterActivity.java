package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

    EditText editTextUserName,editTextPassword,editTextConfirmPassword,editTextFirstName,editTextLastName,editTextEmail;
    Button btnCreateAccount;

    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        // Get References of Views
        editTextUserName=(EditText)findViewById(R.id.usernameEditText);
        editTextPassword=(EditText)findViewById(R.id.passwordEditText);
        editTextConfirmPassword=(EditText)findViewById(R.id.password2EditText);
        editTextFirstName=(EditText)findViewById(R.id.firstNameEditText);
        editTextLastName=(EditText)findViewById(R.id.lastNameEditView);
        editTextEmail=(EditText)findViewById(R.id.emailEditText);

        btnCreateAccount=(Button)findViewById(R.id.registerButton);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();

                String eMail = editTextEmail.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                // check if any of the fields are vaccant
                if (firstName.equals("")) {
                    Toast.makeText(getApplicationContext(), "First name is empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (lastName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Last name is empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (eMail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Email is empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (!eMail.matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (userName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Username is empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Password is empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Password confirmation is empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                // check if both password matches
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Save the Data in Database
                    loginDataBaseAdapter.insertEntry(userName, password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
}
