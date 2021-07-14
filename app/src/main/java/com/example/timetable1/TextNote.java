package com.example.timetable1;

public class TextNote {
    private String topic;
    private String note;
    private boolean clicked;

    public TextNote(String topic, String note) {
        this.topic = topic;
        this.note = note;
        this.clicked = false;
    }

    public String getTopic() {
        return topic;
    }

    public String getNote() {
        return note;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void changeClickedState(){
        this.clicked = !this.clicked;
    }
}
