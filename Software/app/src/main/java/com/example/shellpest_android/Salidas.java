package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Salidas extends AppCompatActivity  implements View.OnClickListener{

    public String Usuario, Perfil, Huerta;
    Spinner sp_Empresa, sp_Almacen, sp_Responsable;
    EditText et_Fecha;
    private int dia,mes, anio;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPAlm, ItemSPRps;
    private AdaptadorSpinner CopiEmp, CopiAlm, CopiRps;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salidas);

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        sp_Empresa = (Spinner) findViewById(R.id.sp_Empresa);
        sp_Almacen = (Spinner) findViewById(R.id.sp_Almacen);
        sp_Responsable = (Spinner) findViewById(R.id.sp_Responsable);

        ItemSPEmp = new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa"));
        ItemSPEmp.add(new ItemDatoSpinner("Desarrollo Agricola Alegro"));
        ItemSPEmp.add(new ItemDatoSpinner("Alejandro Guerrero Vazquez"));
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa.setAdapter(CopiEmp);

        ItemSPAlm = new ArrayList<>();
        ItemSPAlm.add(new ItemDatoSpinner("Almacen"));
        ItemSPAlm.add(new ItemDatoSpinner("Arroyos"));
        ItemSPAlm.add(new ItemDatoSpinner("Chimilpa"));
        ItemSPAlm.add(new ItemDatoSpinner("Fontana"));
        CopiAlm = new AdaptadorSpinner(this, ItemSPAlm);
        sp_Almacen.setAdapter(CopiAlm);

        ItemSPRps = new ArrayList<>();
        ItemSPRps.add(new ItemDatoSpinner("Responsable"));
        ItemSPRps.add(new ItemDatoSpinner("Luis Olalde"));
        ItemSPRps.add(new ItemDatoSpinner("Gonzalo Cano"));
        CopiRps = new AdaptadorSpinner(this, ItemSPRps);
        sp_Responsable.setAdapter(CopiRps);

        et_Fecha=(EditText)findViewById(R.id.et_Fecha);
        et_Fecha.setOnClickListener(this);

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        et_Fecha.setText(objSDF.format(date1));
        et_Fecha.setInputType(InputType.TYPE_NULL);
        et_Fecha.requestFocus();
    }

    @Override
    public void onClick(View view) {
        if(view==et_Fecha){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> et_Fecha.setText(i2+"/"+(i1+1)+"/"+i),anio,mes,dia);
            dtpd.show();
        }
    }
}