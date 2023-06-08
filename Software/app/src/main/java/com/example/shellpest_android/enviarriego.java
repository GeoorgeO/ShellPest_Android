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

public class enviarriego extends AppCompatActivity {

    public String MyIp;
    ConexionInternet obj;

    TextView txt_NBloques,txt_RFechas;

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

        CargaDatos();
    }

    public void Enviar(View view) {
        if (obj.isConnected() ) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha , Hora, Id_Bloque, Precipitacion_Sistema, Caudal_Inicio , Caudal_Fin , Horas_Riego , Id_Usuario, c_codigo_eps, Temperatura, ET,F_UsuCrea from t_Riego ", null);

            if (Renglon.moveToFirst()) {
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    insertWebRiego(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getFloat(3),Renglon.getFloat(4),Renglon.getFloat(5),Renglon.getFloat(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10),Renglon.getString(11));
                } while (Renglon.moveToNext());
            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
            }
            Cursor Renglon3 = BD.rawQuery("select  Fecha, Hora, Id_Bloque, Precipitacion_Sistema, Caudal_Inicio, Caudal_Fin, Horas_riego, Id_Usuario, c_codigo_eps, Temperatura, ET,F_UsuCrea from t_RiegoEliminado ", null);

            if (Renglon3.moveToFirst()) {
                do {
                    insertWebRiegoElim(Renglon3.getString(0),Renglon3.getString(1),Renglon3.getString(2),Renglon3.getFloat(3),Renglon3.getFloat(4),Renglon3.getFloat(5),Renglon3.getFloat(6),Renglon3.getString(7),Renglon3.getString(8),Renglon3.getString(9),Renglon3.getString(10),Renglon3.getString(11));
                } while (Renglon3.moveToNext());
            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
            }
            BD.close();
        }else{
            Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }
        CargaDatos();
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
                Liga = Clase.IpLocal+Clase.Puerto+"//Control/Riego?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque +  "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_Riego+"&Id_Usuario="+Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
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
                Liga = Clase.IpLocal+Clase.Puerto+"//Control/RiegoEliminado?Fecha=" + ano + "" + mes + "" + dia + "&Hora=" + Hora + "&Id_Bloque=" + Id_Bloque +  "&Precipitacion_Sistema=" + Precipitacion_Sistema + "&Caudal_Inicio=" + Caudal_Inicio + "&Caudal_Fin=" + Caudal_Fin + "&Horas_riego=" + Horas_riego+ "&Id_Usuario=" + Id_Usuario+"&c_codigo_eps="+Empresa+"&Temperatura="+Temperatura+"&ET="+ET+"&F_UsuCrea="+ano2 + "" + mes2 + "" + dia2;
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


}