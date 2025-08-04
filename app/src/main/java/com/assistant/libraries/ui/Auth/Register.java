package com.assistant.libraries.ui.Auth;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.assistant.libraries.ApiServices;
import com.assistant.libraries.MainActivity;
import com.assistant.libraries.R;
import com.assistant.libraries.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    EditText btnTextName, btnTextEmail, btnTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        btnTextName = findViewById(R.id.btnTextName);
        btnTextEmail = findViewById(R.id.btnTextEmail);
        btnTextPassword = findViewById(R.id.btnTextPassword);
        Button button = findViewById(R.id.btnRegister);
        button.setOnClickListener(v -> {
            registerUser();
        });
    }
    void registerUser(){
        // Create a Retrofit instance
        String name, email, password;
        name = btnTextName.getText().toString();
        email = btnTextEmail.getText().toString();
        password = btnTextPassword.getText().toString();
        if (name.isEmpty()){
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.1.22.113:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServices apiServices = retrofit.create(ApiServices.class);
        User user = new User(name, email, password);
        Call<ResponseBody> call = apiServices.registerUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Response", response.toString());
                if (response.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
