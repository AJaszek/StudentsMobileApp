package com.example.timetable1.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable1.AddSubjectActivity;
import com.example.timetable1.CalendarCustom;
import com.example.timetable1.FileHandler;
import com.example.timetable1.NotificationReciever;
import com.example.timetable1.R;
import com.example.timetable1.Subject;
import com.example.timetable1.SubjectAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.List;

import static com.example.timetable1.App.CHANNEL_1_ID;
import static com.example.timetable1.App.CHANNEL_2_ID;

public class HomeFragment extends Fragment implements SubjectAdapter.ClickListener {

    public RecyclerView subjectView;
    public RecyclerView.Adapter adapter;
    private List<Subject>[][] subjectList;
    Activity activity = (Activity) getContext();
    private View root;
    public CalendarCustom calendar = new CalendarCustom();
    public FileHandler fileHandler = new FileHandler();


    public List<Subject> todayListSubjects() {
        return subjectList[calendar.checkDayOfWeek()][calendar.evenWeekCheck()];
    }

    private void changeDayView() {
        setTextDayOfWeek();
        setAdapter(todayListSubjects());
    }

    private void setTextDayOfWeek() {
        TextView text = (TextView) root.findViewById(R.id.weekDay);
        TextView date = (TextView) root.findViewById(R.id.dateString);
        text.setText(calendar.getTextDayOfWeek());
        date.setText(calendar.getDay() + "." + calendar.getMonth());
    }

    public void setAdapter(List<Subject> subjectList) {
        String date = String.valueOf(calendar.getDay()) + calendar.getMonth() + ".";
        adapter = new SubjectAdapter(subjectList, this, date);
        subjectView.setAdapter(adapter);
    }

    public void openAddSubjectActivity() {
        Intent intent = new Intent(getActivity(), AddSubjectActivity.class);
        startActivity(intent);
    }

    private void sortSubjectLists() {
        for (int l = 0; l < 2; l++) {
            for (int k = 0; k < 7; k++) {
                for (int j = 0; j < subjectList[k][l].size() + 10; j++) {
                    for (int i = 0; i < subjectList[k][l].size() - 1; i++) {
                        if (subjectList[k][l].get(i).getStartHourDate().compareTo(subjectList[k][l].get(i + 1).getStartHourDate()) > 0) {
                            Subject tmp = subjectList[k][l].get(i);
                            subjectList[k][l].set(i, subjectList[k][l].get(i + 1));
                            subjectList[k][l].set(i + 1, tmp);

                        }
                    }
                }
            }
        }
    }

    public RecyclerView initializeRecycleView(RecyclerView subjectView) {
        subjectView = (RecyclerView) root.findViewById(R.id.subjectsView);
        subjectView.setHasFixedSize(true);
        subjectView.setLayoutManager(new LinearLayoutManager(activity));
        return subjectView;
    }

    public void resizeSubjectView(int position) {
        ConstraintLayout viewById = (ConstraintLayout) subjectView.findViewHolderForLayoutPosition(position)
                .itemView.findViewById(R.id.resizingSubjectConstrain);

        boolean clicked = todayListSubjects().get(position).clicked;
        if (clicked) {
            viewById.setVisibility(View.GONE);
        } else {
            viewById.setVisibility(View.VISIBLE);
        }
        todayListSubjects().get(position).clicked = !clicked;
    }

    @Override
    public void onSubjectClick(int position) {

        resizeSubjectView(position);

    }

    @Override
    public void onDeleteClick(int position) {
        deleteSubject(position);
        //Log.d("aaa", "pos"+position);
    }

    public String findStringSubject(int position) {

        Subject subj = todayListSubjects().get(position);

        return subj.getName() + ";" + subj.getStartHour() + ";" +
                subj.getFinishHour() + ";" + subj.getTeacherName() + ";" + subj.getRoomNumber() + ";" + calendar.checkDayOfWeek();
    }

    private void deleteSubject(int position) {

        String subjectToRemove = findStringSubject(position);

        File inputFile;
        File tempFile;


        try {


            inputFile = new File("/data/data/com.example.timetable1/files/Date");
            tempFile = new File("/data/data/com.example.timetable1/files/DateTemp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            // String lineToRemove = "aaa;12:50;13:00;69;1";
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if (trimmedLine.startsWith(subjectToRemove)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));

            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);


            todayListSubjects().remove(position);
            setAdapter(todayListSubjects());


        } catch (IOException e) {
            e.printStackTrace();
        }

