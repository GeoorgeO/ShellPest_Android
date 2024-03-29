package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Riego extends AppCompatActivity implements View.OnClickListener{

    public String Usuario, Perfil, Huerta;

    TextView et_fecha,tv_Valvula;
    Spinner sp_Blq, sp_Hue,sp_Empresa3,sp_Cam;
    EditText txt_Precipitacion,txt_CaudalIni,txt_CaudalFin,txt_Riego,txt_Temperatura,txt_ET;
    ListView lv_GridRiego;

    int LineHuerta,LineEmpresa;

    Boolean SoloUnaHuerta;
    private AdaptadorSpinner CopiHue,CopiBlq,CopiEmp,CopiCamb;
    private ArrayList<ItemDatoSpinner> ItemSPHue,ItemSPBlq,ItemSPEmp,ItemSPCamb;

    int dia,mes,anio;

    String[] ADatosVal,AdatosClean,AValorVal;
    boolean[] AsinoVal,AsinoClean;

    ItemRiego Tabla;
    Adaptador_GridRiego Adapter;
    ArrayList<ItemRiego> arrayArticulos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riego);
        getSupportActionBar().hide();

        SoloUnaHuerta=false;

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        et_fecha = (TextView) findViewById(R.id.et_fecha);
        sp_Hue = (Spinner) findViewById(R.id.sp_Hue);
        sp_Blq = (Spinner) findViewById(R.id.sp_Blo);
        sp_Empresa3 = (Spinner) findViewById(R.id.sp_Empresa3);
        sp_Cam = (Spinner) findViewById(R.id.sp_Cam);

        txt_Precipitacion=(EditText) findViewById(R.id.txt_Precipitacion);
        txt_CaudalIni=(EditText) findViewById(R.id.txt_CaudalIni);
        txt_CaudalFin=(EditText) findViewById(R.id.txt_CaudalFin);
        txt_Riego=(EditText) findViewById(R.id.txt_Riego);
        txt_Temperatura=(EditText) findViewById(R.id.txt_Temperatura);
        txt_ET=(EditText) findViewById(R.id.txt_ET);

        lv_GridRiego=(ListView) findViewById(R.id.lv_GridRiego);

        tv_Valvula = (TextView) findViewById(R.id.tv_Valvula);

        LineHuerta=0;
        LineEmpresa=0;

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = objDate;
        et_fecha.setText(objSDF.format(date1));

        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa3.setAdapter(CopiEmp);

        if (sp_Empresa3.getCount()==2){
            sp_Empresa3.setSelection(1);
        }

        /*cargaSpinnerHue();
        CopiHue = new AdaptadorSpinner(Riego.this, ItemSPHue);
        // CopiHue=AdaptadorSpiner;
        sp_Hue.setAdapter(CopiHue);*/

        ItemSPBlq = new ArrayList<>();
        ItemSPBlq.add(new ItemDatoSpinner("Bloque",""));
        CopiBlq = new AdaptadorSpinner(Riego.this, ItemSPBlq);
        sp_Blq.setAdapter(CopiBlq);

        et_fecha.setOnClickListener(this);

        arrayArticulos = new ArrayList<>();

        sp_Empresa3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //if(ItemSPHue==null){
                    cargaSpinnerHue();
                    CopiHue = new AdaptadorSpinner(Riego.this, ItemSPHue);
                    sp_Hue.setAdapter(CopiHue);

                    if (sp_Hue.getCount()==2 || SoloUnaHuerta==true){
                        if(SoloUnaHuerta==true){

                        }else{
                            sp_Hue.setSelection(1);
                        }

                        Cargagrid();
                    }else{
                        if (sp_Hue.getCount()<=1){
                            ItemSPHue = new ArrayList<>();
                            ItemSPHue.add(new ItemDatoSpinner("Huerta",""));
                            CopiHue = new AdaptadorSpinner(Riego.this, ItemSPHue);
                            sp_Hue.setAdapter(CopiHue);
                        }
                    }
                /*}else{
                    if((ItemSPHue.size()<=1  ) || LineEmpresa!=i){
                        cargaSpinnerHue();
                        CopiHue = new AdaptadorSpinner(Riego.this, ItemSPHue);
                        sp_Hue.setAdapter(CopiHue);


                        if(LineHuerta>0){
                            LineHuerta=0;
                        }else{
                            if (sp_Hue.getCount()==2){
                                sp_Hue.setSelection(1);
                            }else{
                                if (sp_Hue.getCount()<=1){
                                    ItemSPHue = new ArrayList<>();
                                    ItemSPHue.add(new ItemDatoSpinner("Huerta"));
                                    CopiHue = new AdaptadorSpinner(Riego.this, ItemSPHue);
                                    sp_Hue.setAdapter(CopiHue);
                                }
                            }
                        }
                    }
                }*/

                if(i>0){
                    Cargagrid();
                }else{
                    lv_GridRiego.setAdapter(null);
                    arrayArticulos.clear();
                }

                /*cargaSpinnerHue();
                CopiHue = new AdaptadorSpinner(Riego.this, ItemSPHue);
                // CopiHue=AdaptadorSpiner;
                sp_Hue.setAdapter(CopiHue);*/
                LineEmpresa=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Hue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Notify the selected item text
                Huerta = CopiHue.getItem(i).getTexto().substring(0, 5);

                cargaSpinnerBlq();
                CopiBlq = new AdaptadorSpinner(getApplicationContext(), ItemSPBlq);
                sp_Blq.setAdapter(CopiBlq);
                
                LineHuerta=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Blq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargaSpinnerCambio(CopiBlq.getItem(sp_Blq.getSelectedItemPosition()).getTexto().substring(0,4));
                CopiCamb = new AdaptadorSpinner(Riego.this, ItemSPCamb);
                sp_Cam.setAdapter(CopiCamb);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_Cam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarAValvulas(CopiCamb.getItem(sp_Cam.getSelectedItemPosition()).getValor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_fecha.setInputType(InputType.TYPE_NULL);
        et_fecha.requestFocus();

        lv_GridRiego.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Riego.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar los datos de riego del bloque: "+arrayArticulos.get(i).getNombre_Bloque()+". ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();
                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        String currentTime = simpleDateFormat.format(new Date());

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Riego.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                        //BD.beginTransaction();
                        Cursor Renglon =BD.rawQuery("select Fecha ,Hora,Id_Bloque,Precipitacion_Sistema,Caudal_Inicio,Caudal_Fin,Horas_Riego,Id_Usuario,F_UsuCrea,c_codigo_eps from t_Riego where Fecha='"+et_fecha.getText().toString().trim()+"' and Id_Bloque='"+arrayArticulos.get(i).getId_Bloque()+"' and c_codigo_eps='"+arrayArticulos.get(i).getCEPS()+"'  ",null);

                        if (Renglon.moveToFirst()) {

                            ContentValues registro= new ContentValues();
                            do {
                                registro.put("Fecha",Renglon.getString(0));
                                registro.put("Hora",currentTime);
                                registro.put("Id_Bloque",Renglon.getString(2));
                                registro.put("Precipitacion_Sistema",Renglon.getString(3));
                                registro.put("Caudal_Inicio",Renglon.getString(4));
                                registro.put("Caudal_Fin",Renglon.getString(5));
                                registro.put("Horas_Riego",Renglon.getString(6));
                                registro.put("Id_Usuario",Renglon.getString(7));
                                registro.put("F_UsuCrea",Renglon.getString(8));
                                registro.put("c_codigo_eps",Renglon.getString(9));
                                BD.insert("t_RiegoEliminado",null,registro);

                                int cantidad= BD.delete("t_Riego","Fecha='"+et_fecha.getText().toString().trim()+"' and Id_Bloque='"+arrayArticulos.get(i).getId_Bloque()+"' ",null);

                                if(cantidad>0){

                                }else{
                                    Toast.makeText(Riego.this,"Ocurrio un error al intentar eliminar riego del bloque, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                }
                            } while (Renglon.moveToNext());


                        } else {
                            //Toast.makeText(Puntos_Capturados.this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                        }
                        BD.close();
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

        tv_Valvula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(
                        Riego.this
                );
                builder.setTitle("Valvulas");
                builder.setCancelable(false);
                builder. setMultiChoiceItems(ADatosVal, AsinoVal, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                            AsinoVal[i]=b;

                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Limpiar todo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j=0;j<AsinoVal.length;j++){
                            AsinoVal[j]=false;
                        }
                    }
                });
                builder.show();

            }
        });


    }

    private void cargarAValvulas(String Id_Cambio){

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select CRD.Id_Valvula,V.N_Valvula " +
                "from  t_Cambio_Riego_Det as CRD  " +
                "inner join t_Valvulas as V on CRD.Id_Valvula=V.Id_Valvula and CRD.Id_Bloque=V.Id_Bloque " +
                "where CRD.Id_Cambio='"+Id_Cambio+"' ",null);

        if(Renglon.moveToFirst()){
            int tamanio;
            tamanio=0;
            ADatosVal=new String[Renglon.getCount()];
            AValorVal=new String[Renglon.getCount()];
            AsinoVal=new boolean[Renglon.getCount()];
            do {
                ADatosVal[tamanio] =Renglon.getString(1);
                AValorVal[tamanio] =Renglon.getString(0);
                AsinoVal[tamanio]=true;
                tamanio++;
            } while(Renglon.moveToNext());

            BD.close();



        }else{
            BD.close();
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

    private void cargaSpinnerHue(){
        CopiHue=null;
        ItemSPHue=new ArrayList<>();

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }else{
            Renglon=BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
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

        /*if(Perfil.equals("001")){
            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
        }else{
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and Id_Huerta='"+Huerta+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
            sp_Hue.setEnabled(false);
        }*/

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

    private void cargaSpinnerBlq(){
        CopiBlq=null;

        ItemSPBlq=new ArrayList<>();
        ItemSPBlq.add(new ItemDatoSpinner("Bloque",""));

        if(Huerta.length()>0 && !Huerta.equals("NULL")){
            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            String Consulta;
            Consulta="select B.Id_Bloque,B.Nombre_Bloque from t_Bloque as B  where B.TipoBloque='R' and B.Id_Huerta='"+Huerta+"' and B.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'";
            Renglon=BD.rawQuery(Consulta,null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSPBlq.add(new ItemDatoSpinner(Renglon.getString(0)+" -    "+Renglon.getString(1),Renglon.getString(0)));
                } while(Renglon.moveToNext());
            }else{
                Toast.makeText(this,"No se encontraron datos en Bloques",Toast.LENGTH_SHORT).show();
                BD.close();
            }
            BD.close();
        }else{

        }
    }

    private void cargaSpinnerCambio(String Id_Bloque ){
        CopiCamb=null;

        ItemSPCamb=new ArrayList<>();
        ItemSPCamb.add(new ItemDatoSpinner("Cambio",""));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select CRD.Id_Cambio,CRD.N_Cambio from t_Cambio_Riego_Det as CRD  where CRD.Id_Bloque='"+Id_Bloque+"' group by CRD.Id_Cambio ",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPCamb.add(new ItemDatoSpinner(Renglon.getString(1),Renglon.getString(0)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }
    }

    private void LimpiarCampos(){
        txt_Precipitacion.setText("0");
        txt_CaudalIni.setText("0");
        txt_CaudalFin.setText("0");
        txt_Riego.setText("0");
    }

    public void AddRenglon(View view){
        if(txt_Precipitacion.getText().length()==0){
            txt_Precipitacion.setText("0");
        }
        if(txt_CaudalIni.getText().length()==0){
            txt_CaudalIni.setText("0");
        }
        if(txt_CaudalFin.getText().length()==0){
            txt_CaudalFin.setText("0");
        }
        if(txt_Riego.getText().length()==0){
            txt_Riego.setText("0");
        }
        if(txt_Temperatura.getText().length()==0){
            txt_Temperatura.setText("0");
        }
        if(txt_ET.getText().length()==0){
            txt_ET.setText("0");
        }
        if(sp_Blq.getSelectedItemPosition()>0){
            boolean FaltoAlgo;
            FaltoAlgo=false;
            if(Double.parseDouble(String.valueOf(txt_Precipitacion.getText()))>=0){
                if(Double.parseDouble(String.valueOf(txt_CaudalIni.getText()))>=0){
                    if(Double.parseDouble(String.valueOf(txt_CaudalFin.getText()))>=0){
                        if(Double.parseDouble(String.valueOf(txt_Riego.getText()))>=0){
                            if(Double.parseDouble(String.valueOf(txt_Temperatura.getText()))>0){
                                if(Double.parseDouble(String.valueOf(txt_ET.getText()))>0){
                                    FaltoAlgo=false;
                                }else{
                                    FaltoAlgo=true;
                                    Toast.makeText(this,"Falta agregar el ET.",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                FaltoAlgo=true;
                                Toast.makeText(this,"Falta agregar la temperatura",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            FaltoAlgo=true;
                            Toast.makeText(this,"Falta agregar horas de riego.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        FaltoAlgo=true;
                        Toast.makeText(this,"Falta agregar el caudal Final",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    FaltoAlgo=true;
                    Toast.makeText(this,"Falta agregar el caudal Inicial",Toast.LENGTH_SHORT).show();
                }

            }else{
                FaltoAlgo=true;
                Toast.makeText(this,"Falta agregar la precipitación",Toast.LENGTH_SHORT).show();
            }
            if (!FaltoAlgo){

                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                Date date1=objDate;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String currentTime = simpleDateFormat.format(new Date());

                Cursor Renglon;

                Renglon =BD.rawQuery("select count(Id_Bloque) " +
                            "from t_Riego " +
                            "where Fecha='"+et_fecha.getText().toString().trim()+"' and Id_Bloque='"+CopiBlq.getItem(sp_Blq.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                if(Renglon.moveToFirst()){
                    if(Renglon.getInt(0)>0){
                       // Toast.makeText(this,"Ya existen datos capturados en ese bloque, deseas sobreescribirlos?",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Riego.this);
                        dialogo1.setTitle("Dato ya ingresado");
                        dialogo1.setMessage("¿ Ya existen datos capturados en ese bloque, deseas sobreescribirlos ?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //aceptar();

                                ContentValues registro2= new ContentValues();

                                registro2.put("Hora",currentTime);
                                registro2.put("Precipitacion_Sistema",Double.parseDouble(String.valueOf(txt_Precipitacion.getText())));
                                registro2.put("Caudal_Inicio",Double.parseDouble(String.valueOf(txt_CaudalIni.getText())));
                                registro2.put("Caudal_Fin",Double.parseDouble(String.valueOf(txt_CaudalFin.getText())));
                                registro2.put("Horas_Riego",Double.parseDouble(String.valueOf(txt_Riego.getText())));
                                registro2.put("Id_Usuario",Usuario);
                                registro2.put("F_UsuCrea",objSDF.format(date1));
                                registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2));
                                registro2.put("Temperatura",Double.parseDouble(String.valueOf(txt_Temperatura.getText())));
                                registro2.put("ET",Double.parseDouble(String.valueOf(txt_ET.getText())));
                                BD.update("t_Riego",registro2,"Fecha='"+et_fecha.getText().toString().trim()+"' and Id_Bloque='"+CopiBlq.getItem(sp_Blq.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
                                BD.close();
                                Cargagrid();
                            }

                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //cancelar();
                                BD.close();
                            }
                        });
                        dialogo1.show();

                    }
                    else{
                        ContentValues registro2= new ContentValues();
                        registro2.put("Fecha",et_fecha.getText().toString().trim());
                        registro2.put("Hora",currentTime);
                        registro2.put("Id_Bloque",CopiBlq.getItem(sp_Blq.getSelectedItemPosition()).getTexto().substring(0,4));

                        registro2.put("Precipitacion_Sistema",Double.parseDouble(String.valueOf(txt_Precipitacion.getText())));
                        registro2.put("Caudal_Inicio",Double.parseDouble(String.valueOf(txt_CaudalIni.getText())));
                        registro2.put("Caudal_Fin",Double.parseDouble(String.valueOf(txt_CaudalFin.getText())));
                        registro2.put("Horas_Riego",Double.parseDouble(String.valueOf(txt_Riego.getText())));
                        registro2.put("Id_Usuario",Usuario);
                        registro2.put("F_UsuCrea",objSDF.format(date1));
                        registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2));
                        registro2.put("Temperatura",Double.parseDouble(String.valueOf(txt_Temperatura.getText())));
                        registro2.put("ET",Double.parseDouble(String.valueOf(txt_ET.getText())));
                        BD.insert("t_Riego",null,registro2);
                        BD.close();
                        Cargagrid();
                    }

                }else{
                    Toast.makeText(this,"No Regreso nada la consulta de Riego",Toast.LENGTH_SHORT).show();
                    BD.close();
                }
            }

        }else{
            Toast.makeText(Riego.this,"Falta seleccionar un punto de control",Toast.LENGTH_SHORT).show();
        }
    }

    public void cerrar(View view ){
        this.onBackPressed();
    }

    private void Cargagrid(){
        lv_GridRiego.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select R.Fecha, \n" +
                "\tR.Hora,\n" +
                "\tB.Nombre_Bloque, \n" +
                "\tR.Id_Bloque, \n" +
                "\tR.Precipitacion_Sistema, \n" +
                "\tR.Caudal_Inicio, \n" +
                "\tR.Caudal_Fin, \n" +
                "\tR.Horas_Riego, R.c_codigo_eps, R.Temperatura, R.ET \n" +
                "from t_Riego as R \n" +
                "left join t_Bloque as B on B.Id_Bloque=R.Id_Bloque and B.c_codigo_eps=R.c_codigo_eps \n" +
                "where R.Fecha='"+et_fecha.getText().toString().trim()+"' and R.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemRiego(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridRiego(getApplicationContext(),arrayArticulos);
            lv_GridRiego.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        if(view==et_fecha){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> et_fecha.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
            LineEmpresa=0;
            sp_Empresa3.setSelection(0);

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

}