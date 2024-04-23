package com.example.praktikum_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvUsername;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        View header = findViewById(R.id.rv_header);
        ivProfile = findViewById(R.id.IV_Profile);
        tvName = findViewById(R.id.TV_name);
        tvUsername = findViewById(R.id.TV_username);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        Instagram instagram = intent.getParcelableExtra("instagram");

        // Sembunyikan ImageView dan TextView sementara u/ menampilkan ProgressBar
        header.setVisibility(View.GONE);
        ivProfile.setVisibility(View.GONE);
        tvName.setVisibility(View.GONE);
        tvUsername.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // pemrosesan data
        new Thread(() -> {
            try {
                // Menunggu selama 2 detik
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Setelah penundaan selesai, jalankan kode yang diperlukan di UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivProfile.setVisibility(View.VISIBLE);
                    tvName.setVisibility(View.VISIBLE);
                    tvUsername.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    // Set foto profil, nama, dan username
                    ivProfile.setImageResource(instagram.getFotoProfile());
                    tvName.setText(instagram.getName());
                    tvUsername.setText(instagram.getUsername());
                }
            });
        }).start();
    }
}