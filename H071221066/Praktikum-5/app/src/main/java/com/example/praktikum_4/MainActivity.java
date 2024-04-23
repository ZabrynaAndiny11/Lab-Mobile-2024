package com.example.praktikum_4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.praktikum_4.Fragment.HomeFragment;
import com.example.praktikum_4.Fragment.PostingFragment;
import com.example.praktikum_4.Fragment.ProfileFragment;
import com.example.praktikum_4.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    Fragment selectedFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        Fragment fragment = fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
        if (!(fragment instanceof HomeFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, homeFragment)
                    .commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.navmenu);
        bottomNav.inflateMenu(R.menu.bottom_nav_menu);
        selectedFragment = new HomeFragment();
        bottomNav.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.homebtn) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.searchbtn) {
                selectedFragment = new SearchFragment();
            } else if (item.getItemId() == R.id.postbtn) {
                selectedFragment = new PostingFragment();
            } else if (item.getItemId() == R.id.profilebtn) {
                selectedFragment = new ProfileFragment();
            }

            if(selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .commit();
                return true;
            } else {
                return false;
            }
        });
    }
}