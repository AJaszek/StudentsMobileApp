package com.example.timetable1;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context = null;
    private int appWidgetId;
    public CalendarCustom calendar;
    public FileHandler fileHandler = new FileHandler();

    private List<Subject> widgetList = new ArrayList<Subject>();

    public RemoteViewsFactory(Context context, Intent intent)
    {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    private void updateWidgetListView()
    {
        calendar = new CalendarCustom();
        List<Subject>[][] subjectList;
        subjectList = fileHandler.loadDataFromDevice();


        List<Subject> convertedToList = subjectList[calendar.checkDayOfWeek()][calendar.evenWeekCheck()];
        this.widgetList = convertedToList;
    }

    @Override
    public int getCount()
    {
        return widgetList.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {

        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.subject_row_widget);
        Subject s = widgetList.get(position);

        String line = widgetList.get(position).getName() + " - " + widgetList.get(position).getStartHour() + " - " + widgetList.get(position).getRoomNumber();
        remoteView.setTextViewText(R.id.widget_item, line);

        return remoteView;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public void onCreate()
    {
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged()
    {
        updateWidgetListView();
    }

    @Override
    public void onDestroy()
    {
        widgetList.clear();
    }
}
