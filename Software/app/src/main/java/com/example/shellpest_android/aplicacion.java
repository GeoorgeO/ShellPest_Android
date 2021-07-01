package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class aplicacion extends AppCompatActivity implements View.OnClickListener {

    public String Usuario, Perfil, Huerta;

    Spinner sp_TipoAplicacion, sp_Presentacion,sp_huerta;
    AutoCompleteTextView actv_Productos;
    TextView text_Codigo;
    EditText etd_Fecha,pt_Observaciones,etn_ApliCantidad,etn_Pipadas;
    ListView lv_GridAplicacion;

    private ArrayList<ItemDatoSpinner> ItemSPApli, ItemSPPre, ItemSPHue;
    private AdaptadorSpinner CopiApli, CopiPre,CopiHue;
    private ArrayAdapter Adaptador_Arreglos;
    private ArrayList<String> ArrayProductos;

    Itemaplicacion Tabla;
    Adaptador_GridAplicacion Adapter;
    ArrayList<Itemaplicacion> arrayArticulos;

    private int dia,mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicacion);

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        getSupportActionBar().hide();

        sp_TipoAplicacion = (Spinner) findViewById(R.id.sp_TipoAplicacion);
        sp_Presentacion = (Spinner) findViewById(R.id.sp_Presentacion);
        sp_huerta = (Spinner) findViewById(R.id.sp_huerta);
        text_Codigo = (TextView) findViewById(R.id.text_Codigo);
        lv_GridAplicacion = (ListView) findViewById(R.id.lv_GridAplicacion);
        pt_Observaciones = (EditText) findViewById(R.id.pt_Observaciones);
        etn_ApliCantidad = (EditText) findViewById(R.id.etn_ApliCantidad);
        etn_Pipadas = (EditText) findViewById(R.id.etn_Pipadas);
        etd_Fecha = (EditText) findViewById(R.id.etd_Fecha);
        actv_Productos = (AutoCompleteTextView) findViewById(R.id.actv_Productos);

        ItemSPHue = new ArrayList<>();
        ItemSPHue.add(new ItemDatoSpinner("Huerta"));
        ItemSPHue.add(new ItemDatoSpinner("Arroyos"));
        ItemSPHue.add(new ItemDatoSpinner("Chimilpa"));
        ItemSPHue.add(new ItemDatoSpinner("Fontana"));
        CopiHue = new AdaptadorSpinner(this, ItemSPHue);
        sp_huerta.setAdapter(CopiHue);

        ItemSPApli = new ArrayList<>();
        ItemSPApli.add(new ItemDatoSpinner("Tipo de Aplicacion"));
        ItemSPApli.add(new ItemDatoSpinner("Hectaria"));
        ItemSPApli.add(new ItemDatoSpinner("Pipada"));
        CopiApli = new AdaptadorSpinner(this, ItemSPApli);
        sp_TipoAplicacion.setAdapter(CopiApli);

        ItemSPPre = new ArrayList<>();
        ItemSPPre.add(new ItemDatoSpinner("Presentacion"));
        ItemSPPre.add(new ItemDatoSpinner("Kilogramos"));
        ItemSPPre.add(new ItemDatoSpinner("2000 Litros"));
        CopiPre = new AdaptadorSpinner(this, ItemSPPre);
        sp_Presentacion.setAdapter(CopiPre);

        ArrayProductos=new ArrayList<>();
        ArrayProductos.add(new String("Agrex abc  Acidificante"));
        ArrayProductos.add(new String("INEX-A"));
        ArrayProductos.add(new String("ACIDO NITRICO"));
        ArrayProductos.add(new String("Agrex-F"));
        ArrayProductos.add(new String("Medal"));
        ArrayProductos.add(new String("Xpander Plus"));
        ArrayProductos.add(new String("Acigib "));
        ArrayProductos.add(new String("Pardy "));

        Adaptador_Arreglos=new ArrayAdapter(this, android.R.layout.simple_list_item_1,ArrayProductos);
        actv_Productos.setAdapter(Adaptador_Arreglos);

        etd_Fecha=(EditText)findViewById(R.id.etd_Fecha);
        etd_Fecha.setOnClickListener(this);

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        etd_Fecha.setText(objSDF.format(date1));
        etd_Fecha.setInputType(InputType.TYPE_NULL);
        etd_Fecha.requestFocus();

        arrayArticulos = new ArrayList<>();
    }
    public void onClick(View view) {
        if(view==etd_Fecha){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> etd_Fecha.setText(i2+"/"+(i1+1)+"/"+i),anio,mes,dia);
            dtpd.show();
        }
    }
}