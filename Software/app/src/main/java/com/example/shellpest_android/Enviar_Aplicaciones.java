package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

public class Enviar_Aplicaciones extends AppCompatActivity {

    public String MyIp,CodAplicacion;
    ConexionInternet obj;

    TextView text_NAplicaciones,text_Fechas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_aplicaciones);

        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

        text_NAplicaciones=(TextView)findViewById(R.id.text_NAplicaciones);
        text_Fechas=(TextView)findViewById(R.id.text_Fechas);

        CargaDatos();

    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select count(Ap.Id_Aplicacion) as Aplicaciones from t_Aplicaciones as Ap  where Ap.Enviado='0' " , null);

        if (Renglon.moveToFirst()) {

            do {
                text_NAplicaciones.setText(Renglon.getString(0));

            } while (Renglon.moveToNext());


        } else {
            Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();

        }

        Cursor Renglon10 = BD.rawQuery("select min(ApD.Fecha) as Fini,max(ApD.Fecha)  as Ffin  from t_Aplicaciones_Det as ApD  where ifnull(ApD.Enviado,'0')='0' " , null);

        if (Renglon10.moveToFirst()) {

            do {

                text_Fechas.setText(Renglon10.getString(0)+" - "+Renglon10.getString(1));
            } while (Renglon10.moveToNext());


        } else {
            Toast.makeText(this, "No hay datos guardados para enviar #2", Toast.LENGTH_SHORT).show();

        }
        BD.close();
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

    public void Enviar(View view) {
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select A.Id_Aplicacion,A.Id_Huerta,A.Observaciones,A.Id_TipoAplicacion,A.Id_Presentacion,A.Id_Usuario,A.F_Creacion,A.c_codigo_eps,(select min(AD.Fecha) from t_Aplicaciones_Det as AD where AD.Id_Aplicacion=A.Id_Aplicacion and ifnull(AD.Enviado,'0')='0') as Fecha,A.Centro_Costos,A.Unidades_aplicadas from t_Aplicaciones as A where A.Enviado='0'", null);

            if (Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    insertWebEncabezado(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(8),Renglon.getString(7),Renglon.getString(9),Renglon.getString(10));
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

    private void insertWebEncabezado(String Id_Aplicacion,String Id_Huerta,String Observaciones,String Id_TipoAplicacion,String Id_Presentacion,String Id_Usuario,String F_Creacion,String Fecha,String c_codigo_eps,String CC,String Unidades_aplicadas) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(Observaciones == null){
            Observaciones="";
        }
        if(Id_Huerta== null){
            Id_Huerta="";
        }

        if(Id_TipoAplicacion==null){
            Id_TipoAplicacion="";
        }

        if(Unidades_aplicadas==null){
            Unidades_aplicadas="";
        }

        if(Id_Usuario==null){
            Id_Usuario="";
        }

        if(Id_Presentacion==null){
            Id_Presentacion="";
        }
        if(F_Creacion==null){
            F_Creacion="";
        }

        String dia,mes,ano,ano2;
        ano=F_Creacion.substring(6);
        mes=F_Creacion.substring(3, 5);
        dia=F_Creacion.substring(0, 2);
        ano2=Fecha.substring(6);
        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Aplicaciones?Id_Aplicacion="  + Id_Aplicacion + "&Id_Huerta=" + Id_Huerta + "&Observaciones=" + Observaciones + "&Id_TipoAplicacion=" + Id_TipoAplicacion + "&Id_Presentacion=" + Id_Presentacion + "&Id_Usuario=" + Id_Usuario + "&F_Creacion=" + ano + "" + mes + "" + dia + "&Anio="+ano2 +"&c_codigo_eps="+c_codigo_eps+"&CC="+CC +"&Unidades_aplicadas="+Unidades_aplicadas ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.Puerto+"//Control/Aplicaciones?Id_Aplicacion="  + Id_Aplicacion + "&Id_Huerta=" + Id_Huerta + "&Observaciones=" + Observaciones + "&Id_TipoAplicacion=" + Id_TipoAplicacion + "&Id_Presentacion=" + Id_Presentacion + "&Id_Usuario=" + Id_Usuario + "&F_Creacion=" + ano + "" + mes + "" + dia + "&Anio="+ano2+"&c_codigo_eps="+c_codigo_eps+"&CC="+CC +"&Unidades_aplicadas="+Unidades_aplicadas ;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Aplicaciones?Id_Aplicacion="  + Id_Aplicacion + "&Id_Huerta=" + Id_Huerta + "&Observaciones=" + Observaciones + "&Id_TipoAplicacion=" + Id_TipoAplicacion + "&Id_Presentacion=" + Id_Presentacion + "&Id_Usuario=" + Id_Usuario + "&F_Creacion=" + ano + "" + mes + "" + dia + "&Anio="+ano2+"&c_codigo_eps="+c_codigo_eps+"&CC="+CC +"&Unidades_aplicadas="+Unidades_aplicadas ;
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

                    CodAplicacion=jsonobject.optString("Mensaje");
                    if (!jsonobject.optString("Mensaje").trim().equals("0")) {

                        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
                        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                        Cursor Renglon2 = BD.rawQuery("select Fecha,c_codigo_pro,Dosis,Id_Usuario,F_Creacion,c_codigo_eps from t_Aplicaciones_Det where Id_Aplicacion='"+Id_Aplicacion+"' and c_codigo_eps='"+c_codigo_eps+"'", null);

                        if (Renglon2.moveToFirst()) {

                            do {
                                //Toast.makeText(this, Renglon2.getString(7), Toast.LENGTH_SHORT).show();
                                insertWebDetalle(CodAplicacion,Renglon2.getString(0),Renglon2.getString(1),Renglon2.getString(2),Renglon2.getString(3),Renglon2.getString(4),Renglon2.getString(5));
                            } while (Renglon2.moveToNext());
                            if (EliminadeAplicacionEncabezado(Id_Aplicacion,c_codigo_eps)){
                                RegistrosEnviados++;
                            }
                        } else {
                            //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        if (jsonobject.optString("Mensaje").indexOf("Violation of PRIMARY KEY")>=0){
                            Toast.makeText(Enviar_Aplicaciones.this, "Ya se envio ese punto en la fecha indicada", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Enviar_Aplicaciones.this, "Ocurrio error al intentar guardar, notifique al administrador del sistema.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Aplicaciones.this, "Fallo la conexion al servidor [MALFORMEDURLCAB]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Aplicaciones.this, "Fallo la conexion al servidor [IOCAB]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Aplicaciones.this, "Fallo la conexion al servidor [JSONCAB]", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertWebDetalle(String Id_Aplicacion,String Fecha,String c_codigo_pro,String Dosis,String Id_Usuario,String F_Creacion,String c_codigo_eps) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Network Clase= new Network();

        if(c_codigo_pro== null){
            c_codigo_pro="";
        }
        if(Dosis==null){
            Dosis="";
        }

        if(Id_Usuario==null){
            Id_Usuario="";
        }
        if(F_Creacion==null){
            F_Creacion="";
        }

        String dia,mes,ano,dia2,mes2,ano2;
        ano=Fecha.substring(6);
        mes=Fecha.substring(3, 5);
        dia=Fecha.substring(0, 2);

        ano2=F_Creacion.substring(6);
        mes2=F_Creacion.substring(3, 5);
        dia2=F_Creacion.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Aplicaciones_Det?Id_Aplicacion=" +Id_Aplicacion+ "&Fecha="  + ano + "" + mes + "" + dia +  "&c_codigo_pro=" + c_codigo_pro + "&Dosis=" + Dosis +  "&Id_Usuario=" + Id_Usuario + "&F_Creacion="  + ano2 + "" + mes2 + "" + dia2 +"&c_codigo_eps="+c_codigo_eps ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = Clase.IpLocal+Clase.Puerto+"//Control/Aplicaciones_Det?Id_Aplicacion=" +Id_Aplicacion+ "&Fecha="  + ano + "" + mes + "" + dia +  "&c_codigo_pro=" + c_codigo_pro + "&Dosis=" + Dosis +  "&Id_Usuario=" + Id_Usuario + "&F_Creacion="  + ano2 + "" + mes2 + "" + dia2 +"&c_codigo_eps="+c_codigo_eps ;
            }else{
                Liga = Clase.IpoDNS+Clase.Puerto+"//Control/Aplicaciones_Det?Id_Aplicacion=" +Id_Aplicacion+ "&Fecha="  + ano + "" + mes + "" + dia +  "&c_codigo_pro=" + c_codigo_pro + "&Dosis=" + Dosis +  "&Id_Usuario=" + Id_Usuario + "&F_Creacion="  + ano2 + "" + mes2 + "" + dia2 +"&c_codigo_eps="+c_codigo_eps ;
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
                        if (EliminadeAplicacionDetalle(Id_Aplicacion,Fecha,c_codigo_pro,c_codigo_eps)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Aplicaciones.this, "Fallo la conexion al servidor [MALFORMEDDET]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Aplicaciones.this, "Fallo la conexion al servidor [IODET]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Aplicaciones.this, "Fallo la conexion al servidor [JSONDET]", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean EliminadeAplicacionEncabezado(String Id_Aplicacion,String c_codigo_eps) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
       /* int cantidad = BD.delete("t_Aplicaciones", "Id_Aplicacion='"+Id_Aplicacion+"'", null);
        BD.close();*/
        ContentValues registro = new ContentValues();

        registro.put("Enviado","1");

        int cantidad=BD.update("t_Aplicaciones",registro,"Id_Aplicacion='"+Id_Aplicacion+"' and c_codigo_eps='"+c_codigo_eps+"'",null);

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminadeAplicacionDetalle(String Id_Aplicacion, String Fecha, String c_codigo_pro,String c_codigo_eps) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        /*int cantidad = BD.delete("t_Aplicaciones_Det", "Id_Aplicacion='"+Id_Aplicacion+"' and Fecha='"+Fecha+"' and c_codigo_pro='"+c_codigo_pro+"'", null);
        BD.close();*/
        ContentValues registro = new ContentValues();

        registro.put("Enviado","1");

        int cantidad=BD.update("t_Aplicaciones_Det",registro,"Id_Aplicacion='"+Id_Aplicacion+"' and Fecha='"+Fecha+"' and c_codigo_pro='"+c_codigo_pro+"' and c_codigo_eps='"+c_codigo_eps+"'" ,null);

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }


    }

}