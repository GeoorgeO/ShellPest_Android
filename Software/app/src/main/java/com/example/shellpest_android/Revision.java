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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Revision extends AppCompatActivity implements View.OnClickListener {

    EditText etd_Fecha2,etn_Narboles,etn_Nhume,pt_Observaciones;
    Spinner sp_Empresa5,sp_huerta5,sp_Blo2;
    ListView lv_GridRevision;
    RadioButton rb_Fsi,rb_Fno,rb_Asi,rb_Ano;

    int dia,mes,anio,LineEmpresa,LineHuerta;

    private ArrayList<ItemDatoSpinner> ItemSPBlq, ItemSPHue,ItemSPEmp;
    private AdaptadorSpinner CopiBlq,CopiHue,CopiEmp;

    String Usuario,Perfil,Id,Huerta;

    private ArrayAdapter Adaptador_Arreglos;
    private ArrayList<String> ArrayProductos,ArrayProductosLimpia;

    ItemRevision Tabla;
    Adaptador_GridRevision Adapter;
    ArrayList<ItemRevision> arrayArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision);

        getSupportActionBar().hide();

        etd_Fecha2=(EditText) findViewById(R.id.etd_Fecha2);
        sp_Empresa5=(Spinner) findViewById(R.id.sp_Empresa5);
        sp_huerta5=(Spinner) findViewById(R.id.sp_huerta5);
        sp_Blo2=(Spinner) findViewById(R.id.sp_Blo2);
        etn_Narboles=(EditText) findViewById(R.id.etn_Narboles);
        etn_Nhume=(EditText) findViewById(R.id.etn_Nhume);
        pt_Observaciones=(EditText) findViewById(R.id.pt_Observaciones);
        rb_Asi=(RadioButton) findViewById(R.id.rb_Asi);
        rb_Ano=(RadioButton) findViewById(R.id.rb_Ano);
        rb_Fsi=(RadioButton) findViewById(R.id.rb_Fsi);
        rb_Fno=(RadioButton) findViewById(R.id.rb_Fno);

        lv_GridRevision=(ListView) findViewById(R.id.lv_GridRevision);

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

        sp_Empresa5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(ItemSPHue==null || ItemSPHue.size()<=1){
                    cargaSpinnerHuertas();
                    CopiHue = new AdaptadorSpinner(Revision.this, ItemSPHue);
                    sp_huerta5.setAdapter(CopiHue);

                    if (sp_huerta5.getCount()==2){
                        sp_huerta5.setSelection(1);
                    }
                }else{
                    if(LineEmpresa != i){
                        cargaSpinnerHuertas();
                        CopiHue = new AdaptadorSpinner(Revision.this, ItemSPHue);
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
                Cargagrid(etd_Fecha2.getText().toString(),CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Blo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    @Override
    public void onClick(View v) {
        if(v==etd_Fecha2){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> etd_Fecha2.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
            sp_huerta5.setSelection(0);
            Cargagrid(etd_Fecha2.getText().toString(),CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5));
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

    private void Cargagrid(String Fecha,String Id_Huerta){
        lv_GridRevision.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        Cursor Renglon =BD.rawQuery("select R.Fecha," +
                "R.Id_bloque, " +
                "BLQ.Nombre_Bloque, "+
                "R.Fruta," +
                "R.Floracion," +
                "R.N_Arboles, " +
                "R.Observaciones, "+
                "R.Nivel_Humedad "+
                "from t_Revision as R " +
                "inner join t_Bloque as BLQ on BLQ.Id_Bloque=R.Id_Bloque " +
                "where BLQ.Id_Huerta='"+Id_Huerta+"' and R.Fecha='"+Fecha+"' ",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemRevision(Renglon.getString(0),Renglon.getString(1),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());

                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_podas guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridRevision(getApplicationContext(),arrayArticulos);
            lv_GridRevision.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void InsertarRevision (View view){
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
            if (Double.parseDouble(etn_Nhume.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear El nivel de Humedad,Verifica por favor";
            }
        } catch (IllegalStateException e) {
            etn_Nhume.setText("0");
        }
        catch(NumberFormatException f)
        {
            etn_Nhume.setText("0");
        }
        catch(Exception g)
        {
            etn_Nhume.setText("0");
        }

        if(etn_Narboles.getText().toString().trim().length()==0){
            etn_Narboles.setText("0");
        }

        if (!FaltoAlgo){
            if (guardarEncabezado()){
                Cargagrid(etd_Fecha2.getText().toString(),CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5));
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
        Renglon=BD.rawQuery("select count(P.Id_bloque) as Sihay from t_Revision as P where P.Fecha='"+etd_Fecha2.getText().toString()+"' and P.Id_bloque='"+CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4)+"'  ",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){
                    ContentValues registro = new ContentValues();
                    if(rb_Asi.isChecked()){
                        registro.put("Fruta", 1);
                    }else{
                        registro.put("Fruta", 0);
                    }
                    if(rb_Fsi.isChecked()){
                        registro.put("Floracion", 1);
                    }else{
                        registro.put("Floracion", 0);
                    }

                    registro.put("N_Arboles",Integer.parseInt( etn_Narboles.getText().toString().trim()));
                    registro.put("Observaciones",pt_Observaciones.getText().toString().trim());
                    registro.put("Nivel_Humedad",Double.parseDouble(etn_Nhume.getText().toString().trim()));
                    int cantidad=BD.update("t_Revision",registro,"Fecha='"+etd_Fecha2.getText().toString().trim()+"' and Id_bloque='"+CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4)+"' ",null);

                    return true;
                }else{
                    ContentValues registro= new ContentValues();

                    registro.put("Fecha",etd_Fecha2.getText().toString().trim());
                    registro.put("Id_bloque",CopiBlq.getItem(sp_Blo2.getSelectedItemPosition()).getTexto().substring(0,4));
                    if(rb_Asi.isChecked()){
                        registro.put("Fruta", 1);
                    }else{
                        registro.put("Fruta", 0);
                    }
                    if(rb_Fsi.isChecked()){
                        registro.put("Floracion", 1);
                    }else{
                        registro.put("Floracion", 0);
                    }
                    registro.put("N_Arboles",Integer.parseInt( etn_Narboles.getText().toString().trim()));
                    registro.put("Observaciones",pt_Observaciones.getText().toString().trim());
                    registro.put("Nivel_Humedad",Double.parseDouble(etn_Nhume.getText().toString().trim()));
                    BD.insert("t_Revision",null,registro);

                    return true;
                }
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No se logro consultar la tabla de revision.",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}