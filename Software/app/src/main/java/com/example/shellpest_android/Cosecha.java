package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Cosecha extends AppCompatActivity implements View.OnClickListener{

    EditText etd_Fecha2,et_Bico,etn_Cosecha,text_DesechoCap,text_PepenaCap,text_DiariaCap;
    Spinner sp_Empresa5,sp_huerta5,sp_Blo2;
    ListView lv_GridCosecha;

    private ArrayList<ItemDatoSpinner> ItemSPBlq, ItemSPHue,ItemSPEmp;
    private AdaptadorSpinner CopiBlq,CopiHue,CopiEmp;

    int dia,mes,anio,LineHuerta,LineEmpresa;

    String Usuario,Perfil,Id,Huerta;

    ItemCosecha Tabla;
    Adaptador_GridCosecha Adapter;
    ArrayList<ItemCosecha> arrayArticulos;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosecha);

        getSupportActionBar().hide();

        etd_Fecha2=(EditText) findViewById(R.id.etd_Fecha2);
        sp_Empresa5=(Spinner) findViewById(R.id.sp_Empresa5);
        sp_huerta5=(Spinner) findViewById(R.id.sp_huerta5);
        sp_Blo2=(Spinner) findViewById(R.id.sp_Blo2);
        et_Bico=(EditText) findViewById(R.id.et_Bico);
        etn_Cosecha=(EditText) findViewById(R.id.etn_Cosecha);
        text_DesechoCap=(EditText) findViewById(R.id.text_DesechoCap);
        text_PepenaCap=(EditText) findViewById(R.id.text_PepenaCap);
        text_DiariaCap=(EditText) findViewById(R.id.text_DiariaCap);
        lv_GridCosecha=(ListView) findViewById(R.id.lv_GridCosecha);

        etd_Fecha2.setOnClickListener(this);

        arrayArticulos = new ArrayList<>();

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        etd_Fecha2.setText(objSDF.format(date1));
        etd_Fecha2.setInputType(InputType.TYPE_NULL);
        etd_Fecha2.requestFocus();

        Usuario = getIntent().getStringExtra("usuario3");
        Perfil = getIntent().getStringExtra("perfil3");
        Id = getIntent().getStringExtra("ID");

        LineHuerta=0;
        LineEmpresa=0;

        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa5.setAdapter(CopiEmp);

        if (sp_Empresa5.getCount()==2){
            sp_Empresa5.setSelection(1);
        }


        sp_Empresa5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(ItemSPHue==null || ItemSPHue.size()<=1){
                    Cargagrid(etd_Fecha2.getText().toString(),CopiEmp.getItem(i).getTexto().substring(0,2));
                    cargaSpinnerHuertas();
                    CopiHue = new AdaptadorSpinner(Cosecha.this, ItemSPHue);
                    sp_huerta5.setAdapter(CopiHue);

                    if (sp_huerta5.getCount()==2){
                        sp_huerta5.setSelection(1);
                    }
                }else{
                    if(LineEmpresa != i){
                        Cargagrid(etd_Fecha2.getText().toString(),CopiEmp.getItem(i).getTexto().substring(0,2));
                        cargaSpinnerHuertas();
                        CopiHue = new AdaptadorSpinner(Cosecha.this, ItemSPHue);
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
                LineEmpresa=i;
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
            Consulta="select B.Id_Bloque,B.Nombre_Bloque from t_Bloque as B  where B.TipoBloque='R' and B.Id_Huerta='"+Huerta+"' and B.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'";
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

    public void InsertarCosecha (View view){
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

        if(et_Bico.getText().toString().length()==0){
            FaltoAlgo = true;
            Mensaje = "Falta teclear el número de BICO, Verifica por favor";
        }

        try {
            if (Double.parseDouble(etn_Cosecha.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de cajas cosechadas, Verifica por favor";
            }
        } catch (IllegalStateException e) {
            etn_Cosecha.setText("0");
        }
        catch(NumberFormatException f)
        {
            etn_Cosecha.setText("0");
        }
        catch(Exception g)
        {
            etn_Cosecha.setText("0");
        }

        try {
            if (Double.parseDouble(text_DesechoCap.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de cajas desecho, Verifica por favor";
            }
        } catch (IllegalStateException e) {
            text_DesechoCap.setText("0");
        }
        catch(NumberFormatException f)
        {
            text_DesechoCap.setText("0");
        }
        catch(Exception g)
        {
            text_DesechoCap.setText("0");
        }

        try {
            if (Double.parseDouble(text_PepenaCap.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de cajas pepena, Verifica por favor";
            }
        } catch (IllegalStateException e) {
            text_PepenaCap.setText("0");
        }
        catch(NumberFormatException f)
        {
            text_PepenaCap.setText("0");
        }
        catch(Exception g)
        {
            text_PepenaCap.setText("0");
        }

        try {
            if (Double.parseDouble(text_DiariaCap.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de cajas recolección diaria, Verifica por favor";
            }
        } catch (IllegalStateException e) {
            text_DiariaCap.setText("0");
        }
        catch(NumberFormatException f)
        {
            text_DiariaCap.setText("0");
        }
        catch(Exception g)
        {
            text_DiariaCap.setText("0");
        }



        if (!FaltoAlgo){
            if (guardarEncabezado()){

                Cargagrid(etd_Fecha2.getText().toString().trim(),CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
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
        Renglon=BD.rawQuery("select count(P.Id_bloque) as Sihay from t_Cosecha as P where P.Fecha='"+etd_Fecha2.getText().toString()+"' and P.Id_bloque='"+CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4)+"' and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){
                    ContentValues registro = new ContentValues();

                    registro.put("BICO", et_Bico.getText().toString());
                    registro.put("Cajas_Cosecha",Integer.parseInt(etn_Cosecha.getText().toString()));
                    registro.put("Cajas_Desecho",Integer.parseInt(text_DesechoCap.getText().toString()));
                    registro.put("Cajas_Pepena",Integer.parseInt(text_PepenaCap.getText().toString()));
                    registro.put("Cajas_RDiaria",Integer.parseInt(text_DiariaCap.getText().toString()));
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    int cantidad=BD.update("t_Cosecha",registro,"Fecha='"+etd_Fecha2.getText().toString().trim()+"' and Id_bloque='"+CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                    return true;
                }else{
                    ContentValues registro= new ContentValues();

                    registro.put("Fecha",etd_Fecha2.getText().toString().trim());
                    registro.put("Id_bloque",CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4));
                    registro.put("BICO", et_Bico.getText().toString());
                    registro.put("Cajas_Cosecha",Integer.parseInt(etn_Cosecha.getText().toString()));
                    registro.put("Cajas_Desecho",Integer.parseInt(text_DesechoCap.getText().toString()));
                    registro.put("Cajas_Pepena",Integer.parseInt(text_PepenaCap.getText().toString()));
                    registro.put("Cajas_RDiaria",Integer.parseInt(text_DiariaCap.getText().toString()));
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    registro.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
                    BD.insert("t_Cosecha",null,registro);

                    return true;
                }
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void Cargagrid(String Fecha,String c_codigo_eps){
        lv_GridCosecha.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;


        Cursor Renglon =BD.rawQuery("select PD.Fecha," +
                "PD.Id_bloque, " +
                "BLQ.Nombre_Bloque, " +
                "PD.c_codigo_eps, " +
                "PD.BICO," +
                "PD.Cajas_Cosecha," +
                "PD.Cajas_Desecho," +
                "PD.Cajas_Pepena," +
                "PD.Cajas_RDiaria " +
                "from t_Cosecha as PD " +
                "inner join t_Bloque as BLQ on BLQ.Id_Bloque=PD.Id_Bloque and BLQ.c_codigo_eps=PD.c_codigo_eps " +
                "where PD.Fecha='"+Fecha+"' and PD.c_codigo_eps='"+c_codigo_eps+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemCosecha(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());

                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_podas guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridCosecha(getApplicationContext(),arrayArticulos);
            lv_GridCosecha.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

}