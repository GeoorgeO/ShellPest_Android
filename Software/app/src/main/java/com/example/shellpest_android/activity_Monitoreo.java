package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.text.InputType;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

    public String Usuario,Perfil,Huerta;

    private ArrayList<ItemDatoSpinner> ItemSpinner;
    private AdaptadorSpinner AdaptadorSpiner;
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

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1=objDate;
        et_fecha.setText("Fecha: "+objSDF.format(date1));

        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Plaga/Enfermedad"));
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_PE.setAdapter(AdaptadorSpiner);



        cargaSpinnerHue();


        cargaSpinnerPto();
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_Pto.setAdapter(AdaptadorSpiner);
        cargaSpinnerOrg();
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_Org.setAdapter(AdaptadorSpiner);
        cargaSpinnerInd();
        AdaptadorSpiner=new AdaptadorSpinner(this,ItemSpinner);
        sp_Ind.setAdapter(AdaptadorSpiner);



        sp_PE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    //Toast.makeText(getApplicationContext(), "Selected : " + i, Toast.LENGTH_SHORT).show();
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

        Usuario= getIntent().getStringExtra("usuario");
        Perfil= getIntent().getStringExtra("perfil");
        Huerta= getIntent().getStringExtra("huerta");
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
        ItemSpinner.add(new ItemDatoSpinner("Huerta"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta from t_Huerta where Id_Huerta='"+Huerta+"'",null);
            sp_Hue.setEnabled(false);
        }else{
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta from t_Huerta",null);
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

        sp_Hue.setAdapter(AdaptadorSpiner);

    }

    private void cargaSpinnerPto(){
        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Punto de control"));
        ItemSpinner.add(new ItemDatoSpinner("001"));
        ItemSpinner.add(new ItemDatoSpinner("002"));
        ItemSpinner.add(new ItemDatoSpinner("003"));
    }

    private void cargaSpinnerOrg(){
        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Organo Muestreado"));
        ItemSpinner.add(new ItemDatoSpinner("Flor"));
        ItemSpinner.add(new ItemDatoSpinner("Fruto"));
        ItemSpinner.add(new ItemDatoSpinner("Brote"));
    }

    private void cargaSpinnerInd(){
        ItemSpinner=new ArrayList<>();
        ItemSpinner.add(new ItemDatoSpinner("Individuos"));
        ItemSpinner.add(new ItemDatoSpinner(">10"));
        ItemSpinner.add(new ItemDatoSpinner("1"));
        ItemSpinner.add(new ItemDatoSpinner("<5"));
        ItemSpinner.add(new ItemDatoSpinner("100"));
    }







}