package com.example.jakoo.entity;

import java.sql.Timestamp;

public class WorkdaysNote {
    private Long workdaysNoteId;
    private Long emloyeeId;
    private Long workdayId;
    private Long noteId;
    private Long workTime;
    private Timestamp date;

    public Long getWorkdaysNoteId() {
        return workdaysNoteId;
    }

    public void setWorkdaysNoteId(Long workdaysNoteId) {
        this.workdaysNoteId = workdaysNoteId;
    }

    public Long getEmloyeeId() {
        return emloyeeId;
    }

    public void setEmloyeeId(Long emloyeeId) {
        this.emloyeeId = emloyeeId;
    }

    public Long getWorkdayId() {
        return workdayId;
    }

    public void setWorkdayId(Long workdayId) {
        this.workdayId = workdayId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}

