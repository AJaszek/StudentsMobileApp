package com.example.timetable1.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetable1.FileHandler;
import com.example.timetable1.R;
import com.example.timetable1.TextNote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment {


    SharedPreferences settings;
    Switch switchNotifications;
    Switch switchNightMode;
    LinearLayout deleteAllDataLayout;
    LinearLayout exportDataLayout;
    LinearLayout importDataLayout;
    LinearLayout exportTimetable;

    public SettingsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        settings = getActivity().getSharedPreferences("Pref", Context.MODE_PRIVATE);

        initializeButtons(view);


        return view;

    }

    private void initializeButtons(View view) {

        switchNotifications = view.findViewById(R.id.switchNotifications);
        switchNightMode = view.findViewById(R.id.switchNightMode);
        deleteAllDataLayout = view.findViewById(R.id.deleteAllDataLayout);
        exportDataLayout = view.findViewById(R.id.exportDataLayout);
        importDataLayout = view.findViewById(R.id.importDataLayout);
        exportTimetable = view.findViewById(R.id.exportTimatebleLayout);

        switchNotifications.setChecked(settings.getBoolean("notifications", false));
        switchNightMode.setChecked(settings.getBoolean("nightMode", false));




        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                putBooleanPreferences("notifications", isChecked);
            }
        });
        switchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                putBooleanPreferences("nightMode", isChecked);
                changeNightMode();
            }
        });
        deleteAllDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllData();

            }
        });
        exportDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData();

            }
        });
        importDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData();

            }
        });
        exportTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportTable();

            }
        });
    }

    private void changeNightMode() {
        if(settings.getBoolean("nightMode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void deleteAllData() {

        FileHandler fileHandler = new FileHandler();
        if (fileHandler.deleteAllData())
            Toast.makeText(getContext(), getString(R.string.dataDeleted), Toast.LENGTH_SHORT).show();
    }

    private void exportData() {
        FileHandler fileHandler = new FileHandler();
        if (fileHandler.exportData())
            Toast.makeText(getContext(), getString(R.string.added), Toast.LENGTH_SHORT).show();
    }
    private void exportTable() {
        FileHandler fileHandler = new FileHandler();
        if (fileHandler.exportTimetable())
            Toast.makeText(getContext(), getString(R.string.added), Toast.LENGTH_SHORT).show();
    }
    private void importData() {

        Intent intent;
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPathSegments().get(1);
                    String[] splittedPath = path.split(":");
                    String filePath = Environment.getExternalStorageDirectory() + "/" + splittedPath[1];
                    FileHandler fileHandler = new FileHandler();
                    if (fileHandler.importData(filePath))
                        Toast.makeText(getContext(), getString(R.string.added), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), getString(R.string.wrongFile), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void putBooleanPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}