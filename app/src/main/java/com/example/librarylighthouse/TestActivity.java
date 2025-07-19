package com.example.librarylighthouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.librarylighthouse.ui.Auth.LoginActivity;

public class TestActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        sharedPreferences = getApplication()
                .getSharedPreferences("com.example.library_lighthouse", MODE_PRIVATE);
        if (sharedPreferences.getString("status", "true").equals("true")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        };

    }
}
