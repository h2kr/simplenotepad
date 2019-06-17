package com.h2kr.simplenotepad.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import com.h2kr.simplenotepad.R;
import com.h2kr.simplenotepad.callbacks.NoteEventListeren;
import com.h2kr.simplenotepad.model.Note;
import com.h2kr.simplenotepad.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.noteHolder> {


    private ArrayList<Note> notes;
    private Context context;
    private NoteEventListeren listeren;
    private boolean multiCheckMode = false;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public noteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.notepad_layout, viewGroup, false);

        return new noteHolder(v);
    }

    @Override
    public void onBindViewHolder( noteHolder noteHolder, int position) {
        final Note note = getNote(position);
        if(note != null) {
            noteHolder.noteText.setText(note.getNoteText());
            noteHolder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));
        }

        noteHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeren.onNoteClick(note);
            }
        });

        noteHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listeren.onNoteLongClick(note);
                return false;
            }
        });

        if(multiCheckMode) {
            noteHolder.checkBox.setVisibility(View.VISIBLE);
            noteHolder.checkBox.setChecked(note.isChecked());
        }else noteHolder.checkBox.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private Note getNote(int position) {
        return notes.get(position);
    }

    class noteHolder extends RecyclerView.ViewHolder {
        TextView noteText, noteDate;
        CheckBox checkBox;

        public noteHolder(View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.note_text);
            noteDate = itemView.findViewById(R.id.note_date);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public void setListeren(NoteEventListeren listeren) {
        this.listeren = listeren;
    }

    public void setMultiCheckMode(boolean multiCheckMode) {
        this.multiCheckMode = multiCheckMode;
        notifyDataSetChanged();
    }

    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>();

        for (Note n : this.notes) {

            if(n.isChecked())
                checkedNotes.add(n);

        }

        return checkedNotes;
    }
}
