package com.example.timetable1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<Subject> subjectList;
    private ClickListener mclickListener;
    int noteType;
    String noteString;
    String date;

    public SubjectAdapter(List<Subject> subjectList, ClickListener clickListener, String date) {
        this.subjectList = subjectList;
        this.mclickListener = clickListener;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_row, parent, false);
        return new ViewHolder(view, mclickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Subject subject = subjectList.get(position);
        setSubjectTexts(holder, subject);

        int noteIndex;
        if((noteIndex = subject.findNoteIndex(date)) != -1){
            String note = subject.getNote(noteIndex);
            noteString = note;
            writeNoteAtSubject(holder, note);
        }
        else{
            noteNotExist(holder);
        }

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                noteType = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public LinearLayout editNoteLayout;
        public ConstraintLayout borderColorLayout;
        public TextView subjectName;
        public TextView subjectHours;
        public TextView roomNumber;
        public TextView subjectNote;
        public EditText editNote;
        public Button delButton;
        public Button addNoteButton;
        public Button delNoteButton;
        public Spinner spinner;

        ClickListener clickListener;


        public ViewHolder(@NonNull View itemView, final ClickListener clickListener) {
            super(itemView);

            ArrayList<String> noteTypes = new ArrayList<>();

            noteTypes.add(itemView.getResources().getString(R.string.other));
            noteTypes.add(itemView.getResources().getString(R.string.test));
            noteTypes.add(itemView.getResources().getString(R.string.quiz));
            noteTypes.add(itemView.getResources().getString(R.string.homework));

            spinner = (Spinner) itemView.findViewById(R.id.noteType);
            ArrayAdapter adapter = new ArrayAdapter(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, noteTypes );
            spinner.setAdapter(adapter);

            subjectName = (TextView) itemView.findViewById(R.id.subjectName);
            subjectHours = (TextView) itemView.findViewById(R.id.subjectHours);
            roomNumber = (TextView) itemView.findViewById(R.id.roomNumber);
            subjectNote = (TextView) itemView.findViewById(R.id.subjectNote);
            editNote = (EditText) itemView.findViewById(R.id.editNote);
            delButton = (Button) itemView.findViewById(R.id.delButton);
            addNoteButton = (Button) itemView.findViewById(R.id.addTextNoteButton);
            delNoteButton = (Button) itemView.findViewById(R.id.delNoteButton);
            editNoteLayout = (LinearLayout) itemView.findViewById(R.id.editNoteLayout);
            borderColorLayout = (ConstraintLayout) itemView.findViewById(R.id.subjectAllConstrain);

            itemView.setOnClickListener(this);
            this.clickListener = clickListener;

            delButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    clickListener.onDeleteClick(getAdapterPosition());
                }
            });
            addNoteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    clickListener.addNoteClick(getAdapterPosition(), editNote.getText().toString(), noteType);
                }
            });
            delNoteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    clickListener.delNoteClick(getAdapterPosition(), noteString);
                }
            });
        }

        @Override
        public void onClick(View view) {
            clickListener.onSubjectClick(getAdapterPosition());
        }
    }

    public interface ClickListener{
        void onSubjectClick(int position);
        void onDeleteClick(int position);
        void addNoteClick(int position, String note, int noteType);
        void delNoteClick(int position, String note);


    }

    private void setSubjectTexts(ViewHolder holder, Subject subject) {
        holder.subjectName.setText(subject.getName());
        holder.subjectHours.setText(subject.getStartHour() + " - " + subject.getFinishHour());
        holder.roomNumber.setText(subject.getRoomNumber());
    }
    private void noteNotExist(ViewHolder holder) {
        holder.delNoteButton.setVisibility(View.GONE);
        holder.subjectNote.setVisibility(View.GONE);
    }
    private void writeNoteAtSubject(ViewHolder holder, String note) {
        String noteWithoutAttribiutes = " - " + note.substring(note.indexOf('.') + 1);
        holder.subjectNote.setText(noteWithoutAttribiutes);
        holder.addNoteButton.setVisibility(View.GONE);
        holder.editNoteLayout.setVisibility(View.GONE);
        switch (note.charAt(0)){
            case '1':
                holder.borderColorLayout.setForeground(holder.borderColorLayout.getResources().getDrawable(R.drawable.button_transparent_backgroun_red));
                break;
            case '2':
                holder.borderColorLayout.setForeground(holder.borderColorLayout.getResources().getDrawable(R.drawable.button_transparent_backgroun_green));
                break;
            case '3':
                holder.borderColorLayout.setForeground(holder.borderColorLayout.getResources().getDrawable(R.drawable.button_transparent_backgroun_blue));
                break;
        }
    }

}
