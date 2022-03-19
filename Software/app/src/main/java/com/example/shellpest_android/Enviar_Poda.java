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

public class Enviar_Poda extends AppCompatActivity {
    ConexionInternet obj;
    public String MyIp;
    TextView txt_PNBloques,txt_PFechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_poda);

        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        txt_PNBloques=(TextView)findViewById(R.id.txt_NBloques);
        txt_PFechas=(TextView)findViewById(R.id.txt_RFechas);

        CargaDatos();
    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select (select count(Id_bloque) as P from t_Podas) as Puntos,min(Fecha) as Fini,max(Fecha) as Ffin from t_Podas " , null);

        if (Renglon.moveToFirst()) {

            do {
                txt_PNBloques.setText(Renglon.getString(0));

                txt_PFechas.setText(Renglon.getString(1)+" - "+Renglon.getString(2));
            } while (Renglon.moveToNext());
        } else {
            Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
        }
    }

    public void Enviar(View view) {
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Bloque,c_codigo_eps,N_arboles,Observaciones,Id_Usuario,F_Creacion from t_Podas ", null);

            if (Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    insertWebPoda(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6));
                } while (Renglon.moveToNext());


            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

            }


            Cursor Renglon2 = BD.rawQuery("select Fecha,Id_Bloque,c_codigo_eps,Actividad from t_PodasDet ", null);

            if (Renglon2.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon2.getString(7), Toast.LENGTH_SHORT).show();
                    insertWebPodaDet(Renglon2.getString(0),Renglon2.getString(1),Renglon2.getString(2),Renglon2.getString(3));
                } while (Renglon2.moveToNext());
            } else {
                //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
            }





            BD.close();
        }else{
            Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }

        CargaDatos();
    }

    private void insertWebPoda(String Fecha ,String Id_Bloque,String c_Codigo_eps,String N_arboles,String Observaciones ,String Id_Usuario_Crea ,String F_Usuario_Crea) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(Fecha == null){
            Fecha="";
        }
        if(Id_Bloque== null){
            Id_Bloque="";
        }
        if(c_Codigo_eps==null){
            c_Codigo_eps="";
        }
        if(Observaciones==null){
            Observaciones="";
        }

        String dia,mes,ano,dia2,mes2,ano2;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);
        ano2=F_Usuario_Crea.substring(6);
        mes2=F_Usuario_Crea.substring(3, 5);
        dia2=F_Usuario_Crea.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/Podas?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&N_arboles=" + N_arboles + "&Observaciones=" + Observaciones + "&Id_Usuario_Crea=" + Id_Usuario_Crea + "&F_Usuario_Crea=" +  ano2 + "" + mes2 + "" + dia2;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/Podas?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&N_arboles=" + N_arboles + "&Observaciones=" + Observaciones + "&Id_Usuario_Crea=" + Id_Usuario_Crea + "&F_Usuario_Crea=" + ano2 + "" + mes2 + "" + dia2;
            }else{
                Liga = "http://177.241.250.117:8090//Control/Podas?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&N_arboles=" + N_arboles + "&Observaciones=" + Observaciones + "&Id_Usuario_Crea=" + Id_Usuario_Crea + "&F_Usuario_Crea=" + ano2 + "" + mes2 + "" + dia2;
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


                    if (jsonobject.optString("resultado").equals("True")) {

                        if (EliminaPoda( Fecha , Id_Bloque, c_Codigo_eps, N_arboles, Observaciones , Id_Usuario_Crea , F_Usuario_Crea)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Poda.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Poda.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Poda.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }


    private void insertWebPodaDet(String Fecha ,String Id_Bloque,String c_Codigo_eps,String Actividad) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(Fecha == null){
            Fecha="";
        }
        if(Id_Bloque== null){
            Id_Bloque="";
        }
        if(c_Codigo_eps==null){
            c_Codigo_eps="";
        }

        if(Actividad==null){
            Actividad="";
        }

        String dia,mes,ano;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/PodasDet?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&Actividad=" + Actividad ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/PodasDet?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&Actividad=" + Actividad ;
            }else{
                Liga = "http://177.241.250.117:8090//Control/PodasDet?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&Actividad=" + Actividad ;
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


                    if (jsonobject.optString("resultado").equals("True")) {

                        if (EliminaPodaDet( Fecha , Id_Bloque, c_Codigo_eps, Actividad)){
                            RegistrosEnviados++;
                        }


                    }
                }


            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Poda.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Poda.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Poda.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean EliminaPoda(String Fecha ,String Id_Bloque,String c_Codigo_eps,String N_arboles,String Observaciones ,String Id_Usuario_Crea ,String F_Usuario_Crea) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;

            cantidad = BD.delete("t_Podas", "Fecha='"+Fecha+"' and Id_Bloque='"+Id_Bloque+"' and c_Codigo_eps='"+c_Codigo_eps+"' and N_arboles='"+N_arboles+"' and  Observaciones='" + Observaciones + "' ", null);


        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminaPodaDet(String Fecha ,String Id_Bloque,String c_Codigo_eps,String Actividad) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;

        cantidad = BD.delete("t_PodasDet", "Fecha='"+Fecha+"' and Id_Bloque='"+Id_Bloque+"' and c_Codigo_eps='"+c_Codigo_eps+"' and Actividad='"+Actividad+"'  ", null);


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