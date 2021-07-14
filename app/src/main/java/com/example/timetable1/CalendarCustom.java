package com.example.timetable1;

import android.widget.TextView;

import java.util.Calendar;

public class CalendarCustom {

    Calendar calendar = Calendar.getInstance();
    private int dayOfWeek = 0;

    public CalendarCustom() {
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }

   /* public int checkDayOfWeek(){
        return calendar.get(Calendar.DAY_OF_WEEK);
    }*/
    public int getDay(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public int getMonth(){
        return calendar.get(Calendar.MONTH);
    }
    public long getTimeInMillis(){ return calendar.getTimeInMillis();}
    public int checkDayOfWeek() {
        return dayOfWeek;
    }
    public int evenWeekCheck(){
        if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0)
            return 1;
        else
            return 0;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void nextDay(){
        if(dayOfWeek<6)
            dayOfWeek++;
        else
            dayOfWeek=0;
        calendar.add(Calendar.DAY_OF_MONTH, 1);
    }
    public void prevDay(){
        if(dayOfWeek>0)
            dayOfWeek--;
        else
            dayOfWeek = 6;
        calendar.add(Calendar.DAY_OF_MONTH, -1);
    }

    public int getTextDayOfWeek(){
        int dayName[]={
                R.string.sunday,
                R.string.monday,
                R.string.tuesday,
                R.string.wednesday,
                R.string.thursday,
                R.string.friday,
                R.string.saturday
        };
        return dayName[dayOfWeek];
    }
}
