package com.example.timetable1;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {

    public Subject getSampleSubject(){
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList("note1", "note2", "note3"));
        Subject subject = new Subject("Name", "", "", "", "", list);
        return subject;
    }
    public List<Subject>[][] getSampleSubjectList(){
        List<Subject>[][] subjectList = (List<Subject>[][]) new List[1][1];
        subjectList[0][0] = new ArrayList<>();
        subjectList[0][0].add(getSampleSubject());
        subjectList[0][0].get(0).setStartHour("08:00");
        subjectList[0][0].add(getSampleSubject());
        subjectList[0][0].get(1).setStartHour("09:00");
        subjectList[0][0].add(getSampleSubject());
        subjectList[0][0].get(2).setStartHour("10:00");
        return subjectList;
    }


    @Test
    public void cantImportEmptyFile(){
        FileHandler fileHandler = new FileHandler();
        assertFalse(fileHandler.importData(null));
    }

    @Test
    public void removeNotExistingTodo(){
        FileHandler fileHandler = new FileHandler();
        assertFalse(fileHandler.removeTodo(-5));
    }
    @Test
    public void changeStateNotExistingTodo(){
        FileHandler fileHandler = new FileHandler();
        assertFalse(fileHandler.changeStateTodo(-5, true));
    }

    @Test
    public void cantSetWrongDayOfWeek(){
        CalendarCustom calendarCustom = new CalendarCustom();
        int lastDay = 4;
        calendarCustom.setDayOfWeek(lastDay);
        calendarCustom.setDayOfWeek(10);
        assertEquals(calendarCustom.getDayOfWeek(),lastDay);
    }

    @Test
    public void getIndexOfNotExistingNote(){
        Subject subject = getSampleSubject();
        int index = subject.findNoteIndex("note4");
        assertEquals(index, -1);
    }

    @Test
    public void getNotExistingNote(){
        Subject subject = getSampleSubject();
        String note = subject.getNote(5);
        assertNull(note);
    }

    @Test
    public void removeNotExistingNote(){
        Subject subject = getSampleSubject();
        assertFalse(subject.removeNote(5));
    }

    @Test
    public void findSubjectToDisplayInNotfication(){
        NotificationReciever r = new NotificationReciever();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK,1);
        calendar.set(Calendar.WEEK_OF_YEAR,2);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE,46);
        Subject s = r.findSubjectToDisplay(getSampleSubjectList(), calendar);
        assertEquals(s.getStartHour(),"09:00");
    }

    @Test
    public void testRegexHours(){
        AddSubjectActivity activity = new AddSubjectActivity();
        assertFalse(activity.testRegex("1234","1234"));
    }

}