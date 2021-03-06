package com.example.timetable1;

public class Todo {
    private String topic;
    private String description;
    private boolean done;
    private boolean clicked;
    private int style;

    public Todo(String topic, String note, boolean done, int style) {
        this.topic = topic;
        this.description = note;
        this.done = done;
        this.clicked = false;
        this.style = style;
    }

    public int getStyle() {
        return style;
    }

    public boolean isDone() {
        return done;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void changeClickedState(){
        this.clicked = !this.clicked;
    }

    public boolean getDone() {
        return this.done;
    }
}
