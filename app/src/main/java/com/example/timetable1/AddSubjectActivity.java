package com.example.timetable1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.timetable1.ui.home.HomeFragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class AddSubjectActivity extends AppCompatActivity {

    private int dayOfWeek;
    private int evenWeek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Pref", Context.MODE_PRIVATE);
        if(settings.getBoolean("nightMode", true))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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
            public void onItemSelected(AdapterView<?> parentView, View arg1,
                                       int position, long id) {
                //String item = arg0.getItemAtPosition(position).toString();
                dayOfWeek = position;
                ((TextView)parentView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));

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
            public void onItemSelected(AdapterView<?> parentView, View arg1,
                                       int position, long id) {
                //String item = arg0.getItemAtPosition(position).toString();
                evenWeek = position;
                ((TextView)parentView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));

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
        TextView event = findViewById(R.id.eventTextView);


        if( testRegex(startHour,finishHour)){

        FileOutputStream fos = null;
        try{
            fos = openFileOutput("Date", MODE_APPEND);
            String dataToSave = name + ";" + startHour + ";" + finishHour + ";" + teacherName+ ";" + roomNumber+ ";" + dayOfWeek + evenWeek + ";";
            fos.write(dataToSave.getBytes());
            fos.write('\n');

            event.setText(getString(R.string.added));
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
            event.setText(getString(R.string.incHour));
            //Toast.makeText(this, getString(R.string.incHour), Toast.LENGTH_LONG).show();
        }

    }
    public void back(View v){
        Intent intent = new Intent(this, HomeFragment.class);
        startActivity(intent);
    }
    private void setColorMode() {
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Pref", Context.MODE_PRIVATE);
        if(settings.getBoolean("nightMode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }
}
