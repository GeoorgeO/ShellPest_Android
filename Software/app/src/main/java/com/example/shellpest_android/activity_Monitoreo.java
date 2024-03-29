package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.DatePickerDialog;
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

import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class activity_Monitoreo extends AppCompatActivity implements View.OnClickListener{
    TextView et_fecha, et_PuntoControl, et_EP, et_Organo, et_Individuo;
    Spinner sp_PE, sp_Hue, sp_Pto, sp_Org, sp_Ind,sp_Empresa2;
    RadioButton rb_Plaga, rb_Enfermedad,rb_SinPresencia,rb_Fumi;
    RadioGroup rg_PE;
    ListView lv_GridMonitoreo;
    Boolean SoloUnaHuerta;
    Button button_UpFecha;

    EditText et_individuos,et_Observa;

    //private LocationManager Ubicacion;

    Itemmonitoreo Tabla;
    Adaptador_GridMonitorio Adapter;
    ArrayList<Itemmonitoreo> arrayArticulos;

    public String Usuario, Perfil, Huerta, Zona, Humbral,Individuo, Fecha_act;

    private ArrayList<ItemDatoSpinner> ItemSPHue, ItemSPPE, ItemSPPto, ItemSPOrg, ItemSPInd,ItemSPEmp;
    private AdaptadorSpinner CopiHue, CopiPE, CopiPto, CopiOrg, CopiInd,CopiEmp;

    LocationManager mlocManager ;
    Localizacion Local ;

    int dia,mes,anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__monitoreo);


        SoloUnaHuerta=false;
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
        sp_Empresa2 = (Spinner) findViewById(R.id.sp_Empresa2);

        rb_Plaga = (RadioButton) findViewById(R.id.rb_Plaga);
        rb_Enfermedad = (RadioButton) findViewById(R.id.rb_Enfermedad);
        rb_SinPresencia= (RadioButton) findViewById(R.id.rb_SinPresencia);
        rb_Fumi= (RadioButton) findViewById(R.id.rb_Fumi);

        rg_PE = (RadioGroup) findViewById(R.id.rg_PE);

        lv_GridMonitoreo = (ListView) findViewById(R.id.lv_GridMonitoreo);

        et_individuos=(EditText) findViewById(R.id.et_individuos) ;

        button_UpFecha=(Button) findViewById(R.id.button_UpFecha);
        et_Observa=(EditText) findViewById(R.id.et_Observa) ;

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");


        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa2.setAdapter(CopiEmp);

        if (sp_Empresa2.getCount()==2){
            sp_Empresa2.setSelection(1);
        }

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = objDate;
        et_fecha.setText(objSDF.format(date1));
        et_fecha.setInputType(InputType.TYPE_NULL);
        et_fecha.requestFocus();

        et_fecha.setOnClickListener(this);

        ItemSPPE = new ArrayList<>();
        ItemSPPE.add(new ItemDatoSpinner("Plaga/Enfermedad",""));
        CopiPE = new AdaptadorSpinner(this, ItemSPPE);
        sp_PE.setAdapter(CopiPE);

        /*cargaSpinnerHue();
        CopiHue = new AdaptadorSpinner(this, ItemSPHue);
        // CopiHue=AdaptadorSpiner;
        sp_Hue.setAdapter(CopiHue);

        if (sp_Hue.getCount()==2){
            sp_Hue.setSelection(1);
        }*/

        ItemSPPto = new ArrayList<>();
        ItemSPPto.add(new ItemDatoSpinner("Punto de control",""));
        CopiPto = new AdaptadorSpinner(this, ItemSPPto);
        sp_Pto.setAdapter(CopiPto);

        if (sp_Pto.getCount()==2){
            sp_Pto.setSelection(1);
        }

        /*cargaSpinnerOrg();
        CopiOrg = new AdaptadorSpinner(this, ItemSPOrg);
        //CopiOrg=AdaptadorSpiner;
        sp_Org.setAdapter(CopiOrg);*/

        ItemSPInd = new ArrayList<>();
        ItemSPInd.add(new ItemDatoSpinner("Est.Fenologico",""));
        CopiInd = new AdaptadorSpinner(this, ItemSPInd);
        sp_Ind.setAdapter(CopiInd);

        ItemSPOrg=new ArrayList<>();
        ItemSPOrg.add(new ItemDatoSpinner("Organo muestreado",""));
        CopiOrg = new AdaptadorSpinner(this, ItemSPOrg);
        sp_Org.setAdapter(CopiOrg);

        et_individuos.setText("0");

        sp_Empresa2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cargaSpinnerHue();
                CopiHue = new AdaptadorSpinner(activity_Monitoreo.this, ItemSPHue);
                // CopiHue=AdaptadorSpiner;
                sp_Hue.setAdapter(CopiHue);
                System.out.println(CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getValor()+sp_Empresa2.getSelectedItemPosition());

                if (sp_Hue.getCount()==2 || SoloUnaHuerta==true){
                    if(SoloUnaHuerta==true){

                    }else{
                        sp_Hue.setSelection(1);
                    }

                }else{
                    if (sp_Hue.getCount()<=1){
                        ItemSPHue = new ArrayList<>();
                        ItemSPHue.add(new ItemDatoSpinner("Huerta",""));
                        CopiHue = new AdaptadorSpinner(activity_Monitoreo.this, ItemSPHue);
                        sp_Hue.setAdapter(CopiHue);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Hue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if ((SoloUnaHuerta==true && i == 0 ) || (SoloUnaHuerta==false && i>0)) {
                    // Notify the selected item text
                    Huerta = CopiHue.getItem(i).getTexto().substring(0, 5);
                    Zona = CopiHue.getItem(i).getTexto().substring(CopiHue.getItem(i).getTexto().length() - 4);
                    cargaSpinnerPto();
                    CopiPto = new AdaptadorSpinner(getApplicationContext(), ItemSPPto);
                    //CopiPto=AdaptadorSpiner;
                    sp_Pto.setAdapter(CopiPto);

                  /*  cargaSpinnerInd();
                    CopiInd = new AdaptadorSpinner(getApplicationContext(), ItemSPInd);
                    //CopiInd=AdaptadorSpiner;
                    sp_Ind.setAdapter(CopiInd);*/
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

                    if (sp_Ind.getCount()==2){
                        sp_Ind.setSelection(1);
                    }
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
                    cargaSpinnerOrg();
                    CopiOrg = new AdaptadorSpinner(getApplicationContext(), ItemSPOrg);
                    //CopiOrg=AdaptadorSpiner;
                    sp_Org.setAdapter(CopiOrg);

                    if (sp_Org.getCount()==2){
                        sp_Org.setSelection(1);
                    }

                    // Notify the selected item text
                   /* cargaSpinnerInd();
                    CopiInd = new AdaptadorSpinner(getApplicationContext(), ItemSPInd);
                    //CopiInd=AdaptadorSpiner;
                    sp_Ind.setAdapter(CopiInd);*/


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
                   // Humbral=CopiInd.getItem(i).getTexto().substring(CopiInd.getItem(i).getTexto().length() - 4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rg_PE.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rb_SinPresencia.isChecked() || rb_Fumi.isChecked()){
                    CopiPE=null;
                    ItemSPPE=new ArrayList<>();
                    ItemSPPE.add(new ItemDatoSpinner("Plaga/Enfermedad",""));
                    CopiPE = new AdaptadorSpinner(getApplicationContext(), ItemSPPE);
                    //CopiInd=AdaptadorSpiner;
                    sp_PE.setAdapter(CopiPE);
                    sp_PE.setEnabled(false);
                    sp_Org.setEnabled(false);
                    sp_Ind.setEnabled(false);
                    if (Revisa_SiGuardado()>0){
                        button_UpFecha.setVisibility(View.VISIBLE);
                    }else{
                        button_UpFecha.setVisibility(View.INVISIBLE);
                    }
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
                        String SQLWhere;
                        SQLWhere="";
                        if (arrayArticulos.get(i).getcPlaga()!=null){
                            if (arrayArticulos.get(i).getcPlaga().trim().length()>0) {
                                SQLWhere = "Id_PuntoControl='" + arrayArticulos.get(i).getcPto() + "' and Fecha='" + et_fecha.getText().toString().trim() + "'  and Id_Deteccion='" + arrayArticulos.get(i).getcDet() + "' and Id_Plagas='" + arrayArticulos.get(i).getcPlaga() + "' and Id_Fenologico='" + arrayArticulos.get(i).getcInd() + "' ";
                            }
                        }else{
                            if (arrayArticulos.get(i).getcEnferma()!=null){
                                if (arrayArticulos.get(i).getcEnferma().trim().length()>0) {
                                    SQLWhere = "Id_PuntoControl='" + arrayArticulos.get(i).getcPto() + "' and Fecha='" + et_fecha.getText().toString().trim() + "'  and Id_Deteccion='" + arrayArticulos.get(i).getcDet() + "' and  Id_Enfermedad='" + arrayArticulos.get(i).getcEnferma() + "' and Id_Fenologico='" + arrayArticulos.get(i).getcInd() + "' ";
                                }
                            }else{
                                //SQLWhere = "Id_PuntoControl='" + arrayArticulos.get(i).getcPto() + "' and Fecha='" + et_fecha.getText().toString().trim() + "' ";
                            }
                        }

                        int cantidad= BD.delete("t_Monitoreo_PEDetalle",SQLWhere,null);
                        BD.close();

                        if(cantidad>0){

                        }else{
                            Toast.makeText(activity_Monitoreo.this,"Ocurrio un error al intentar eliminar el detalle seleccionado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
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
                    CargaObserva();
                    if (lv_GridMonitoreo.getCount()==0 && revisaSinPResencia()!=2 ){
                        if (revisaSinPResencia()==0){
                            rb_SinPresencia.setChecked(true);
                        }
                        if (revisaSinPResencia()==1){
                            rb_Fumi.setChecked(true);
                        }
                    }else{
                        rb_Plaga.setChecked(true);
                    }
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

    public void onClick(View view) {
        if(view==et_fecha){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> et_fecha.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
            sp_Pto.setSelection(0);
        }
    }

    private void CargaObserva(){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;
        String SQLS="select ifnull(Observaciones,'') from t_Monitoreo_PEEncabezado as E  where E.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and E.Fecha='"+et_fecha.getText().toString().trim()+"'";
        Renglon=BD.rawQuery(SQLS,null);

        if(Renglon.moveToFirst()){
            do {

                    BD.close();
                    et_Observa.setText(Renglon.getString(0).trim());
            } while(Renglon.moveToNext());


        }else{
            BD.close();
            et_Observa.setText("");
        }
    }

    private String rellenarCeros (String Valor,int NCaracteres){
        if (Valor.length()<NCaracteres){
            int faltan=0;
            faltan=NCaracteres - Valor.length();
            if (faltan==3){
                return "000"+Valor;
            }
            if (faltan==2){
                return "00"+Valor;
            }
            if (faltan==1){
                return "0"+Valor;
            }
        }
        return Valor;
    }

    private int revisaSinPResencia(){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;
        String SQLS="select count(D.Id_PuntoControl),ifnull(Fumigado,0) from t_Monitoreo_PEEncabezado as E left join t_Monitoreo_PEDetalle as D on D.Fecha=E.Fecha and D.Id_PuntoControl=E.Id_PuntoControl where E.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and E.Fecha='"+et_fecha.getText().toString().trim()+"'";
        Renglon=BD.rawQuery(SQLS,null);

        if(Renglon.moveToFirst()){
            do {
               if( Renglon.getInt(0)>0){
                   BD.close();
                  return 2;//el 2 es cuando hay plagas o enfermedad, antes return true;
               }else{
                   if( Renglon.getString(1).equals("1")) {
                       BD.close();
                       return 1; //el 1 es para fumigados;
                   }else{
                       BD.close();
                       return 0; //el 0 es para sin presencia;
                   }
               }
            } while(Renglon.moveToNext());


        }else{
            BD.close();
            return 0;
        }

    }

    private void cargaSpinnerEmpresa(){
        CopiEmp=null;

        ItemSPEmp=new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa",""));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select E.c_codigo_eps,E.v_abrevia_eps from conempresa as E inner join t_Usuario_Empresa as UE on UE.c_codigo_eps=E.c_codigo_eps where UE.Id_Usuario='"+Usuario+"' ",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPEmp.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1),Renglon.getString(0)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    public void MtdCerrar(View view){

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
                if(rb_SinPresencia.isChecked() || rb_Fumi.isChecked()){
                    FaltoAlgo=false;
                }else{
                    FaltoAlgo=true;
                    Toast.makeText(this,"Es necesario marcar 'Sin presencia' o 'Fumigado' en caso de que no se detecte ninguna plaga o enfermedad.",Toast.LENGTH_SHORT).show();
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
                Renglon=BD.rawQuery("select count(M.Id_PuntoControl) as Sihay,Hora from t_Monitoreo_PEEncabezado as M where M.Fecha='"+et_fecha.getText().toString().trim()+"' and M.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
                if(Renglon.moveToFirst()){

                    do {
                        if (Renglon.getInt(0)>0){
                            currentTime=Renglon.getString(1);
                            /*if(rb_SinPresencia.isChecked()){

                            }else{*/
                                ContentValues registro = new ContentValues();

                                registro.put("n_coordenadaX",Local.Long);
                                registro.put("n_coordenadaY",Local.Lat);

                                int cantidad=BD.update("t_Monitoreo_PEEncabezado",registro,"Fecha='"+et_fecha.getText().toString().trim()+"' and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                                if(cantidad>0){
                                    //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                                }else{
                                    //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                }
                            //}


                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Fecha",et_fecha.getText().toString().trim());
                            registro.put("Id_Huerta",Huerta);
                            registro.put("Id_PuntoControl",CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));
                            registro.put("Id_Usuario",Usuario);
                            registro.put("F_UsuCrea",(objSDF.format(date1)));
                            registro.put("n_coordenadaX",Local.Long);
                            registro.put("n_coordenadaY",Local.Lat);
                            registro.put("Hora",currentTime);
                            registro.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2));
                            registro.put("Observaciones",et_Observa.getText().toString().trim());
                            if(rb_Fumi.isChecked()){
                                registro.put("Fumigado","1");
                            }else{
                                registro.put("Fumigado","0");
                            }

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
                            "and  Fecha='"+et_fecha.getText().toString().trim()+"' " +
                            "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                }else{
                    if(rb_Plaga.isChecked()){
                        Renglon =BD.rawQuery("select count(Id_PuntoControl) " +
                                "from t_Monitoreo_PEDetalle " +
                                "where Id_Plagas='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and Id_Enfermedad='' " +
                                "and Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                                "and  Fecha='"+et_fecha.getText().toString().trim()+"' " +
                                "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
                    }else{
                        Renglon =BD.rawQuery("select count(Id_PuntoControl) " +
                                "from t_Monitoreo_PEDetalle " +
                                "where Id_Plagas='' and Id_Enfermedad='' " +
                                "and Id_Deteccion='' "+ //'"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                                "and  Fecha='"+et_fecha.getText().toString().trim()+"' " +
                                "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
                    }
                }

                if(Renglon.moveToFirst()){
                    if(Renglon.getInt(0)>0){
                        Toast.makeText(this,"Ya existe un dato en esta fecha con misma PE y Punto de control en esta fecha",Toast.LENGTH_SHORT).show();
                    }else{
                        if(rb_SinPresencia.isChecked() || rb_Fumi.isChecked()){
                          /*  ContentValues registro2= new ContentValues();
                            registro2.put("Fecha",et_fecha.getText().toString().trim());
                            registro2.put("Id_PuntoControl",CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));
                            registro2.put("Id_Humbral","0001");
                            registro2.put("Hora",currentTime);
                            registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2));
                            BD.insert("t_Monitoreo_PEDetalle",null,registro2);*/
                        }else{
                            ContentValues registro2= new ContentValues();
                            registro2.put("Fecha",et_fecha.getText().toString().trim());
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
                            registro2.put("Id_Individuo",Individuo);
                            registro2.put("Id_Humbral",Humbral);
                            registro2.put("Hora",currentTime);
                            registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2));
                            registro2.put("Cant_Individuos",Integer.parseInt( et_individuos.getText().toString()));
                            registro2.put("Id_Fenologico",CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,2));
                            BD.insert("t_Monitoreo_PEDetalle",null,registro2);
                        }

                    }

                }else{
                    Toast.makeText(this,"No Regreso nada la consulta de Detalle",Toast.LENGTH_SHORT).show();
                }

                BD.close();
                Cargagrid();
                Toast.makeText(this,"Punto Guardado",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(activity_Monitoreo.this,"Falta seleccionar un punto de control",Toast.LENGTH_SHORT).show();
        }


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
       ItemSPPE.add(new ItemDatoSpinner("Plaga/Enfermedad",""));

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
               ItemSPPE.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1),Renglon.getString(0)));
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

            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }else{
            Renglon=BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta and Hue.c_codigo_eps=UH.c_codigo_eps where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and Hue.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
            //Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and Id_Huerta='"+Huerta+"'",null);
            //sp_Hue.setEnabled(false);
        }

        if(Renglon.getCount()>1){
            ItemSPHue.add(new ItemDatoSpinner("Huerta",""));
            SoloUnaHuerta=false;
        }else{
            if(Renglon.getCount()==1){
                SoloUnaHuerta=true;
            }
        }

        if(Renglon.moveToFirst()){
            do {
                ItemSPHue.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" "+Renglon.getString(2),Renglon.getString(0)));
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

            ItemSPPto.add(new ItemDatoSpinner("Punto de control",""));

            Renglon=BD.rawQuery("select P.Id_PuntoControl,P.Nombre_PuntoControl,P.Id_Bloque,P.n_coordenadaX,P.n_coordenadaY from t_Puntocontrol as P, t_Bloque as B  where  P.Id_Bloque=B.Id_Bloque and B.Id_Huerta='"+Huerta+"' and P.c_codigo_eps=B.c_codigo_eps and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSPPto.add(new ItemDatoSpinner(Renglon.getString(0)+" -    "+Renglon.getString(1)+"     ("+Renglon.getString(2)+","+Renglon.getString(3)+")",Renglon.getString(0)));
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
        ItemSPOrg.add(new ItemDatoSpinner("Organo muestreado",""));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;
        if(rb_Enfermedad.isChecked()){
            Renglon=BD.rawQuery("select distinct D.Id_Deteccion,D.Nombre_Deteccion from t_Deteccion as D inner join t_Monitoreo as M on M.Id_Deteccion=D.Id_Deteccion where M.Id_zona='"+Zona+"' and M.Id_Plagas='' and M.Id_Enfermedad='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);
        }else{
            //Renglon=BD.rawQuery("select distinct D.Id_Deteccion,D.Nombre_Deteccion from t_Deteccion as D inner join t_Monitoreo as M on M.Id_Deteccion=D.Id_Deteccion where M.Id_zona='"+Zona+"' and M.Id_Plagas='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_Enfermedad=''",null); Quite para que salgan todos los Organos
            Renglon=BD.rawQuery("select distinct D.Id_Deteccion,D.Nombre_Deteccion from t_Deteccion as D ",null);
        }


        if(Renglon.moveToFirst()){

            do {
                ItemSPOrg.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1),Renglon.getString(0)));
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
        ItemSPInd.add(new ItemDatoSpinner("Est.Fenologico",""));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        String Consulta;
        if(rb_Enfermedad.isChecked()){
            //Renglon=BD.rawQuery("select I.Id_Fenologico,I.Nombre_Fenologico from t_Est_Fenologico as I where PoE='E' ",null);
            Renglon=BD.rawQuery("select M.Id_Fenologico,I.Nombre_Fenologico from t_Monitoreo as M inner join t_Est_Fenologico as I on I.Id_Fenologico=M.Id_Fenologico where M.Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_zona='"+Zona+"' and M.Id_Enfermedad='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and I.PoE='E' group by M.Id_Fenologico,I.Nombre_Fenologico,I.PoE",null);
        }else{
            Consulta="select Id_Fenologico,Nombre_Fenologico from t_Est_Fenologico where PoE='P'";
            //Consulta="select M.Id_Fenologico,I.Nombre_Fenologico from t_Monitoreo as M left join t_Est_Fenologico as I on I.Id_Fenologico=M.Id_Fenologico where M.Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_zona='"+Zona+"' and M.Id_Plagas='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' group by M.Id_Fenologico,I.Nombre_Fenologico"; Lo quite para que aparescan todas los estados fenologicos
            Renglon=BD.rawQuery(Consulta,null);
        }
        if(Renglon.moveToFirst()){

            do {
                ItemSPInd.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1),Renglon.getString(0)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            //Toast.makeText(this,"No hay datos en t_Monitoreo guardados",Toast.LENGTH_SHORT).show();
            BD.close();
        }
        }else{

        }
    }

    private boolean EscogerNivelInfeccion(int cantidad){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;
        if(rb_Enfermedad.isChecked()){
            Renglon=BD.rawQuery("select I.No_Inicial,I.No_Final,M.Id_Individuo,M.Id_Humbral from t_Monitoreo as M inner join t_Individuo as I on I.Id_Individuo=M.Id_Individuo where M.Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_zona='"+Zona+"' and M.Id_Plagas='' and M.Id_Enfermedad='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and Id_Fenologico='"+CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
        }else{
            Renglon=BD.rawQuery("select I.No_Inicial,I.No_Final,M.Id_Individuo,M.Id_Humbral from t_Monitoreo as M inner join t_Individuo as I on I.Id_Individuo=M.Id_Individuo where M.Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_zona='"+Zona+"' and M.Id_Plagas='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.Id_Enfermedad='' and Id_Fenologico='"+CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
        }
        if(Renglon.moveToFirst()){

            do {
                if(cantidad >= Renglon.getInt(0) && cantidad<=Renglon.getInt(1)){
                    Humbral=Renglon.getString(3);
                    Individuo=Renglon.getString(2);
                    return false;
                }

            } while(Renglon.moveToNext());
            Humbral="";
            Individuo="";
            BD.close();
            return false; //Regresar a true cuando se llene la tabla de niveles de infeccion
        }else{
            //Toast.makeText(this,"No hay datos en t_Monitoreo guardados",Toast.LENGTH_SHORT).show();
            BD.close();
            return false; //Regresar a true cuando se llene la tabla de niveles de infeccion
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
                            Toast.makeText(this,"Falta seleccionar su estado Fenologico.",Toast.LENGTH_SHORT).show();
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
                if(rb_SinPresencia.isChecked() || rb_Fumi.isChecked()){
                    FaltoAlgo=false;
                }else{
                    FaltoAlgo=true;
                    Toast.makeText(this,"Es necesario marcar 'Sin presencia' en caso de que no se detecte ninguna plaga o enfermedad.",Toast.LENGTH_SHORT).show();
                }
            }
            if (!FaltoAlgo) {
                if(rb_SinPresencia.isChecked() || rb_Fumi.isChecked()){

                }
                else{
                    FaltoAlgo = EscogerNivelInfeccion(Integer.parseInt(et_individuos.getText().toString()));
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

                Renglon=BD.rawQuery("select count(M.Id_PuntoControl) as Sihay,Hora from t_Monitoreo_PEEncabezado as M where M.Fecha='"+et_fecha.getText().toString().trim()+"' and M.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' ",null);
                if(Renglon.moveToFirst()){
                    do {
                        if (Renglon.getInt(0)>0){
                            currentTime=Renglon.getString(1);
                           /* if(rb_SinPresencia.isChecked()){

                            }else{*/
                                ContentValues registro = new ContentValues();

                                registro.put("n_coordenadaX",Local.Long);
                                registro.put("n_coordenadaY",Local.Lat);
                                registro.put("Observaciones",et_Observa.getText().toString().trim());

                                int cantidad=BD.update("t_Monitoreo_PEEncabezado",registro,"Fecha='"+et_fecha.getText().toString().trim()+"' and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"'",null);

                                if(cantidad>0){
                                    //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                                }else{
                                    //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                }
                            //}
                        }else{
                            ContentValues registro= new ContentValues();
                            registro.put("Fecha",et_fecha.getText().toString().trim());
                            registro.put("Id_Huerta",Huerta);
                            registro.put("Id_PuntoControl",CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));
                            registro.put("Id_Usuario",Usuario);
                            registro.put("F_UsuCrea",(objSDF.format(date1)));
                            registro.put("n_coordenadaX",Local.Long);
                            registro.put("n_coordenadaY",Local.Lat);
                            registro.put("Hora",currentTime);
                            registro.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2));
                            registro.put("Observaciones",et_Observa.getText().toString().trim());
                            if(rb_Fumi.isChecked()){
                                registro.put("Fumigado","1");
                            }else{
                                registro.put("Fumigado","0");
                            }
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
                            "and  Fecha='"+et_fecha.getText().toString().trim()+"' " +
                            "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                            "and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'" +
                            "and Id_Fenologico='"+CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);

                }else{
                    if(rb_Plaga.isChecked()){
                        Renglon =BD.rawQuery("select count(Id_PuntoControl) " +
                                "from t_Monitoreo_PEDetalle " +
                                "where Id_Plagas='"+CopiPE.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4)+"' and Id_Enfermedad='' " +
                                "and Id_Deteccion='"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                                "and  Fecha='"+et_fecha.getText().toString().trim()+"' " +
                                "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'" +
                                "and Id_Fenologico='"+CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
                    }else{
                        Renglon =BD.rawQuery("select count(Id_PuntoControl) " +
                                "from t_Monitoreo_PEDetalle " +
                                "where Id_Plagas=null and Id_Enfermedad=null " +
                                "and Id_Deteccion=null "+ // '"+CopiOrg.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4)+"' " +
                                "and  Fecha='"+et_fecha.getText().toString().trim()+"' " +
                                "and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"' " +
                                "and Id_Fenologico=null ",null);
                    }
                }

                if(Renglon.moveToFirst()){
                    if(Renglon.getInt(0)>0){
                        Toast.makeText(this,"Ya existe un dato en esta fecha con misma PE y Punto de control en esta fecha",Toast.LENGTH_SHORT).show();
                    }else{
                        if(rb_SinPresencia.isChecked() || rb_Fumi.isChecked()){
                           /* ContentValues registro2= new ContentValues();
                            registro2.put("Fecha",et_fecha.getText().toString().trim());
                            registro2.put("Id_PuntoControl",CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));
                            registro2.put("Id_Humbral","0001");
                            registro2.put("Hora",currentTime);
                            registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2));
                            BD.insert("t_Monitoreo_PEDetalle",null,registro2);*/
                        }else{
                            ContentValues registro2= new ContentValues();
                            registro2.put("Fecha",et_fecha.getText().toString().trim());
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
                            registro2.put("Id_Individuo",Individuo);
                            registro2.put("Id_Humbral",Humbral);
                            registro2.put("Hora",currentTime);
                            registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2));
                            registro2.put("Cant_Individuos",Integer.parseInt( et_individuos.getText().toString()));
                            registro2.put("Id_Fenologico",CopiInd.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,2));
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
                "\tM.Id_Fenologico, \n" +
                "\tM.Id_Plagas, \n" +
                "\tM.Id_Enfermedad,M.c_codigo_eps,EPS.v_abrevia_eps,M.Cant_Individuos \n" +
                "from t_Monitoreo_PEDetalle as M \n" +
                "inner join t_Puntocontrol as P on M.Id_PuntoControl=P.Id_PuntoControl and M.c_codigo_eps=P.c_codigo_eps \n" +
                "left join t_Deteccion as D on M.Id_Deteccion=D.Id_Deteccion \n" +
                "left join t_Plagas as Pl on M.Id_Plagas=Pl.Id_Plagas\n" +
                "left join t_Enfermedad as E on M.Id_enfermedad=E.Id_enfermedad\n" +
                "left join t_Individuo as I on I.Id_Individuo=M.Id_Individuo\n" +
                "left join conempresa as EPS on EPS.c_codigo_eps=M.c_codigo_eps\n" +
                "where M.Fecha='"+et_fecha.getText().toString()+"' and M.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' and M.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa2.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {
                do {
                    Tabla=new Itemmonitoreo(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(11),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8));
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
            button_UpFecha.setVisibility(View.VISIBLE);
        }else{
            button_UpFecha.setVisibility(View.INVISIBLE);
            Adapter=new Adaptador_GridMonitorio(getApplicationContext(),arrayArticulos);
            lv_GridMonitoreo.setAdapter(Adapter);
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Actualiza_Fecha(View view){

        Calendar c=Calendar.getInstance();

        dia=c.get(Calendar.DAY_OF_MONTH);
        mes=c.get(Calendar.MONTH);
        anio=c.get(Calendar.YEAR);

        DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> Actualiza_Fecha(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
        dtpd.show();

    }

    private void Actualiza_Fecha(String Fecha){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        ContentValues registro2= new ContentValues();
        int cantidad;

        registro2.put("Fecha", Fecha);
        cantidad=BD.update("t_Monitoreo_PEEncabezado",registro2,"Fecha='"+et_fecha.getText().toString().trim()+"' and Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4)+"' ",null);
        if (cantidad>0) {
            BD.update("t_Monitoreo_PEDetalle", registro2, "Fecha='" + et_fecha.getText().toString().trim() + "' and Id_PuntoControl='" + CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0, 4) + "' ", null);
            et_fecha.setText(Fecha);
        }else{
            Toast.makeText(this, "No se logro actualizar la fecha [RETURN0ROWSMONENC], notifique al administrador del sistema", Toast.LENGTH_SHORT).show();
        }
        BD.close();
    }
    private int Revisa_SiGuardado(){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

            Renglon=BD.rawQuery("select count(Id_PuntoControl) from t_Monitoreo_PEEncabezado as M  where M.Fecha='"+et_fecha.getText().toString().trim()+"' and M.Id_PuntoControl='"+CopiPto.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0, 4)+"' ",null);

        if(Renglon.moveToFirst()){
            do {
                return Renglon.getInt(0);
            } while(Renglon.moveToNext());
        }
        BD.close();
        return 0;
    }

}