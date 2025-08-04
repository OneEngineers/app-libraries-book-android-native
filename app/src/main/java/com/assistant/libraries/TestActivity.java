package com.assistant.libraries;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.assistant.libraries.ui.Auth.LoginActivity;

public class TestActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        sharedPreferences = getApplication()
                .getSharedPreferences("com.assistant.library_lighthouse", MODE_PRIVATE);
        if (sharedPreferences.getString("status", "true").equals("true")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        };

    }
}
