package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class Gasolina extends AppCompatActivity {

    Spinner sp_activoGas, sp_huertaGas, sp_tipoGas, sp_actividadGas;
    private ArrayList<ItemDatoSpinner> ItemSPhuerta, ItemSPactivo, ItemSPtipo,ItemSPactividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolina);
        getSupportActionBar().hide();

        sp_activoGas = (Spinner) findViewById(R.id.sp_activoGas);
        String[] activo = {"Activo","Camioneta","Tractor","Maquinaria"};
        sp_activoGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activo));

        sp_huertaGas= (Spinner) findViewById(R.id.sp_huertaGas);
        String[] huerta = {"Huerta","San Tadeo","Arrendadora","Producción Agrícola"};
        sp_huertaGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, huerta));

        sp_tipoGas = (Spinner) findViewById(R.id.sp_tipoGas);
        String[] tipo = {"Tipo","Gasolina Magna","Gasolina Premium","Diesel"};
        sp_tipoGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo));

        sp_actividadGas = (Spinner) findViewById(R.id.sp_actividadGas);
        String[] actividad = {"Actividad", "Poda","Traslado", "Viaje"};
        sp_actividadGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actividad));
    }
}