package com.example.crimeintent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();

        Fragment crimeFragment = fm.findFragmentById(R.id.fragment_container);
        if (crimeFragment == null) {
            crimeFragment = createFragment();

            fm.beginTransaction().add(R.id.fragment_container, crimeFragment).commit();
        }
    }
}
