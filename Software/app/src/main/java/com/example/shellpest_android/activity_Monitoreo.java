package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.text.InputType;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class activity_Monitoreo extends AppCompatActivity {
    TextView et_fecha,et_PuntoControl,et_EP,et_Organo,et_Individuo;
    Spinner sp_PE,sp_Hue,sp_Pto,sp_Org,sp_Ind;
    RadioButton rb_Plaga,rb_Enfermedad;
    RadioGroup rg_PE;
    ListView lv_GridMonitoreo;

    public String Usuario,Perfil,Huerta;

    private ArrayList<ItemDatoSpinner> ItemSpinner;
    private AdaptadorSpinner AdaptadorSpiner,CopiHue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__monitoreo);

        getSupportActionBar().hide();
        et_fecha=(TextView)findViewById(R.id.et_fecha);
        et_PuntoControl=(TextView)findViewById(R.id.et_PuntoControl);
        et_EP=(TextView)findViewById(R.id.et_EP);
        et_Organo=(TextView)findViewById(R.id.et_Organo);
        et_Individuo=(TextView)findViewById(R.id.et_Individuo);

        sp_PE=(Spinner)findViewById(R.id.sp_PE);
        sp_Hue=(Spinner)findViewById(R.id.sp_Hue);
        sp_Pto=(Spinner)findViewById(R.id.sp_Pto);
        sp_Org=(Spinner)findViewById(R.id.sp_Org);
        sp_Ind=(Spinner)findViewById(R.id.sp_Ind);

        rb_Plaga=(RadioButton)findViewById(R.id.rb_Plaga);
        rb_Enfermedad=(RadioButton)findViewById(R.id.rb_Enfermedad);

        rg_PE=(RadioGroup)findViewById(R.id.rg_PE);

        lv_GridMonitoreo=(ListView)findViewById(R.id.lv_GridMonitoreo);

        Usuario= getIntent().getStringExtra("usuario2");
        Perfil= getIntent().getStringExtra("perfil2");
        Huerta= getIntent().getStringExtra("huerta2");

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1=objDate;
        et_fecha.setText("Fecha: "+objSDF.format(date1));

        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Plaga/Enfermedad"));
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_PE.setAdapter(AdaptadorSpiner);

        cargaSpinnerHue();
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        CopiHue=AdaptadorSpiner;
        sp_Hue.setAdapter(AdaptadorSpiner);

        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Punto de control"));
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_Pto.setAdapter(AdaptadorSpiner);

        cargaSpinnerOrg();
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_Org.setAdapter(AdaptadorSpiner);

        cargaSpinnerInd();
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_Ind.setAdapter(AdaptadorSpiner);

        sp_Hue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    Huerta=CopiHue.getItem(i).getTexto().substring(0,5);

                    cargaSpinnerPto();
                    AdaptadorSpiner=new AdaptadorSpinner(getApplicationContext(),ItemSpinner);
                    sp_Pto.setAdapter(AdaptadorSpiner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rg_PE.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                cargaSpinnerPE();
                AdaptadorSpiner=new AdaptadorSpinner(getApplicationContext(),ItemSpinner);
                sp_PE.setAdapter(AdaptadorSpiner);
            }
        });
    }

   private void cargaSpinnerPE(){
       AdaptadorSpiner=null;

       ItemSpinner=new ArrayList<>();
       ItemSpinner.add(new ItemDatoSpinner("Plaga/Enfermedad"));

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
               ItemSpinner.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
           } while(Renglon.moveToNext());


           BD.close();
       }else{
           Toast.makeText(this,"No hay usuarios guardados",Toast.LENGTH_SHORT).show();
           BD.close();
       }
   }

    private void cargaSpinnerHue(){
        AdaptadorSpiner=null;

        ItemSpinner=new ArrayList<>();

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            ItemSpinner.add(new ItemDatoSpinner("Huerta"));
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta from t_Huerta",null);
        }else{
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta from t_Huerta where Id_Huerta='"+Huerta+"'",null);
            sp_Hue.setEnabled(false);
        }

        if(Renglon.moveToFirst()){
            do {
                ItemSpinner.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No se encontraron datos en huertas",Toast.LENGTH_SHORT).show();
            BD.close();
        }
        BD.close();
    }

    private void cargaSpinnerPto(){

        if(Huerta.length()>0 && !Huerta.equals("NULL")){
            AdaptadorSpiner=null;

            ItemSpinner=new ArrayList<>();

            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            ItemSpinner.add(new ItemDatoSpinner("Punto de control"));

            Renglon=BD.rawQuery("select P.Id_PuntoControl,P.Nombre_PuntoControl,P.Id_Bloque,P.n_coordenadaX,P.n_coordenadaY from t_Puntocontrol as P, t_Bloque as B  where  P.Id_Bloque=B.Id_Bloque and B.Id_Huerta='"+Huerta+"'",null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSpinner.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
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
        AdaptadorSpiner=null;

        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Organo muestreado"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select Id_Deteccion,Nombre_Deteccion from t_Deteccion",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSpinner.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
            } while(Renglon.moveToNext());


            BD.close();
        }else{
            Toast.makeText(this,"No hay datos en t_deteccion guardados",Toast.LENGTH_SHORT).show();
            BD.close();
        }
    }

    private void cargaSpinnerInd(){
        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Individuos"));
        ItemSpinner.add(new ItemDatoSpinner("001 - >10"));
        ItemSpinner.add(new ItemDatoSpinner("002 - 1"));
        ItemSpinner.add(new ItemDatoSpinner("003 - <5"));
        ItemSpinner.add(new ItemDatoSpinner("004 - 100"));
    }

    private void agregarAGrid (){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        ContentValues registro= new ContentValues();
        registro.put("Fecha",objSDF.format(date1));
        registro.put("Id_Huerta",Huerta);
        if(rb_Enfermedad.isChecked()){
            AdaptadorSpiner=(AdaptadorSpinner) sp_PE.getSelectedItem();
            registro.put("Id_Plagas","");
            registro.put("Id_Enfermedad",AdaptadorSpiner.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4));
        }else{
            AdaptadorSpiner=(AdaptadorSpinner) sp_PE.getSelectedItem();
            registro.put("Id_Plagas",AdaptadorSpiner.getItem(sp_PE.getSelectedItemPosition()).getTexto().substring(0,4));
            registro.put("Id_Enfermedad","");
        }
        AdaptadorSpiner=(AdaptadorSpinner) sp_Pto.getSelectedItem();
        registro.put("Id_PuntoControl",AdaptadorSpiner.getItem(sp_Pto.getSelectedItemPosition()).getTexto().substring(0,4));
        AdaptadorSpiner=(AdaptadorSpinner) sp_Org.getSelectedItem();
        registro.put("Id_Deteccion",AdaptadorSpiner.getItem(sp_Org.getSelectedItemPosition()).getTexto().substring(0,4));
        AdaptadorSpiner=(AdaptadorSpinner) sp_Ind.getSelectedItem();
        registro.put("Id_Individuo",AdaptadorSpiner.getItem(sp_Ind.getSelectedItemPosition()).getTexto().substring(0,3));
        registro.put("Id_Usuario",Usuario);
        registro.put("Id_Humbral","0001");
        registro.put("n_coordenadaX","0.123");
        registro.put("n_coordenadaY","0.567");

        BD.insert("t_Monitoreo_PE",null,registro);
        BD.close();


    }
}