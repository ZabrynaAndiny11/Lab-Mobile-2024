package com.example.praktikum_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView ivProfile = findViewById(R.id.IV_Profile);
        TextView tvName = findViewById(R.id.TV_name);
        TextView tvUsername = findViewById(R.id.TV_username);

        Intent intent = getIntent();
        Instagram instagram = intent.getParcelableExtra("instagram");

        ivProfile.setImageResource(instagram.getFotoProfile());
        tvName.setText(instagram.getName());
        tvUsername.setText(instagram.getUsername());

        ImageView iv_row = findViewById(R.id.iv_row);
        iv_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}