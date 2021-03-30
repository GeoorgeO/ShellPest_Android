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

    private LocationManager Ubicacion;

    Itemmonitoreo Tabla;
    Adaptador_GridMonitorio Adapter;
    ArrayList<Itemmonitoreo> arrayArticulos;

    public String Usuario, Perfil, Huerta, Zona, Humbral,CoorX,CoorY;

    private ArrayList<ItemDatoSpinner> ItemSPHue, ItemSPPE, ItemSPPto, ItemSPOrg, ItemSPInd;
    private AdaptadorSpinner CopiHue, CopiPE, CopiPto, CopiOrg, CopiInd;

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
                Toast.makeText(activity_Monitoreo.this,"Hola",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(activity_Monitoreo.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar la P/E: "+arrayArticulos.get(i).getPE()+" en "+arrayArticulos.get(i).Nombre_Deteccion+". ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();
                        String e=arrayArticulos.get(i).Nombre_Deteccion;

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

       // Localizacion();
    }


    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
       // mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        Toast.makeText(activity_Monitoreo.this,mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()+","+mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(),Toast.LENGTH_SHORT).show();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, (LocationListener) Local);
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

    public void obtenerXeY(String X,String Y){

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

    private void Localizacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this,new String[]{
                   Manifest.permission.ACCESS_FINE_LOCATION
           },1000);
            return;
        }

        Ubicacion=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location loc = Ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(Ubicacion!=null){
            CoorX= String.valueOf(loc.getLatitude());
            CoorY= String.valueOf(loc.getLongitude());

            Toast.makeText(this,CoorX+","+CoorY,Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());

        ContentValues registro= new ContentValues();
        registro.put("Fecha",objSDF.format(date1));
        registro.put("Id_Huerta",Huerta);
        if(rb_Enfermedad.isChecked()){
            registro.put("Id_Plagas","");
            registro.put("Id_Enfermedad",CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4));
        }else{
            registro.put("Id_Plagas",CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4));
            registro.put("Id_Enfermedad","");
        }
        registro.put("Id_PuntoControl",CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));
        registro.put("Id_Deteccion",CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4));
        registro.put("Id_Individuo",CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,5));
        registro.put("Id_Usuario",Usuario);
        registro.put("Id_Humbral",Humbral);
        registro.put("n_coordenadaX",CoorX);
        registro.put("n_coordenadaY",CoorY);
        registro.put("Hora",currentTime);
        BD.insert("t_Monitoreo_PE",null,registro);
        BD.close();

        sp_Ind.setSelection(0);
        sp_Org.setSelection(0);
        sp_PE.setSelection(0);

        Cargagrid();
    }
    private void Cargagrid(){
        lv_GridMonitoreo.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select P.Nombre_PuntoControl, \n" +
                "\tCASE  when length(TRIM(PL.Id_Plagas)) >0 then PL.Nombre_Plagas else E.Nombre_Enfermedad end as PE,\n" +
                "\tD.Nombre_Deteccion,\n" +
                "\tI.No_Individuo \n" +
                "from t_Monitoreo_PE as M \n" +
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
                    Tabla=new Itemmonitoreo(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3));
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