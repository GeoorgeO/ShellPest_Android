package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Salidas extends AppCompatActivity  implements View.OnClickListener{

    public String Usuario, Perfil, Huerta,cUnidad;
    Spinner sp_Empresa, sp_Almacen, sp_Aplicacion,sp_Bloque;
    AutoCompleteTextView actv_Producto;
    EditText et_Fecha,etn_Cantidad;
    ListView lv_GridSalidas;
    private int dia,mes, anio;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPAlm, ItemSPApli,ItemSPBlo;
    private ArrayList<String> ArrayProductos;
    private AdaptadorSpinner CopiEmp, CopiAlm, CopiApli,CopiBlo;
    private ArrayAdapter Adaptador_Arreglos;
    TextView tv_Responsable,tv_Unidad;
    Boolean SoloUnaHuerta;
    int secuencia;

    Itemsalida Tabla;
    Adaptador_GridSalida Adapter;
    ArrayList<Itemsalida> arrayArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salidas);

        SoloUnaHuerta=false;

        secuencia=0;

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        getSupportActionBar().hide();

        sp_Empresa = (Spinner) findViewById(R.id.sp_Empresa);
        sp_Almacen = (Spinner) findViewById(R.id.sp_Almacen);
        sp_Aplicacion = (Spinner) findViewById(R.id.sp_Aplicacion);
        lv_GridSalidas = (ListView) findViewById(R.id.lv_GridSalidas);
        tv_Unidad = (TextView) findViewById(R.id.tv_Unidad);
        sp_Bloque = (Spinner) findViewById(R.id.sp_Bloque);
        etn_Cantidad = (EditText) findViewById(R.id.etn_Cantidad);
        actv_Producto = (AutoCompleteTextView) findViewById(R.id.actv_Producto);
        tv_Responsable=(TextView) findViewById(R.id.tv_Responsable);

       /* ItemSPEmp = new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa"));
        ItemSPEmp.add(new ItemDatoSpinner("Desarrollo Agricola Alegro"));
        ItemSPEmp.add(new ItemDatoSpinner("Alejandro Guerrero Vazquez"));*/
        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa.setAdapter(CopiEmp);



        cargaSpinnerHue();
        CopiAlm = new AdaptadorSpinner(this, ItemSPAlm);
        // CopiHue=AdaptadorSpiner;
        sp_Almacen.setAdapter(CopiAlm);

       /* ItemSPAlm = new ArrayList<>();
        ItemSPAlm.add(new ItemDatoSpinner("Almacen"));
        ItemSPAlm.add(new ItemDatoSpinner("Arroyos"));
        ItemSPAlm.add(new ItemDatoSpinner("Chimilpa"));
        ItemSPAlm.add(new ItemDatoSpinner("Fontana"));
        CopiAlm = new AdaptadorSpinner(this, ItemSPAlm);
        sp_Almacen.setAdapter(CopiAlm);*/

        ItemSPApli = new ArrayList<>();
        ItemSPApli.add(new ItemDatoSpinner("Aplicación"));
       /* ItemSPApli.add(new ItemDatoSpinner("Luis Olalde"));
        ItemSPApli.add(new ItemDatoSpinner("Gonzalo Cano"));
        CopiApli = new AdaptadorSpinner(this, ItemSPApli);
        cargaSpinnerAplicacion();
        sp_Aplicacion.setAdapter(CopiApli);*/

       /* ItemSPUni = new ArrayList<>();
        ItemSPUni.add(new ItemDatoSpinner("Unidad"));
        ItemSPUni.add(new ItemDatoSpinner("Kilogramo"));
        ItemSPUni.add(new ItemDatoSpinner("Litro"));
        ItemSPUni.add(new ItemDatoSpinner("Pieza"));
        CopiUni = new AdaptadorSpinner(this, ItemSPUni);
        sp_Unidad.setAdapter(CopiUni);*/

       /* ItemSPBlo = new ArrayList<>();
        ItemSPBlo.add(new ItemDatoSpinner("Centro Costos"));
        ItemSPBlo.add(new ItemDatoSpinner("Bloque 1"));
        ItemSPBlo.add(new ItemDatoSpinner("Bloque 2"));
        ItemSPBlo.add(new ItemDatoSpinner("Bloque 3"));*/

        cargarResponsable();

        ArrayProductos=new ArrayList<>();
        /*ArrayProductos.add(new String("Agrex abc  Acidificante | EQUI0008"));
        ArrayProductos.add(new String("INEX-A | FILM0025"));
        ArrayProductos.add(new String("ACIDO NITRICO | MANT0007"));
        ArrayProductos.add(new String("Agrex-F | PAPE0011"));
        ArrayProductos.add(new String("Medal | PLU30018"));
        ArrayProductos.add(new String("Xpander Plus | TANA0001"));
        ArrayProductos.add(new String("Acigib | PAPE0032"));
        ArrayProductos.add(new String("Pardy | MANT0008"));*/
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

        arrayArticulos = new ArrayList<>();

        sp_Almacen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if ((SoloUnaHuerta==true && i == 0 ) || (SoloUnaHuerta==false && i>0)) {
                    // Notify the selected item text
                    Huerta = CopiAlm.getItem(i).getTexto().substring(0, 2);

                    cargaSpinnerBloque();
                    CopiBlo = new AdaptadorSpinner(getApplicationContext(), ItemSPBlo);
                    //CopiPto=AdaptadorSpiner;
                    sp_Bloque.setAdapter(CopiBlo);

                    cargaSpinnerAplicacion();
                    CopiApli = new AdaptadorSpinner(getApplicationContext(), ItemSPApli);
                    sp_Aplicacion.setAdapter(CopiApli);

                    CargarSalida(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2));
                }
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

                //String pro;
                //pro="select P.c_codigo_uni,U.v_nombre_uni from t_Productos as P left join  t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni where P.c_codigo_pro='"+actv_Productos.getText().toString().substring(actv_Productos.getText().toString().indexOf("|")+2).trim()+"'";
                Renglon=BD.rawQuery("select P.c_codigo_uni,U.v_nombre_uni from t_Productos as P left join  t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni where ltrim(rtrim(P.c_codigo_pro))='"+actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim()+"'",null);

                if(Renglon.moveToFirst()){

                    do {
                        tv_Unidad.setText("Unidad: "+Renglon.getString(1));
                        cUnidad=Renglon.getString(0);
                    } while(Renglon.moveToNext());

                    BD.close();
                }else{
                    BD.close();
                }
            }

        });

    }

    @Override
    public void onClick(View view) {
        if(view==et_Fecha){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> et_Fecha.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
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

    public void agregargrid(View view){
        lv_GridSalidas.setAdapter(null);

        Tabla=new Itemsalida(et_Fecha.getText().toString(),actv_Producto.getText().toString(),etn_Cantidad.getText().toString(),tv_Unidad.getText().toString().substring(tv_Unidad.getText().toString().indexOf(":")+2),CopiBlo.getItem(sp_Bloque.getSelectedItemPosition()).getTexto(),actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim(),cUnidad);
        arrayArticulos.add(Tabla);
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridSalida(getApplicationContext(),arrayArticulos);
            lv_GridSalidas.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }



    }

    private void LimpiarDetalle(){
        actv_Producto.setText("");
        actv_Producto.showDropDown();
        etn_Cantidad.setText("0");

    }

    private void cargaSpinnerEmpresa(){
        CopiEmp=null;

        ItemSPEmp=new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select E.c_codigo_eps,E.v_nombre_eps from conempresa as E ",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPEmp.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    private void cargaSpinnerAplicacion(){
        CopiApli=null;

        ItemSPApli=new ArrayList<>();
        ItemSPApli.add(new ItemDatoSpinner("Aplicacion"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select A.Id_Aplicacion,Min(Fecha) as Fmin,Max(AD.Fecha) as Fmax from t_Aplicaciones as A inner join t_Aplicaciones_Det as AD on AD.Id_Aplicacion=A.Id_Aplicacion inner join t_Huerta as Hue on Hue.Id_Huerta=A.Id_Huerta inner join t_Almacen as Alm on Alm.Id_Huerta=Hue.Id_Huerta where Alm.Id_Almacen='"+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' group by A.Id_Aplicacion",null);

        if(Renglon.moveToFirst()){
            do {
                ItemSPApli.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" al "+Renglon.getString(2)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    private void cargarProductos(){
        //ArrayProductos=null;
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select T.v_nombre_pro,T.c_codigo_pro from t_Productos as T ",null);

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
        CopiBlo=null;

        ItemSPBlo=new ArrayList<>();
        ItemSPBlo.add(new ItemDatoSpinner("Centro Costos"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select B.Id_Bloque,B.Nombre_Bloque from t_Almacen as A inner join t_Huerta as H on H.Id_Huerta=A.Id_Huerta inner join t_Bloque as B on B.Id_Huerta=H.Id_Huerta  where A.Id_Almacen='"+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPBlo.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    private void cargaSpinnerHue(){
        CopiAlm=null;

        ItemSPAlm=new ArrayList<>();

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;




        if(Perfil.equals("001")){

            Renglon=BD.rawQuery("select Id_Almacen,Nombre_Almacen from t_Almacen",null);
        }else{
            Renglon=BD.rawQuery("select Hue.Id_Almacen,Hue.Nombre_Almacen from t_Almacen as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"'",null);
            //Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and Id_Huerta='"+Huerta+"'",null);
            //sp_Hue.setEnabled(false);
        }

        if(Renglon.getCount()>1){
            ItemSPAlm.add(new ItemDatoSpinner("Almacen"));
        }else{
            if(Renglon.getCount()==1){
                SoloUnaHuerta=true;
            }
        }

        if(Renglon.moveToFirst()){
            do {
                ItemSPAlm.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
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

    public void Agregar(View view){
        boolean FaltoAlgo;
        FaltoAlgo=false;

        if ()

        if (!FaltoAlgo){
            AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

            Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
            SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
            Date date1=objDate;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            String currentTime = simpleDateFormat.format(new Date());

            Cursor Renglon;
            Renglon=BD.rawQuery("select count(sal.Id_Salida) as Sihay from t_Salidas as sal where sal.Id_Salida='"+et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
            if(Renglon.moveToFirst()){
                do {
                    if (Renglon.getInt(0)>0){


                        ContentValues registro = new ContentValues();

                        registro.put("c_codigo_eps", CopiEmp.getItem(sp_Empresa.getSelectedItemPosition()).getTexto().substring(0,2));
                        registro.put("Id_Aplicacion",CopiApli.getItem(sp_Aplicacion.getSelectedItemPosition()).getTexto().substring(0,3));
                        registro.put("Id_Usuario",Usuario);
                        registro.put("F_Creacion",objSDF.format(date1));
                        int cantidad=BD.update("t_Salidas",registro,"Id_Salida='"+et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

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
                        registro.put("Id_Aplicacion",CopiApli.getItem(sp_Aplicacion.getSelectedItemPosition()).getTexto());
                        registro.put("Fecha",et_Fecha.getText().toString());
                        registro.put("Id_Usuario",Usuario);
                        registro.put("F_Creacion",objSDF.format(date1));
                        BD.insert("t_Salidas",null,registro);
                    }

                } while(Renglon.moveToNext());



            }else{
                Toast.makeText(this,"No Regreso nada la consulta de Encabezado",Toast.LENGTH_SHORT).show();

            }


            Renglon =BD.rawQuery("select count(Id_Salida) " +
                    "from t_Salidas_Det " +
                    "where Id_Salida='"+et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)+"' " +
                    "and ltrim(rtrim(c_codigo_pro)=ltrim(rtrim('"+ArrayProductos.get(1).substring(ArrayProductos.get(1).indexOf("|"))+2+"')) " ,null);



            if(Renglon.moveToFirst()){
                if(Renglon.getInt(0)>0){
                    Toast.makeText(this,"Ya se encuntra ese producto en la lista, favor de revisar.",Toast.LENGTH_SHORT).show();
                }else{

                    ContentValues registro2= new ContentValues();
                    registro2.put("Id_Salida",et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2)); //objSDF.format(date1)
                    registro2.put("c_codigo_pro",actv_Producto.getText().toString().substring(actv_Producto.getText().toString().indexOf("|")+2).trim());
                    registro2.put("Cantidad",etn_Cantidad.getText().toString());
                    registro2.put("Id_Bloque",CopiBlo.getItem(sp_Bloque.getSelectedItemPosition()).getTexto().substring(0,4));
                    registro2.put("Id_Usuario",Usuario);
                    registro2.put("F_Creacion",objSDF.format(date1));

                    BD.insert("t_Salidas_Det",null,registro2);
                }

            }else{
                Toast.makeText(this,"No Regreso nada la consulta de Detalle",Toast.LENGTH_SHORT).show();
            }



            BD.close();

            LimpiarDetalle();
            Cargagrid(et_Fecha.getText().toString().substring(6)+et_Fecha.getText().toString().substring(3,5)+et_Fecha.getText().toString().substring(0,2)+CopiAlm.getItem(sp_Almacen.getSelectedItemPosition()).getTexto().substring(0,2));
        }
    }

    private void CargarSalida(String Id){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select S.c_codigo_eps,E.v_nombre_eps,S.Id_Responsable,U.Nombre,S.Id_Almacen,A.Nombre_Almacen,S.Id_Aplicacion,S.Fecha " +
                "from t_Salidas as S inner join conempresa as E on E.c_codigo_eps=S.c_codigo_eps " +
                "inner join UsuarioLogin as U on U.Id_Usuario=S.Id_Responsable "+
                "inner join t_Almacen as A on A.Id_Almacen=S.Id_Almacen "+
                "where Id_Salida='"+Id+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {

                    int item;
                    item=0;
                    for (int x=0; x<ItemSPEmp.size();x++){
                        if( ItemSPEmp.get(x).getTexto().equals(Renglon.getString(0)+" - "+Renglon.getString(1))){
                            item=x;
                            break;
                        }
                    }
                    sp_Empresa.setSelection(item);
                    tv_Responsable.setText("Responsable: "+Renglon.getString(3));
                    item=0;
                    for (int x=0; x<ItemSPAlm.size();x++){
                        if( ItemSPAlm.get(x).getTexto().equals(Renglon.getString(4)+" - "+Renglon.getString(5))){
                            item=x;
                            break;
                        }
                    }
                    sp_Almacen.setSelection(item);
                    item=0;
                    for (int x=0; x<ItemSPApli.size();x++){
                        if( ItemSPApli.get(x).getTexto().equals(Renglon.getString(6))){
                            item=x;
                            break;
                        }
                    }
                    sp_Aplicacion.setSelection(item);
                    et_Fecha.setText(Renglon.getString(7));

                    Cargagrid(Id);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Aplicaciones guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
    }

    private void Cargagrid(String Id){
        lv_GridSalidas.setAdapter(null);
        arrayArticulos.clear();

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
                "\tU.v_nombre_uni \n" +
                "from t_Salidas_Det as SD \n" +
                "left join t_Productos as P on ltrim(rtrim(SD.c_codigo_pro))=ltrim(rtrim(P.c_codigo_pro)) \n" +
                "left join t_Unidad as U on U.c_codigo_uni=P.c_codigo_uni \n" +

                "where SD.Id_Salida='"+Id+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    String Salida,cproducto,cantida,bloque,cunida,producto,unidad;
                    Salida=Renglon.getString(0);
                    cproducto=Renglon.getString(1);
                    cantida=Renglon.getString(2);
                    bloque=Renglon.getString(3);
                    cunida=Renglon.getString(4);
                    producto=Renglon.getString(5);
                    unidad=Renglon.getString(6);
                    Tabla=new Itemsalida(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6));
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

}