package com.example.practicapis.viewmodel;

import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.entities.Login;
import com.example.practicapis.entities.Note;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotesViewModel extends AndroidViewModel {

    private static NotesViewModel vmInstance;
    private NoteDatabase noteDatabase;
    public List<Note> noteList;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        noteDatabase = NoteDatabase.getDatabase(application);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteList = noteDatabase.noteDao().getAllNotes();
            }
        });

    }

    @MainThread
    public static NotesViewModel get(@NonNull Application application){
        if(vmInstance == null){
            vmInstance = new NotesViewModel(application);
        }
        return vmInstance;
    }

    public List<Note> getNoteList() throws ExecutionException, InterruptedException {
        Future<List<Note>> futureNoteDatabase = Executors.newSingleThreadExecutor().submit(new Callable<List<Note>>() {
            @Override
            public List<Note> call() throws Exception {
                return noteDatabase.noteDao().getAllNotes();
            }
        });

        noteList = futureNoteDatabase.get();
        return noteList;
    }

    public void insertNote(Note note){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDatabase.noteDao().insertNote(note);
            }
        });
    }

    public void deleteNote(Note note){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDatabase.noteDao().deleteNote(note);
            }
        });
    }

    public void updateNote(Note note){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDatabase.noteDao().updateNote(note);
            }
        });
    }

}
