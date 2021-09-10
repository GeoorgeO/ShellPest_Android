package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Riego extends AppCompatActivity {

    public String Usuario, Perfil, Huerta;

    TextView et_fecha;
    Spinner sp_Blq, sp_Hue,sp_Empresa3;
    EditText txt_Precipitacion,txt_CaudalIni,txt_CaudalFin,txt_Riego;
    ListView lv_GridRiego;

    private AdaptadorSpinner CopiHue,CopiBlq,CopiEmp;
    private ArrayList<ItemDatoSpinner> ItemSPHue,ItemSPBlq,ItemSPEmp;

    ItemRiego Tabla;
    Adaptador_GridRiego Adapter;
    ArrayList<ItemRiego> arrayArticulos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riego);

        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        et_fecha = (TextView) findViewById(R.id.et_fecha);
        sp_Hue = (Spinner) findViewById(R.id.sp_Hue);
        sp_Blq = (Spinner) findViewById(R.id.sp_Blo);
        sp_Empresa3 = (Spinner) findViewById(R.id.sp_Empresa3);

        txt_Precipitacion=(EditText) findViewById(R.id.txt_Precipitacion);
        txt_CaudalIni=(EditText) findViewById(R.id.txt_CaudalIni);
        txt_CaudalFin=(EditText) findViewById(R.id.txt_CaudalFin);
        txt_Riego=(EditText) findViewById(R.id.txt_Riego);
        lv_GridRiego=(ListView) findViewById(R.id.lv_GridRiego);


        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = objDate;
        et_fecha.setText("Fecha: " + objSDF.format(date1));

        cargaSpinnerEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_Empresa3.setAdapter(CopiEmp);

        if (sp_Empresa3.getCount()==2){
            sp_Empresa3.setSelection(1);
        }

        cargaSpinnerHue();
        CopiHue = new AdaptadorSpinner(Riego.this, ItemSPHue);
        // CopiHue=AdaptadorSpiner;
        sp_Hue.setAdapter(CopiHue);

        ItemSPBlq = new ArrayList<>();
        ItemSPBlq.add(new ItemDatoSpinner("Bloque"));
        CopiBlq = new AdaptadorSpinner(Riego.this, ItemSPBlq);
        sp_Blq.setAdapter(CopiBlq);

        if (sp_Hue.getCount()==2){
            sp_Hue.setSelection(1);
        }

        arrayArticulos = new ArrayList<>();

        sp_Hue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    // Notify the selected item text
                    Huerta = CopiHue.getItem(i).getTexto().substring(0, 5);

                    cargaSpinnerBlq();
                    CopiBlq = new AdaptadorSpinner(getApplicationContext(), ItemSPBlq);

                    sp_Blq.setAdapter(CopiBlq);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv_GridRiego.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Riego.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar los datos de riego del bloque: "+arrayArticulos.get(i).getNombre_Bloque()+". ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();
                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        String currentTime = simpleDateFormat.format(new Date());

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Riego.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                        //BD.beginTransaction();
                        Cursor Renglon =BD.rawQuery("select Fecha ,Hora,Id_Bloque,Precipitacion_Sistema,Caudal_Inicio,Caudal_Fin,Horas_Riego,Id_Usuario,c_codigo_eps from t_Riego where Fecha='"+objSDF.format(date1)+"' and Id_Bloque='"+arrayArticulos.get(i).getId_Bloque()+"' and c_codigo_eps='"+arrayArticulos.get(i).getCEPS()+"'  ",null);

                        if (Renglon.moveToFirst()) {

                            ContentValues registro= new ContentValues();
                            do {
                                registro.put("Fecha",Renglon.getString(0));
                                registro.put("Hora",currentTime);
                                registro.put("Id_Bloque",Renglon.getString(2));
                                registro.put("Precipitacion_Sistema",Renglon.getString(3));
                                registro.put("Caudal_Inicio",Renglon.getString(4));
                                registro.put("Caudal_Fin",Renglon.getString(5));
                                registro.put("Horas_Riego",Renglon.getString(6));
                                registro.put("Id_Usuario",Renglon.getString(7));
                                registro.put("c_codigo_eps",Renglon.getString(8));
                                BD.insert("t_RiegoEliminado",null,registro);

                                int cantidad= BD.delete("t_Riego","Fecha='"+objSDF.format(date1)+"' and Id_Bloque='"+arrayArticulos.get(i).getId_Bloque()+"' ",null);

                                if(cantidad>0){

                                }else{
                                    Toast.makeText(Riego.this,"Ocurrio un error al intentar eliminar riego del bloque, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                }
                            } while (Renglon.moveToNext());


                        } else {
                            //Toast.makeText(Puntos_Capturados.this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                        }
                        BD.close();
                        Cargagrid();
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

        Cargagrid();

    }

    private void cargaSpinnerEmpresa(){
        CopiEmp=null;

        ItemSPEmp=new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa"));

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        Renglon=BD.rawQuery("select E.c_codigo_eps,E.v_nombre_eps from conempresa as E inner join t_Usuario_Empresa as UE on UE.c_codigo_eps=E.c_codigo_eps where UE.Id_Usuario='"+Usuario+"' ",null);

        if(Renglon.moveToFirst()){

            do {
                ItemSPEmp.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    private void cargaSpinnerHue(){
        CopiHue=null;

        ItemSPHue=new ArrayList<>();

        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"' ",null);
        }else{
            Renglon=BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and Id_Huerta='"+Huerta+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
            sp_Hue.setEnabled(false);
        }

        if(Renglon.moveToFirst()){
            do {
                ItemSPHue.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" "+Renglon.getString(2)));
            } while(Renglon.moveToNext());
        }else{
            Toast.makeText(this,"No se encontraron datos en huertas",Toast.LENGTH_SHORT).show();
            BD.close();
        }
        BD.close();
    }

    private void cargaSpinnerBlq(){

        if(Huerta.length()>0 && !Huerta.equals("NULL")){
            CopiBlq=null;

            ItemSPBlq=new ArrayList<>();

            AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD=SQLAdmin.getReadableDatabase();
            Cursor Renglon;

            ItemSPBlq.add(new ItemDatoSpinner("Bloque"));

            Renglon=BD.rawQuery("select B.Id_Bloque,B.Nombre_Bloque from t_Bloque as B  where B.Id_Huerta='"+Huerta+"' and B.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

            if(Renglon.moveToFirst()){
                do {
                    ItemSPBlq.add(new ItemDatoSpinner(Renglon.getString(0)+" -    "+Renglon.getString(1)));
                } while(Renglon.moveToNext());
            }else{
                Toast.makeText(this,"No se encontraron datos en Bloques",Toast.LENGTH_SHORT).show();
                BD.close();
            }
            BD.close();
        }else{

        }
    }

    private void LimpiarCampos(){
        txt_Precipitacion.setText("0");
        txt_CaudalIni.setText("0");
        txt_CaudalFin.setText("0");
        txt_Riego.setText("0");
    }

    public void AddRenglon(View view){
        if(sp_Blq.getSelectedItemPosition()>0){
            boolean FaltoAlgo;
            FaltoAlgo=false;
            if(Double.parseDouble(String.valueOf(txt_Precipitacion.getText()))>=0){
                if(Double.parseDouble(String.valueOf(txt_CaudalIni.getText()))>0){
                    if(Double.parseDouble(String.valueOf(txt_CaudalFin.getText()))>0){
                        if(Double.parseDouble(String.valueOf(txt_Riego.getText()))>0){
                            FaltoAlgo=false;
                        }else{
                            FaltoAlgo=true;
                            Toast.makeText(this,"Falta agregar horas de riego.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        FaltoAlgo=true;
                        Toast.makeText(this,"Falta agregar el caudal Final",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    FaltoAlgo=true;
                    Toast.makeText(this,"Falta agregar el caudal Inicial",Toast.LENGTH_SHORT).show();
                }

            }else{
                FaltoAlgo=true;
                Toast.makeText(this,"Falta agregar la precipitación",Toast.LENGTH_SHORT).show();
            }
            if (!FaltoAlgo){

                AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
                SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

                Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                Date date1=objDate;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String currentTime = simpleDateFormat.format(new Date());

                Cursor Renglon;

                Renglon =BD.rawQuery("select count(Id_Bloque) " +
                            "from t_Riego " +
                            "where Fecha='"+objSDF.format(date1)+"' and Id_Bloque='"+CopiBlq.getItem(sp_Blq.getSelectedItemPosition()).getTexto().substring(0,4)+"' and c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

                if(Renglon.moveToFirst()){
                    if(Renglon.getInt(0)>0){
                        Toast.makeText(this,"Ya existen datos capturados en ese bloque, deseas sobreescribirlos?",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Riego.this);
                        dialogo1.setTitle("Dato ya ingresado");
                        dialogo1.setMessage("¿ Ya existen datos capturados en ese bloque, deseas sobreescribirlos ?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //aceptar();

                                    ContentValues registro2= new ContentValues();
                                    registro2.put("Fecha",objSDF.format(date1));
                                    registro2.put("Hora",currentTime);
                                    registro2.put("Id_Bloque",CopiBlq.getItem(sp_Blq.getSelectedItemPosition()).getTexto().substring(0,4));

                                    registro2.put("Precipitacion_Sistema",Double.parseDouble(String.valueOf(txt_Precipitacion.getText())));
                                    registro2.put("Caudal_Inicio",Double.parseDouble(String.valueOf(txt_CaudalIni.getText())));
                                    registro2.put("Caudal_Fin",Double.parseDouble(String.valueOf(txt_CaudalFin.getText())));
                                    registro2.put("Horas_Riego",Double.parseDouble(String.valueOf(txt_Riego.getText())));
                                    registro2.put("Id_Usuario",Usuario);
                                    registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2));
                                    BD.insert("t_Riego",null,registro2);

                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //cancelar();
                            }
                        });
                        dialogo1.show();

                    }
                    else{
                        ContentValues registro2= new ContentValues();
                        registro2.put("Fecha",objSDF.format(date1));
                        registro2.put("Hora",currentTime);
                        registro2.put("Id_Bloque",CopiBlq.getItem(sp_Blq.getSelectedItemPosition()).getTexto().substring(0,4));

                        registro2.put("Precipitacion_Sistema",Double.parseDouble(String.valueOf(txt_Precipitacion.getText())));
                        registro2.put("Caudal_Inicio",Double.parseDouble(String.valueOf(txt_CaudalIni.getText())));
                        registro2.put("Caudal_Fin",Double.parseDouble(String.valueOf(txt_CaudalFin.getText())));
                        registro2.put("Horas_Riego",Double.parseDouble(String.valueOf(txt_Riego.getText())));
                        registro2.put("Id_Usuario",Usuario);
                        registro2.put("c_codigo_eps",CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2));
                        BD.insert("t_Riego",null,registro2);
                    }

                }else{
                    Toast.makeText(this,"No Regreso nada la consulta de Riego",Toast.LENGTH_SHORT).show();
                }



                BD.close();



                Cargagrid();
            }

        }else{
            Toast.makeText(Riego.this,"Falta seleccionar un punto de control",Toast.LENGTH_SHORT).show();
        }
    }

    public void cerrar(View view ){
        this.onBackPressed();
    }

    private void Cargagrid(){
        lv_GridRiego.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        // Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select R.Fecha, \n" +
                "\tR.Hora,\n" +
                "\tB.Nombre_Bloque, \n" +
                "\tR.Id_Bloque, \n" +
                "\tR.Precipitacion_Sistema, \n" +
                "\tR.Caudal_Inicio, \n" +
                "\tR.Caudal_Fin, \n" +
                "\tR.Horas_Riego, R.c_codigo_eps \n" +
                "from t_Riego as R \n" +
                "left join t_Bloque as B on B.Id_Bloque=R.Id_Bloque and B.c_codigo_eps=R.c_codigo_eps \n" +
                "where R.Fecha='"+objSDF.format(date1)+"' and R.c_codigo_eps='"+CopiEmp.getItem(sp_Empresa3.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemRiego(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridRiego(getApplicationContext(),arrayArticulos);
            lv_GridRiego.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

}