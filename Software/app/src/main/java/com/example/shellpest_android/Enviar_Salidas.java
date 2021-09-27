package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
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
import java.util.Iterator;

public class Enviar_Salidas extends AppCompatActivity {

    public String MyIp,CodAplicacion;
    ConexionInternet obj;

    TextView text_NAplicaciones2,text_Fechas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_salidas);

        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

        text_NAplicaciones2=(TextView)findViewById(R.id.text_NAplicaciones2);
        text_Fechas2=(TextView)findViewById(R.id.text_Fechas2);

        CargaDatos();
    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select count(S.Id_Salida) as Aplicaciones, S.Fecha,D.productos from t_Salidas as S, (select count(c_codigo_pro) as productos from t_Salidas_Det) as  D " , null);

        if (Renglon.moveToFirst()) {

            do {
                if(Renglon.getInt(0)>1){
                    text_NAplicaciones2.setText(Renglon.getString(0));
                }else{
                    if (Renglon.getInt(0)==0){
                        text_NAplicaciones2.setText(Renglon.getString(0));
                    }else{
                        text_NAplicaciones2.setText(Renglon.getString(0)+" salida del "+Renglon.getString(1));
                    }

                }
                text_Fechas2.setText(Renglon.getString(2));
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

    public void Enviar(View view) {
        if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Cursor Renglon = BD.rawQuery("select S.Id_Salida,S.c_codigo_eps,S.Id_Responsable,S.Id_Almacen,S.Id_Aplicacion,S.Fecha,S.Id_Usuario,S.F_Creacion from t_Salidas as S", null);

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

            BD.close();
        }else{
            Toast.makeText(this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
        }
        if (HaySalidas()){
            ActualizaExistenciaAlmacen();
        }
        CargaDatos();
    }

    private void insertWebEncabezado(String Id_Salida,String c_codigo_eps,String Id_Responsable,String Id_Almacen,String Id_Aplicacion,String Fecha,String Id_Usuario,String F_Creacion) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if(c_codigo_eps == null){
            c_codigo_eps="";
        }
        if(Id_Responsable== null){
            Id_Responsable="";
        }

        if(Id_Almacen==null){
            Id_Almacen="";
        }

        if(Id_Aplicacion==null){
            Id_Aplicacion="";
        }

        if(Id_Usuario==null){
            Id_Usuario="";
        }
        if(F_Creacion==null){
            F_Creacion="";
        }

        String dia,mes,ano,dia2,mes2,ano2;
        ano=F_Creacion.substring(6);
        mes=F_Creacion.substring(3, 5);
        dia=F_Creacion.substring(0, 2);

        ano2=Fecha.substring(6);
        mes2=Fecha.substring(3, 5);
        dia2=Fecha.substring(0, 2);

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/Salidas?c_codigo_sal="  + Id_Salida + "&c_codigo_ent= &c_codigo_tmv=04 &c_codigo_tra= &d_documento_sal=" + ano2 + "" + mes2 + "" + dia2  + "&c_codigo_alm=" + Id_Almacen + " &c_codigo_eps=" + c_codigo_eps + "&Id_Responsable=" + Id_Responsable + "&Id_Aplicacion=" + Id_Aplicacion  + "&Id_Usuario=" + Id_Usuario + "&F_Creacion=" + ano + "" + mes + "" + dia  ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/Salidas?c_codigo_sal="  + Id_Salida + "&c_codigo_ent= &c_codigo_tmv=04 &c_codigo_tra= &d_documento_sal=" + ano2 + "" + mes2 + "" + dia2  + "&c_codigo_alm=" + Id_Almacen + " &c_codigo_eps=" + c_codigo_eps + "&Id_Responsable=" + Id_Responsable + "&Id_Aplicacion=" + Id_Aplicacion  + "&Id_Usuario=" + Id_Usuario + "&F_Creacion=" + ano + "" + mes + "" + dia ;
            }else{
                Liga = "http://177.241.250.117:8090//Control/Salidas?c_codigo_sal="  + Id_Salida + "&c_codigo_ent= &c_codigo_tmv=04 &c_codigo_tra= &d_documento_sal=" + ano2 + "" + mes2 + "" + dia2  + "&c_codigo_alm=" + Id_Almacen + " &c_codigo_eps=" + c_codigo_eps + "&Id_Responsable=" + Id_Responsable +  "&Id_Aplicacion=" + Id_Aplicacion  + "&Id_Usuario=" + Id_Usuario + "&F_Creacion=" + ano + "" + mes + "" + dia  ;
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
                        Cursor Renglon2 = BD.rawQuery("select Id_Salida,c_codigo_pro,Cantidad,Id_Bloque,c_codigo_eps,n_exiant_mov from t_Salidas_Det where Id_Salida='"+Id_Salida+"' and c_codigo_eps='"+c_codigo_eps+"' ", null);

                        if (Renglon2.moveToFirst()) {

                            do {
                                //Toast.makeText(this, Renglon2.getString(7), Toast.LENGTH_SHORT).show();
                                insertWebDetalle(Renglon2.getString(0),Renglon2.getString(1),Renglon2.getString(2),Renglon2.getString(3),Renglon2.getString(4),Renglon2.getString(5));
                            } while (Renglon2.moveToNext());
                            if (EliminadeSalidaEncabezado(Id_Salida)){
                                RegistrosEnviados++;
                            }
                        } else {
                            //Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        if (jsonobject.optString("Mensaje").indexOf("Violation of PRIMARY KEY")>=0){
                            Toast.makeText(Enviar_Salidas.this, "Ya se envio ese punto en la fecha indicada", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Enviar_Salidas.this, "Ocurrio error al intentar guardar, notifique al administrador del sistema.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [MALFORMEDURLCAB]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [IOCAB]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [JSONCAB]", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertWebDetalle(String Id_Salida,String c_codigo_pro,String Cantidad,String Id_Bloque,String c_codigo_eps,String n_exiant_mov) {
        //Toast.makeText(MainActivity.this, Liga, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        if(c_codigo_pro== null){
            c_codigo_pro="";
        }
        if(Cantidad==null){
            Cantidad="0";
        }
        if(Id_Bloque==null){
            Id_Bloque="";
        }

        Obtener_Ip();
        String Liga="";
        if(MyIp.equals("0.0.0.0")){
            Liga = "http://177.241.250.117:8090//Control/Salidas_Det?c_tipodoc_mov=S &c_coddoc_mov=" +Id_Salida+ "&c_codigo_pro="  + c_codigo_pro +  "&n_movipro_mov=0 &n_exiant_mov="+n_exiant_mov+" &n_cantidad_mov=-" + Cantidad + "&Id_Bloque=" + Id_Bloque  +" &c_codigo_eps="+ c_codigo_eps ;
        } else {
            if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0 ||  MyIp.indexOf("10.0.2")>=0 ){
                Liga = "http://192.168.3.254:8090//Control/Salidas_Det?c_tipodoc_mov=S &c_coddoc_mov=" +Id_Salida+ "&c_codigo_pro="  + c_codigo_pro +  "&n_movipro_mov=0 &n_exiant_mov="+n_exiant_mov+" &n_cantidad_mov=-" + Cantidad + "&Id_Bloque=" + Id_Bloque +" &c_codigo_eps="+ c_codigo_eps ;
            }else{
                Liga = "http://177.241.250.117:8090//Control/Salidas_Det?c_tipodoc_mov=S &c_coddoc_mov=" +Id_Salida+ "&c_codigo_pro="  + c_codigo_pro +  "&n_movipro_mov=0 &n_exiant_mov="+n_exiant_mov+" &n_cantidad_mov=-" + Cantidad + "&Id_Bloque=" + Id_Bloque +" &c_codigo_eps="+ c_codigo_eps ;
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
                        if (EliminadeSalidaDetalle(Id_Salida,c_codigo_pro)){
                            RegistrosEnviados++;
                        }

                    }
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [MALFORMEDDET]", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [IODET]", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
            Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [JSONDET]", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean EliminadeSalidaEncabezado(String Id_Salida) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_Salidas", "Id_Salida='"+Id_Salida+"'", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean EliminadeSalidaDetalle(String Id_Salida, String c_codigo_pro) {
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        int cantidad = BD.delete("t_Salidas_Det", "Id_Salida='"+Id_Salida+"' and c_codigo_pro='"+c_codigo_pro+"'", null);
        BD.close();

        if (cantidad > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void ActualizaExistenciaAlmacen(){
        String LinkServerWeb;
        LinkServerWeb="http://177.241.250.117:8090//Control/ExistenciaProAlm";
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url= null;
            try {
                url = new URL(LinkServerWeb);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn= null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                conn.setRequestMethod("GET");
                conn.connect();

                BufferedReader in =new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;

                StringBuffer response =new StringBuffer();

                String json="";

                while ((inputLine=in.readLine())!=null){
                    response.append(inputLine);
                }

                json=response.toString();

                JSONArray jsonarr=null;

                try {
                    jsonarr=new JSONArray(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonarr.length()>0) {
                    String[][] datos;
                    JSONObject jsonobject = jsonarr.getJSONObject(0);
                    datos = new String[jsonarr.length()][jsonobject.length()];

                    String NombreId = "";
                    for (int i = 0; i < jsonarr.length(); i++) {
                        jsonobject = jsonarr.getJSONObject(i);

                        int columnas = 0;

                        Iterator llaves = jsonobject.keys();

                        while (llaves.hasNext()) {
                            String currentDynamicKey = (String) llaves.next();
                            //Toast.makeText(MainActivity.this, currentDynamicKey, Toast.LENGTH_SHORT).show();
                            datos[i][columnas] = jsonobject.optString(currentDynamicKey);
                            if (columnas == 0) {
                                NombreId = currentDynamicKey;
                            }
                            columnas++;
                        }
                    }

                    for (int x = 0; x < datos.length; x++) {


                        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
                        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
                        try {
                            Cursor Renglon = BD.rawQuery("select count(c_codigo_pro) as c_codigo_pro from t_existencias where c_codigo_eps='" + datos[x][0] + "' and c_codigo_alm='" + datos[x][2] + "' and c_codigo_pro='" + datos[x][1] + "'", null);

                            if (Renglon.moveToFirst()) {

                                if (Renglon.getInt(0) > 0) {
                                    ContentValues registro = new ContentValues();

                                    registro.put("Existencia", datos[x][3]);

                                    int cantidad = BD.update("t_existencias", registro, "c_codigo_eps='" + datos[x][0] + "' and c_codigo_alm='" + datos[x][2] + "' and c_codigo_pro='" + datos[x][1] + "'", null);

                                    if (cantidad > 0) {
                                        //////Toast.makeText(MainActivity.this,"Se actualizo t_Monitoreo correctamente.",Toast.LENGTH_SHORT).show();
                                    } else {
                                        //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Monitoreo ["+x+"], favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ContentValues registro2 = new ContentValues();
                                    registro2.put("c_codigo_eps", datos[x][0]);
                                    registro2.put("c_codigo_pro", datos[x][1]);
                                    registro2.put("c_codigo_alm", datos[x][2]);
                                    registro2.put("Existencia", datos[x][3]);

                                    BD.insert("t_existencias", null, registro2);
                                }
                                BD.close();
                            } else {

                                BD.close();
                            }
                        } catch (SQLiteConstraintException sqle) {
                            //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            String Pro = e.getMessage();
                            Toast.makeText(Enviar_Salidas.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(Enviar_Salidas.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
            }
        }catch (WindowManager.BadTokenException E){

        }
    }

    private boolean HaySalidas(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select count(S.Id_Salida) as Movimientos from t_Salidas_Det as S " , null);

        if (Renglon.moveToFirst()) {

            do {
                if(Renglon.getInt(0)>1){
                    return false;
                }else{
                    return true;
                }

            } while (Renglon.moveToNext());


        } else {
            return true;

        }
    }

}