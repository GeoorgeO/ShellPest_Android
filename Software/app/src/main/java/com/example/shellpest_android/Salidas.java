package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Salidas extends AppCompatActivity  implements View.OnClickListener{

    public String Usuario, Perfil, Huerta,cUnidad,FechaAnt,SID,SEPS;

    int LineEmpresa,LineAlmacen,LineAplicacion;

    Spinner sp_Empresa, sp_Almacen, sp_Aplicacion;
    AutoCompleteTextView actv_Producto;
    EditText et_Fecha,etn_Cantidad;
    ListView lv_GridSalidas;
    private int dia,mes, anio;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPAlm, ItemSPApli;
    private ArrayList<String> ArrayProductos;
    private AdaptadorSpinner CopiEmp, CopiAlm, CopiApli;
    private ArrayAdapter Adaptador_Arreglos;
    TextView tv_Responsable,tv_Unidad,tv_BloquesT;
    Boolean SoloUnaHuerta,EsporAplica,EsporSalida;
    int secuencia;
    double existencia;
    Button btn_Agregar;

    boolean seAbrio,clicLargo;

    Itemsalida Tabla;
    Adaptador_GridSalida Adapter;
    ArrayList<Itemsalida> arrayArticulos;

    DatePickerDialog dtpd;

    boolean[] Diaseleccionado,Diasseleccionadoclean;
    ArrayList<Integer> Listadias,ListadiasClean = new ArrayList<>();
    String[] Arreglodias,ArreglosdiasClean;

    boolean[] DiaseleccionadoDT,DiasseleccionadocleanDT;
    ArrayList<Integer> ListadiasDT,ListadiasCleanDT = new ArrayList<>();
    String[] ArreglodiasDT,ArreglosdiasCleanDT;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salidas);

        SoloUnaHuerta=false;

        secuencia=0;

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");
        SID = getIntent().getStringExtra("ID");
        SEPS = getIntent().getStringExtra("CEPS");
        seAbrio = getIntent().getBooleanExtra("seAbrio",false);

        getSupportActionBar().hide();

        sp_Empresa = (Spinner) findViewById(R.id.sp_Empresa);
        sp_Almacen = (Spinner) findViewById(R.id.sp_Almacen);
        sp_Aplicacion = (Spinner) findViewById(R.id.sp_Aplicacion);
        lv_GridSalidas = (ListView) findViewById(R.id.lv_GridSalidas);
        tv_Unidad = (TextView) findViewById(R.id.tv_Unidad);
        tv_BloquesT=(TextView) findViewById(R.id.tv_BloquesT);

        etn_Cantidad = (EditText) findViewById(R.id.etn_Cantidad);
        actv_Producto = (AutoCompleteTextView) findViewById(R.id.actv_Producto);
        tv_Responsable=(TextView) findViewById(R.id.tv_Responsable);
        btn_Agregar=(Button) findViewById(R.id.btn_Agregar);

        LineEmpresa=0;
        LineAlmacen=0;
        LineAplicacion=0;

        EsporAplica=false;
        EsporSalida=false;

        clicLargo=false;

        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa.setAdapter(CopiEmp);

        if (sp_Empresa.getCount()==2){
            sp_Empresa.setSelection(1);
        }

        existencia=0;

       /* cargaSpinnerHue();
        CopiAlm = new AdaptadorSpinner(this, ItemSPAlm);
        // CopiHue=AdaptadorSpiner;
        sp_Almacen.setAdapter(CopiAlm);*/

        ItemSPApli = new ArrayList<>();
        ItemSPApli.add(new ItemDatoSpinner("Aplicación",""));

        cargarResponsable();

        ArrayProductos=new ArrayList<>();

        cargarProductos();
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

        FechaAnt=et_Fecha.getText().toString();

        arrayArticulos = new ArrayList<>();

        sp_Empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SoloUnaHuerta=false;
                seAbrio=false;

                if(ItemSPAlm==null){
                    cargaSpinnerHue();
                    CopiAlm = new AdaptadorSpinner(Salidas.this, ItemSPAlm);
                    sp_Almacen.setAdapter(CopiAlm);

                    if (sp_Almacen.getCount()==2){
                        sp_Almacen.setSelection(1);
                    }
                }else{
                    if((ItemSPAlm.size()<=1  ) || LineEmpresa!=i){
                        cargaSpinnerHue();
                        CopiAlm = new AdaptadorSpinner(Salidas.this, ItemSPAlm);
                        sp_Almacen.setAdapter(CopiAlm);


                        if(LineEmpresa>0){
                            sp_Almacen.setSelection(LineEmpresa);
                        }else{
                            if (sp_Almacen.getCount()==2){
                                sp_Almacen.setSelection(1);
                            }
                        }
                    }
                }

                   /* cargaSpinnerHue();
                    CopiAlm = new AdaptadorSpinner(getApplicationContext(), ItemSPAlm);
                    // CopiHue=AdaptadorSpiner;
                    sp_Almacen.setAdapter(CopiAlm);
                    LineEmpresa=i;*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Almacen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seAbrio=false;
                if ((SoloUnaHuerta==true && i == 0 ) || (SoloUnaHuerta==false && i>0)) {
                    // Notify the selected item text

                    Arreglodias=ArreglosdiasClean;
                    Diaseleccionado=Diasseleccionadoclean;
                    Listadias=ListadiasClean;
                    Listadias.clear();

                    ArreglodiasDT=ArreglosdiasCleanDT;
                    DiaseleccionadoDT=DiasseleccionadocleanDT;
                    ListadiasDT=ListadiasCleanDT;
                    ListadiasDT.clear();

                    tv_BloquesT.setText("V");
                    Huerta = CopiAlm.getItem(i).getTexto().substring(0, 2);

                    cargaSpinnerBloque();


                    if(ItemSPApli==null){
                        cargaSpinnerAplicacion();
                        CopiApli = new AdaptadorSpinner(Salidas.this, ItemSPApli);
                        sp_Aplicacion.setAdapter(CopiApli);

                        if (sp_Aplicacion.getCount()==2){
                            sp_Aplicacion.setSelection(1);
                        }
                    }else{
                        if(ItemSPApli.size()<=1 || LineEmpresa!=i){
                            cargaSpinnerAplicacion();
                            CopiApli = new AdaptadorSpinner(Salidas.this, ItemSPApli);
                            sp_Aplicacion.setAdapter(CopiApli);

                            if (sp_Aplicacion.getCount()==2){
                                sp_Aplicacion.setSelection(1);
                            }
                        }
                    }

                   /* cargaSpinnerAplicacion(i);
                    CopiApli = new AdaptadorSpinner(getApplicationContext(), ItemSPApli);
                    sp_Aplicacion.setAdapter(CopiApli);*/

                    cargarProductos();
                    Adaptador_Arreglos=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,ArrayProductos);
                    actv_Producto.setAdapter(Adaptador_Arreglos);

                    CargarSalida(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));

                    if(!EsporSalida){
                    if (sp_Aplicacion.getCount()==2){
                            EsporAplica=true;
                            sp_Aplicacion.setSelection(1);
                        }

                    }

                }
                LineAlmacen=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Aplicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seAbrio=false;
                int aplicasel,empsel;
                aplicasel=sp_Aplicacion.getSelectedItemPosition();
                empsel=sp_Empresa.getSelectedItemPosition();
                if(LineAplicacion==0 && lv_GridSalidas.getCount()==0 && SID==null){
                    CargarGridxAplicacion(CopiApli.getItem(sp_Aplicacion.getSelectedItemPosition()).getTexto().substring(0,10),et_Fecha.getText().toString(),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
                    if(arrayArticulos.size()>0 ){
                        GuardarxAplicacion();
                    }
                }

                LineAplicacion=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        actv_Producto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //////////////////////////////////////Inhabilite hasta que este sincronizacion///////////////////////////////////////////////////////
                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(Salidas.this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
                Cursor Renglon;

                String pro;
                pro="select P.c_codigo_uni,U.v_nombre_uni " +
                        "from t_Productos as P " +
                        "left join t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni and U.c_codigo_eps=P.c_codigo_eps    " +
                        "where ltrim(rtrim(P.c_codigo_pro))='"+actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim()+"' " +
                        "and P.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"' ";
                Renglon=BD.rawQuery(pro,null);
               // Renglon=BD.rawQuery("select '000','LITROTE',exi.Existencia from  t_existencias as exi where exi.c_codigo_alm='"+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' and exi.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"' and ltrim(rtrim(c_codigo_pro))='"+actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim()+"'",null);
                if(Renglon.moveToFirst()){

                    do {
                        existencia=SelectExistencias(CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim(),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
                        tv_Unidad.setText("Unidad: "+Renglon.getString(1) +" Existencia: "+existencia);
                        cUnidad=Renglon.getString(0);


                    } while(Renglon.moveToNext());

                    BD.close();
                }else{
                    BD.close();
                }
            }

        });

        actv_Producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actv_Producto.setText("");
            }
        });

        etn_Cantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!String.valueOf(existencia).isEmpty() && etn_Cantidad.getText().toString().length()>0){
                    /*if(Double.parseDouble(etn_Cantidad.getText().toString())>existencia){
                        Toast.makeText(Salidas.this, "NO SE PUEDE DAR SALIDA. La cantidad ingresada excede la existencia de producto.", Toast.LENGTH_SHORT).show();
                        btn_Agregar.setEnabled(false);
                    }else{
                        btn_Agregar.setEnabled(true);
                    }*/
                }else{

                }
                return false;
            }
        });

        /*et_Fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!FechaAnt.equals(et_Fecha.getText().toString())){
                    FechaAnt=et_Fecha.getText().toString();
                    CargarSalida(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
                }
            }
        });*/

        lv_GridSalidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (clicLargo == false) {
                    //ArreglodiasDT=ArreglosdiasCleanDT;
                    DiaseleccionadoDT = DiasseleccionadocleanDT;
                    ListadiasDT = ListadiasCleanDT;
                    ListadiasDT.clear();

                    int TrenglonGrid = i;
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            Salidas.this
                    );
                    builder.setTitle("Selecciona al menos un bloque");
                    builder.setCancelable(false);
                    builder.setMultiChoiceItems(ArreglodiasDT, DiaseleccionadoDT, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                            if (b) {
                                ListadiasDT.add(i);
                                Collections.sort(ListadiasDT);
                            } else {
                                ListadiasDT.remove(i);
                            }
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int j = 0; j < ListadiasDT.size(); j++) {
                                stringBuilder.append(ArreglodiasDT[ListadiasDT.get(j)].substring(0, 4));
                                if (j != ListadiasDT.size() - 1) {
                                    stringBuilder.append(", ");
                                }
                            }
                            String TCC;
                            TCC=stringBuilder.toString();
                            arrayArticulos.get(TrenglonGrid).setCentro_Costos(stringBuilder.toString());
                            TCC=stringBuilder.toString();
                            UpdateCentrosCosto(arrayArticulos.get(TrenglonGrid).getFecha(),arrayArticulos.get(TrenglonGrid).getcProducto().trim(),stringBuilder.toString());
                            TCC=stringBuilder.toString();
                            lv_GridSalidas.setAdapter(null);
                            if (arrayArticulos.size() > 0) {
                                Adapter = new Adaptador_GridSalida(getApplicationContext(), arrayArticulos);
                                lv_GridSalidas.setAdapter(Adapter);
                            } else {
                                //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
                            }
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
                            for (int j = 0; j < DiaseleccionadoDT.length; j++) {
                                DiaseleccionadoDT[j] = false;
                                ListadiasDT.clear();
                                arrayArticulos.get(TrenglonGrid).setCentro_Costos("");
                                lv_GridSalidas.setAdapter(null);
                                if (arrayArticulos.size() > 0) {
                                    Adapter = new Adaptador_GridSalida(getApplicationContext(), arrayArticulos);
                                    lv_GridSalidas.setAdapter(Adapter);
                                } else {
                                    //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    builder.show();
                }
                clicLargo=false;
            }
        });

          lv_GridSalidas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                clicLargo=true;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Salidas.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar el producto: "+arrayArticulos.get(i).getNombre_Producto()+" de la salida?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();
                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Salidas.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

                        int cantidad= BD.delete("t_Salidas_Det","Id_Salida='"+et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' and c_codigo_pro='"+arrayArticulos.get(i).getcProducto()+"' and c_codigo_eps='"+arrayArticulos.get(i).getceps()+"'  ",null);

                        if(cantidad>0){
                            SumarExistencia(Double.parseDouble(arrayArticulos.get(i).getCantidad()),arrayArticulos.get(i).getcProducto().trim(),CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),arrayArticulos.get(i).getceps());


                            if (lv_GridSalidas.getCount()==1){
                                int cantidad4= BD.delete("t_Salidas","Id_Salida='"+et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);

                            }
                        }else{
                            Toast.makeText(Salidas.this,"Ocurrio un error al intentar eliminar el detalle de la salida, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }
                        BD.close();
                        Cargagrid(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
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

          tv_BloquesT.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  AlertDialog.Builder builder=new AlertDialog.Builder(
                          Salidas.this
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
                              Listadias.remove(i);
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
                          tv_BloquesT.setText(stringBuilder.toString());
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
                              tv_BloquesT.setText("");
                          }
                      }
                  });
                  builder.show();
              }
          });

        if(SID!=null){
            CargarAplicacion();
            Cargagrid(SID,SEPS);

        }
    }

    private void DescansoEnMilisegundos(int milisegundos){
        CountDownTimer contadorbajotiempo=new CountDownTimer(milisegundos,1000) {
            @Override
            public void onTick(long l) {
                int a;
                a=1;
                if((l/1000)>5){
                    a++;
                }

            }

            @Override
            public void onFinish() {
                Toast.makeText(Salidas.this, "Ya.", Toast.LENGTH_SHORT).show();


            }
        }.start();
    }

    public void CargarAplicacion(){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon;
        Renglon=BD.rawQuery(" select distinct  A.Id_Salida,A.Id_Responsable,U.Nombre ,A.Id_Almacen ,A.Id_Aplicacion ,A.Fecha,Alm.Nombre_Almacen,A.c_codigo_eps, Emp.v_nombre_eps,AP.Fmin,AP.Fmax " +
                "from t_Salidas as A " +
                "left join t_Almacen as Alm on Alm.Id_Almacen=A.Id_Almacen and A.c_codigo_eps=Alm.c_codigo_eps " +
                "left join UsuarioLogin as U on U.Id_Usuario=A.Id_Responsable  " +
                "left join conempresa as Emp on Emp.c_codigo_eps=A.c_codigo_eps  " +
                "left join (select AD.Id_Aplicacion,AD.c_codigo_eps,Min(AD.Fecha) as Fmin,Max(AD.Fecha) as Fmax from  t_Aplicaciones_Det as AD group by AD.Id_Aplicacion,AD.c_codigo_eps) as AP on Ap.Id_Aplicacion=A.Id_Aplicacion and Ap.c_codigo_eps=A.c_codigo_eps "+
                "where A.Id_Salida='"+SID+"' and A.c_codigo_eps='"+SEPS+"'",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){
                    int item;

                    if(sp_Empresa.getSelectedItemPosition()<2){
                        item=0;
                        for (int x=0; x<ItemSPEmp.size();x++){
                            if( ItemSPEmp.get(x).getTexto().equals(Renglon.getString(7).trim()+" - "+Renglon.getString(8).trim())){
                                item=x;
                                break;
                            }
                        }
                        if(item>0){
                            sp_Empresa.setSelection(item);
                            LineEmpresa=item;
                        }
                    }

                    if(ItemSPAlm==null){
                        cargaSpinnerHue();
                        CopiAlm = new AdaptadorSpinner(Salidas.this, ItemSPAlm);
                        sp_Almacen.setAdapter(CopiAlm);
                    }else{
                        if (ItemSPAlm.size()<=1){
                            cargaSpinnerHue();
                            CopiAlm = new AdaptadorSpinner(Salidas.this, ItemSPAlm);
                            sp_Almacen.setAdapter(CopiAlm);
                        }
                    }
                    String THuerta;
                    THuerta=Renglon.getString(3)+" - "+Renglon.getString(6);
                    item=0;
                    if(ItemSPAlm!=null) {
                        for (int x = 0; x < ItemSPAlm.size(); x++) {
                            THuerta = ItemSPAlm.get(x).getTexto();
                            if (ItemSPAlm.get(x).getTexto().equals(Renglon.getString(3) + " - " + Renglon.getString(6))) {
                                item = x;
                                break;
                            }
                        }
                        if (item > 0) {
                            sp_Almacen.setSelection(item);
                            LineAlmacen = item;
                        }
                    }

                    if(ItemSPApli==null){
                        cargaSpinnerAplicacion();
                        CopiApli= new AdaptadorSpinner(Salidas.this, ItemSPApli);
                        sp_Aplicacion.setAdapter(CopiApli);
                    }else{
                        if (ItemSPApli.size()<=1){
                            cargaSpinnerAplicacion();
                            CopiApli = new AdaptadorSpinner(Salidas.this, ItemSPApli);
                            sp_Aplicacion.setAdapter(CopiApli);
                        }
                    }
                    item=0;
                    if(ItemSPApli!=null) {
                        String TAplica;
                        for (int x = 0; x < ItemSPApli.size(); x++) {
                            TAplica=ItemSPApli.get(x).getTexto()+" == "+Renglon.getString(4) + " - " + Renglon.getString(9) + " al " + Renglon.getString(10);
                            if (ItemSPApli.get(x).getTexto().equals(Renglon.getString(4) + " - " + Renglon.getString(9) + " al " + Renglon.getString(10))) {
                                item = x;
                                break;
                            }
                        }
                        if(item>0) {
                            sp_Aplicacion.setSelection(item);
                            LineAplicacion=item;
                        }
                    }

                    et_Fecha.setText(Renglon.getString(5));

                    tv_Responsable.setText(Renglon.getString(2));


                }else{

                }
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();
        }
        BD.close();
    }

    @Override
    public void onClick(View view) {
        if(view==et_Fecha){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> et_Fecha.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);

            dtpd.show();
            String fecha=et_Fecha.getText().toString();
              if (!FechaAnt.equals(et_Fecha.getText().toString())){
                    FechaAnt=et_Fecha.getText().toString();
                    CargarSalida(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
                }

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

    /*public void agregargrid(View view){
        lv_GridSalidas.setAdapter(null);

        Tabla=new Itemsalida(et_Fecha.getText().toString(),actv_Producto.getText().toString(),etn_Cantidad.getText().toString(),tv_Unidad.getText().toString().substring(tv_Unidad.getText().toString().indexOf(":")+2),CopiBlo.getItem(sp_Bloque.getSelectedItemPosition()).getTexto(),actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim(),cUnidad);
        arrayArticulos.add(Tabla);
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridSalida(getApplicationContext(),arrayArticulos);
            lv_GridSalidas.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    } */

    private void LimpiarDetalle(){
        actv_Producto.setText("");
        actv_Producto.showDropDown();
        etn_Cantidad.setText("0");

    }

    private void cargaSpinnerEmpresa(){
        //CopiEmp=null;

        ItemSPEmp=new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa",""));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;
   // System.out.println("select E.c_codigo_eps,E.v_nombre_eps from conempresa as E inner join t_Usuario_Empresa as UE on UE.c_codigo_eps=E.c_codigo_eps where UE.Id_Usuario='"+Usuario+"'");
        Renglon=BD.rawQuery("select E.c_codigo_eps,E.v_nombre_eps from conempresa as E inner join t_Usuario_Empresa as UE on UE.c_codigo_eps=E.c_codigo_eps where UE.Id_Usuario='"+Usuario+"' ",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPEmp.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1),Renglon.getString(0)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }
    }

    private void CargarGridxAplicacion(String IdAplicacion,String Fecha,String c_codigo_eps){ //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        lv_GridSalidas.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select P.v_nombre_pro, \n" +
                "\tAD.Dosis,\n" +
                "\tA.Unidades_aplicadas,U.v_nombre_uni , AD.c_codigo_pro,P.c_codigo_uni,ifnull(Texi.Existencia,0) as  Existencia, AD.Centro_Costos\n" +
                "from t_Aplicaciones_Det as AD \n" +
                "left join t_Aplicaciones as A on A.Id_Aplicacion=AD.Id_Aplicacion and A.c_codigo_eps=AD.c_codigo_eps \n" +
                "left join t_Productos as P on ltrim(rtrim(AD.c_codigo_pro))=ltrim(rtrim(P.c_codigo_pro)) and P.c_codigo_eps=AD.c_codigo_eps \n" +
                "left join (select exi.c_codigo_pro,exi.c_codigo_eps,exi.c_codigo_alm,exi.Existencia from t_existencias as exi  where exi.c_codigo_eps='"+c_codigo_eps+"' and exi.c_codigo_alm='"+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"')as Texi on ltrim(rtrim(AD.c_codigo_pro))=ltrim(rtrim(Texi.c_codigo_pro)) and Texi.c_codigo_eps=AD.c_codigo_eps \n" +
                "left join t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni and U.c_codigo_eps=P.c_codigo_eps \n" +
                "where AD.Id_Aplicacion='"+IdAplicacion+"' and AD.Fecha='"+Fecha+"' and AD.c_codigo_eps='"+c_codigo_eps+"'",null);

        if(Renglon.moveToFirst()) {

            if (Renglon.moveToFirst()) {

                do {
                    String Existencia,producto;
                    Existencia=Renglon.getString(6);
                    producto=Renglon.getString(4);
                    Tabla=new Itemsalida(Fecha,Renglon.getString(0),String.valueOf(Renglon.getDouble(1) * Renglon.getDouble(2)),Renglon.getString(3),Renglon.getString(7),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),c_codigo_eps);
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Aplicaciones guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridSalida(getApplicationContext(),arrayArticulos);
            lv_GridSalidas.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargaSpinnerAplicacion(){
        //CopiApli=null;
        //CopiApli.clear();

        ItemSPApli=new ArrayList<>();
        ItemSPApli.add(new ItemDatoSpinner("Aplicacion",""));


            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            String consulta="select A.Id_Aplicacion ,Min(Fecha) as Fmin,Max(AD.Fecha) as Fmax " +
                    "from t_Aplicaciones as A " +
                    "left join  t_Almacen as Alm on ltrim(rtrim(Alm.Id_Huerta))=ltrim(rtrim(A.Id_Huerta)) and Alm.c_codigo_eps=A.c_codigo_eps " +
                    "left join  t_Aplicaciones_Det as AD on ltrim(rtrim(AD.Id_Aplicacion))=ltrim(rtrim(A.Id_Aplicacion)) and A.c_codigo_eps=AD.c_codigo_eps " +
                    //"left join  t_Huerta as Hue on ltrim(rtrim(Hue.Id_Huerta))=ltrim(rtrim(A.Id_Huerta)) and A.c_codigo_eps=Hue.c_codigo_eps " +
                    "where ltrim(rtrim(Alm.Id_Almacen))='"+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' and A.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"' " +
                    "group by A.Id_Aplicacion, A.c_codigo_eps ";

            Renglon=BD.rawQuery(consulta,null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSPApli.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" al "+Renglon.getString(2),Renglon.getString(0)));
                } while(Renglon.moveToNext());

                BD.close();
            }else{
                BD.close();
            }

    }

    private void cargarProductos(){

        ArrayProductos.clear();
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select T.v_nombre_pro,T.c_codigo_pro from t_Productos as T where c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

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
                "from t_Almacen as A " +
                "inner join t_Huerta as H on H.Id_Huerta=A.Id_Huerta and H.c_codigo_eps=A.c_codigo_eps " +
                "inner join t_Bloque as B on B.Id_Huerta=H.Id_Huerta and B.c_codigo_eps=H.c_codigo_eps " +
                "where A.Id_Almacen='"+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' " +
                "and A.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"' and B.TipoBloque='B'",null);

        if(Renglon.moveToFirst()){
            int tamanio;
            tamanio=0;
            Arreglodias=new String[Renglon.getCount()];
            ArreglodiasDT=new String[Renglon.getCount()];
            do {


                    Arreglodias[tamanio] =Renglon.getString(0)+" - "+Renglon.getString(1);
                    ArreglodiasDT[tamanio] =Renglon.getString(0)+" - "+Renglon.getString(1);
                tamanio++;

            } while(Renglon.moveToNext());

            BD.close();

            Diaseleccionado=new boolean[Arreglodias.length];
            DiaseleccionadoDT=new boolean[ArreglodiasDT.length];
        }else{
            BD.close();
        }
    }

    private void cargaSpinnerHue(){
       // CopiAlm=null;
       // CopiAlm.clear();

        ItemSPAlm=new ArrayList<>();

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            if(sp_Empresa.getSelectedItemPosition()==0){
                Renglon=BD.rawQuery("select Id_Almacen,Nombre_Almacen from t_Almacen where c_codigo_eps='"+SEPS+"' group by c_codigo_eps,Id_Almacen",null);
            }else{
                Renglon=BD.rawQuery("select Id_Almacen,Nombre_Almacen from t_Almacen where c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"' group by c_codigo_eps,Id_Almacen",null);
            }
        }else{
            Renglon=BD.rawQuery("select distinct Hue.Id_Almacen,Hue.Nombre_Almacen from t_Almacen as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta and Hue.c_codigo_eps=UH.c_codigo_eps where UH.Id_Usuario='"+Usuario+"' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
            //Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and Id_Huerta='"+Huerta+"'",null);
            //sp_Hue.setEnabled(false);
        }

        if(Renglon.getCount()>1){
            ItemSPAlm.add(new ItemDatoSpinner("Almacen",""));
        }else{
            if(Renglon.getCount()==1){
                SoloUnaHuerta=true;
            }
        }

        if(Renglon.moveToFirst()){
            do {
                ItemSPAlm.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1),Renglon.getString(0)));
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No se encontraron datos en Almacen",Toast.LENGTH_SHORT).show();
            BD.close();
        }
        BD.close();
    }

    private void cargarResponsable(){

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select Nombre from UsuarioLogin where Id_Usuario='"+Usuario+"' ",null);

        if(Renglon.moveToFirst()){

            do {
                tv_Responsable.setText("Responsable: "+Renglon.getString(0));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    private void GuardarEcabezado(){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());

        Cursor Renglon;
        Renglon=BD.rawQuery("select count(sal.Id_Salida) as Sihay from t_Salidas as sal where sal.Id_Salida='"+et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' and sal.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        if(Renglon.moveToFirst()){
            do {
                if (Renglon.getInt(0)>0){

                    ContentValues registro = new ContentValues();

                    registro.put("c_codigo_eps", CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
                    registro.put("Id_Aplicacion",CopiApli.getItem(sp_Aplicacion.getSelectedItemPosition()).getTexto().substring(0,10));
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    int cantidad=BD.update("t_Salidas",registro,"Id_Salida='"+et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                    if(cantidad>0){
                        //////Toast.makeText(MainActivity.this,"Se actualizo t_Calidad correctamente.",Toast.LENGTH_SHORT).show();
                    }else{
                        //////Toast.makeText(MainActivity.this,"Ocurrio un error al intentar actualizar t_Calidad, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    ContentValues registro= new ContentValues();
                    registro.put("Id_Salida",et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2));
                    registro.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
                    registro.put("Id_Responsable",Usuario);
                    registro.put("Id_Almacen",CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2));
                    registro.put("Id_Aplicacion",CopiApli.getItem(sp_Aplicacion.getSelectedItemPosition()).getTexto().substring(0,10));
                    registro.put("Fecha",et_Fecha.getText().toString());
                    registro.put("Id_Usuario",Usuario);
                    registro.put("F_Creacion",objSDF.format(date1));
                    BD.insert("t_Salidas",null,registro);
                }

            } while(Renglon.moveToNext());



        }else{
            Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();
        }
    }

    public void Agregar(View view) {
        boolean FaltoAlgo;
        FaltoAlgo = false;
        String Mensaje;
        Mensaje = "";

        if (sp_Empresa.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una empresa,Verifica por favor";
        }
        if (sp_Almacen.getSelectedItemPosition() > 0 || SoloUnaHuerta==true) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar un almacen,Verifica por favor";
        }
        if (sp_Aplicacion.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una aplicacion,Verifica por favor";
        }
        if (actv_Producto.getText().toString().indexOf("|") >= 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta teclear un producto,Verifica por favor";
        }
        try {
            if (Double.parseDouble(etn_Cantidad.getText().toString()) > 0) {

            } else {
                FaltoAlgo = true;
                Mensaje = "Falta teclear la canrtidad de producto,Verifica por favor";
            }
        } catch (IllegalStateException e) {
            etn_Cantidad.setText("0");
        }
        catch(NumberFormatException f)
        {
            etn_Cantidad.setText("0");
        }
        catch(Exception g)
        {
            etn_Cantidad.setText("0");
        }
        int seleccionados=0;
        for (int i=0;i<Diaseleccionado.length;i++){
            if(Diaseleccionado[i]){
                seleccionados++;
            }
        }
        if(seleccionados>0){

        }else{
            FaltoAlgo=true;
            Mensaje="Falta seleccionar un bloque,Verifica por favor";
        }

        /*if (!String.valueOf(existencia).isEmpty() && etn_Cantidad.getText().toString().length()>0){
            if(Double.parseDouble(etn_Cantidad.getText().toString())>existencia){
                FaltoAlgo=true;
                Mensaje= "NO SE PUEDE DAR SALIDA. La cantidad ingresada excede la existencia de producto.";

            }
        }*/

        /*if(String.valueOf(existencia).isEmpty()){
            FaltoAlgo=true;
            Mensaje= "NO SE PUEDE DAR SALIDA. Existencia Nula.";
        }*/

        if (!FaltoAlgo){
            GuardarEcabezado();

            InsertarSalidaDet(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim(),Double.parseDouble(etn_Cantidad.getText().toString()),tv_BloquesT.getText().toString().trim(),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2),existencia,CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2));


            LimpiarDetalle();
            Cargagrid(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
        }else{
            Toast.makeText(this,Mensaje,Toast.LENGTH_SHORT).show();
        }
    }

    private void GuardarxAplicacion(){
        boolean FaltoAlgo;
        FaltoAlgo = false;
        String Mensaje;
        Mensaje = "";

        if (sp_Empresa.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una empresa,Verifica por favor";
        }
        if (sp_Almacen.getSelectedItemPosition() > 0 || SoloUnaHuerta==true) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar un almacen,Verifica por favor";
        }
        if (sp_Aplicacion.getSelectedItemPosition() > 0) {

        } else {
            FaltoAlgo = true;
            Mensaje = "Falta seleccionar una aplicacion,Verifica por favor";
        }

        if (!FaltoAlgo){
            GuardarEcabezado();

            for(int i=0;i<arrayArticulos.size();i++) {

                if (arrayArticulos.get(i).getcProducto().trim().length() > 0) {
                    InsertarSalidaDet(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2),arrayArticulos.get(i).getcProducto().trim(),Double.parseDouble(arrayArticulos.get(i).getCantidad()),arrayArticulos.get(i).getCentro_Costos(),arrayArticulos.get(i).getceps(),Double.parseDouble(arrayArticulos.get(i).getExistencia()),CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0, 2));


                    int almsel;
                    almsel=sp_Almacen.getSelectedItemPosition();
                    if(sp_Almacen.getSelectedItemPosition()>1) {
                        Cargagrid(et_Fecha.getText().toString().substring(6) + et_Fecha.getText().toString().substring(3, 5) + et_Fecha.getText().toString().substring(0, 2) + CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0, 2), CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0, 2));
                    }
                }
            }


        }else{
            Toast.makeText(this,Mensaje,Toast.LENGTH_SHORT).show();
        }
    }

    public void AbrirSalidas(View view){
        Intent intento = new Intent(this, Salidas_Capturadas.class);
        intento.putExtra("usuario", Usuario);
        intento.putExtra("perfil", Perfil);
        intento.putExtra("huerta", Huerta);

        //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
        startActivity(intento);
       finish();
    }

    private void CargarSalida(String Id,String c_codigo_eps){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select S.c_codigo_eps," +
                "E.v_nombre_eps," +
                "S.Id_Responsable," +
                "U.Nombre," +
                "S.Id_Almacen," +
                "A.Nombre_Almacen," +
                "S.Id_Aplicacion," +
                "S.Fecha," +
                "TAD.Fmin," +
                "TAD.Fmax " +
                "from t_Salidas as S " +
                "inner join conempresa as E on E.c_codigo_eps=S.c_codigo_eps " +
                "inner join (select Min(AD.Fecha) as Fmin,Max(AD.Fecha) as Fmax, AD.Id_Aplicacion,AD.c_codigo_eps from t_Aplicaciones_Det as AD group by AD.Id_Aplicacion, AD.c_codigo_eps) as TAD on TAD.Id_Aplicacion=S.Id_Aplicacion and TAD.c_codigo_eps=S.c_codigo_eps "+
                "inner join UsuarioLogin as U on U.Id_Usuario=S.Id_Responsable "+
                "inner join t_Almacen as A on A.Id_Almacen=S.Id_Almacen and A.c_codigo_eps=S.c_codigo_eps "+
                "where Id_Salida='"+Id+"' and S.c_codigo_eps='"+c_codigo_eps+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    EsporSalida=true;
                    int item;
                    if(EsporAplica==false){

                        item=0;
                        for (int x=0; x<ItemSPEmp.size();x++){
                            if( ItemSPEmp.get(x).getTexto().equals(Renglon.getString(0)+" - "+Renglon.getString(1))){
                                item=x;
                                break;
                            }
                        }
                        sp_Empresa.setSelection(item);


                        item=0;
                        for (int x=0; x<ItemSPAlm.size();x++){
                            String TAlmacen;
                            TAlmacen= ItemSPAlm.get(x).getTexto();
                            if( ItemSPAlm.get(x).getTexto().equals(Renglon.getString(4)+" - "+Renglon.getString(5))){
                                item=x;
                                break;
                            }
                        }
                        sp_Almacen.setSelection(item);
                    }
                    tv_Responsable.setText("Responsable: "+Renglon.getString(3));

                    item=0;
                    for (int x=0; x<ItemSPApli.size();x++){
                        if( ItemSPApli.get(x).getTexto().equals(Renglon.getString(6)+" - "+Renglon.getString(8)+" al "+Renglon.getString(9))){
                            item=x;
                            break;
                        }
                    }
                    sp_Aplicacion.setSelection(item);
                    et_Fecha.setText(Renglon.getString(7));

                    Cargagrid(Id,c_codigo_eps);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Aplicaciones guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        else{
            lv_GridSalidas.setAdapter(null);
            arrayArticulos.clear();
        }
    }

    private void Cargagrid(String Id,String c_codigo_eps){
        lv_GridSalidas.setAdapter(null);
        arrayArticulos.clear();

        if(ItemSPAlm==null){
            cargaSpinnerHue();
            CopiAlm = new AdaptadorSpinner(Salidas.this, ItemSPAlm);
            sp_Almacen.setAdapter(CopiAlm);
        }else{
            if (ItemSPAlm.size()<=1){
                cargaSpinnerHue();
                CopiAlm = new AdaptadorSpinner(Salidas.this, ItemSPAlm);
                sp_Almacen.setAdapter(CopiAlm);
            }
        }

        if(LineAlmacen>0){
            AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

            Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
            SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
            Date date1=objDate;

            // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

            Cursor Renglon =BD.rawQuery("select SD.Id_Salida, \n" +
                    "\tSD.c_codigo_pro,\n" +
                    "\tSD.Cantidad,\n" +
                    "\tSD.Id_Bloque, \n" +
                    "\tP.c_codigo_uni, \n" +
                    "\tP.v_nombre_pro, \n" +
                    "\tU.v_nombre_uni,SD.c_codigo_eps \n" +
                    "from t_Salidas_Det as SD \n" +
                    "left join t_Productos as P on ltrim(rtrim(SD.c_codigo_pro))=ltrim(rtrim(P.c_codigo_pro)) and P.c_codigo_eps=SD.c_codigo_eps \n" +
                    "left join t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni and U.c_codigo_eps=P.c_codigo_eps \n" +
                    "where SD.Id_Salida='"+Id+"' and SD.c_codigo_eps='"+c_codigo_eps+"'",null);

            if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
                if (Renglon.moveToFirst()) {

                    do {

                        Tabla=new Itemsalida(Renglon.getString(0),Renglon.getString(5),Renglon.getString(2),Renglon.getString(6),Renglon.getString(3),Renglon.getString(1),Renglon.getString(4),String.valueOf(SelectExistencias(CopiAlm.getItem(LineAlmacen).getTexto().substring(0,2),Renglon.getString(1),Renglon.getString(8))),Renglon.getString(7));
                        arrayArticulos.add(Tabla);
                    } while (Renglon.moveToNext());

                    BD.close();
                } else {
                    Toast.makeText(this, "No hay datos en t_Aplicaciones guardados", Toast.LENGTH_SHORT).show();
                    BD.close();
                }
            }
        }

        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridSalida(getApplicationContext(),arrayArticulos);
            lv_GridSalidas.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean SumarExistencia(Double Cantidad,String Producto,String Almacen,String Empresa){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        existencia=SelectExistencias(Almacen,Producto,Empresa);
        /*Cursor Renglon;

        Renglon =BD.rawQuery("select Existencia " +
                "from t_existencias " +
                "where c_codigo_eps='"+Empresa+"' " +
                "and ltrim(rtrim(c_codigo_pro))='"+Producto+"' and c_codigo_alm='"+Almacen+"' " ,null);

        if(Renglon.moveToFirst()) {
            if (Renglon.getInt(0) > 0) {
                existencia=Renglon.getDouble(0);

            }
        }else {
            InsertarProAExiAlm(Producto,Almacen,Empresa);
        }*/
        ContentValues registro3 = new ContentValues();

        registro3.put("Existencia", existencia +  Cantidad);

        int cantidad3=BD.update("t_existencias",registro3,"ltrim(rtrim(c_codigo_pro))='"+Producto.trim()+"' and c_codigo_eps='"+Empresa+"' and c_codigo_alm='"+Almacen+"'",null);

        if(cantidad3>0){
            BD.close();
            return true;
        }else{
            BD.close();
            return false;
        }

    }
    private boolean RestarExistencia(Double Cantidad,String Producto,String Almacen,String Empresa){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        existencia=SelectExistencias(Almacen,Producto,Empresa);
       /* Cursor Renglon;

        Renglon =BD.rawQuery("select Existencia " +
                "from t_existencias " +
                "where c_codigo_eps='"+Empresa+"' " +
                "and ltrim(rtrim(c_codigo_pro))='"+Producto+"' and c_codigo_alm='"+Almacen+"' " ,null);

        if(Renglon.moveToFirst()) {
            if (Renglon.getInt(0) > 0) {
                existencia=Renglon.getDouble(0);

            }
        }else {
            InsertarProAExiAlm(Producto,Almacen,Empresa);
        }*/
        ContentValues registro3 = new ContentValues();

        registro3.put("Existencia", existencia - Cantidad);

        int cantidad3=BD.update("t_existencias",registro3,"ltrim(rtrim(c_codigo_pro))='"+Producto.trim()+"' and c_codigo_eps='"+Empresa+"' and c_codigo_alm='"+Almacen+"'",null);

        if(cantidad3>0){
            BD.close();
            return true;
        }else{
            BD.close();
            return false;
        }
    }

    private boolean InsertarProAExiAlm(String Producto,String Alamcen,String Empresa){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        ContentValues registro2= new ContentValues();
        registro2.put("c_codigo_eps",Empresa); //objSDF.format(date1)
        registro2.put("c_codigo_pro",Producto.trim());
        registro2.put("c_codigo_alm",Alamcen);
        registro2.put("Existencia",0);

        long cantidad=BD.insert("t_existencias",null,registro2);
        if(cantidad>0){
            BD.close();
            return true;
        }else{
            BD.close();
            return false;
        }

    }

    private boolean InsertarSalidaDet(String Salida,String Producto,Double Cantidad,String CC,String Empresa,Double Existencia, String Almacen){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        Cursor Renglon;

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        Renglon =BD.rawQuery("select count(Id_Salida) " +
                "from t_Salidas_Det " +
                "where Id_Salida='"+Salida+"' " +
                "and ltrim(rtrim(c_codigo_pro))='"+Producto.trim()+"' and c_codigo_eps='"+ Empresa+"' " ,null);

        if(Renglon.moveToFirst()){
            if(Renglon.getInt(0)>0){
                Toast.makeText(this,"Ya se encuentra ese producto en la lista, favor de revisar.",Toast.LENGTH_SHORT).show();
                BD.close();
                return true;
            }else{
                //if (Double.parseDouble(arrayArticulos.get(i).getExistencia())>0 && Double.parseDouble(arrayArticulos.get(i).getCantidad())>0){
                //if(Double.parseDouble(arrayArticulos.get(i).getCantidad())<=Double.parseDouble(arrayArticulos.get(i).getExistencia())){
                ContentValues registro2= new ContentValues();
                registro2.put("Id_Salida",Salida); //objSDF.format(date1)
                registro2.put("c_codigo_pro",Producto.trim());
                registro2.put("Cantidad",Cantidad);
                registro2.put("Id_Bloque",CC);
                registro2.put("Id_Usuario",Usuario);
                registro2.put("F_Creacion",objSDF.format(date1));
                registro2.put("c_codigo_eps",Empresa);
                registro2.put("n_exiant_mov",Existencia);
                long Temcantidad=BD.insert("t_Salidas_Det",null,registro2);
                if(Temcantidad>0){
                    BD.close();
                    return RestarExistencia(Cantidad,Producto,Almacen,Empresa);

                }else{
                    BD.close();
                    return false;
                }

            }

        }else{
            BD.close();
            Toast.makeText(this,"No Regreso nada la consulta de Detalle",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean UpdateCentrosCosto(String Salida,String Producto,String CC){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        ContentValues registro2= new ContentValues();
        registro2.put("Id_Bloque",CC); //objSDF.format(date1)

        long cantidad=BD.update("t_Salidas_Det",registro2,"Id_Salida='"+Salida+"' and c_codigo_pro='"+Producto+"' ",null);
        if(cantidad>0){
            BD.close();
            return true;
        }else{
            BD.close();
            return false;
        }
    }

    private double SelectExistencias(String Almacen,String Producto,String Empresa){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon;

        Renglon =BD.rawQuery("select Existencia " +
                "from t_existencias " +
                "where c_codigo_eps='"+Empresa+"' " +
                "and ltrim(rtrim(c_codigo_pro))='"+Producto+"' and c_codigo_alm='"+Almacen+"' " ,null);

        if(Renglon.moveToFirst()) {
            BD.close();
                return Renglon.getDouble(0);
        }else {
            if (InsertarProAExiAlm(Producto,Almacen,Empresa)){
                BD.close();
                return 0;
            }else{
                BD.close();
                return 0;
            }
        }
    }

}