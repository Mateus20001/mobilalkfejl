package com.example.allaskereso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameET;
    EditText passwordET;
    EditText emailET;
    EditText passwordrET;
    SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        usernameET = findViewById(R.id.editTextTextUserName);
        passwordET = findViewById(R.id.editTextTextPassword);
        passwordrET = findViewById(R.id.editTextTextPasswordRepeat);
        emailET = findViewById(R.id.editTextEmailAddress);
        preferences = getSharedPreferences("Álláskereső", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");
        usernameET.setText(userName);
        passwordET.setText(password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //CRUD - Create
    public void register(View view) {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordr = passwordrET.getText().toString();
        String email = emailET.getText().toString();
        if (!passwordr.equals(password)) {
            return;
        }

        Thread thread = new Thread(() -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        runOnUiThread(() -> {
                            startBrowsing();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "A felhasználó létrehozása sikertelen: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                }
            });
        });
        thread.start();
    }

    private void startBrowsing(/* */) {
        Intent intent = new Intent(this, AllasActivity.class);
        intent.putExtra("SECRET_KEY", 99);
        startActivity(intent);
    }

    public void undo(View view) {
        finish();
    }
}