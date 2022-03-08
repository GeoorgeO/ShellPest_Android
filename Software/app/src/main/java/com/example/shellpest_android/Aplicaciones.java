package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Aplicaciones extends AppCompatActivity {

    public String Usuario, Perfil, Huerta, Accion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicaciones);

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");
        Accion = getIntent().getStringExtra("Accion");
    }

    public void Monitoreo (View view){
        Intent intento=null;
        if (Accion.equals("Captura")){
            intento=new Intent(this,activity_Monitoreo.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);

        }
        if (Accion.equals("Enviar")){
           intento=new Intent(this,Enviar.class);

        }
        startActivity(intento);
    }

    public void Riego (View view){
        Intent intento=null;
        if (Accion.equals("Captura")){
            intento=new Intent(this,Riego.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);
        }
        if (Accion.equals("Enviar")){
            intento=new Intent(this,enviarriego.class);

        }
        startActivity(intento);
    }

    public void Salida(View view){
        Intent intento=null;
        if (Accion.equals("Captura")){
            intento=new Intent(this,Salidas.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);

        }
        if (Accion.equals("Enviar")){
            intento=new Intent(this,Enviar_Salidas.class);
        }
        startActivity(intento);
    }

    public void Aplicacion(View view){
        Intent intento=null;
        if (Accion.equals("Captura")){
            intento=new Intent(this,aplicacion.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);

        }
        if (Accion.equals("Enviar")){
            intento=new Intent(this,Enviar_Aplicaciones.class);
        }
        startActivity(intento);
    }

    public void Poda(View view){
        Intent intento=null;
        if (Accion.equals("Captura")){
            intento=new Intent(this,Podas.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);

        }
        if (Accion.equals("Enviar")){
            //intento=new Intent(this,Enviar_Aplicaciones.class);
        }
        startActivity(intento);
    }

}