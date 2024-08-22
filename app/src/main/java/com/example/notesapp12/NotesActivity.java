package com.example.notesapp12;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private List<String> notesList;
    private List<String> noteIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        notesList = new ArrayList<>();
        noteIds = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList, noteIds, firestore);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);

        Button btnAddNote = findViewById(R.id.btnAddNote);
        EditText etNoteText = findViewById(R.id.etNoteText);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnAddNote.setOnClickListener(v -> {
            String noteText = etNoteText.getText().toString();
            if (!noteText.isEmpty()) {
                Map<String, Object> note = new HashMap<>();
                note.put("text", noteText);
                note.put("userId", auth.getCurrentUser().getUid());

                firestore.collection("notes")
                        .add(note)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(NotesActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                                etNoteText.setText("");  // Очистка текстового поля после добавления заметки
                            } else {
                                Toast.makeText(NotesActivity.this, "Error adding note", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(NotesActivity.this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            auth.signOut(); // Выход из аккаунта
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
            finish(); // Завершение текущей активности, чтобы не было возможности вернуться назад
        });

        loadNotes();
    }

    private void loadNotes() {
        firestore.collection("notes")
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(NotesActivity.this, "Error loading notes", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        notesList.clear();
                        noteIds.clear();

                        for (QueryDocumentSnapshot document : value) {
                            notesList.add(document.getString("text"));
                            noteIds.add(document.getId());
                        }
                        notesAdapter.notifyDataSetChanged();
                    }
                });
    }
}
