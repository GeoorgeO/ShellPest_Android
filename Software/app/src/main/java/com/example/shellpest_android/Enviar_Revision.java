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

public class Enviar_Revision extends AppCompatActivity {

    ConexionInternet obj;
    public String MyIp;
    TextView txt_PNBloques,txt_PFechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_revision);

        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        txt_PNBloques=(TextView)findViewById(R.id.txt_NBloques3);
        txt_PFechas=(TextView)findViewById(R.id.txt_RFechas3);

        CargaDatos();

    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select (select count(Id_Bloque) as P from t_Revision) as Puntos,min(Fecha) as Fini,max(Fecha) as Ffin from t_Revision " , null);

        if (Renglon.moveToFirst()) {

            do {
                try {
                    String Temporal;
                    Temporal=String.valueOf(Renglon.getInt(0));
                    txt_PNBloques.setText(Temporal);

                    txt_PFechas.setText(Renglon.getString(1) + " - " + Renglon.getString(2));
                }catch (NullPointerException ecepNull){
                    Toast.makeText(this, "Valor Nulo", Toast.LENGTH_SHORT).show();
                }

            } while (Renglon.moveToNext());
        } else {
            Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
        }
    }

    public void Enviar(View view) {
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Bloque,Fruta,Floracion,N_Arboles,Observaciones,Nivel_Humedad from t_Revision ", null);

            if (Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    boolean Flor,Fruta;
                    if (Renglon.getString(2).equals("1")){
                        Fruta=true;
                    }else{
                        Fruta=false;
                    }
                    if (Renglon.getString(3).equals("1")){
                        Flor=true;
                    }else{
                        Flor=false;
                    }
                    insertWebRevision(Renglon.getString(0),Renglon.getString(1),Fruta,Flor,Renglon.getString(4),Renglon.getString(5),Renglon.getString(6));
                } while (Renglon.moveToNext());


            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }


            BD.close();
        }else{
            Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }

        CargaDatos();
    }

    private void insertWebRevision(String Fecha ,String Id_Bloque,boolean Fruta,Boolean Floracion,String N_arboles,String Observaciones  ,String Nivel_Humedad) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Fecha == null){
            Fecha="";
        }
        if(Id_Bloque== null){
            Id_Bloque="";
        }

        if(Observaciones==null){
            Observaciones="";
        }

        if(Nivel_Humedad==null){
            Nivel_Humedad="";
        }
        if(N_arboles==null){
            N_arboles="0";
        }

        String dia,mes,ano;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);


        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Revision?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&Fruta="+Fruta +"&N_arboles=" + N_arboles + "&Observaciones=" + Observaciones + "&Floracion=" + Floracion + "&Nivel_Humedad=" +  Nivel_Humedad + "" ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.Puerto+"//Control/Revision?Fecha=" + ano + "" + mes + "" + dia  + "&Id_bloque=" + Id_Bloque +"&Fruta="+Fruta + "&Floracion=" + Floracion+"&N_Arboles=" + N_arboles + "&Observaciones=" + Observaciones  + "&Nivel_Humedad=" + Nivel_Humedad + "" ;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Revision?Fecha=" + ano + "" + mes + "" + dia  + "&Id_bloque=" + Id_Bloque +"&Fruta="+Fruta + "&Floracion=" + Floracion +"&N_Arboles=" + N_arboles + "&Observaciones=" + Observaciones  + "&Nivel_Humedad=" + Nivel_Humedad + "" ;
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

                    if (jsonobject.optString("resultado").trim().equals("1")) {
                        if (EliminaRevision( Fecha , Id_Bloque, Observaciones  )){
                            RegistrosEnviados++;
                        }
                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Revision.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Revision.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Revision.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean EliminaRevision(String Fecha ,String Id_Bloque,String Observaciones ) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;

        cantidad = BD.delete("t_Revision", "Fecha='"+Fecha+"' and Id_Bloque='"+Id_Bloque+"' ", null);

        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

}