package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Gasolina extends AppCompatActivity implements View.OnClickListener {

    public String Usuario, Perfil, Huerta, Activo, Responsable, Tipo;
    public String Magna = "Magna - 87 - octanos", Premium = "Premium - 92 - octanos", Diesel= "Diesel";

    Spinner sp_responsableGas, sp_empresaGas, sp_activoGas, sp_huertaGas, sp_tipoGas;

    AutoCompleteTextView actxt_actividadGas;

    EditText etxt_folioGas, etxt_fechainiGas, etxt_fechafinGas, etxt_cantidadiniGas,
            etxt_cantidadsaldoGas, etxt_kminiGas, etxt_kmfinGas, etxt_horometroGas, etxt_observacionesGas;
    Button btn_agregarGas;
    ListView lv_GridGasolina;
    TableRow tr_kminiT, tr_kmfinT, tr_hrT;

    private ArrayAdapter Adaptador_Arreglos;
    private ArrayList<String> ArrayActividades,ArrayActividadesLimpia;

    int LineHuerta, LineEmpresa, LineActivo, LineTipo, LineActividad;

    private AdaptadorSpinner CopiHue, CopiActivo, CopiEmp, CopiResp, CopiTipo;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPHue, ItemSPActivo, ItemSPResp, ItemSPTipo, ItemSPActividad;

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
        actxt_actividadGas = (AutoCompleteTextView) findViewById(R.id.atxt_actividadGas);

        lv_GridGasolina = (ListView) findViewById(R.id.lv_GridGasolina);
        btn_agregarGas = (Button) findViewById(R.id.btn_agregarGas);
        tr_kminiT = (TableRow) findViewById(R.id.tr_kminiT);
        tr_kmfinT = (TableRow) findViewById(R.id.tr_kmfinT);
        tr_hrT = (TableRow) findViewById(R.id.tr_hrT);

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

        sp_huertaGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Huerta = CopiHue.getItem(i).getTexto().substring(0,5);
                Log.e("Huerta ",Huerta);
                cargarActivo();
                CopiActivo = new AdaptadorSpinner(getApplicationContext(), ItemSPActivo);
                sp_activoGas.setAdapter(CopiActivo);

                LineHuerta = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_activoGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Activo = CopiActivo.getItem(i).getTexto().substring(0,4);
                Log.e("Activo", Activo);

                Habilitar();

                cargarResponsable();
                CopiResp = new AdaptadorSpinner(getApplicationContext(), ItemSPResp);
                sp_responsableGas.setAdapter(CopiResp);


                LineActivo = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_responsableGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                Responsable = CopiResp.getItem(i).getTexto().substring(0,6);
                Log.e("Responsable", Responsable);

                cargarActividad();
                Adaptador_Arreglos = new ArrayAdapter(Gasolina.this, android.R.layout.simple_list_item_1,ArrayActividades);
                actxt_actividadGas.setAdapter(Adaptador_Arreglos);

                cargarTipogas();
                CopiTipo = new AdaptadorSpinner(getApplicationContext(), ItemSPTipo);
                sp_tipoGas.setAdapter(CopiTipo);

                LineTipo = i;

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_tipoGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                Tipo = CopiTipo.getItem(i).getTexto();
                Log.e("Tipo", Tipo);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        actxt_actividadGas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(Gasolina.this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                Cursor Renglon;
                Renglon = BD.rawQuery("select AH.c_codigo_act, AH.c_codigo_cam,AH.v_nombre_act" +
                        " from t_Actividades_Huerta as AH " +
                        "where ltrim(rtrim(AH.id_Huerta))='"+Huerta+"' or AH.c_codigo_cam = '00'",null);

                if(Renglon.moveToFirst()){

                    do {
                        Log.e("Renglon",Renglon.toString());
                    } while(Renglon.moveToNext());

                    BD.close();
                }else{
                    BD.close();
                }
            }
        });

        actxt_actividadGas.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                actxt_actividadGas.setText(" ");
            }
        });


        //cargarHuerta();
        //cargarActivo();
        //cargarActividad();
        //cargarResponsable();
        //cargarTipogas();


        //cada item representa cada fila del spinner al cual se le agregan items marcados por posicion
        //Empresa
        //Huerta - dependiendo de la huerta se muestran los activos del lugar
        //Activo - dependiendo del tipo de activo se muestran las actividades
        //Responsable
        //Actividad
        //tipo gasolina

    }

    public void Habilitar(){

        //sin medidor
        if (Activo.equals("0967")){
            tr_kminiT.setVisibility(View.INVISIBLE);
            tr_kmfinT.setVisibility(View.INVISIBLE);
            tr_hrT.setVisibility(View.INVISIBLE);
        }

        //
        //Kilometros
        if (Activo.equals("0001")||Activo.equals("0002")||Activo.equals("0003")||Activo.equals("0004")||Activo.equals("0005")||Activo.equals("0006")||Activo.equals("0007")||Activo.equals("0008")||Activo.equals("0009")||Activo.equals("0011")||Activo.equals("0013")||Activo.equals("0014")||Activo.equals("0015")||Activo.equals("0016")||Activo.equals("0024")
                ||Activo.equals("0025")||Activo.equals("0026")||Activo.equals("0027")||Activo.equals("0028")||Activo.equals("0029")||Activo.equals("0032")||Activo.equals("0052")||Activo.equals("0601")||Activo.equals("0602")||Activo.equals("0603")||Activo.equals("0700")||Activo.equals("0702")||Activo.equals("0703")||Activo.equals("0704")||Activo.equals("0952")
                ||Activo.equals("0954")||Activo.equals("0955")||Activo.equals("0956")||Activo.equals("0957")||Activo.equals("0966")||Activo.equals("0969")||Activo.equals("0971")||Activo.equals("0982")||Activo.equals("0993")||Activo.equals("1035")||Activo.equals("1043")||Activo.equals("1046")||Activo.equals("1059")||Activo.equals("1069")||Activo.equals("2017")
                ||Activo.equals("2019")||Activo.equals("2020")||Activo.equals("2021")||Activo.equals("2022")||Activo.equals("2023")||Activo.equals("2024")){
            tr_kminiT.setVisibility(View.VISIBLE);
            tr_kmfinT.setVisibility(View.VISIBLE);
            tr_hrT.setVisibility(View.INVISIBLE);
        }

        //Horometro
        if (Activo.equals("0978")){
            tr_kminiT.setVisibility(View.INVISIBLE);
            tr_kmfinT.setVisibility(View.INVISIBLE);
            tr_hrT.setVisibility(View.VISIBLE);
        }
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
        CopiResp = null;
        ItemSPResp = new ArrayList<>();
        ItemSPResp.add(new ItemDatoSpinner("Responsable"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;
        String Consulta;

        Consulta="select EH.c_codigo_emp, EH.Id_Huerta, EH.Nombre_Completo from t_Empleados_Huerta as EH where ltrim(rtrim(EH.Id_Huerta))='"+Huerta+"' ";
        Renglon=BD.rawQuery(Consulta,null);


        if(Renglon.moveToFirst()){

            do {
                ItemSPResp.add(new ItemDatoSpinner(Renglon.getString(0)+"-"+Renglon.getString(1)+"- "+Renglon.getString(2)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

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
        CopiActivo = null;

        ItemSPActivo = new ArrayList<>();
        ItemSPActivo.add(new ItemDatoSpinner("Activo"));

        if(Huerta.length()>0 && !Huerta.equals("NULL")){

            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            String Consulta;
            Consulta="select A.Id_ActivosGas,A.v_descripcorta_act, A.v_serie_act  from t_Activos_Huerta as A ";
            Renglon = BD.rawQuery(Consulta,null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSPActivo.add(new ItemDatoSpinner(Renglon.getString(0)+"-"+Renglon.getString(1)+"-"+Renglon.getString(2)));
                } while(Renglon.moveToNext());
            }else{
                Toast.makeText(this,"No se encontraron datos en Activos",Toast.LENGTH_SHORT).show();
                BD.close();
            }
            BD.close();
        }else{

        }
    }

    private void cargarTipogas(){
        CopiTipo = null;

        ItemSPTipo = new ArrayList<>();
        ItemSPTipo.add(new ItemDatoSpinner("Tipo"));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Magna));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Premium));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Diesel));

        sp_tipoGas.setGravity(Gravity.CENTER);

    }

    private void cargarActividad(){
        ArrayActividades=ArrayActividadesLimpia;
        ArrayActividades=new ArrayList<>();
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon = BD.rawQuery("select AH.v_nombre_act,AH.c_codigo_act,AH.c_codigo_cam" +
                " from t_Actividades_Huerta as AH " +
                "where ltrim(rtrim(AH.id_Huerta))='"+Huerta+"' or AH.c_codigo_cam = '00'",null);

        if(Renglon.moveToFirst()){
            do {
                ArrayActividades.add(new String(Renglon.getString(0)+" | "+Renglon.getString(1)+" | "+Renglon.getString(2)));
            } while(Renglon.moveToNext());
            BD.close();
        }else{
            BD.close();
        }
    }

    public void agregarDatos (View view){
       boolean Falta = false;
       String Mensaje = "";

       if(sp_empresaGas.getSelectedItemPosition() > 0){ }else{
           Falta = true;
           Mensaje = "Falta seleccionar Empresa, Verifica por favor.";
       }if (sp_huertaGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar Huerta, Verifica por favor.";
        }


    }

    private void cargaGrid(){
        lv_GridGasolina.setAdapter(null);
        arrayGas.clear(); //se usa para guardar loa datos y cargarlos en el grid

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