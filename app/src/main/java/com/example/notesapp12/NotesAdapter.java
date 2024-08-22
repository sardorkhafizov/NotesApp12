package com.example.notesapp12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<String> notesList;
    private List<String> noteIds;
    private FirebaseFirestore firestore;

    public NotesAdapter(List<String> notesList, List<String> noteIds, FirebaseFirestore firestore) {
        this.notesList = notesList;
        this.noteIds = noteIds;
        this.firestore = firestore;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String note = notesList.get(position);
        String noteId = noteIds.get(position);
        holder.noteTextView.setText(note);

        holder.btnDeleteNote.setOnClickListener(v -> {
            firestore.collection("notes").document(noteId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            notesList.remove(position);
                            noteIds.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(holder.itemView.getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Error deleting note", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;
        Button btnDeleteNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.noteTextView);
            btnDeleteNote = itemView.findViewById(R.id.btnDeleteNote); // Добавляем кнопку удаления
        }
    }
}
