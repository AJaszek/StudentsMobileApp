package com.example.timetable1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TextNotesAdapter extends RecyclerView.Adapter<TextNotesAdapter.ViewHolder> {

    private List<TextNote> notesList;
    private ItemClickListener clickListener;

    TextNotesAdapter(Context context, List<TextNote> notesList) {
        this.notesList = notesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_text_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextNote note = notesList.get(position);
        holder.topic.setText(note.getTopic());
        holder.note.setText(note.getNote());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView topic;
        public TextView note;

        ViewHolder(View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.textNoteTopic);
            note = itemView.findViewById(R.id.textNote);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    TextNote getItem(int id) {
        return notesList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

