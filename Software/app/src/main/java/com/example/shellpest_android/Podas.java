package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Podas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podas);

        getSupportActionBar().hide();
    }
}