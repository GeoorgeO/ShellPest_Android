package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Fertilizacion extends AppCompatActivity {

    Spinner sp_TipoAplicacion, sp_Presentacion,sp_huerta5,sp_Empresa5,sp_Receta;
    AutoCompleteTextView actv_Productos;
    TextView text_Codigo ,text_UnidadPro,text_CenCos;
    EditText etd_Fecha,pt_Observaciones,etn_ApliCantidad,etn_HaApli;
    ListView lv_GridFertiliza;
    Button btn_Agrega;

    private int dia,mes, anio;

    public String Usuario,Huerta, Perfil ,Id,UnidadPro,cepsselapli;

    private ArrayList<ItemDatoSpinner> ItemSPApli, ItemSPPre, ItemSPHue,ItemSPEmp,ItemSPRec;
    private AdaptadorSpinner CopiApli, CopiPre,CopiHue,CopiEmp,CopiRec;
    private ArrayAdapter Adaptador_Arreglos;
    private ArrayList<String> ArrayProductos,ArrayProductosLimpia;

    int LineEmpresa,LineHuerta,LineReceta,LineTipoA,LinePresentacion;

    boolean[] Diaseleccionado,Diasseleccionadoclean;
    ArrayList<Integer> Listadias,ListadiasClean = new ArrayList<>();
    String[] Arreglodias,ArreglosdiasClean;

    float vHatemp=0;

    Itemaplicacion Tabla;
    Adaptador_GridAplicacion Adapter;
    ArrayList<Itemaplicacion> arrayArticulos;

    boolean seldet;
    int nseldet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizacion);

        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");
        Id = getIntent().getStringExtra("ID");
        cepsselapli=getIntent().getStringExtra("CEPS");

        sp_TipoAplicacion = (Spinner) findViewById(R.id.sp_TipoAplicacion);
        sp_Presentacion = (Spinner) findViewById(R.id.sp_Presentacion);
        sp_huerta5 = (Spinner) findViewById(R.id.sp_huerta5);
        sp_Empresa5 = (Spinner) findViewById(R.id.sp_Empresa5);
        sp_Receta = (Spinner) findViewById(R.id.sp_Receta);
        text_Codigo = (TextView) findViewById(R.id.text_Codigo);
        lv_GridFertiliza = (ListView) findViewById(R.id.lv_GridFertiliza);
        pt_Observaciones = (EditText) findViewById(R.id.pt_Observaciones);
        etn_ApliCantidad = (EditText) findViewById(R.id.etn_ApliCantidad);
        etn_HaApli = (EditText) findViewById(R.id.etn_HaApli);
        etd_Fecha = (EditText) findViewById(R.id.etd_Fecha);
        actv_Productos = (AutoCompleteTextView) findViewById(R.id.actv_Productos);
        text_UnidadPro= (TextView) findViewById(R.id.text_UnidadPro);
        text_CenCos= (TextView) findViewById(R.id.text_CenCos);
        btn_Agrega=(Button) findViewById(R.id.btn_Agrega);

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        etd_Fecha.setText(objSDF.format(date1));

        LineEmpresa=0;
        LineHuerta=0;
        LineReceta=0;

        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa5.setAdapter(CopiEmp);

        arrayArticulos = new ArrayList<>();

        nseldet=-1;

        if (sp_Empresa5.getCount()==2){
            sp_Empresa5.setSelection(1);
        }

        sp_Empresa5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(ItemSPHue==null){
                    cargaSpinnerHuertas();
                    CopiHue = new AdaptadorSpinner(Fertilizacion.this, ItemSPHue);
                    sp_huerta5.setAdapter(CopiHue);

                    if (sp_huerta5.getCount()==2){
                        sp_huerta5.setSelection(1);
                    }
                }else {
                    if (ItemSPHue.get(0).getTexto().trim().equals("Huerta") /*CopiHue.getItem(i).getTexto().trim().equals("Huerta")*/){
                        if ((ItemSPHue.size() <= 1) || LineEmpresa != i) {
                            cargaSpinnerHuertas();
                            CopiHue = new AdaptadorSpinner(Fertilizacion.this, ItemSPHue);
                            sp_huerta5.setAdapter(CopiHue);


                            if (LineHuerta > 0) {
                                //sp_huerta.setSelection(LineHuerta);
                            } else {
                                if (sp_huerta5.getCount() == 2) {
                                    sp_huerta5.setSelection(1);
                                }
                            }

                        }
                        if(LineEmpresa == i && LineHuerta>0){
                            sp_huerta5.setSelection(LineHuerta);
                        }
                    }
                }

                if(Id==null){
                    if(ItemSPApli==null){
                        cargaSpinnerTipoAplicacion();
                        CopiApli = new AdaptadorSpinner(Fertilizacion.this, ItemSPApli);
                        sp_TipoAplicacion.setAdapter(CopiApli);

                        if (sp_TipoAplicacion.getCount()==2){
                            sp_TipoAplicacion.setSelection(1);
                        }
                    }else{
                        if(ItemSPApli.size()<=1 || LineEmpresa!=i){
                            cargaSpinnerTipoAplicacion();
                            CopiApli = new AdaptadorSpinner(Fertilizacion.this, ItemSPApli);
                            sp_TipoAplicacion.setAdapter(CopiApli);

                            if (sp_TipoAplicacion.getCount()==2){
                                sp_TipoAplicacion.setSelection(1);
                            }
                        }
                    }
                }

                //if(Id==null) {
                cargarProductos();
                Adaptador_Arreglos = new ArrayAdapter(Fertilizacion.this, android.R.layout.simple_list_item_1, ArrayProductos);
                actv_Productos.setAdapter(Adaptador_Arreglos);
                //}
                LineEmpresa=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_huerta5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0 && Id==null){
                    AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(Fertilizacion.this,"ShellPest",null,1);
                    SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                    Cursor Renglon;

                    String MaxCod,Consulta;
                    MaxCod="";
                    Consulta="select count(A.Id_Aplicacion) num" +
                            "                            from t_Aplicaciones as A " +
                            "                            left join (select substr(min(ADT.Fecha),-4,4) as Fecha," +
                            "   ADT.Id_Aplicacion,ADT.c_codigo_eps " +
                            "   from t_Aplicaciones_Det as ADT where ADT.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' " +
                            "group by ADT.Id_Aplicacion,ADT.c_codigo_eps ) as AD on A.Id_Aplicacion=AD.Id_Aplicacion and AD.c_codigo_eps=A.c_codigo_eps " +
                            "                            where rtrim(ltrim(A.Id_Huerta))='"+CopiHue.getItem(i).getTexto().substring(0, 5)+"' and AD.Fecha ='"+etd_Fecha.getText().toString().substring(6, 10)+"' and A.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' ";
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
                    CopiRec = new AdaptadorSpinner(Fertilizacion.this, ItemSPRec);
                    sp_Receta.setAdapter(CopiRec);
                }else{
                    if(ItemSPRec.size()<=1 || LineEmpresa!=i){
                        cargaSpinnerRecetas();
                        CopiRec = new AdaptadorSpinner(Fertilizacion.this, ItemSPRec);
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

        text_CenCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(
                        Fertilizacion.this
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

                        StringBuilder stringBuilderLot=new StringBuilder();
                        for(int j=0; j<Listadias.size();j++){
                            stringBuilderLot.append(Arreglodias[Listadias.get(j)].substring(0,4));
                            if(j!=Listadias.size()-1){
                                stringBuilderLot.append("', '");
                            }
                        }
                        if(etn_HaApli.getText().toString().trim().length()==0){
                            vHatemp=0;
                        }else{
                            vHatemp=Float.valueOf(etn_HaApli.getText().toString());
                        }

                        etn_HaApli.setText(String.valueOf(TomarHectareas(stringBuilderLot.toString())));

                        if(text_CenCos.getText().toString().trim().length()>0 ){
                            if(Id!=null){
                                ActualizaCentroCostos(Id,cepsselapli);
                            }
                        }
                        //Aqui llamar metodo para actualizar lo aplicado en el grid.
                        actualizaCantidad();
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

        sp_Receta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (CopiRec.getCount() > 0 && sp_Receta.getSelectedItemPosition()>0 && Id==null) {

                    AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(Fertilizacion.this, "ShellPest", null, 1);
                    SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                    Cursor Renglon;

                    String Consulta;

                    Consulta = "select R.Id_TipoAplicacion,T.Nombre_TipoAplicacion,R.Id_Presentacion,P.Nombre_Presentacion,U.v_abrevia_uni " +
                            "from t_Receta as R "+
                            "inner join t_TipoAplicacion as T on T.Id_TipoAplicacion=R.Id_TipoAplicacion and T.c_codigo_eps=R.c_codigo_eps " +
                            "inner join t_Presentacion as P on P.Id_Presentacion=R.Id_Presentacion and P.c_codigo_eps=R.c_codigo_eps " +
                            "inner join t_Unidad as U on U.c_codigo_uni=P.Id_Unidad and U.c_codigo_eps=P.c_codigo_eps " +
                            "where R.c_codigo_eps='" + CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0, 2) + "' " +
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
                        if(etn_HaApli.getText().toString().trim().length()==0){
                            etn_HaApli.setText("0");
                        }
                        cargaGridxReceta(CopiRec.getItem(sp_Receta.getSelectedItemPosition()).getTexto().substring(0, 7),text_Codigo.getText().toString().substring(0,3)+objSDF.format(date1).substring(8, 10)+CopiHue.getItem(i).getTexto().substring(0, 5),CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0, 2));
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

        sp_TipoAplicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){

                    // textView32.setText(CopiApli.getItem(i).getTexto().substring(CopiApli.getItem(i).getTexto().indexOf("-")+2)+"s");


                    cargaSpinnerPresentacion();
                    CopiPre = new AdaptadorSpinner(getApplicationContext(), ItemSPPre);
                    sp_Presentacion.setAdapter(CopiPre);



                    if (sp_Presentacion.getCount() == 2 && sp_Presentacion.getSelectedItemPosition()==0) {
                        sp_Presentacion.setSelection(1);

                    }
                }
                LineTipoA=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        actv_Productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(Fertilizacion.this,"ShellPest",null,1);
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
                        "where alm.Id_Huerta='"+Huerta+"' and exi.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' "+
                        "group by exi.c_codigo_eps,exi.c_codigo_pro,alm.Id_Huerta ) as Texi on ltrim(rtrim(Texi.c_codigo_pro))=ltrim(rtrim(P.c_codigo_pro)) and Texi.c_codigo_eps=p.c_codigo_eps "+
                        "where ltrim(rtrim(P.c_codigo_pro))='"+actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim()+"' and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                if(Renglon.moveToFirst()){

                    do {
                        text_UnidadPro.setText("Uni:"+Renglon.getString(1));
                        //text_UnidadPro.setText("Unidad: "+Renglon.getString(1)+" Existencia: "+Renglon.getDouble(2));
                        UnidadPro=Renglon.getString(1)+"s";

                    } while(Renglon.moveToNext());

                    BD.close();
                }else{
                    BD.close();
                }
            }

        });

        actv_Productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actv_Productos.setText("");
            }
        });

        lv_GridFertiliza.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(nseldet==i){
                    seldet=false;
                    nseldet=-1;

                    actv_Productos.setText("");
                    etn_ApliCantidad.setText("0");
                    //etn_Pipadas.setText("0");
                    //etd_Fecha.setText(arrayArticulos.get(i).getFecha());
                    text_UnidadPro.setText("Uni:");
                    UnidadPro="";



                }else{
                    seldet=true;
                    nseldet=i;
                    String tarrsay=arrayArticulos.get(i).getcProducto();

                    if(!arrayArticulos.get(i).getcProducto().equals("")){
                        if (arrayArticulos.get(i).getNombre_Producto()!=null){
                            actv_Productos.setText(arrayArticulos.get(i).getNombre_Producto().trim() +" | "+arrayArticulos.get(i).getcProducto().trim());
                        }else{
                            actv_Productos.setText(" | "+arrayArticulos.get(i).getcProducto().trim());
                        }

                    }

                    etn_ApliCantidad.setText(arrayArticulos.get(i).getCantidad());

                    etd_Fecha.setText(arrayArticulos.get(i).getFecha());
                    text_UnidadPro.setText("Uni: "+arrayArticulos.get(i).getNombre_Unidad());
                    UnidadPro=arrayArticulos.get(i).getNombre_Unidad()+"s";

                    if (arrayArticulos.get(i).getCC()==null){
                        // text_CenCos.setText("");
                    }else{
                        //text_CenCos.setText(arrayArticulos.get(i).getCC().trim());
                    }
                }
            }
        });

        if(Id!=null){
            Cargagrid(Id,cepsselapli);
            CargarFertilizacion();
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

    private void cargaSpinnerHuertas(){

        CopiHue = null;

        ItemSPHue = new ArrayList<>();
        ItemSPHue.add(new ItemDatoSpinner("Huerta"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }else{
            Renglon=BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
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

    private void cargaSpinnerTipoAplicacion(){
        CopiApli=null;

        ItemSPApli=new ArrayList<>();
        ItemSPApli.add(new ItemDatoSpinner("Tipo de Aplicacion"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select T.Id_TipoAplicacion,T.Nombre_TipoAplicacion from t_TipoAplicacion as T where T.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPApli.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }
    }

    private void cargarProductos(){
        ArrayProductos=ArrayProductosLimpia;
        ArrayProductos=new ArrayList<>();
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select T.v_nombre_pro,T.c_codigo_pro from t_Productos as T where T.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);

        if(Renglon.moveToFirst()){
            do {
                ArrayProductos.add(new String(Renglon.getString(0)+" | "+Renglon.getString(1)));
            } while(Renglon.moveToNext());
            BD.close();
        }else{
            BD.close();
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
                "and H.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' and B.TipoBloque='B'",null);

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

    private void cargaSpinnerRecetas(){

        CopiRec = null;

        ItemSPRec = new ArrayList<>();
        ItemSPRec.add(new ItemDatoSpinner("Receta"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select R.Id_Receta,R.Fecha_Receta from  t_Receta_Huerta  as RH  inner join t_Receta as R on RH.Id_Receta=R.Id_Receta and RH.c_codigo_eps=R.c_codigo_eps where R.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' and RH.Id_Huerta='"+Huerta+"' and R.Para='A' ",null);

        if (Renglon.moveToFirst()) {
            do {
                ItemSPRec.add(new ItemDatoSpinner(Renglon.getString(0) + " - " + Renglon.getString(1)));
            } while (Renglon.moveToNext());

            BD.close();
        } else {
            BD.close();
        }
    }

    private void ActualizaCentroCostos(String Id,String EPS){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
        Cursor Renglon;

        ContentValues registro3= new ContentValues();

       /* registro3.put("Centro_Costos",text_CenCos.getText().toString().trim());
        //registro3.put("Unidades_aplicadas", etn_Pipadas.getText().toString());
        int cantidad=BD.update("t_Aplicaciones",registro3,"Id_Aplicacion='"+Id+
                "' and c_codigo_eps='"+EPS+"' ",null);  LO QUITE YA QUE ACTUALIZA PERO DE APLICACION

        if(cantidad>0){
            Toast.makeText(Fertilizacion.this,"Se actualizo el o los Bloques.",Toast.LENGTH_SHORT).show();
        }else{
            //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
        }*/
    }

    private void cargaSpinnerPresentacion(){
        //if(sp_Presentacion.getSelectedItemPosition()>0 ) {
        CopiPre = null;

        ItemSPPre = new ArrayList<>();
        ItemSPPre.add(new ItemDatoSpinner("Presentacion"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon = BD.rawQuery("select P.Id_Presentacion,P.Nombre_Presentacion,U.v_abrevia_uni from t_Presentacion as P left join t_Unidad as U on U.c_codigo_uni=P.Id_Unidad  and U.c_codigo_eps=P.c_codigo_eps where P.Id_TipoAplicacion='"+CopiApli.getItem(sp_TipoAplicacion.getSelectedItemPosition()).getTexto().substring(0,3)+"' and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'", null);

        if (Renglon.moveToFirst()) {
            do {
                ItemSPPre.add(new ItemDatoSpinner(Renglon.getString(0).trim() + " - " + Renglon.getString(1).trim()+ " "+ Renglon.getString(2).trim()));
            } while (Renglon.moveToNext());

            BD.close();
        } else {
            BD.close();
        }
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


    private float TomarHectareas(String Bloques){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;
        float SumHa=0;
        String SQLQ="select B.Ha_Produccion from t_Bloque as B  where Id_Bloque in ('"+Bloques+"')";
        Renglon = BD.rawQuery(SQLQ, null);

        if (Renglon.moveToFirst()) {
            do {
                SumHa=SumHa+Renglon.getFloat(0);
            } while (Renglon.moveToNext());

            BD.close();
        } else {
            BD.close();
        }
        return SumHa;
    }

    private void cargaGridxReceta(String Id,String IdAplica,String c_codigo_eps){
        lv_GridFertiliza.setAdapter(null);
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

            if (Renglon.moveToFirst()) {

                do {
                    String NProducto=Renglon.getString(1).trim();
                    String cProducto=Renglon.getString(8);

                    if(etn_HaApli.getText().toString().trim().equals("0")){
                        Tabla=new Itemaplicacion(Renglon.getString(0),Renglon.getString(1).trim(),Renglon.getString(2),Renglon.getString(4).trim(),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),"");
                    }else{
                        Tabla=new Itemaplicacion(Renglon.getString(0),Renglon.getString(1).trim(),String.valueOf(Float.valueOf(etn_HaApli.getText().toString()) *Float.valueOf(Renglon.getString(2))),Renglon.getString(4).trim(),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),"");
                    }
                    //}

                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());

                if(arrayArticulos.size()>0){
                    if(SGC!=arrayArticulos.size()){
                        GuardaDeReceta();
                    }
                }
                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Aplicaciones guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridAplicacion(getApplicationContext(),arrayArticulos);
            lv_GridFertiliza.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizaCantidad(){
        for (int i=0; i<arrayArticulos.size();i++){
            if(etn_HaApli.getText().toString().trim().equals("0.0")){
                arrayArticulos.get(i).setCantidad(String.valueOf(Float.valueOf(arrayArticulos.get(i).getCantidad())/vHatemp));
            }else{
                arrayArticulos.get(i).setCantidad(String.valueOf(Float.valueOf(etn_HaApli.getText().toString()) *Float.valueOf(arrayArticulos.get(i).getCantidad())));
            }

        }
        lv_GridFertiliza.setAdapter(null);
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridAplicacion(getApplicationContext(),arrayArticulos);
            lv_GridFertiliza.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Agregar(View view){
        boolean FaltoAlgo;
        FaltoAlgo=false;
        String Mensaje;
        Mensaje = "";

        if (sp_Empresa5.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una empresa,Verifica por favor";
        }
        if (sp_huerta5.getSelectedItemPosition() > 0 ) {

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
            if (Double.parseDouble(etn_HaApli.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la cantidad de la aplicados,Verifica por favor";
            }
        } catch (IllegalStateException e) {
            etn_HaApli.setText("0");
        }
        catch(NumberFormatException f)
        {
            etn_HaApli.setText("0");
        }
        catch(Exception g)
        {
            etn_HaApli.setText("0");
        }

        if (!FaltoAlgo){
            if (guardarEncabezado()){
                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                Date date1=objDate;

                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
                Cursor Renglon;

                Renglon =BD.rawQuery("select count(Id_Fertiliza) " +
                        "from t_Fertiliza_Det " +
                        "where Id_Fertiliza='"+text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5)+"' and Fecha='"+etd_Fecha.getText()+"' " +
                        "and c_codigo_pro='"+actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim()+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' " ,null);

                if(Renglon.moveToFirst()){
                    if(seldet){
                        ContentValues registro3= new ContentValues();
                        registro3.put("Fecha",etd_Fecha.getText().toString());
                        registro3.put("c_codigo_pro",actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim());
                        registro3.put("Cantidad_Aplicada",etn_ApliCantidad.getText().toString());
                        registro3.put("Centro_Costos",text_CenCos.getText().toString().trim());
                        //registro3.put("Unidades_aplicadas", etn_Pipadas.getText().toString());
                        int cantidad=BD.update("t_Fertiliza_Det",registro3,"Id_Fertiliza='"+text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5)+
                                "' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'"+
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
                            registro2.put("Id_Fertiliza",text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5)); //objSDF.format(date1)
                            registro2.put("Fecha",etd_Fecha.getText().toString());
                            registro2.put("c_codigo_pro",actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim());
                            registro2.put("Cantidad_Aplicada",etn_ApliCantidad.getText().toString());
                            registro2.put("Id_Usuario",Usuario);
                            registro2.put("F_Creacion",objSDF.format(date1));
                            registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
                            registro2.put("Centro_Costos",text_CenCos.getText().toString().trim());
                            BD.insert("t_Fertiliza_Det",null,registro2);
                        }
                    }
                    seldet=false;
                    nseldet=-1;
                }else{
                    Toast.makeText(this,"No Regreso nada la consulta de Detalle",Toast.LENGTH_SHORT).show();
                }
                BD.close();

                LimpiarDetalle();
                Cargagrid(text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5),CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
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
        Renglon=BD.rawQuery("select count(Fer.Id_Fertiliza) as Sihay from t_Fertiliza as Fer where Fer.Id_Fertiliza='"+text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5)+"' and Fer.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"' and Enviado='0' ",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){
                    ContentValues registro = new ContentValues();
                    String Parrafo=String.valueOf(pt_Observaciones.getText());
                    registro.put("Observaciones", String.valueOf(pt_Observaciones.getText()));
                    registro.put("Id_TipoAplicacion",CopiApli.getItem(sp_TipoAplicacion.getSelectedItemPosition()).getTexto().substring(0,3));
                    registro.put("Id_Presentacion",CopiPre.getItem(sp_Presentacion.getSelectedItemPosition()).getTexto().substring(0,4));
                    registro.put("Id_Receta",CopiRec.getItem(sp_Receta.getSelectedItemPosition()).getTexto().substring(0,6));
                    registro.put("Ha_aplicadas",etn_HaApli.getText().toString());
                    int cantidad=BD.update("t_Fertiliza",registro,"Id_Fertiliza='"+text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                    if(cantidad>0){
                        //Cambie en el Id, ya no tome la fecha del sistema, si no la fecha que seleccionen en la ventana
                        Id=text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5);
                        cepsselapli=CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2);
                        //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                    }else{
                        //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }else{
                    ContentValues registro= new ContentValues();

                    String Parrafo=String.valueOf(pt_Observaciones.getText());
                    //Cambie en el Id, ya no tome la fecha del sistema (objSDF.format(date1)), si no la fecha que seleccionen en la ventana
                    registro.put("Id_Fertiliza",text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5));
                    registro.put("Id_Huerta",Huerta);
                    registro.put("Observaciones",pt_Observaciones.getText().toString());
                    registro.put("Id_TipoAplicacion",CopiApli.getItem(sp_TipoAplicacion.getSelectedItemPosition()).getTexto().substring(0,3));
                    registro.put("Id_Presentacion",CopiPre.getItem(sp_Presentacion.getSelectedItemPosition()).getTexto().substring(0,4));
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    registro.put("Enviado","0");
                    registro.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2));
                    if(ItemSPRec.size()==1){
                        registro.put("Id_Receta","");
                    }else{
                        registro.put("Id_Receta",CopiRec.getItem(sp_Receta.getSelectedItemPosition()).getTexto().substring(0,7));
                    }

                    registro.put("Ha_aplicadas",etn_HaApli.getText().toString());
                    BD.insert("t_Fertiliza",null,registro);
                    //Cambie en el Id, ya no tome la fecha del sistema (objSDF.format(date1)), si no la fecha que seleccionen en la ventana
                    Id=text_Codigo.getText().toString().substring(0,3)+etd_Fecha.getText().toString().trim().substring(8, 10)+CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0,5);
                    cepsselapli=CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0,2);
                    return true;
                }
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void LimpiarDetalle(){
        actv_Productos.setText("");
        actv_Productos.showDropDown();
        etn_ApliCantidad.setText("0");
    }

    private void Cargagrid(String Id,String c_codigo_eps){
        lv_GridFertiliza.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select AD.Fecha, \n" +
                "\tAD.c_codigo_pro,\n" +
                "\tAD.Cantidad_Aplicada,\n" +
                "\t '1' as uno, \n" +
                "\tP.c_codigo_uni, \n" +
                "\tP.v_nombre_pro, \n" +
                "\tU.v_abrevia_uni, AD.c_codigo_eps, AD.Centro_Costos \n" +
                "from t_Fertiliza_Det as AD \n" +
                "left join t_Productos as P on rtrim(ltrim(AD.c_codigo_pro))=rtrim(ltrim(P.c_codigo_pro)) and P.c_codigo_eps=AD.c_codigo_eps \n" +
                "left join t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni and U.c_codigo_eps=P.c_codigo_eps \n" +
                "where AD.Id_Fertiliza='"+Id+"' and AD.c_codigo_eps='"+c_codigo_eps+"'",null);

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
            lv_GridFertiliza.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void GuardaDeReceta(){
        boolean FaltoAlgo;
        FaltoAlgo=false;
        String Mensaje;
        Mensaje = "";

        if (sp_Empresa5.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una empresa,Verifica por favor";
        }
        if (sp_huerta5.getSelectedItemPosition() > 0 ) {

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
                        Renglon = BD.rawQuery("select count(Id_Fertiliza) " +
                                "from t_Fertiliza_Det " +
                                "where Id_Fertiliza='" + text_Codigo.getText().toString().substring(0, 3) + etd_Fecha.getText().toString().trim().substring(8, 10) + CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0, 5) + "' and Fecha='" + etd_Fecha.getText() + "' " +
                                "and c_codigo_pro='" + arrayArticulos.get(i).getcProducto().trim() + "' and c_codigo_eps='" + CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0, 2) + "' ", null);

                        if (Renglon.moveToFirst()) {
                            if (Renglon.getInt(0) > 0) {
                                Toast.makeText(this, "Ya se encuntra ese producto en la lista, favor de revisar.", Toast.LENGTH_SHORT).show();
                            } else {
                                ContentValues registro2 = new ContentValues();
                                registro2.put("Id_Fertiliza", text_Codigo.getText().toString().substring(0, 3) + etd_Fecha.getText().toString().trim().substring(8, 10) + CopiHue.getItem(sp_huerta5.getSelectedItemPosition()).getTexto().substring(0, 5)); //objSDF.format(date1)
                                registro2.put("Fecha", etd_Fecha.getText().toString());
                                registro2.put("c_codigo_pro", arrayArticulos.get(i).getcProducto().trim());
                                registro2.put("Cantidad_Aplicada", arrayArticulos.get(i).getCantidad());
                                // registro2.put("Unidades_aplicadas", arrayArticulos.get(i).getUnidades_aplicadas());
                                registro2.put("Id_Usuario", Usuario);
                                registro2.put("F_Creacion", objSDF.format(date1));
                                registro2.put("c_codigo_eps", CopiEmp.getItem(sp_Empresa5.getSelectedItemPosition()).getTexto().substring(0, 2));
                                BD.insert("t_Fertiliza_Det", null, registro2);
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

    public void ListarFertiliza(View view){
        Intent intento = new Intent(this, Fertiliza_Capturadas.class);
        intento.putExtra("usuario", Usuario);
        intento.putExtra("perfil", Perfil);
        intento.putExtra("huerta", Huerta);

        //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
        startActivity(intento);
        finish();
    }

    public void CargarFertilizacion(){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon;
        Renglon=BD.rawQuery(" select A.Id_Fertiliza,A.Id_Huerta ,A.Observaciones ,A.Id_TipoAplicacion ,A.Id_Presentacion,H.Nombre_Huerta,Pre.Nombre_Presentacion,ta.Nombre_TipoAplicacion,A.c_codigo_eps,eps.v_abrevia_eps,U.v_abrevia_uni,A.Id_Receta,rec.Fecha_Receta,A.Ha_aplicadas, A.Centro_Costos  " +
                "from t_Fertiliza as A " +
                "inner join t_Huerta as H on H.Id_Huerta=A.Id_Huerta and A.c_codigo_eps=H.c_codigo_eps " +
                "inner join t_Presentacion as Pre on A.Id_Presentacion=Pre.Id_Presentacion and Pre.c_codigo_eps=H.c_codigo_eps " +
                "inner join t_Unidad as U on U.c_codigo_uni=Pre.Id_Unidad and U.c_codigo_eps=Pre.c_codigo_eps "+
                "inner join t_TipoAplicacion as ta on A.Id_TipoAplicacion=ta.Id_TipoAplicacion and Ta.c_codigo_eps=Pre.c_codigo_eps " +
                "left join conempresa as eps on eps.c_codigo_eps=A.c_codigo_eps  " +
                "left join t_Receta as rec on rec.c_codigo_eps=A.c_codigo_eps and rec.Id_Receta=A.Id_Receta " +
                "where A.Id_Fertiliza='"+Id+"' and A.c_codigo_eps='"+cepsselapli+"'",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){

                    text_Codigo.setText(Id.substring(0,3)+"-"+Id.substring(3,5)+"-"+Id.substring(5,10));

                    if(Renglon.getString(14)==null){
                        text_CenCos.setText("");
                    }else{
                        text_CenCos.setText(Renglon.getString(14).trim());
                    }

                    int item;

                    if(sp_Empresa5.getSelectedItemPosition()<2){
                        item=0;
                        for (int x=0; x<ItemSPEmp.size();x++){
                            if( ItemSPEmp.get(x).getTexto().trim().equals(Renglon.getString(8).trim()+" - "+Renglon.getString(9).trim())){
                                item=x;
                                break;
                            }
                        }
                        if(item>0){
                            sp_Empresa5.setSelection(item);
                            LineEmpresa=item;
                        }
                    }

                    if(ItemSPHue==null){
                        cargaSpinnerHuertas();
                        CopiHue = new AdaptadorSpinner(Fertilizacion.this, ItemSPHue);
                        sp_huerta5.setAdapter(CopiHue);
                    }else{
                        if (ItemSPHue.size()<=1){
                            cargaSpinnerHuertas();
                            CopiHue = new AdaptadorSpinner(Fertilizacion.this, ItemSPHue);
                            sp_huerta5.setAdapter(CopiHue);
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
                            sp_huerta5.setSelection(item);
                            LineHuerta=item;
                        }
                        String PArrafo;
                        PArrafo=Renglon.getString(2);
                        pt_Observaciones.setText(Renglon.getString(2));
                        String Presentacion=Renglon.getString(4).trim()+" - "+Renglon.getString(6).trim()+ " "+ Renglon.getString(10);
                        String TIpo=Renglon.getString(3).trim()+" - "+Renglon.getString(7);
                        if(ItemSPApli==null){
                            cargaSpinnerTipoAplicacion();
                            CopiApli = new AdaptadorSpinner(Fertilizacion.this, ItemSPApli);
                            sp_TipoAplicacion.setAdapter(CopiApli);
                        }else{
                            if (ItemSPApli.size()<=1){
                                cargaSpinnerTipoAplicacion();
                                CopiApli = new AdaptadorSpinner(Fertilizacion.this, ItemSPApli);
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
                            CopiPre = new AdaptadorSpinner(Fertilizacion.this, ItemSPPre);
                            sp_Presentacion.setAdapter(CopiPre);
                        }else{
                            if (ItemSPPre.size()<=1){
                                cargaSpinnerPresentacion();
                                CopiPre = new AdaptadorSpinner(Fertilizacion.this, ItemSPPre);
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
                        etn_HaApli.setText(Renglon.getString(13).trim());

                        item=0;

                        if(ItemSPRec==null){
                            cargaSpinnerRecetas();
                            CopiRec = new AdaptadorSpinner(Fertilizacion.this, ItemSPRec);
                            sp_Receta.setAdapter(CopiRec);
                        }else{
                            if (ItemSPRec.size()<=1){
                                cargaSpinnerRecetas();
                                CopiRec = new AdaptadorSpinner(Fertilizacion.this, ItemSPRec);
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

}