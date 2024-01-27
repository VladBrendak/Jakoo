package com.example.jakoo.entity;

import java.sql.Timestamp;

public class Note {
    private Long noteId;
    private Long emloyeeId;
    private String text;
    private Timestamp date;

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getEmloyeeId() {
        return emloyeeId;
    }

    public void setEmloyeeId(Long emloyeeId) {
        this.emloyeeId = emloyeeId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}

