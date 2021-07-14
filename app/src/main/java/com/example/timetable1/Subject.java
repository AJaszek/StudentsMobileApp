package com.example.timetable1;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Subject implements Serializable {
    String name;
    String startHour;
    String finishHour;
    String roomNumber;
    String teacherName;
    ArrayList<String> notes;
    public boolean clicked;

    public Subject(String name){
        this.name = name;
    }

    public Subject(String name, String startHour, String finishHour,String teacherName, String roomNumber, ArrayList<String> notes){
        this.name = name;
        this.startHour = startHour;
        this.finishHour = finishHour;
        this.teacherName = teacherName;
        this.roomNumber = roomNumber;
        this.clicked = false;
        if(notes != null)
            this.notes = notes;
        else
            this.notes = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }
    public String getStartHour() { return startHour; }
    public Date getStartHourDate() {

       // String czas = "12:43";
        DateFormat sdf = new SimpleDateFormat("HH:mm"); // or "hh:mm" for 12 hour format
        try {
            Date date = sdf.parse(startHour);

            return date;
        } catch (ParseException e) {

            return  new Date(0);
        }
    }
    public String getFinishHour() { return finishHour; }
    public String getRoomNumber() { return roomNumber; }
    public String getNote(int index) {
        return notes.get(index);
    }
    public void addNote(String note) {
        this.notes.add(note);
    }
    public void removeNote(int index){
        this.notes.remove(index);
    }
    public ArrayList<String> getNotesArray() {
        return notes;
    }
    public int findNoteIndex(String toFind){
        if(notes != null) {
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).contains(toFind)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public String getTeacherName() { return teacherName;}
}
