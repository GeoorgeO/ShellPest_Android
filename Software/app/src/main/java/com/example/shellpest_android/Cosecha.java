package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
        text_PepenaCap=(EditText) findViewById(R.id.text_DesechoCap);
        text_DesechoCap=(EditText) findViewById(R.id.text_DesechoCap);
        lv_GridCosecha=(ListView) findViewById(R.id.lv_GridCosecha);

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
                    cargaSpinnerHuertas();
                    CopiHue = new AdaptadorSpinner(Cosecha.this, ItemSPHue);
                    sp_huerta5.setAdapter(CopiHue);

                    if (sp_huerta5.getCount()==2){
                        sp_huerta5.setSelection(1);
                    }
                }else{
                    if(LineEmpresa != i){
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

}