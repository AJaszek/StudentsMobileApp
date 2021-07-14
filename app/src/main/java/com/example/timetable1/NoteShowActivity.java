package com.example.timetable1;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class NoteShowActivity extends Activity {

    private ViewGroup mainLayout;
    ScaleGestureDetector scaleGestureDetector;
    ImageView fullNote;
    Button zoomInBtn;
    Button zoomOutBtn;
    android.graphics.Matrix matrix;
    Float scaleFactor = 1f;
    private int xDelta;
    private int yDelta;
    //Matrix matrix;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_show);

        fullNote = findViewById(R.id.fullNote);
        zoomInBtn = findViewById(R.id.zoomIn);
        zoomOutBtn = findViewById(R.id.zoomOut);
       // mainLayout = (RelativeLayout) findViewById(R.id.main);

        Intent callingActivityIntent = getIntent();

        if(callingActivityIntent != null){
            Uri imageUri = callingActivityIntent.getData();
            if(imageUri != null && fullNote != null){
                fullNote.setImageURI(imageUri);

            }
            //String aa = imageUri.
        }
        matrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        fullNote.setOnTouchListener(onTouchListener());
        //String noteFromIntent = getIntent().getExtras().getString("noteImg");

        //fullNote.setImageURI(Uri.parse(noteFromIntent));
        zoomInBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fullNote.setScaleX((float)(fullNote.getScaleX()+0.5));
                fullNote.setScaleY((float)(fullNote.getScaleY()+0.5));
            }
        });
        zoomOutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fullNote.setScaleX((float)(fullNote.getScaleX()-0.5));
                fullNote.setScaleY((float)(fullNote.getScaleY()-0.5));
            }
        });
    }




    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int x = (int) motionEvent.getRawX();
                final int y = (int) motionEvent.getRawY();

                int pointerL = motionEvent.getPointerCount();
                //Log.d("aaa", String.valueOf(pointerL));
                if (pointerL == 1) {
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            xDelta = x - fullNote.getLeft();
                            yDelta = y - fullNote.getTop();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            fullNote.setLeft(x - xDelta);
                            fullNote.setTop(y - yDelta);
                            break;

                    }
                }
                return true;
            }
        };
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scaleFactor = scaleFactor * detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5f));
            matrix.setScale(scaleFactor, scaleFactor);
            fullNote.setImageMatrix(matrix);

            return true;
        }
    }


}

