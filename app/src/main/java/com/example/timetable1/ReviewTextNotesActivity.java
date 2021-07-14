package com.example.timetable1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ReviewTextNotesActivity extends AppCompatActivity implements TextNotesAdapter.ItemClickListener{



    private List<TextNote> notesList = new ArrayList<>();
    private RecyclerView notesRecyclerView;
    private TextNotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_text_notes);

        initializeNotes();


    }

    private void initializeNotes() {
        notesRecyclerView = (RecyclerView) findViewById(R.id.NotesView);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notesList.add(new TextNote("aaaa","bbb"));
        notesList.add(new TextNote("bbbb","ccc"));
        notesList.add(new TextNote("cccc","ddd"));

        setAdapter(notesList);
    }

    public void setAdapter(List<TextNote> notesList) {
        adapter = new TextNotesAdapter(getApplicationContext(), notesList);
        adapter.setClickListener(this);
        notesRecyclerView.setAdapter(adapter);
    }


   public void resizeNoteView(int position) {
       ConstraintLayout viewById = (ConstraintLayout) notesRecyclerView.findViewHolderForLayoutPosition(position)
               .itemView.findViewById(R.id.resizingTextNoteConstrain);

       boolean clicked = notesList.get(position).isClicked();
       if (clicked) {
           viewById.setVisibility(View.GONE);
       } else {
           viewById.setVisibility(View.VISIBLE);
       }
       notesList.get(position).changeClickedState();
   }

    @Override
    public void onItemClick(View view, int position) {
        resizeNoteView(position);
    }
}