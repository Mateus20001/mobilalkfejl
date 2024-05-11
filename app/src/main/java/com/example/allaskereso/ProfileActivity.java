package com.example.allaskereso;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private TextView emailTextView;
    private EditText emailEditText;
    private Button changeEmailButton;
    private Button deleteProfileButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        changeEmailButton = findViewById(R.id.changeEmailButton);
        deleteProfileButton = findViewById(R.id.deleteProfileButton);
        emailTextView = findViewById(R.id.emailTextView);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && !currentUser.getEmail().equals("")) {
            String email = currentUser.getEmail();
            if (email != null) {
                emailTextView.setText("Email: " + email);
            }
        } else {
            // Ha nincs bejelentkezett felhasználó, itt kezelheted le
            emailTextView.setText("Nincs bejelentkezett felhasználó");
            changeEmailButton.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            deleteProfileButton.setVisibility(View.GONE);
        }
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });
    }
    //CRUD - DELETE
    public void deleteProfile(View view) {
        Thread thread = new Thread(() -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                runOnUiThread(() -> {
                                    Toast.makeText(ProfileActivity.this, "Profil sikeresen törölve", Toast.LENGTH_SHORT).show();
                                    navigateToMain();
                                });
                            } else {
                                runOnUiThread(() -> {
                                    Toast.makeText(ProfileActivity.this, "Hiba történt a profil törlése közben", Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
            }
        });
        thread.start();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    //CRUD - Update
    private void updateEmail() {
        String newEmail = emailEditText.getText().toString().trim();
        // Add validation logic here if needed

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Külön szál létrehozása és futtatása
            new Thread(new Runnable() {
                @Override
                public void run() {
                    user.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Email frissítése sikeres
                                        Log.d(TAG, "Email address updated.");
                                    } else {
                                        // Hiba esetén kezelés
                                        Log.w(TAG, "Failed to update email address.", task.getException());

                                    }
                                }
                            });
                }
            }).start();
        }
        Intent intent = new Intent(this, AllasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}

