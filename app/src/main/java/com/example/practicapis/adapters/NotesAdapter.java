package com.example.practicapis.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.practicapis.R;
import com.example.practicapis.entities.Note;
import com.example.practicapis.listeners.NoteListener;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private NoteListener noteListener;

    public NotesAdapter(List<Note> notes, NoteListener noteListener){
        this.notes = notes;
        this.noteListener = noteListener;
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
        TextView textTitle, textDate;
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



            if(note.getImagePath().trim().equals("")){
                imageNoteView.setVisibility(View.GONE);
            }else{
                imageNoteView.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNoteView.setVisibility(View.VISIBLE);
            }


        }
    }
}
