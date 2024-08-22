package com.example.notesapp12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        Button btnLogin = findViewById(R.id.btnLogin);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                                        startActivity(intent);
                                        finish();  // Закрывает текущую активность после успешного входа
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }
}
