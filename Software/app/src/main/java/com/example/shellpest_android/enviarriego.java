package com.example.shellpest_android;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
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
import java.util.Date;
//import java.sql.Date;



public class enviarriego extends AppCompatActivity {

    public String MyIp;
    ConexionInternet obj;

    TextView txt_NBloques,txt_RFechas;
    CheckBox check_Archivo;
    String ParaTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviarriego);

        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

        txt_NBloques=(TextView)findViewById(R.id.txt_NBloques);
        txt_RFechas=(TextView)findViewById(R.id.txt_RFechas);

        check_Archivo=(CheckBox) findViewById(R.id.check_Archivo);

        CargaDatos();

        if(!Utilidades.PermisoConcedido(this)){
            new AlertDialog.Builder(this).setTitle("Permitir Acceso a archivos")
                    .setMessage("Resriccion de android, Esta aplicacion requiere que concedas acceso a tus archivos")
                    .setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ConcederPermisos();
                            try {



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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if(requestCode==101){
                boolean readExt= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(!readExt){
                    ConcederPermisos();
                }
            }
        }
    }

    public void Enviar(View view) {

        if (obj.isConnected() || check_Archivo.isChecked()) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha , Hora, Id_Bloque, Precipitacion_Sistema, Caudal_Inicio , Caudal_Fin , Horas_Riego , Id_Usuario, c_codigo_eps, Temperatura, ET,F_UsuCrea from t_Riego ", null);
            ParaTexto="";

            if (Renglon.moveToFirst()) {
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    GeneraLineaSql(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getFloat(3),Renglon.getFloat(4),Renglon.getFloat(5),Renglon.getFloat(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10),Renglon.getString(11));
                    if(!check_Archivo.isChecked()){
                        insertWebRiego(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getFloat(3),Renglon.getFloat(4),Renglon.getFloat(5),Renglon.getFloat(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10),Renglon.getString(11));
                    }
                } while (Renglon.moveToNext());
                EscribirArchivo(ParaTexto);
            } else {
                if(check_Archivo.isChecked()){
                    File textFile = new File(Environment.getExternalStorageDirectory(), "Riego.txt");
                    if(textFile.exists()){
                        CompartirArchivo(textFile);
                    }
                }else{
                    Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
                }

            }
            /* Quite para no enviar eliminidados
            Cursor Renglon3 = BD.rawQuery("select  Fecha, Hora, Id_Bloque, Precipitacion_Sistema, Caudal_Inicio, Caudal_Fin, Horas_riego, Id_Usuario, c_codigo_eps, Temperatura, ET,F_UsuCrea from t_RiegoEliminado ", null);

            if (Renglon3.moveToFirst()) {
                do {
                    insertWebRiegoElim(Renglon3.getString(0),Renglon3.getString(1),Renglon3.getString(2),Renglon3.getFloat(3),Renglon3.getFloat(4),Renglon3.getFloat(5),Renglon3.getFloat(6),Renglon3.getString(7),Renglon3.getString(8),Renglon3.getString(9),Renglon3.getString(10),Renglon3.getString(11));
                } while (Renglon3.moveToNext());
            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
            }
            BD.close();*/
        }else{
            Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }
        CargaDatos();
    }

    private void GeneraLineaSql (String Fecha ,String Hora,String Id_Bloque,float Precipitacion_Sistema,float Caudal_Inicio ,float Caudal_Fin ,float Horas_Riego ,String Id_Usuario, String Empresa, String Temperatura, String ET,String F_UsuCrea){

        ParaTexto=ParaTexto+"insert into t_Riego (Fecha,Hora,Id_Bloque,Precipitacion_Sistema,Caudal_Inicio,Caudal_Fin,Horas_Riego,Id_Usuario_Crea,c_codigo_eps,Temperatura,ET,F_Usuario_Crea,F_Envio) " +
                "values ('"+ Fecha.substring(6,10)+Fecha.substring(3,5)+Fecha.substring(0,2)+"','"+Hora+"','"+Id_Bloque+"',"+Precipitacion_Sistema+","+Caudal_Inicio+","+Caudal_Fin+","+Horas_Riego+",'"+Id_Usuario+"','"+Empresa+"','"+Temperatura+"','"+ET+"','"+F_UsuCrea.substring(6,10)+F_UsuCrea.substring(3,5)+F_UsuCrea.substring(0,2)+"',getdate()) \n";
        if(ParaTexto.trim().length()>0){
            EliminadeRiego(Fecha,  Id_Bloque);
        }
    }

    private void CompartirArchivo(File Archivo){
        Intent Compartir=new Intent(Intent.ACTION_SEND);

        //String Ruta=Archivo.getPath() + "/Riego.txt";
        //Uri uriArchivo=Uri.fromFile(Archivo);
        Uri uriArchivo = FileProvider.getUriForFile(
                getApplicationContext(),
                BuildConfig.APPLICATION_ID  + ".provider",
                Archivo);
        //Uri uriArchivo=Uri.parse( Ruta);
        Compartir.setDataAndType(uriArchivo,"text/txt");

        //Compartir.setType("text/txt");
        //Compartir.putExtra(MediaStore.EXTRA_OUTPUT, Archivo);
        Compartir.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Compartir.putExtra(Intent.EXTRA_STREAM, uriArchivo);
        //Compartir.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(Compartir, "Enviar por"));

    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select (select count(Id_Bloque) as P from t_Riego)+(select count(Id_Bloque) as P from t_RiegoEliminado) as Puntos,min(Fecha) as Fini,max(Fecha) as Ffin from t_Riego " , null);

        if (Renglon.moveToFirst()) {

            do {
                txt_NBloques.setText(Renglon.getString(0));

                txt_RFechas.setText(Renglon.getString(1)+" - "+Renglon.getString(2));
            } while (Renglon.moveToNext());
        } else {
            Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
        }
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

    private void insertWebRiego(String Fecha ,String Hora,String Id_Bloque,float Precipitacion_Sistema,float Caudal_Inicio ,float Caudal_Fin ,float Horas_Riego ,String Id_Usuario, String Empresa, String Temperatura, String ET,String F_UsuCrea) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Hora == null){
            Hora="";
        }
        if(Id_Bloque== null){
            Id_Bloque="";
        }
        if(Id_Usuario==null){
            Id_Usuario="";
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
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Riego?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque + "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_Riego+"&Id_Usuario="+Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.PortLocal+"//Control/Riego?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque +  "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_Riego+"&Id_Usuario="+Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Riego?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque +  "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_Riego+"&Id_Usuario="+Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
            }
        }

        //Toast.makeText(enviarriego.this, Liga, Toast.LENGTH_SHORT).show();
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
                        if (EliminadeRiego(Fecha,  Id_Bloque)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(enviarriego.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(enviarriego.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(enviarriego.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertWebRiegoElim(String Fecha,String Hora,String Id_Bloque,float Precipitacion_Sistema,float Caudal_Inicio,float Caudal_Fin,float Horas_riego,String Id_Usuario,String Empresa, String Temperatura, String ET, String F_UsuCrea) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Hora == null){
            Hora="";
        }
        if(Id_Bloque== null){
            Id_Bloque="";
        }
        if(Id_Usuario==null){
            Id_Usuario="";
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
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/RiegoEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque + "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_riego+ "&Id_Usuario=" + Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.PortLocal+"//Control/RiegoEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque +  "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_riego+ "&Id_Usuario=" + Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/RiegoEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque +  "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_riego+ "&Id_Usuario=" + Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
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
                        if (EliminaRiegoElim(Fecha,  Id_Bloque,  Hora)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(enviarriego.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(enviarriego.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(enviarriego.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean EliminadeRiego(String Fecha, String Id_Bloque) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_Riego", "Fecha='"+Fecha+"' and Id_Bloque='"+Id_Bloque+"'  ", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminaRiegoElim(String Fecha, String Id_Bloque, String Hora) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_RiegoEliminado", "Fecha='"+Fecha+"' and Id_Bloque='"+Id_Bloque+"'  and  Hora='" + Hora + "' ", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }


    private void EscribirArchivo(String Texto){
        try {

            File textFile = new File(Environment.getExternalStorageDirectory(), "Riego.txt");
            textFile.createNewFile();
            if(textFile.exists()){
                textFile.setReadable(true);
                if(textFile.canRead()){

                }else{
                    Toast.makeText(enviarriego.this, "No se puede leer", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(enviarriego.this, "No existe", Toast.LENGTH_SHORT).show();
            }
            textFile.setWritable(true);

            if(textFile.canWrite()) {


                FileOutputStream fos = new FileOutputStream(textFile);
                fos.write(Texto.getBytes());

                fos.flush();
                fos.close();


                if(check_Archivo.isChecked()){
                    Toast.makeText(enviarriego.this, "Archivo Generado Correctamente", Toast.LENGTH_SHORT).show();
                    CompartirArchivo(textFile);
                }
            }else{
                Toast.makeText(enviarriego.this, "No se puede escribir", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}