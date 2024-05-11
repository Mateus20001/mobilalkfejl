package com.example.allaskereso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText Email;
    EditText Password;
    FirebaseAuth mAuth;
    private GoogleSignInAccount mGoogleSignInAccount;

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Email = findViewById(R.id.editTextEmailAddress);
        Password = findViewById(R.id.editTextTextPassword);
        Email.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        Password.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        preferences = getSharedPreferences("Álláskereső", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        setupAlarm();
        sendNotification();
    }
    //CRUD - Read
    public void login(View view) {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        Thread thread = new Thread(() -> {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        runOnUiThread(() -> {
                            startBrowsing();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
    public void registration(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET KEY", 99);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", Email.getText().toString());
        editor.putString("password", Password.getText().toString());
        editor.apply();
    }

    public void loginasGuest(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    startBrowsing();
                } else {
                    Toast.makeText(MainActivity.this, "User wasn't created successfully" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void setupAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Az ébresztés időzítése minden órában
        long intervalMillis = AlarmManager.INTERVAL_HOUR; // Minden órában
        long triggerTime = System.currentTimeMillis(); // Azonnal induljon
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalMillis, pendingIntent);
    }
    private void sendNotification() {
        // Értesítés létrehozása és megjelenítése
        NotificationUtils.showNotification(this, "Üdvözöllek!", "Köszönjük, hogy használod az alkalmazást!");

    }

}