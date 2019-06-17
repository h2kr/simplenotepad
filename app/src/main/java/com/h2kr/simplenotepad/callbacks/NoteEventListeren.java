package com.h2kr.simplenotepad.callbacks;

import com.h2kr.simplenotepad.model.Note;

public interface NoteEventListeren {

    /**
     * call wen note clicked.
     *
     * @param note: note item
     */
    void onNoteClick(Note note);

    /**
     * call wen long Click to note.
     *
     * @param note : item
     */
    void onNoteLongClick(Note note);

}
