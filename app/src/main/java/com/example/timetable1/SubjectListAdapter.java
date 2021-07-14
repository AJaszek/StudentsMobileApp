package com.example.timetable1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {
    private List<Subject> subjectList;
    private Context context;
    private ClickListener mclickListener;

    public SubjectListAdapter(List<Subject> subjectList, Context context, ClickListener clickListener) {
        this.subjectList = subjectList;
        this.context = context;
        this.mclickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_list_row, parent, false);
        return new ViewHolder(view, mclickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Subject subject = subjectList.get(position);

        holder.subjectName.setText(subject.getName());
       // holder.subjectHours.setText(subject.getStartHour() + " - " + subject.getFinishHour());
       // holder.roomNumber.setText(subject.getRoomNumber());

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView subjectName;
        public TextView subjectHours;
        public TextView roomNumber;
        public Button delButton;

        ClickListener clickListener;

        public ViewHolder(@NonNull View itemView, final ClickListener clickListener) {
            super(itemView);

            subjectName = (TextView) itemView.findViewById(R.id.subjectName);
          //  subjectHours = (TextView) itemView.findViewById(R.id.subjectHours);
          //  roomNumber = (TextView) itemView.findViewById(R.id.roomNumber);
          //  delButton = (Button) itemView.findViewById(R.id.delButton);

            itemView.setOnClickListener(this);
            this.clickListener = clickListener;

           /* delButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    clickListener.onDeleteClick(getAdapterPosition());
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            clickListener.onSubjectClick(getAdapterPosition());
        }
    }

    public interface ClickListener{
        void onSubjectClick(int position);
        void onDeleteClick(int position);


    }
}
