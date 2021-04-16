package com.example.practicapis.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practicapis.R;
import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.entities.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class new_note extends AppCompatActivity {

    private EditText inputTitle, inputText;
    private TextView dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        inputTitle = findViewById(R.id.inputNoteTitle);
        inputText = findViewById(R.id.inputNote);
        dateTime = findViewById(R.id.textDataTime);

        dateTime.setText(
                new SimpleDateFormat("EEE dd MMMM yyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        ImageView imageDone = findViewById(R.id.imageSave);
        imageDone.setOnClickListener(v -> saveNote());
    }

    private void saveNote(){
        if(inputTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note title can't be empty", Toast.LENGTH_LONG).show();
            return;
        }else if (inputText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Note can't be empty", Toast.LENGTH_LONG).show();
            return;
        }

        final Note note = new Note();
        note.setTitle(inputTitle.getText().toString());
        note.setNoteText(inputText.getText().toString());


        @SuppressLint("StaticFieldLeak")
        class saveNoteTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids){
                NoteDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new saveNoteTask().execute();
    }
}
