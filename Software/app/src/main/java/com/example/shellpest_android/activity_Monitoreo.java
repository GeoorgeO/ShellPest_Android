package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class activity_Monitoreo extends AppCompatActivity {
    TextView et_fecha, et_PuntoControl, et_EP, et_Organo, et_Individuo;
    Spinner sp_PE, sp_Hue, sp_Pto, sp_Org, sp_Ind;
    RadioButton rb_Plaga, rb_Enfermedad,rb_SinPresencia;
    RadioGroup rg_PE;
    ListView lv_GridMonitoreo;

    //private LocationManager Ubicacion;

    Itemmonitoreo Tabla;
    Adaptador_GridMonitorio Adapter;
    ArrayList<Itemmonitoreo> arrayArticulos;

    public String Usuario, Perfil, Huerta, Zona, Humbral;

    private ArrayList<ItemDatoSpinner> ItemSPHue, ItemSPPE, ItemSPPto, ItemSPOrg, ItemSPInd;
    private AdaptadorSpinner CopiHue, CopiPE, CopiPto, CopiOrg, CopiInd;

    LocationManager mlocManager ;
    Localizacion Local ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__monitoreo);

        getSupportActionBar().hide();
        et_fecha = (TextView) findViewById(R.id.et_fecha);
        et_PuntoControl = (TextView) findViewById(R.id.et_PuntoControl);
        et_EP = (TextView) findViewById(R.id.et_EP);
        et_Organo = (TextView) findViewById(R.id.et_Organo);
        et_Individuo = (TextView) findViewById(R.id.et_Individuo);

        sp_PE = (Spinner) findViewById(R.id.sp_PE);
        sp_Hue = (Spinner) findViewById(R.id.sp_Hue);
        sp_Pto = (Spinner) findViewById(R.id.sp_Pto);
        sp_Org = (Spinner) findViewById(R.id.sp_Org);
        sp_Ind = (Spinner) findViewById(R.id.sp_Ind);

        rb_Plaga = (RadioButton) findViewById(R.id.rb_Plaga);
        rb_Enfermedad = (RadioButton) findViewById(R.id.rb_Enfermedad);
        rb_SinPresencia= (RadioButton) findViewById(R.id.rb_SinPresencia);

        rg_PE = (RadioGroup) findViewById(R.id.rg_PE);

        lv_GridMonitoreo = (ListView) findViewById(R.id.lv_GridMonitoreo);

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = objDate;
        et_fecha.setText("Fecha: " + objSDF.format(date1));

        ItemSPPE = new ArrayList<>();
        ItemSPPE.add(new ItemDatoSpinner("Plaga/Enfermedad"));
        CopiPE = new AdaptadorSpinner(this, ItemSPPE);
        sp_PE.setAdapter(CopiPE);

        cargaSpinnerHue();
        CopiHue = new AdaptadorSpinner(this, ItemSPHue);
        // CopiHue=AdaptadorSpiner;
        sp_Hue.setAdapter(CopiHue);

        ItemSPPto = new ArrayList<>();
        ItemSPPto.add(new ItemDatoSpinner("Punto de control"));
        CopiPto = new AdaptadorSpinner(this, ItemSPPto);
        sp_Pto.setAdapter(CopiPto);

        cargaSpinnerOrg();
        CopiOrg = new AdaptadorSpinner(this, ItemSPOrg);
        //CopiOrg=AdaptadorSpiner;
        sp_Org.setAdapter(CopiOrg);

        ItemSPInd = new ArrayList<>();
        ItemSPInd.add(new ItemDatoSpinner("Individuo"));
        CopiInd = new AdaptadorSpinner(this, ItemSPInd);
        sp_Ind.setAdapter(CopiInd);

        sp_Hue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    Huerta = CopiHue.getItem(i).getTexto().substring(0, 5);
                    Zona = CopiHue.getItem(i).getTexto().substring(CopiHue.getItem(i).getTexto().length() - 4);
                    cargaSpinnerPto();
                    CopiPto = new AdaptadorSpinner(getApplicationContext(), ItemSPPto);
                    //CopiPto=AdaptadorSpiner;
                    sp_Pto.setAdapter(CopiPto);

                    cargaSpinnerInd();
                    CopiInd = new AdaptadorSpinner(getApplicationContext(), ItemSPInd);
                    //CopiInd=AdaptadorSpiner;
                    sp_Ind.setAdapter(CopiInd);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Org.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    cargaSpinnerInd();
                    CopiInd = new AdaptadorSpinner(getApplicationContext(), ItemSPInd);
                    //CopiInd=AdaptadorSpiner;
                    sp_Ind.setAdapter(CopiInd);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_PE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    cargaSpinnerInd();
                    CopiInd = new AdaptadorSpinner(getApplicationContext(), ItemSPInd);
                    //CopiInd=AdaptadorSpiner;
                    sp_Ind.setAdapter(CopiInd);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Ind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    Humbral=CopiInd.getItem(i).getTexto().substring(CopiInd.getItem(i).getTexto().length() - 4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rg_PE.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rb_SinPresencia.isChecked()){
                    CopiPE=null;
                    ItemSPPE=new ArrayList<>();
                    ItemSPPE.add(new ItemDatoSpinner("Plaga/Enfermedad"));
                    CopiPE = new AdaptadorSpinner(getApplicationContext(), ItemSPPE);
                    //CopiInd=AdaptadorSpiner;
                    sp_PE.setAdapter(CopiPE);
                    sp_PE.setEnabled(false);
                    sp_Org.setEnabled(false);
                    sp_Ind.setEnabled(false);
                }else{
                    sp_PE.setEnabled(true);
                    sp_Org.setEnabled(true);
                    sp_Ind.setEnabled(true);
                    cargaSpinnerPE();
                    CopiPE = new AdaptadorSpinner(getApplicationContext(), ItemSPPE);
                    //CopiPE=AdaptadorSpiner;
                    sp_PE.setAdapter(CopiPE);
                }
            }
        });

        lv_GridMonitoreo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(activity_Monitoreo.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar la P/E: "+arrayArticulos.get(i).getPE()+" en "+arrayArticulos.get(i).Nombre_Deteccion+". ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();
                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(activity_Monitoreo.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

                        int cantidad= BD.delete("t_Monitoreo_PEDetalle","Id_PuntoControl='"+arrayArticulos.get(i).getcPto()+"' and Fecha='"+objSDF.format(date1)+"' and Id_Individuo='"+arrayArticulos.get(i).getcInd()+"' and Id_Deteccion='"+arrayArticulos.get(i).getcDet()+"' and  Id_Enfermedad='"+arrayArticulos.get(i).getcEnferma()+"' and Id_Plagas='"+arrayArticulos.get(i).getcPlaga()+"' ",null);
                        BD.close();

                        if(cantidad>0){

                        }else{
                            Toast.makeText(activity_Monitoreo.this,"Ocurrio un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                        Cargagrid();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //cancelar();
                    }
                });
                dialogo1.show();
                return false;
            }
        });
        arrayArticulos = new ArrayList<>();

        sp_Pto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    Cargagrid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Local= new Localizacion();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {


            locationStart();
        }

       // Localizacion();
    }



    private void locationStart() {

        mlocManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }else{
            Local.setMainActivity(activity_Monitoreo.this);
            final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {

                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            }
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        }
       // mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);


        //Toast.makeText(activity_Monitoreo.this,mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()+","+mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(),Toast.LENGTH_SHORT).show();
        //latitud.setText("Localización agregada");
        //direccion.setText("");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void ListarPuntos(View view){
        Intent intento = new Intent(this, Puntos_Capturados.class);
        intento.putExtra("usuario", Usuario);
        intento.putExtra("perfil", Perfil);
        intento.putExtra("huerta", Huerta);

        //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
        startActivity(intento);
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    //direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   private void cargaSpinnerPE(){
       CopiPE=null;

       ItemSPPE=new ArrayList<>();
       ItemSPPE.add(new ItemDatoSpinner("Plaga/Enfermedad"));

       AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
       SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
       Cursor Renglon;
       if(rb_Plaga.isChecked()){
           Renglon=BD.rawQuery("select Id_Plagas,Nombre_Plagas from t_Plagas",null);
       }else{
           Renglon =BD.rawQuery("select Id_Enfermedad,Nombre_Enfermedad from t_Enfermedad",null);
       }

       if(Renglon.moveToFirst()){

           do {
               ItemSPPE.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
           } while(Renglon.moveToNext());


           BD.close();
       }else{
           Toast.makeText(this,"No hay usuarios guardados",Toast.LENGTH_SHORT).show();
           BD.close();
       }
   }

    private void cargaSpinnerHue(){
        CopiHue=null;

        ItemSPHue=new ArrayList<>();

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True'",null);
        }else{
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and Id_Huerta='"+Huerta+"'",null);
            sp_Hue.setEnabled(false);
        }

        if(Renglon.moveToFirst()){
            do {
                ItemSPHue.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" "+Renglon.getString(2)));
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No se encontraron datos en huertas",Toast.LENGTH_SHORT).show();
            BD.close();
        }
        BD.close();
    }

    private void cargaSpinnerPto(){

        if(Huerta.length()>0 && !Huerta.equals("NULL")){
            CopiPto=null;

            ItemSPPto=new ArrayList<>();

            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            ItemSPPto.add(new ItemDatoSpinner("Punto de control"));

            Renglon=BD.rawQuery("select P.Id_PuntoControl,P.Nombre_PuntoControl,P.Id_Bloque,P.n_coordenadaX,P.n_coordenadaY from t_Puntocontrol as P, t_Bloque as B  where  P.Id_Bloque=B.Id_Bloque and B.Id_Huerta='"+Huerta+"'",null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSPPto.add(new ItemDatoSpinner(Renglon.getString(0)+" -    "+Renglon.getString(1)+"     ("+Renglon.getString(2)+","+Renglon.getString(3)+")"));
                } while(Renglon.moveToNext());
            }else{
                Toast.makeText(this,"No se encontraron datos en huertas",Toast.LENGTH_SHORT).show();
                BD.close();
            }
            BD.close();
        }else{

        }
    }

    private void cargaSpinnerOrg(){
        CopiOrg=null;

        ItemSPOrg=new ArrayList<>();
        ItemSPOrg.add(new ItemDatoSpinner("Organo muestreado"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select Id_Deteccion,Nombre_Deteccion from t_Deteccion",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPOrg.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
            } while(Renglon.moveToNext());


            BD.close();
        }else{
            Toast.makeText(this,"No hay datos en t_deteccion guardados",Toast.LENGTH_SHORT).show();
            BD.close();
        }
    }

    private void cargaSpinnerInd(){
        if(Huerta.length()>0 && !Huerta.equals("NULL") && sp_Org.getSelectedItemPosition()>0 && sp_PE.getSelectedItemPosition()>0){

        CopiInd=null;

        ItemSPInd=new ArrayList<>();
        ItemSPInd.add(new ItemDatoSpinner("Individuo"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(rb_Enfermedad.isChecked()){
            Renglon=BD.rawQuery("select M.Id_Individuo,I.No_Individuo,M.Id_Humbral from t_Monitoreo as M inner join t_Individuo as I on I.Id_Individuo=M.Id_Individuo where M.Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_zona='"+Zona+"' and M.Id_Plagas='' and M.Id_Enfermedad='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);
        }else{
            Renglon=BD.rawQuery("select M.Id_Individuo,I.No_Individuo,M.Id_Humbral from t_Monitoreo as M inner join t_Individuo as I on I.Id_Individuo=M.Id_Individuo where M.Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_zona='"+Zona+"' and M.Id_Plagas='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_Enfermedad=''",null);
        }
        if(Renglon.moveToFirst()){

            do {
                ItemSPInd.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+"         Humbral: "+Renglon.getString(2)));
            } while(Renglon.moveToNext());


            BD.close();
        }else{
            Toast.makeText(this,"No hay datos en t_Monitoreo guardados",Toast.LENGTH_SHORT).show();
            BD.close();
        }
        }else{

        }
    }

    public void agregarAGrid (View view){
        if(sp_Pto.getSelectedItemPosition()>0){
            boolean FaltoAlgo;
            FaltoAlgo=false;
            if(rb_Enfermedad.isChecked() || rb_Plaga.isChecked()){
                if(sp_PE.getSelectedItemPosition()>0){
                    if(sp_Org.getSelectedItemPosition()>0){
                        if(sp_Ind.getSelectedItemPosition()>0){
                            FaltoAlgo=false;
                        }else{
                            FaltoAlgo=true;
                            Toast.makeText(this,"Falta seleccionar un numero de individuos.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        FaltoAlgo=true;
                        Toast.makeText(this,"Falta seleccionar el organo muestreado",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    FaltoAlgo=true;
                    Toast.makeText(this,"Falta seleccionar la plaga o enfermedad",Toast.LENGTH_SHORT).show();
                }

            }else{
                if(rb_SinPresencia.isChecked()){
                    FaltoAlgo=false;
                }else{
                    FaltoAlgo=true;
                    Toast.makeText(this,"Es necesario marcar 'Sin presencia' en caso de que no se detecte ninguna plaga o enfermedad.",Toast.LENGTH_SHORT).show();
                }
            }
            if (!FaltoAlgo){
                /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                        } else {
                            locationStart();
                        }*/
                //Toast.makeText(this,Local.Lat+","+Local.Long,Toast.LENGTH_SHORT).show();
                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                Date date1=objDate;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String currentTime = simpleDateFormat.format(new Date());

                Cursor Renglon;
                Renglon=BD.rawQuery("select count(M.Id_PuntoControl) as Sihay from t_Monitoreo_PEEncabezado as M where M.Fecha='"+objSDF.format(date1)+"' and M.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' ",null);
                if(Renglon.moveToFirst()){

                    do {
                        if (Renglon.getInt(0)>0){
                            if(rb_SinPresencia.isChecked()){

                            }else{
                                ContentValues registro = new ContentValues();

                                registro.put("n_coordenadaX",Local.Lat);
                                registro.put("n_coordenadaY",Local.Long);

                                int cantidad=BD.update("t_Monitoreo_PEEncabezado",registro,"Fecha='"+objSDF.format(date1)+"' and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);

                                if(cantidad>0){
                                    //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                                }else{
                                    //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                }
                            }


                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Fecha",objSDF.format(date1));
                            registro.put("Id_Huerta",Huerta);

                            registro.put("Id_PuntoControl",CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));

                            registro.put("Id_Usuario",Usuario);

                            registro.put("n_coordenadaX",Local.Lat);
                            registro.put("n_coordenadaY",Local.Long);
                            registro.put("Hora",currentTime);
                            BD.insert("t_Monitoreo_PEEncabezado",null,registro);
                        }

                    } while(Renglon.moveToNext());



                }else{
                    Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();

                }

                if(rb_Enfermedad.isChecked()){
                    Renglon =BD.rawQuery("select count(Id_PuntoControl) " +
                            "from t_Monitoreo_PEDetalle " +
                            "where Id_Plagas='' and Id_Enfermedad='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                            "and Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                            "and  Fecha='"+objSDF.format(date1)+"' " +
                            "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);

                }else{
                    if(rb_Plaga.isChecked()){
                        Renglon =BD.rawQuery("select count(Id_PuntoControl) " +
                                "from t_Monitoreo_PEDetalle " +
                                "where Id_Plagas='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and Id_Enfermedad='' " +
                                "and Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                                "and  Fecha='"+objSDF.format(date1)+"' " +
                                "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);
                    }else{
                        Renglon =BD.rawQuery("select count(Id_PuntoControl) " +
                                "from t_Monitoreo_PEDetalle " +
                                "where Id_Plagas='' and Id_Enfermedad='' " +
                                "and Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                                "and  Fecha='"+objSDF.format(date1)+"' " +
                                "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);
                    }
                }

                if(Renglon.moveToFirst()){

                    if(Renglon.getInt(0)>0){
                        Toast.makeText(this,"Ya existe un dato en esta fecha con misma PE y Punto de control en esta fecha",Toast.LENGTH_SHORT).show();
                    }else{
                        if(rb_SinPresencia.isChecked()){

                        }else{
                            ContentValues registro2= new ContentValues();
                            registro2.put("Fecha",objSDF.format(date1));
                            registro2.put("Id_PuntoControl",CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));
                            if(rb_Enfermedad.isChecked()){
                                registro2.put("Id_Plagas","");
                                registro2.put("Id_Enfermedad",CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4));
                            }else{
                                if(rb_Plaga.isChecked()){
                                    registro2.put("Id_Plagas",CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4));
                                    registro2.put("Id_Enfermedad","");
                                }else{
                                    registro2.put("Id_Plagas","");
                                    registro2.put("Id_Enfermedad","");
                                }

                            }
                            registro2.put("Id_Deteccion",CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4));
                            registro2.put("Id_Individuo",CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,5));
                            registro2.put("Id_Humbral",Humbral);
                            BD.insert("t_Monitoreo_PEDetalle",null,registro2);
                        }

                    }

                }else{
                    Toast.makeText(this,"No Regreso nada la consulta de Detalle",Toast.LENGTH_SHORT).show();
                }



                BD.close();

        /*sp_Ind.setSelection(0);
        sp_Org.setSelection(0);
        sp_PE.setSelection(0);*/

                Cargagrid();
            }

        }else{
            Toast.makeText(activity_Monitoreo.this,"Falta seleccionar un punto de control",Toast.LENGTH_SHORT).show();
        }

    }
    private void Cargagrid(){
        lv_GridMonitoreo.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

       // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select P.Nombre_PuntoControl, \n" +
                "\tCASE  when length(TRIM(PL.Id_Plagas)) >0 then PL.Nombre_Plagas else E.Nombre_Enfermedad end as PE,\n" +
                "\tD.Nombre_Deteccion,\n" +
                "\tI.No_Individuo, \n" +
                "\tM.Id_PuntoControl, \n" +
                "\tM.Id_Deteccion, \n" +
                "\tM.Id_Individuo, \n" +
                "\tM.Id_Plagas, \n" +
                "\tM.Id_Enfermedad \n" +
                "from t_Monitoreo_PEDetalle as M \n" +
                "inner join t_Puntocontrol as P on M.Id_PuntoControl=P.Id_PuntoControl \n" +
                "inner join t_Deteccion as D on M.Id_Deteccion=D.Id_Deteccion \n" +
                "left join t_Plagas as Pl on M.Id_Plagas=Pl.Id_Plagas\n" +
                "left join t_Enfermedad as E on M.Id_enfermedad=E.Id_enfermedad\n" +
                "left join t_Individuo as I on I.Id_Individuo=M.Id_Individuo\n" +
                "where M.Fecha='"+objSDF.format(date1)+"' and M.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new Itemmonitoreo(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridMonitorio(getApplicationContext(),arrayArticulos);
            lv_GridMonitoreo.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }
}