package com.example.shellpest_android;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class Gasolina extends AppCompatActivity implements View.OnClickListener {

    public String Usuario, Perfil, Huerta, Activo, Responsable, Tipo;
    public String Magna = "Magna - 87 - octanos", Premium = "Premium - 92 - octanos", Diesel= "Diesel";
    public Boolean Verde = false, Amarillo = false, Naranja = false, Azul = false;

    Spinner sp_responsableGas, sp_empresaGas, sp_activoGas, sp_huertaGas, sp_tipoGas;

    AutoCompleteTextView actxt_actividadGas;

    EditText etxt_folioGas, etxt_fechainiGas, etxt_fechafinGas, etxt_cantidadiniGas,
            etxt_cantidadsaldoGas, etxt_kminiGas, etxt_kmfinGas, etxt_horometroGas, etxt_observacionesGas;
    Button btn_agregarGas;
    ListView lv_GridGasolina;
    TableRow tr_kminiT, tr_kmfinT, tr_hrT;
    LinearLayout ly_cantidadGas, ly_actividadGas;
    TextView txtv_tipoComb;

    private ArrayAdapter Adaptador_Arreglos;
    private ArrayList<String> ArrayActividades,ArrayActividadesLimpia;

    int LineHuerta, LineEmpresa, LineActivo, LineTipo, LineActividad;

    private AdaptadorSpinner CopiHue, CopiActivo, CopiEmp, CopiResp, CopiTipo;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPHue, ItemSPActivo, ItemSPResp, ItemSPTipo, ItemSPActividad;

    Boolean solounaEmpresa, solounaHuerta, largo;
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
        ly_cantidadGas = (LinearLayout) findViewById(R.id.ly_cantidadGas);
        ly_actividadGas = (LinearLayout) findViewById(R.id.ly_actividadGas);
        txtv_tipoComb = (TextView) findViewById(R.id.txtv_tipoComb);

        LineHuerta=0;
        LineEmpresa=0;
        LineActivo=0;
        LineTipo=0;
        LineActividad=0;

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = objDate;

        etxt_fechainiGas.setText("");
        etxt_fechainiGas.setInputType(InputType.TYPE_NULL);
        etxt_fechainiGas.requestFocus();

        Date objDate2 = new Date();
        SimpleDateFormat objSDF2 = new SimpleDateFormat("dd/MM/yyyy");
        Date date2 = objDate2;

        etxt_fechafinGas.setText("");
        etxt_fechafinGas.setInputType(InputType.TYPE_NULL);
        etxt_fechafinGas.requestFocus();

        sp_responsableGas = (Spinner)findViewById(R.id.sp_responsableGas);
        sp_empresaGas = (Spinner)findViewById(R.id.sp_empresaGas);
        sp_activoGas = (Spinner) findViewById(R.id.sp_activoGas);
        sp_huertaGas= (Spinner) findViewById(R.id.sp_huertaGas);
        sp_tipoGas = (Spinner) findViewById(R.id.sp_tipoGas);

        arrayGas = new ArrayList<>();

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
                        cargaGrid();
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

                if(i>0){
                    cargaGrid();
                }else{
                    lv_GridGasolina.setAdapter(null);
                    arrayGas.clear();
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

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cargarTipogas();
        CopiTipo = new AdaptadorSpinner(getApplicationContext(), ItemSPTipo);
        sp_tipoGas.setAdapter(CopiTipo);

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

        lv_GridGasolina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //SE VA A UTILIZAR SI SE SELECCIONA ALGO DEL LIST VIEW Y CON LLEVA ALGUNA ACCION
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

        //sin medidor VERDE
        if (Activo.equals("0958")||Activo.equals("0959")||Activo.equals("0967")||Activo.equals("0970")||Activo.equals("0976")||Activo.equals("0977")||Activo.equals("0979")||Activo.equals("0980")||Activo.equals("0981")||Activo.equals("0988")||Activo.equals("0990")||Activo.equals("0991")||Activo.equals("0992")||Activo.equals("0996")||Activo.equals("1000")||Activo.equals("1017")||Activo.equals("1031")||Activo.equals("1032")||Activo.equals("1033")||Activo.equals("1034")||Activo.equals("1037")||Activo.equals("1038")||Activo.equals("1056")||Activo.equals("1070")||Activo.equals("2001")||Activo.equals("2002")||Activo.equals("2003")
                ||Activo.equals("2004")||Activo.equals("2005")||Activo.equals("2006")||Activo.equals("2007")||Activo.equals("2008")||Activo.equals("2009")||Activo.equals("2010")||Activo.equals("2011")||Activo.equals("2012")||Activo.equals("2013")||Activo.equals("2014")||Activo.equals("2015")||Activo.equals("2016")||Activo.equals("2018")||Activo.equals("2031")||Activo.equals("2033")||Activo.equals("2039")||Activo.equals("2047")||Activo.equals("2056")||Activo.equals("2059")||Activo.equals("2060")||Activo.equals("2066")||Activo.equals("2067")||Activo.equals("2072")||Activo.equals("2073")||Activo.equals("2074")||Activo.equals("2077")
                ||Activo.equals("5001")||Activo.equals("5002")||Activo.equals("5003")||Activo.equals("5004")||Activo.equals("5005")||Activo.equals("5006")||Activo.equals("5007")||Activo.equals("5008")||Activo.equals("5009")||Activo.equals("5010")||Activo.equals("5011")||Activo.equals("5012")||Activo.equals("5013")||Activo.equals("5014")||Activo.equals("5015")||Activo.equals("5016")||Activo.equals("5017")||Activo.equals("5018")||Activo.equals("5019")||Activo.equals("5021")||Activo.equals("5029")||Activo.equals("6001")||Activo.equals("6014")||Activo.equals("6015")||Activo.equals("6019")||Activo.equals("6025")||Activo.equals("6027")
                ||Activo.equals("6045")||Activo.equals("6049")||Activo.equals("6052")||Activo.equals("6060")||Activo.equals("6062")||Activo.equals("6066")||Activo.equals("6069")||Activo.equals("6070")||Activo.equals("6089")||Activo.equals("6090")||Activo.equals("6091")||Activo.equals("6092")||Activo.equals("6122")||Activo.equals("6123")||Activo.equals("6124")||Activo.equals("6125")||Activo.equals("6158")||Activo.equals("6161")||Activo.equals("6162")||Activo.equals("6163")||Activo.equals("6164")||Activo.equals("6165")||Activo.equals("6166")||Activo.equals("6172")||Activo.equals("6173")||Activo.equals("6174")||Activo.equals("6175")
                ||Activo.equals("6176")||Activo.equals("6177")||Activo.equals("6178")||Activo.equals("6180")||Activo.equals("6185")||Activo.equals("6189")||Activo.equals("6190")||Activo.equals("6199")||Activo.equals("6215")||Activo.equals("6229")||Activo.equals("6232")||Activo.equals("6234")||Activo.equals("6245")||Activo.equals("6259")||Activo.equals("6260")||Activo.equals("6261")||Activo.equals("6262")||Activo.equals("6263")||Activo.equals("6275")||Activo.equals("6287")||Activo.equals("6288")||Activo.equals("6289")||Activo.equals("6290")||Activo.equals("6291")||Activo.equals("6292")||Activo.equals("6293")||Activo.equals("6298")
                ||Activo.equals("6299")||Activo.equals("6300")||Activo.equals("6301")||Activo.equals("6302")||Activo.equals("6303")||Activo.equals("6304")||Activo.equals("6305")||Activo.equals("6306")||Activo.equals("6310")||Activo.equals("6312")||Activo.equals("6315")||Activo.equals("6316")||Activo.equals("6317")||Activo.equals("6318")||Activo.equals("6319")||Activo.equals("6320")||Activo.equals("6321")||Activo.equals("6322")||Activo.equals("6325")||Activo.equals("6326")||Activo.equals("6327")||Activo.equals("6328")||Activo.equals("6329")||Activo.equals("6330")||Activo.equals("6331")||Activo.equals("6336")||Activo.equals("6343")
                ||Activo.equals("6344")||Activo.equals("6347")||Activo.equals("6348")||Activo.equals("6350")||Activo.equals("6351")||Activo.equals("6352")||Activo.equals("6353")||Activo.equals("6356")||Activo.equals("6357")||Activo.equals("6368")||Activo.equals("6369")||Activo.equals("6370")||Activo.equals("6371")||Activo.equals("6372")||Activo.equals("6373")||Activo.equals("6374")||Activo.equals("6385")||Activo.equals("6392")||Activo.equals("6393")||Activo.equals("6394")||Activo.equals("6395")||Activo.equals("6396")||Activo.equals("6397")||Activo.equals("6398")||Activo.equals("6399")||Activo.equals("6400")||Activo.equals("6407")
                ||Activo.equals("6409")||Activo.equals("6414")||Activo.equals("6415")||Activo.equals("6442")||Activo.equals("6453")||Activo.equals("6457")||Activo.equals("6458")||Activo.equals("6462")||Activo.equals("6468")||Activo.equals("6469")||Activo.equals("6470")||Activo.equals("6471")||Activo.equals("6478")||Activo.equals("6485")||Activo.equals("6486")||Activo.equals("6487")||Activo.equals("6488")||Activo.equals("6489")||Activo.equals("6498")||Activo.equals("6520")||Activo.equals("6521")||Activo.equals("6544")||Activo.equals("6545")||Activo.equals("6546")||Activo.equals("6558")||Activo.equals("6559")||Activo.equals("6560")
                ||Activo.equals("6567")||Activo.equals("6568")||Activo.equals("6570")||Activo.equals("6575")||Activo.equals("6578")||Activo.equals("6580")||Activo.equals("6581")||Activo.equals("6582")||Activo.equals("6583")||Activo.equals("6584")||Activo.equals("6585")||Activo.equals("6587")||Activo.equals("6600")||Activo.equals("6601")||Activo.equals("6610")||Activo.equals("6611")||Activo.equals("6612")||Activo.equals("6613")||Activo.equals("6619")||Activo.equals("6620")||Activo.equals("6621")||Activo.equals("6622")||Activo.equals("6623")||Activo.equals("6624")||Activo.equals("6625")||Activo.equals("6626")||Activo.equals("6627")
                ||Activo.equals("6628")||Activo.equals("6637")||Activo.equals("6638")||Activo.equals("6639")||Activo.equals("6640")||Activo.equals("6641")||Activo.equals("6643")||Activo.equals("6644")||Activo.equals("6646")
        ){
            Verde = true;
            Amarillo = false;
            Naranja = false;
            Azul = false;
            tr_kminiT.setVisibility(View.INVISIBLE);
            tr_kmfinT.setVisibility(View.INVISIBLE);
            tr_hrT.setVisibility(View.INVISIBLE);
            actxt_actividadGas.setVisibility(View.VISIBLE);
            etxt_cantidadiniGas.setVisibility(View.VISIBLE);
            etxt_cantidadsaldoGas.setVisibility(View.VISIBLE);
            sp_tipoGas.setVisibility(View.VISIBLE);
            ly_cantidadGas.setVisibility(View.VISIBLE);
            ly_actividadGas.setVisibility(View.VISIBLE);
            txtv_tipoComb.setVisibility(View.VISIBLE);

            Toast ToastMensaje = Toast.makeText(this, "Sin medidor", Toast.LENGTH_SHORT);
            View toastView = ToastMensaje.getView();
            toastView.setBackgroundResource(R.drawable.spinner_style);
            ToastMensaje.show();
        }
        //Kilometros AMARILLO
        if (Activo.equals("0001")||Activo.equals("0002")||Activo.equals("0003")||Activo.equals("0004")||Activo.equals("0005")||Activo.equals("0006")||Activo.equals("0007")||Activo.equals("0008")||Activo.equals("0009")||Activo.equals("0011")||Activo.equals("0013")||Activo.equals("0014")||Activo.equals("0015")||Activo.equals("0016")||Activo.equals("0024")||Activo.equals("0025")||Activo.equals("0026")||Activo.equals("0027")||Activo.equals("0028")||Activo.equals("0029")||Activo.equals("0032")||Activo.equals("0052")||Activo.equals("0601")||Activo.equals("0602")||Activo.equals("0603")||Activo.equals("0700")||Activo.equals("0702")
                ||Activo.equals("0703")||Activo.equals("0704")||Activo.equals("0952")||Activo.equals("0954")||Activo.equals("0955")||Activo.equals("0956")||Activo.equals("0957")||Activo.equals("0966")||Activo.equals("0969")||Activo.equals("0971")||Activo.equals("0982")||Activo.equals("0993")||Activo.equals("1014")||Activo.equals("1015")||Activo.equals("1018")||Activo.equals("1019")||Activo.equals("1020")||Activo.equals("1035")||Activo.equals("1043")||Activo.equals("1046")||Activo.equals("1055")||Activo.equals("1059")||Activo.equals("1069")||Activo.equals("2017")||Activo.equals("2019")||Activo.equals("2020")||Activo.equals("2021")
                ||Activo.equals("2022")||Activo.equals("2023")||Activo.equals("2024")||Activo.equals("6072")||Activo.equals("6077")||Activo.equals("6078")||Activo.equals("6079")||Activo.equals("6080")||Activo.equals("6081")||Activo.equals("6082")||Activo.equals("6083")||Activo.equals("6126")||Activo.equals("6127")||Activo.equals("6128")||Activo.equals("6129")||Activo.equals("6157")||Activo.equals("6171")||Activo.equals("6179")||Activo.equals("6182")||Activo.equals("6209")||Activo.equals("6225")||Activo.equals("6226")||Activo.equals("6233")||Activo.equals("6241")||Activo.equals("6243")||Activo.equals("6244")||Activo.equals("6264")
                ||Activo.equals("6265")||Activo.equals("6266")||Activo.equals("6273")||Activo.equals("6277")||Activo.equals("6282")||Activo.equals("6283")||Activo.equals("6284")||Activo.equals("6285")||Activo.equals("6309")||Activo.equals("6311")||Activo.equals("6314")||Activo.equals("6349")||Activo.equals("6361")||Activo.equals("6363")||Activo.equals("6364")||Activo.equals("6365")||Activo.equals("6376")||Activo.equals("6377")||Activo.equals("6382")||Activo.equals("6383")||Activo.equals("6391")||Activo.equals("6402")||Activo.equals("6408")||Activo.equals("6412")||Activo.equals("6416")||Activo.equals("6417")||Activo.equals("6418")
                ||Activo.equals("6419")||Activo.equals("6420")||Activo.equals("6421")||Activo.equals("6422")||Activo.equals("6423")||Activo.equals("6424")||Activo.equals("6425")||Activo.equals("6426")||Activo.equals("6427")||Activo.equals("6428")||Activo.equals("6429")||Activo.equals("6430")||Activo.equals("6431")||Activo.equals("6432")||Activo.equals("6433")||Activo.equals("6434")||Activo.equals("6435")||Activo.equals("6436")||Activo.equals("6437")||Activo.equals("6438")||Activo.equals("6439")||Activo.equals("6440")||Activo.equals("6444")||Activo.equals("6452")||Activo.equals("6454")||Activo.equals("6455")||Activo.equals("6461")
                ||Activo.equals("6464")||Activo.equals("6495")||Activo.equals("6500")||Activo.equals("6501")||Activo.equals("6502")||Activo.equals("6526")||Activo.equals("6527")||Activo.equals("6528")||Activo.equals("6535")||Activo.equals("6536")||Activo.equals("6537")||Activo.equals("6571")||Activo.equals("6572")||Activo.equals("6576")||Activo.equals("6577")||Activo.equals("6579")||Activo.equals("6586")||Activo.equals("6589")||Activo.equals("6590")||Activo.equals("6591")||Activo.equals("6592")||Activo.equals("6599")||Activo.equals("6605")||Activo.equals("6606")||Activo.equals("6631")
        ){
            Verde = false;
            Amarillo = true;
            Naranja = false;
            Azul = false;
            tr_kminiT.setVisibility(View.VISIBLE);
            tr_kmfinT.setVisibility(View.VISIBLE);
            tr_hrT.setVisibility(View.INVISIBLE);
            actxt_actividadGas.setVisibility(View.VISIBLE);
            etxt_cantidadiniGas.setVisibility(View.VISIBLE);
            etxt_cantidadsaldoGas.setVisibility(View.VISIBLE);
            sp_tipoGas.setVisibility(View.VISIBLE);
            ly_cantidadGas.setVisibility(View.VISIBLE);
            ly_actividadGas.setVisibility(View.VISIBLE);
            txtv_tipoComb.setVisibility(View.VISIBLE);

            Toast ToastMensaje = Toast.makeText(this, "Por favor, registra los kilometros", Toast.LENGTH_SHORT);
            View toastView = ToastMensaje.getView();
            toastView.setBackgroundResource(R.drawable.spinner_style);
            ToastMensaje.show();
        }
        //Horometro NARANJA
        if (Activo.equals("0978")||Activo.equals("0987")||Activo.equals("0989")||Activo.equals("1001")||Activo.equals("1002")||Activo.equals("1003")||Activo.equals("1004")||Activo.equals("1005")||Activo.equals("1006")||Activo.equals("1007")||Activo.equals("1008")||Activo.equals("1009")||Activo.equals("1010")||Activo.equals("1011")||Activo.equals("1012")||Activo.equals("1028")||Activo.equals("1039")||Activo.equals("1040")||Activo.equals("1041")||Activo.equals("1042")||Activo.equals("1044")||Activo.equals("1049")||Activo.equals("1050")||Activo.equals("1052")||Activo.equals("1053")||Activo.equals("1057")||Activo.equals("1058")
                ||Activo.equals("1060")||Activo.equals("2025")||Activo.equals("2026")||Activo.equals("6007")||Activo.equals("6013")||Activo.equals("6168")||Activo.equals("6169")||Activo.equals("6170")||Activo.equals("6187")||Activo.equals("6235")||Activo.equals("6239")||Activo.equals("6240")||Activo.equals("6247")||Activo.equals("6248")||Activo.equals("6249")||Activo.equals("6250")||Activo.equals("6251")||Activo.equals("6252")||Activo.equals("6253")||Activo.equals("6254")||Activo.equals("6255")||Activo.equals("6256")||Activo.equals("6257")||Activo.equals("6258")||Activo.equals("6274")||Activo.equals("6308")||Activo.equals("6340")
                ||Activo.equals("6345")||Activo.equals("6346")||Activo.equals("6463")||Activo.equals("6467")||Activo.equals("6475")||Activo.equals("6538")||Activo.equals("6539")||Activo.equals("6540")||Activo.equals("6569")||Activo.equals("6588")||Activo.equals("6617")||Activo.equals("6618")||Activo.equals("6636")
        ){
            Verde = false;
            Amarillo = false;
            Naranja = true;
            Azul = false;
            tr_kminiT.setVisibility(View.INVISIBLE);
            tr_kmfinT.setVisibility(View.INVISIBLE);
            tr_hrT.setVisibility(View.VISIBLE);
            actxt_actividadGas.setVisibility(View.VISIBLE);
            etxt_cantidadiniGas.setVisibility(View.VISIBLE);
            etxt_cantidadsaldoGas.setVisibility(View.VISIBLE);
            sp_tipoGas.setVisibility(View.VISIBLE);
            ly_cantidadGas.setVisibility(View.VISIBLE);
            ly_actividadGas.setVisibility(View.VISIBLE);
            txtv_tipoComb.setVisibility(View.VISIBLE);

            Toast ToastMensaje = Toast.makeText(this, "Por favor, registra el horometro", Toast.LENGTH_SHORT);
            View toastView = ToastMensaje.getView();
            toastView.setBackgroundResource(R.drawable.spinner_style);
            ToastMensaje.show();
        }
        //sin gasolina AZUL
        if (Activo.equals("0010")||Activo.equals("0012")||Activo.equals("0022")||Activo.equals("0701")||Activo.equals("0953")||Activo.equals("0960")||Activo.equals("0961")||Activo.equals("0962")||Activo.equals("0963")||Activo.equals("0964")||Activo.equals("0965")||Activo.equals("0968")||Activo.equals("0972")||Activo.equals("0973")||Activo.equals("0974")||Activo.equals("0975")||Activo.equals("0983")||Activo.equals("0984")||Activo.equals("0985")||Activo.equals("0986")||Activo.equals("6230")||Activo.equals("6231")||Activo.equals("0994")||Activo.equals("0995")||Activo.equals("0997")||Activo.equals("0998")||Activo.equals("0999")||Activo.equals("1013")||Activo.equals("1021")||Activo.equals("1022")||Activo.equals("1023")||Activo.equals("1026")||Activo.equals("1027")||Activo.equals("1029")||Activo.equals("1030")||Activo.equals("1061")||Activo.equals("1062")||Activo.equals("1063")||Activo.equals("1064")||Activo.equals("1065")||Activo.equals("1066")||Activo.equals("1067")||Activo.equals("1068")||Activo.equals("2027")||Activo.equals("2028")||Activo.equals("2029")||Activo.equals("2035")||Activo.equals("2036")||Activo.equals("2037")||Activo.equals("2038")||Activo.equals("2040")||Activo.equals("2042")||Activo.equals("2043")||Activo.equals("2045")||Activo.equals("2048")||Activo.equals("2049")||Activo.equals("2051")||Activo.equals("2053")||Activo.equals("2054")||Activo.equals("2055")||Activo.equals("2061")||Activo.equals("2062")
                ||Activo.equals("2063")||Activo.equals("2064")||Activo.equals("2065")||Activo.equals("2069")||Activo.equals("2070")||Activo.equals("3001")||Activo.equals("3002")||Activo.equals("3003")||Activo.equals("3004")||Activo.equals("3005")||Activo.equals("3006")||Activo.equals("3007")||Activo.equals("3008")||Activo.equals("3009")||Activo.equals("3010")||Activo.equals("3011")||Activo.equals("3012")||Activo.equals("3013")||Activo.equals("3014")||Activo.equals("3015")||Activo.equals("3016")||Activo.equals("3017")||Activo.equals("3018")||Activo.equals("3019")||Activo.equals("3020")||Activo.equals("3021")||Activo.equals("3022")||Activo.equals("3023")||Activo.equals("3026")||Activo.equals("3027")||Activo.equals("3028")||Activo.equals("3029")||Activo.equals("3030")||Activo.equals("3031")||Activo.equals("3032")||Activo.equals("3033")||Activo.equals("4001")||Activo.equals("4002")||Activo.equals("4003")||Activo.equals("4004")||Activo.equals("4005")||Activo.equals("4006")||Activo.equals("4007")||Activo.equals("4008")||Activo.equals("4009")||Activo.equals("4010")||Activo.equals("4011")||Activo.equals("4012")||Activo.equals("4013")||Activo.equals("4014")||Activo.equals("4015")||Activo.equals("4016")||Activo.equals("4017")||Activo.equals("4018")||Activo.equals("4019")||Activo.equals("4020")||Activo.equals("4021")||Activo.equals("4022")||Activo.equals("4023")||Activo.equals("4024")||Activo.equals("4025")||Activo.equals("4026")
                ||Activo.equals("4027")||Activo.equals("4028")||Activo.equals("4029")||Activo.equals("4030")||Activo.equals("4031")||Activo.equals("4032")||Activo.equals("4033")||Activo.equals("4034")||Activo.equals("4035")||Activo.equals("4036")||Activo.equals("4037")||Activo.equals("4038")||Activo.equals("4039")||Activo.equals("4040")||Activo.equals("4041")||Activo.equals("4042")||Activo.equals("4043")||Activo.equals("4044")||Activo.equals("4045")||Activo.equals("4046")||Activo.equals("4047")||Activo.equals("4048")||Activo.equals("4049")||Activo.equals("4050")||Activo.equals("4051")||Activo.equals("4052")||Activo.equals("4053")||Activo.equals("4054")||Activo.equals("4055")||Activo.equals("4056")||Activo.equals("4057")||Activo.equals("4058")||Activo.equals("4059")||Activo.equals("4060")||Activo.equals("4061")||Activo.equals("4062")||Activo.equals("4063")||Activo.equals("4064")||Activo.equals("4065")||Activo.equals("4066")||Activo.equals("4067")||Activo.equals("4068")||Activo.equals("4069")||Activo.equals("4070")||Activo.equals("4071")||Activo.equals("4072")||Activo.equals("4073")||Activo.equals("4074")||Activo.equals("4075")||Activo.equals("4076")||Activo.equals("4077")||Activo.equals("4078")||Activo.equals("4079")||Activo.equals("4080")||Activo.equals("4081")||Activo.equals("4082")||Activo.equals("4083")||Activo.equals("4084")||Activo.equals("4085")||Activo.equals("4086")||Activo.equals("4087")||Activo.equals("4088")
                ||Activo.equals("4089")||Activo.equals("4090")||Activo.equals("4091")||Activo.equals("4092")||Activo.equals("4093")||Activo.equals("4094")||Activo.equals("4095")||Activo.equals("4096")||Activo.equals("4097")||Activo.equals("4098")||Activo.equals("4099")||Activo.equals("4100")||Activo.equals("4101")||Activo.equals("4102")||Activo.equals("4103")||Activo.equals("4104")||Activo.equals("4105")||Activo.equals("4106")||Activo.equals("4107")||Activo.equals("4108")||Activo.equals("4109")||Activo.equals("4110")||Activo.equals("4111")||Activo.equals("4112")||Activo.equals("4113")||Activo.equals("4114")||Activo.equals("4115")||Activo.equals("4116")||Activo.equals("4117")||Activo.equals("4118")||Activo.equals("4119")||Activo.equals("4120")||Activo.equals("4121")||Activo.equals("4122")||Activo.equals("4123")||Activo.equals("4124")||Activo.equals("4125")||Activo.equals("4126")||Activo.equals("4127")||Activo.equals("4128")||Activo.equals("4129")||Activo.equals("4130")||Activo.equals("4131")||Activo.equals("4132")||Activo.equals("4133")||Activo.equals("4134")||Activo.equals("4135")||Activo.equals("4136")||Activo.equals("4137")||Activo.equals("4138")||Activo.equals("4139")||Activo.equals("4140")||Activo.equals("4141")||Activo.equals("4142")||Activo.equals("4143")||Activo.equals("5020")||Activo.equals("5022")||Activo.equals("5023")||Activo.equals("5024")||Activo.equals("5025")||Activo.equals("5026")||Activo.equals("5027")
                ||Activo.equals("5028")||Activo.equals("6002")||Activo.equals("6003")||Activo.equals("6004")||Activo.equals("6005")||Activo.equals("6030")||Activo.equals("6034")||Activo.equals("6037")||Activo.equals("6038")||Activo.equals("6039")||Activo.equals("6041")||Activo.equals("6043")||Activo.equals("6044")||Activo.equals("6050")||Activo.equals("6064")||Activo.equals("6065")||Activo.equals("6085")||Activo.equals("6086")||Activo.equals("6087")||Activo.equals("6088")||Activo.equals("6093")||Activo.equals("6094")||Activo.equals("6095")||Activo.equals("6096")||Activo.equals("6097")||Activo.equals("6098")||Activo.equals("6099")||Activo.equals("6100")||Activo.equals("6101")||Activo.equals("6102")||Activo.equals("6103")||Activo.equals("6104")||Activo.equals("6105")||Activo.equals("6106")||Activo.equals("6107")||Activo.equals("6108")||Activo.equals("6109")||Activo.equals("6110")||Activo.equals("6111")||Activo.equals("6112")||Activo.equals("6113")||Activo.equals("6114")||Activo.equals("6115")||Activo.equals("6116")||Activo.equals("6117")||Activo.equals("6118")||Activo.equals("6119")||Activo.equals("6120")||Activo.equals("6121")||Activo.equals("6130")||Activo.equals("6131")||Activo.equals("6132")||Activo.equals("6133")||Activo.equals("6134")||Activo.equals("6135")||Activo.equals("6136")||Activo.equals("6137")||Activo.equals("6138")||Activo.equals("6139")||Activo.equals("6140")||Activo.equals("6141")||Activo.equals("6142")
                ||Activo.equals("6143")||Activo.equals("6144")||Activo.equals("6145")||Activo.equals("6146")||Activo.equals("6147")||Activo.equals("6148")||Activo.equals("6149")||Activo.equals("6150")||Activo.equals("6151")||Activo.equals("6152")||Activo.equals("6153")||Activo.equals("6154")||Activo.equals("6155")||Activo.equals("6156")||Activo.equals("6159")||Activo.equals("6160")||Activo.equals("6167")||Activo.equals("6181")||Activo.equals("6186")||Activo.equals("6188")||Activo.equals("6191")||Activo.equals("6192")||Activo.equals("6193")||Activo.equals("6194")||Activo.equals("6195")||Activo.equals("6196")||Activo.equals("6197")||Activo.equals("6198")||Activo.equals("6200")||Activo.equals("6201")||Activo.equals("6202")||Activo.equals("6203")||Activo.equals("6204")||Activo.equals("6205")||Activo.equals("6206")||Activo.equals("6207")||Activo.equals("6208")||Activo.equals("6210")||Activo.equals("6211")||Activo.equals("6212")||Activo.equals("6213")||Activo.equals("6214")||Activo.equals("6216")||Activo.equals("6217")||Activo.equals("6218")||Activo.equals("6219")||Activo.equals("6220")||Activo.equals("6221")||Activo.equals("6222")||Activo.equals("6223")||Activo.equals("6224")||Activo.equals("6227")||Activo.equals("6228")||Activo.equals("6236")||Activo.equals("6237")||Activo.equals("6238")||Activo.equals("6242")||Activo.equals("6267")||Activo.equals("6268")||Activo.equals("6269")||Activo.equals("6270")||Activo.equals("6271")
                ||Activo.equals("6272")||Activo.equals("6276")||Activo.equals("6278")||Activo.equals("6279")||Activo.equals("6280")||Activo.equals("6281")||Activo.equals("6294")||Activo.equals("6295")||Activo.equals("6296")||Activo.equals("6297")||Activo.equals("6307")||Activo.equals("6323")||Activo.equals("6324")||Activo.equals("6332")||Activo.equals("6333")||Activo.equals("6334")||Activo.equals("6335")||Activo.equals("6337")||Activo.equals("6338")||Activo.equals("6339")||Activo.equals("6341")||Activo.equals("6342")||Activo.equals("6354")||Activo.equals("6355")||Activo.equals("6358")||Activo.equals("6359")||Activo.equals("6360")||Activo.equals("6362")||Activo.equals("6366")||Activo.equals("6367")||Activo.equals("6375")||Activo.equals("6378")||Activo.equals("6379")||Activo.equals("6380")||Activo.equals("6381")||Activo.equals("6384")||Activo.equals("6386")||Activo.equals("6387")||Activo.equals("6388")||Activo.equals("6389")||Activo.equals("6390")||Activo.equals("6401")||Activo.equals("6403")||Activo.equals("6404")||Activo.equals("6405")||Activo.equals("6406")||Activo.equals("6410")||Activo.equals("6411")||Activo.equals("6413")||Activo.equals("6441")||Activo.equals("6443")||Activo.equals("6445")||Activo.equals("6446")||Activo.equals("6447")||Activo.equals("6448")||Activo.equals("6449")||Activo.equals("6450")||Activo.equals("6451")||Activo.equals("6459")||Activo.equals("6460")||Activo.equals("6465")||Activo.equals("6466")
                ||Activo.equals("6472")||Activo.equals("6473")||Activo.equals("6474")||Activo.equals("6476")||Activo.equals("6477")||Activo.equals("6479")||Activo.equals("6480")||Activo.equals("6481")||Activo.equals("6482")||Activo.equals("6483")||Activo.equals("6490")||Activo.equals("6491")||Activo.equals("6492")||Activo.equals("6493")||Activo.equals("6494")||Activo.equals("6496")||Activo.equals("6497")||Activo.equals("6499")||Activo.equals("6503")||Activo.equals("6504")||Activo.equals("6505")||Activo.equals("6506")||Activo.equals("6507")||Activo.equals("6508")||Activo.equals("6509")||Activo.equals("6510")||Activo.equals("6512")||Activo.equals("6513")||Activo.equals("6514")||Activo.equals("6515")||Activo.equals("6516")||Activo.equals("6517")||Activo.equals("6518")||Activo.equals("6519")||Activo.equals("6522")||Activo.equals("6523")||Activo.equals("6524")||Activo.equals("6525")||Activo.equals("6530")||Activo.equals("6531")||Activo.equals("6532")||Activo.equals("6533")||Activo.equals("6534")||Activo.equals("6541")||Activo.equals("6542")||Activo.equals("6543")||Activo.equals("6547")||Activo.equals("6548")||Activo.equals("6549")||Activo.equals("6550")||Activo.equals("6551")||Activo.equals("6552")||Activo.equals("6553")||Activo.equals("6554")||Activo.equals("6555")||Activo.equals("6556")||Activo.equals("6557")||Activo.equals("6561")||Activo.equals("6563")||Activo.equals("6564")||Activo.equals("6565")||Activo.equals("6566")
                ||Activo.equals("6573")||Activo.equals("6574")||Activo.equals("6593")||Activo.equals("6594")||Activo.equals("6595")||Activo.equals("6596")||Activo.equals("6597")||Activo.equals("6598")||Activo.equals("6602")||Activo.equals("6603")||Activo.equals("6604")||Activo.equals("6607")||Activo.equals("6608")||Activo.equals("6609")||Activo.equals("6614")||Activo.equals("6615")||Activo.equals("6616")||Activo.equals("6629")||Activo.equals("6630")||Activo.equals("6632")||Activo.equals("6633")||Activo.equals("6634")||Activo.equals("6642")||Activo.equals("6645")||Activo.equals("6647")
        ){
            Verde = false;
            Amarillo = false;
            Naranja = false;
            Azul = true;
            tr_kminiT.setVisibility(View.INVISIBLE);
            tr_kmfinT.setVisibility(View.INVISIBLE);
            tr_hrT.setVisibility(View.INVISIBLE);
            actxt_actividadGas.setVisibility(View.INVISIBLE);
            etxt_cantidadiniGas.setVisibility(View.INVISIBLE);
            etxt_cantidadsaldoGas.setVisibility(View.INVISIBLE);
            sp_tipoGas.setVisibility(View.INVISIBLE);
            ly_cantidadGas.setVisibility(View.INVISIBLE);
            ly_actividadGas.setVisibility(View.INVISIBLE);
            txtv_tipoComb.setVisibility(View.INVISIBLE);

            Toast ToastMensaje = Toast.makeText(this, "Sin consumo de gasolina", Toast.LENGTH_SHORT);
            View toastView = ToastMensaje.getView();
            toastView.setBackgroundResource(R.drawable.spinner_style);
            ToastMensaje.show();
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
        ItemSPHue.add(new ItemDatoSpinner("Huerta"));
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
                ItemSPHue.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" - "+Renglon.getString(2)));
            }while(Renglon.moveToNext());
        }else{
            Toast ToastMensaje = Toast.makeText(this, "No se encontraron datos en huertas", Toast.LENGTH_SHORT);
            View toastView = ToastMensaje.getView();
            toastView.setBackgroundResource(R.drawable.spinner_style);
            toastView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ToastMensaje.show();
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

        Renglon = BD.rawQuery("select AH.c_codigo_act,AH.v_nombre_act,AH.c_codigo_cam" +
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
       boolean Falta = false, FaltaFolio = false;
        String fechaini=etxt_fechainiGas.getText().toString().replace("/","");
        String fechafin=etxt_fechafinGas.getText().toString().replace("/","");
        String tipogas=CopiTipo.getItem(sp_tipoGas.getSelectedItemPosition()).getTexto().replace("-", "");
       String Mensaje = "";
       if (Verde == true){
           Log.e("Verde",Verde.toString());
           etxt_horometroGas.setText("00:00");
           etxt_kminiGas.setText("0.0");
           etxt_kmfinGas.setText("0.0");
       }//fin VERDE
        if (Amarillo == true){
            Log.e("Amarillo",Amarillo.toString());
            etxt_horometroGas.setText("00:00");
            if (etxt_kmfinGas.getText().length() > 0){ }else{
                Falta = true;
                Mensaje = "Falta ingresar KILOMETROS FINALES, Verifica por favor.";
            }if (etxt_kminiGas.getText().length() > 0){ }else{
                Falta = true;
                Mensaje = "Falta ingresar KILOMETROS INICIALES, Verifica por favor.";
            }
        }//fin Amarillo
        if (Naranja == true){
            Log.e("Naranja",Naranja.toString());
            etxt_kminiGas.setText("0.0");
            etxt_kmfinGas.setText("0.0");
            if (etxt_horometroGas.getText().length() > 0){ }else{
                Falta = true;
                Mensaje = "Falta ingresar HOROMETROS, Verifica por favor.";
            }
        }//fin Naranja
        if (etxt_observacionesGas.getText().length() > 0){ }else{
            Falta = true;
            Mensaje = "Falta ingresar OBSERVACIONES, Verifica por favor.";
        }if (sp_tipoGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar TIPO, Verifica por favor.";
        }if (etxt_cantidadsaldoGas.getText().length() > 0){ }else{
            Falta = true;
            Mensaje = "Falta ingresar CANTIDAD SALDO, Verifica por favor.";
        }if (etxt_cantidadiniGas.getText().length() > 0){ }else{
            Falta = true;
            Mensaje = "Falta ingresar CANTIDAD INICIAL, Verifica por favor.";
        }if (actxt_actividadGas.getText().length() > 0){ }else {
            Falta = true;
            Mensaje = "Falta ingresar ACTIVIDAD, Verifica por favor.";
        }if (sp_responsableGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar RESPONSABLE, Verifica por favor.";
        }if (sp_activoGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar ACTIVO, Verifica por favor.";
        }if (sp_huertaGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar HUERTA, Verifica por favor.";
        }if(sp_empresaGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar EMPRESA, Verifica por favor.";
        }if (etxt_fechafinGas.getText().length() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar FECHA SALIDA, Verifica por favor.";
        }if(etxt_fechainiGas.getText().length() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar FECHA INGRESO, Verifica por favor.";
        }if (etxt_folioGas.getText().length() > 0){ }else{
            FaltaFolio = true;
        }
        if (Azul == true){
            Log.e("Azul",Azul.toString());
            Falta = true;
            Mensaje = "El activo seleccionado NO consume gasolina, Verifica por favor.";
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Gasolina.this, R.style.Theme_AppCompat_Dialog_Alert);
            dialogo1.setTitle("NO CONSUME GASOLINA");
            dialogo1.setMessage("El activo seleccionado NO consume gasolina. \n Verifica por favor.");
            dialogo1.setCancelable(true);
            dialogo1.show();
        }//fin Azul

        if (!Falta){
            AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
            ContentValues registroGas = new ContentValues();
            if (FaltaFolio){
                AlertDialog.Builder dialogo = new AlertDialog.Builder(Gasolina.this, R.style.Theme_AppCompat_Dialog_Alert);
                dialogo.setTitle("NO SE HA INGRESADO FOLIO");
                dialogo.setMessage("No ha ingresado folio, si selecciona ACEPTAR se agregará el registro sin folio. \n   ¿Desea continuar?");
                dialogo.setCancelable(true);
                Toast ToastMensaje = Toast.makeText(this,"Agregado a la Base de Datos",Toast.LENGTH_SHORT);
                dialogo.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registroGas.put("c_folio_gas", etxt_folioGas.getText().toString());
                        registroGas.put("d_fechainicio_gas", fechaini);
                        registroGas.put("d_fechafin_gas", fechafin);
                        registroGas.put("c_codigo_eps", CopiEmp.getItem(sp_empresaGas.getSelectedItemPosition()).getTexto().substring(0,2));//c_codigo_eps
                        registroGas.put("Id_Huerta", CopiHue.getItem(sp_huertaGas.getSelectedItemPosition()).getTexto().substring(0,5));//Id_Huerta
                        registroGas.put("Id_ActivosGas", CopiActivo.getItem(sp_activoGas.getSelectedItemPosition()).getTexto().substring(0,4));//Id_ActivosGas
                        registroGas.put("c_codigo_emp",CopiResp.getItem(sp_responsableGas.getSelectedItemPosition()).getTexto().substring(0,6));//c_codigo_emp
                        registroGas.put("c_codigo_act", actxt_actividadGas.getText().toString().substring(0,4));//c_codigo_act
                        registroGas.put("v_cantingreso_gas", etxt_cantidadiniGas.getText().toString());
                        registroGas.put("v_cantsaldo_gas", etxt_cantidadsaldoGas.getText().toString());
                        registroGas.put("v_tipo_gas", tipogas);
                        registroGas.put("v_horometro_gas", etxt_horometroGas.getText().toString());
                        registroGas.put("v_kminicial_gas", etxt_kminiGas.getText().toString());
                        registroGas.put("v_kmfinal_gas", etxt_kmfinGas.getText().toString());
                        registroGas.put("v_observaciones_gas", etxt_observacionesGas.getText().toString());
                        Log.e("Registro", registroGas.toString());
                        BD.insert("t_Consumo_Gasolina",null,registroGas);
                        View toastView = ToastMensaje.getView();
                        toastView.setBackgroundResource(R.drawable.spinner_style);
                        toastView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        ToastMensaje.show();

                        limpiar();
                    }
                });
                Toast ToastMensaje2 = Toast.makeText(this,"Registra el Folio por favor",Toast.LENGTH_SHORT);
                dialogo.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View toastView = ToastMensaje2.getView();
                        toastView.setBackgroundResource(R.drawable.spinner_style);
                        toastView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        ToastMensaje2.show();
                    }
                });
                dialogo.show();

            }else{
                registroGas.put("c_folio_gas", etxt_folioGas.getText().toString());
                registroGas.put("d_fechainicio_gas", fechaini);
                registroGas.put("d_fechafin_gas", fechafin);
                registroGas.put("c_codigo_eps", CopiEmp.getItem(sp_empresaGas.getSelectedItemPosition()).getTexto().substring(0,2));//c_codigo_eps
                registroGas.put("Id_Huerta", CopiHue.getItem(sp_huertaGas.getSelectedItemPosition()).getTexto().substring(0,5));//Id_Huerta
                registroGas.put("Id_ActivosGas", CopiActivo.getItem(sp_activoGas.getSelectedItemPosition()).getTexto().substring(0,4));//Id_ActivosGas
                registroGas.put("c_codigo_emp",CopiResp.getItem(sp_responsableGas.getSelectedItemPosition()).getTexto().substring(0,6));//c_codigo_emp
                registroGas.put("c_codigo_act", actxt_actividadGas.getText().toString().substring(0,4));//c_codigo_act
                registroGas.put("v_cantingreso_gas", etxt_cantidadiniGas.getText().toString());
                registroGas.put("v_cantsaldo_gas", etxt_cantidadsaldoGas.getText().toString());
                registroGas.put("v_tipo_gas", tipogas);
                registroGas.put("v_horometro_gas", etxt_horometroGas.getText().toString());
                registroGas.put("v_kminicial_gas", etxt_kminiGas.getText().toString());
                registroGas.put("v_kmfinal_gas", etxt_kmfinGas.getText().toString());
                registroGas.put("v_observaciones_gas", etxt_observacionesGas.getText().toString());
                Log.e("Registro", registroGas.toString());
                BD.insert("t_Consumo_Gasolina",null,registroGas);

                Toast ToastMensaje3 = Toast.makeText(this,"Agregado a la Base de Datos",Toast.LENGTH_SHORT);
                View toastView = ToastMensaje3.getView();
                toastView.setBackgroundResource(R.drawable.spinner_style);
                toastView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ToastMensaje3.show();

                limpiar();
            }

        }else{
            Toast ToastMensaje = Toast.makeText(this,Mensaje,Toast.LENGTH_SHORT);
            View toastView = ToastMensaje.getView();
            toastView.setBackgroundResource(R.drawable.spinner_style);
            toastView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ToastMensaje.show();
        }
    }

    private void cargaGrid(){
        lv_GridGasolina.setAdapter(null);
        arrayGas.clear(); //se usa para guardar los datos y cargarlos en el grid

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select G.c_folio_gas,\n" +
                "\tG.d_fechainicio_gas,\n"+
                "\tG.d_fechafin_gas,\n"+
                "\tG.c_codigo_eps,\n"+
                "\tG.Id_Huerta,\n"+
                "\tG.Id_ActivosGas,\n"+
                "\tG.c_codigo_emp,\n"+
                "\tG.c_codigo_act,\n"+
                "\tG.v_cantingreso_gas,\n"+
                "\tG.v_cantsaldo_gas,\n"+
                "\tG.v_tipo_gas,\n"+
                "\tG.v_horometro_gas,\n"+
                "\tG.v_kminicial_gas,\n"+
                "\tG.v_kmfinal_gas,\n"+
                "\tG.v_observaciones_gas \n"+
                "from t_Consumo_Gasolina as G",null);

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
                Toast.makeText(this, "No hay datos en t_Consumo_Gasolina guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }

        if(arrayGas.size()>0){
            Adapter=new Adaptador_GridGasolina(getApplicationContext(),arrayGas);
            lv_GridGasolina.setAdapter(Adapter);
        }else{
            //Toast.makeText(Gasolina.this, "No existen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

    private void limpiar(){
        etxt_folioGas.setText("");
        etxt_fechainiGas.setText("");
        etxt_fechafinGas.setText("");
        sp_empresaGas.setSelection(0);
        sp_huertaGas.setSelection(0);
        sp_activoGas.setSelection(0);
        sp_responsableGas.setSelection(0);
        actxt_actividadGas.setText("");
        etxt_cantidadiniGas.setText("");
        etxt_cantidadsaldoGas.setText("");
        sp_tipoGas.setSelection(0);
        etxt_observacionesGas.setText("");
        etxt_horometroGas.setText("");
        etxt_kminiGas.setText("");
        etxt_kmfinGas.setText("");
    }
}