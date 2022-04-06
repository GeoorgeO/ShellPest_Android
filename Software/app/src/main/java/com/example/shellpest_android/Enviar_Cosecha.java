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

public class Enviar_Cosecha extends AppCompatActivity {

    ConexionInternet obj;
    public String MyIp;
    TextView txt_NBloques2,txt_RFechas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_cosecha);

        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        txt_NBloques2=(TextView)findViewById(R.id.txt_NBloques2);
        txt_RFechas2=(TextView)findViewById(R.id.txt_RFechas2);

        CargaDatos();
    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select (select count(Id_bloque) as P from t_Cosecha) as Puntos,min(Fecha) as Fini,max(Fecha) as Ffin from t_Cosecha " , null);

        if (Renglon.moveToFirst()) {

            do {
                txt_NBloques2.setText(Renglon.getString(0));

                txt_RFechas2.setText(Renglon.getString(1)+" - "+Renglon.getString(2));
            } while (Renglon.moveToNext());
        } else {
            Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
        }
    }

    public void Enviar(View view) {
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Bloque,c_codigo_eps,BICO,Cajas_Cosecha,Cajas_Desecho,Cajas_Pepena,Cajas_RDiaria,Id_Usuario,F_Creacion from t_Cosecha ", null);

            if (Renglon.moveToFirst()) {
                do {
                    //Toast.makeText(this, Renglon.getString(6), Toast.LENGTH_SHORT).show();
                    insertWebCosecha(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9));
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

    private void insertWebCosecha(String Fecha ,String Id_Bloque,String c_Codigo_eps,String BICO,String Cajas_Cosecha,String Cajas_Desecho,String Cajas_Pepena,String Cajas_RDiaria ,String Id_Usuario_Crea ,String F_Usuario_Crea) {

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
        if(BICO==null){
            BICO="";
        }
        if(Cajas_Cosecha==null){
            Cajas_Cosecha="0";
        }
        if(Cajas_Desecho==null){
            Cajas_Desecho="0";
        }
        if(Cajas_Pepena==null){
            Cajas_Pepena="0";
        }
        if(Cajas_RDiaria==null){
            Cajas_RDiaria="0";
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
            Liga = "http://177.241.250.117:8090//Control/Cosecha?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&BICO=" + BICO + "&Cajas_Cosecha=" + Cajas_Cosecha + "&Cajas_Desecho=" + Cajas_Desecho + "&Cajas_Pepena=" + Cajas_Pepena + "&Cajas_RDiaria=" + Cajas_RDiaria+ "&Id_Usuario=" + Id_Usuario_Crea + "&F_Fecha_Crea=" +  ano2 + "" + mes2 + "" + dia2;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/Cosecha?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&BICO=" + BICO + "&Cajas_Cosecha=" + Cajas_Cosecha + "&Cajas_Desecho=" + Cajas_Desecho + "&Cajas_Pepena=" + Cajas_Pepena + "&Cajas_RDiaria=" + Cajas_RDiaria+ "&Id_Usuario=" + Id_Usuario_Crea + "&F_Fecha_Crea=" + ano2 + "" + mes2 + "" + dia2;
            }else{
                Liga = "http://177.241.250.117:8090//Control/Cosecha?Fecha=" + ano + "" + mes + "" + dia  + "&Id_Bloque=" + Id_Bloque +"&c_codigo_eps="+c_Codigo_eps +"&BICO=" + BICO + "&Cajas_Cosecha=" + Cajas_Cosecha + "&Cajas_Desecho=" + Cajas_Desecho + "&Cajas_Pepena=" + Cajas_Pepena + "&Cajas_RDiaria=" + Cajas_RDiaria+ "&Id_Usuario=" + Id_Usuario_Crea + "&F_Fecha_Crea=" + ano2 + "" + mes2 + "" + dia2;
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

                        if (EliminaCosecha( Fecha , Id_Bloque, c_Codigo_eps, BICO)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Cosecha.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Cosecha.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Cosecha.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

    private boolean EliminaCosecha(String Fecha ,String Id_Bloque,String c_Codigo_eps,String BICO) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;

        cantidad = BD.delete("t_Cosecha", "Fecha='"+Fecha+"' and Id_Bloque='"+Id_Bloque+"' and c_Codigo_eps='"+c_Codigo_eps+"' and BICO='"+BICO+"' ", null);

        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

}