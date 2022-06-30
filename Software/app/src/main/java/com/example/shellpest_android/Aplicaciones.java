package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Aplicaciones extends AppCompatActivity {

    TextView txtv_modulos;
    public String Usuario, Perfil, Huerta, Accion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicaciones);
		getSupportActionBar().hide();

        txtv_modulos = (TextView) findViewById(R.id.txtv_modulos);
        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");
        Accion = getIntent().getStringExtra("Accion");
        txtv_modulos.setText("MODULOS " + Accion);

    }

    public void Home (View view){
        finish();
        Intent intento=new Intent(this,EnviaRecibe.class);
        startActivity(intento);
    }

    public void Monitoreo (View view){
        Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,activity_Monitoreo.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);
        }
        if (Accion.equals("ENVIAR")){
           intento=new Intent(this,Enviar.class);
        }
        startActivity(intento);
    }

    public void Riego (View view){
        Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,Riego.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);
        }
        if (Accion.equals("ENVIAR")){
            intento=new Intent(this,enviarriego.class);
        }
        startActivity(intento);
    }

    public void Salida(View view){
      /*  Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,Salidas.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);
        }
        if (Accion.equals("ENVIAR")){
            intento=new Intent(this,Enviar_Salidas.class);
        }
        startActivity(intento);*/
    }

    public void Aplicacion(View view){
        Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,aplicacion.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);
            intento.putExtra("FoF", "A");
        }
        if (Accion.equals("ENVIAR")){
            intento=new Intent(this,Enviar_Aplicaciones.class);
        }
        startActivity(intento);
    }

    public void Poda(View view){
        Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,Podas.class);
            intento.putExtra("usuario3", Usuario);
            intento.putExtra("perfil3", Perfil);
            intento.putExtra("huerta2", Huerta);
        }
        if (Accion.equals("ENVIAR")){
            intento=new Intent(this,Enviar_Poda.class);
        }
        startActivity(intento);
    }
	
	 public void Gasolina(View view){
        Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,Entradas_Gasolina.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);
        }
        if (Accion.equals("ENVIAR")){
            intento=new Intent(this,Enviar_Gasolina.class);
        }
        startActivity(intento);
    }

    public void Cosecha(View view){
        Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,Cosecha.class);
            intento.putExtra("usuario3", Usuario);
            intento.putExtra("perfil3", Perfil);
            intento.putExtra("huerta2", Huerta);
        }
        if (Accion.equals("ENVIAR")){
            intento=new Intent(this,Enviar_Cosecha.class);
        }
        startActivity(intento);
    }

    public void Fertiliza(View view){
        Intent intento=null;
        if (Accion.equals("CAPTURA")){
            intento=new Intent(this,Fertilizacion.class);
            intento.putExtra("usuario2", Usuario);
            intento.putExtra("perfil2", Perfil);
            intento.putExtra("huerta2", Huerta);
            intento.putExtra("FoF", "F");
        }
        if (Accion.equals("ENVIAR")){
            intento=new Intent(this,Enviar_Fertiliza.class);
        }
        startActivity(intento);
    }

}