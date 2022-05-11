package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.SplittableRandom;

public class Enviar_Gasolina extends AppCompatActivity {

    private ArrayList<String> FechasIni, FechasFin;
    ArrayAdapter<String> AdaptadorIni, AdaptadorFin;
    public String MyIp, CodAplicacion;
    ConexionInternet obj;

    TextView txtv_Activos;
    ListView lv_FechasIni,  lv_FechasFin;

    /*
    Fecha de creacion
    Fecha de envio
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_gasolina);
        getSupportActionBar().hide();

        lv_FechasIni = (ListView) findViewById(R.id.lv_fechasIni);
        txtv_Activos = (TextView) findViewById(R.id.txtv_Activos);
        lv_FechasFin = (ListView) findViewById(R.id.lv_fechasFin);
        FechasIni = new ArrayList<String>();
        FechasFin = new ArrayList<String>();

        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        CargarDatos();
    }

    private void CargarDatos() {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select G.Id_ActivosGas, G.d_fechainicio_gas, G.d_fechafin_gas from t_Consumo_Gasolina as G", null);

            if (Renglon.moveToFirst()) {
                Log.e("move", String.valueOf(Renglon.getPosition()));
                do {
                    txtv_Activos.setText(Renglon.getString(0));
                    //txtv_FechasIni.setText(Renglon.getString(1)+" - "+Renglon.getString(2));
                    //txtv_FechasFin.setText(Renglon.getString(3)+" - "+Renglon.getString(4));
                    FechasIni.add(Renglon.getString(1));
                    AdaptadorIni = new ArrayAdapter<String>(Enviar_Gasolina.this, android.R.layout.simple_list_item_1, FechasIni);
                    lv_FechasIni.setAdapter(AdaptadorIni);
                    Log.e("Renglon------",FechasIni.toString());
                    FechasFin.add(Renglon.getString(2));
                    AdaptadorFin = new ArrayAdapter<String>(Enviar_Gasolina.this, android.R.layout.simple_list_item_1, FechasFin);
                    lv_FechasFin.setAdapter(AdaptadorFin);
                    Log.e("Renglon------",FechasFin.toString());
                } while (Renglon.moveToNext());
            }else {
                Toast ToastMensaje = Toast.makeText(this,"No hay datos en t_Consumo_Gasolina guardados",Toast.LENGTH_SHORT);
                View toastView = ToastMensaje.getView();
                toastView.setBackgroundResource(R.drawable.spinner_style);
                toastView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ToastMensaje.show();BD.close();
            }

        if (FechasIni.size()>0){
            AdaptadorIni = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,FechasIni);
            lv_FechasIni.setAdapter(AdaptadorIni);
            Log.e("Renglon------",FechasFin.toString());
        }
    }

    public void EnviarGas(View view){
        if (obj.isConnected()) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select Fecha,Id_Bloque,c_codigo_eps,BICO,Cajas_Cosecha,Cajas_Desecho,Cajas_Pepena,Cajas_RDiaria,Id_Usuario,F_Creacion from t_Cosecha ", null);

            if (Renglon.moveToFirst()) {
                do {
                    //insertWebGasolina(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10), Renglon.getString(11),Renglon.getString(12),Renglon.getString(13),Renglon.getString(14));
                } while (Renglon.moveToNext());
            } else {
                Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
            }
            BD.close();
        }else{
            Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }
        CargarDatos();
    }

    private void insertWebGasolina(String c_folio_gas, String d_fechainicio_gas, String d_fechafin_gas, String c_codigo_eps, String Id_Huerta, String Id_ActivosGas, String c_codigo_emp, String c_codigo_act, String v_cantingreso_gas, String v_cantsaldo_gas, String v_tipo_gas, String v_horometro_gas, String v_kminicial_gas, String v_kmfinal_gas, String v_observaciones_gas){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (c_folio_gas == null){
            c_folio_gas="";
        }if (d_fechainicio_gas == null){
            d_fechainicio_gas="";
        }if (d_fechafin_gas == null){
            d_fechafin_gas="";
        }if (c_codigo_eps == null){
            c_codigo_eps="";
        }if (Id_Huerta == null){
            Id_Huerta="";
        }if (Id_ActivosGas == null){
            Id_ActivosGas="";
        }if (c_codigo_emp == null){
            c_codigo_emp="";
        }if (c_codigo_act == null){
            c_codigo_act="";
        }if (v_cantingreso_gas == null){
            v_cantingreso_gas="0";
        }if (v_cantsaldo_gas == null){
            v_cantsaldo_gas="0";
        }if (v_tipo_gas == null){
            v_tipo_gas="";
        }if (v_horometro_gas == "null"){
            v_horometro_gas="0";
        }if (v_kminicial_gas==null){
            v_kminicial_gas="0";
        }if (v_kmfinal_gas == null){
            v_kmfinal_gas="0";
        }if (v_observaciones_gas == null){
            v_observaciones_gas="";
        }

        String diaFI, mesFI,anoFI,diaFF,mesFF,anoFF;
        anoFI=d_fechainicio_gas.substring(6);
        mesFI=d_fechainicio_gas.substring(2,4);
        diaFI=d_fechainicio_gas.substring(0,2);
        anoFF=d_fechafin_gas.substring(6);
        mesFF=d_fechafin_gas.substring(2,4);
        diaFF=d_fechafin_gas.substring(0,2);

        Obtener_Ip();
        String Liga = "";
        if(MyIp.equals("0.0.0.0")){
            //Liga = "http://177.241.250.117:8090//      ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                //Liga = "http://192.168.3.254:8090//      ;
            }else{
                //Liga = "http://177.241.250.117:8090//         ;
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
                    if (jsonobject.optString("resultado").equals("True")) {

                    }
                }
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Gasolina.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Gasolina.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Gasolina.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
        }
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
        //Toast.makeText(this, MyIp, Toast.LENGTH_SHORT).show();
    }

}