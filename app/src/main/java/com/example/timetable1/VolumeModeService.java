package com.example.timetable1;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VolumeModeService extends IntentService {

    public VolumeModeService() {
        super(null);
    }
    public VolumeModeService(String name) {
        super(name);
    }

    private List<Subject>[][] readExtraSubjects(byte[] subjectsByteArray) {
        ByteArrayInputStream bis = new ByteArrayInputStream(subjectsByteArray);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return (List<Subject>[][]) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static boolean timeBefore(Date date, Date date2) {
        DateFormat f = new SimpleDateFormat("HH:mm");
        return f.format(date).compareTo(f.format(date2)) < 0;
    }
    public void setVibrationMode(boolean vibration){
        AudioManager mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if(vibration)
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        else
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    private Long findSubjectToDisplay(List<Subject>[][] subjectList, Calendar calendar, Intent intent) {

      //  Log.d("aaa", "weszlo");
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        int evenWeek = calendar.get(Calendar.WEEK_OF_YEAR) % 2;
        for (Subject subject : subjectList[calendar.get(Calendar.DAY_OF_WEEK) - 1][evenWeek]) {

            Date now = calendar.getTime();
            Date start = null;
            Date finish = null;
            try {
                start = formatter.parse(subject.getStartHour());
                finish = formatter.parse(subject.getFinishHour());


                if(!timeBefore(finish, now)){
                    
                    if(timeBefore(start,now)){
                        String text =  subject.getName() + " - " +  getApplicationContext().getString(R.string.vibrationMode);
                        Toast.makeText(getApplicationContext(),text,
                                Toast.LENGTH_SHORT).show();
                        setVibrationMode(true);
                        return timeToLong(finish);
                    }
                    else
                    {
                        String text =  getApplicationContext().getString(R.string.noClasses) + " - " +  getApplicationContext().getString(R.string.normalMode);
                        Toast.makeText(getApplicationContext(),text,
                                Toast.LENGTH_SHORT).show();
                        setVibrationMode(false);
                        return timeToLong(start);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return getNextDay();
    }

    private Long getNextDay(){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 1);

        return now.getTimeInMillis();
    }
    private Long timeToLong(Date time) {
        Calendar now = Calendar.getInstance();
        Calendar timeTemp = Calendar.getInstance();
        timeTemp.setTime(time);
        now.set(Calendar.HOUR_OF_DAY, timeTemp.get(Calendar.HOUR_OF_DAY));
        now.set(Calendar.MINUTE, timeTemp.get(Calendar.MINUTE));
        Log.d("aaa", String.valueOf(now.getTimeInMillis()));
        return now.getTimeInMillis();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Pref", Context.MODE_PRIVATE);

        if (preferences.getBoolean("vibrations", false)) {
            Calendar calendar = Calendar.getInstance();
            List<Subject>[][] subjectList = readExtraSubjects(intent.getByteArrayExtra("subjects"));

            long nextCheck = findSubjectToDisplay(subjectList, calendar, intent);


       /* FileHandler f = new FileHandler();
        f.loadDataFromDevice()*/

            Intent newIntent = new Intent(getApplicationContext(), VolumeModeService.class);
            PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 5, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextCheck + 60 * 1000, pIntent);
        }
    }
}

