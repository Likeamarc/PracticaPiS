package com.example.practicapis.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.practicapis.entities.Note;

import java.util.List;

public class HomePageViewModel extends BaseObservable{

    private MutableLiveData<List<Note>> notes;

    public LiveData<List<Note>> getNotes() {
        if (notes == null) {
            notes = new MutableLiveData<List<Note>>();
            loadNotes();
        }
        return notes;
    }

    private void loadNotes(){

    }

}
