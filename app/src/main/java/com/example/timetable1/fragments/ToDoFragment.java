package com.example.timetable1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.timetable1.FileHandler;
import com.example.timetable1.R;
import com.example.timetable1.Todo;
import com.example.timetable1.adapters.TodoListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ToDoFragment extends Fragment implements TodoListAdapter.ItemClickListener{

    private List<Todo> todoList = new ArrayList<>();

    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private FileHandler fileHandler = new FileHandler();

    public ToDoFragment() {
        // Required empty public constructor
    }



   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    public void setAdapter(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.todosView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        todoList = fileHandler.loadTodoFromDevice();

        adapter = new TodoListAdapter(getContext(), todoList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_to_do, container, false);



        Button resizingButton = (Button) root.findViewById(R.id.resizingAddTodoButton);
        resizingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                resizeAddTodoConstrain();
            }
        });
        Button addTodoButton = (Button) root.findViewById(R.id.addTodoButton);
        addTodoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addTodo();
            }
        });


        setAdapter(root);

        return root;
    }
    public void addTodo() {



        String topic = ((EditText) getActivity().findViewById(R.id.editTodoTopic)).getText().toString();
        String description = ((EditText) getActivity().findViewById(R.id.editTodoDescription)).getText().toString();


        if(fileHandler.addTodo(topic, description)) {
            Toast.makeText(getContext(), getString(R.string.added), Toast.LENGTH_LONG).show();
            todoList.add(0,new Todo(topic,description,false));
            adapter.updateTodoList(todoList);
            adapter.notifyDataSetChanged();
            //initializeNotes();
        }
        // if( testRegex(startHour,finishHour)){


    }
    private void resizeAddTodoConstrain() {
        LinearLayout viewById = (LinearLayout) getActivity().findViewById(R.id.resizingAddTodoConctrain);
        Button resizingButton = (Button) getActivity().findViewById(R.id.resizingAddTodoButton);
        //.itemView.findViewById(R.id.resizingSubjectConstrain);

        ;

        //int visibility = viewById.getVisibility();
        //boolean clicked = todayListSubjects().get(position).clicked;
        if (resizeView(viewById)) {
            resizingButton.setText(getResources().getString(R.string.dontAddToDo));
        } else {
            resizingButton.setText(getResources().getString(R.string.addToDo));
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        View description = view.findViewById(R.id.resizingDescriptionConstrain);
        resizeView(description);

    }

    private boolean resizeView(View view) {
        int visibility = view.getVisibility();
        if (visibility == View.GONE) {
            view.setVisibility(View.VISIBLE);
            return true;
        } else {
            view.setVisibility(View.GONE);
            return false;
        }
    }

    @Override
    public void onDeleteClick(int position) {
        int todoPosition = todoList.size() - position - 1;
        fileHandler.removeTodo(todoPosition);
        todoList.remove(position);
        adapter.updateTodoList(todoList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckBoxClick(int position, CheckBox checkBox) {
        int todoPosition = todoList.size() - position - 1;
       // CheckBox doneCheck = getActivity().findViewById(R.id.checkTodoDone);
        checkBox.setChecked(fileHandler.changeStateTodo(todoPosition, todoList.get(position).getDone()));



    }
}