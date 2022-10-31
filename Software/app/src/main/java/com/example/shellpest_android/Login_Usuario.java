package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Login_Usuario extends AppCompatActivity {

    private EditText et_Usuario,et_Password;
    public String MyIp;
    ConexionInternet obj;

    String Version="V.22.10.03.02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);
		getSupportActionBar().hide();

        ImageView img = (ImageView) findViewById(R.id.loadingviewlogin);
        img.setBackgroundResource(R.drawable.loadinggif);

        AnimationDrawable frameAnimation;
        frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

        et_Usuario=(EditText)findViewById(R.id.et_Usuario);
        et_Password=(EditText)findViewById(R.id.et_Password);
        obj = new ConexionInternet(this);

        Existe_Usuario(revisa_Version());
    }

    private boolean revisa_Version(){
        //obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            return true;
        }else{
            Obtener_Ip();
            String sql;
            if(MyIp.equals("0.0.0.0")){
                sql="http://177.241.250.117:8090//Control/Version";
            }else{
                if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0){
                    sql = "http://192.168.3.254:8090//Control/Version";
                }else{
                    sql="http://177.241.250.117:8090//Control/Version";
                }
            }
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = null;
            try {
                url = new URL(sql);
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

                jsonarr = new JSONArray(json);

                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject jsonobject = jsonarr.getJSONObject(i);

                    if (jsonobject.optString("Version").length() > 0) {
                        if(!jsonobject.optString("Version").equals(Version)){
                            return false;

                        }else{
                            return true;
                        }
                    } else {
                        Toast.makeText(this, "Si entro al service web, pero no retorno datos", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(this, "Fallo la conexion al servidor [OPENPEDINS] 1", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(this, "Fallo la conexion al servidor [OPENPEDINS] 2", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(this, "Fallo la conexion al servidor [OPENPEDINS] 3", Toast.LENGTH_SHORT).show();
            }
            return false;
        }


    }

    public void Agrega_Usuario(String perfil,String huerta,String Nombre){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        String Id_Usuario=et_Usuario.getText().toString();
        String Contrasena=et_Password.getText().toString();

        ContentValues registro= new ContentValues();
        registro.put("Id_Usuario",Id_Usuario.toUpperCase());
        registro.put("Contrasena",Contrasena);
        registro.put("Id_Perfil",perfil);
        registro.put("Id_Huerta",huerta);
        registro.put("Nombre",Nombre);

        BD.insert("UsuarioLogin",null,registro);
        BD.close();
    }

    public void Existe_Usuario(boolean vertion){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select distinct Id_Usuario,Id_Perfil,Id_Huerta from UsuarioLogin",null);

        if(Renglon.moveToFirst()){
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.getString(0).length()>0){

                Toast.makeText(this,"Bienvenido "+Renglon.getString(0),Toast.LENGTH_SHORT).show();

                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                ParsePosition pp1 = new ParsePosition(0);
                java.util.Date convertidaFecha=formato.parse( RevisaFechaSync(), pp1);

                Intent intento;
                if (RevisaFechaSync().trim().length()>0){

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    java.util.Date fechaactual = cal.getTime(); //Para tomas solo fecha sin horas ni min.


                    if( fechaactual.getTime() - convertidaFecha.getTime() >172800000){ //la resta arroja los datos en milisegundos, la cantidad permitida son 2 dias

                        intento= new Intent(this, MainActivity.class);
                        intento.putExtra("usuario", Renglon.getString(0));
                        intento.putExtra("perfil", Renglon.getString(1));
                        intento.putExtra("huerta", Renglon.getString(2));


                        //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
                        //startActivity(intento);
                    }
                    else{
                        intento = new Intent(this, EnviaRecibe.class);
                        intento.putExtra("usuario", Renglon.getString(0));
                        intento.putExtra("perfil", Renglon.getString(1));
                        intento.putExtra("huerta", Renglon.getString(2));

                        //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();

                        //startActivity(intento);
                    }
                }else{
                    intento = new Intent(this, MainActivity.class);
                    intento.putExtra("usuario", Renglon.getString(0));
                    intento.putExtra("perfil", Renglon.getString(1));
                    intento.putExtra("huerta", Renglon.getString(2));

                    //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();

                    //startActivity(intento);
                }
                if(!vertion){
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Login_Usuario.this);
                    dialogo1.setTitle("VERSION DESACTUALIZADA");
                    dialogo1.setMessage("Pregunta a la encargada de agricultura de precision sobre la nueva version.");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            startActivity(intento);
                            finish();
                        }
                    });

                    dialogo1.show();
                }else{
                    startActivity(intento);
                    finish();
                }

            }
            BD.close();
        }else{
            if(obj.isConnected())
            {
                Toast.makeText(this,"No hay usuarios guardados",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Login_Usuario.this, "Es necesario tener internet para iniciar sesion por primera vez.", Toast.LENGTH_SHORT).show();
            }

            BD.close();
        }
        //String Id_Usuario=et_Usuario.getText().toString();
        //String Contrasena=et_Password.getText().toString();
        /*if(!Id_Usuario.isEmpty() && !Contrasena.isEmpty()){

        }else{
            Toast.makeText(this,"Es necesario ingresar usuario y contraseña",Toast.LENGTH_SHORT).show();
        }*/
    }

    public void Eliminar_UsuarioLogin(){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

        int cantidad= BD.delete("UsuarioLogin","Id_Usuario<>'root'",null);
        BD.close();

        if(cantidad>0){
            Toast.makeText(this,"Se Finalizo la sesion con exito.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Ocurrio un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
        }
    }

    public void Modificar_Usuario(View view){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

        String Id_Usuario=et_Usuario.getText().toString();
        String Contrasena=et_Password.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("Id_Usuario",Id_Usuario);
        registro.put("Contrasena",Contrasena);

        int cantidad=BD.update("UsuarioLogin",registro,"Id_Usuario="+Id_Usuario,null);
        BD.close();
        if(cantidad>0){
            Toast.makeText(this,"Se actualizo el usuario correctamente.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Ocurrio un error al intentar actualizar el usuario, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
        }
    }

     public void Obtener_Ip (){
            WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

            String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
            MyIp=Cip;
         Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
     }

    public void validarLogin(View view){
        if (et_Usuario.getText().length()>0 && et_Password.getText().length()>0){

            Obtener_Ip();
            if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
                //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
                String sql;
                if(MyIp.equals("0.0.0.0")){
                    sql="http://177.241.250.117:8090//Usuarios/LoginUsuario?User=" + et_Usuario.getText().toString().toUpperCase() + "&Pass=" + et_Password.getText().toString();
                }else{
                    if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0){
                        sql = "http://192.168.3.254:8090//Usuarios/LoginUsuario?User=" + et_Usuario.getText().toString().toUpperCase() + "&Pass=" + et_Password.getText().toString();
                    }else{
                        sql="http://177.241.250.117:8090//Usuarios/LoginUsuario?User=" + et_Usuario.getText().toString().toUpperCase() + "&Pass=" + et_Password.getText().toString();
                    }
                }

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url = null;
                try {
                    url = new URL(sql);
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

                    jsonarr = new JSONArray(json);

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobject = jsonarr.getJSONObject(i);

                        if (jsonobject.optString("Id_Usuario").length() > 0) {

                            Agrega_Usuario(jsonobject.optString("Id_Perfil"), jsonobject.optString("Id_Huerta"),jsonobject.optString("Nombre_Usuario"));

                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                            ParsePosition pp1 = new ParsePosition(0);
                            java.util.Date convertidaFecha=formato.parse( RevisaFechaSync(), pp1);

                            if (RevisaFechaSync().trim().length()>0){

                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                                java.util.Date fechaactual = cal.getTime(); //Para tomas solo fecha sin horas ni min.

                                //Date fechaactual = new Date(System.currentTimeMillis());
                                if( fechaactual.getTime() - convertidaFecha.getTime() >172800000){

                                    Intent intento = new Intent(this, MainActivity.class);
                                    intento.putExtra("usuario", jsonobject.optString("Id_Usuario").toUpperCase());
                                    intento.putExtra("perfil", jsonobject.optString("Id_Perfil"));
                                    intento.putExtra("huerta", jsonobject.optString("Id_Huerta"));


                                   //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();

                                   startActivity(intento);
                               }else{

                                    Intent intento = new Intent(this, EnviaRecibe.class);
                                    intento.putExtra("usuario", jsonobject.optString("Id_Usuario").toUpperCase());
                                    intento.putExtra("perfil", jsonobject.optString("Id_Perfil"));
                                    intento.putExtra("huerta", jsonobject.optString("Id_Huerta"));

                                    //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
                                    startActivity(intento);
                                }
                            }else{
                                Intent intento = new Intent(this, MainActivity.class);
                                intento.putExtra("usuario", jsonobject.optString("Id_Usuario").toUpperCase());
                                intento.putExtra("perfil", jsonobject.optString("Id_Perfil"));
                                intento.putExtra("huerta", jsonobject.optString("Id_Huerta"));

                                //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
                                startActivity(intento);
                            }

                            setVisible(false);
                            finish();

                        } else {
                            Toast.makeText(this, "Si entro al service web, pero no retorno datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    conn.disconnect();
                    Toast.makeText(this, "Fallo la conexion al servidor [OPENPEDINS] 1", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    conn.disconnect();
                    Toast.makeText(this, "Fallo la conexion al servidor [OPENPEDINS] 2", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    conn.disconnect();
                    Toast.makeText(this, "Fallo la conexion al servidor [OPENPEDINS] 3", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingresar usuario y contraseña.", Toast.LENGTH_SHORT).show();
        }
    }

    private String RevisaFechaSync(){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select Fecha_Sincroniza from FechaSincroniza",null);

        if(Renglon.moveToFirst()){
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.getString(0).length()>0){
               return Renglon.getString(0);
            }
            BD.close();
        }else{
            if(obj.isConnected())
            {
               return "";
            }else{
                Toast.makeText(Login_Usuario.this, "Es necesario tener internet para iniciar sesion por primera vez.", Toast.LENGTH_SHORT).show();
            }

            BD.close();
        }
        return "";
    }
}