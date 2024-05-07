package com.example.pertemuan7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class RegisterActivity extends AppCompatActivity {
    EditText et_nim, et_password;
    Button btn_register;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_nim = findViewById(R.id.et_nimRegister);
        et_password = findViewById(R.id.et_passwordResgister);
        btn_register = findViewById(R.id.btn_register2);

        btn_register.setOnClickListener(view -> {
            String nim = et_nim.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (nim.isEmpty()) {
                et_nim.setError("Please enter your NIM");
            } else if (password.isEmpty()) {
                et_password.setError("Please enter your password");
            } else {
                // Mengambil SharedPreferences untuk menyimpan data registrasi
                sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("nim", nim); // Menyimpan data registrasi
                editor.putString("password", password);
                editor.apply();
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}