        // FileInputStream fileToRead = openFileInputStream();
        //  FileOutputStream tempFile = openFileOutput


    }

    @Override
    public void addNoteClick(int position, String note, int noteType) {

        String subjectToAddNote = findStringSubject(position);
        File inputFile;
        File tempFile;


        try {


            inputFile = new File("/data/data/com.example.timetable1/files/Date");
            tempFile = new File("/data/data/com.example.timetable1/files/DateTemp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            // String lineToRemove = "aaa;12:50;13:00;69;1";

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                Log.d("aaa", trimmedLine);
                Log.d("aaa", subjectToAddNote);
                if (trimmedLine.startsWith(subjectToAddNote)) {

                    writer.write(currentLine + noteType + String.valueOf(calendar.getDay()) + calendar.getMonth() + "." + note + ":" + System.getProperty("line.separator"));
                } else {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);


            todayListSubjects().get(position).addNote(note);
            setAdapter(todayListSubjects());


        } catch (IOException e) {
            e.printStackTrace();
        }

        // FileInputStream fileToRead = openFileInputStream();
        //  FileOutputStream tempFile = openFileOutput


    }

    @Override
    public void delNoteClick(int position, String noteToRemove) {
        String subjectToRemoveNote = findStringSubject(position);

        File inputFile;
        File tempFile;


        try {


            inputFile = new File("/data/data/com.example.timetable1/files/Date");
            tempFile = new File("/data/data/com.example.timetable1/files/DateTemp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            // String lineToRemove = "aaa;12:50;13:00;69;1";
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {


                String trimmedLine = currentLine.trim();
                if (trimmedLine.startsWith(subjectToRemoveNote)) {
                    //currentLine = currentLine.substring(0, currentLine.lastIndexOf(";"));
                    currentLine = currentLine.replace(noteToRemove + ":", "");
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);

            todayListSubjects().get(position).removeNote(todayListSubjects().get(position).findNoteIndex(noteToRemove));
            setAdapter(todayListSubjects());


        } catch (IOException e) {
            e.printStackTrace();
        }

        // FileInputStream fileToRead = openFileInputStream();
        //  FileOutputStream tempFile = openFileOutput


        // FileInputStream fileToRead = openFileInputStream();
        //  FileOutputStream tempFile = openFileOutput
    }

    public void buttonsOnClickHandler() {
        Button nextBut = root.findViewById(R.id.nextDay);
        Button prevBut = root.findViewById(R.id.previousDay);
        FloatingActionButton addSubject = root.findViewById(R.id.addSubject);

        nextBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.nextDay();
                changeDayView();
                //nextDay();
            }
        });
        prevBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.prevDay();
                changeDayView();
                //prevDay();
            }
        });
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddSubjectActivity();
            }
        });
    }

    public Intent putExtraSubjectsIntoIntent(Intent intent) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(subjectList);
            out.flush();
            byte[] data = bos.toByteArray();
            intent.putExtra("subjects", data);
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void startNotificationService() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Pref", Context.MODE_PRIVATE);

        if (preferences.getBoolean("notifications", false)) {
            Intent intent = new Intent(getContext(), NotificationReciever.class);

            intent = putExtraSubjectsIntoIntent(intent);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pendingIntent);
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);


        buttonsOnClickHandler();

        subjectView = initializeRecycleView(subjectView);
        calendar.setDayOfWeek(calendar.checkDayOfWeek());
        subjectList = fileHandler.loadDataFromDevice();
        sortSubjectLists();

        setTextDayOfWeek();
        setAdapter(todayListSubjects());
        startNotificationService();

        /*notificationManager = NotificationManagerCompat.from(getContext());

        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle("tutyl")
                .setContentText("wiadomosc")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
*/

        // Calendar calendar1 = Calendar.getInstance();
        /*calendar1.set(Calendar.HOUR_OF_DAY, 11);
        calendar1.set(Calendar.MINUTE, 34);
        calendar1.set(Calendar.SECOND, 10);*/
        //  calendar1.add(Calendar.SECOND, 5);


        // intent.putExtra("subject", todayListSubjects().get(0));
        // intent.putExtra("aa", "subjectList");

        //Log.d("aaa", String.valueOf(calendar.checkDayOfWeek()));


        return root;
    }


}