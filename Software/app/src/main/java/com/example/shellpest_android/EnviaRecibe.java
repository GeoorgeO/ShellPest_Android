package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnviaRecibe extends AppCompatActivity {

    Button btn_Enviar,btn_Recibir,btn_Siguiente;
    public String Usuario,Perfil,Huerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envia_recibe);
        getSupportActionBar().hide();

        btn_Enviar=(Button)findViewById(R.id.btn_Enviar);
        btn_Recibir=(Button)findViewById(R.id.btn_Recibir);
        btn_Siguiente=(Button)findViewById(R.id.btn_Siguiente);

        Usuario= getIntent().getStringExtra("usuario");
        Perfil= getIntent().getStringExtra("perfil");
        Huerta= getIntent().getStringExtra("huerta");
    }

    public void Recibir(View vie){
        Intent intento = new Intent(this, MainActivity.class);
        intento.putExtra("usuario", Usuario);
        intento.putExtra("perfil", Perfil);
        intento.putExtra("huerta", Huerta);

        //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }

    public void Continuar (View view){
        Intent intento=new Intent(this,activity_Monitoreo.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);
        //Toast.makeText(this, Usuario+","+Perfil+","+Huerta,Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }
    public void Enviar(View view){
        Intent intento=new Intent(this,Enviar.class);

        //Toast.makeText(this, Usuario+","+Perfil+","+Huerta,Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }

}