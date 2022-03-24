package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Gasolina extends AppCompatActivity {

    public String Usuario, Perfil, Huerta;

    Spinner sp_responsableGas, sp_empresaGas, sp_activoGas, sp_huertaGas, sp_tipoGas, sp_actividadGas;
    TextView txtv_responsableGas;
    EditText etxt_folioGas, etxt_fechainiGas, etxt_fechafinGas, etxt_cantidadiniGas,
            etxt_cantidadsaldoGas, etxt_kminiGas, etxt_kmfinGas, etxt_horometroGas, etxt_observacionesGas;
    Button btn_agregarGas;
    ListView lv_GridGasolina;

    int LineHuerta, LineEmpresa, LineActivo, LineTipo, LineActividad;

    private AdaptadorSpinner CopiHue, CopiActivo, CopiEmp;
    private ArrayList<ItemDatoSpinner> ItemSPUsu, ItemSPEmp, ItemSPHue, ItemSPactivo, ItemSPtipo, ItemSPactividad;

    Boolean solounaEmpresa, solounaHuerta;
    private int dia,mes, anio;

    Itemgasolina Tabla;
    Adaptador_GridGasolina Adapter;
    ArrayList<Itemgasolina> arrayGas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolina);
        getSupportActionBar().hide();

        solounaEmpresa = false;
        solounaHuerta = false;

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        txtv_responsableGas = (TextView) findViewById(R.id.txtv_responsableGas);
        etxt_folioGas = (EditText) findViewById(R.id.etxt_folioGas);
        etxt_fechainiGas = (EditText) findViewById(R.id.etxt_fechainiGas);
        etxt_fechainiGas.setOnClickListener(this::onClick);
        etxt_fechafinGas = (EditText) findViewById(R.id.etxt_fechafinGas);
        etxt_fechafinGas.setOnClickListener(this::onClick);
        etxt_cantidadiniGas = (EditText) findViewById(R.id.etxt_cantidadiniGas);
        etxt_cantidadsaldoGas = (EditText) findViewById(R.id.etxt_cantidadsaldoGas);
        etxt_kminiGas = (EditText) findViewById(R.id.etxt_kminiGas);
        etxt_kmfinGas = (EditText) findViewById(R.id.etxt_kmfinGas);
        etxt_horometroGas = (EditText) findViewById(R.id.etxt_horometroGas);
        etxt_observacionesGas = (EditText) findViewById(R.id.etxt_observacionesGas);

        lv_GridGasolina = (ListView) findViewById(R.id.lv_GridGasolina);
        btn_agregarGas = (Button) findViewById(R.id.btn_agregarGas);

        LineHuerta=0;
        LineEmpresa=0;
        LineActivo=0;
        LineTipo=0;
        LineActividad=0;

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = objDate;

        etxt_fechainiGas.setText(objSDF.format(date1));
        etxt_fechainiGas.setInputType(InputType.TYPE_NULL);
        etxt_fechainiGas.requestFocus();

        Date objDate2 = new Date();
        SimpleDateFormat objSDF2 = new SimpleDateFormat("dd/MM/yyyy");
        Date date2 = objDate2;

        etxt_fechafinGas.setText(objSDF2.format(date2));
        etxt_fechafinGas.setInputType(InputType.TYPE_NULL);
        etxt_fechafinGas.requestFocus();

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        sp_responsableGas = (Spinner)findViewById(R.id.sp_responsableGas);
        sp_empresaGas = (Spinner)findViewById(R.id.sp_empresaGas);
        sp_activoGas = (Spinner) findViewById(R.id.sp_activoGas);
        sp_huertaGas= (Spinner) findViewById(R.id.sp_huertaGas);
        sp_tipoGas = (Spinner) findViewById(R.id.sp_tipoGas);
        sp_actividadGas = (Spinner) findViewById(R.id.sp_actividadGas);

        cargarEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_empresaGas.setAdapter(CopiEmp);

        if(sp_empresaGas.getCount()==2){
            sp_empresaGas.setSelection(1);
        }

        sp_empresaGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(ItemSPHue==null){
                    cargarHuerta();
                    CopiHue = new AdaptadorSpinner(Gasolina.this, ItemSPHue);
                    sp_huertaGas.setAdapter(CopiHue);

                    if(sp_huertaGas.getCount()==2){
                        sp_huertaGas.setSelection(1);
                        ////////////////////
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cargarHuerta();
        cargarActivo();
        cargarActividad();
        cargarResponsable();
        cargarTipogas();

        //cada item representa cada fila del spinner al cual se le agregan items marcados por posicion
        //Empresa
        //Huerta - dependiendo de la huerta se muestran los activos del lugar
        //Activo - dependiendo del tipo de activo se muestran las actividades
        //Responsable
        //Actividad
        //tipo gasolina



    }

    public void onClick(View view) {
        if(view==etxt_fechainiGas){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> etxt_fechainiGas.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
        }

        if(view==etxt_fechafinGas){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> etxt_fechafinGas.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
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
    private void cargarResponsable(){
        String[] responsable = {"Responsable", "Jorge"};
        sp_responsableGas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, responsable));
    }

    private void cargarEmpresa(){
        String[] empresa = {"Empresa", "AGV", "FTVL"};
        sp_empresaGas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empresa));

    }

    private void cargarHuerta(){
        String[] huerta = {"Huerta","La Fontana","Tepehuaje","Los Arroyos"};
        sp_huertaGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, huerta));

    }

    private void cargarActivo(){
        String[] activo = {"Activo","Camioneta","Tractor","Maquinaria"};
        sp_activoGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activo));

    }

    private void cargarTipogas(){
        String[] tipo = {"Tipo","Gasolina Magna","Gasolina Premium","Diesel"};
        sp_tipoGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo));

    }

    private void cargarActividad(){
        String[] actividad = {"Actividad", "Poda","Traslado", "Viaje"};
        sp_actividadGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actividad));

    }

    public void agregarDatos (View view){
        Toast.makeText(this, "sp_empresa", Toast.LENGTH_SHORT).show();
    }

    private void cargaGrid(){
        lv_GridGasolina.setAdapter(null);
        arrayGas.clear();

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "HellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        //Cursor Renglon = BD.rawQuery();
    }

}