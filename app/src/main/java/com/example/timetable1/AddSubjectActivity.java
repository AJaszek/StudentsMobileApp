package com.example.timetable1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.timetable1.ui.home.HomeFragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class AddSubjectActivity extends Activity {

    private int dayOfWeek;
    private int evenWeek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);


       // List<int> dayName
        String[] dayName ={
                getResources().getString(R.string.sunday),
                getResources().getString(R.string.monday),
                getResources().getString(R.string.tuesday),
                getResources().getString(R.string.wednesday),
                getResources().getString(R.string.thursday),
                getResources().getString(R.string.friday),
                getResources().getString(R.string.saturday)
        };
        String[] evenWeekString ={
                getResources().getString(R.string.everyWeek),
                getResources().getString(R.string.evenWeek),
                getResources().getString(R.string.oddWeek)
        };


        Spinner spinnerDayOfWeek = (Spinner)findViewById(R.id.dayOfWeek);
        ArrayAdapter adapterDay = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dayName);
        spinnerDayOfWeek.setAdapter(adapterDay);

        spinnerDayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                //String item = arg0.getItemAtPosition(position).toString();
                dayOfWeek = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
       });

        Spinner spinnerEvenWeek = (Spinner)findViewById(R.id.evenWeekCheck);
        ArrayAdapter adapterWeek = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, evenWeekString );
        spinnerEvenWeek.setAdapter(adapterWeek);

        spinnerEvenWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                //String item = arg0.getItemAtPosition(position).toString();
                evenWeek = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }
    public boolean testRegex(String startHour, String finishHour){
        if(Pattern.matches("[0-9]{2}:[0-9]{2}", startHour) && Pattern.matches("[0-9]{2}:[0-9]{2}", finishHour))
            return true;
        else
            return false;
    }
    public void save(View v){
        String name = ((EditText) findViewById(R.id.editSubjectName)).getText().toString();
        String startHour = ((EditText) findViewById(R.id.startHour)).getText().toString();
        String finishHour = ((EditText) findViewById(R.id.finishHour)).getText().toString();
        String roomNumber = ((EditText) findViewById(R.id.roomNumber)).getText().toString();
        String teacherName = ((EditText) findViewById(R.id.editTeacherName)).getText().toString();


        if( testRegex(startHour,finishHour)){

        FileOutputStream fos = null;
        try{
            fos = openFileOutput("Date", MODE_APPEND);
            String dataToSave = name + ";" + startHour + ";" + finishHour + ";" + teacherName+ ";" + roomNumber+ ";" + dayOfWeek + evenWeek + ";";
            fos.write(dataToSave.getBytes());
            fos.write('\n');


            Toast.makeText(this, getString(R.string.added), Toast.LENGTH_LONG).show();

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } else{
            Toast.makeText(this, getString(R.string.incHour), Toast.LENGTH_LONG).show();
        }

    }
    public void back(View v){
        Intent intent = new Intent(this, HomeFragment.class);
        startActivity(intent);
    }
}
