package com.h2kr.simplenotepad.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.h2kr.simplenotepad.model.Note;

@Database(entities = Note.class, version = 1)
public abstract class NotesDB extends RoomDatabase {

    public abstract NotesDao notesDao();

    public static final String DB_NAME = "notesDB";
    private static NotesDB instance;

    public static NotesDB getInstance(Context context) {

        if(instance == null) {
            instance = Room.databaseBuilder(context, NotesDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }


}
