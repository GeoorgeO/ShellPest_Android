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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Podas extends AppCompatActivity implements View.OnClickListener{

    EditText etd_Fecha2,etn_ApliCantidad,pt_Observaciones;
    Spinner sp_Empresa5,sp_huerta5,sp_Blo2,sp_Actividad;
    ListView lv_GridPodas;

    int dia,mes,anio,LineEmpresa,LineHuerta;

    private ArrayList<ItemDatoSpinner>  ItemSPBlq, ItemSPHue,ItemSPEmp,ItemSPAct;
    private AdaptadorSpinner CopiBlq,CopiHue,CopiEmp,CopiAct;

    String Usuario,Perfil,Id,Huerta;

    private ArrayAdapter Adaptador_Arreglos;
    private ArrayList<String> ArrayProductos,ArrayProductosLimpia;

    ItemPoda Tabla;
    Adaptador_GridPoda Adapter;
    ArrayList<ItemPoda> arrayArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podas);

        getSupportActionBar().hide();

        etd_Fecha2=(EditText) findViewById(R.id.etd_Fecha2);
        sp_Empresa5=(Spinner) findViewById(R.id.sp_Empresa5);
        sp_huerta5=(Spinner) findViewById(R.id.sp_huerta5);
        sp_Blo2=(Spinner) findViewById(R.id.sp_Blo2);
        etn_ApliCantidad=(EditText) findViewById(R.id.etn_ApliCantidad);
        pt_Observaciones=(EditText) findViewById(R.id.pt_Observaciones);
        sp_Actividad=(Spinner) findViewById(R.id.sp_Actividad);
        lv_GridPodas=(ListView) findViewById(R.id.lv_GridPodas);

        LineEmpresa=0;
        LineHuerta=0;

        arrayArticulos = new ArrayList<>();

        etd_Fecha2.setOnClickListener(this);

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        etd_Fecha2.setText(objSDF.format(date1));
        etd_Fecha2.setInputType(InputType.TYPE_NULL);
        etd_Fecha2.requestFocus();

        Usuario = getIntent().getStringExtra("usuario3");
        Perfil = getIntent().getStringExtra("perfil3");
        Id = getIntent().getStringExtra("ID");
        Huerta = getIntent().getStringExtra("huerta3");

        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa5.setAdapter(CopiEmp);

        if (sp_Empresa5.getCount()==2){
            sp_Empresa5.setSelection(1);
        }

        cargarActividades();
        CopiAct = new AdaptadorSpinner(this, ItemSPAct);
        sp_Actividad.setAdapter(CopiAct);

        sp_Empresa5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(ItemSPHue==null || ItemSPHue.size()<=1){
                    cargaSpinnerHuertas();
                    CopiHue = new AdaptadorSpinner(Podas.this, ItemSPHue);
                    sp_huerta5.setAdapter(CopiHue);

                    if (sp_huerta5.getCount()==2){
                        sp_huerta5.setSelection(1);
                    }
                }else{
                    if(LineEmpresa != i){
                        cargaSpinnerHuertas();
                        CopiHue = new AdaptadorSpinner(Podas.this, ItemSPHue);
                        sp_huerta5.setAdapter(CopiHue);

                        if (LineHuerta > 0) {
                            //sp_huerta.setSelection(LineHuerta);
                        } else {
                            if (sp_huerta5.getCount() == 2) {
                                sp_huerta5.setSelection(1);
                            }
                        }
                    }
                    if(LineEmpresa == i && LineHuerta>0){
                        sp_huerta5.setSelection(LineHuerta);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_huerta5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0 && Id==null){
                    Huerta=CopiHue.getItem(i).getTexto().substring(0, 5);

                    cargaSpinnerBlq();
                    CopiBlq = new AdaptadorSpinner(getApplicationContext(), ItemSPBlq);
                    sp_Blo2.setAdapter(CopiBlq);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Blo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Podas.this,"ShellPest",null,1);
                SQLiteDatabase BD=SQLAdmin.getReadableDatabase();

                Cursor Renglon10;
                String Consulta;

                Consulta="select E.N_arboles,E.Observaciones from t_Podas as E  where E.Fecha='"+etd_Fecha2.getText().toString().trim()+"' and E.Id_bloque='"+CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4)+"' and E.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' ";
                Renglon10=BD.rawQuery(Consulta,null);

                if(Renglon10.moveToFirst()){

                    do {
                        etn_ApliCantidad.setText(Renglon10.getString(0));
                        pt_Observaciones.setText(Renglon10.getString(1));

                        Cargagrid(etd_Fecha2.getText().toString().trim(),CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4),CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
                    } while(Renglon10.moveToNext());

                    BD.close();
                }else{
                    etn_ApliCantidad.setText("");
                    pt_Observaciones.setText("");
                    lv_GridPodas.setAdapter(null);
                    arrayArticulos.clear();

                    BD.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv_GridPodas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Podas.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar el registro seleccionado ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();

                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Podas.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                        //BD.beginTransaction();

                        int cantidad= BD.delete("t_PodasDet","Fecha='"+arrayArticulos.get(i).getFecha()+"' and Id_bloque='"+arrayArticulos.get(i).getId_Bloque()+"' and actividad='"+arrayArticulos.get(i).getC_codigo_act()+"' and c_codigo_eps='"+arrayArticulos.get(i).getC_codigo_eps()+"' ",null);

                        if(cantidad>0){

                        }else{
                            Toast.makeText(Podas.this,arrayArticulos.get(i).getFecha(),Toast.LENGTH_SHORT).show();
                        }
                        //BD.endTransaction();
                        BD.close();
                        Cargagrid(etd_Fecha2.getText().toString().trim(),CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4),CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
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

    @Override
    public void onClick(View view) {
        if(view==etd_Fecha2){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> etd_Fecha2.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
            sp_Blo2.setSelection(0);
        }
    }

    private void cargaSpinnerEmpresa(){
        CopiEmp=null;

        ItemSPEmp=new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon10;
        String Consulta;

        Consulta="select E.c_codigo_eps,E.v_abrevia_eps from conempresa as E inner join t_Usuario_Empresa as UE on UE.c_codigo_eps=E.c_codigo_eps where ltrim(rtrim(UE.Id_Usuario))='"+Usuario+"' ";
        Renglon10=BD.rawQuery(Consulta,null);

        if(Renglon10.moveToFirst()){

            do {
                ItemSPEmp.add(new ItemDatoSpinner(Renglon10.getString(0)+" - "+Renglon10.getString(1)));
            } while(Renglon10.moveToNext());

            BD.close();
        }else{
            BD.close();
        }
    }

    private void cargaSpinnerHuertas(){

        CopiHue = null;

        ItemSPHue = new ArrayList<>();
        ItemSPHue.add(new ItemDatoSpinner("Huerta"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }else{
            Renglon=BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }

        if (Renglon.moveToFirst()) {
            do {
                ItemSPHue.add(new ItemDatoSpinner(Renglon.getString(0) + " - " + Renglon.getString(1)));
            } while (Renglon.moveToNext());

            BD.close();
        } else {
            BD.close();
        }
    }

    private void cargaSpinnerBlq(){
        CopiBlq=null;

        ItemSPBlq=new ArrayList<>();
        ItemSPBlq.add(new ItemDatoSpinner("Bloque"));
        if(Huerta.length()>0 && !Huerta.equals("NULL")){

            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            String Consulta;
            Consulta="select B.Id_Bloque,B.Nombre_Bloque from t_Bloque as B  where B.TipoBloque='B' and B.Id_Huerta='"+Huerta+"' and B.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'";
            Renglon=BD.rawQuery(Consulta,null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSPBlq.add(new ItemDatoSpinner(Renglon.getString(0)+" -    "+Renglon.getString(1)));
                } while(Renglon.moveToNext());
            }else{
                Toast.makeText(this,"No se encontraron datos en Bloques",Toast.LENGTH_SHORT).show();
                BD.close();
            }
            BD.close();
        }else{

        }
    }

    private void cargarActividades(){
        CopiAct=null;

        ItemSPAct=new ArrayList<>();
        ItemSPAct.add(new ItemDatoSpinner("Actividad de poda"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select c_codigo_act,v_nombre_act from cosactividad   ",null);

        if(Renglon.moveToFirst()){
            do {
                ItemSPAct.add(new ItemDatoSpinner(Renglon.getString(0)+" -    "+Renglon.getString(1)));
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No se encontraron datos en Bloques",Toast.LENGTH_SHORT).show();
            BD.close();
        }
        BD.close();

    }

    private void Cargagrid(String Fecha,String Id_Bloque,String c_codigp_eps){
        lv_GridPodas.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;


        Cursor Renglon =BD.rawQuery("select PD.Fecha," +
                    "PD.Id_bloque, " +
                    "PD.actividad," +
                    "ACT.v_nombre_act," +
                    "PD.c_codigo_eps " +
                "from t_PodasDet as PD " +
                "inner join t_Bloque as BLQ on BLQ.Id_Bloque=PD.Id_Bloque and BLQ.c_codigo_eps=PD.c_codigo_eps " +
                "inner join  cosactividad as ACT on ACT.c_codigo_act=PD.actividad",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemPoda(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());

                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_podas guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridPoda(getApplicationContext(),arrayArticulos);
            lv_GridPodas.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void InsertarPodas (View view){
        boolean FaltoAlgo;
        FaltoAlgo=false;
        String Mensaje;
        Mensaje = "";

        if (sp_Empresa5.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una empresa,Verifica por favor";
        }
        if (sp_huerta5.getSelectedItemPosition() > 0 ) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una huerta,Verifica por favor";
        }
        if (sp_Blo2.getSelectedItemPosition() > 0 ) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar un bloque,Verifica por favor";
        }

        try {
            if (Double.parseDouble(etn_ApliCantidad.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de arboles,Verifica por favor";
            }
        } catch (IllegalStateException e) {
            etn_ApliCantidad.setText("0");
        }
        catch(NumberFormatException f)
        {
            etn_ApliCantidad.setText("0");
        }
        catch(Exception g)
        {
            etn_ApliCantidad.setText("0");
        }

        if (sp_Actividad.getSelectedItemPosition() > 0 ) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta teclear una actividad de poda ,Verifica por favor";
        }

        if (!FaltoAlgo){
            if (guardarEncabezado()){
                guardarDetalle(etd_Fecha2.getText().toString(),CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4),CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2),CopiAct.getItem(sp_Actividad.getSelectedItemPosition()).getTexto().substring(0,4));
                Cargagrid(etd_Fecha2.getText().toString(),CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4),CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
            }

        }else{
            Toast.makeText(this,Mensaje,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean guardarEncabezado(){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());

        Cursor Renglon;
        Renglon=BD.rawQuery("select count(P.Id_bloque) as Sihay from t_Podas as P where P.Fecha='"+etd_Fecha2.getText().toString()+"' and P.Id_bloque='"+CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4)+"' and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){
                    ContentValues registro = new ContentValues();

                    registro.put("N_arboles", Integer.parseInt(etn_ApliCantidad.getText().toString()));
                    registro.put("Observaciones",pt_Observaciones.getText().toString());
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    int cantidad=BD.update("t_Podas",registro,"Fecha='"+etd_Fecha2.getText().toString().trim()+"' and Id_bloque='"+CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                    return true;
                }else{
                    ContentValues registro= new ContentValues();

                    registro.put("Fecha",etd_Fecha2.getText().toString().trim());
                    registro.put("Id_bloque",CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4));
                    registro.put("N_arboles",Integer.parseInt(etn_ApliCantidad.getText().toString()));
                    registro.put("Observaciones",pt_Observaciones.getText().toString());
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    registro.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
                    BD.insert("t_Podas",null,registro);

                    return true;
                }
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean guardarDetalle(String Fecha,String Id_bloque,String c_codigo_eps,String actividad){

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        Cursor Renglon;

        Renglon =BD.rawQuery("select count(Id_bloque) " +
                "from t_PodasDet " +
                "where Fecha='"+Fecha+"' and actividad='"+actividad+"' " +
                "and Id_bloque='"+Id_bloque+"' and c_codigo_eps='"+c_codigo_eps+"' " ,null);
        if(Renglon.moveToFirst()) {

                if (Renglon.getInt(0) > 0) {

                } else {
                    ContentValues registro2 = new ContentValues();
                    registro2.put("Fecha", Fecha);
                    registro2.put("Id_bloque", Id_bloque);
                    registro2.put("c_codigo_eps", c_codigo_eps);
                    registro2.put("actividad", actividad);
                    BD.insert("t_PodasDet", null, registro2);
                }

        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Detalle",Toast.LENGTH_SHORT).show();

        }
        BD.close();
        return true;
    }

}