package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Enviar extends AppCompatActivity {

    public String MyIp;
    ConexionInternet obj;

    TextView txt_NPuntos,txt_NPE,txt_Fechas;

    CheckBox check_Archivo;

    String ParaTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

        txt_NPuntos=(TextView)findViewById(R.id.txt_NBloques);
        txt_NPE=(TextView)findViewById(R.id.txt_NPE);
        txt_Fechas=(TextView)findViewById(R.id.txt_RFechas);

        check_Archivo=(CheckBox) findViewById(R.id.check_ArchivoM);

        CargaDatos();

        if(!Utilidades.PermisoConcedido(this)){
            new AlertDialog.Builder(this).setTitle("Permitir Acceso a archivos")
                    .setMessage("Resriccion de android, Esta aplicacion requiere que concedas acceso a tus archivos")
                    .setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ConcederPermisos();
                            try {
                                //Intent Compartir=new Intent(Intent.ACTION_SEND);


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("Denegar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        else{
            //Toast.makeText(this, "Permisos ya concedidos", Toast.LENGTH_SHORT).show();
        }

    }

    private void ConcederPermisos(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            try {
                Intent intento= new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intento.addCategory("android.intent.category.DEFAULT");
                Uri uri=Uri.fromParts("package",getPackageName(),null);
                intento.setData(uri);
                startActivityForResult(intento,101);
            }catch (Exception e){
                e.printStackTrace();
                Intent intento= new Intent();
                intento.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intento,101);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select (select count(Id_PuntoControl) as P from t_Monitoreo_PEEncabezado)+(select count(Id_PuntoControl) as P from t_Monitoreo_Eliminados_PEEncabezado) as Puntos,(select count(Id_PuntoControl) from t_Monitoreo_PEDetalle)+(select count(Id_PuntoControl) from t_Monitoreo_Eliminados_PEDetalle) as PE,min(Fecha) as Fini,max(Fecha) as Ffin from t_Monitoreo_PEEncabezado " , null);

        if (Renglon.moveToFirst()) {

            do {
                txt_NPuntos.setText(Renglon.getString(0));
                        txt_NPE.setText(Renglon.getString(1));
                txt_Fechas.setText(Renglon.getString(2)+" - "+Renglon.getString(3));
            } while (Renglon.moveToNext());


        } else {
            Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

        }
    }

    public void Enviar(View view) {
        if (obj.isConnected() || check_Archivo.isChecked() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora,c_codigo_eps,F_UsuCrea,Observaciones,Fumigado from t_Monitoreo_PEEncabezado ", null);

            if (Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    insertWebEncabezado(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10));
                } while (Renglon.moveToNext());


            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }


            Cursor Renglon2 = BD.rawQuery("select Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,Id_Humbral,Hora,Cant_Individuos,Id_Fenologico,c_codigo_eps from t_Monitoreo_PEDetalle ", null);

            if (Renglon2.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon2.getString(7), Toast.LENGTH_SHORT).show();
                    insertWebDetalle(Renglon2.getString(0),Renglon2.getString(1),Renglon2.getString(2),Renglon2.getString(3),Renglon2.getString(4),Renglon2.getString(5),Renglon2.getString(6),Renglon2.getString(7),Renglon2.getString(8),Renglon2.getString(9),Renglon2.getString(10));
                } while (Renglon2.moveToNext());
            } else {
                //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
            }

            Cursor Renglon3 = BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora,c_codigo_eps,F_UsuCrea,Observaciones,Fumigado from t_Monitoreo_Eliminados_PEEncabezado ", null);

            if (Renglon3.moveToFirst()) {

                do {
                    insertWebEncabezadoElim(Renglon3.getString(0),Renglon3.getString(1),Renglon3.getString(2),Renglon3.getString(3),Renglon3.getString(4),Renglon3.getString(5),Renglon3.getString(6),Renglon3.getString(7),Renglon3.getString(8),Renglon3.getString(9),Renglon3.getString(10));
                } while (Renglon3.moveToNext());


            } else {
                //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }


            Cursor Renglon4 = BD.rawQuery("select Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,Id_Humbral,Hora,Cant_Individuos,Id_Fenologico,c_codigo_eps from t_Monitoreo_Eliminados_PEDetalle ", null);

            if (Renglon4.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    String Tho0;
                    if (Renglon4.getString(0).isEmpty()){
                        Tho0="";
                    }else{
                        Tho0=Renglon4.getString(0);
                    }
                    String Tho1;
                    if (Renglon4.getString(1).isEmpty()){
                        Tho1="";
                    }else{
                        Tho1=Renglon4.getString(1);
                    }
                    String Tho2;
                    if (Renglon4.getString(2).isEmpty()){
                        Tho2="";
                    }else{
                        Tho2=Renglon4.getString(2);
                    }
                    String Tho3;
                    if (Renglon4.getString(3).isEmpty()){
                        Tho3="";
                    }else{
                        Tho3=Renglon4.getString(3);
                    }
                    String Tho4;
                    if (Renglon4.getString(4).isEmpty()){
                        Tho4="";
                    }else{
                        Tho4=Renglon4.getString(4);
                    }
                    String Tho5;
                    if (Renglon4.getString(5).isEmpty()){
                        Tho5="";
                    }else{
                        Tho5=Renglon4.getString(5);
                    }
                    String Tho6;
                    if (Renglon4.getString(6).isEmpty()){
                        Tho6="";
                    }else{
                        Tho6=Renglon4.getString(6);
                    }
                    String Tho7;
                    if (Renglon4.getString(7).isEmpty()){
                        Tho7="";
                    }else{
                        Tho7=Renglon4.getString(7);
                    }
                    int Tho8;

                        Tho8=Renglon4.getInt(8);

                    String Tho9;
                    if (Renglon4.getString(9).isEmpty()){
                        Tho9="";
                    }else{
                        Tho9=Renglon4.getString(9);
                    }
                    String Tho10;
                    if (Renglon4.getString(10).isEmpty()){
                        Tho10="";
                    }else{
                        Tho10=Renglon4.getString(10);
                    }
                    insertWebDetalleElim(Tho0,Tho1,Tho2,Tho3,Tho4,Tho5,Tho6,Tho7,String.valueOf(Tho8),Tho9,Tho10);
                } while (Renglon4.moveToNext());
            } else {
                //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }
            BD.close();
        }else{
        Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }

        CargaDatos();
    }

    public void enviarNew(View view){
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora,c_codigo_eps,F_UsuCrea,Observaciones,Fumigado from t_Monitoreo_PEEncabezado ", null);
            ParaTexto="";
            if (Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();

                    if(!check_Archivo.isChecked()) {
                        insertWebEncabezado(Renglon.getString(0), Renglon.getString(1), Renglon.getString(2), Renglon.getString(3), Renglon.getString(4), Renglon.getString(5), Renglon.getString(6), Renglon.getString(7), Renglon.getString(8), Renglon.getString(9), Renglon.getString(10));
                    }
                    GeneraLineaSql(Renglon.getString(0), Renglon.getString(1), Renglon.getString(2), Renglon.getString(3), Renglon.getString(4), Renglon.getString(5), Renglon.getString(6), Renglon.getString(7), Renglon.getString(8), Renglon.getString(9), Renglon.getString(10));
                } while (Renglon.moveToNext());
                EscribirArchivo(ParaTexto);
            } else {
                if(check_Archivo.isChecked()){
                    File textFile = new File(Environment.getExternalStorageDirectory(), "Monitoreo.txt");
                    if(textFile.exists()){
                        CompartirArchivo(textFile);
                    }
                }else{
                    Toast.makeText(getApplicationContext() , "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
                }
            }

/*Omitimos eliminados, no se requieren
            Cursor Renglon3 = BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora,c_codigo_eps,F_UsuCrea,Observaciones,Fumigado from t_Monitoreo_Eliminados_PEEncabezado ", null);

            if (Renglon3.moveToFirst()) {

                do {
                    insertWebEncabezadoElim(Renglon3.getString(0),Renglon3.getString(1),Renglon3.getString(2),Renglon3.getString(3),Renglon3.getString(4),Renglon3.getString(5),Renglon3.getString(6),Renglon3.getString(7),Renglon3.getString(8),Renglon3.getString(9),Renglon3.getString(10));
                } while (Renglon3.moveToNext());


            } else {
                //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }


            Cursor Renglon4 = BD.rawQuery("select Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,Id_Humbral,Hora,Cant_Individuos,Id_Fenologico,c_codigo_eps from t_Monitoreo_Eliminados_PEDetalle ", null);

            if (Renglon4.moveToFirst()) {
            --et_Usuario.setText(Renglon.getString(0));
            --et_Password.setText(Renglon.getString(1));
                do {
                    String Tho0;
                    if (Renglon4.getString(0).isEmpty()){
                        Tho0="";
                    }else{
                        Tho0=Renglon4.getString(0);
                    }
                    String Tho1;
                    if (Renglon4.getString(1).isEmpty()){
                        Tho1="";
                    }else{
                        Tho1=Renglon4.getString(1);
                    }
                    String Tho2;
                    if (Renglon4.getString(2).isEmpty()){
                        Tho2="";
                    }else{
                        Tho2=Renglon4.getString(2);
                    }
                    String Tho3;
                    if (Renglon4.getString(3).isEmpty()){
                        Tho3="";
                    }else{
                        Tho3=Renglon4.getString(3);
                    }
                    String Tho4;
                    if (Renglon4.getString(4).isEmpty()){
                        Tho4="";
                    }else{
                        Tho4=Renglon4.getString(4);
                    }
                    String Tho5;
                    if (Renglon4.getString(5).isEmpty()){
                        Tho5="";
                    }else{
                        Tho5=Renglon4.getString(5);
                    }
                    String Tho6;
                    if (Renglon4.getString(6).isEmpty()){
                        Tho6="";
                    }else{
                        Tho6=Renglon4.getString(6);
                    }
                    String Tho7;
                    if (Renglon4.getString(7).isEmpty()){
                        Tho7="";
                    }else{
                        Tho7=Renglon4.getString(7);
                    }
                    int Tho8;

                    Tho8=Renglon4.getInt(8);

                    String Tho9;
                    if (Renglon4.getString(9).isEmpty()){
                        Tho9="";
                    }else{
                        Tho9=Renglon4.getString(9);
                    }
                    String Tho10;
                    if (Renglon4.getString(10).isEmpty()){
                        Tho10="";
                    }else{
                        Tho10=Renglon4.getString(10);
                    }
                    insertWebDetalleElim(Tho0,Tho1,Tho2,Tho3,Tho4,Tho5,Tho6,Tho7,String.valueOf(Tho8),Tho9,Tho10);
                } while (Renglon4.moveToNext());
            } else {
                //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }
        */
            BD.close();
        }else{
            Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }

        CargaDatos();
    }

    private void GeneraLineaSql (String Fecha,String Id_Huerta,String Id_PuntoControl,String Id_Usuario,String n_coordenadaX,String n_coordenadaY,String Hora,String c_codigo_eps,String F_UsuCrea, String Observaciones, String Fumigado){
        ParaTexto=ParaTexto+"INSERT INTO t_Monitoreo_PE_Encabezado\n" +
                "(Id_PuntoControl, Fecha, Hora, Id_Huerta, n_CoordenadaX, n_CoordenadaY, Id_Usuario, F_Envio, c_codigo_eps, F_UsuCrea, Observaciones, Fumigado)\n" +
                "values ('"+Id_PuntoControl+"','"+Fecha.substring(6,10)+Fecha.substring(3,5)+Fecha.substring(0,2)+"','"+Hora+"','"+Id_Huerta+"',"+n_coordenadaX+","+n_coordenadaY+",'"+Id_Usuario+"',getdate(),'"+c_codigo_eps+"','"+F_UsuCrea.substring(6,10)+F_UsuCrea.substring(3,5)+F_UsuCrea.substring(0,2)+"','"+Observaciones+"','"+Fumigado+"') \n";
        selectDetMonIndiviual(Fecha,Id_PuntoControl,Hora);
        EliminadeMonitoreoEncabezado(Fecha,  Id_Huerta,  Id_PuntoControl,  Hora);
    }

    private void GeneraLineaSqlDet (String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo,String Id_Humbral,String Hora,String Cant_Individuos,String Id_Fenologico,String c_codigo_eps){
        ParaTexto=ParaTexto+"INSERT INTO ShellPest.dbo.t_Monitoreo_PE_Detalle \n"+
        "(Id_PuntoControl, Fecha, Hora, Id_Plagas, Id_Enfermedad, Id_Deteccion, Id_Individuo, Id_Humbral, c_codigo_eps, Cant_Individuos, Id_Fenologico) \n"+
        "values ('"+Id_PuntoControl+"','"+Fecha.substring(6,10)+Fecha.substring(3,5)+Fecha.substring(0,2)+"','"+Hora+"','"+Id_Plagas+"','"+Id_Enfermedad+"','"+Id_Deteccion+"','"+Id_Individuo+"','"+Id_Humbral+"','"+c_codigo_eps+"','"+Cant_Individuos+"','"+Id_Fenologico+"') \n";

        EliminadeMonitoreoDetalle(Fecha,   Id_Plagas,  Id_Enfermedad,  Id_PuntoControl,  Id_Deteccion,  Id_Fenologico);
    }

    private void EscribirArchivo(String Texto){
        try {

            File textFile = new File(Environment.getExternalStorageDirectory(), "Monitoreo.txt");
            textFile.createNewFile();
            if (textFile.exists()) {
                textFile.setReadable(true);
                if (textFile.canRead()) {

                } else {
                    Toast.makeText(Enviar.this, "No se puede leer", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Enviar.this, "No existe", Toast.LENGTH_SHORT).show();
            }
            textFile.setWritable(true);

            if (textFile.canWrite()) {

                FileOutputStream fos = new FileOutputStream(textFile);
                fos.write(Texto.getBytes());

                fos.flush();
                fos.close();

                if (check_Archivo.isChecked()) {
                    Toast.makeText(Enviar.this, "Archivo Generado Correctamente", Toast.LENGTH_SHORT).show();
                    CompartirArchivo(textFile);
                }
            } else {
                Toast.makeText(Enviar.this, "No se puede escribir", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ioe){
            Toast.makeText(Enviar.this, "Falta permiso ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CompartirArchivo(File Archivo){
        Intent Compartir=new Intent(Intent.ACTION_SEND);

        Uri uriArchivo = FileProvider.getUriForFile(
                getApplicationContext(),
                BuildConfig.APPLICATION_ID  + ".provider",
                Archivo);

        Compartir.setDataAndType(uriArchivo,"text/txt");

        Compartir.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Compartir.putExtra(Intent.EXTRA_STREAM, uriArchivo);

        startActivity(Intent.createChooser(Compartir, "Enviar por"));
    }

    private void selectDetMonIndiviual(String pFecha,String pId_PuntoControl,String pHora){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon2 = BD.rawQuery("select Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,Id_Humbral,Hora,Cant_Individuos,Id_Fenologico,c_codigo_eps from t_Monitoreo_PEDetalle where Fecha='"+pFecha+"' and Id_PuntoControl='"+pId_PuntoControl+"' and Hora='"+pHora+"' ", null);

        if (Renglon2.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            do {
                //Toast.makeText(this, Renglon2.getString(7), Toast.LENGTH_SHORT).show();

                if(!check_Archivo.isChecked()) {
                    insertWebDetalle(Renglon2.getString(0), Renglon2.getString(1), Renglon2.getString(2), Renglon2.getString(3), Renglon2.getString(4), Renglon2.getString(5), Renglon2.getString(6), Renglon2.getString(7), Renglon2.getString(8), Renglon2.getString(9), Renglon2.getString(10));
                }
                GeneraLineaSqlDet(Renglon2.getString(0), Renglon2.getString(1), Renglon2.getString(2), Renglon2.getString(3), Renglon2.getString(4), Renglon2.getString(5), Renglon2.getString(6), Renglon2.getString(7), Renglon2.getString(8), Renglon2.getString(9), Renglon2.getString(10));
            } while (Renglon2.moveToNext());
        } else {
            //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
        }
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

    private void insertWebEncabezado(String Fecha,String Id_Huerta,String Id_PuntoControl,String Id_Usuario,String n_coordenadaX,String n_coordenadaY,String Hora,String c_codigo_eps,String F_UsuCrea, String Observaciones, String Fumigado) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Hora == null){
            Hora="";
        }
        if(Id_Huerta== null){
            Id_Huerta="";
        }

        if(Id_PuntoControl==null){
            Id_PuntoControl="";
        }

        if(Id_Usuario==null){
            Id_Usuario="";
        }

        if(n_coordenadaX==null){
            n_coordenadaX="";
        }
        if(n_coordenadaY==null){
            n_coordenadaY="";
        }

        if(Observaciones==null){
            Observaciones="";
        }
        if(Fumigado==null){
            Fumigado="";
        }

        String dia,mes,ano,dia2,mes2,ano2;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        ano2=F_UsuCrea.substring(6);
        mes2=F_UsuCrea.substring(3, 5);
        dia2=F_UsuCrea.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEEncabezado_new?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps+ "&F_UsuCrea=" +ano2 + "" + mes2 + "" + dia2+ "&Observaciones=" + Observaciones+ "&Fumigado=" + Fumigado;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.PortLocal+"//Control/MonitoreoPEEncabezado_new?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps+ "&F_UsuCrea=" +ano2 + "" + mes2 + "" + dia2+ "&Observaciones=" + Observaciones+ "&Fumigado=" + Fumigado;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEEncabezado_new?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps+ "&F_UsuCrea=" +ano2 + "" + mes2 + "" + dia2+ "&Observaciones=" + Observaciones+ "&Fumigado=" + Fumigado;
            }
        }
        URL url = null;
        try {
            url = new URL(Liga);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonarr = null;

            try {
                jsonarr = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonarr.length() > 0) {
                String[][] datos;
                JSONObject jsonobject = jsonarr.getJSONObject(0);
                datos = new String[jsonarr.length()][jsonobject.length()];

                String NombreId = "";
                for (int i = 0; i < jsonarr.length(); i++) {
                    jsonobject = jsonarr.getJSONObject(i);

                    int columnas = 0;



                    int RegistrosEnviados=0;


                        if (jsonobject.optString("Mensaje").equals("1")) {
                            selectDetMonIndiviual(Fecha,Id_PuntoControl,Hora);
                            /* inhabilitado para que no borre solo hasta que se genere el archivo
                            if (EliminadeMonitoreoEncabezado(Fecha,  Id_Huerta,  Id_PuntoControl,  Hora)){
                                RegistrosEnviados++;
                            }*/

                    }else{
                            if (jsonobject.optString("Mensaje").indexOf("Violation of PRIMARY KEY")>=0){
                                Toast.makeText(Enviar.this, "Ya se envio ese punto en la fecha indicada", Toast.LENGTH_SHORT).show();
                            }
                        }
                }
            }
            in.close();
            conn.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertWebDetalle(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo,String Id_Humbral,String Hora,String Cant_Individuos,String Id_Fenologico,String c_codigo_eps) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Id_Plagas== null){
            Id_Plagas="";
        }
        if(Id_Enfermedad==null){
            Id_Enfermedad="";
        }
        if(Id_PuntoControl==null){
            Id_PuntoControl="";
        }
        if(Id_Deteccion==null){
            Id_Deteccion="";
        }
        if(Id_Individuo==null){
            Id_Individuo="";
        }

        if(Id_Humbral==null){
            Id_Humbral="";
        }

        if (Cant_Individuos==null){
            Cant_Individuos="";
        }

        if(Id_Fenologico==null){
            Id_Fenologico="";
        }

        String dia,mes,ano;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.PortLocal+"//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
            }
        }
        URL url = null;
        try {
            url = new URL(Liga);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonarr = null;

            try {
                jsonarr = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonarr.length() > 0) {
                String[][] datos;
                JSONObject jsonobject = jsonarr.getJSONObject(0);
                datos = new String[jsonarr.length()][jsonobject.length()];

                String NombreId = "";
                for (int i = 0; i < jsonarr.length(); i++) {
                    jsonobject = jsonarr.getJSONObject(i);

                    int columnas = 0;
                    int RegistrosEnviados=0;

                    if (jsonobject.optString("Mensaje").equals("1")) {
                        /*Inhabilitado para que no borre hasta que se genere el archivo
                        if (EliminadeMonitoreoDetalle(Fecha,   Id_Plagas,  Id_Enfermedad,  Id_PuntoControl,  Id_Deteccion,  Id_Individuo)){
                            RegistrosEnviados++;
                        }*/

                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }


    private void insertWebEncabezadoElim(String Fecha,String Id_Huerta,String Id_PuntoControl,String Id_Usuario,String n_coordenadaX,String n_coordenadaY,String Hora,String c_codigo_eps,String F_UsuCrea, String Observaciones,String Fumigado) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Hora == null){
            Hora="";
        }
        if(Id_Huerta== null){
            Id_Huerta="";
        }

        if(Id_PuntoControl==null){
            Id_PuntoControl="";
        }

        if(Id_Usuario==null){
            Id_Usuario="";
        }

        if(n_coordenadaX==null){
            n_coordenadaX="";
        }
        if(n_coordenadaY==null){
            n_coordenadaY="";
        }

        if(Observaciones== null){
            Observaciones="";
        }

        if(Fumigado== null){
            Fumigado="";
        }

        String dia,mes,ano,dia2,mes2,ano2;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        ano2=F_UsuCrea.substring(6);
        mes2=F_UsuCrea.substring(3, 5);
        dia2=F_UsuCrea.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEEncabezadoEliminado_new?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps+ "&F_UsuCrea=" +ano2 + "" + mes2 + "" + dia2+ "&Observaciones=" + Observaciones+ "&Fumigado=" + Fumigado;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.PortLocal+"//Control/MonitoreoPEEncabezadoEliminado_new?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps+ "&F_UsuCrea=" +ano2 + "" + mes2 + "" + dia2+ "&Observaciones=" + Observaciones+ "&Fumigado=" + Fumigado;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEEncabezadoEliminado_new?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps+ "&F_UsuCrea=" +ano2 + "" + mes2 + "" + dia2+ "&Observaciones=" + Observaciones+ "&Fumigado=" + Fumigado;
            }
        }
        URL url = null;
        try {
            url = new URL(Liga);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonarr = null;

            try {
                jsonarr = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonarr.length() > 0) {
                String[][] datos;
                JSONObject jsonobject = jsonarr.getJSONObject(0);
                datos = new String[jsonarr.length()][jsonobject.length()];

                String NombreId = "";
                for (int i = 0; i < jsonarr.length(); i++) {
                    jsonobject = jsonarr.getJSONObject(i);

                    int columnas = 0;
                    int RegistrosEnviados=0;
                    if (jsonobject.optString("Mensaje").equals("1")) {
                        if (EliminadeMonitoreoEncabezadoElim(Fecha,  Id_Huerta,  Id_PuntoControl,  Hora)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertWebDetalleElim(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo,String Id_Humbral,String Hora,String Cant_Individuos,String Id_Fenologico,String c_codigo_eps) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Id_Plagas== null){
            Id_Plagas="";
        }
        if(Id_Enfermedad==null){
            Id_Enfermedad="";
        }
        if(Id_PuntoControl==null){
            Id_PuntoControl="";
        }
        if(Id_Deteccion==null){
            Id_Deteccion="";
        }
        if(Id_Individuo==null){
            Id_Individuo="";
        }
        if(Id_Humbral==null){
            Id_Humbral="";
        }

        if (Cant_Individuos==null){
            Cant_Individuos="";
        }

        if(Id_Fenologico==null){
            Id_Fenologico="";
        }

        String dia,mes,ano;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEDetalleEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.PortLocal+"//Control/MonitoreoPEDetalleEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/MonitoreoPEDetalleEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral + "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
            }
        }
        URL url = null;
        try {
            url = new URL(Liga);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonarr = null;

            try {
                jsonarr = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonarr.length() > 0) {
                String[][] datos;
                JSONObject jsonobject = jsonarr.getJSONObject(0);
                datos = new String[jsonarr.length()][jsonobject.length()];

                String NombreId = "";
                for (int i = 0; i < jsonarr.length(); i++) {
                    jsonobject = jsonarr.getJSONObject(i);

                    int columnas = 0;
                    int RegistrosEnviados=0;

                    if (jsonobject.optString("Mensaje").equals("1")) {
                        if (EliminadeMonitoreoDetalleElim(Fecha,   Id_Plagas,  Id_Enfermedad,  Id_PuntoControl,  Id_Deteccion,  Id_Individuo)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean EliminadeMonitoreoEncabezado(String Fecha, String Id_Huerta, String Id_PuntoControl, String Hora) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_Monitoreo_PEEncabezado", "Fecha='"+Fecha+"' and Id_Huerta='"+Id_Huerta+"' and Id_PuntoControl='"+Id_PuntoControl+"' and  Hora='" + Hora + "' ", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminadeMonitoreoDetalle(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Fenologico) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;
        if(Id_Deteccion.length()==0){
            cantidad= BD.delete("t_Monitoreo_PEDetalle", "Fecha='"+Fecha+"' and Id_PuntoControl='"+Id_PuntoControl+"' ", null);
        }else{
            cantidad = BD.delete("t_Monitoreo_PEDetalle", "Fecha='"+Fecha+"' and Id_Plagas='"+Id_Plagas+"' and Id_Enfermedad='"+Id_Enfermedad+"' and Id_PuntoControl='"+Id_PuntoControl+"' and  Id_Deteccion='" + Id_Deteccion + "' and Id_Fenologico='"+Id_Fenologico+"'", null);
        }

        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminadeMonitoreoEncabezadoElim(String Fecha, String Id_Huerta, String Id_PuntoControl, String Hora) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_Monitoreo_Eliminados_PEEncabezado", "Fecha='"+Fecha+"' and Id_Huerta='"+Id_Huerta+"' and Id_PuntoControl='"+Id_PuntoControl+"' and  Hora='" + Hora + "' ", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminadeMonitoreoDetalleElim(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_Monitoreo_Eliminados_PEDetalle", "Fecha='"+Fecha+"' and Id_Plagas='"+Id_Plagas+"' and Id_Enfermedad='"+Id_Enfermedad+"' and Id_PuntoControl='"+Id_PuntoControl+"' and  Id_Deteccion='" + Id_Deteccion + "' and Id_Individuo='"+Id_Individuo+"'", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

}