package com.example.shellpest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.InputType;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Tablas_Sincronizadas Tabla;
    ListView Grid_Cambios;
    ArrayList<Tablas_Sincronizadas> arrayArticulos;
    Adaptador_Tabla Adapter;
    EditText date_Sinc;
    private int dia,mes, anio;
    public String Usuario,Perfil,Huerta;

    public String MyIp;

    ConexionInternet obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(MainActivity.this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

        date_Sinc=(EditText)findViewById(R.id.date_Sinc);
        date_Sinc.setOnClickListener(this);

        Grid_Cambios=(ListView) findViewById(R.id.lv_Cambios);

        Grid_Cambios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, arrayArticulos.get(position).getArticuloDescripcion(), Toast.LENGTH_SHORT).show();
            }
        });

        arrayArticulos=new ArrayList<>();

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;
        /*try {
            date1=new SimpleDateFormat("dd/MM/yyyy").parse(objDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        date_Sinc.setText(objSDF.format(date1));
        date_Sinc.setInputType(InputType.TYPE_NULL);
        date_Sinc.requestFocus();

        Usuario= getIntent().getStringExtra("usuario");
        Perfil= getIntent().getStringExtra("perfil");
        Huerta= getIntent().getStringExtra("huerta");


    }


    public void Existe_Sinc(View view){
        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;
        try {
            date1=new SimpleDateFormat("dd/MM/yyyy").parse(date_Sinc.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Sincroniza_Datos(objSDF.format(date1),view);

        /*AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select Fecha_Sincroniza from FechaSincroniza ",null);

        if(Renglon.moveToFirst()){

            if(Renglon.getString(0).length()>0){


                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate

                SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto


                Date date1=objDate;
                try {
                    date1=new SimpleDateFormat("dd/MM/yyyy").parse(Renglon.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, objSDF.format(date1)+"Aqui entro", Toast.LENGTH_SHORT).show();
                Sincroniza_Datos(objSDF.format(date1),view);
            }else{
                Sincroniza_Datos("01/01/1900",view );
                Toast.makeText(MainActivity.this, "01/01/1900", Toast.LENGTH_SHORT).show();
            }


            BD.close();
        }else{
            Sincroniza_Datos("01/01/1900",view);
            Toast.makeText(MainActivity.this, "01/01/1900", Toast.LENGTH_SHORT).show();
            BD.close();
        } Aqui quite por que se sincronizara en base a una fecha elegida por usuario */

    }

    public void continuar(View view){
        Intent intento=new Intent(this,activity_Monitoreo.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);
        //Toast.makeText(MainActivity.this, Usuario+","+Perfil+","+Huerta,Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }

    public void Obtener_Ip (){
        WifiManager ip= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        String Cip= Formatter.formatIpAddress(ip.getConnectionInfo().getIpAddress());
        MyIp=Cip;

    }

    public void Sincroniza_Datos (String esaFecha,View view){
        try{
            if (obj.isConnected() /*&& !MyIp.equals("0.0.0.0")*/) {
                Obtener_Ip();
                List <String> Ligas_Web =new ArrayList<>();

                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate

                SimpleDateFormat objSDF = new SimpleDateFormat("yyyyMMdd"); // La cadena de formato de fecha se pasa como un argumento al objeto

                Date date1=objDate;
                try {
                    date1=new SimpleDateFormat("dd/MM/yyyy").parse(esaFecha);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(MyIp.equals("0.0.0.0")){
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Calidad?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Cultivo?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Duenio?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Deteccion?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Enfermedad?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Humbral?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Plagas?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Productor?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Pais?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Estado?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Ciudad?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Huerta?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Bloques?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/PuntoControl?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Zona?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Individuo?Fecha=" + objSDF.format(date1));
                    Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Monitoreo?Fecha=" + objSDF.format(date1));
                } else {
                    if (MyIp.indexOf("192.168.3")>=0 || MyIp.indexOf("192.168.68")>=0  ||  MyIp.indexOf("10.0.2")>=0){
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Calidad?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Cultivo?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Duenio?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Deteccion?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Enfermedad?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Humbral?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Plagas?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Productor?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Pais?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Estado?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Ciudad?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Huerta?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Bloques?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/PuntoControl?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Zona?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Individuo?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://192.168.3.254:8090//Catalogos/Monitoreo?Fecha=" + objSDF.format(date1));
                    }else{
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Calidad?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Cultivo?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Duenio?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Deteccion?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Enfermedad?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Humbral?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Plagas?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Productor?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Pais?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Estado?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Ciudad?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Huerta?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Bloques?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/PuntoControl?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Zona?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Individuo?Fecha=" + objSDF.format(date1));
                        Ligas_Web.add("http://177.241.250.117:8090//Catalogos/Monitoreo?Fecha=" + objSDF.format(date1));
                    }
                }

                int regla3=0;

                Grid_Cambios.setAdapter(null);
                arrayArticulos.clear();

                for (int i=0;i<Ligas_Web.size();i++){
                    //Toast.makeText(MainActivity.this, Ligas_Web.get(i), Toast.LENGTH_SHORT).show();
                    LlamarWebService(Ligas_Web.get(i),regla3,i,Ligas_Web.size(),view);
                }

                if(arrayArticulos.size()>0){
                    Adapter=new Adaptador_Tabla(getApplicationContext(),arrayArticulos);
                    Grid_Cambios.setAdapter(Adapter);
                }else{
                    Toast.makeText(MainActivity.this, "No hay datos para sincronizar de la fecha seleccionada.", Toast.LENGTH_SHORT).show();
                }
                //ActualizaFechaSinc();
            }else{
                Toast.makeText(MainActivity.this, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
            }
        }catch (WindowManager.BadTokenException E){

        }


    }

    public void LlamarWebService(String Liga,int porcentaje,int ix,int total,View view){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url= null;
            try {
                url = new URL(Liga);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn= null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
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

                if (jsonarr.length()>0){
                    String [][] datos;
                    JSONObject jsonobject=jsonarr.getJSONObject(0);
                    datos = new String[jsonarr.length()][jsonobject.length()];

                    String NombreId="";
                    for (int i=0;i<jsonarr.length();i++){
                        jsonobject=jsonarr.getJSONObject(i);

                        int columnas=0;

                        Iterator llaves = jsonobject.keys();

                        while(llaves.hasNext()) {
                            String currentDynamicKey = (String)llaves.next();
                            //Toast.makeText(MainActivity.this, currentDynamicKey, Toast.LENGTH_SHORT).show();
                            datos[i][columnas]=jsonobject.optString(currentDynamicKey);
                            if (columnas==0){
                                NombreId=currentDynamicKey;
                            }
                            columnas++;
                        }
                    }

                    // declaración de switch
                    switch(NombreId)
                    {
                        // declaración case
                        // los valores deben ser del mismo tipo de la expresión
                        case "Id_Calidad" :
                            // Declaraciones
                            Actualiza_Calidad(datos);
                            break; // break es opcional
                        case "Id_Cultivo" :
                            // Declaraciones
                            Actualiza_Cultivo(datos);
                            break; // break es opcional
                        case "Id_Duenio" :
                            // Declaraciones
                            Actualiza_Duenio(datos);
                            break; // break es opcional
                        case "Id_Deteccion" :
                            // Declaraciones
                            Actualiza_Deteccion(datos);
                            break; // break es opcional
                        case "Id_Enfermedad" :
                            // Declaraciones
                            Actualiza_Enfermedad(datos);
                            break; // break es opcional
                        case "Id_Humbral" :
                            // Declaraciones
                            Actualiza_Humbral(datos);
                            break; // break es opcional
                        case "Id_Plagas" :
                            // Declaraciones
                            Actualiza_Plagas(datos);
                            break; // break es opcional
                        case "Id_Productor" :
                            // Declaraciones
                            Actualiza_Productor(datos);
                            break; // break es opcional
                        case "Id_Pais" :
                            // Declaraciones
                            Actualiza_Pais(datos);
                            break; // break es opcional
                        case "Id_Estado" :
                            // Declaraciones
                            Actualiza_Estado(datos);
                            break; // break es opcional
                        case "Id_Ciudad" :
                            // Declaraciones
                            Actualiza_Ciudades(datos);
                            break; // break es opcional
                        case "Id_Huerta" :
                            // Declaraciones
                            Actualiza_Huerta(datos);
                            break; // break es opcional
                        case "Id_Bloque" :
                            // Declaraciones
                            Actualiza_Bloque(datos);
                            break; // break es opcional
                        case "Id_PuntoControl" :
                            // Declaraciones
                            Actualiza_Puntocontrol(datos);
                            break; // break es opcional
                        case "Id_zona" :
                            // Declaraciones
                            Actualiza_Zona(datos);
                            break; // break es opcional
                        case "Id_Individuo" :
                            Actualiza_Individuo(datos);
                            break; // break es opcional
                        case "Id_monitoreo" :
                            Actualiza_Valores(datos);
                            break; // break es opcional
                    }
                }

                conn.disconnect();

                porcentaje=((ix+1)*100)/total;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(MainActivity.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(MainActivity.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                conn.disconnect();
                Toast.makeText(MainActivity.this, "Fallo la conexion al servidor [OPENPEDINS]", Toast.LENGTH_SHORT).show();
            }
        }catch (WindowManager.BadTokenException E){

        }
    }

    private void ActualizaFechaSinc(){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
        try{
            int cantidad= BD.delete("FechaSincroniza","id_Sincroniza<>'1000'",null);

            if(cantidad>0){
                //////Toast.makeText(MainActivity.this,"Se Elimino FechaSinc.",Toast.LENGTH_SHORT).show();
            }else{
                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar eliminar FechaSinc, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
            }

            ContentValues registro = new ContentValues();
            registro.put("id_Sincroniza", String.valueOf('1'));
            Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
            SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
            Date date1=objDate;

            registro.put("Fecha_Sincroniza", objSDF.format(date1));

            BD.insert("FechaSincroniza",null,registro);

            BD.close();

        }catch (Exception e){
            //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void hayCalidades(){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select count(Id_Calidad) from t_Calidad",null);

        if(Renglon.moveToFirst()){
            //////Toast.makeText(MainActivity.this,Renglon.getString(0),Toast.LENGTH_SHORT).show();
            }else{

            }
        BD.close();
    }

    private void Actualiza_Calidad(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Calidad",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Calidad) from t_Calidad where Id_Calidad='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Calidad",Datos[x][1]);

                            int cantidad=BD.update("t_Calidad",registro,"Id_Calidad='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Calidad",Datos[x][0]);
                            registro.put("Nombre_Calidad",Datos[x][1]);

                            BD.insert("t_Calidad",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Cultivo(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Cultivo",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Cultivo) from t_Cultivo where Id_Cultivo='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Cultivo",Datos[x][1]);

                            int cantidad=BD.update("t_Cultivo",registro,"Id_Cultivo='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Cultivo correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Cultivo, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Cultivo",Datos[x][0]);
                            registro.put("Nombre_Cultivo",Datos[x][1]);

                            BD.insert("t_Cultivo",null,registro);
                        }

                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Duenio(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Duenio",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Duenio) from t_Duenio where Id_Duenio='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Duenio",Datos[x][1]);

                            int cantidad=BD.update("t_Duenio",registro,"Id_Duenio='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Duenio correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Duenio, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Duenio",Datos[x][0]);
                            registro.put("Nombre_Duenio",Datos[x][1]);

                            BD.insert("t_Duenio",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Deteccion(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Deteccion",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Deteccion) from t_Deteccion where Id_Deteccion='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Deteccion",Datos[x][1]);

                            int cantidad=BD.update("t_Deteccion",registro,"Id_Deteccion='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Deteccion correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Deteccion, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Deteccion",Datos[x][0]);
                            registro.put("Nombre_Deteccion",Datos[x][1]);

                            BD.insert("t_Deteccion",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

    }

    private void Actualiza_Enfermedad(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Enfermedad",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){


                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Enfermedad) from t_Enfermedad where Id_Enfermedad='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Enfermedad",Datos[x][1]);

                            int cantidad=BD.update("t_Enfermedad",registro,"Id_Enfermedad='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Enfermedad correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Enfermedad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Enfermedad",Datos[x][0]);
                            registro.put("Nombre_Enfermedad",Datos[x][1]);

                            BD.insert("t_Enfermedad",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Humbral(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Humbral",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                Cursor Renglon =BD.rawQuery("select count(Id_Humbral) from t_Humbral where Id_Humbral='"+Datos[x][0].toString()+"'",null);

                if(Renglon.moveToFirst()){

                    if(Renglon.getInt(0)>0){
                        ContentValues registro = new ContentValues();
                        registro.put("Valor_Humbral",Datos[x][1]);
                        registro.put("Nombre_Humbral",Datos[x][2]);
                        registro.put("Color_Humbral",Datos[x][3]);

                        int cantidad=BD.update("t_Humbral",registro,"Id_Humbral='"+Datos[x][0].toString()+"'",null);

                        if(cantidad>0){
                            //////Toast.makeText(MainActivity.this,"Se actualizo t_Humbral correctamente.",Toast.LENGTH_SHORT).show();
                        }else{
                            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Humbral, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ContentValues registro= new ContentValues();
                        registro.put("Id_Humbral",Datos[x][0]);
                        registro.put("Valor_Humbral",Datos[x][1]);
                        registro.put("Nombre_Humbral",Datos[x][2]);
                        registro.put("Color_Humbral",Datos[x][3]);

                        BD.insert("t_Humbral",null,registro);
                    }
                    BD.close();
                }else{
                    BD.close();
                }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Plagas(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Plagas",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                Cursor Renglon =BD.rawQuery("select count(Id_Plagas) from t_Plagas where Id_Plagas='"+Datos[x][0].toString()+"'",null);

                if(Renglon.moveToFirst()){

                    if(Renglon.getInt(0)>0){
                        ContentValues registro = new ContentValues();

                        registro.put("Nombre_Plagas",Datos[x][1]);

                        int cantidad=BD.update("t_Plagas",registro,"Id_Plagas='"+Datos[x][0].toString()+"'",null);

                        if(cantidad>0){
                            //////Toast.makeText(MainActivity.this,"Se actualizo t_Plagas correctamente.",Toast.LENGTH_SHORT).show();
                        }else{
                            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Plagas, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ContentValues registro= new ContentValues();
                        registro.put("Id_Plagas",Datos[x][0]);
                        registro.put("Nombre_Plagas",Datos[x][1]);

                        BD.insert("t_Plagas",null,registro);
                    }
                    BD.close();
                }else{
                    BD.close();
                }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

    }

    private void Actualiza_Productor(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Productor",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Productor) from t_Productor where Id_Productor='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Productor",Datos[x][1]);

                            int cantidad=BD.update("t_Productor",registro,"Id_Productor='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Productor correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Productor, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Productor",Datos[x][0]);
                            registro.put("Nombre_Productor",Datos[x][1]);

                            BD.insert("t_Productor",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Pais(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Pais",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Pais) from t_Pais where Id_Pais='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Pais",Datos[x][1]);

                            int cantidad=BD.update("t_Pais",registro,"Id_Pais='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Pais correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Pais, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Pais",Datos[x][0]);
                            registro.put("Nombre_Pais",Datos[x][1]);

                            BD.insert("t_Pais",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Estado(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Estado",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Estado) from t_Estado where Id_Estado='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Estado",Datos[x][1]);
                            registro.put("Id_Pais",Datos[x][2]);

                            int cantidad=BD.update("t_Estado",registro,"Id_Estado='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Estado correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Estado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Estado",Datos[x][0]);
                            registro.put("Nombre_Estado",Datos[x][1]);
                            registro.put("Id_Pais",Datos[x][2]);

                            BD.insert("t_Estado",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Ciudades(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Ciudades",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Ciudad) from t_Ciudades where Id_Ciudad='"+Datos[x][0]+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Ciudad",Datos[x][1]);
                            registro.put("Id_Estado",Datos[x][2]);

                            int cantidad=BD.update("t_Ciudades",registro,"Id_Ciudad='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Ciudades correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Ciudades, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Ciudad",Datos[x][0]);
                            registro.put("Nombre_Ciudad",Datos[x][1]);
                            registro.put("Id_Estado",Datos[x][2]);

                            BD.insert("t_Ciudades",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Huerta(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Huerta",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Huerta) from t_Huerta where Id_Huerta='"+Datos[x][0]+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Nombre_Huerta",Datos[x][1]);
                            registro.put("Registro_Huerta",Datos[x][2]);
                            registro.put("Id_Productor",Datos[x][3]);
                            registro.put("Id_Estado",Datos[x][4]);
                            registro.put("Id_Ciudad",Datos[x][5]);
                            registro.put("Id_Calidad",Datos[x][6]);
                            registro.put("Id_Cultivo",Datos[x][7]);
                            registro.put("Id_Tratamiento",Datos[x][8]);
                            registro.put("zona_Huerta",Datos[x][9]);
                            registro.put("banda_Huerta",Datos[x][10]);
                            registro.put("este_Huerta",Datos[x][11]);
                            registro.put("norte_Huerta",Datos[x][12]);
                            registro.put("asnm_Huerta",Datos[x][13]);
                            registro.put("latitud_Huerta",Datos[x][14]);
                            registro.put("longitud_Huerta",Datos[x][15]);
                            registro.put("Activa_Huerta",Datos[x][20]);
                            registro.put("Id_zona",Datos[x][21]);
                            int cantidad=BD.update("t_Huerta",registro,"Id_Huerta='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Huerta correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Huerta, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Huerta",Datos[x][0]);
                            registro.put("Nombre_Huerta",Datos[x][1]);
                            registro.put("Registro_Huerta",Datos[x][2]);
                            registro.put("Id_Productor",Datos[x][3]);
                            registro.put("Id_Estado",Datos[x][4]);
                            registro.put("Id_Ciudad",Datos[x][5]);
                            registro.put("Id_Calidad",Datos[x][6]);
                            registro.put("Id_Cultivo",Datos[x][7]);
                            registro.put("Id_Tratamiento",Datos[x][8]);
                            registro.put("zona_Huerta",Datos[x][9]);
                            registro.put("banda_Huerta",Datos[x][10]);
                            registro.put("este_Huerta",Datos[x][11]);
                            registro.put("norte_Huerta",Datos[x][12]);
                            registro.put("asnm_Huerta",Datos[x][13]);
                            registro.put("latitud_Huerta",Datos[x][14]);
                            registro.put("longitud_Huerta",Datos[x][15]);
                            registro.put("Activa_Huerta",Datos[x][20]);
                            registro.put("Id_zona",Datos[x][21]);
                            BD.insert("t_Huerta",null,registro);
                        }

                        BD.close();
                    }else{

                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }
    private void Actualiza_Bloque(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Bloque",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){

                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_Bloque) from t_Bloque where Id_Bloque='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Id_Huerta",Datos[x][1]);
                            registro.put("Nombre_Bloque",Datos[x][2]);

                            int cantidad=BD.update("t_Bloque",registro,"Id_Bloque='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Bloque correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Bloque, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_Bloque",Datos[x][0]);
                            registro.put("Id_Huerta",Datos[x][1]);
                            registro.put("Nombre_Bloque",Datos[x][2]);

                            BD.insert("t_Bloque",null,registro);
                        }
                        BD.close();
                    }else{
                        BD.close();
                    }
                }catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }
    private void Actualiza_Puntocontrol(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Puntocontrol",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){


                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                try{
                    Cursor Renglon =BD.rawQuery("select count(Id_PuntoControl) from t_Puntocontrol where Id_PuntoControl='"+Datos[x][0].toString()+"'",null);

                    if(Renglon.moveToFirst()){

                        if(Renglon.getInt(0)>0){
                            ContentValues registro = new ContentValues();

                            registro.put("Id_Bloque",Datos[x][1]);
                            registro.put("Nombre_PuntoControl",Datos[x][2]);
                            registro.put("n_coordenadaX",Datos[x][3]);
                            registro.put("n_coordenadaY",Datos[x][4]);

                            int cantidad=BD.update("t_Puntocontrol",registro,"Id_PuntoControl='"+Datos[x][0].toString()+"'",null);

                            if(cantidad>0){
                                //////Toast.makeText(MainActivity.this,"Se actualizo t_Puntocontrol correctamente.",Toast.LENGTH_SHORT).show();
                            }else{
                                //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Puntocontrol ["+x+"], favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Id_PuntoControl",Datos[x][0]);
                            registro.put("Id_Bloque",Datos[x][1]);
                            registro.put("Nombre_PuntoControl",Datos[x][2]);
                            registro.put("n_coordenadaX",Datos[x][3]);
                            registro.put("n_coordenadaY",Datos[x][4]);


                            BD.insert("t_Puntocontrol",null,registro);
                        }

                        BD.close();
                    }else{

                        BD.close();
                    }
                } catch (SQLiteConstraintException sqle){
                    //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void Actualiza_Zona(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Zona",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){


            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
            try{
                Cursor Renglon =BD.rawQuery("select count(Id_zona) from t_Zona where Id_zona='"+Datos[x][0].toString()+"'",null);

                if(Renglon.moveToFirst()){

                    if(Renglon.getInt(0)>0){
                        ContentValues registro = new ContentValues();

                        registro.put("Nombre_zona",Datos[x][1]);


                        int cantidad=BD.update("t_Zona",registro,"Id_zona='"+Datos[x][0].toString()+"'",null);

                        if(cantidad>0){
                            //////Toast.makeText(MainActivity.this,"Se actualizo t_Zona correctamente.",Toast.LENGTH_SHORT).show();
                        }else{
                            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Zona ["+x+"], favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ContentValues registro= new ContentValues();
                        registro.put("Id_zona",Datos[x][0]);
                        registro.put("Nombre_zona",Datos[x][1]);



                        BD.insert("t_Zona",null,registro);
                    }

                    BD.close();
                }else{

                    BD.close();
                }
            } catch (SQLiteConstraintException sqle){
                //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void Actualiza_Individuo(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Individuo",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){


            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
            try{
                Cursor Renglon =BD.rawQuery("select count(Id_Individuo) from t_Individuo where Id_Individuo='"+Datos[x][0].toString()+"'",null);

                if(Renglon.moveToFirst()){

                    if(Renglon.getInt(0)>0){
                        ContentValues registro = new ContentValues();

                        registro.put("No_Individuo",Datos[x][1]);


                        int cantidad=BD.update("t_Individuo",registro,"Id_Individuo='"+Datos[x][0].toString()+"'",null);

                        if(cantidad>0){
                            //////Toast.makeText(MainActivity.this,"Se actualizo t_Individuo correctamente.",Toast.LENGTH_SHORT).show();
                        }else{
                            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Individuo ["+x+"], favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ContentValues registro= new ContentValues();
                        registro.put("Id_Individuo",Datos[x][0]);
                        registro.put("No_Individuo",Datos[x][1]);



                        BD.insert("t_Individuo",null,registro);
                    }

                    BD.close();
                }else{

                    BD.close();
                }
            } catch (SQLiteConstraintException sqle){
                //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void Actualiza_Valores(String [][] Datos ){
        Tabla=new Tablas_Sincronizadas("t_Monitoreo",Datos.length);
        arrayArticulos.add(Tabla);
        for(int x=0;x<Datos.length;x++){


            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
            try{
                Cursor Renglon =BD.rawQuery("select count(Id_monitoreo) from t_Monitoreo where Id_monitoreo='"+Datos[x][0].toString()+"'",null);

                if(Renglon.moveToFirst()){

                    if(Renglon.getInt(0)>0){
                        ContentValues registro = new ContentValues();

                        registro.put("Id_zona",Datos[x][1]);
                        registro.put("Id_Plagas",Datos[x][2]);
                        registro.put("Id_Enfermedad",Datos[x][3]);
                        registro.put("Id_Deteccion",Datos[x][4]);
                        registro.put("Id_Individuo",Datos[x][5]);
                        registro.put("Id_Humbral",Datos[x][6]);

                        int cantidad=BD.update("t_Monitoreo",registro,"Id_monitoreo='"+Datos[x][0].toString()+"'",null);

                        if(cantidad>0){
                            //////Toast.makeText(MainActivity.this,"Se actualizo t_Monitoreo correctamente.",Toast.LENGTH_SHORT).show();
                        }else{
                            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Monitoreo ["+x+"], favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ContentValues registro= new ContentValues();
                        registro.put("Id_monitoreo",Datos[x][0]);
                        registro.put("Id_zona",Datos[x][1]);
                        registro.put("Id_Plagas",Datos[x][2]);
                        registro.put("Id_Enfermedad",Datos[x][3]);
                        registro.put("Id_Deteccion",Datos[x][4]);
                        registro.put("Id_Individuo",Datos[x][5]);
                        registro.put("Id_Humbral",Datos[x][6]);

                        BD.insert("t_Monitoreo",null,registro);
                    }

                    BD.close();
                }else{

                    BD.close();
                }
            } catch (SQLiteConstraintException sqle){
                //////Toast.makeText(MainActivity.this,sqle.getMessage(),Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                //////Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onClick(View view) {
        if(view==date_Sinc){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> date_Sinc.setText(i2+"/"+(i1+1)+"/"+i),anio,mes,dia);
            dtpd.show();
        }
    }
}