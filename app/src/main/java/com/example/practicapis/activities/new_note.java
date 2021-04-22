package com.example.practicapis.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practicapis.R;
import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class new_note extends AppCompatActivity {

    private EditText inputTitle, inputText;
    private TextView dateTime;

    private String selectedNoteColor;

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

        selectedNoteColor = "#696969"; //color per defecte.


        initMiscellanious();

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
        note.setDateTime(dateTime.getText().toString());
        note.setColor(selectedNoteColor);


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

    private void initMiscellanious(){
        final LinearLayout layoutMiscellanious = findViewById(R.id.layout_miscelanious);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellanious);
        layoutMiscellanious.findViewById(R.id.textMiscellanious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != bottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellanious.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellanious.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellanious.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellanious.findViewById(R.id.imageColor4);

        layoutMiscellanious.findViewById(R.id.imageColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#696969";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
            }
        });

        layoutMiscellanious.findViewById(R.id.imageColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#ff4c4c";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
            }
        });

        layoutMiscellanious.findViewById(R.id.imageColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#1DA1F2";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
            }
        });

        layoutMiscellanious.findViewById(R.id.imageColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FFFF33";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
            }
        });
    }
}
