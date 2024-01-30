package com.example.jakoo.entity;

import java.sql.Timestamp;

public class WorkdaysNote {
    private Long workdaysNoteId;
    private Long emloyeeId;
    private Long workdayId;
    private Long noteId;
    private Long paid_additional_time;
    private Long unpaid_additional_time;
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

    public Long getPaid_additional_time() {
        return paid_additional_time;
    }

    public void setPaid_additional_time(Long paid_additional_time) {
        this.paid_additional_time = paid_additional_time;
    }

    public Long getUnpaid_additional_time() {
        return unpaid_additional_time;
    }

    public void setUnpaid_additional_time(Long unpaid_additional_time) {
        this.unpaid_additional_time = unpaid_additional_time;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}

