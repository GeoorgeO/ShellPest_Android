package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.View;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class Enviar extends AppCompatActivity {

    public String MyIp;
    ConexionInternet obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    public void Enviar(View view) {
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora from t_Monitoreo_PEEncabezado ", null);

            if (Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    insertWebEncabezado(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6));
                } while (Renglon.moveToNext());

                BD.close();
            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }else{
        Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }


    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

    private void insertWebEncabezado(String Fecha,String Id_Huerta,String Id_PuntoControl,String Id_Usuario,String n_coordenadaX,String n_coordenadaY,String Hora) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


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

        String dia,mes,ano;
                ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/MonitoreoPEEncabezado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/MonitoreoPEEncabezado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY;
            }else{
                Liga = "http://177.241.250.117:8090//Control/MonitoreoPEEncabezado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY;
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
                            if (EliminadeMonitoreoEncabezado(Fecha,  Id_Huerta,  Id_PuntoControl,  Hora)){
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

    private void insertWebDetalle(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo,String Id_Humbral,String Hora) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



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

        String dia,mes,ano;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral ;
            }else{
                Liga = "http://177.241.250.117:8090//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral ;
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
                        if (EliminadeMonitoreoDetalle(Fecha,   Id_Plagas,  Id_Enfermedad,  Id_PuntoControl,  Id_Deteccion,  Id_Individuo)){
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

    private boolean EliminadeMonitoreoDetalle(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_Monitoreo_PEDetalle", "Fecha='"+Fecha+"' and Id_Plagas='"+Id_Plagas+"' and Id_Enfermedad='"+Id_Enfermedad+"' and Id_PuntoControl='"+Id_PuntoControl+"' and  Id_Deteccion='" + Id_Deteccion + "' and Id_Individuo='"+Id_Individuo+"'", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

}