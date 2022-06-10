package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
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
    TextView MensajeToast;
    View layout;

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
        Cursor Renglon = BD.rawQuery("select G.d_fechainicio_gas, G.d_fechafin_gas from t_Consumo_Gasolina as G", null);
        Cursor Renglon2 = BD.rawQuery("select count() from t_Consumo_Gasolina", null);

            if (Renglon2.moveToFirst()){
                do{
                    txtv_Activos.setText(Renglon2.getString(0));
                }while (Renglon2.moveToNext());
            }else{
                MensajeToast.setText("No hay datos en t_Consumo_Gasolina guardados");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }

            String diaini,mesini,anoini, diafin,mesfin,anofin;
            if (Renglon.moveToFirst()) {
                do {
                    String fechaIni=Renglon.getString(0), fechaFin=Renglon.getString(1);
                    anoini=fechaIni.substring(6);mesini=fechaIni.substring(2,4);diaini=fechaIni.substring(0,2);
                    anofin=fechaFin.substring(6);mesfin=fechaFin.substring(2,4);diafin =fechaFin.substring(0,2);
                    FechasIni.add(diaini+"/"+mesini+"/"+anoini);
                    AdaptadorIni = new ArrayAdapter<String>(Enviar_Gasolina.this, android.R.layout.simple_list_item_1, FechasIni);
                    lv_FechasIni.setAdapter(AdaptadorIni);
                    Log.e("Renglon------",FechasIni.toString());

                    FechasFin.add(diafin+"/"+mesfin+"/"+anofin);
                    AdaptadorFin = new ArrayAdapter<String>(Enviar_Gasolina.this, android.R.layout.simple_list_item_1, FechasFin);
                    lv_FechasFin.setAdapter(AdaptadorFin);
                    Log.e("Renglon------",FechasFin.toString());
                } while (Renglon.moveToNext());
            }else {
                MensajeToast.setText("No hay datos en t_Consumo_Gasolina guardados");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                BD.close();
            }

        if (FechasIni.size()>0){
            AdaptadorIni = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,FechasIni);
            lv_FechasIni.setAdapter(AdaptadorIni);
        }
    }

    public void EnviarGas(View view){
        if (obj.isConnected()) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select d_fechacrea_gas,c_folio_gas,d_fechainicio_gas,d_fechafin_gas,c_codigo_eps,Id_Huerta,v_Bloques_gas,Id_ActivosGas,c_codigo_emp,c_codigo_act,v_cantingreso_gas,v_cantsaldo_gas,v_tipo_gas,v_horometro_gas,v_kminicial_gas,v_kmfinal_gas,v_observaciones_gas from t_Consumo_Gasolina ", null);

            if (Renglon.moveToFirst()) {
                do {
                    insertWebGasolina(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10),Renglon.getString(11),Renglon.getString(12),Renglon.getString(13),Renglon.getString(14),Renglon.getString(15),Renglon.getString(16));
                } while (Renglon.moveToNext());
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

    private void insertWebGasolina(String d_fechacrea_gas, String c_folio_gas, String d_fechainicio_gas, String d_fechafin_gas, String c_codigo_eps, String Id_Huerta, String v_Bloques_gas, String Id_ActivosGas, String c_codigo_emp, String c_codigo_act, String v_cantingreso_gas, String v_cantsaldo_gas, String v_tipo_gas, String v_horometro_gas, String v_kminicial_gas, String v_kmfinal_gas, String v_observaciones_gas){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (d_fechacrea_gas == null){
            d_fechacrea_gas = "";
        }if (c_folio_gas == null){
            c_folio_gas="";
        }if (d_fechainicio_gas == null){
            d_fechainicio_gas="";
        }if (d_fechafin_gas == null){
            d_fechafin_gas="";
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

        String diaCrea,mesCrea,anoCrea,diaFI, mesFI,anoFI,diaFF,mesFF,anoFF;
        anoCrea=d_fechacrea_gas.substring(6);mesCrea=d_fechacrea_gas.substring(2,4);diaCrea=d_fechacrea_gas.substring(0,2);anoFI=d_fechainicio_gas.substring(6);mesFI=d_fechainicio_gas.substring(2,4);diaFI=d_fechainicio_gas.substring(0,2);anoFF=d_fechafin_gas.substring(6);mesFF=d_fechafin_gas.substring(2,4);diaFF=d_fechafin_gas.substring(0,2);

        Obtener_Ip();
        String Liga = "";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/Gasolina?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechainicio_gas="+ anoFI + "" + mesFI + "" + diaFI + "&d_fechafin_gas=" + anoFF + "" + mesFF + "" + diaFF + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&v_Bloques_gas=" + v_Bloques_gas + "&Id_ActivosGas=" + Id_ActivosGas + "&c_codigo_emp=" + c_codigo_emp + "&c_codigo_act=" + c_codigo_act + "&v_cantingreso_gas=" + v_cantingreso_gas + "&v_cantsaldo_gas=" + v_cantsaldo_gas + "&v_tipo_gas=" + v_tipo_gas + "&v_horometro_gas=" + v_horometro_gas + "&v_kminicial_gas=" + v_kminicial_gas + "&v_kmfinal_gas=" + v_kmfinal_gas + "&v_observaciones_gas=" + v_observaciones_gas;
            EliminaGasolina( d_fechacrea_gas, c_folio_gas, d_fechainicio_gas, d_fechafin_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas, c_codigo_emp, c_codigo_act, v_cantingreso_gas, v_cantsaldo_gas, v_tipo_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas);
            CargarDatos();
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Gasolina?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechainicio_gas="+ anoFI + "" + mesFI + "" + diaFI + "&d_fechafin_gas=" + anoFF + "" + mesFF + "" + diaFF + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&v_Bloques_gas=" + v_Bloques_gas + "&Id_ActivosGas=" + Id_ActivosGas + "&c_codigo_emp=" + c_codigo_emp + "&c_codigo_act=" + c_codigo_act + "&v_cantingreso_gas=" + v_cantingreso_gas + "&v_cantsaldo_gas=" + v_cantsaldo_gas + "&v_tipo_gas=" + v_tipo_gas + "&v_horometro_gas=" + v_horometro_gas + "&v_kminicial_gas=" + v_kminicial_gas + "&v_kmfinal_gas=" + v_kmfinal_gas + "&v_observaciones_gas=" + v_observaciones_gas;
                EliminaGasolina( d_fechacrea_gas, c_folio_gas, d_fechainicio_gas, d_fechafin_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas, c_codigo_emp, c_codigo_act, v_cantingreso_gas, v_cantsaldo_gas, v_tipo_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas);
                CargarDatos();
            }else{
                Liga = "http://177.241.250.117:8090//Gasolina?d_fecha_crea=" + anoCrea + "" + mesCrea + "" + diaCrea + "&c_folio_gas="+ c_folio_gas + "&d_fechainicio_gas="+ anoFI + "" + mesFI + "" + diaFI + "&d_fechafin_gas=" + anoFF + "" + mesFF + "" + diaFF + "&c_codigo_eps=" + c_codigo_eps + "&Id_Huerta=" + Id_Huerta + "&v_Bloques_gas=" + v_Bloques_gas + "&Id_ActivosGas=" + Id_ActivosGas + "&c_codigo_emp=" + c_codigo_emp + "&c_codigo_act=" + c_codigo_act + "&v_cantingreso_gas=" + v_cantingreso_gas + "&v_cantsaldo_gas=" + v_cantsaldo_gas + "&v_tipo_gas=" + v_tipo_gas + "&v_horometro_gas=" + v_horometro_gas + "&v_kminicial_gas=" + v_kminicial_gas + "&v_kmfinal_gas=" + v_kmfinal_gas + "&v_observaciones_gas=" + v_observaciones_gas;
                EliminaGasolina( d_fechacrea_gas, c_folio_gas, d_fechainicio_gas, d_fechafin_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas, c_codigo_emp, c_codigo_act, v_cantingreso_gas, v_cantsaldo_gas, v_tipo_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas);
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
                        if (EliminaGasolina( d_fechacrea_gas, c_folio_gas, d_fechainicio_gas, d_fechafin_gas, c_codigo_eps, Id_Huerta, v_Bloques_gas, Id_ActivosGas, c_codigo_emp, c_codigo_act, v_cantingreso_gas, v_cantsaldo_gas, v_tipo_gas, v_horometro_gas, v_kminicial_gas, v_kmfinal_gas, v_observaciones_gas)){
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

    private boolean EliminaGasolina(String d_fechacrea_gas, String c_folio_gas, String d_fechainicio_gas, String d_fechafin_gas, String c_codigo_eps, String Id_Huerta, String v_Bloques_gas, String Id_ActivosGas, String c_codigo_emp, String c_codigo_act, String v_cantingreso_gas, String v_cantsaldo_gas, String v_tipo_gas, String v_horometro_gas, String v_kminicial_gas, String v_kmfinal_gas, String v_observaciones_gas){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        int cantidad;
        cantidad=0;
        cantidad = BD.delete("t_Consumo_Gasolina", "d_fechacrea_gas='"+d_fechacrea_gas+"' and c_folio_gas='"+c_folio_gas+"' and d_fechainicio_gas='"+d_fechainicio_gas+"' and d_fechafin_gas='"+d_fechafin_gas+"' and c_codigo_eps='"+c_codigo_eps+"' and Id_Huerta='"+Id_Huerta+"' and v_Bloques_gas='"+v_Bloques_gas+"' and Id_ActivosGas='"+Id_ActivosGas+"' and c_codigo_emp='"+c_codigo_emp+"' and c_codigo_act='"+c_codigo_act+"' and v_cantingreso_gas='"+v_cantingreso_gas+"' and v_cantsaldo_gas='"+v_cantsaldo_gas+"' and v_tipo_gas='"+v_tipo_gas+"' and v_horometro_gas='"+v_horometro_gas+"' and v_kminicial_gas='"+v_kminicial_gas+"' and v_kmfinal_gas='"+v_kmfinal_gas+"' and v_observaciones_gas='"+v_observaciones_gas+"' ", null);
        CargarDatos();

        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

}