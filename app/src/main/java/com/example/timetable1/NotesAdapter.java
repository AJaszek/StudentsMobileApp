package com.example.timetable1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {


    ArrayList<File> notesList;


    public NotesAdapter(ArrayList<File> notesList){
        this.notesList = notesList;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int i) {
        return notesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup ) {
        View convertView = null;

        if(convertView == null){
            convertView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.notes_row, viewGroup, false);




            ImageView note = convertView.findViewById(R.id.noteImage);



           // Bitmap bitmap = ThumbnailUtils.createImageThumbnail(notesList.get(i).toString(), MediaStore.Images.Thumbnails.KIND);

           // note.setImageBitmap(thumbImage);
            note.setImageURI(Uri.parse(notesList.get(i).toString()));

        }


        return convertView;
    }
}
