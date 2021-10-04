package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        Intent intento=new Intent(this,Aplicaciones.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);
        intento.putExtra("Accion", "Captura");
        //Toast.makeText(this, Usuario+","+Perfil+","+Huerta,Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }
    public void Enviar(View view){
        Intent intento=new Intent(this,Aplicaciones.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);
        intento.putExtra("Accion", "Enviar");
        //Toast.makeText(this, Usuario+","+Perfil+","+Huerta,Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }

    public void Reportes(View view){
        Intent intento=new Intent(this,Aplicaciones.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);
        intento.putExtra("Accion", "Reportes");
        //Toast.makeText(this, Usuario+","+Perfil+","+Huerta,Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }
    public void CerrarSesion(View view){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(EnviaRecibe.this);
        dialogo1.setTitle("¿Esta seguro que desea cerrar sesión?");
        dialogo1.setMessage("Esto quitara tu usuario y no podrás volver a entrar si no cuentas con conexión a Internet");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(EnviaRecibe.this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

                int cantidad= BD.delete("UsuarioLogin","Id_Usuario<>'root'",null);
                BD.close();

                if(cantidad>0){
                    Toast.makeText(EnviaRecibe.this,"Se Finalizo la sesión con éxito.",Toast.LENGTH_SHORT).show();
                    Intent intento=new Intent(EnviaRecibe.this,Login_Usuario.class);
                    startActivity(intento);
                    finish();
                }else{
                    Toast.makeText(EnviaRecibe.this,"Ocurrió un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                }
            }
    });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogo1, int id) {
            //cancelar();
        }
    });
                dialogo1.show();
    }

}