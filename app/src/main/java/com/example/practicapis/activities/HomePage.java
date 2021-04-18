package com.example.practicapis.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.practicapis.Adapter;
import com.example.practicapis.ExampleItem;
import com.example.practicapis.R;
import com.example.practicapis.adapters.NotesAdapter;
import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.entities.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;

public class HomePage extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Note> notesList;
    private NotesAdapter notesAdapter;
    private static final int REQUEST_CODE_ADD_NOTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton addNote;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);


        addNote = findViewById(R.id.addNoteButton);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(HomePage.this, new_note.class),
                        REQUEST_CODE_ADD_NOTE);
            }
        });

        getNotes();


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList);
        mRecyclerView.setAdapter(notesAdapter);



    }

    private void getNotes(){

        class getNotesText extends AsyncTask<Void, Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NoteDatabase.getDatabase(getApplicationContext()).noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if(notesList.size() == 0){
                    notesList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else{
                    notesList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                }
                mRecyclerView.smoothScrollToPosition(0);
            }
        }
        new getNotesText().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getNotes();
        }
    }
}
