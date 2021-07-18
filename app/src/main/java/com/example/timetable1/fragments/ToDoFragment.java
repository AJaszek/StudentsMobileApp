package com.example.timetable1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.timetable1.R;
import com.example.timetable1.Todo;
import com.example.timetable1.adapters.TodoListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ToDoFragment extends Fragment implements TodoListAdapter.ItemClickListener{

    private List<Todo> todoList = new ArrayList<>();

    private RecyclerView recyclerView;
    private TodoListAdapter adapter;

    public ToDoFragment() {
        // Required empty public constructor
    }



   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    public void setAdapter(List<Todo> todoList) {

        adapter = new TodoListAdapter(getContext(), todoList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_to_do, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.todosView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Button resizingButton = (Button) root.findViewById(R.id.resizingAddTodoButton);
        resizingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                resizeAddTodaoConstrain();
            }
        });

        todoList.add(new Todo("aaa","bbbb",false));
        todoList.add(new Todo("bbb","cccc",true));
        todoList.add(new Todo("ccc","dddd",false));

        setAdapter(todoList);

        return root;
    }

    private void resizeAddTodaoConstrain() {
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

    }
}