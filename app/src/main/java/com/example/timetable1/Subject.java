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


        DateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date date = sdf.parse(startHour);

            return date;
        } catch (ParseException e) {

            return  new Date(0);
        }
    }
    public String getFinishHour() { return finishHour; }
    public void setStartHour(String startHour){
        this.startHour = startHour;
    }
    public void setFinishHour(String finishHour){
        this.finishHour = finishHour;
    }
    public String getRoomNumber() { return roomNumber; }
    public String getNote(int index) {
        if(index < notes.size())
            return notes.get(index);
        else
            return null;
    }
    public void addNote(String note) {
        this.notes.add(note);
    }
    public boolean removeNote(int index) {
        if(index < notes.size()) {
            this.notes.remove(index);
            return true;
        }
        else
            return false;
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
