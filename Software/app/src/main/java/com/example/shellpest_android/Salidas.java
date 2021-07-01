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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Salidas extends AppCompatActivity  implements View.OnClickListener{

    public String Usuario, Perfil, Huerta;
    Spinner sp_Empresa, sp_Almacen, sp_Responsable,sp_Unidad,sp_Bloque;
    AutoCompleteTextView actv_Producto;
    EditText et_Fecha,etn_Cantidad;
    ListView lv_GridSalidas;
    private int dia,mes, anio;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPAlm, ItemSPRps,ItemUni,ItemBlo;
    private ArrayList<String> ArrayProductos;
    private AdaptadorSpinner CopiEmp, CopiAlm, CopiRps,CopiUni,CopiBlo;
    private ArrayAdapter Adaptador_Arreglos;

    Itemsalida Tabla;
    Adaptador_GridSalida Adapter;
    ArrayList<Itemsalida> arrayArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salidas);

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        getSupportActionBar().hide();

        sp_Empresa = (Spinner) findViewById(R.id.sp_Empresa);
        sp_Almacen = (Spinner) findViewById(R.id.sp_Almacen);
        sp_Responsable = (Spinner) findViewById(R.id.sp_Responsable);
        lv_GridSalidas = (ListView) findViewById(R.id.lv_GridSalidas);
        sp_Unidad = (Spinner) findViewById(R.id.sp_Unidad);
        sp_Bloque = (Spinner) findViewById(R.id.sp_Bloque);
        etn_Cantidad = (EditText) findViewById(R.id.etn_Cantidad);
        actv_Producto = (AutoCompleteTextView) findViewById(R.id.actv_Producto);

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

        ItemUni = new ArrayList<>();
        ItemUni.add(new ItemDatoSpinner("Unidad"));
        ItemUni.add(new ItemDatoSpinner("Kilogramo"));
        ItemUni.add(new ItemDatoSpinner("Litro"));
        ItemUni.add(new ItemDatoSpinner("Pieza"));
        CopiUni = new AdaptadorSpinner(this, ItemUni);
        sp_Unidad.setAdapter(CopiUni);

        ItemBlo = new ArrayList<>();
        ItemBlo.add(new ItemDatoSpinner("Centro Costos"));
        ItemBlo.add(new ItemDatoSpinner("Bloque 1"));
        ItemBlo.add(new ItemDatoSpinner("Bloque 2"));
        ItemBlo.add(new ItemDatoSpinner("Bloque 3"));
        CopiBlo = new AdaptadorSpinner(this, ItemBlo);
        sp_Bloque.setAdapter(CopiBlo);

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
        actv_Producto.setAdapter(Adaptador_Arreglos);

        et_Fecha=(EditText)findViewById(R.id.et_Fecha);
        et_Fecha.setOnClickListener(this);

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        et_Fecha.setText(objSDF.format(date1));
        et_Fecha.setInputType(InputType.TYPE_NULL);
        et_Fecha.requestFocus();

        arrayArticulos = new ArrayList<>();
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

    public void agregargrid(View view){
        lv_GridSalidas.setAdapter(null);
        String fecha,nproducto,cantidad,nunidad,nbloque,cproducto,cunidad;
        fecha=et_Fecha.getText().toString();
        nproducto=actv_Producto.getText().toString();
        cantidad=etn_Cantidad.getText().toString();
        nunidad=CopiUni.getItem(sp_Unidad.getSelectedItemPosition()).getTexto();
        nbloque=CopiBlo.getItem(sp_Bloque.getSelectedItemPosition()).getTexto();
        cproducto="00001";
        cunidad="002";
        Tabla=new Itemsalida(et_Fecha.getText().toString(),actv_Producto.getText().toString(),etn_Cantidad.getText().toString(),CopiUni.getItem(sp_Unidad.getSelectedItemPosition()).getTexto(),CopiBlo.getItem(sp_Bloque.getSelectedItemPosition()).getTexto(),"0001","002");
        arrayArticulos.add(Tabla);
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridSalida(getApplicationContext(),arrayArticulos);
            lv_GridSalidas.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }
}