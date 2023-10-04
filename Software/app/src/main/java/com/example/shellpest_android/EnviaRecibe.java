package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class EnviaRecibe extends AppCompatActivity {

    Button btn_Enviar,btn_Recibir,btn_Siguiente;
    public String Usuario,Perfil,Huerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envia_recibe);
        getSupportActionBar().hide();

        /* esto era para las animaciones del aguacatito
        ImageView img = (ImageView) findViewById(R.id.loadingviewmenu);
        img.setBackgroundResource(R.drawable.loadinggif);

        AnimationDrawable frameAnimation;
        frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();*/

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
        intento.putExtra("Accion", "CAPTURA");
        //Toast.makeText(this, Usuario+","+Perfil+","+Huerta,Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }
    public void Enviar(View view){
        Intent intento=new Intent(this,Aplicaciones.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);
        intento.putExtra("Accion", "ENVIAR");
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

                int cantidad=0;
                cantidad=BD.delete("UsuarioLogin","Id_Usuario<>'root'",null);

                BD.delete("FechaSincroniza","id_Sincroniza<>'1000'",null);
                BD.delete("t_Calidad","Id_Calidad<>'1000'",null);
                BD.delete("t_Cultivo","Id_Cultivo<>'1000'",null);
                BD.delete("t_Duenio","Id_Duenio<>'1000'",null);
                BD.delete("t_Deteccion","Id_Deteccion<>'1000'",null);
                BD.delete("t_Enfermedad","Id_Enfermedad<>'1000'",null);
                BD.delete("t_Humbral","Id_Humbral<>'1000'",null);
                BD.delete("t_Plagas","Id_Plagas<>'1000'",null);
                BD.delete("t_Productor","Id_Productor<>'1000'",null);
                BD.delete("t_Pais","Id_Pais<>'1000'",null);
                BD.delete("t_Estado","Id_Estado<>'1000'",null);
                BD.delete("t_Ciudades","Id_Ciudad<>'1000'",null);
                BD.delete("t_Huerta","Id_Huerta<>'1000'",null);
                BD.delete("t_Bloque","Id_Bloque<>'1000'",null);
                BD.delete("t_Puntocontrol","Id_PuntoControl<>'1000'",null);
                BD.delete("t_Zona","Id_zona<>'1000'",null);
                BD.delete("t_Monitoreo_PE","Fecha<>'1000'",null);
                BD.delete("t_Monitoreo_Eliminados_PE","Fecha<>'1000'",null);
                BD.delete("t_Individuo","Id_Individuo<>'1000'",null);
                BD.delete("t_Monitoreo","Id_monitoreo<>'1000'",null);
                BD.delete("t_Monitoreo_PEEncabezado","Fecha<>'1000'",null);
                BD.delete("t_Monitoreo_Eliminados_PEEncabezado","Fecha<>'1000'",null);
                BD.delete("t_Riego","Fecha<>'1000'",null);
                BD.delete("t_RiegoEliminado","Fecha<>'1000'",null);
                BD.delete("t_Usuario_Huerta","Id_Usuario<>'1000'",null);
                BD.delete("t_Productos","c_codigo_pro<>'1000'",null);
                BD.delete("t_Unidad","c_codigo_uni<>'1000'",null);
                BD.delete("t_Presentacion","Id_Presentacion<>'1000'",null);
                BD.delete("t_TipoAplicacion","Id_TipoAplicacion<>'1000'",null);
                BD.delete("t_Aplicaciones","Id_Aplicacion<>'1000'",null);
                BD.delete("t_Aplicaciones_Det","Id_Aplicacion<>'1000'",null);
                BD.delete("t_Usuario_Empresa","Id_Usuario<>'1000'",null);
                BD.delete("conempresa","c_codigo_eps<>'1000'",null);
                BD.delete("t_Salidas","Id_Salida<>'1000'",null);
                BD.delete("t_Salidas_Det","Id_Salida<>'1000'",null);
                BD.delete("t_Almacen","Id_Almacen<>'1000'",null);
                BD.delete("invmovimiento","c_coddoc_mov<>'1000'",null);
                BD.delete("t_existencias","c_codigo_eps<>'1000'",null);
                BD.delete("t_Receta","Id_Receta<>'1000'",null);
                BD.delete("t_RecetaDet","Id_Receta<>'1000'",null);
                //BD.delete("t_Receta_Huerta","id_Sincroniza<>'1000'",null);
                BD.delete("t_Est_Fenologico","Id_Fenologico<>'1000'",null);
                BD.delete("cosactividad","c_codigo_act<>'1000'",null);
                BD.delete("t_Podas","Fecha<>'1000'",null);
                BD.delete("t_PodasDet","Fecha<>'1000'",null);
                BD.delete("t_Empleados_Huerta","c_codigo_emp<>'1000'",null);
                BD.delete("t_Cosecha","Fecha<>'1000'",null);
                BD.delete("t_Activos_Huerta","Id_ActivosGas<>'1000'",null);
                BD.delete("t_Actividades_Huerta","v_nombre_act<>'1000'",null);
                BD.delete("t_Fertiliza","Id_Fertiliza<>'1000'",null);
                BD.delete("t_Fertiliza_Det","Id_Fertiliza<>'1000'",null);
                BD.delete("t_Consumo_Gasolina","d_fechacrea_gas<>'1000'",null);
                BD.delete("t_Eliminados_Gasolina","d_fechacrea_gas<>'1000'",null);
                BD.delete("t_Entradas_Gasolina","d_fechacrea_gas<>'1000'",null);
                BD.delete("t_Variedad","Id_Variedad<>'1000'",null);
                BD.delete("t_Revision","Fecha<>'1000'",null);
                //BD.delete("t_RiegoV2","id_Sincroniza<>'1000'",null);
                //BD.delete("t_Riego_Valvulas","id_Sincroniza<>'1000'",null);
                //BD.delete("t_Valvulas","id_Sincroniza<>'1000'",null);
                //BD.delete("t_Cambio_Riego_Det","id_Sincroniza<>'1000'",null);

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