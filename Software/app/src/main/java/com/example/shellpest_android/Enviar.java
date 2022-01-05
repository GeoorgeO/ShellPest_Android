package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
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

public class Enviar extends AppCompatActivity {

    public String MyIp;
    ConexionInternet obj;

    TextView txt_NPuntos,txt_NPE,txt_Fechas;

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

        CargaDatos();
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
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora,c_codigo_eps from t_Monitoreo_PEEncabezado ", null);

            if (Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    insertWebEncabezado(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7));
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

            Cursor Renglon3 = BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora,c_codigo_eps from t_Monitoreo_Eliminados_PEEncabezado ", null);

            if (Renglon3.moveToFirst()) {

                do {
                    insertWebEncabezadoElim(Renglon3.getString(0),Renglon3.getString(1),Renglon3.getString(2),Renglon3.getString(3),Renglon3.getString(4),Renglon3.getString(5),Renglon3.getString(6),Renglon3.getString(7));
                } while (Renglon3.moveToNext());


            } else {
                //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }


            Cursor Renglon4 = BD.rawQuery("select Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,Id_Humbral,Hora,Cant_Individuos,Id_Fenologico,c_codigo_eps from t_Monitoreo_Eliminados_PEDetalle ", null);

            if (Renglon4.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    String Tho=Renglon4.getString(7);
                    insertWebDetalleElim(Renglon4.getString(0),Renglon4.getString(1),Renglon4.getString(2),Renglon4.getString(3),Renglon4.getString(4),Renglon4.getString(5),Renglon4.getString(6),Renglon4.getString(7),Renglon2.getString(8),Renglon2.getString(9),Renglon2.getString(10));
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

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

    private void insertWebEncabezado(String Fecha,String Id_Huerta,String Id_PuntoControl,String Id_Usuario,String n_coordenadaX,String n_coordenadaY,String Hora,String c_codigo_eps) {
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
            Liga = "http://177.241.250.117:8090//Control/MonitoreoPEEncabezado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/MonitoreoPEEncabezado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps;
            }else{
                Liga = "http://177.241.250.117:8090//Control/MonitoreoPEEncabezado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps;
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

                    }else{
                            if (jsonobject.optString("Mensaje").indexOf("Violation of PRIMARY KEY")>=0){
                                Toast.makeText(Enviar.this, "Ya se envio ese punto en la fecha indicada", Toast.LENGTH_SHORT).show();
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

    private void insertWebDetalle(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo,String Id_Humbral,String Hora,String Cant_Individuos,String Id_Fenologico,String c_codigo_eps) {
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
            Liga = "http://177.241.250.117:8090//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
            }else{
                Liga = "http://177.241.250.117:8090//Control/MonitoreoPEDetalle?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
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


    private void insertWebEncabezadoElim(String Fecha,String Id_Huerta,String Id_PuntoControl,String Id_Usuario,String n_coordenadaX,String n_coordenadaY,String Hora,String c_codigo_eps) {
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
            Liga = "http://177.241.250.117:8090//Control/MonitoreoPEEncabezadoEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/MonitoreoPEEncabezadoEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps;
            }else{
                Liga = "http://177.241.250.117:8090//Control/MonitoreoPEEncabezadoEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Huerta=" + Id_Huerta +  "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Usuario=" + Id_Usuario + "&n_CoordenadaX=" + n_coordenadaX + "&n_CoordenadaY=" + n_coordenadaY + "&c_codigo_eps=" + c_codigo_eps;
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
            Liga = "http://177.241.250.117:8090//Control/MonitoreoPEDetalleEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/MonitoreoPEDetalleEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral+ "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
            }else{
                Liga = "http://177.241.250.117:8090//Control/MonitoreoPEDetalleEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_PuntoControl=" + Id_PuntoControl + "&Id_Plagas=" + Id_Plagas + "&Id_Enfermedad=" + Id_Enfermedad + "&Id_Deteccion=" + Id_Deteccion + "&Id_Individuo=" + Id_Individuo + "&Id_Humbral=" + Id_Humbral + "&Cant_Individuos=" + Cant_Individuos + "&Id_Fenologico=" + Id_Fenologico + "&c_codigo_eps=" + c_codigo_eps;
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

    private boolean EliminadeMonitoreoDetalle(String Fecha,String Id_Plagas,String Id_Enfermedad,String Id_PuntoControl,String Id_Deteccion,String Id_Individuo) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;
        if(Id_Deteccion.length()==0){
            cantidad= BD.delete("t_Monitoreo_PEDetalle", "Fecha='"+Fecha+"' and Id_PuntoControl='"+Id_PuntoControl+"' ", null);
        }else{
            cantidad = BD.delete("t_Monitoreo_PEDetalle", "Fecha='"+Fecha+"' and Id_Plagas='"+Id_Plagas+"' and Id_Enfermedad='"+Id_Enfermedad+"' and Id_PuntoControl='"+Id_PuntoControl+"' and  Id_Deteccion='" + Id_Deteccion + "' and Id_Individuo='"+Id_Individuo+"'", null);
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