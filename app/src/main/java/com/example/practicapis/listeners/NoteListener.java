package com.example.practicapis.listeners;

import com.example.practicapis.entities.Note;

public interface NoteListener {

    void onNoteClicked(Note note, int position);
}