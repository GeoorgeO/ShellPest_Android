package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class aplicacion extends AppCompatActivity implements View.OnClickListener {

    public String Usuario,Huerta, Perfil ,Id,UnidadPro,cepsselapli;
    int LineEmpresa,LineHuerta,LineReceta,LineTipoA,LinePresentacion;

    boolean yasemovio,seldet;
    int nseldet;

    Spinner sp_TipoAplicacion, sp_Presentacion,sp_huerta,sp_Empresa4,sp_Receta;
    AutoCompleteTextView actv_Productos;
    TextView text_Codigo,text_Aplicados,text_CantidadTotal,text_UnidadPro,text_CenCos;
    EditText etd_Fecha,pt_Observaciones,etn_ApliCantidad,etn_Pipadas;
    ListView lv_GridAplicacion;

    private ArrayList<ItemDatoSpinner> ItemSPApli, ItemSPPre, ItemSPHue,ItemSPEmp,ItemSPRec;
    private AdaptadorSpinner CopiApli, CopiPre,CopiHue,CopiEmp,CopiRec;
    private ArrayAdapter Adaptador_Arreglos;
    private ArrayList<String> ArrayProductos,ArrayProductosLimpia;

    Button btn_Agrega;

    Itemaplicacion Tabla;
    Adaptador_GridAplicacion Adapter;
    ArrayList<Itemaplicacion> arrayArticulos;

    double existencia;

    boolean[] Diaseleccionado,Diasseleccionadoclean;
    ArrayList<Integer> Listadias,ListadiasClean = new ArrayList<>();
    String[] Arreglodias,ArreglosdiasClean;

    private int dia,mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicacion);

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");
        Id = getIntent().getStringExtra("ID");
        cepsselapli=getIntent().getStringExtra("CEPS");
        getSupportActionBar().hide();

        LineEmpresa=0;
        LineHuerta=0;
        LineReceta=0;
        LineTipoA=0;
        LinePresentacion=0;

        sp_TipoAplicacion = (Spinner) findViewById(R.id.sp_TipoAplicacion);
        sp_Presentacion = (Spinner) findViewById(R.id.sp_Presentacion);
        sp_huerta = (Spinner) findViewById(R.id.sp_huerta);
        sp_Empresa4 = (Spinner) findViewById(R.id.sp_Empresa4);
        sp_Receta = (Spinner) findViewById(R.id.sp_Receta);
        text_Codigo = (TextView) findViewById(R.id.text_Codigo);
        lv_GridAplicacion = (ListView) findViewById(R.id.lv_GridAplicacion);
        pt_Observaciones = (EditText) findViewById(R.id.pt_Observaciones);
        etn_ApliCantidad = (EditText) findViewById(R.id.etn_ApliCantidad);
        etn_Pipadas = (EditText) findViewById(R.id.etn_Pipadas);
        etd_Fecha = (EditText) findViewById(R.id.etd_Fecha);
        actv_Productos = (AutoCompleteTextView) findViewById(R.id.actv_Productos);
        text_Aplicados= (TextView) findViewById(R.id.text_Aplicados);
        text_CantidadTotal= (TextView) findViewById(R.id.text_CantidadTotal);
        text_UnidadPro= (TextView) findViewById(R.id.text_UnidadPro);
        text_CenCos= (TextView) findViewById(R.id.text_CenCos);

        btn_Agrega=(Button) findViewById(R.id.btn_Agrega);

        yasemovio=false;

        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa4.setAdapter(CopiEmp);

        if (sp_Empresa4.getCount()==2){
            sp_Empresa4.setSelection(1);
        }



        existencia=0;
        /*cargaSpinnerHuertas();
        CopiHue = new AdaptadorSpinner(this, ItemSPHue);
        sp_huerta.setAdapter(CopiHue);

        if (sp_huerta.getCount()==2){
            sp_huerta.setSelection(1);
        }*/

        /*cargaSpinnerTipoAplicacion();
        CopiApli = new AdaptadorSpinner(this, ItemSPApli);
        sp_TipoAplicacion.setAdapter(CopiApli);

        if (sp_TipoAplicacion.getCount()==2){
            sp_TipoAplicacion.setSelection(1);
        }*/
        /*if (Id==null){
            ItemSPPre = new ArrayList<>();

        }*/

        nseldet=-1;


        /*cargarProductos();
        Adaptador_Arreglos=new ArrayAdapter(this, android.R.layout.simple_list_item_1,ArrayProductos);
        actv_Productos.setAdapter(Adaptador_Arreglos);*/

        etd_Fecha=(EditText)findViewById(R.id.etd_Fecha);
        etd_Fecha.setOnClickListener(this);

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        etd_Fecha.setText(objSDF.format(date1));
        etd_Fecha.setInputType(InputType.TYPE_NULL);
        etd_Fecha.requestFocus();

        etn_Pipadas.setText("1");
        etn_ApliCantidad.setText("0");

        arrayArticulos = new ArrayList<>();

        borrarAplicaciones15Days();


        actv_Productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //////////////////////////////////////Inhabilite hasta que este sincronizacion///////////////////////////////////////////////////////
                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(aplicacion.this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                Cursor Renglon;

                //String pro;
                //pro="select P.c_codigo_uni,U.v_nombre_uni from t_Productos as P left join  t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni where P.c_codigo_pro='"+actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim()+"'";
                Renglon=BD.rawQuery("select P.c_codigo_uni,U.v_nombre_uni,Texi.Existencia " +
                        "from t_Productos as P " +
                        "left join  t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni and U.c_codigo_eps=P.c_codigo_eps " +
                        "left join (select exi.c_codigo_eps,exi.c_codigo_pro,sum(exi.Existencia) as Existencia " +
                            "from t_existencias as exi " +
                            "inner join  t_Almacen as alm on alm.Id_Almacen=exi.c_codigo_alm and alm.c_codigo_eps=exi.c_codigo_eps " +
                            "where alm.Id_Huerta='"+Huerta+"' and exi.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' "+
                        "group by exi.c_codigo_eps,exi.c_codigo_pro,alm.Id_Huerta ) as Texi on ltrim(rtrim(Texi.c_codigo_pro))=ltrim(rtrim(P.c_codigo_pro)) and Texi.c_codigo_eps=p.c_codigo_eps "+
                        "where ltrim(rtrim(P.c_codigo_pro))='"+actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim()+"' and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                if(Renglon.moveToFirst()){

                    do {
                        text_UnidadPro.setText("Unidad: "+Renglon.getString(1));
                        //text_UnidadPro.setText("Unidad: "+Renglon.getString(1)+" Existencia: "+Renglon.getDouble(2));
                        UnidadPro=Renglon.getString(1)+"s";
                        existencia=Renglon.getDouble(2);
                    } while(Renglon.moveToNext());

                    BD.close();
                }else{
                    BD.close();
                }
            }

        });

        lv_GridAplicacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(nseldet==i){
                    seldet=false;
                    nseldet=-1;

                    actv_Productos.setText("");
                    etn_ApliCantidad.setText("0");
                    etn_Pipadas.setText("0");
                    //etd_Fecha.setText(arrayArticulos.get(i).getFecha());
                    text_UnidadPro.setText("Unidad:");
                    UnidadPro="";
                    text_CantidadTotal.setText("Cantidad de Producto Aplicado");
                    text_CenCos.setText("");

                }else{
                    seldet=true;
                    nseldet=i;
                    String tarrsay=arrayArticulos.get(i).getcProducto();

                    if(!arrayArticulos.get(i).getcProducto().equals("")){
                        actv_Productos.setText(arrayArticulos.get(i).getNombre_Producto().trim() +" | "+arrayArticulos.get(i).getcProducto().trim());
                    }

                    etn_ApliCantidad.setText(arrayArticulos.get(i).getCantidad());

                    etd_Fecha.setText(arrayArticulos.get(i).getFecha());
                    text_UnidadPro.setText("Unidad: "+arrayArticulos.get(i).getNombre_Unidad());
                    UnidadPro=arrayArticulos.get(i).getNombre_Unidad()+"s";
                    text_CantidadTotal.setText(String.valueOf(Double.parseDouble(etn_Pipadas.getText().toString())* Double.parseDouble(etn_ApliCantidad.getText().toString()))+" "+UnidadPro+" de Producto aplicados");
                    if (arrayArticulos.get(i).getCC()==null){
                        text_CenCos.setText("");
                    }else{
                        text_CenCos.setText(arrayArticulos.get(i).getCC().trim());
                    }

                }
            }
        });

        sp_Empresa4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(ItemSPHue==null){
                    cargaSpinnerHuertas();
                    CopiHue = new AdaptadorSpinner(aplicacion.this, ItemSPHue);
                    sp_huerta.setAdapter(CopiHue);

                    if (sp_huerta.getCount()==2){
                        sp_huerta.setSelection(1);
                    }
                }else{
                    if((ItemSPHue.size()<=1  ) || LineEmpresa!=i){
                        cargaSpinnerHuertas();
                        CopiHue = new AdaptadorSpinner(aplicacion.this, ItemSPHue);
                        sp_huerta.setAdapter(CopiHue);


                        if(LineEmpresa>0){
                            sp_huerta.setSelection(LineEmpresa);
                        }else{
                            if (sp_huerta.getCount()==2){
                                sp_huerta.setSelection(1);
                            }
                        }
                    }
                }





                /*if (sp_Receta.getCount()==2){
                    sp_Receta.setSelection(1);
                }*/

                if(Id==null){
                    if(ItemSPApli==null){
                        cargaSpinnerTipoAplicacion();
                        CopiApli = new AdaptadorSpinner(aplicacion.this, ItemSPApli);
                        sp_TipoAplicacion.setAdapter(CopiApli);

                        if (sp_TipoAplicacion.getCount()==2){
                            sp_TipoAplicacion.setSelection(1);
                        }
                    }else{
                        if(ItemSPApli.size()<=1 || LineEmpresa!=i){
                            cargaSpinnerTipoAplicacion();
                            CopiApli = new AdaptadorSpinner(aplicacion.this, ItemSPApli);
                            sp_TipoAplicacion.setAdapter(CopiApli);

                            if (sp_TipoAplicacion.getCount()==2){
                                sp_TipoAplicacion.setSelection(1);
                            }
                        }
                    }


                }

                //if(Id==null) {
                    cargarProductos();
                    Adaptador_Arreglos = new ArrayAdapter(aplicacion.this, android.R.layout.simple_list_item_1, ArrayProductos);
                    actv_Productos.setAdapter(Adaptador_Arreglos);
                //}
                LineEmpresa=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        actv_Productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actv_Productos.setText("");

            }
        });



        etn_Pipadas.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(  etn_Pipadas.getText().length()>0 && etn_ApliCantidad.getText().length()>0){
                    text_CantidadTotal.setText(String.valueOf(Double.parseDouble(etn_Pipadas.getText().toString())* Double.parseDouble(etn_ApliCantidad.getText().toString()))+" "+UnidadPro+" de Producto aplicados");
                }
                return false;
            }
        });

        etn_ApliCantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (!String.valueOf(existencia).isEmpty() && etn_ApliCantidad.getText().toString().length()>0){
                    if(Double.parseDouble(etn_ApliCantidad.getText().toString())>existencia){
                        Toast.makeText(aplicacion.this, "NO SE PUEDE APLICAR. La cantidad ingresada excede la existencia de producto.", Toast.LENGTH_SHORT).show();
                        btn_Agrega.setEnabled(false);
                    }else{
                        btn_Agrega.setEnabled(true);
                    }
                }else{

                }

                if(  etn_Pipadas.getText().length()>0 && etn_ApliCantidad.getText().length()>0){
                    text_CantidadTotal.setText(String.valueOf(Double.parseDouble(etn_Pipadas.getText().toString())* Double.parseDouble(etn_ApliCantidad.getText().toString()))+" "+UnidadPro+" de Producto aplicados");
                }

                return false;
            }
        });

        sp_huerta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0 && Id==null){
                    AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(aplicacion.this,"ShellPest",null,1);
                    SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                    Cursor Renglon;

                    String MaxCod,Consulta;
                    MaxCod="";
                    Consulta="select count(A.Id_Aplicacion) num" +
                            "                            from t_Aplicaciones as A " +
                            "                            left join (select substr(min(ADT.Fecha),-4,4) as Fecha," +
                                    "   ADT.Id_Aplicacion,ADT.c_codigo_eps " +
                            "   from t_Aplicaciones_Det as ADT where ADT.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' " +
                            "group by ADT.Id_Aplicacion,ADT.c_codigo_eps ) as AD on A.Id_Aplicacion=AD.Id_Aplicacion and AD.c_codigo_eps=A.c_codigo_eps " +
                            "                            where rtrim(ltrim(A.Id_Huerta))='"+CopiHue.getItem(i).getTexto().substring(0, 5)+"' and AD.Fecha ='"+etd_Fecha.getText().toString().substring(6, 10)+"' and A.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' ";
                    Renglon=BD.rawQuery(Consulta,null);
                    //strftime('%Y',)
                    if(Renglon.moveToFirst()){

                        do {
                            MaxCod=("000"+String.valueOf(Renglon.getInt(0)+1)).substring(("000"+String.valueOf(Renglon.getInt(0)+1)).length()-3);
                        } while(Renglon.moveToNext());

                        BD.close();
                    }else{
                        BD.close();
                    }

                    text_Codigo.setText(MaxCod+"-"+objSDF.format(date1).substring(8, 10)+"-"+CopiHue.getItem(i).getTexto().substring(0, 5)+"-"+CopiHue.getItem(i).getTexto().substring(8));
                }
                Huerta=CopiHue.getItem(i).getTexto().substring(0, 5);

                Arreglodias=ArreglosdiasClean;
                Diaseleccionado=Diasseleccionadoclean;
                Listadias=ListadiasClean;
                Listadias.clear();

                cargaSpinnerBloque();

                if(ItemSPRec==null){
                    cargaSpinnerRecetas();
                    CopiRec = new AdaptadorSpinner(aplicacion.this, ItemSPRec);
                    sp_Receta.setAdapter(CopiRec);
                }else{
                    if(ItemSPRec.size()<=1 || LineEmpresa!=i){
                        cargaSpinnerRecetas();
                        CopiRec = new AdaptadorSpinner(aplicacion.this, ItemSPRec);
                        sp_Receta.setAdapter(CopiRec);
                    }
                    if(LineReceta>0){
                        sp_Receta.setSelection(LineReceta);
                    }
                }

                LineHuerta=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_TipoAplicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    text_Aplicados.setText("N° "+CopiApli.getItem(i).getTexto().substring(CopiApli.getItem(i).getTexto().indexOf("-")+2)+"s:");

                   // textView32.setText(CopiApli.getItem(i).getTexto().substring(CopiApli.getItem(i).getTexto().indexOf("-")+2)+"s");

                    if(ItemSPPre==null){
                        cargaSpinnerPresentacion();
                        CopiPre = new AdaptadorSpinner(getApplicationContext(), ItemSPPre);
                        sp_Presentacion.setAdapter(CopiPre);
                    }


                    if (sp_Presentacion.getCount() == 2 && sp_Presentacion.getSelectedItemPosition()==0) {
                        sp_Presentacion.setSelection(1);
                        yasemovio=true;
                    }
                }
                LineTipoA=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv_GridAplicacion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(aplicacion.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar el registro seleccionado ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();

                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(aplicacion.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                        //BD.beginTransaction();

                        int cantidad= BD.delete("t_Aplicaciones_Det","Id_Aplicacion='"+text_Codigo.getText().toString().substring(0,3)+text_Codigo.getText().toString().substring(4,6)+text_Codigo.getText().toString().substring(7,12)+"' and Fecha='"+arrayArticulos.get(i).getFecha()+"' and c_codigo_pro='"+arrayArticulos.get(i).getcProducto()+"' and c_codigo_eps='"+arrayArticulos.get(i).getcEps()+"' ",null);

                        if(cantidad>0){

                        }else{
                            Toast.makeText(aplicacion.this,arrayArticulos.get(i).getFecha(),Toast.LENGTH_SHORT).show();
                        }
                        //BD.endTransaction();
                        BD.close();
                        Cargagrid(Id,CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2));
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //cancelar();
                    }
                });
                dialogo1.show();
                return false;
            }
        });

        sp_Receta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (CopiRec.getCount() > 0 && sp_Receta.getSelectedItemPosition()>0 && Id==null) {

                    AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(aplicacion.this, "ShellPest", null, 1);
                    SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                    Cursor Renglon;

                    String Consulta;

                    Consulta = "select R.Id_TipoAplicacion,T.Nombre_TipoAplicacion,R.Id_Presentacion,P.Nombre_Presentacion,U.v_abrevia_uni " +
                            "from t_Receta as R " +
                            "inner join t_TipoAplicacion as T on T.Id_TipoAplicacion=R.Id_TipoAplicacion and T.c_codigo_eps=R.c_codigo_eps " +
                            "inner join t_Presentacion as P on P.Id_Presentacion=R.Id_Presentacion and P.c_codigo_eps=R.c_codigo_eps " +
                            "inner join t_Unidad as U on U.c_codigo_uni=P.Id_Unidad and U.c_codigo_eps=P.c_codigo_eps " +
                            "where R.c_codigo_eps='" + CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0, 2) + "' " +
                            " and R.Id_Receta ='" + CopiRec.getItem(sp_Receta.getSelectedItemPosition()).getTexto().substring(0, 7) + "' ";
                    Renglon = BD.rawQuery(Consulta, null);
                    //strftime('%Y',)
                    if (Renglon.moveToFirst()) {

                        do {
                            int item;

                            item = 0;
                            for (int x = 0; x < ItemSPApli.size(); x++) {
                                if (ItemSPApli.get(x).getTexto().equals(Renglon.getString(0).trim() + " - " + Renglon.getString(1).trim())) {
                                    item = x;
                                    break;
                                }
                            }
                            if (item > 0) {
                                sp_TipoAplicacion.setSelection(item);
                            }

                            cargaSpinnerPresentacion();
                            CopiPre = new AdaptadorSpinner(getApplicationContext(), ItemSPPre);
                            sp_Presentacion.setAdapter(CopiPre);
                            DescansoEnMilisegundos2(3);
                            item = 0;
                            for (int x = 0; x < ItemSPPre.size(); x++) {
                                String Presenta="";
                                Presenta=Renglon.getString(2).trim() + " - " + Renglon.getString(3).trim() + " " + Renglon.getString(4).trim();
                                Presenta=ItemSPPre.get(x).getTexto();
                                if (ItemSPPre.get(x).getTexto().equals(Renglon.getString(2).trim() + " - " + Renglon.getString(3).trim() + " " + Renglon.getString(4).trim())) {
                                    item = x;
                                    break;
                                }
                            }
                            if (item > 0) {
                                sp_Presentacion.setSelection(item);
                            }

                        } while (Renglon.moveToNext());

                        BD.close();

                        cargaGridxReceta(CopiRec.getItem(sp_Receta.getSelectedItemPosition()).getTexto().substring(0, 7),text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(i).getTexto().substring(0, 5),CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0, 2));
                    } else {
                        BD.close();
                    }
                }
                LineReceta=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etn_Pipadas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(Id!=null){
                    ActualizaPipadas(Id,cepsselapli);
                }
            }
        });

        text_CenCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(
                        aplicacion.this
                );
                builder.setTitle("Selecciona al menos un bloque");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(Arreglodias, Diaseleccionado, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            Listadias.add(i);
                            Collections.sort(Listadias);
                        }else{
                            if (Listadias.size()==1){
                                Listadias.clear();
                            }else{
                                Listadias.remove(i);
                            }
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder=new StringBuilder();
                        for(int j=0; j<Listadias.size();j++){
                            stringBuilder.append(Arreglodias[Listadias.get(j)].substring(0,4));
                            if(j!=Listadias.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        text_CenCos.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Limpiar todo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j=0;j<Diaseleccionado.length;j++){
                            Diaseleccionado[j]=false;
                            Listadias.clear();
                            text_CenCos.setText("");
                        }
                    }
                });
                builder.show();
            }
        });

        if(Id!=null){
            Cargagrid(Id,cepsselapli);
            CargarAplicacion();
        }
    }
    public void onClick(View view) {
        if(view==etd_Fecha){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> etd_Fecha.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
        }
    }

    private void cargaSpinnerBloque(){

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select B.Id_Bloque,B.Nombre_Bloque " +
                "from  t_Huerta as H  " +
                "inner join t_Bloque as B on B.Id_Huerta=H.Id_Huerta and B.c_codigo_eps=H.c_codigo_eps " +
                "where H.Id_Huerta='"+Huerta+"' " +
                "and H.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' and B.TipoBloque='B'",null);

        if(Renglon.moveToFirst()){
            int tamanio;
            tamanio=0;
            Arreglodias=new String[Renglon.getCount()];

            do {


                Arreglodias[tamanio] =Renglon.getString(0)+" - "+Renglon.getString(1);

                tamanio++;

            } while(Renglon.moveToNext());

            BD.close();

            Diaseleccionado=new boolean[Arreglodias.length];

        }else{
            BD.close();
        }
    }

    private void cargaGridxReceta(String Id,String IdAplica,String c_codigo_eps){
        lv_GridAplicacion.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();
            String TC;
            TC="select '"+etd_Fecha.getText().toString()+"' as Fecha , \n" +
                    "R.c_codigo_pro,\n" +
                    "R.Dosis, \n" +
                    " '1'  , \n" +
                    "R.c_codigo_uni, \n" +
                    "P.v_nombre_pro, \n" +
                    "U.v_abrevia_uni, \n" +
                    "R.c_codigo_eps, \n" +
                    "P.c_codigo_pro \n" +
                    "from t_RecetaDet as R \n" +
                    "left join t_Unidad as U on U.c_codigo_uni=R.c_codigo_uni and U.c_codigo_eps=R.c_codigo_eps \n"+
                    "left join t_Productos as P on R.c_codigo_pro=P.c_codigo_pro and P.c_codigo_eps=R.c_codigo_eps \n" +
                    "where R.Id_Receta='"+Id+"' and R.c_codigo_eps='"+c_codigo_eps+"'";

       // TC="select c_codigo_pro from t_Productos";
        boolean Sinproducto=false,SG=false;
        int SGC=0;
        Cursor Renglon =BD.rawQuery(TC,null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    String NProducto=Renglon.getString(1).trim();
                    String cProducto=Renglon.getString(8);
                    /*if (Renglon.getString(8)==null){
                        Sinproducto=true;
                        SGC++;
                        if(SG==false){
                            SG=true;
                        }
                    }else{
                        Sinproducto=false;
                    }
                    if (Sinproducto){
                        Tabla=new Itemaplicacion(Renglon.getString(0),"",Renglon.getString(2),Renglon.getString(4).trim(),Renglon.getString(1).trim() +"[Prod. Sin Existencia]",Renglon.getString(6),Renglon.getString(7));
                    }else{*/
                        Tabla=new Itemaplicacion(Renglon.getString(0),Renglon.getString(1).trim(),Renglon.getString(2),Renglon.getString(4).trim(),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),"");
                    //}

                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());
               /* if(SG){
                    Toast.makeText(this, "Algunos productos de la receta no estan en existencia en tu almacen, favor de notificar al administrador de recetas, los productos son los que estan en codigo", Toast.LENGTH_SHORT).show();
                }*/
                if(arrayArticulos.size()>0){
                    if(SGC!=arrayArticulos.size())
                    GuardaDeReceta();
                    DescansoEnMilisegundos(8000,IdAplica,c_codigo_eps);

                }
                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Aplicaciones guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridAplicacion(getApplicationContext(),arrayArticulos);
            lv_GridAplicacion.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void GuardaDeReceta(){
        boolean FaltoAlgo;
        FaltoAlgo=false;
        String Mensaje;
        Mensaje = "";

        if (sp_Empresa4.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una empresa,Verifica por favor";
        }
        if (sp_huerta.getSelectedItemPosition() > 0 ) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar un almacen,Verifica por favor";
        }
        if (sp_TipoAplicacion.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una tipo de aplicacion,Verifica por favor";
        }

        if (sp_Presentacion.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una Presentacion,Verifica por favor";
        }


        if (!FaltoAlgo){
            if (guardarEncabezado()){

                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                Date date1=objDate;

                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
                Cursor Renglon;

                for(int i=0;i<arrayArticulos.size();i++){

                    if(arrayArticulos.get(i).getcProducto().trim().length()>0) {
                        Renglon = BD.rawQuery("select count(Id_Aplicacion) " +
                                "from t_Aplicaciones_Det " +
                                "where Id_Aplicacion='" + text_Codigo.getText().toString().substring(0, 3) + objSDF.format(date1).substring(8, 10) + CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0, 5) + "' and Fecha='" + etd_Fecha.getText() + "' " +
                                "and c_codigo_pro='" + arrayArticulos.get(i).getcProducto().trim() + "' and c_codigo_eps='" + CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0, 2) + "' ", null);

                        if (Renglon.moveToFirst()) {
                            if (Renglon.getInt(0) > 0) {
                                Toast.makeText(this, "Ya se encuntra ese producto en la lista, favor de revisar.", Toast.LENGTH_SHORT).show();
                            } else {
                                ContentValues registro2 = new ContentValues();
                                registro2.put("Id_Aplicacion", text_Codigo.getText().toString().substring(0, 3) + objSDF.format(date1).substring(8, 10) + CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0, 5)); //objSDF.format(date1)
                                registro2.put("Fecha", etd_Fecha.getText().toString());
                                registro2.put("c_codigo_pro", arrayArticulos.get(i).getcProducto().trim());
                                registro2.put("Dosis", arrayArticulos.get(i).getCantidad());
                               // registro2.put("Unidades_aplicadas", arrayArticulos.get(i).getUnidades_aplicadas());
                                registro2.put("Id_Usuario", Usuario);
                                registro2.put("F_Creacion", objSDF.format(date1));
                                registro2.put("c_codigo_eps", CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0, 2));
                                BD.insert("t_Aplicaciones_Det", null, registro2);
                            }
                        } else {
                            Toast.makeText(this, "No Regreso nada la consulta de Detalle", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                BD.close();

                LimpiarDetalle();
            }
        }else{
            Toast.makeText(this,Mensaje,Toast.LENGTH_SHORT).show();
        }

    }

    private void DescansoEnMilisegundos(int milisegundos,String Id,String c_codigo_eps){
        CountDownTimer contadorbajotiempo=new CountDownTimer(milisegundos,1000) {
            @Override
            public void onTick(long l) {
                if((l/1000)>5)
                Toast.makeText(getApplicationContext(), "Algunos productos de la receta no estan en existencia en tu almacen, favor de notificar al administrador de recetas.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                //Toast.makeText(getApplicationContext(), "Se quitaron los productos que no tienen existencia.", Toast.LENGTH_SHORT).show();
                Cargagrid(Id,c_codigo_eps);
            }
        }.start();
    }

    private void DescansoEnMilisegundos2(int milisegundos){

        CountDownTimer contadorbajotiempo=new CountDownTimer(milisegundos,1000) {
            int C=0;
            @Override
            public void onTick(long l) {
               C++;
            }

            @Override
            public void onFinish() {
                C=0;
            }
        }.start();
    }

    private void borrarAplicaciones15Days()  {
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
        Cursor Renglon;

        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.add(Calendar.DATE, -15);
        Date nowMinus15 = c.getTime();

        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto

        Renglon=BD.rawQuery("select E.Id_Aplicacion,E.c_codigo_eps,E.F_Creacion from t_Aplicaciones as E ",null);

        if(Renglon.moveToFirst()){
            String Apli,Eps;
            String Fecha;
            Date FechaD;
            do {
                Apli=Renglon.getString(0);
                Eps=Renglon.getString(1);
                Fecha=Renglon.getString(2);


                try {
                    FechaD = objSDF.parse(Fecha);

                    if (FechaD.before(nowMinus15) ){
                        int cantidad= BD.delete("t_Aplicaciones_Det","Id_Aplicacion='"+Apli+"' and  c_codigo_eps='"+Eps+"' ",null);

                        if(cantidad>0){
                            int cantidad2= BD.delete("t_Aplicaciones","Id_Aplicacion='"+Apli+"' and  c_codigo_eps='"+Eps+"' ",null);

                            if(cantidad2>0){

                            }else{
                                Toast.makeText(aplicacion.this,"No se depuro la Lista de Aplicaciones antiguas",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(aplicacion.this,"No se depuro la Lista de Aplicaciones antiguas",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }
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

    private void ActualizaPipadas(String Id,String EPS){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        Cursor Renglon;

        ContentValues registro3= new ContentValues();

        registro3.put("Unidades_aplicadas",etn_Pipadas.getText().toString());
        //registro3.put("Unidades_aplicadas", etn_Pipadas.getText().toString());
        int cantidad=BD.update("t_Aplicaciones",registro3,"Id_Aplicacion='"+Id+
                "' and c_codigo_eps='"+EPS+"' ",null);

        if(cantidad>0){
            //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
        }else{
            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
        }
    }


    public void CargarAplicacion(){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon;
        Renglon=BD.rawQuery(" select A.Id_Aplicacion,A.Id_Huerta ,A.Observaciones ,A.Id_TipoAplicacion ,A.Id_Presentacion,H.Nombre_Huerta,Pre.Nombre_Presentacion,ta.Nombre_TipoAplicacion,A.c_codigo_eps,eps.v_abrevia_eps,U.v_abrevia_uni,A.Id_Receta,rec.Fecha_Receta,A.Unidades_aplicadas  " +
                "from t_Aplicaciones as A " +
                "inner join t_Huerta as H on H.Id_Huerta=A.Id_Huerta and A.c_codigo_eps=H.c_codigo_eps " +
                "inner join t_Presentacion as Pre on A.Id_Presentacion=Pre.Id_Presentacion and Pre.c_codigo_eps=H.c_codigo_eps " +
                "inner join t_Unidad as U on U.c_codigo_uni=Pre.Id_Unidad and U.c_codigo_eps=Pre.c_codigo_eps "+
                "inner join t_TipoAplicacion as ta on A.Id_TipoAplicacion=ta.Id_TipoAplicacion and Ta.c_codigo_eps=Pre.c_codigo_eps " +
                "left join conempresa as eps on eps.c_codigo_eps=A.c_codigo_eps  " +
                "left join t_Receta as rec on rec.c_codigo_eps=A.c_codigo_eps and rec.Id_Receta=A.Id_Receta " +
                "where A.Id_Aplicacion='"+Id+"' and A.c_codigo_eps='"+cepsselapli+"'",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){

                    text_Codigo.setText(Id.substring(0,3)+"-"+Id.substring(3,5)+"-"+Id.substring(5,10));

                    int item;

                    if(sp_Empresa4.getSelectedItemPosition()<2){
                        item=0;
                        for (int x=0; x<ItemSPEmp.size();x++){
                            if( ItemSPEmp.get(x).getTexto().equals(Renglon.getString(8).trim()+" - "+Renglon.getString(9).trim())){
                                item=x;
                                break;
                            }
                        }
                        if(item>0){
                            sp_Empresa4.setSelection(item);
                            LineEmpresa=item;
                        }
                    }

                    if(ItemSPHue==null){
                        cargaSpinnerHuertas();
                        CopiHue = new AdaptadorSpinner(aplicacion.this, ItemSPHue);
                        sp_huerta.setAdapter(CopiHue);
                    }else{
                        if (ItemSPHue.size()<=1){
                            cargaSpinnerHuertas();
                            CopiHue = new AdaptadorSpinner(aplicacion.this, ItemSPHue);
                            sp_huerta.setAdapter(CopiHue);
                        }
                    }
                    String THuerta;
                    THuerta=Renglon.getString(1)+" - "+Renglon.getString(5);
                    item=0;
                    if(ItemSPHue!=null){
                        for (int x=0; x<ItemSPHue.size();x++){
                            THuerta=ItemSPHue.get(x).getTexto();
                            if( ItemSPHue.get(x).getTexto().equals(Renglon.getString(1)+" - "+Renglon.getString(5))){
                                item=x;
                                break;
                            }
                        }
                        if(item>0) {
                            sp_huerta.setSelection(item);
                            LineHuerta=item;
                        }
                        String PArrafo;
                        PArrafo=Renglon.getString(2);
                        pt_Observaciones.setText(Renglon.getString(2));
                        String Presentacion=Renglon.getString(4).trim()+" - "+Renglon.getString(6).trim()+ " "+ Renglon.getString(10);
                        String TIpo=Renglon.getString(3).trim()+" - "+Renglon.getString(7);
                        if(ItemSPApli==null){
                            cargaSpinnerTipoAplicacion();
                            CopiApli = new AdaptadorSpinner(aplicacion.this, ItemSPApli);
                            sp_TipoAplicacion.setAdapter(CopiApli);
                        }else{
                            if (ItemSPApli.size()<=1){
                                cargaSpinnerTipoAplicacion();
                                CopiApli = new AdaptadorSpinner(aplicacion.this, ItemSPApli);
                                sp_TipoAplicacion.setAdapter(CopiApli);
                            }
                        }

                        item=0;
                        for (int x=0; x<ItemSPApli.size();x++){
                            TIpo= ItemSPApli.get(x).getTexto();
                            if( ItemSPApli.get(x).getTexto().equals(Renglon.getString(3).trim()+" - "+Renglon.getString(7).trim())){
                                item=x;
                                break;
                            }
                        }
                        if(item>0) {
                            sp_TipoAplicacion.setSelection(item);
                            LineTipoA=item;
                        }

                        if(ItemSPPre==null){
                            cargaSpinnerPresentacion();
                            CopiPre = new AdaptadorSpinner(aplicacion.this, ItemSPPre);
                            sp_Presentacion.setAdapter(CopiPre);
                        }else{
                            if (ItemSPPre.size()<=1){
                                cargaSpinnerPresentacion();
                                CopiPre = new AdaptadorSpinner(aplicacion.this, ItemSPPre);
                                sp_Presentacion.setAdapter(CopiPre);
                            }
                        }


                        item=0;
                        for (int x=0; x<ItemSPPre.size();x++){
                            if( ItemSPPre.get(x).getTexto().equals(Renglon.getString(4).trim()+" - "+Renglon.getString(6).trim()+ " "+ Renglon.getString(10))){
                                item=x;
                                break;
                            }
                        }
                        if(item>0){
                            sp_Presentacion.setSelection(item);
                            LinePresentacion=item;
                        }
                        String nPipadas;
                        nPipadas=Renglon.getString(13).trim();
                        etn_Pipadas.setText(Renglon.getString(13).trim());

                        item=0;

                        if(ItemSPRec==null){
                            cargaSpinnerRecetas();
                            CopiRec = new AdaptadorSpinner(aplicacion.this, ItemSPRec);
                            sp_Receta.setAdapter(CopiRec);
                        }else{
                            if (ItemSPRec.size()<=1){
                                cargaSpinnerRecetas();
                                CopiRec = new AdaptadorSpinner(aplicacion.this, ItemSPRec);
                                sp_Receta.setAdapter(CopiRec);
                            }
                        }
                        for (int x=0; x<ItemSPRec.size();x++){
                            TIpo= ItemSPRec.get(x).getTexto()+" = "+Renglon.getString(11) + " - " + Renglon.getString(12);
                            if( ItemSPRec.get(x).getTexto().equals(Renglon.getString(11) + " - " + Renglon.getString(12))){
                                item=x;
                                break;
                            }
                        }
                        if(item>0) {
                            sp_Receta.setSelection(item);
                            LineReceta=item;
                        }

                    }
                }else{

                }
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();
        }
        BD.close();
    }

    private void LimpiarDetalle(){
        actv_Productos.setText("");
        actv_Productos.showDropDown();
        etn_ApliCantidad.setText("0");

    }

    public void Agregar(View view){
        boolean FaltoAlgo;
        FaltoAlgo=false;
        String Mensaje;
        Mensaje = "";

        if (sp_Empresa4.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una empresa,Verifica por favor";
        }
        if (sp_huerta.getSelectedItemPosition() > 0 ) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar un almacen,Verifica por favor";
        }
        if (sp_TipoAplicacion.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una tipo de aplicacion,Verifica por favor";
        }

        if (sp_Presentacion.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una Presentacion,Verifica por favor";
        }

        if (actv_Productos.getText().toString().indexOf("|") >= 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta teclear un producto,Verifica por favor";
        }
        try {
            if (Double.parseDouble(etn_ApliCantidad.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de dosis,Verifica por favor";
            }
        } catch (IllegalStateException e) {
            etn_ApliCantidad.setText("0");
        }
        catch(NumberFormatException f)
        {
            etn_ApliCantidad.setText("0");
        }
        catch(Exception g)
        {
            etn_ApliCantidad.setText("0");
        }

        try {
            if (Double.parseDouble(etn_Pipadas.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de la aplicados,Verifica por favor";
            }
        } catch (IllegalStateException e) {
            etn_Pipadas.setText("0");
        }
        catch(NumberFormatException f)
        {
            etn_Pipadas.setText("0");
        }
        catch(Exception g)
        {
            etn_Pipadas.setText("0");
        }

        if (!FaltoAlgo){
            if (guardarEncabezado()){
                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                Date date1=objDate;

                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
                Cursor Renglon;

                Renglon =BD.rawQuery("select count(Id_Aplicacion) " +
                        "from t_Aplicaciones_Det " +
                        "where Id_Aplicacion='"+text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0,5)+"' and Fecha='"+etd_Fecha.getText()+"' " +
                        "and c_codigo_pro='"+actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim()+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' " ,null);

                if(Renglon.moveToFirst()){
                    if(seldet){
                        ContentValues registro3= new ContentValues();
                        registro3.put("Fecha",etd_Fecha.getText().toString());
                        registro3.put("c_codigo_pro",actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim());
                        registro3.put("Dosis",etn_ApliCantidad.getText().toString());
                        registro3.put("Centro_Costos",text_CenCos.getText().toString().trim());
                        //registro3.put("Unidades_aplicadas", etn_Pipadas.getText().toString());
                        int cantidad=BD.update("t_Aplicaciones_Det",registro3,"Id_Aplicacion='"+text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0,5)+
                                "' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"'"+
                                " and c_codigo_pro='"+arrayArticulos.get(nseldet).getcProducto()+"' and Fecha='"+arrayArticulos.get(nseldet).getFecha()+"' ",null);

                        if(cantidad>0){
                            //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                        }else{
                            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(Renglon.getInt(0)>0){
                            if(seldet){

                            }else{
                                Toast.makeText(this,"Ya se encuntra ese producto en la lista, favor de revisar.",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            ContentValues registro2= new ContentValues();
                            registro2.put("Id_Aplicacion",text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0,5)); //objSDF.format(date1)
                            registro2.put("Fecha",etd_Fecha.getText().toString());
                            registro2.put("c_codigo_pro",actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim());
                            registro2.put("Dosis",etn_ApliCantidad.getText().toString());
                            registro2.put("Unidades_aplicadas", etn_Pipadas.getText().toString());
                            registro2.put("Id_Usuario",Usuario);
                            registro2.put("F_Creacion",objSDF.format(date1));
                            registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2));
                            registro2.put("Centro_Costos",text_CenCos.getText().toString().trim());
                            BD.insert("t_Aplicaciones_Det",null,registro2);
                        }
                    }
                    seldet=false;
                    nseldet=-1;
                }else{
                    Toast.makeText(this,"No Regreso nada la consulta de Detalle",Toast.LENGTH_SHORT).show();
                }
                BD.close();

                LimpiarDetalle();
                Cargagrid(text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0,5),CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2));
            }


        }else{
            Toast.makeText(this,Mensaje,Toast.LENGTH_SHORT).show();
        }
    }
    private boolean guardarEncabezado(){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());

        Cursor Renglon;
        Renglon=BD.rawQuery("select count(Apli.Id_Aplicacion) as Sihay from t_Aplicaciones as Apli where Apli.Id_Aplicacion='"+text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0,5)+"' and Apli.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' and Enviado='0' ",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){
                    ContentValues registro = new ContentValues();
                    String Parrafo=String.valueOf(pt_Observaciones.getText());
                    registro.put("Observaciones", String.valueOf(pt_Observaciones.getText()));
                    registro.put("Id_TipoAplicacion",CopiApli.getItem(sp_TipoAplicacion.getSelectedItemPosition()).getTexto().substring(0,3));
                    registro.put("Id_Presentacion",CopiPre.getItem(sp_Presentacion.getSelectedItemPosition()).getTexto().substring(0,4));
                    registro.put("Id_Receta",CopiRec.getItem(sp_Receta.getSelectedItemPosition()).getTexto().substring(0,6));
                    registro.put("Unidades_aplicadas",etn_Pipadas.getText().toString());
                    int cantidad=BD.update("t_Aplicaciones",registro,"Id_Aplicacion='"+text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0,5)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                    if(cantidad>0){
                        //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                    }else{
                        //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }else{
                    ContentValues registro= new ContentValues();

                    String Parrafo=String.valueOf(pt_Observaciones.getText());
                    registro.put("Id_Aplicacion",text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(sp_huerta.getSelectedItemPosition()).getTexto().substring(0,5));
                    registro.put("Id_Huerta",Huerta);
                    registro.put("Observaciones",pt_Observaciones.getText().toString());
                    registro.put("Id_TipoAplicacion",CopiApli.getItem(sp_TipoAplicacion.getSelectedItemPosition()).getTexto().substring(0,3));
                    registro.put("Id_Presentacion",CopiPre.getItem(sp_Presentacion.getSelectedItemPosition()).getTexto().substring(0,4));
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    registro.put("Enviado","0");
                    registro.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2));
                    registro.put("Id_Receta",CopiRec.getItem(sp_Receta.getSelectedItemPosition()).getTexto().substring(0,7));
                    registro.put("Unidades_aplicadas",etn_Pipadas.getText().toString());
                    BD.insert("t_Aplicaciones",null,registro);
                    return true;
                }
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void Cargagrid(String Id,String c_codigo_eps){
        lv_GridAplicacion.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select AD.Fecha, \n" +
                "\tAD.c_codigo_pro,\n" +
                "\tAD.Dosis,\n" +
                "\t '1' as uno, \n" +
                "\tP.c_codigo_uni, \n" +
                "\tP.v_nombre_pro, \n" +
                "\tU.v_abrevia_uni, AD.c_codigo_eps, AD.Centro_Costos \n" +
                "from t_Aplicaciones_Det as AD \n" +
                "left join t_Productos as P on rtrim(ltrim(AD.c_codigo_pro))=rtrim(ltrim(P.c_codigo_pro)) and P.c_codigo_eps=AD.c_codigo_eps \n" +
                "left join t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni and U.c_codigo_eps=P.c_codigo_eps \n" +
                "where AD.Id_Aplicacion='"+Id+"' and AD.c_codigo_eps='"+c_codigo_eps+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new Itemaplicacion(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());

                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Aplicaciones guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridAplicacion(getApplicationContext(),arrayArticulos);
            lv_GridAplicacion.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarProductos(){
        ArrayProductos=ArrayProductosLimpia;
        ArrayProductos=new ArrayList<>();
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select T.v_nombre_pro,T.c_codigo_pro from t_Productos as T where T.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);

        if(Renglon.moveToFirst()){
            do {
                ArrayProductos.add(new String(Renglon.getString(0)+" | "+Renglon.getString(1)));
            } while(Renglon.moveToNext());
            BD.close();
        }else{
            BD.close();
        }

    }

    public void ListarAplicaciones(View view){
        Intent intento = new Intent(this, Aplicaciones_Capturadas.class);
        intento.putExtra("usuario", Usuario);
        intento.putExtra("perfil", Perfil);
        intento.putExtra("huerta", Huerta);

        //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
        startActivity(intento);
        finish();
    }

    private void cargaSpinnerTipoAplicacion(){
            CopiApli=null;

            ItemSPApli=new ArrayList<>();
            ItemSPApli.add(new ItemDatoSpinner("Tipo de Aplicacion"));

            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            Renglon=BD.rawQuery("select T.Id_TipoAplicacion,T.Nombre_TipoAplicacion from t_TipoAplicacion as T where T.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

            if(Renglon.moveToFirst()){

                do {
                    ItemSPApli.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
                } while(Renglon.moveToNext());

                BD.close();
            }else{
                BD.close();
            }
    }

    private void cargaSpinnerPresentacion(){
        //if(sp_Presentacion.getSelectedItemPosition()>0 ) {
            CopiPre = null;

            ItemSPPre = new ArrayList<>();
            ItemSPPre.add(new ItemDatoSpinner("Presentacion"));

            AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            Renglon = BD.rawQuery("select P.Id_Presentacion,P.Nombre_Presentacion,U.v_abrevia_uni from t_Presentacion as P left join t_Unidad as U on U.c_codigo_uni=P.Id_Unidad  and U.c_codigo_eps=P.c_codigo_eps where P.Id_TipoAplicacion='"+CopiApli.getItem(sp_TipoAplicacion.getSelectedItemPosition()).getTexto().substring(0,3)+"' and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"'", null);

            if (Renglon.moveToFirst()) {
                do {
                    ItemSPPre.add(new ItemDatoSpinner(Renglon.getString(0).trim() + " - " + Renglon.getString(1).trim()+ " "+ Renglon.getString(2).trim()));
                } while (Renglon.moveToNext());

                BD.close();
            } else {
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
                Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
            }else{
                Renglon=BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
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

    private void cargaSpinnerRecetas(){

        CopiRec = null;

        ItemSPRec = new ArrayList<>();
        ItemSPRec.add(new ItemDatoSpinner("Receta"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select R.Id_Receta,R.Fecha_Receta from t_Receta as R where R.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa4.getSelectedItemPosition()).getTexto().substring(0,2)+"' and R.Id_Huerta='"+Huerta+"' ",null);

        if (Renglon.moveToFirst()) {
            do {
                ItemSPRec.add(new ItemDatoSpinner(Renglon.getString(0) + " - " + Renglon.getString(1)));
            } while (Renglon.moveToNext());

            BD.close();
        } else {
            BD.close();
        }
    }

}