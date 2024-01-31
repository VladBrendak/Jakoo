package com.example.jakoo.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class WorkdaysNote {
    private Long workdaysNoteId;
    private Long emloyeeId;
    private Long workdayId;
    private String note;
    private Long paid_additional_time;
    private Long unpaid_additional_time;
    private Timestamp date;

    public WorkdaysNote() {
    }

    public WorkdaysNote(Long workdaysNoteId, Long emloyeeId, Long workdayId, String note, Long paid_additional_time, Long unpaid_additional_time, Timestamp date) {
        this.workdaysNoteId = workdaysNoteId;
        this.emloyeeId = emloyeeId;
        this.workdayId = workdayId;
        this.note = note;
        this.paid_additional_time = paid_additional_time;
        this.unpaid_additional_time = unpaid_additional_time;
        this.date = date;
    }

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    @Override
    public int hashCode() {
        return Objects.hash(emloyeeId);
    }
}

