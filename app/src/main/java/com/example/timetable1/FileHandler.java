package com.example.timetable1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandler {

    String pathFile = "/data/data/com.example.timetable1/files/Date";
    String pathDirectory = "/data/data/com.example.timetable1/files";

    public void makeDateFile(){
        FileOutputStream fos = null;
        try {
            File myDir = new File(pathDirectory);
            myDir.mkdir();
            fos =  new FileOutputStream(pathFile);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    public FileInputStream openFileInputStream(){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(pathFile);
        } catch (FileNotFoundException e) {
            makeDateFile();
            fis = openFileInputStream();
            e.printStackTrace();
        }
        return fis;
    }
    public void closeFileInputStream(FileInputStream fis){
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeSubjectList( List<Subject>[][] subjectList ){
        for(int i=0; i<7; i++) {
            for(int j=0; j<2; j++) {
                subjectList[i][j] =new ArrayList<>();
            }
        }
    }
    public  List<Subject>[][] loadDataFromDevice() {

        List<Subject>[][] subjectList = (List<Subject>[][]) new List[7][2];
        initializeSubjectList(subjectList);

        FileInputStream file = openFileInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(file);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String text;

        try {
            while ((text = bufferedReader.readLine()) != null) {

                loadSubjectList(text, subjectList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeFileInputStream(file);
        return subjectList;
    }
    public void loadSubjectList(String text, List<Subject>[][] subjectList){
        String[] splittedText = text.split(";");
        int day = Integer.parseInt(String.valueOf(splittedText[5].charAt(0)));
        int week;
        try {
            week = Integer.parseInt(String.valueOf(splittedText[5].charAt(1)));
        }catch (IndexOutOfBoundsException e){
            week = 0;
        }


        if(week != 0){
            if(splittedText.length == 6) {
                subjectList[day][week - 1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3],  splittedText[4], null));
            }
            else {
                ArrayList<String> notes = new ArrayList(Arrays.asList(splittedText[6].split(":")));
                subjectList[day][week-1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], notes ));
            }
        }
        else {
            if(splittedText.length == 6) {
                subjectList[day][0].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], null));
                subjectList[day][1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], null));
            }
            else {
                ArrayList<String> notes = new ArrayList(Arrays.asList(splittedText[6].split(":")));
                subjectList[day][0].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], notes ));
                subjectList[day][1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], notes ));
            }
        }

    }



}
