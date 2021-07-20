package com.example.timetable1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReviewTextNotesActivity extends AppCompatActivity implements TextNotesAdapter.ItemClickListener {


    private List<TextNote> notesList = new ArrayList<>();
    private RecyclerView notesRecyclerView;
    private TextNotesAdapter adapter;
    private FileHandler fileHandler = new FileHandler();
    private String subjectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_text_notes);
        subjectName = (String) getIntent().getSerializableExtra("subjectName");

        Button resizingButton = (Button) findViewById(R.id.resizingAddTextNoteButton);
        Button addNoteButton = (Button) findViewById(R.id.addLongTextNoteButton);
        EditText findNoteEditText = (EditText) findViewById(R.id.editFindNote);

        resizingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resizeAddNoteConstrain();
            }
        });
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });
        findNoteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findNotes(s);
                //Log.d("aaa", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        initializeNotes();


    }

    private void findNotes(CharSequence sequence) {
        List<TextNote> notesListTemp = new ArrayList<>();

        for (TextNote note : notesList) {
            if (note.getTopic().contains(sequence))
                notesListTemp.add(note);
        }

        setAdapter(notesListTemp);
    }

    public void addNote() {

        String topic = ((EditText) findViewById(R.id.editTopic)).getText().toString();
        String note = ((EditText) findViewById(R.id.editLongNote)).getText().toString();

        if (fileHandler.addTextNote(subjectName, topic, note)) {
            Toast.makeText(this, getString(R.string.added), Toast.LENGTH_LONG).show();
            initializeNotes();
        }
    }


    public void resizeAddNoteConstrain() {
        ConstraintLayout viewById = (ConstraintLayout) findViewById(R.id.resizingAddTextNoteConstrain);
        int visibility = viewById.getVisibility();

        if (visibility == View.GONE) {
            viewById.setVisibility(View.VISIBLE);
        } else {
            viewById.setVisibility(View.GONE);
        }

    }

    private void initializeNotes() {
        notesRecyclerView = (RecyclerView) findViewById(R.id.todosView);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notesList = fileHandler.loadTextNotesFromDevice(subjectName);
        /*notesList.add(new TextNote("aaaa","bbb"));
        notesList.add(new TextNote("bbbb","ccc"));
        notesList.add(new TextNote("cccc","ddd"));
*/
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

    @Override
    public void onDeleteClick(int position) {
        fileHandler.removeTextNote(subjectName, position);
        initializeNotes();
    }
}