package com.assistant.libraries;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.assistant.libraries.ui.Home.HomeFragment;
import com.assistant.libraries.ui.Home.LibraryFragment;
import com.assistant.libraries.ui.Home.ListFragment;
import com.assistant.libraries.ui.Home.ProfileFragment;
import com.assistant.libraries.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                LoadFragment(new HomeFragment());
            } else if (itemId == R.id.nav_list) {
                LoadFragment(new ListFragment());
            } else if (itemId == R.id.nav_user) {
                LoadFragment(new ProfileFragment());
            } else if (itemId == R.id.nav_library) {
                LoadFragment(new LibraryFragment());
            }else {
                return false;
            }
            return true;
        });

        if (savedInstanceState == null) {
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else {
            binding.bottomNavigationView.setSelectedItemId(savedInstanceState.getInt("selectedItemId"));
        }
    }

    private void LoadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}

