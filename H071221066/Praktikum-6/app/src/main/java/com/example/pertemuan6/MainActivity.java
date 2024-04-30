package com.example.pertemuan6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private TextView errorTextView;
    private Button retryButton, loadMoreButton;
    private ProgressBar progressBar;
    private Context context;
    private ArrayList<User> users = new ArrayList<>();
    private Handler handler = new Handler(Looper.myLooper());
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerView = findViewById(R.id.recyclerView);
        errorTextView = findViewById(R.id.errorTextView);
        retryButton = findViewById(R.id.retryButton);
        loadMoreButton = findViewById(R.id.loadButton);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadInitialData();

        loadMoreButton.setOnClickListener(v -> {
            loadMoreButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            loadMoreData();
        });
    }

    private void loadInitialData() {
        if (isNetworkAvailable()) {
            currentPage = 1;
            loadData();
        } else {
            showErrorView();
        }
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        Call<UserResponse> call = apiService.getUsers(currentPage, 6);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    users = new ArrayList<>(response.body().getData());
                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        userAdapter = new UserAdapter(users, context);
                        recyclerView.setAdapter(userAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (users.size() >= 6) {
                            loadMoreButton.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        showErrorView();
                    });
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    showErrorView();
                });
            }
        });
    }
    private void loadMoreData() {
        currentPage++;
        Call<UserResponse> call = apiService.getUsers(currentPage, 6);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    List<User> newUsers = response.body().getData();
                    if (!newUsers.isEmpty()) {
                        users.addAll(newUsers);
                        if (userAdapter == null) {
                            userAdapter = new UserAdapter(users, context);
                            recyclerView.setAdapter(userAdapter);
                        } else {
                            userAdapter.notifyDataSetChanged();
                        }
                        if (newUsers.size() < 6) {
                            loadMoreButton.setVisibility(View.GONE);
                        }
                    } else {
                        loadMoreButton.setVisibility(View.GONE);
                    }
                } else {
                    showErrorView();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorView();
            }
        });
    }
    private void showErrorView() {
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);

        retryButton.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                errorTextView.setVisibility(View.GONE);
                retryButton.setVisibility(View.GONE);
                currentPage = 1;
                loadData();
            } else {
                Toast.makeText(MainActivity.this, "Internet connection still unavailable", Toast.LENGTH_SHORT).show();
                handler.post(() -> {
                    loadData();
                    errorTextView.setVisibility(View.GONE);
                    retryButton.setVisibility(View.GONE);
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