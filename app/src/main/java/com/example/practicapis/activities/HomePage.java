package com.example.practicapis.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.example.practicapis.Adapter;
import com.example.practicapis.ExampleItem;
import com.example.practicapis.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class HomePage extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton addNote;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);


        addNote = findViewById(R.id.addNoteButton);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(getApplicationContext(), new_note.class));
            }
        });

        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem(R.drawable.ic_baseline_camera_alt_24, "Camera Note", "Description"));
        exampleList.add(new ExampleItem(R.drawable.ic_baseline_speaker_24, "Sound Note", "Description"));
        exampleList.add(new ExampleItem(R.drawable.ic_baseline_pending_24, "Text Note", "Description"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }

}
