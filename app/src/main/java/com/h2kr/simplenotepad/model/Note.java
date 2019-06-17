package com.h2kr.simplenotepad.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity( tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "text")
    private String noteText;

    @ColumnInfo(name = "date")
    private long noteDate;

    @Ignore
    private boolean checked = false;

    public Note(String noteText, long noteDate) {
        this.noteText = noteText;
        this.noteDate = noteDate;
    }

    public Note() {

    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteText() {
        return noteText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}
