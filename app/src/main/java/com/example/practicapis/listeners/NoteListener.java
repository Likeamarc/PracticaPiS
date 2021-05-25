package com.example.practicapis.listeners;

import com.example.practicapis.Model.Note;

public interface NoteListener {

    void onNoteClicked(Note note, int position);
}