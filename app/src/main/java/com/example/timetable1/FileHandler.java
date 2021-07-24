package com.example.timetable1;

import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FileHandler {

    String pathFile = "/data/data/com.example.timetable1/files/Date";
    String pathTextNotes = "/data/data/com.example.timetable1/files/Notes";
    String pathTodo = "/data/data/com.example.timetable1/files/Todo";
    String pathDirectory = "/data/data/com.example.timetable1/files";
    String pathSheredPreferences = "/data/data/com.example.timetable1/shared_prefs/Pref.xml";

    public void makeDateFile(String directory) {
        FileOutputStream fos = null;
        try {
            File myDir = new File(pathDirectory);
            myDir.mkdir();
            fos = new FileOutputStream(directory);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public FileInputStream openFileInputStream(String directory) {
        if (directory.equals("subject"))
            directory = pathFile;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(directory);
        } catch (FileNotFoundException e) {
            makeDateFile(directory);
            fis = openFileInputStream(directory);
            e.printStackTrace();
        }
        return fis;
    }

    public void closeFileInputStream(FileInputStream fis) {
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeSubjectList(List<Subject>[][] subjectList) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                subjectList[i][j] = new ArrayList<>();
            }
        }
    }

    public List<Subject>[][] loadDataFromDevice() {

        List<Subject>[][] subjectList = (List<Subject>[][]) new List[7][2];
        initializeSubjectList(subjectList);

        FileInputStream file = openFileInputStream(pathFile);
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

    public void loadSubjectList(String text, List<Subject>[][] subjectList) {
        String[] splittedText = text.split(";");
        int day = Integer.parseInt(String.valueOf(splittedText[5].charAt(0)));
        int week;
        try {
            week = Integer.parseInt(String.valueOf(splittedText[5].charAt(1)));
        } catch (IndexOutOfBoundsException e) {
            week = 0;
        }


        if (week != 0) {
            if (splittedText.length == 6) {
                subjectList[day][week - 1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], null));
            } else {
                ArrayList<String> notes = new ArrayList(Arrays.asList(splittedText[6].split(":")));
                subjectList[day][week - 1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], notes));
            }
        } else {
            if (splittedText.length == 6) {
                subjectList[day][0].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], null));
                subjectList[day][1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], null));
            } else {
                ArrayList<String> notes = new ArrayList(Arrays.asList(splittedText[6].split(":")));
                subjectList[day][0].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], notes));
                subjectList[day][1].add(new Subject(splittedText[0], splittedText[1], splittedText[2], splittedText[3], splittedText[4], notes));
            }
        }

    }


    public boolean addTextNote(String subjectName, String topic, String note) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pathTextNotes, true);
            //fos = openFileOutput("Note", MODE_APPEND);
            String dataToSave = subjectName + "`" + topic + "`" + note;
            fos.write(dataToSave.getBytes());
            fos.write('\n');
            fos.close();
            return true;

        } catch (IOException e) {
            return false;
            //  e.printStackTrace();
        }

    }

    public List<TextNote> loadTextNotesFromDevice(String subject) {

        List<TextNote> notesList = new ArrayList<>();
        //initializeSubjectList(subjectList);
        FileInputStream file = null;
        try {
            file = openFileInputStream(pathTextNotes);
            InputStreamReader inputStreamReader = new InputStreamReader(file);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String text;


            while ((text = bufferedReader.readLine()) != null) {

                String[] splittedText = text.split("`");

                if (splittedText[0].equals(subject))
                    notesList.add(new TextNote(splittedText[1], splittedText[2]));
            }
        } catch (FileNotFoundException e) {
            makeDateFile(pathTextNotes);
            file = openFileInputStream(pathTextNotes);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeFileInputStream(file);
        return notesList;

    }


    public void removeTextNote(String subjectName, int position) {

        File inputFile;
        File tempFile;
        int counter = -1;
        try {


            inputFile = new File(pathTextNotes);
            tempFile = new File(pathTextNotes + "Temp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                String trimmedLine = currentLine.trim();


                if (trimmedLine.startsWith(subjectName)) {
                    counter++;
                    if (counter == position) continue;
                    else {
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    //currentLine = currentLine.substring(0, currentLine.lastIndexOf(";"));
                    // currentLine = currentLine.replace(noteToRemove + ":", "");
                } else
                    writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);

            //  todayListSubjects().get(position).removeNote(todayListSubjects().get(position).findNoteIndex(noteToRemove));
            //  setAdapter(todayListSubjects());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Todo> loadTodoFromDevice() {
        List<Todo> todoList = new ArrayList<>();

        FileInputStream file = null;
        try {
            file = openFileInputStream(pathTodo);
            InputStreamReader inputStreamReader = new InputStreamReader(file);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String text;


            while ((text = bufferedReader.readLine()) != null) {

                String[] splittedText = text.split("`");

                if(splittedText.length==3)
                todoList.add(new Todo(splittedText[0], splittedText[1], Boolean.parseBoolean(splittedText[2]), 0));
                else
                    todoList.add(new Todo(splittedText[0], splittedText[1], Boolean.parseBoolean(splittedText[2]), Integer.parseInt(splittedText[3])));
            }
        } catch (FileNotFoundException e) {
            makeDateFile(pathTodo);
            file = openFileInputStream(pathTodo);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeFileInputStream(file);
        Collections.reverse(todoList);
        return todoList;
    }

    public boolean addTodo(String topic, String description, int style) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pathTodo, true);
            //fos = openFileOutput("Note", MODE_APPEND);
            String dataToSave = topic + "`" + description + "`false`"+String.valueOf(style);
            fos.write(dataToSave.getBytes());
            fos.write('\n');
            fos.close();
            return true;

        } catch (IOException e) {
            return false;
            //  e.printStackTrace();
        }
    }

    public void removeTodo(int position) {
        File inputFile;
        File tempFile;
        int counter = -1;
        try {


            inputFile = new File(pathTodo);
            tempFile = new File(pathTodo + "Temp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                counter++;
                if (counter == position) continue;
                else
                    writer.write(currentLine + System.getProperty("line.separator"));



            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);

            //  todayListSubjects().get(position).removeNote(todayListSubjects().get(position).findNoteIndex(noteToRemove));
            //  setAdapter(todayListSubjects());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean changeStateTodo(int position, boolean done) {
        File inputFile;
        File tempFile;
        int counter = -1;
        boolean checked = done;
        try {


            inputFile = new File(pathTodo);
            tempFile = new File(pathTodo + "Temp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                counter++;
                if (counter == position) {
                    String[] splittedText = currentLine.split("`");
                    if(splittedText[2].equals("true")){
                        splittedText[2] = "false";
                        checked = false;
                    }
                    else{
                        splittedText[2] = "true";
                        checked = true;
                    }
                    String line = splittedText[0] + "`" +splittedText[1] + "`" +splittedText[2]+ "`" +splittedText[3];
                    writer.write(line + System.getProperty("line.separator"));
                } else
                    writer.write(currentLine + System.getProperty("line.separator"));



            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);

            //  todayListSubjects().get(position).removeNote(todayListSubjects().get(position).findNoteIndex(noteToRemove));
            //  setAdapter(todayListSubjects());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return checked;
    }

    public boolean deleteAllData() {

        File data = new File(pathDirectory);
        File imageNotes = new File( Environment.getExternalStorageDirectory()+ "/TimeTable/Notes");
        File settings = new File(pathSheredPreferences);

        return deleteContentRecursive(data)
                && deleteContentRecursive(imageNotes)
                && settings.delete();

    }
    boolean deleteContentRecursive(File file) {

        if (file.isDirectory())
            for (File child : file.listFiles())
                deleteContentRecursive(child);

        file.delete();

        return true;

    }
}
