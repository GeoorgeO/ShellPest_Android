package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.BundleCompat;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Gasolina extends AppCompatActivity implements View.OnClickListener {

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

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");
        Log.e("Usuario", Usuario);

        solounaEmpresa = false;
        solounaHuerta = false;

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

        sp_responsableGas = (Spinner)findViewById(R.id.sp_responsableGas);
        sp_empresaGas = (Spinner)findViewById(R.id.sp_empresaGas);
        sp_activoGas = (Spinner) findViewById(R.id.sp_activoGas);
        sp_huertaGas= (Spinner) findViewById(R.id.sp_huertaGas);
        sp_tipoGas = (Spinner) findViewById(R.id.sp_tipoGas);
        sp_actividadGas = (Spinner) findViewById(R.id.sp_actividadGas);

        cargarEmpresa();
        Toast.makeText(this, "CARGA EMPRESA", Toast.LENGTH_SHORT).show();
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
                        //cargaGrid();
                    }else{
                        if (sp_huertaGas.getCount()<=1){
                            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
                            CopiHue = new AdaptadorSpinner(Gasolina.this, ItemSPHue);
                            sp_huertaGas.setAdapter(CopiHue);
                        }
                    }
                }else{
                    if ((ItemSPHue.size()<=1) || LineEmpresa!=i){
                        cargarHuerta();
                        CopiHue = new AdaptadorSpinner(Gasolina.this, ItemSPHue);
                        sp_huertaGas.setAdapter(CopiHue);

                        if (LineHuerta > 0){
                            LineHuerta = 0;
                        }else{
                            if(sp_huertaGas.getCount() == 2){
                                sp_huertaGas.setSelection(1);
                            }else{
                                if (sp_huertaGas.getCount() <= 1){
                                    ItemSPHue = new ArrayList<>();
                                    ItemSPHue.add(new ItemDatoSpinner("Huerta"));
                                    CopiHue = new AdaptadorSpinner(Gasolina.this, ItemSPHue);
                                    sp_huertaGas.setAdapter(CopiHue);
                                }
                            }
                        }
                    }
                }

                if(i > 0){
                    //cargaGrid();
                }

                LineEmpresa = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //cargarHuerta();
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
        CopiEmp = null;
        ItemSPEmp = new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
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

        //cargarHuerta();
    }

    private void cargarHuerta(){
        CopiHue = null;
        ItemSPHue = new ArrayList<>();
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            Renglon = BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_empresaGas.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }else{
            Renglon = BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_empresaGas.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }

        if(Renglon.getCount()>1){
            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
        }

        if(Renglon.moveToFirst()){
            do{
                ItemSPHue.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" "+Renglon.getString(2)));
            }while(Renglon.moveToNext());
        }else{
            Toast.makeText(this, "No se encontraron datos en huertas", Toast.LENGTH_SHORT).show();
            BD.close();
        }
        BD.close();

        //String[] huerta = {"Huerta","La Fontana","Tepehuaje","Los Arroyos"};
        //sp_huertaGas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, huerta));

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
        if(etxt_fechainiGas.getText().length()==0){
            etxt_fechainiGas.setText("dd/mm/aa");
        }
        if(etxt_fechafinGas.getText().length()==0){
            etxt_fechafinGas.setText("dd/mm/aa");
        }
        if(etxt_cantidadiniGas.getText().length()==0){
            etxt_cantidadiniGas.setText("0.0");
        }
        if(etxt_cantidadsaldoGas.getText().length()==0){
            etxt_cantidadsaldoGas.setText("0.0");
        }
        if(etxt_kminiGas.getText().length()==0){
            etxt_kminiGas.setText("0.0");
        }
        if(etxt_kmfinGas.getText().length()==0){
            etxt_kmfinGas.setText("0.0");
        }
        if (etxt_folioGas.getText().length()==0){
            etxt_folioGas.setText("0000");
        }
        if(etxt_horometroGas.getText().length()==0){
            etxt_horometroGas.setText("00:00");
        }
        if(etxt_observacionesGas.getText().length()==0){
            etxt_observacionesGas.setText("Observaciones");
        }
        if(sp_actividadGas.getSelectedItemPosition()>0){
            boolean FaltaAlgo = false;
            if(Double.parseDouble(String.valueOf(etxt_folioGas.getText()))>=0){
                if(Double.parseDouble(String.valueOf(etxt_fechainiGas.getText()))>=0){
                    if(Double.parseDouble(String.valueOf(etxt_fechafinGas.getText()))>=0){
                        if(Double.parseDouble(String.valueOf(etxt_cantidadiniGas.getText()))>=0){
                            if (Double.parseDouble(String.valueOf(etxt_cantidadsaldoGas.getText()))>=0){
                                if (Double.parseDouble(String.valueOf(etxt_kminiGas.getText()))>=0){
                                    if(Double.parseDouble(String.valueOf(etxt_kmfinGas.getText()))>=0){
                                        if(Double.parseDouble(String.valueOf(etxt_horometroGas.getText()))>=0){
                                            if(Double.parseDouble(String.valueOf(etxt_observacionesGas.getText()))>=0){

                                            }else{
                                                FaltaAlgo = true;
                                                Toast.makeText(this, "Falta agregar observaciones", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            FaltaAlgo = true;
                                            Toast.makeText(this, "Falta agregar Horometros",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        FaltaAlgo = true;
                                        Toast.makeText(this,"Falta agregar Km Final", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    FaltaAlgo = true;
                                    Toast.makeText(this, "Falta agregar Km Inicial", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                FaltaAlgo = true;
                                Toast.makeText(this,"Falta agregar Cantidad Saldo", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            FaltaAlgo = true;
                            Toast.makeText(this, "Falta agregar Cantidad Inicial", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        FaltaAlgo = true;
                        Toast.makeText(this,"Falta seleccionar Fecha Final", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    FaltaAlgo = true;
                    Toast.makeText(this, "Falta seleccionar Fecha Inicial", Toast.LENGTH_SHORT).show();
                }
            }else{
                FaltaAlgo = true;
                Toast.makeText(this, "Falta agregar Folio Vale", Toast.LENGTH_SHORT).show();
            }
        }




        //Toast.makeText(this, "sp_empresa", Toast.LENGTH_SHORT).show();
    }

    private void cargaGrid(){
        lv_GridGasolina.setAdapter(null);
        //arrayGas.clear(); //se usa para guardar loa datos y cargarlos en el grid

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "HellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        Cursor Renglon =BD.rawQuery("select R.Nombre_Responsable, \n" +
                "\tR.Folio_vale,\n" +
                "\tB.Fecha_Inicio, \n" +
                "\tR.Fecha_final, \n" +
                "\tR.Cantidad_ingreso, \n" +
                "\tR.Cantidad_saldo, \n" +
                "\tR.Empresa, \n" +
                "\tR.Huerta, \n" +
                "\tR.Km_Inicial, \n" +
                "\tR.Km_Final, \n" +
                "\tR.Horometro, \n" +
                "\tR.Tipo_Combustible, \n" +
                "\tR.Actividad, \n" +
                "\tR.Observaciones, \n" +

                "\tR.Horas_Riego, R.c_codigo_eps, R.Temperatura, R.ET \n" +
                "from t_Riego as R \n" +
                "left join t_Bloque as B on B.Id_Bloque=R.Id_Bloque and B.c_codigo_eps=R.c_codigo_eps \n" +
                "where R.Fecha='"+objSDF.format(date1)+"' and R.c_codigo_eps='"+CopiEmp.getItem(sp_empresaGas.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new Itemgasolina(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10),Renglon.getString(11),Renglon.getString(12),Renglon.getString(13),Renglon.getString(14));
                    arrayGas.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayGas.size()>0){
            Adapter=new Adaptador_GridGasolina(getApplicationContext(),arrayGas);
            lv_GridGasolina.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

}