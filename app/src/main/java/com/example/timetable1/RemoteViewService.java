package com.example.timetable1;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new com.example.timetable1.RemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
