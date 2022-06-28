package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Enviar_Gasolina extends AppCompatActivity {

    private ArrayList<String> FechasConsumo, Activos, FechasIngreso;
    ArrayAdapter<String> AdaptadorConsumo, AdaptadorActivos, AdaptadorIngreso;
    public String MyIp;
    ConexionInternet obj;

    TextView txtv_registrosConsumo, txtv_registrosIngreso;
    ListView lv_FechasConsumo,  lv_Activos, lv_FechasIngreso;
    TextView MensajeToast;
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_gasolina);
        getSupportActionBar().hide();

        lv_FechasConsumo = (ListView) findViewById(R.id.lv_fechasConsumo);
        txtv_registrosConsumo = (TextView) findViewById(R.id.txtv_registrosConsumo);
        lv_Activos = (ListView) findViewById(R.id.lv_fechasFin);
        txtv_registrosIngreso = (TextView) findViewById(R.id.txtv_registrosIngreso);
        lv_FechasIngreso = (ListView) findViewById(R.id.lv_fechasIngreso);
        FechasConsumo = new ArrayList<String>();
        Activos = new ArrayList<String>();
        FechasIngreso = new ArrayList<String>();

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custome_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        MensajeToast = (TextView) layout.findViewById(R.id.MensajeToast);

        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            MensajeToast.setText("Es necesario una conexion a internet");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
            super.onBackPressed();
        }
        CargarDatos();
    }

    private void CargarDatos() {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select G.d_fechaconsumo_gas, G.Id_ActivosGas from t_Consumo_Gasolina as G", null);
        Cursor Renglon2 = BD.rawQuery("select count() from t_Consumo_Gasolina", null);
        Cursor Renglon3 = BD.rawQuery("select count() from t_Entradas_Gasolina", null);
        Cursor Renglon4 = BD.rawQuery("select d_fechaingreso_gas from t_Entradas_Gasolina", null);

            if (Renglon2.moveToFirst()){
                do{
                    txtv_registrosConsumo.setText(Renglon2.getString(0));
                }while (Renglon2.moveToNext());
            }else{
                MensajeToast.setText("No hay datos en t_Consumo_Gasolina guardados");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }

            String diacon,mescon,anocon;
            if (Renglon.moveToFirst()) {
                do {
                    String fechaCon=Renglon.getString(0);
                    anocon=fechaCon.substring(6);mescon=fechaCon.substring(2,4);diacon=fechaCon.substring(0,2);
                    FechasConsumo.add(diacon+"/"+mescon+"/"+anocon);
                    AdaptadorConsumo = new ArrayAdapter<String>(Enviar_Gasolina.this, android.R.layout.simple_list_item_1, FechasConsumo);
                    lv_FechasConsumo.setAdapter(AdaptadorConsumo);

                    Activos.add(Renglon.getString(1));
                    AdaptadorActivos = new ArrayAdapter<String>(Enviar_Gasolina.this, android.R.layout.simple_list_item_1, Activos);
                    lv_Activos.setAdapter(AdaptadorActivos);
                } while (Renglon.moveToNext());
            }else {
                MensajeToast.setText("No hay datos en t_Consumo_Gasolina guardados");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                BD.close();
            }

        if (FechasConsumo.size()>0){
            AdaptadorConsumo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FechasConsumo);
            lv_FechasConsumo.setAdapter(AdaptadorConsumo);
        }

        if (Renglon3.moveToFirst()){
            do{
                txtv_registrosIngreso.setText(Renglon3.getString(0));
            }while (Renglon3.moveToNext());
        }else{
            MensajeToast.setText("No hay datos en t_Consumo_Gasolina guardados");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

        String diaing,mesing,anoing;
        if (Renglon4.moveToFirst()) {
            do {
                String fechaIng=Renglon4.getString(0);
                anoing=fechaIng.substring(6);mesing=fechaIng.substring(2,4);diaing=fechaIng.substring(0,2);
                FechasIngreso.add(diaing+"/"+mesing+"/"+anoing);
                AdaptadorIngreso = new ArrayAdapter<String>(Enviar_Gasolina.this, android.R.layout.simple_list_item_1, FechasIngreso);
                lv_FechasIngreso.setAdapter(AdaptadorIngreso);
                Log.e("Renglon------", FechasIngreso.toString());

            } while (Renglon4.moveToNext());
        }else {
            MensajeToast.setText("No hay datos en t_Consumo_Gasolina guardados");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
            BD.close();
        }

        if (FechasIngreso.size()>0){
            AdaptadorIngreso = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FechasIngreso);
            lv_FechasIngreso.setAdapter(AdaptadorIngreso);
        }
    }

    public void EnviarGas(View view){
        if (obj.isConnected()) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select d_fechacrea_gas, c_folio_gas, d_fechaconsumo_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas,c_codigo_emp, c_codigo_act, v_tipo_gas, v_cantutilizada_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas from t_Consumo_Gasolina  ", null);

            if (Renglon.moveToFirst()) {
                do {
                    insertWebGasolinaConsumo(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10),Renglon.getString(11),Renglon.getString(12),Renglon.getString(13),Renglon.getString(14));
                } while (Renglon.moveToNext());
            } else {
                MensajeToast.setText("No hay datos guardados para enviar");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }

            Cursor Renglon2 = BD.rawQuery("select d_fechacrea_gas, c_folio_gas, d_fechaingreso_gas, c_codigo_eps, Id_Huerta, c_codigo_emp, v_tipo_gas, v_cantingreso_gas,v_observaciones_gas from t_Entradas_Gasolina", null);

            if (Renglon2.moveToFirst()) {
                do {
                    insertWebGasolinaIngreso(Renglon2.getString(0),Renglon2.getString(1),Renglon2.getString(2),Renglon2.getString(3),Renglon2.getString(4),Renglon2.getString(5),Renglon2.getString(6),Renglon2.getString(7),Renglon2.getString(8));
                } while (Renglon2.moveToNext());
            } else {
                MensajeToast.setText("No hay datos guardados para enviar");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }


            BD.close();
        }else{
            MensajeToast.setText("Sin conexion a internet");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
        CargarDatos();
    }

    private void insertWebGasolinaConsumo(String d_fechacrea_gas, String c_folio_gas, String d_fechaconsumo_gas, String c_codigo_eps, String Id_Huerta, String v_Bloques_gas, String Id_ActivosGas, String c_codigo_emp, String c_codigo_act, String v_tipo_gas, String v_cantutilizada_gas, String v_horometro_gas, String v_kminicial_gas, String v_kmfinal_gas, String v_observaciones_gas){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (d_fechacrea_gas == null){
            d_fechacrea_gas = "";
        }if (c_folio_gas == null){
            c_folio_gas="";
        }if (d_fechaconsumo_gas == null){
            d_fechaconsumo_gas="";
        }if (c_codigo_eps == null){
            c_codigo_eps="";
        }if (Id_Huerta == null){
            Id_Huerta="";
        }if (v_Bloques_gas == null){
            v_Bloques_gas ="";
        }if (Id_ActivosGas == null){
            Id_ActivosGas="";
        }if (c_codigo_emp == null){
            c_codigo_emp="";
        }if (c_codigo_act == null){
            c_codigo_act="";
        }if (v_tipo_gas == null){
            v_tipo_gas="";
        }if (v_cantutilizada_gas == null){
            v_cantutilizada_gas="0";
        }if (v_horometro_gas == "null"){
            v_horometro_gas="0";
        }if (v_kminicial_gas==null){
            v_kminicial_gas="0";
        }if (v_kmfinal_gas == null){
            v_kmfinal_gas="0";
        }if (v_observaciones_gas == null){
            v_observaciones_gas="";
        }

        String diaCrea,mesCrea,anoCrea,diaCon, mesCon,anoCon;
        anoCrea=d_fechacrea_gas.substring(6);
        mesCrea=d_fechacrea_gas.substring(2,4);
        diaCrea=d_fechacrea_gas.substring(0,2);
        anoCon=d_fechaconsumo_gas.substring(6);
        mesCon=d_fechaconsumo_gas.substring(2,4);
        diaCon=d_fechaconsumo_gas.substring(0,2);

        Obtener_Ip();
        String Liga = "";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/Gasolina?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechaconsumo_gas="+ anoCon + "" + mesCon + "" + diaCon + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&v_Bloques_gas=" + v_Bloques_gas + "&Id_ActivosGas=" + Id_ActivosGas + "&c_codigo_emp=" + c_codigo_emp + "&c_codigo_act=" + c_codigo_act + "&v_tipo_gas=" + v_tipo_gas + "&v_cantutilizada_gas=" + v_cantutilizada_gas + "&v_horometro_gas=" + v_horometro_gas + "&v_kminicial_gas=" + v_kminicial_gas + "&v_kmfinal_gas=" + v_kmfinal_gas + "&v_observaciones_gas=" + v_observaciones_gas;
            EliminaGasolina( d_fechacrea_gas, c_folio_gas, d_fechaconsumo_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas,c_codigo_emp, c_codigo_act, v_tipo_gas, v_cantutilizada_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas);
            CargarDatos();
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/Gasolina?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechaconsumo_gas="+ anoCon + "" + mesCon + "" + diaCon + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&v_Bloques_gas=" + v_Bloques_gas + "&Id_ActivosGas=" + Id_ActivosGas + "&c_codigo_emp=" + c_codigo_emp + "&c_codigo_act=" + c_codigo_act + "&v_tipo_gas=" + v_tipo_gas + "&v_cantutilizada_gas=" + v_cantutilizada_gas + "&v_horometro_gas=" + v_horometro_gas + "&v_kminicial_gas=" + v_kminicial_gas + "&v_kmfinal_gas=" + v_kmfinal_gas + "&v_observaciones_gas=" + v_observaciones_gas;
                EliminaGasolina(d_fechacrea_gas, c_folio_gas, d_fechaconsumo_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas,c_codigo_emp, c_codigo_act, v_tipo_gas, v_cantutilizada_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas);
                CargarDatos();
            }else{
                Liga = "http://177.241.250.117:8090//Control/Gasolina?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechaconsumo_gas="+ anoCon + "" + mesCon + "" + diaCon + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&v_Bloques_gas=" + v_Bloques_gas + "&Id_ActivosGas=" + Id_ActivosGas + "&c_codigo_emp=" + c_codigo_emp + "&c_codigo_act=" + c_codigo_act + "&v_tipo_gas="+ v_tipo_gas + "&v_cantutilizada_gas=" + v_cantutilizada_gas  + "&v_horometro_gas=" + v_horometro_gas + "&v_kminicial_gas=" + v_kminicial_gas + "&v_kmfinal_gas=" + v_kmfinal_gas + "&v_observaciones_gas=" + v_observaciones_gas;
                EliminaGasolina( d_fechacrea_gas, c_folio_gas, d_fechaconsumo_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas,c_codigo_emp, c_codigo_act, v_tipo_gas, v_cantutilizada_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas);
                CargarDatos();
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
                        MensajeToast.setText("Registros Enviados con éxito!");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        if (EliminaGasolina( d_fechacrea_gas, c_folio_gas, d_fechaconsumo_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas,c_codigo_emp, c_codigo_act, v_tipo_gas, v_cantutilizada_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            MensajeToast.setText("Fallo la conexion al servidor [OPENPEDINS]");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            MensajeToast.setText("Fallo la conexion al servidor [OPENPEDINS]");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            MensajeToast.setText("Fallo la conexion al servidor [OPENPEDINS]");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }


    private void insertWebGasolinaIngreso(String d_fechacrea_gas, String c_folio_gas, String d_fechaingreso_gas, String c_codigo_eps, String Id_Huerta, String c_codigo_emp, String v_tipo_gas, String v_cantingreso_gas, String v_observaciones_gas){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (d_fechacrea_gas == null){
            d_fechacrea_gas = "";
        }if (c_folio_gas == null){
            c_folio_gas="";
        }if (d_fechaingreso_gas == null){
            d_fechaingreso_gas="";
        }if (c_codigo_eps == null){
            c_codigo_eps="";
        }if (Id_Huerta == null){
            Id_Huerta="";
        }if (c_codigo_emp == null){
            c_codigo_emp="";
        }if (v_tipo_gas == null){
            v_tipo_gas="";
        }if (v_cantingreso_gas == null){
            v_cantingreso_gas="0";
        }if (v_observaciones_gas == null){
            v_observaciones_gas="";
        }

        String diaCrea,mesCrea,anoCrea,diaIng, mesIng,anoIng;
        anoCrea=d_fechacrea_gas.substring(6);
        mesCrea=d_fechacrea_gas.substring(2,4);
        diaCrea=d_fechacrea_gas.substring(0,2);
        anoIng=d_fechaingreso_gas.substring(6);
        mesIng=d_fechaingreso_gas.substring(2,4);
        diaIng=d_fechaingreso_gas.substring(0,2);

        Obtener_Ip();
        String Liga = "";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/GasolinaIngreso?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechaingreso_gas="+ anoIng + "" + mesIng + "" + diaIng + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&c_codigo_emp=" + c_codigo_emp + "&v_tipo_gas="+ v_tipo_gas + "&v_cantingreso_gas=" + v_cantingreso_gas + "&v_observaciones_gas=" + v_observaciones_gas;
            EliminaGasolinaIngreso( d_fechacrea_gas,c_folio_gas,d_fechaingreso_gas,c_codigo_eps,Id_Huerta,c_codigo_emp,v_tipo_gas,v_cantingreso_gas,v_observaciones_gas);
            CargarDatos();
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/GasolinaIngreso?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechaingreso_gas="+ anoIng + "" + mesIng + "" + diaIng + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&c_codigo_emp=" + c_codigo_emp + "&v_tipo_gas="+ v_tipo_gas + "&v_cantingreso_gas=" + v_cantingreso_gas + "&v_observaciones_gas=" + v_observaciones_gas;
                EliminaGasolinaIngreso(d_fechacrea_gas,c_folio_gas,d_fechaingreso_gas,c_codigo_eps,Id_Huerta,c_codigo_emp,v_tipo_gas,v_cantingreso_gas,v_observaciones_gas);
                CargarDatos();
            }else{
                Liga = "http://177.241.250.117:8090//Control/GasolinaIngreso?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechaingreso_gas="+ anoIng + "" + mesIng + "" + diaIng + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&c_codigo_emp=" + c_codigo_emp + "&v_tipo_gas="+ v_tipo_gas + "&v_cantingreso_gas=" + v_cantingreso_gas + "&v_observaciones_gas=" + v_observaciones_gas;
                EliminaGasolinaIngreso( d_fechacrea_gas,c_folio_gas,d_fechaingreso_gas,c_codigo_eps,Id_Huerta,c_codigo_emp,v_tipo_gas,v_cantingreso_gas,v_observaciones_gas);
                CargarDatos();
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
                        MensajeToast.setText("Registros Enviados con éxito!");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        if (EliminaGasolinaIngreso( d_fechacrea_gas,c_folio_gas,d_fechaingreso_gas,c_codigo_eps,Id_Huerta,c_codigo_emp,v_tipo_gas,v_cantingreso_gas,v_observaciones_gas)){
                            RegistrosEnviados++;
                        }
                    }
                }
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            MensajeToast.setText("Fallo la conexion al servidor [OPENPEDINS]");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            MensajeToast.setText("Fallo la conexion al servidor [OPENPEDINS]");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            MensajeToast.setText("Fallo la conexion al servidor [OPENPEDINS]");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;
    }

    private boolean EliminaGasolina(String d_fechacrea_gas, String c_folio_gas, String d_fechaconsumo_gas, String c_codigo_eps, String Id_Huerta, String v_Bloques_gas, String Id_ActivosGas, String c_codigo_emp, String c_codigo_act, String v_tipo_gas, String v_cantutilizada_gas, String v_horometro_gas, String v_kminicial_gas, String v_kmfinal_gas, String v_observaciones_gas){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;
        cantidad = BD.delete("t_Consumo_Gasolina", "d_fechacrea_gas='"+d_fechacrea_gas+"' and c_folio_gas='"+c_folio_gas+"' and d_fechaconsumo_gas='"+d_fechaconsumo_gas+"' and c_codigo_eps='"+c_codigo_eps+"' and Id_Huerta='"+Id_Huerta+"' and v_Bloques_gas='"+v_Bloques_gas+"' and Id_ActivosGas='"+Id_ActivosGas+"' and c_codigo_emp='"+c_codigo_emp+"' and c_codigo_act='"+c_codigo_act+"' and v_tipo_gas='"+v_tipo_gas+"' and v_cantutilizada_gas='"+v_cantutilizada_gas+"' and v_horometro_gas='"+v_horometro_gas+"' and v_kminicial_gas='"+v_kminicial_gas+"' and v_kmfinal_gas='"+v_kmfinal_gas+"' and v_observaciones_gas='"+v_observaciones_gas+"' ", null);
        CargarDatos();

        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminaGasolinaIngreso(String d_fechacrea_gas, String c_folio_gas, String d_fechaingreso_gas, String c_codigo_eps, String Id_Huerta, String c_codigo_emp, String v_tipo_gas, String v_cantingreso_gas, String v_observaciones_gas){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;
        cantidad = BD.delete("t_Entradas_Gasolina", "d_fechacrea_gas='"+d_fechacrea_gas+"' and c_folio_gas='"+c_folio_gas+"' and d_fechaingreso_gas='"+d_fechaingreso_gas+"' and c_codigo_eps='"+c_codigo_eps+"' and Id_Huerta='"+Id_Huerta+"' and c_codigo_emp='"+c_codigo_emp+"' and v_tipo_gas='"+v_tipo_gas+"' and v_cantingreso_gas='"+v_cantingreso_gas+"' and v_observaciones_gas='"+v_observaciones_gas+"' ", null);
        CargarDatos();

        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

}