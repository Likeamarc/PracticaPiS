 package com.example.practicapis.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.practicapis.R;
import com.example.practicapis.database.FavouriteDatabase;
import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.Model.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class new_note extends AppCompatActivity {

    private EditText inputTitle, inputText;
    private TextView dateTime;
    private ImageView imageNote;
    private TextView textWebURL;
    private LinearLayout layoutWebURL;
    private ImageView favourite_false;
    private ImageView favourite_true;

    private String selectedNoteColor;
    private String selectedImagePath;

    private int favourite;

    private Note alreadyExistingNote;

    private AlertDialog dialogDeleteNote;
    private AlertDialog dialogAddURL;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        initView();
    }

    private void initView(){

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        inputTitle = findViewById(R.id.inputNoteTitle);
        inputText = findViewById(R.id.inputNote);
        dateTime = findViewById(R.id.textDataTime);
        imageNote = findViewById(R.id.imageNote);
        textWebURL = findViewById(R.id.textWebURL);
        layoutWebURL = findViewById(R.id.layoutWebURL);
        favourite_false = findViewById(R.id.starNotFavouriteNewNote);
        favourite_true = findViewById(R.id.starFavouriteNewNote);

        dateTime.setText(
                new SimpleDateFormat("EEE dd MMMM yyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        ImageView imageDone = findViewById(R.id.imageSave);
        imageDone.setOnClickListener(v -> saveNote());

        selectedNoteColor = "#696969"; //color per defecte.
        selectedImagePath = "";
        favourite = 0; //per defecte no es favorita.

        findViewById(R.id.imageRemoveURL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textWebURL.setText(null);
                layoutWebURL.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.starNotFavouriteNewNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favourite_false.setVisibility(View.GONE);
                favourite_true.setVisibility(View.VISIBLE);
                favourite = 0;
            }
        });

        findViewById(R.id.starFavouriteNewNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favourite_true.setVisibility(View.GONE);
                favourite_false.setVisibility(View.VISIBLE);
                favourite = 1;
            }
        });

        findViewById(R.id.imageRemoveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });

        if(getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyExistingNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }


        initMiscellanious();

    }

    private void setViewOrUpdateNote(){
        inputTitle.setText(alreadyExistingNote.getTitle());
        inputText.setText(alreadyExistingNote.getNoteText());
        dateTime.setText(alreadyExistingNote.getDateTime());
        textWebURL.setText(alreadyExistingNote.getWebLink());

        if(!textWebURL.getText().equals("")){
            layoutWebURL.setVisibility(View.VISIBLE);
        }

        if(!alreadyExistingNote.getImagePath().trim().equals("") && !alreadyExistingNote.getImagePath().trim().isEmpty()){
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyExistingNote.getImagePath()));
            selectedImagePath = alreadyExistingNote.getImagePath();
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
        }
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
        note.setImagePath(selectedImagePath);
        note.setFavourite(favourite);

        if(layoutWebURL.getVisibility() == View.VISIBLE){
            note.setWebLink(textWebURL.getText().toString());
        }


        if(alreadyExistingNote != null){
            note.setId(alreadyExistingNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class saveNoteTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids){
                if(favourite == 0) {
                    NoteDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                }else{
                    FavouriteDatabase.getDatabase(getApplicationContext()).favouriteDao().insertNote(note);
                }
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

        layoutMiscellanious.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            new_note.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                }else{
                    selectImage();
                }
            }
        });

        layoutMiscellanious.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDialogAddURL();
            }
        });

        if(alreadyExistingNote != null && alreadyExistingNote.getColor() != null && alreadyExistingNote.getColor().trim().isEmpty()){
            switch(alreadyExistingNote.getColor()){
                case "#ff4c4c":
                    layoutMiscellanious.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#1DA1F2":
                    layoutMiscellanious.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#FFFF33":
                    layoutMiscellanious.findViewById(R.id.viewColor4).performClick();
                    break;
            }
        }

        if(alreadyExistingNote != null){
            layoutMiscellanious.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellanious.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteNoteDialog();
                }
            });
        }
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{
                Toast.makeText(this, "Permision Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null){
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

                        selectedImagePath = getPathFromUri(selectedImageUri);

                    }catch(Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if(cursor == null){
            filePath = contentUri.getPath();
        }else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void showDeleteNoteDialog(){
        if(dialogDeleteNote == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(new_note.this);
            View view = getLayoutInflater().from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if(dialogDeleteNote.getWindow() != null){
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.deleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    class DeleteNoteTask extends AsyncTask<Void, Void, Void>{

                        @Override
                        protected Void doInBackground(Void... voids) {
                            if(favourite == 1){
                                FavouriteDatabase.getDatabase(getApplicationContext()).favouriteDao()
                                        .deleteNote(alreadyExistingNote);
                            }else{
                                NoteDatabase.getDatabase(getApplicationContext()).noteDao()
                                        .deleteNote(alreadyExistingNote);
                            }
                           return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });
        }

        dialogDeleteNote.show();
    }

    private void showDialogAddURL(){
        if(dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(new_note.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );
            builder.setView(view);

            dialogAddURL= builder.create();
            if (dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }


            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputURL.getText().toString().trim().isEmpty()){
                        Toast.makeText(new_note.this, "Enter URL", Toast.LENGTH_LONG).show();
                    }else if(!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()){
                        Toast.makeText(new_note.this, "Enter a valid ULR", Toast.LENGTH_LONG).show();
                    }else{
                        textWebURL.setText(inputURL.getText().toString());
                        layoutWebURL.setVisibility(View.VISIBLE);
                        dialogAddURL.dismiss();
                    }
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
             });
        }
        dialogAddURL.show();
    }
}
