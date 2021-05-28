package com.example.practicapis.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.practicapis.R;
import com.example.practicapis.adapters.NotesAdapter;
import com.example.practicapis.database.FavouriteDatabase;
import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.entities.Note;
import com.example.practicapis.listeners.NoteListener;
import com.example.practicapis.viewmodel.LoginViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

public class HomePage extends AppCompatActivity implements NoteListener, NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView, favsRecyclerView;
    private List<Note> notesList;
    private List<Note> favouriteNotesList;
    private NotesAdapter notesAdapter, favsAdapter;
    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    DrawerLayout drawerLayout;
    LoginViewModel loginViewModel;

    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = LoginViewModel.get(getApplication());
        setContentView(R.layout.homescreen);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


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

        mRecyclerView = findViewById(R.id.recyclerViewNormal);
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );

        favsRecyclerView = findViewById(R.id.recyclerViewFavs);
        favsRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );

        notesList = new ArrayList<>();
        favouriteNotesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList, this);
        favsAdapter = new NotesAdapter(favouriteNotesList, this);
        mRecyclerView.setAdapter(notesAdapter);
        favsRecyclerView.setAdapter(favsAdapter);

        //getFavourites(REQUEST_CODE_SHOW_NOTES, false);
        getNotes(REQUEST_CODE_SHOW_NOTES, false);

        EditText inputSearch = findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
                favsAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(notesList.size() != 0){
                    notesAdapter.searchNotes(s.toString());
                }

                if(favouriteNotesList.size() != 0){
                    favsAdapter.searchNotes(s.toString());
                }
            }
         });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(HomePage.this, HomePage.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(HomePage.this, activity_settings.class));
                break;
            case R.id.nav_logout:
                loginViewModel.currentUser = null;
                startActivity(new Intent(HomePage.this, MainActivity.class));
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), new_note.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    private void getFavourites(final int requestCode, final boolean isNoteDeleted){
        class getFavouritesText extends AsyncTask<Void, Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return FavouriteDatabase.getDatabase(getApplicationContext()).favouriteDao().getAllFavourites();
            }

            @Override
            protected void onPostExecute(List<Note> FavNotes) {
                super.onPostExecute(FavNotes);
                if(requestCode == REQUEST_CODE_SHOW_NOTES){
                    favouriteNotesList.addAll(FavNotes);
                    favsAdapter.notifyDataSetChanged();
                } else if(requestCode == REQUEST_CODE_ADD_NOTE){
                    favouriteNotesList.add(0, FavNotes.get(0));
                    favsAdapter.notifyItemInserted(0);
                    mRecyclerView.smoothScrollToPosition(0);
                } else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                    favouriteNotesList.remove(noteClickedPosition);
                    if(isNoteDeleted){
                        favsAdapter.notifyItemRemoved(noteClickedPosition);
                    }else{
                        if(FavNotes.get(noteClickedPosition).getFavourite() == 1){
                            favouriteNotesList.add(noteClickedPosition, FavNotes.get(noteClickedPosition));
                        }else{
                            notesList.add(noteClickedPosition, FavNotes.get(noteClickedPosition));
                            FavNotes.remove(noteClickedPosition);
                        }

                        favsAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }
        new getFavouritesText().execute();
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
                        if(notes.get(noteClickedPosition).getFavourite() == 1){
                            favouriteNotesList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        }else{
                            notesList.add(noteClickedPosition, notes.get(noteClickedPosition));
                            notes.remove(noteClickedPosition);
                        }
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
            //getFavourites(REQUEST_CODE_ADD_NOTE, false);
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if(data != null){
                //getFavourites(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra( "isNoteDeleted", false));
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra( "isNoteDeleted", false));
            }
        }
    }
}