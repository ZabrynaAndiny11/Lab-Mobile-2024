package com.example.praktikum_4.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.praktikum_4.Instagram;
import com.example.praktikum_4.ProfileActivity;
import com.example.praktikum_4.R;

public class PostingFragment extends Fragment {

    private Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iv_foto = view.findViewById(R.id.iv_gambar);
        EditText ET_konten = view.findViewById(R.id.et_konten);
        Button btn_posting = view.findViewById(R.id.btn_posting);

        ActivityResultLauncher<Intent> launcherIntentGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedImageUri = data.getData();
                        if (selectedImageUri != null) {
                            iv_foto.setImageURI(selectedImageUri);
                        }
                    }
                }
            }
        );

        iv_foto.setOnClickListener(v -> {
            Intent openGallery = new Intent(Intent.ACTION_PICK);
            openGallery.setType("image/*");
            launcherIntentGallery.launch(openGallery);
        });

        btn_posting.setOnClickListener(btn ->{
            String konten = ET_konten.getText().toString();

            if (!konten.isEmpty()) {
                if (selectedImageUri != null) {
                   // buat objek ig dan diteruskan ke homefragment
                    Instagram instagram = new Instagram("flow.error", "flowerr", konten, R.drawable.bunga1, selectedImageUri);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(HomeFragment.EXTRA_INSTAGRAM, instagram);

                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setArguments(bundle);

                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, homeFragment)
                            .addToBackStack(null)
                            .commit();

                    Toast.makeText(getActivity(), "Data berhasil diposting", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Konten tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}