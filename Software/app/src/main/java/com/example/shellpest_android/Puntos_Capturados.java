package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Puntos_Capturados extends AppCompatActivity {
String Usuario,Perfil,Huerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_capturados);

        Usuario = getIntent().getStringExtra("usuario");
        Perfil = getIntent().getStringExtra("perfil");
        Huerta = getIntent().getStringExtra("huerta");
    }
}