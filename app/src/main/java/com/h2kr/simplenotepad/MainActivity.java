package com.h2kr.simplenotepad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.h2kr.simplenotepad.adapters.NotesAdapter;
import com.h2kr.simplenotepad.callbacks.MainActionModeCallback;
import com.h2kr.simplenotepad.callbacks.NoteEventListeren;
import com.h2kr.simplenotepad.db.NotesDB;
import com.h2kr.simplenotepad.db.NotesDao;
import com.h2kr.simplenotepad.model.Note;
import com.h2kr.simplenotepad.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;

import static com.h2kr.simplenotepad.EditNoteActivity.NOTE_EXTRA_KEY;

public class MainActivity extends AppCompatActivity implements NoteEventListeren {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDao notesDao;
    private FloatingActionButton fab;
    private MainActionModeCallback modeCallback;
    private int checkedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewNote();
            }
        });

        notesDao = NotesDB.getInstance(this).notesDao();
    }

    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Note> list = notesDao.getNotes();
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);

        this.adapter.setListeren(this);
        this.recyclerView.setAdapter(adapter);
    }

    private void addNewNote() {

    }

    private void onAddNewNote() {
        startActivity(new Intent(this, EditNoteActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void onNoteClick(Note note) {
        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_KEY, note.getId());
        startActivity(edit);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        adapter.setMultiCheckMode(false);
        adapter.setListeren(this);
        fab.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onNoteLongClick(Note note) {
        note.setChecked(true);
        checkedCount = 1;
        adapter.setMultiCheckMode(true);

        adapter.setListeren(new NoteEventListeren() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                if(note.isChecked())
                    checkedCount++;
                else
                    checkedCount--;

                if (checkedCount > 1) {
                    modeCallback.changeShareItemVisible(false);
                } else modeCallback.changeShareItemVisible(true);

                if (checkedCount == 0) {
                    //  finish multi select mode wen checked count =0
                    modeCallback.getAction().finish();
                }

                modeCallback.setCount(checkedCount + "/" + notes.size());


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Note note) {

            }
        });

        modeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.getItemId() == R.id.action_delete_notes)
                    onDeleteMultiNotes();
                else if(item.getItemId() == R.id.action_share_note)
                    onShareNote();

                mode.finish();

                return false;
            }
        };

        startActionMode(modeCallback);

        fab.setVisibility(View.GONE);

        modeCallback.setCount(checkedCount + "/" + notes.size());


//        Log.d(TAG, "onNoteLongClick: " + note.getId());
//
//        new AlertDialog.Builder(this).
//                setTitle("Че хош с этим сделать?").
//                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                notesDao.deleteNote(note);
//                loadNotes(); // рефреш как доте кароч
//            }
//        })
//                .setNegativeButton("share", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Intent share = new Intent(Intent.ACTION_SEND);
//
//                        String text = note.getNoteText() + "\n\nСоздано в Simple Notepad - Блокнот блять";
//
//                        share.setType("text/pain");
//                        share.putExtra(Intent.EXTRA_TEXT, text);
//                        startActivity(share);
//
//                    }
//                })
//                .create()
//                .show();
    }

    private void onShareNote() {
        Note note = adapter.getCheckedNotes().get(0);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = note.getNoteText() + "\n\n Create on : " +
                NoteUtils.dateFromLong(note.getNoteDate()) + "\n  Simple Notepad - Блокнот";
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);
    }

    private void onDeleteMultiNotes() {

        List<Note> chackedNotes = adapter.getCheckedNotes();
        if (chackedNotes.size() != 0) {
            for (Note note : chackedNotes) {
                notesDao.deleteNote(note);
            }
            // refresh Notes
            loadNotes();
            Toast.makeText(this, chackedNotes.size() + " Записи удалены ! ", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, " Записи не выбраны ! ", Toast.LENGTH_SHORT).show();

    }
}
