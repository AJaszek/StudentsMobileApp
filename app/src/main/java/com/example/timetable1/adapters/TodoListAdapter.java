package com.example.timetable1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable1.R;
import com.example.timetable1.TextNote;
import com.example.timetable1.TextNotesAdapter;
import com.example.timetable1.Todo;

import java.util.List;

public class TodoListAdapter  extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>{
    private List<Todo> todoList;
    private TodoListAdapter.ItemClickListener clickListener;

    public TodoListAdapter(Context context, List<Todo> todoList) {
        this.todoList = todoList;
    }

    @Override
    public TodoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.to_do_row, parent, false);
        return new TodoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoListAdapter.ViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.doneCheck.setText(todo.getTopic());
        holder.description.setText(todo.getDescription());
        holder.doneCheck.setChecked(todo.isDone());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void updateTodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //public TextView topic;
        public TextView description;
        public CheckBox doneCheck;
        public Button deleteNoteButton;

        ViewHolder(View itemView) {
            super(itemView);
            //topic = itemView.findViewById(R.id.textTodoTopic);
            description = itemView.findViewById(R.id.textTodoDescription);
            deleteNoteButton = itemView.findViewById(R.id.deleteTodoButton);
            doneCheck = itemView.findViewById(R.id.checkTodoDone);
            itemView.setOnClickListener(this);

            deleteNoteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    clickListener.onDeleteClick(getAdapterPosition());
                }
            });
            doneCheck.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    clickListener.onCheckBoxClick(getAdapterPosition(), (CheckBox) view);
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Todo getItem(int id) {
        return todoList.get(id);
    }

    public void setClickListener(TodoListAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onDeleteClick(int position);
        void onCheckBoxClick(int position, CheckBox checkBox);
    }
}
