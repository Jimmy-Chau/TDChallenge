/*
Project: TD Challenge
Challenge: This particular challenge revolves around Social Media and Opportunities
           in Finance leveraging the mobile platform.
Coder: Jimmy Chau & Adam Fischer
Date: November 18, 2015
Course: INFO-5102 GUI Development
*/

package com.example.adamfischer.jimmychau.tdchallenge;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText txtdate;
    public DateDialog(View view){
        txtdate=(EditText)view;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {


// Use the current date as the default date in the dialog
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        //return new DatePickerDialog(getActivity(), this, year, month, day);
        DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker dp = d.getDatePicker();
        dp.setMinDate(c.getTimeInMillis());
        return d;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //show to the selected date in the text box
        String date=day+"-"+(month+1)+"-"+year;
        txtdate.setText(date);
    }
}