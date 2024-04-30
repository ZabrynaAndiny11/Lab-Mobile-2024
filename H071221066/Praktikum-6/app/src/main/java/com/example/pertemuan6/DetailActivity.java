package com.example.pertemuan6;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private int userId;
    private TextView errorTextView;
    private Button retryButton;
    private ProgressBar progressBar;
    private Handler handler = new Handler(Looper.myLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);

        errorTextView = findViewById(R.id.errorTextView1);
        retryButton = findViewById(R.id.retryButton1);
        progressBar = findViewById(R.id.progressBar1);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId",-1);
            // Panggil API dan tampilkan hasilnya
            getUserDetails(userId);
        } else {
            Toast.makeText(this, "User ID not provided", Toast.LENGTH_SHORT).show();
        }
        if (!isNetworkAvailable()) {
            showErrorView();
        }
    }

    private void getUserDetails(int userId) {
        Retrofit retrofit = RetrofitClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        progressBar.setVisibility(View.VISIBLE);
        avatarImageView.setVisibility(View.GONE);
        nameTextView.setVisibility(View.GONE);
        emailTextView.setVisibility(View.GONE);

        Call<DetailResponse> call = apiService.getUserById(userId);
        call.enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body().getData();

                        if (user != null) {
                            // Tampilkan data pengguna yang ditemukan
                            String fullName = user.getFirst_name() + " " + user.getLast_name();
                            String email = user.getEmail();
                            String avatarUrl = user.getAvatar();

                            // Gunakan data pengguna untuk keperluan aplikasi (misalnya, tampilkan di UI)
                            nameTextView.setText(fullName);
                            emailTextView.setText(email);
                            Picasso.get().load(avatarUrl).into(avatarImageView);

                            avatarImageView.setVisibility(View.VISIBLE);
                            nameTextView.setVisibility(View.VISIBLE);
                            emailTextView.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(DetailActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    showErrorView();
                });
            }
        });
    }
    // Metode untuk mencari user berdasarkan ID dalam daftar userList
//    private User findUserById(List<User> userList, int userId) {
//        for (User user : userList) {
//            if (user.getId() == userId) {
//                return user;
//            }
//        }
//        return null; // Return null jika user tidak ditemukan
//    }

    private void showErrorView() {
        errorTextView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        avatarImageView.setVisibility(View.GONE);
        nameTextView.setVisibility(View.GONE);
        emailTextView.setVisibility(View.GONE);

        retryButton.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                errorTextView.setVisibility(View.GONE);
                retryButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                avatarImageView.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                emailTextView.setVisibility(View.VISIBLE);
                getUserDetails(userId); // Panggil kembali metode untuk mendapatkan detail user setelah koneksi diperbaiki
            } else {
                Toast.makeText(DetailActivity.this, "Internet connection still unavailable", Toast.LENGTH_SHORT).show();
                handler.post(() -> {
                    errorTextView.setVisibility(View.GONE);
                    retryButton.setVisibility(View.GONE);
                    getUserDetails(userId);
                });
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
