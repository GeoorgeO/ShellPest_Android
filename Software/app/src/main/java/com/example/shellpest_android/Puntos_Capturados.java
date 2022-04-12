package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Puntos_Capturados extends AppCompatActivity {
String Usuario,Perfil,Huerta;

    ListView lv_Puntos;
    ItemPuntosControl Tabla;
    Adaptador_GridPuntosControl Adapter;
    ArrayList<ItemPuntosControl> arrayArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_capturados);

        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario");
        Perfil = getIntent().getStringExtra("perfil");
        Huerta = getIntent().getStringExtra("huerta");

        lv_Puntos = (ListView) findViewById(R.id.lv_Puntos);

        arrayArticulos = new ArrayList<>();


        Cargagrid();  

        lv_Puntos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Puntos_Capturados.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar el registro seleccionado ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();

                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Puntos_Capturados.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                        //BD.beginTransaction();
                        Cursor Renglon =BD.rawQuery("select Fecha,Id_Huerta,Id_PuntoControl,Id_Usuario,n_coordenadaX,n_coordenadaY,Hora,c_codigo_eps from t_Monitoreo_PEEncabezado where Fecha='"+objSDF.format(date1)+"' and Id_PuntoControl='"+arrayArticulos.get(i).getcPto()+"' and c_codigo_eps='"+arrayArticulos.get(i).getcEPS()+"' ",null);

                            if (Renglon.moveToFirst()) {

                                ContentValues registro= new ContentValues();
                                do {
                                    registro.put("Fecha",Renglon.getString(0));
                                    registro.put("Id_Huerta",Renglon.getString(1));
                                    registro.put("Id_PuntoControl",Renglon.getString(2));
                                    registro.put("Id_Usuario",Renglon.getString(3));
                                    registro.put("n_coordenadaX",Renglon.getString(4));
                                    registro.put("n_coordenadaY",Renglon.getString(5));
                                    registro.put("Hora",Renglon.getString(6));
                                    registro.put("c_codigo_eps",Renglon.getString(7));
                                    BD.insert("t_Monitoreo_Eliminados_PEEncabezado",null,registro);

                                    int cantidad= BD.delete("t_Monitoreo_PEEncabezado","Id_PuntoControl='"+arrayArticulos.get(i).getcPto()+"' and Fecha='"+objSDF.format(date1)+"' and c_codigo_eps='"+arrayArticulos.get(i).getcEPS()+"'",null);

                                    if(cantidad>0){

                                    }else{
                                        Toast.makeText(Puntos_Capturados.this,"Ocurrio un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                    }
                                } while (Renglon.moveToNext());
                            } else {
                                //Toast.makeText(Puntos_Capturados.this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                            }

                        Cursor Renglon2 =BD.rawQuery("select Fecha,Id_Plagas,Id_Enfermedad,Id_PuntoControl,Id_Deteccion,Id_Individuo,Id_Humbral,Hora,c_codigo_eps,Cant_Individuos,Id_Fenologico from t_Monitoreo_PEDetalle where Fecha='"+objSDF.format(date1)+"' and Id_PuntoControl='"+arrayArticulos.get(i).getcPto()+"' and c_codigo_eps='"+arrayArticulos.get(i).getcEPS()+"'",null);

                        if (Renglon2.moveToFirst()) {

                            ContentValues registro2= new ContentValues();
                            do {

                                registro2.put("Fecha",Renglon2.getString(0));
                                registro2.put("Id_Plagas",Renglon2.getString(1));
                                registro2.put("Id_Enfermedad",Renglon2.getString(2));
                                registro2.put("Id_PuntoControl",Renglon2.getString(3));
                                registro2.put("Id_Deteccion",Renglon2.getString(4));
                                registro2.put("Id_Individuo",Renglon2.getString(5));
                                registro2.put("Id_Humbral",Renglon2.getString(6));
                                String tHora=Renglon2.getString(7);
                                registro2.put("Hora",Renglon2.getString(7));
                                registro2.put("c_codigo_eps",Renglon2.getString(8));
                                registro2.put("Cant_Individuos",Renglon2.getString(9));
                                registro2.put("Id_Fenologico",Renglon2.getString(10));
                                BD.insert("t_Monitoreo_Eliminados_PEDetalle",null,registro2);

                                int cantidad= BD.delete("t_Monitoreo_PEDetalle","Id_PuntoControl='"+arrayArticulos.get(i).getcPto()+"' and Fecha='"+objSDF.format(date1)+"' ",null);

                                if(cantidad>0){

                                }else{
                                    //Toast.makeText(Puntos_Capturados.this,"Ocurrio un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                }
                            } while (Renglon2.moveToNext());
                        } else {
                            //Toast.makeText(Puntos_Capturados.this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                        }

                        //BD.endTransaction();
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

    }

    private void Cargagrid(){
        lv_Puntos.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        //Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select P.Nombre_PuntoControl, \n" +
                "\tH.Nombre_Huerta,\n" +
                "\t count(M.Id_Individuo), \n" +
                "\t ME.Id_PuntoControl,Me.c_codigo_eps,EPS.v_nombre_eps \n" +
                "from t_Monitoreo_PEEncabezado as ME \n" +
                "left join  t_Monitoreo_PEDetalle as M on ME.Id_PuntoControl=M.Id_PuntoControl and M.Fecha=ME.Fecha and M.c_codigo_eps=ME.c_codigo_eps \n "+
                "left join t_Puntocontrol as P on ME.Id_PuntoControl=P.Id_PuntoControl and ME.c_codigo_eps=P.c_codigo_eps \n" +
                "left join t_Bloque as B on P.Id_Bloque=B.Id_Bloque and B.c_codigo_eps=P.c_codigo_eps \n" +
                "left join t_Huerta as H on B.Id_Huerta=H.Id_Huerta and H.c_codigo_eps=B.c_codigo_eps \n" +
                "left join conempresa as EPS on EPS.c_codigo_eps=ME.c_codigo_eps\n" +
                " group by ME.c_codigo_eps,EPS.v_nombre_eps,ME.Fecha,H.Nombre_Huerta,ME.Id_PuntoControl,P.Nombre_PuntoControl ",null);
//where ME.Fecha='"+objSDF.format(date1)+"'
        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemPuntosControl(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridPuntosControl(getApplicationContext(),arrayArticulos);
            lv_Puntos.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }
    public void Regresar(View view){

            this.onBackPressed();

    }

}