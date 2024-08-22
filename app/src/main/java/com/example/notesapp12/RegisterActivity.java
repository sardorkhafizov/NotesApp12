package com.example.notesapp12;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        Button btnRegister = findViewById(R.id.btnRegister);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        TextView tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(RegisterActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
            }
        });

        tvGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }
}
