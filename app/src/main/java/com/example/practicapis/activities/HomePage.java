package com.example.practicapis.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.practicapis.R;
import com.example.practicapis.adapters.NotesAdapter;
import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.Model.Note;
import com.example.practicapis.listeners.NoteListener;
import com.example.practicapis.viewmodel.HomePageViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class HomePage extends AppCompatActivity implements NoteListener {
    private RecyclerView mRecyclerView;
    private List<Note> notesList;
    private List<Note> favouriteNotesList;
    private NotesAdapter notesAdapter;
    private  HomePageViewModel homePageViewModel;
    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;

    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);


        FloatingActionButton addNote;

        addNote = findViewById(R.id.addNoteButton);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(HomePage.this, new_note.class),
                        REQUEST_CODE_ADD_NOTE);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );

        notesList = new ArrayList<>();
        favouriteNotesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList, this);
        mRecyclerView.setAdapter(notesAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES, false);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(notesList.size() != 0){
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });

    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), new_note.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted){

        class getNotesText extends AsyncTask<Void, Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NoteDatabase.getDatabase(getApplicationContext()).noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if(requestCode == REQUEST_CODE_SHOW_NOTES){
                    notesList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if(requestCode == REQUEST_CODE_ADD_NOTE){
                    notesList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    mRecyclerView.smoothScrollToPosition(0);
                } else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                    notesList.remove(noteClickedPosition);

                    if(isNoteDeleted){
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    }else{
                        notesList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }
        new getNotesText().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if(data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra( "isNoteDeleted", false));
            }
        }
    }
}
