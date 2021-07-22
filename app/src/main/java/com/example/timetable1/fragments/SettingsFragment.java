package com.example.timetable1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.timetable1.R;


public class SettingsFragment extends Fragment {


    SharedPreferences settings;
    Switch switchNotifications;

    public SettingsFragment() {
        // Required empty public constructor
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

        initializeSwitches(view);


        return view;

    }

    private void initializeSwitches(View view){

        switchNotifications = view.findViewById(R.id.switchNotifications);
        switchNotifications.setChecked(settings.getBoolean("notifications", false));
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                putBooleanPreferences("notifications", isChecked);
            }
        });
    }

    private void putBooleanPreferences(String key, boolean value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}