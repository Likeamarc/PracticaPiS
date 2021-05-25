package com.example.practicapis.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.practicapis.R;
import com.example.practicapis.Model.Note;
import com.example.practicapis.listeners.NoteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static bolts.Task.delay;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private List<Note> favouriteNotes;
    private NoteListener noteListener;
    Timer timer;
    private List<Note> notesSource;


    public NotesAdapter(List<Note> notes, NoteListener noteListener){
        this.notes = notes;
        this.noteListener = noteListener;
        notesSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.note_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListener.onNoteClicked(notes.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) { return position; }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textDate, textWebURL;
        ImageView noteFavourite, noteNotFavourite;
        LinearLayout layoutNote;
        ImageView imageNoteView;

        NoteViewHolder(@NonNull View itemView){
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNoteView = itemView.findViewById(R.id.imageNoteView);
        }

        void setNote(Note note){
            textTitle.setText(note.getTitle());
            textDate.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#696969"));
            }

            if(note.getFavourite() == 1){
                noteNotFavourite.setVisibility(View.GONE);
                noteFavourite.setVisibility(View.VISIBLE);
            }else{
                noteNotFavourite.setVisibility(View.VISIBLE);
                noteFavourite.setVisibility(View.GONE);
            }


            if(note.getImagePath().trim().equals("")){
                imageNoteView.setVisibility(View.GONE);
            }else{
                imageNoteView.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNoteView.setVisibility(View.VISIBLE);
            }


        }
    }

    public void searchNotes(final String searchKeyWord){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyWord.trim().isEmpty()){
                    notes = notesSource;
                }else{
                    ArrayList<Note> temp = new ArrayList<>();
                    for(Note note : notesSource){
                        if( note.getTitle().toLowerCase().contains(searchKeyWord.toLowerCase())
                        || note.getNoteText().toLowerCase().contains(searchKeyWord.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }
}
