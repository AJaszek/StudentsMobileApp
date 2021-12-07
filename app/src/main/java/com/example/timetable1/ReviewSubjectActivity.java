package com.example.timetable1;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReviewSubjectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NoteAdapter.OnCilckThumbListener {

    Subject subject;
    private List<String> hoursList = new ArrayList<>();

    private final static int READ_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private final static int WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private final static int CAMERA_PERMISSION_RESULT = 0;
    private final static int MEDIASTORE_LOADER_ID = 0;
    private RecyclerView thumbnailRecyclerView;
    private NoteAdapter noteAdapter;
    File directoryToRead;
    ArrayList<File> notesList;


    Uri imageUri;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_subject);

        subject = (Subject) getIntent().getSerializableExtra("subject");
        hoursList = (List<String>) getIntent().getSerializableExtra("hours");


        initializeView();
        checkReadExternalStoragePermission();





        directoryCheckAndCreate();

        Button addTextNoteButton = findViewById(R.id.addTextNoteButton);
        Button addNoteButton = findViewById(R.id.addNoteButton);
        EditText findNoteEditText = (EditText) findViewById(R.id.editFindPhotoNote);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(checkWriteExternalStoragePermission() && checkCameraPermission()) openCamera();
            }
        });
        addTextNoteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewTextNotesActivity.class);
                intent.putExtra("subjectName", subject.getName());
                startActivity(intent);
            }
        });
        findNoteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findNotes(s);

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        TextView subjectName = (TextView) findViewById(R.id.subjectRewName);
        TextView teacherName = (TextView) findViewById(R.id.subjectTeacherName);
        TextView hoursReviewText = (TextView) findViewById(R.id.hoursReview);
        subjectName.setText(subject.getName());
        teacherName.setText(subject.getTeacherName());

        String hours = "";

        for(String hour : hoursList) {
            hours += hour + "\n";
            //Log.d("aaa", subject);
        }
        hoursReviewText.setText(hours);
    }
    private void initializeView(){
        thumbnailRecyclerView = (RecyclerView) findViewById(R.id.notesView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        thumbnailRecyclerView.setLayoutManager(gridLayoutManager);
        noteAdapter = new NoteAdapter(this);
        thumbnailRecyclerView.setAdapter(noteAdapter);
    }
    private void findNotes(CharSequence s) {
        noteAdapter.setDateToFind(s);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  READ_EXTERNAL_STORAGE_PERMISSION_RESULT:
                /*if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this, "Now have acces to view thumbs", Toast.LENGTH_SHORT).show();
                    LoaderManager.getInstance(this).initLoader(MEDIASTORE_LOADER_ID, null, this);
                }*/
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
    private void checkReadExternalStoragePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                LoaderManager.getInstance(this).initLoader(MEDIASTORE_LOADER_ID, null, this);
                //getSupportLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
            } else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "App need to view thumbnails", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        } else{
            LoaderManager.getInstance(this).initLoader(MEDIASTORE_LOADER_ID, null, this);
        }
    }
    private boolean checkWriteExternalStoragePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                LoaderManager.getInstance(this).initLoader(MEDIASTORE_LOADER_ID, null, this);
                return true;
                //getSupportLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
            } else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "App need to view thumbnails", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);
                checkWriteExternalStoragePermission();
            }
        } else{
            LoaderManager.getInstance(this).initLoader(MEDIASTORE_LOADER_ID, null, this);
            return true;
        }
        return false;
    }
    private boolean checkCameraPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                LoaderManager.getInstance(this).initLoader(MEDIASTORE_LOADER_ID, null, this);
                return true;
                //getSupportLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
            } else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(this, "App need to view thumbnails", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_RESULT);
                checkCameraPermission();
            }
        } else{
            LoaderManager.getInstance(this).initLoader(MEDIASTORE_LOADER_ID, null, this);
            return true;
        }
        return false;
    }

    private ArrayList<File> imageReader(File externalStorageDirectory) {

        ArrayList<File> b = new ArrayList<>();
        File[] files = externalStorageDirectory.listFiles();



       for(int i = 0; i< files.length; i++){

            if(files[i].getName().endsWith(".jpg")){

                b.add(files[i]);
            }



        }

        return b;

    }
    private String createTimeStamp(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_HH_mm_ss");
        return sdf.format(calendar.getTime());
    }
    private void openCamera() {

        String formattedDate = createTimeStamp();


        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, "New note");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From camera");

        values.put(MediaStore.Images.Media.DATA, Environment.getExternalStorageDirectory()
                + "/TimeTable/Notes/" + subject.getName() + "/" + formattedDate + ".jpg");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //Log.d("aaa","aa");
        //Uri uriSavedImage=Uri.fromFile(new File("/sdcard/TimeTable/Notes/flashCropped.png"));

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }


    private void directoryCheckAndCreate(){
        directoryToRead = new File(Environment.getExternalStorageDirectory() + "/TimeTable/Notes/"+ subject.getName());
        if (!directoryToRead.exists()) {
            directoryToRead.mkdirs();
        }
        directoryToRead.mkdirs();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            finish();
            startActivity(getIntent());

        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
                //MediaStore.Images.Media.DATA
        };
       /* String selection =  MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;*/

       // String selection =  MediaStore.Files.FileColumns.RELATIVE_PATH + "=\"/TimeTable/Notes\"";
      //  String selection =  MediaStore.Files.FileColumns.RELATIVE_PATH + "="

        String selection = "("+MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO +") AND " +
                MediaStore.Images.Media.DATA + " like ? ";
        String selectionArgs[] = new String[] {"%TimeTable/Notes/"+subject.getName()+"%"};


        //String selectionArgs[] = new String[] {"%/sdcard/TimeTable/Notes%"};
        //Cursor mCursor = mContentResolver.query(mImageUri, columns, MediaStore.Images.Media.DATA + " like ? ",new String[] {"%/YourFolderName/%"}, null);

        CursorLoader cursor = new CursorLoader(
                this,
                //MediaStore.Files.getContentUri("external"),
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                selectionArgs,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
        return  cursor;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
       // Log.d("aaa", data.getString(data.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)));
       // Log.d("aaa",  );
        noteAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        noteAdapter.changeCursor(null);
    }

    @Override
    public void OnClick(Uri imageUri) {
        //Toast.makeText(this, "ImageUti: "+imageUri.toString(), Toast.LENGTH_SHORT).show();
        Intent showNoteIntent = new Intent(this, NoteShowActivity.class);
        showNoteIntent.setData(imageUri);
        startActivity(showNoteIntent);
    }
}