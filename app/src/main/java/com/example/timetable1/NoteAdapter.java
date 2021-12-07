package com.example.timetable1;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Cursor noteAdapterCursor;
    private final Activity activity;
    private OnCilckThumbListener onCilckThumbListener;
    private CharSequence dateToFind = "";

    public interface OnCilckThumbListener{
        void OnClick(Uri imageUri);
    }
    public NoteAdapter(Activity activity) {
        this.activity = activity;
        this.onCilckThumbListener = (OnCilckThumbListener) activity;

    }

    public void setDateToFind(CharSequence dateToFind){
        this.dateToFind = dateToFind;
    }
    /*public static CharSequence getDateToFind(){
        return this.dateToFind;
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_row, parent, false);
        return new ViewHolder(view,dateToFind);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Log.d("aaa", dateToFind.toString());
        //NoteAdapter.getDateToFind();
        Bitmap bitmap = getBitmapFromNoteReview(position, holder.noteDate, dateToFind);

        if(bitmap != null){
            holder.getImageView().setImageBitmap(bitmap);
        }else {
            //holder.notNesseseryView.setVisibility(View.GONE);
            holder.notNesseseryView.removeAllViews();
           // holder.notNesseseryView.

        }
    }

    @Override
    public int getItemCount() {
        return (noteAdapterCursor == null) ? 0 : noteAdapterCursor.getCount();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

         private final ImageView imageView;
         public TextView noteDate;
         public ConstraintLayout notNesseseryView;
        private CharSequence dateToFind = "";

        public ViewHolder(@NonNull View itemView, CharSequence dateToFind) {
            super(itemView);
            this.dateToFind = dateToFind;
            noteDate = (TextView) itemView.findViewById(R.id.noteDate);
            notNesseseryView = (ConstraintLayout) itemView.findViewById(R.id.subjectAllConstrain);
            imageView = (ImageView) itemView.findViewById(R.id.noteImage);
            imageView.setOnClickListener(this);
        }
        public ImageView getImageView(){
            return imageView;
        }

        @Override
        public void onClick(View view) {
            getOnClickUri(getAdapterPosition());
        }
    }

    private Cursor swapCursor(Cursor cursor){
        if(noteAdapterCursor == cursor){
            return null;
        }
        Cursor oldCursor = noteAdapterCursor;
        this.noteAdapterCursor = cursor;
        if(cursor != null){
            this.notifyDataSetChanged();
        }
        return  oldCursor;
    }

    public  void changeCursor(Cursor cursor){
        Cursor oldCursor = swapCursor(cursor);
        if(oldCursor != null){
            oldCursor.close();
        }
    }
    private String trimDate(String notTrimmed){
        //String trimmed="";

        String trimmed = new File(notTrimmed).getName();
        int pos = trimmed.lastIndexOf(".");
        trimmed = trimmed.substring(0, pos);

        String[] arr = trimmed.split("_");

        trimmed = arr[1] +"."+ arr[0] +" "+ arr[2] +":"+arr[3];





        return trimmed;
    }
    private Bitmap getBitmapFromNoteReview(int position, TextView noteDate, CharSequence dateToFind){
        int idIndex = noteAdapterCursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
        int mediaTypeIndex = noteAdapterCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);

        noteAdapterCursor.moveToPosition(position);



        switch (noteAdapterCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:

                int dataIndex = noteAdapterCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                String date = trimDate(noteAdapterCursor.getString(dataIndex));

                if(dateToFind!="" && !date.contains(dateToFind))
                    return null;
                noteDate.setText(date);

                return MediaStore.Images.Thumbnails.getThumbnail(
                    activity.getContentResolver(),
                        noteAdapterCursor.getLong(idIndex),
                        MediaStore.Images.Thumbnails.MICRO_KIND,
                        null
                );
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                return MediaStore.Video.Thumbnails.getThumbnail(
                        activity.getContentResolver(),
                        noteAdapterCursor.getLong(idIndex),
                        MediaStore.Video.Thumbnails.MICRO_KIND,
                        null
                );
            default:
                return null;

        }
    }
    private  void getOnClickUri(int position){
        int mediaTypeIndex = noteAdapterCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
        int dataIndex = noteAdapterCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        noteAdapterCursor.moveToPosition(position);

        switch (noteAdapterCursor.getInt(mediaTypeIndex)) {
            case  MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                String dataString = noteAdapterCursor.getString(dataIndex);
                Uri imageUri = Uri.parse("file://" + dataString);
                onCilckThumbListener.OnClick(imageUri);
                break;
            case  MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:

                break;
            default:

        }

    }
}
