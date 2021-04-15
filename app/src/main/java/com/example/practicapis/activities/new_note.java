package com.example.practicapis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practicapis.R;

public class new_note extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.new_note);

        super.onCreate(savedInstanceState);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
