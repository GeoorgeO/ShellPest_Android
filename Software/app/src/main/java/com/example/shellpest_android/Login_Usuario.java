package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
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



public class Login_Usuario extends AppCompatActivity {

    private EditText et_Usuario,et_Password;
    public String MyIp;
    ConexionInternet obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        et_Usuario=(EditText)findViewById(R.id.et_Usuario);
        et_Password=(EditText)findViewById(R.id.et_Password);
        obj = new ConexionInternet(this);
        Existe_Usuario();
    }

    public void Agrega_Usuario(String perfil,String huerta,String Nombre){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        String Id_Usuario=et_Usuario.getText().toString();
        String Contrasena=et_Password.getText().toString();

        ContentValues registro= new ContentValues();
        registro.put("Id_Usuario",Id_Usuario);
        registro.put("Contrasena",Contrasena);
        registro.put("Id_Perfil",perfil);
        registro.put("Id_Huerta",huerta);
        registro.put("Nombre",Nombre);

        BD.insert("UsuarioLogin",null,registro);
        BD.close();
    }

    public void Existe_Usuario(){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select Id_Usuario,Id_Perfil,Id_Huerta from UsuarioLogin",null);

        if(Renglon.moveToFirst()){
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.getString(0).length()>0){
                Intent intento=new Intent(this,EnviaRecibe.class);
                intento.putExtra("usuario", Renglon.getString(0));
                intento.putExtra("perfil", Renglon.getString(1));
                intento.putExtra("huerta", Renglon.getString(2));

                //Toast.makeText(this,Renglon.getString(0)+","+Renglon.getString(1)+","+Renglon.getString(2),Toast.LENGTH_SHORT).show();
                startActivity(intento);
                finish();
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
                    sql="http://177.241.250.117:8090//Usuarios/LoginUsuario?User=" + et_Usuario.getText().toString() + "&Pass=" + et_Password.getText().toString();
                }else{
                    if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0){
                        sql = "http://192.168.3.254:8090//Usuarios/LoginUsuario?User=" + et_Usuario.getText().toString() + "&Pass=" + et_Password.getText().toString();
                    }else{
                        sql="http://177.241.250.117:8090//Usuarios/LoginUsuario?User=" + et_Usuario.getText().toString() + "&Pass=" + et_Password.getText().toString();
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
                            Intent intento = new Intent(this, EnviaRecibe.class);
                            intento.putExtra("usuario", jsonobject.optString("Id_Usuario"));
                            intento.putExtra("perfil", jsonobject.optString("Id_Perfil"));
                            intento.putExtra("huerta", jsonobject.optString("Id_Huerta"));

                            //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();

                            startActivity(intento);
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

}