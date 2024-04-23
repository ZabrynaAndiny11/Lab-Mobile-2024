package com.example.praktikum_4.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum_4.DataSource;
import com.example.praktikum_4.Instagram;
import com.example.praktikum_4.R;
import com.example.praktikum_4.SearchAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private SearchAdapter searchAdapter;
    private ArrayList<Instagram> instagrams;
    private ProgressBar progressBar;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.rv_search);
        recyclerView.setVisibility(View.GONE); // Sembunyikan RecyclerView dari awal
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // inisialisasi recyclerview

        instagrams = DataSource.getInstagrams();
        searchAdapter = new SearchAdapter(instagrams);

        recyclerView.setAdapter(searchAdapter);

        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.search_user);
//        searchView.clearFocus();

        // Setup search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                // Hapus pesan yang tertunda sebelumnya
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    // Memfilter daftar berdasarkan teks pencarian baru
                    ArrayList<Instagram> filteredList;
                    if (newText.isEmpty()) {
                        filteredList = new ArrayList<>(); // Jika pencarian kosong, tampilkan daftar kosong
                    } else {
                        filteredList = filterList(newText);
                    }

                    // Setelah selesai, kembali ke thread utama untuk mengubah UI
                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);

                        if (!filteredList.isEmpty()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            searchAdapter.setFilteredList(filteredList);
                        } else {
                            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }, 1000); // Delay 1 detik sebelum memulai pemfilteran
                return true;
            }
        });
        return view;
    }

    private ArrayList<Instagram> filterList(String text) {
        ArrayList<Instagram> filteredList = new ArrayList<>();
        for (Instagram instagram : instagrams) {
            if (instagram.getName().toLowerCase().contains(text.toLowerCase()) ||
                    instagram.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(instagram);
            }
        }
        return filteredList;
    }
}