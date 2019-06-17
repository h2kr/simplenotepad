package com.h2kr.simplenotepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.h2kr.simplenotepad.db.NotesDB;
import com.h2kr.simplenotepad.db.NotesDao;
import com.h2kr.simplenotepad.model.Note;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditText inputText;
    private NotesDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY = "note_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        //инпуат текзд
        inputText = findViewById(R.id.input_text);

        dao = NotesDB.getInstance(this).notesDao();

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            temp = dao.getNoteById(id);
            inputText.setText(temp.getNoteText());
        } else inputText.setFocusable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        String text = inputText.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime(); // get  system time
            // if  exist update els crete new
            if (temp == null) {
                temp = new Note(text, date);
                dao.insertNote(temp); // create new note and inserted to database
            } else {
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp); // change text and date and update note on database
            }
            finish(); // return to the MainActivity
        }
        }

    }
